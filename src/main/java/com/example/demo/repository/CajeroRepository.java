package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Cajero;

@Repository
public interface CajeroRepository extends JpaRepository<Cajero, Integer> {
    Optional<Cajero> findByToken(String token);
}
