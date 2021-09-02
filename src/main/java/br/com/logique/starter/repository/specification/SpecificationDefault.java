package br.com.logique.starter.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class SpecificationDefault<T> implements Specification<T> {

    private FiltroCampo filtro;

    public SpecificationDefault(FiltroCampo filtro) {
        this.filtro = filtro;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        Optional<OperadorFiltro> operador = OperadorFiltro.getEnum(filtro.getOperador());

        if (operador.isPresent()) {
            return operador.get().toPredicate(root, filtro, builder);
        }

        return null;
    }
}
