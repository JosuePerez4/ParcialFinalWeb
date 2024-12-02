package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Compra;
import com.example.demo.entities.DetallesCompra;

@Repository
public interface DetallesCompraRepository extends JpaRepository<DetallesCompra, Integer> {
	List<DetallesCompra> findByCompra(Compra compra);
}
