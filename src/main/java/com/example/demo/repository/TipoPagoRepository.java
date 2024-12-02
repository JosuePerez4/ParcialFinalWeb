package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.TipoPago;

@Repository
public interface TipoPagoRepository extends JpaRepository<TipoPago, Integer> {
    Optional<TipoPago> findByNombre(String nombre);
}
