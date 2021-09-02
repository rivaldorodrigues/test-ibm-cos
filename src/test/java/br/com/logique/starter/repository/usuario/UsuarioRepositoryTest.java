package br.com.logique.starter.repository.usuario;

import br.com.logique.starter.RepositoryContext;
import br.com.logique.starter.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("usuario-teste.sql")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepositoryContext.class})
public class UsuarioRepositoryTest {

    private Usuario usuario;

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Test
    public void TestandoAcessoAoBanco() {

        definindoValoresIniciais();

        Optional<Usuario> user = usuarioRepositorio.findById(usuario.getId());

        long idUsuario = user.map(Usuario::getId).orElse(-1L);

        assertEquals(1L, idUsuario);
    }

    private void definindoValoresIniciais() {
        usuario = Usuario.builder()
                .id(1L)
                .build();
    }

}
