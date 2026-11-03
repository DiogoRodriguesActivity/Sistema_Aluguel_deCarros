package br.com.pucminas.aluguel_carros_bh.dto;

import jakarta.validation.constraints.NotNull;

public record PedidoRequest(@NotNull Long automovelId) {
}