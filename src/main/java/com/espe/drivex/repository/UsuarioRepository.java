package com.espe.drivex.repository;

import com.espe.drivex.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByEmailAndActivoTrue(String email);

    List<Usuario> findAllByActivoTrue();

    List<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombres, String apellidos);

    boolean existsByEmail(String email);
}
