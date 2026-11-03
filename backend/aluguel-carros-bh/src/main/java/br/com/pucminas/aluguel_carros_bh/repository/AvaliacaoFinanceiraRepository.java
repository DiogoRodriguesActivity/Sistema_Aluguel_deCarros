package br.com.pucminas.aluguel_carros_bh.repository;

import br.com.pucminas.aluguel_carros_bh.model.AvaliacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoFinanceiraRepository extends JpaRepository<AvaliacaoFinanceira, Long> {
    List<AvaliacaoFinanceira> findByPedidoId(Long pedidoId);
}