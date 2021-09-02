package br.com.logique.starter.repository.specification;

import lombok.Getter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Optional;

/**
 * Representa os tipos de operações possíveis nas especificações de consultas
 *
 * @author Rivaldo
 */
@Getter
public enum OperadorFiltro {

    CONTEM(":") {
        @Override
        <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder) {
            if (root.get(filtro.getCampo()).getJavaType() == String.class) {
                return builder.like(root.<String>get(filtro.getCampo()), "%" + filtro.getValor() + "%");
            } else {
                return builder.equal(root.get(filtro.getCampo()), filtro.getValor());
            }
        }
    },
    MAIOR(">") {
        @Override
        <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder) {
            return builder.greaterThan(root.<String> get(filtro.getCampo()), filtro.getValor().toString());
        }
    },
    MAIOR_E_IGUAL(">=") {
        @Override
        <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder) {
            return builder.greaterThanOrEqualTo(root.<String> get(filtro.getCampo()), filtro.getValor().toString());
        }
    },
    MENOR("<") {
        @Override
        <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder) {
            return builder.lessThan(root.<String> get(filtro.getCampo()), filtro.getValor().toString());
        }
    },
    MENOR_E_IGUAL("<=") {
        @Override
        <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder) {
            return builder.lessThanOrEqualTo(root.<String> get(filtro.getCampo()), filtro.getValor().toString());
        }
    };

    private String operador;

    OperadorFiltro(String operador) {
        this.operador = operador;
    }

    public static Optional<OperadorFiltro> getEnum(String origem) {
        return Arrays.stream(values())
                .filter(o -> o.name().equalsIgnoreCase(origem) || o.operador.equals(origem))
                .findFirst();
    }

    /**
     * Transforma o operador em um predicado a ser utilizado para consultas com specification no spring.
     */
    abstract <T> Predicate toPredicate(Root<T> root, FiltroCampo filtro, CriteriaBuilder builder);
}
