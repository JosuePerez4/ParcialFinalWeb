package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "detalles_compra")
public class DetallesCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    private Double precio;

    private Double descuento;

    // Getters y setters
}
