package br.com.logique.starter.model.dto.usuario;

import br.com.logique.starter.config.security.DetalhesUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespostaLogin {
    DetalhesUsuario detalhesUsuario;
    String token;
}
