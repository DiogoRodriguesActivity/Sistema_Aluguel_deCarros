package br.com.pucminas.aluguel_carros_bh.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RendimentoDTO(
        @NotBlank String entidadeEmpregadora,
        @NotNull @DecimalMin("0.0") BigDecimal valor
) {
}