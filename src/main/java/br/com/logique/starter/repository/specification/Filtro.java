package br.com.logique.starter.repository.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Filtro {
    private String global;
    private List<FiltroCampo> individuais;
}
