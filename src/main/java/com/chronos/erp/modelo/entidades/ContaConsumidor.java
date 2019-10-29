package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "conta_consumidor")
public class ContaConsumidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data_movimentacao")
    private Date dataMovimentacao;
    @Column(name = "tipo_credito")
    private String tipoCredito;
    @Column(name = "valor_credito")
    private BigDecimal valorCredito;
    @Column(name = "credito_utilizado")
    private BigDecimal creditoUtilizado;
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "cpf_cnpj")
    private String cpfCnpj;
    @OneToMany(mappedBy = "contaConsumidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContaConsumidorItem> items;

    public ContaConsumidor() {
    }
}
