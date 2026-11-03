package br.com.pucminas.aluguel_carros_bh.repository;

import br.com.pucminas.aluguel_carros_bh.model.ContratoCredito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContratoCreditoRepository extends JpaRepository<ContratoCredito, Long> {
    Optional<ContratoCredito> findByPedidoId(Long pedidoId);
}