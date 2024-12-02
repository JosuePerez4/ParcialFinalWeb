package com.example.demo.DTO;

import lombok.Data;

@Data
public class FacturaConsultaRequestDTO {
    private String token;
    private String cliente; // Documento del cliente
    private int factura; // Número de factura
}
