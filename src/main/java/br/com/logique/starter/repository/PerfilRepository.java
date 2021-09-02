package br.com.logique.starter.repository;

import br.com.logique.starter.model.Perfil;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends CrudRepository<Perfil, Long> {

	@Query("SELECT p FROM Perfil p ORDER BY p.nome")
	List<Perfil> findAll();

}
