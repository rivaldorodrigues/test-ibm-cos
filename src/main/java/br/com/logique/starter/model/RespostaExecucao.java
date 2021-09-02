package br.com.logique.starter.model;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class RespostaExecucao {

    @Getter
    private boolean sucesso;

    @Getter
    private String mensagemErro;

    public RespostaExecucao(boolean sucesso, @Nullable String mensagemErro) {
        this.sucesso = sucesso;
        this.mensagemErro = mensagemErro;
    }

    public static RespostaExecucao ok() {
        return new RespostaExecucao(true, null);
    }

    public static RespostaExecucao erro(String mensagem) {
        return new RespostaExecucao(false, mensagem);
    }

    public boolean possuiErro() {
        return !this.sucesso;
    }

    public <X extends Throwable> void ifErrorThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (possuiErro()) {
            throw exceptionSupplier.get();
        }
    }
}


