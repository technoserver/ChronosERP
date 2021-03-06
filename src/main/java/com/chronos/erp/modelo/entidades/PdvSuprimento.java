
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "PDV_SUPRIMENTO")
public class PdvSuprimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_SUPRIMENTO")
    private Date dataSuprimento;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_PDV_MOVIMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvMovimento pdvMovimento;

    public PdvSuprimento() {
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

    public PdvMovimento getPdvMovimento() {
        return pdvMovimento;
    }

    public void setPdvMovimento(PdvMovimento pdvMovimento) {
        this.pdvMovimento = pdvMovimento;
    }


}
