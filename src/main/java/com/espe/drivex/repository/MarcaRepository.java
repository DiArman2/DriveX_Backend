package com.espe.drivex.repository;

import com.espe.drivex.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    Optional<Marca> findByNombre(String nombre);

    List<Marca> findAllByActivoTrue();

    List<Marca> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
