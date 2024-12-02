package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	@Query("SELECT c FROM Cliente c WHERE c.documento = :documento AND c.tipoDocumento = :tipoDocumento")
	Optional<Cliente> findByDocumentoAndTipoDocumento(@Param("documento") String documento, @Param("tipoDocumento") String tipoDocumento);
}