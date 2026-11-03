package br.com.pucminas.aluguel_carros_bh.dto;

import br.com.pucminas.aluguel_carros_bh.model.Automovel;

public record AutomovelResponse(
        Long id, String matricula, int ano, String marca,
        String modelo, String placa, Long proprietarioId
) {
    public static AutomovelResponse de(Automovel a) {
        return new AutomovelResponse(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(),
                a.getModelo(), a.getPlaca(),
                a.getProprietario() != null ? a.getProprietario().getId() : null);
    }
}