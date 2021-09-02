package br.com.logique.starter.controller.v1;

import br.com.logique.starter.model.RespostaExecucao;
import br.com.logique.starter.model.dto.usuario.CodigoRedefinicaoSenha;
import br.com.logique.starter.model.dto.usuario.Credenciais;
import br.com.logique.starter.model.dto.usuario.DadosRedefinicaoSenha;
import br.com.logique.starter.model.dto.usuario.RespostaLogin;
import br.com.logique.starter.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestHeader("X-Origem") String origem, @RequestBody @Valid Credenciais credenciais) {
        try {
            RespostaLogin resposta = this.authenticationService.login(credenciais.getLogin(), credenciais.getSenha(), origem);
            return new ResponseEntity<>(resposta, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erro ao fazer login", e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciais inválidas.");
        }
    }

    @PostMapping("/codigo/solicitar")
    public boolean solicitarCodigo(@RequestBody String login) {
        RespostaExecucao result = authenticationService.solicitarCodigo(login);
        result.ifErrorThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getMensagemErro()));
        return true;
    }

    @PostMapping("/codigo/validar")
    public boolean validarCodigo(@RequestBody @Valid CodigoRedefinicaoSenha codigo) {
        RespostaExecucao result = authenticationService.validarCodigoRedefinicaoSenha(codigo);
        result.ifErrorThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getMensagemErro()));
        return true;
    }

    @PatchMapping("/senha/alterar")
    public boolean alterarSenha(@RequestBody @Valid DadosRedefinicaoSenha dados) {
        RespostaExecucao result = authenticationService.alterarSenha(dados);
        result.ifErrorThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getMensagemErro()));
        return true;
    }

}
