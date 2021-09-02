package br.com.logique.starter.controller.v1;

import br.com.logique.starter.model.Usuario;
import br.com.logique.starter.model.exception.ValidationException;
import br.com.logique.starter.repository.usuario.EspecificacaoUsuario;
import br.com.logique.starter.repository.usuario.UsuarioRepository;
import br.com.logique.starter.service.MessageService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "v1/usuario")
public class UsuarioController {

    private MessageService message;
    private PasswordEncoder passwordEncoder;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(MessageService message,
                             UsuarioRepository usuarioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.message = message;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/listar")
    public Page<Usuario> listar(@RequestBody EspecificacaoUsuario especificacao) {
        Pageable pageable = especificacao.toPageable();
        Specification<Usuario> specification = especificacao.toSpecification();
        return usuarioRepository.findAll(specification, pageable);
    }

    @GetMapping(value = "/{id}")
    public Usuario buscar(@PathVariable("id") Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Usuario salvar(@RequestBody @Valid Usuario novoUsuario) {

        Preconditions.checkNotNull(novoUsuario);

        validarUsuario(novoUsuario);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());

        Usuario usuario = Usuario.builder()
                .id(novoUsuario.getId())
                .nome(novoUsuario.getNome())
                .email(novoUsuario.getEmail())
                .login(novoUsuario.getLogin().toLowerCase())
                .senha(senhaCriptografada)
                .perfis(novoUsuario.getPerfis())
                .build();

        return usuarioRepository.save(usuario);
    }

    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public Usuario atualizar(@PathVariable("id") Long id, @RequestBody Usuario usuario) {

        Preconditions.checkNotNull(usuario);
        Usuario usuarioBanco = obterUsuarioPorId(id);

        usuario.setId(id);
        validarUsuario(usuario);

        if (StringUtils.isNotBlank(usuario.getSenha())) {
            String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
        } else {
            usuario.setSenha(usuarioBanco.getSenha());
        }

        usuario.setLogin(usuario.getLogin().toLowerCase());
        return usuarioRepository.save(usuario);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public void remover(@PathVariable("id") Long id) {
        obterUsuarioPorId(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario obterUsuarioPorId(Long id) {
        Optional<Usuario> usuarioBanco = usuarioRepository.findById(id);

        if (!usuarioBanco.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message.get("erro.usuario.inexistente"));
        }

        return usuarioBanco.get();
    }

    private void validarUsuario(Usuario usuario) {

        ValidationException.Builder builder = new ValidationException.Builder();

        Optional<Usuario> usuarioBanco = usuarioRepository.findByLogin(usuario.getLogin());

        if (usuarioBanco.isPresent() && !usuarioBanco.get().getId().equals(usuario.getId())) {
            builder.addValidationError("login", message.get("erro.login.em-uso"));
        }

        usuarioBanco = usuarioRepository.findByEmail(usuario.getEmail());

        if (usuarioBanco.isPresent() && !usuarioBanco.get().getId().equals(usuario.getId())) {
            builder.addValidationError("email", message.get("erro.email.em-uso"));
        }

        if (builder.comErro()) {
            throw builder.build();
        }
    }

}

