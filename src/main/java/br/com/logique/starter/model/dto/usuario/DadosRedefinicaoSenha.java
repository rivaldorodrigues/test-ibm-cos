package br.com.logique.starter.model.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DadosRedefinicaoSenha implements Serializable {

    @NotNull
    private String login;

    @NotNull
    private String codigo;

    @NotNull
    @Size(min = 6, max = 60)
    private String senha;

    @NotNull
    @Size(min = 6, max = 60)
    private String confirmacaoSenha;
}



