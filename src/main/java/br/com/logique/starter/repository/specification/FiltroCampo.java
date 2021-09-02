package br.com.logique.starter.repository.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FiltroCampo implements Serializable {

    private String campo;
    private Object valor;
    private String operador;

    private boolean isOrPredicate = false;

    public FiltroCampo(String campo, String operador, Object valor) {
        this.campo = campo;
        this.operador = operador;
        this.valor = valor;
    }

    public FiltroCampo(String campo, String operador, Object valor, boolean isOrPredicate) {
        this.campo = campo;
        this.valor = valor;
        this.operador = operador;
        this.isOrPredicate = isOrPredicate;
    }
}
