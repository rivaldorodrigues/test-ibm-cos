package br.com.logique.starter.config.security.jwt;

import br.com.logique.starter.config.RelogioSistema;
import br.com.logique.starter.util.DataUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private RelogioSistema relogio;

    private int intervaloRefrash = 15;
    private JwtTokenProvider tokenProvider;

    private Date dataCriacaoToken = DataUtil.toDate(LocalDateTime.of(2020, 1, 1, 0, 0));
    private Date dataExpiracaoToken = DataUtil.toDate(LocalDateTime.of(2020, 1, 1, 1, 0));

    @BeforeEach
    public void setUp() {
        tokenProvider = new JwtTokenProvider(null, relogio, "Teste", 15, 60);
    }

    @Test
    public void tokenValido10Min() {

        LocalDateTime dataHoraCorrente = LocalDateTime.of(2020, 1, 1, 0, 10);

        when(relogio.dateAtual()).thenReturn(DataUtil.toDate(dataHoraCorrente));

        TokenStatus resposta = tokenProvider.identificarStatusPorTempo(dataCriacaoToken, dataExpiracaoToken, intervaloRefrash);
        Assertions.assertEquals(TokenStatus.VALIDO, resposta);
    }

    @Test
    public void tokenRefresh20Min() {

        LocalDateTime dataHoraCorrente = LocalDateTime.of(2020, 1, 1, 0, 20);

        when(relogio.dateAtual()).thenReturn(DataUtil.toDate(dataHoraCorrente));

        TokenStatus resposta = tokenProvider.identificarStatusPorTempo(dataCriacaoToken, dataExpiracaoToken, intervaloRefrash);
        Assertions.assertEquals(TokenStatus.REFRESH, resposta);
    }

    @Test
    public void tokenRefresh40Min() {

        LocalDateTime dataHoraCorrente = LocalDateTime.of(2020, 1, 1, 0, 40);

        when(relogio.dateAtual()).thenReturn(DataUtil.toDate(dataHoraCorrente));

        TokenStatus resposta = tokenProvider.identificarStatusPorTempo(dataCriacaoToken, dataExpiracaoToken, intervaloRefrash);
        Assertions.assertEquals(TokenStatus.REFRESH, resposta);
    }

    @Test
    public void tokenExpirado80Min() {

        LocalDateTime dataHoraCorrente = LocalDateTime.of(2020, 1, 1, 1, 20);

        when(relogio.dateAtual()).thenReturn(DataUtil.toDate(dataHoraCorrente));

        TokenStatus resposta = tokenProvider.identificarStatusPorTempo(dataCriacaoToken, dataExpiracaoToken, intervaloRefrash);
        Assertions.assertEquals(TokenStatus.EXPIRADO, resposta);
    }
}
