package fch_shell.dal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fch_shell.dal.models.Usuario;

public interface UsuariosRepository extends JpaRepository<Usuario, Integer> {

	@Query("SELECT u FROM Usuario u WHERE LOWER(u.correo) = LOWER(?1)")
	Usuario findByCorreo(String correo);
	
}
