package br.com.pucminas.aluguel_carros_bh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "agente")
@Getter
@Setter
public abstract class Agente extends Usuario {

    @Column(nullable = false)
    private String nome;
}