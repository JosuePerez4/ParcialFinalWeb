package com.example.demo.DTO;

import lombok.Data;
import java.util.List;

@Data
public class FacturaRequestDTO {
	private double impuesto;
	private ClienteDTO cliente;
	private List<ProductoDTO> productos;
	private List<MedioPagoDTO> mediosPago;
	private VendedorDTO vendedor;
	private CajeroDTO cajero;

	@Data
	public static class ClienteDTO {
		private String documento;
		private String nombre;
		private String tipoDocumento;
	}

	@Data
	public static class ProductoDTO {
		private String referencia;
		private int cantidad;
		private double descuento;
	}

	@Data
	public static class MedioPagoDTO {
		private String tipoPago;
		private String tipoTarjeta; // Opcional, solo para tarjetas
		private int cuotas; // Opcional, solo para tarjetas
		private double valor;
	}

	@Data
	public static class VendedorDTO {
		private String documento;
	}

	@Data
	public static class CajeroDTO {
		private String token;
	}
}
