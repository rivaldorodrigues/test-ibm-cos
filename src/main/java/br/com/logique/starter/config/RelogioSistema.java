package br.com.logique.starter.config;

import java.time.LocalDateTime;
import java.util.Date;

public interface RelogioSistema {
    Date dateAtual();
    LocalDateTime localDateTimeAtual();
}
