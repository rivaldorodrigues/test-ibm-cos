package br.com.logique.starter.service;

import br.com.logique.starter.config.security.DetalhesUsuario;
import br.com.logique.starter.model.Usuario;
import br.com.logique.starter.repository.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class DetalhesUsuarioService implements UserDetailsService {

    private final MessageService message;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public DetalhesUsuarioService(UsuarioRepository usuarioRepository, MessageService message) {
        this.message = message;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {

        Optional<Usuario> usuarioPorLogin = usuarioRepository.findByLogin(login);

        return usuarioPorLogin
                .map(DetalhesUsuario::fromUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(message.get("erro.usuario.nao-autorizado")));

    }

}
