package br.com.logique.starter.model.dto.usuario;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
public class Credenciais implements Serializable {

    @NotNull
    private String login;

    @NotNull
    private String senha;
}

