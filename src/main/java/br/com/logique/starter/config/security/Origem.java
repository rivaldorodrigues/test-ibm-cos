package br.com.logique.starter.config.security;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Origem {

    WEB("jwt.token.expiration.web"),
    APP("jwt.token.expiration.app");

    private String chaveTempoExpiracao;

    Origem(String chaveTempoExpiracao) {
        this.chaveTempoExpiracao = chaveTempoExpiracao;
    }

    public static Optional<Origem> getEnum(String origem) {
        return Arrays.stream(values())
                .filter(o -> o.name().equals(origem))
                .findFirst();
    }
}
