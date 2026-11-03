package br.com.pucminas.aluguel_carros_bh.dto;

import br.com.pucminas.aluguel_carros_bh.model.Pedido;
import br.com.pucminas.aluguel_carros_bh.model.StatusPedido;

import java.time.LocalDateTime;

public record PedidoResponse(
        Long id, LocalDateTime dataCriacao, StatusPedido status,
        Long clienteId, String clienteNome, AutomovelResponse automovel
) {
    public static PedidoResponse de(Pedido p) {
        return new PedidoResponse(p.getId(), p.getDataCriacao(), p.getStatus(),
                p.getCliente().getId(), p.getCliente().getNome(),
                AutomovelResponse.de(p.getAutomovel()));
    }
}