package com.espe.drivex.repository;

import com.espe.drivex.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findAllByActivoTrue();

    List<Reserva> findByClienteIdAndActivoTrue(Long clienteId);

    List<Reserva> findByVehiculoIdAndActivoTrue(Long vehiculoId);
}
