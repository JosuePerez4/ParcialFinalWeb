package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cliente")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 20)
	private String nombre;

	@Column(nullable = false, length = 100)
	private String documento;

	@ManyToOne
	@JoinColumn(name = "tipo_documento_id", nullable = false)
	private TipoDocumento tipoDocumento;
}
