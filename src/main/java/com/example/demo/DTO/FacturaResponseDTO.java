package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacturaResponseDTO {
    private String status;
    private String message;
    private FacturaData data;

    @Data
    public static class FacturaData {
        private int numero;
        private double total;
        private LocalDateTime fecha;
    }
}
