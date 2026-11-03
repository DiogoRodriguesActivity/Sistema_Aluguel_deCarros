package br.com.pucminas.aluguel_carros_bh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "avaliacao_financeira")
@Getter
@Setter
public class AvaliacaoFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agente_id")
    private Agente agente;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Parecer parecer;

    @Column(columnDefinition = "text")
    private String observacao;

    @PrePersist
    void aoCriar() {
        if (data == null) data = LocalDateTime.now();
    }
}