package br.com.logique.starter.service;

import br.com.logique.starter.config.RelogioSistema;
import br.com.logique.starter.config.security.DetalhesUsuario;
import br.com.logique.starter.config.security.jwt.JwtTokenProvider;
import br.com.logique.starter.model.RespostaExecucao;
import br.com.logique.starter.model.Usuario;
import br.com.logique.starter.model.dto.usuario.DadosRedefinicaoSenha;
import br.com.logique.starter.model.dto.usuario.RespostaLogin;
import br.com.logique.starter.model.dto.usuario.CodigoRedefinicaoSenha;
import br.com.logique.starter.repository.usuario.UsuarioRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class AuthenticationService {

    private EmailService email;
    private MessageService message;
    private RelogioSistema relogioSistema;
    private JwtTokenProvider tokenProvider;
    private UsuarioRepository usuarioRepository;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(EmailService email,
                                 MessageService message,
                                 RelogioSistema relogioSistema,
                                 JwtTokenProvider tokenProvider,
                                 UsuarioRepository usuarioRepository,
                                 AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder) {
        this.email = email;
        this.message = message;
        this.tokenProvider = tokenProvider;
        this.relogioSistema = relogioSistema;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.authenticationManager = authenticationManager;
    }

    public RespostaLogin login(String login, String senha, String origem) {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, senha);

        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        DetalhesUsuario detalhesUsuario = (DetalhesUsuario) auth.getPrincipal();
        String jwtToken = tokenProvider.createToken(auth, origem);

        return new RespostaLogin(detalhesUsuario, jwtToken);
    }

    public RespostaExecucao alterarSenha(@NotNull DadosRedefinicaoSenha dados) {

        Optional<Usuario> usuarioDB = usuarioRepository.findByLogin(dados.getLogin());

        if (usuarioDB.isPresent()) {
            return trocarSenha(dados, usuarioDB.get());
        } else {
            return RespostaExecucao.erro(message.get("erro.usuario.inexistente"));
        }

    }

    public RespostaExecucao solicitarCodigo(String login) {

        Optional<Usuario> usuarioBanco = usuarioRepository.findByLogin(login);

        if (usuarioBanco.isPresent()) {
            return enviarCodigoRedefinicaoSenha(usuarioBanco.get());
        } else {
            return RespostaExecucao.erro(message.get("erro.usuario.inexistente"));
        }
    }

    public RespostaExecucao trocarSenha(@NotNull DadosRedefinicaoSenha dados, @NotNull Usuario usuario) {

        RespostaExecucao validacao = validarResetSenha(dados, usuario);

        if (validacao.possuiErro()) {
            return validacao;
        }

        String senhaCriptografada = passwordEncoder.encode(dados.getSenha());

        usuario.setCodigoResetSenha(null);
        usuario.setSenha(senhaCriptografada);
        usuario.setDataExpiracaoResetSenha(null);

        usuarioRepository.save(usuario);
        return RespostaExecucao.ok();
    }

    private RespostaExecucao enviarCodigoRedefinicaoSenha(@NotNull Usuario usuario) {
        atualizarCodigoUsuario(usuario);
        return email.enviarEmailRedefinicaoSenha(usuario.getEmail(), usuario.getNome(), usuario.getCodigoResetSenha());
    }

    public RespostaExecucao validarCodigoRedefinicaoSenha(CodigoRedefinicaoSenha dados) {
        Optional<Usuario> usuarioBanco = usuarioRepository.findByLogin(dados.getLogin());

        if(!usuarioBanco.isPresent()) {
            return RespostaExecucao.erro(message.get("erro.usuario.inexistente"));
        }

        Usuario usuario = usuarioBanco.get();
        return validarCodigo(dados.getCodigo(), usuario);
    }

    private RespostaExecucao validarResetSenha(@NotNull DadosRedefinicaoSenha dados, @NotNull Usuario usuario) {

        if (!StringUtils.equals(dados.getSenha(), dados.getConfirmacaoSenha())) {
            return RespostaExecucao.erro(message.get("erro.senhas.diferentes"));
        }

        return validarCodigo(dados.getCodigo(), usuario);
    }

    private RespostaExecucao validarCodigo(String codigo, @NotNull Usuario usuario) {

        if (StringUtils.equalsIgnoreCase(codigo, usuario.getCodigoResetSenha())) {

            boolean expirado = isCodigoExpirado(usuario);

            if (expirado) {
                return RespostaExecucao.erro(message.get("erro.codigo.expirado"));
            }
        } else {
            return RespostaExecucao.erro(message.get("erro.codigo.invalido"));
        }

        return RespostaExecucao.ok();
    }

    private boolean isCodigoExpirado(@NotNull Usuario usuario) {

        if (usuario.getDataExpiracaoResetSenha() == null) {
            return true;
        }

        return relogioSistema.localDateTimeAtual().isAfter(usuario.getDataExpiracaoResetSenha());
    }

    private void atualizarCodigoUsuario(@NotNull Usuario usuario) {

        String codigo = usuario.getCodigoResetSenha();

        if (StringUtils.isBlank(codigo) || isCodigoExpirado(usuario)) {
            codigo = RandomStringUtils.randomNumeric(4);
        }

        usuario.setCodigoResetSenha(codigo);
        usuario.setDataExpiracaoResetSenha(relogioSistema.localDateTimeAtual().plusHours(1L));

        usuarioRepository.save(usuario);
    }
}
