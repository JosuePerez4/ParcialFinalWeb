package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "tienda")
public class Tienda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String direccion;

    @Column
    private UUID uuid;

    // Getters y setters
}
