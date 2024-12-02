package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaRequestDTO {
	private double impuesto;
	private ClienteDTO cliente;
	private List<ProductoDTO> productos;
	private List<MedioPagoDTO> mediosPago;
	private VendedorDTO vendedor;
	private CajeroDTO cajero;

	@Data
	public static class ClienteDTO {
		@JsonProperty("documento")
		private String documento;

		@JsonProperty("nombre")
		private String nombre;

		@JsonProperty("tipo_documento") // Cambiado para que coincida con el JSON
		private String tipoDocumento;
	}

	@Data
	public static class ProductoDTO {
		@JsonProperty("referencia")
		private String referencia;

		@JsonProperty("cantidad")
		private int cantidad;

		@JsonProperty("descuento")
		private double descuento;
	}

	@Data
	public static class MedioPagoDTO {
		@JsonProperty("tipo_pago") // Cambiado para que coincida con el JSON
		private String tipoPago;

		@JsonProperty("tipo_tarjeta") // Cambiado para que coincida con el JSON
		private String tipoTarjeta;

		@JsonProperty("cuotas") // Cambiado para que coincida con el JSON
		private int cuotas;

		@JsonProperty("valor")
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
