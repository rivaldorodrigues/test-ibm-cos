package br.com.logique.starter.repository.usuario;

import br.com.logique.starter.model.Usuario;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends PagingAndSortingRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	@Query("SELECT u FROM Usuario u ORDER BY u.nome")
	List<Usuario> findAll();

	Optional<Usuario> findByLogin(@Param("login") String login);

	Optional<Usuario> findByEmail(@Param("email") String email);
}
