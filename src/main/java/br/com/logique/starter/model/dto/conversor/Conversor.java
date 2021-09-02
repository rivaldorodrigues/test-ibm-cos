package br.com.logique.starter.model.dto.conversor;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Padrão para conversão de dados para dto.
 *
 * @author Rivaldo
 * Created on 25/04/2018.
 */
public interface Conversor<T, K> {

    /**
     * Converte o objeto de origem para o objeto de destino.
     *
     * @param origem o objeto a ser convertido
     * @return uma nova instancia do objeto convertido
     */
    K converter(@NotNull T origem);

    default List<K> converter(@NotNull List<T> origem) {

        if (CollectionUtils.isNotEmpty(origem)) {
            return origem.stream().map(this::converter).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }
}
