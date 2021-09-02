package br.com.logique.starter.repository.usuario;

import br.com.logique.starter.model.Usuario;
import br.com.logique.starter.repository.specification.Especificacao;
import br.com.logique.starter.repository.specification.FiltroCampo;
import br.com.logique.starter.repository.specification.OperadorFiltro;
import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;

import java.util.List;

public class EspecificacaoUsuario extends Especificacao<Usuario> {

    @Override
    protected Sort ordenacaoPadrao() {
        return Sort.by("nome").ascending();
    }

    @Override
    protected List<FiltroCampo> filtroGlobalPadrao(String valorFiltroGlobal) {

        String operadorContem = OperadorFiltro.CONTEM.getOperador();

        return Lists.newArrayList(
                new FiltroCampo("nome", operadorContem, valorFiltroGlobal, true),
                new FiltroCampo("login", operadorContem, valorFiltroGlobal, true),
                new FiltroCampo("email", operadorContem, valorFiltroGlobal, true)
        );
    }
}
