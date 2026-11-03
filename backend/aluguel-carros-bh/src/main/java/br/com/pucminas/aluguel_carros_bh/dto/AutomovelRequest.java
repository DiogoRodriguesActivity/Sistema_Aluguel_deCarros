package br.com.pucminas.aluguel_carros_bh.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AutomovelRequest(
        @NotBlank String matricula,
        @Min(1950) @Max(2100) int ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank String placa
) {
}