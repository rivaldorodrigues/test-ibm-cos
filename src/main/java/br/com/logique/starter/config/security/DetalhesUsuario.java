package br.com.logique.starter.config.security;

import br.com.logique.starter.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalhesUsuario implements UserDetails {

    private Long id;

    private String nome;

    private String email;

    private String login;

    @JsonIgnore
    private String senha;

    private Collection<SimpleGrantedAuthority> authorities;

    public static DetalhesUsuario fromUsuario(Usuario usuario) {

        Set<SimpleGrantedAuthority> authorities = usuario.getPerfis()
                .stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getNome()))
                .collect(Collectors.toSet());

        return new DetalhesUsuario(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getSenha(),
                authorities);
    }

    /**
     * Recupera a nome de cada permissão/perfil.
     *
     * Os valores são utilizados para validar permissões no front-end.
     * @return uma lista contendo o nome de cada permissão/perfil
     */
    public List<String> getPermissoes() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public boolean possuiPermissao(String permissao) {
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(permissao));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return senha;
    }

    @Override
    @JsonIgnore
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DetalhesUsuario that = (DetalhesUsuario) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
