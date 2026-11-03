package br.com.pucminas.aluguel_carros_bh.repository;

import br.com.pucminas.aluguel_carros_bh.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Long> {
}