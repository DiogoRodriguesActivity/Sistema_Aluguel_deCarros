package br.com.pucminas.aluguel_carros_bh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato_credito")
@Getter
@Setter
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banco_id")
    private Banco banco;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal taxaJuros;

    @Column(nullable = false)
    private int parcelas;

    @Column(nullable = false)
    private LocalDate data;

    @PrePersist
    void aoCriar() {
        if (data == null) data = LocalDate.now();
    }
}