package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "conta_consumidor_item")
public class ContaConsumidorItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantidade")
    private BigDecimal quantidade;
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "id_conta_consumidor", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaConsumidor contaConsumidor;
    @JoinColumn(name = "id_produto", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;

}
