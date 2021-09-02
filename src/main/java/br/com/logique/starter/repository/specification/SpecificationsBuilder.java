package br.com.logique.starter.repository.specification;

import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class SpecificationsBuilder<T> {

    private final List<FiltroCampo> filtros = Lists.newArrayList();

    public SpecificationsBuilder<T> add(String campo, String operacao, Object valor) {
        filtros.add(new FiltroCampo(campo, operacao, valor));
        return this;
    }

    public SpecificationsBuilder<T> add(FiltroCampo filtroCampo) {
        if (filtroCampo != null) {
            filtros.add(filtroCampo);
        }
        return this;
    }

    public Specification<T> build() {

        if (filtros.isEmpty()) {
            return null;
        }

        List<Specification<T>> specs = filtros.stream()
                .map(SpecificationDefault<T>::new)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < filtros.size(); i++) {
            result = filtros.get(i).isOrPredicate() ?
                    Specification.where(result).or(specs.get(i)) :
                    Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}
