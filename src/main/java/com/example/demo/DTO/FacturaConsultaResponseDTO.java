package com.example.demo.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FacturaConsultaResponseDTO {
	private double total;
	private double impuestos;
	private ClienteDTO cliente;
	private List<ProductoDTO> productos;
	private CajeroDTO cajero;
	private String mensaje; // Nuevo campo para el mensaje de error

	// Constructor para casos de error
	public FacturaConsultaResponseDTO(String mensaje) {
		this.mensaje = mensaje;
	}

	@Data
	public static class ClienteDTO {
		private String documento;
		private String nombre;
		private String tipoDocumento;
	}

	@Data
	public static class ProductoDTO {
		private String referencia;
		private String nombre;
		private int cantidad;
		private double precio;
		private double descuento;
		private double subtotal;
	}

	@Data
	public static class CajeroDTO {
		private String documento;
		private String nombre;
	}
}
