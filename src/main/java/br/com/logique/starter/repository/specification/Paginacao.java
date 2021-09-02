package br.com.logique.starter.repository.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paginacao {

    private int paginaAtual;
    private int linhasPorPagina;

    public int getLinhasPorPagina() {
        return (linhasPorPagina > 0) ? linhasPorPagina : 10;
    }
}
