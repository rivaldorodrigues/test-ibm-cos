package br.com.logique.starter.repository.specification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ordenacao {
    private String campo;
    private boolean descendente;
}
