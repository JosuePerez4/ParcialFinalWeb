package com.example.demo.controller;

import com.example.demo.DTO.FacturaRequestDTO;
import com.example.demo.DTO.FacturaConsultaRequestDTO;
import com.example.demo.DTO.FacturaResponseDTO;
import com.example.demo.DTO.FacturaConsultaResponseDTO;
import com.example.demo.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

	@Autowired
	private FacturaService facturaService;

	// Endpoint para crear una factura
	@PostMapping("/crear/{tiendaUuid}")
	@Operation(summary = "Crear una nueva factura", description = "Crea una factura basada en la información proporcionada.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Factura creada exitosamente", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FacturaResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Error al crear la factura", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FacturaResponseDTO.class))) })
	public ResponseEntity<FacturaResponseDTO> createFactura(
			@Parameter(description = "UUID de la tienda donde se creará la factura", required = true) @PathVariable("tiendaUuid") String tiendaUuid,
			@Parameter(description = "Información de la factura que se desea crear", required = true) @RequestBody FacturaRequestDTO facturaRequestDTO) {

		try {
			// Llamar al servicio para crear la factura
			FacturaResponseDTO facturaResponse = facturaService.createFactura(tiendaUuid, facturaRequestDTO);
			return ResponseEntity.ok(facturaResponse);
		} catch (Exception e) {
			// En caso de error, devolver un mensaje de error con estado 404
			return ResponseEntity.status(404).body(new FacturaResponseDTO("error", e.getMessage(), null));
		}
	}

	@PostMapping("/consultar/{tiendaUuid}")
	@Operation(summary = "Consultar una factura", description = "Consulta los detalles de una factura con la información proporcionada.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Factura consultada exitosamente", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FacturaConsultaResponseDTO.class))),
			@ApiResponse(responseCode = "404", description = "Error al consultar la factura", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = FacturaConsultaResponseDTO.class))) })
	public ResponseEntity<FacturaConsultaResponseDTO> consultarFactura(
			@Parameter(description = "UUID de la tienda donde se realiza la consulta", required = true) @PathVariable("tiendaUuid") String tiendaUuid,
			@Parameter(description = "Datos para realizar la consulta de la factura", required = true) @RequestBody FacturaConsultaRequestDTO facturaConsultaRequestDTO) {

		try {
			// Llamar al servicio para consultar la factura
			FacturaConsultaResponseDTO facturaConsultaResponse = facturaService.consultarFactura(tiendaUuid,
					facturaConsultaRequestDTO);
			return ResponseEntity.ok(facturaConsultaResponse);
		} catch (Exception e) {
			// En caso de error, devolver un mensaje de error con estado 404
			FacturaConsultaResponseDTO errorResponse = new FacturaConsultaResponseDTO(e.getMessage());
			return ResponseEntity.status(404).body(errorResponse);
		}
	}
}
