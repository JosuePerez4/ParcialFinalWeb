package com.example.demo.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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
