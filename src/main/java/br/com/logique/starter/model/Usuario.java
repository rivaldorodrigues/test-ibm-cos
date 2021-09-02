package br.com.logique.starter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.envers.Audited;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Audited
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
    private Long id;

    @NotNull
    private String nome;

    @Email()
    @Size(min = 6, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String login;

    @NotNull
    @Size(min = 6, max = 60)
    @Column(length = 60, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "perfil_usuario",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    )
    private Set<Perfil> perfis;

    @JsonIgnore
    private String codigoResetSenha;

    @JsonIgnore
    private LocalDateTime dataExpiracaoResetSenha;
}
