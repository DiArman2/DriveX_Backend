package com.espe.drivex.repository;

import com.espe.drivex.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPlaca(String placa);

    List<Vehiculo> findAllByActivoTrue();

    List<Vehiculo> findAllByActivoTrueAndDisponibleTrue();

    List<Vehiculo> findByColorContainingIgnoreCaseAndActivoTrue(String color);
}
