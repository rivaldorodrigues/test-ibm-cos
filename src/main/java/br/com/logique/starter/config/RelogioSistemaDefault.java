package br.com.logique.starter.config;

import br.com.logique.starter.util.DataUtil;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RelogioSistemaDefault implements RelogioSistema {

    private Clock clock = Clock.systemDefaultZone();

    @Override
    public Date dateAtual() {
        return DataUtil.toDate(localDateTimeAtual());
    }

    @Override
    public LocalDateTime localDateTimeAtual() {
        return LocalDateTime.now(clock);
    }
}
