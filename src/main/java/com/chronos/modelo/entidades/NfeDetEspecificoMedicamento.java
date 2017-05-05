package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "NFE_DET_ESPECIFICO_MEDICAMENTO")
public class NfeDetEspecificoMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO_LOTE")
    private String numeroLote;
    @Column(name = "QUANTIDADE_LOTE")
    private BigDecimal quantidadeLote;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_FABRICACAO")
    private Date dataFabricacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VALIDADE")
    private Date dataValidade;
    @Column(name = "PRECO_MAXIMO_CONSUMIDOR")
    private BigDecimal precoMaximoConsumidor;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoMedicamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public BigDecimal getQuantidadeLote() {
        return quantidadeLote;
    }

    public void setQuantidadeLote(BigDecimal quantidadeLote) {
        this.quantidadeLote = quantidadeLote;
    }

    public Date getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(Date dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public BigDecimal getPrecoMaximoConsumidor() {
        return precoMaximoConsumidor;
    }

    public void setPrecoMaximoConsumidor(BigDecimal precoMaximoConsumidor) {
        this.precoMaximoConsumidor = precoMaximoConsumidor;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

}
