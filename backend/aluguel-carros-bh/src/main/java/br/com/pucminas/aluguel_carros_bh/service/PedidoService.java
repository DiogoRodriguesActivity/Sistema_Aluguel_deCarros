package br.com.pucminas.aluguel_carros_bh.service;

import br.com.pucminas.aluguel_carros_bh.dto.PedidoResponse;
import br.com.pucminas.aluguel_carros_bh.model.Automovel;
import br.com.pucminas.aluguel_carros_bh.model.Cliente;
import br.com.pucminas.aluguel_carros_bh.model.Pedido;
import br.com.pucminas.aluguel_carros_bh.model.StatusPedido;
import br.com.pucminas.aluguel_carros_bh.repository.AutomovelRepository;
import br.com.pucminas.aluguel_carros_bh.repository.ClienteRepository;
import br.com.pucminas.aluguel_carros_bh.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;

    @Transactional
    public PedidoResponse criar(Long clienteId, Long automovelId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        Pedido p = new Pedido();
        p.setCliente(cliente);
        p.setAutomovel(buscarAutomovel(automovelId));
        return PedidoResponse.de(pedidoRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> meus(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream().map(PedidoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> todos() {
        return pedidoRepository.findAll().stream().map(PedidoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id, Long usuarioId, boolean agente) {
        Pedido p = buscarEntidade(id);
        if (!agente) exigirDono(p, usuarioId);
        return PedidoResponse.de(p);
    }

    @Transactional
    public PedidoResponse alterar(Long id, Long clienteId, Long automovelId) {
        Pedido p = buscarEntidade(id);
        exigirDono(p, clienteId);
        if (p.getStatus() != StatusPedido.CRIADO)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Só é possível alterar pedidos com status CRIADO");
        p.setAutomovel(buscarAutomovel(automovelId));
        return PedidoResponse.de(pedidoRepository.save(p));
    }

    @Transactional
    public PedidoResponse cancelar(Long id, Long clienteId) {
        Pedido p = buscarEntidade(id);
        exigirDono(p, clienteId);
        if (p.getStatus() != StatusPedido.CRIADO && p.getStatus() != StatusPedido.EM_ANALISE)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este pedido não pode mais ser cancelado");
        p.setStatus(StatusPedido.CANCELADO);
        return PedidoResponse.de(pedidoRepository.save(p));
    }

    private Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    private Automovel buscarAutomovel(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Automóvel não encontrado"));
    }

    private void exigirDono(Pedido p, Long usuarioId) {
        if (!p.getCliente().getId().equals(usuarioId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado");
    }
}