package br.com.pucminas.aluguel_carros_bh.dto;

public record LoginResponse(String token, Long id, String papel, String nome) {
}