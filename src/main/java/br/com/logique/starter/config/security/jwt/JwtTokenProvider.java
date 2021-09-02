package br.com.logique.starter.config.security.jwt;

import br.com.logique.starter.config.RelogioSistema;
import br.com.logique.starter.config.security.Origem;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provider responsável pelas operações relacionadas aos tokens JWT
 */
@Slf4j
@Service
public class JwtTokenProvider {

    private static final int MILISSEGUNDOS_POR_MINUTO = 60000;
    private static final String AUTHORITIES_KEY = "auth";

    private String chaveSecreta;
    private Environment environment;
    private RelogioSistema relogioSistema;

    private Integer intervaloRefresh;
    private Integer intervaloExpiracaoDefault;

    @Autowired
    public JwtTokenProvider(Environment environment,
                            RelogioSistema relogioSistema,
                            @Value("${jwt.token.secret}") String chaveSecreta,
                            @Value("${jwt.token.refresh:15}") Integer intervaloRefresh,
                            @Value("${jwt.token.expiration.default:60}") Integer intervaloExpiracaoDefault) {

        this.environment = environment;
        this.chaveSecreta = chaveSecreta;
        this.relogioSistema = relogioSistema;
        this.intervaloRefresh = intervaloRefresh;
        this.intervaloExpiracaoDefault = intervaloExpiracaoDefault;
    }

    @PostConstruct
    protected void init() {
        chaveSecreta = Base64.getEncoder().encodeToString(chaveSecreta.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Authentication authentication, String origem) {

        int tempoExpiracaoMinutos = obterTempoExpiracao(origem);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, chaveSecreta)
                .setIssuedAt(relogioSistema.dateAtual())
                .setExpiration(gerarNovaDataExpiracao(tempoExpiracaoMinutos))
                .compact();
    }

    public String getLoginUsuario(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(chaveSecreta)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public TokenStatus validateToken(String token) {

        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(chaveSecreta).parseClaimsJws(token);

            Date dataCriacaoToken = claimsJws.getBody().getIssuedAt();
            Date dataExpiracaoToken = claimsJws.getBody().getExpiration();

            return identificarStatusPorTempo(dataCriacaoToken, dataExpiracaoToken, intervaloRefresh);
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace:", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace:", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace:", e);
            return TokenStatus.EXPIRADO;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace:", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace:", e);
        }

        return TokenStatus.INVALIDO;
    }

    public int obterTempoExpiracao(String origemHeader) {

        Optional<Origem> origem = Origem.getEnum(origemHeader);

        return origem.map(Origem::getChaveTempoExpiracao)
                .map(key -> environment.getProperty(key))
                .map(Integer::parseInt).orElse(intervaloExpiracaoDefault);
    }

    TokenStatus identificarStatusPorTempo(Date dataCriacaoToken, Date dataExpiracaoToken, int intervaloRefresh) {

        if (dataCriacaoToken == null || dataExpiracaoToken == null) {
            return TokenStatus.INVALIDO;
        }
        
        Long milissegundosAtual = relogioSistema.dateAtual().getTime();
        Long milissegundosRefresh = obterDataRefresh(dataCriacaoToken, intervaloRefresh).getTime();
        Long milissegundosExpiracao = dataExpiracaoToken.getTime();

        if (milissegundosAtual > milissegundosExpiracao) {
            return TokenStatus.EXPIRADO;
        } else if (milissegundosAtual > milissegundosRefresh) {
            return TokenStatus.REFRESH;
        } else {
            return TokenStatus.VALIDO;
        }
    }

    private Date gerarNovaDataExpiracao(int tempoExpiracaoMinutos) {
        Date dataAtual = relogioSistema.dateAtual();
        return new Date(dataAtual.getTime() + tempoExpiracaoMinutos * MILISSEGUNDOS_POR_MINUTO);
    }

    private Date obterDataRefresh(Date dataCriacaoToken, int intervaloRefresh) {
        return new Date(dataCriacaoToken.getTime() + intervaloRefresh * MILISSEGUNDOS_POR_MINUTO);
    }
}
