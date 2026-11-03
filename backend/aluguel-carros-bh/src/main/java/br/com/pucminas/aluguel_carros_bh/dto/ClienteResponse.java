package br.com.pucminas.aluguel_carros_bh.dto;

import java.util.List;

public record ClienteResponse(
        Long id,
        String login,
        String rg,
        String cpf,
        String nome,
        String endereco,
        String profissao,
        List<RendimentoDTO> rendimentos
) {
}