/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author John Vanderson M Lim
 */
@Entity
@Table(name = "tribut_iss")
public class TributIss implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "modalidade_base_calculo")
    private Character modalidadeBaseCalculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcento_base_calculo", precision = 18, scale = 6)
    private BigDecimal porcentoBaseCalculo;
    @Column(name = "aliquota_porcento", precision = 18, scale = 6)
    private BigDecimal aliquotaPorcento;
    @Column(name = "aliquota_unidade", precision = 18, scale = 6)
    private BigDecimal aliquotaUnidade;
    @Column(name = "valor_preco_maximo", precision = 18, scale = 6)
    private BigDecimal valorPrecoMaximo;
    @Column(name = "valor_pauta_fiscal", precision = 18, scale = 6)
    private BigDecimal valorPautaFiscal;
    @Column(name = "item_lista_servico")
    private Integer itemListaServico;
    @Column(name = "codigo_tributacao")
    private Character codigoTributacao;
    @Column(name = "indicador_incentivo_fiscal")
    private Integer indicadorIncentivoFiscal;
    @Column(name = "indicador_exigibilidade")
    private Integer indicadorExigibilidade;
    @JoinColumn(name = "ID_TRIBUT_CONFIGURA_OF_GT", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private TributConfiguraOfGt tributConfiguraOfGt;

    public TributIss() {
    }

    public TributIss(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(Character modalidadeBaseCalculo) {
        this.modalidadeBaseCalculo = modalidadeBaseCalculo;
    }

    public BigDecimal getPorcentoBaseCalculo() {
        return porcentoBaseCalculo;
    }

    public void setPorcentoBaseCalculo(BigDecimal porcentoBaseCalculo) {
        this.porcentoBaseCalculo = porcentoBaseCalculo;
    }

    public BigDecimal getAliquotaPorcento() {
        return aliquotaPorcento;
    }

    public void setAliquotaPorcento(BigDecimal aliquotaPorcento) {
        this.aliquotaPorcento = aliquotaPorcento;
    }

    public BigDecimal getAliquotaUnidade() {
        return aliquotaUnidade;
    }

    public void setAliquotaUnidade(BigDecimal aliquotaUnidade) {
        this.aliquotaUnidade = aliquotaUnidade;
    }

    public BigDecimal getValorPrecoMaximo() {
        return valorPrecoMaximo;
    }

    public void setValorPrecoMaximo(BigDecimal valorPrecoMaximo) {
        this.valorPrecoMaximo = valorPrecoMaximo;
    }

    public BigDecimal getValorPautaFiscal() {
        return valorPautaFiscal;
    }

    public void setValorPautaFiscal(BigDecimal valorPautaFiscal) {
        this.valorPautaFiscal = valorPautaFiscal;
    }

    public Integer getItemListaServico() {
        return itemListaServico;
    }

    public void setItemListaServico(Integer itemListaServico) {
        this.itemListaServico = itemListaServico;
    }

    public Character getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(Character codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public Integer getIndicadorIncentivoFiscal() {
        return indicadorIncentivoFiscal;
    }

    public void setIndicadorIncentivoFiscal(Integer indicadorIncentivoFiscal) {
        this.indicadorIncentivoFiscal = indicadorIncentivoFiscal;
    }

    public Integer getIndicadorExigibilidade() {
        return indicadorExigibilidade;
    }

    public void setIndicadorExigibilidade(Integer indicadorExigibilidade) {
        this.indicadorExigibilidade = indicadorExigibilidade;
    }

    public TributConfiguraOfGt getTributConfiguraOfGt() {
        return tributConfiguraOfGt;
    }

    public void setTributConfiguraOfGt(TributConfiguraOfGt tributConfiguraOfGt) {
        this.tributConfiguraOfGt = tributConfiguraOfGt;
    }

    
 
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TributIss)) {
            return false;
        }
        TributIss other = (TributIss) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "tabelas.TributIss[ id=" + id + " ]";
    }

}
