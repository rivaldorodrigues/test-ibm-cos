package br.com.logique.starter.repository.specification;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Especificação padrão
 * Deve ser estendida para cada classe de domínio que necessita de filtro server side.
 *
 * @author Rivaldo
 */
@Data
public abstract class Especificacao<T> {

    private Filtro filtro;
    private Paginacao paginacao;
    private Ordenacao ordenacao;

    protected abstract Sort ordenacaoPadrao();

    protected abstract List<FiltroCampo> filtroGlobalPadrao(String valorFiltroGlobal);

    /**
     * Cria um objeto Specification para ser usado em repositórios do spring
     * @return uma nova instância de Specification com os dados aqui contidos
     */
    public Specification<T> toSpecification() {
        SpecificationsBuilder<T> builder = new SpecificationsBuilder<>();
        adicionarFiltroGlobal(builder);
        adicionarFiltrosIndividuais(builder);
        return builder.build();
    }

    /**
     * Cria um objeto Pageable para ser usado em repositórios do spring
     * @return uma nova instância de Pageable com os dados aqui contidos
     */
    public Pageable toPageable() {

        Optional<Sort> sort = obterSort();
        Pageable pageable = null;

        if (paginacao == null) {
            paginacao = new Paginacao(0, 10);
        }

        if (sort.isPresent()) {
            pageable = PageRequest.of(paginacao.getPaginaAtual(), paginacao.getLinhasPorPagina(), sort.get());
        } else {
            pageable = PageRequest.of(paginacao.getPaginaAtual(), paginacao.getLinhasPorPagina());
        }

        return pageable;
    }

    private void adicionarFiltroGlobal(SpecificationsBuilder<T> builder) {

        if (filtro != null && StringUtils.hasText(filtro.getGlobal())) {

            String consultaGlobal = this.filtro.getGlobal();
            List<FiltroCampo> campos = filtroGlobalPadrao(consultaGlobal);

            if (CollectionUtils.isNotEmpty(campos)) {
                campos.forEach(builder::add);
            }
        }
    }

    private void adicionarFiltrosIndividuais(SpecificationsBuilder<T> builder) {
        if (filtro != null && CollectionUtils.isNotEmpty(filtro.getIndividuais())) {
            filtro.getIndividuais().forEach(builder::add);
        }
    }

    private Optional<Sort> obterSort() {

        Sort sort = ordenacaoPadrao();

        if (ordenacao != null && StringUtils.hasText(ordenacao.getCampo())) {
            sort = Sort.by(ordenacao.getCampo());
            sort = (ordenacao.isDescendente()) ? sort.descending() : sort.ascending();
        }

        return Optional.ofNullable(sort);
    }
}
