package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaConsultaRequestDTO {
    private String token;
    private String cliente; // Documento del cliente
    private int factura; // Número de factura
}
