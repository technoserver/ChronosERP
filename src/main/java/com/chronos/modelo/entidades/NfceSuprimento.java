
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "NFCE_SUPRIMENTO")
public class NfceSuprimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SUPRIMENTO")
    private Date dataSuprimento;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_NFCE_MOVIMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfceMovimento nfceMovimento;

    public NfceSuprimento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataSuprimento() {
        return dataSuprimento;
    }

    public void setDataSuprimento(Date dataSuprimento) {
        this.dataSuprimento = dataSuprimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public NfceMovimento getNfceMovimento() {
        return nfceMovimento;
    }

    public void setNfceMovimento(NfceMovimento nfceMovimento) {
        this.nfceMovimento = nfceMovimento;
    }


}