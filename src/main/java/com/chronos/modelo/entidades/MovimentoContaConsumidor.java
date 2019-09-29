package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "movimento_conta_consumidor")
public class MovimentoContaConsumidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_movimento")
    private Date dataMovimento;
    @Column(name = "tipo_movimento")
    private String tipoMovimento;
    @Column(name = "numero_documento")
    private String numeroDocumento;
    @Column(name = "codigo_modulo")
    private String codigoModulo;
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "id_conta_consumidor", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaConsumidor contaConsumidor;
}
