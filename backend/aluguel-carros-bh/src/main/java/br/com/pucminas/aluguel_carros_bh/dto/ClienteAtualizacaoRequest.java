package br.com.pucminas.aluguel_carros_bh.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ClienteAtualizacaoRequest(
        @NotBlank String rg,
        @NotBlank String nome,
        @NotBlank String endereco,
        String profissao,
        @Size(max = 3, message = "No máximo 3 rendimentos") List<@Valid RendimentoDTO> rendimentos
) {
}