package br.com.pucminas.aluguel_carros_bh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "empresa")
@DiscriminatorValue("EMPRESA")
@Getter
@Setter
public class Empresa extends Agente {

    @Column(nullable = false, unique = true)
    private String cnpj;
}