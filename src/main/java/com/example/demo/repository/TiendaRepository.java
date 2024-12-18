package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Tienda;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Integer> {
	Optional<Tienda> findByUuid(UUID uuid);
}
