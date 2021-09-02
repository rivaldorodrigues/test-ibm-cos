package br.com.logique.starter.config.security.jwt;

import br.com.logique.starter.service.DetalhesUsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String ORIGEM_HEADER = "X-Origem";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtTokenProvider jwtTokenProvider;
    private DetalhesUsuarioService detalhesUsuarioService;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider,
                          DetalhesUsuarioService detalhesUsuarioService) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.detalhesUsuarioService = detalhesUsuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenAtual = obterHeaderToken(request);

        if (StringUtils.hasText(tokenAtual)) {

            TokenStatus tokenStatus = jwtTokenProvider.validateToken(tokenAtual);

            switch (tokenStatus) {
                case VALIDO:
                    setAuthentication(tokenAtual, request);
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer " + tokenAtual);
                    break;
                case REFRESH:
                    Authentication auth = setAuthentication(tokenAtual, request);
                    String novoToken = jwtTokenProvider.createToken(auth, obterHeaderOrigem(request));
                    response.setHeader(AUTHORIZATION_HEADER, "Bearer " + novoToken);
                    break;
                case EXPIRADO:
                case INVALIDO:
                    SecurityContextHolder.getContext().setAuthentication(null);
                    break;
                default:
                    break;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication setAuthentication(String token, HttpServletRequest request) {

        String loginUsuario = jwtTokenProvider.getLoginUsuario(token);
        UserDetails detalhesUsuario = detalhesUsuarioService.loadUserByUsername(loginUsuario);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(detalhesUsuario, null, detalhesUsuario.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private String obterHeaderToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.replace("Bearer ", "");
        }

        return null;
    }

    private String obterHeaderOrigem(HttpServletRequest request) {
        return request.getHeader(ORIGEM_HEADER);
    }
}
