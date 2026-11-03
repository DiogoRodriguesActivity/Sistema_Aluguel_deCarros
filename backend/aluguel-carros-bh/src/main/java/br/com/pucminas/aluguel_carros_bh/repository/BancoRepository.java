package br.com.pucminas.aluguel_carros_bh.repository;

import br.com.pucminas.aluguel_carros_bh.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {
}