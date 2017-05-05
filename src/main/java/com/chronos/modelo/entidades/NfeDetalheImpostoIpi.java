
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


@Entity
@Table(name = "NFE_DETALHE_IMPOSTO_IPI")
public class NfeDetalheImpostoIpi implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ENQUADRAMENTO_IPI")
    private String enquadramentoIpi;
    @Column(name = "CNPJ_PRODUTOR")
    private String cnpjProdutor;
    @Column(name = "CODIGO_SELO_IPI")
    private String codigoSeloIpi;
    @Column(name = "QUANTIDADE_SELO_IPI")
    private Integer quantidadeSeloIpi;
    @Column(name = "ENQUADRAMENTO_LEGAL_IPI")
    private String enquadramentoLegalIpi;
    @Column(name = "CST_IPI")
    private String cstIpi;
    @Column(name = "VALOR_BASE_CALCULO_IPI")
    private BigDecimal valorBaseCalculoIpi;
    @Column(name = "ALIQUOTA_IPI")
    private BigDecimal aliquotaIpi;
    @Column(name = "QUANTIDADE_UNIDADE_TRIBUTAVEL")
    private BigDecimal quantidadeUnidadeTributavel;
    @Column(name = "VALOR_UNIDADE_TRIBUTAVEL")
    private BigDecimal valorUnidadeTributavel;
    @Column(name = "VALOR_IPI")
    private BigDecimal valorIpi;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetalheImpostoIpi() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnquadramentoIpi() {
        return enquadramentoIpi;
    }

    public void setEnquadramentoIpi(String enquadramentoIpi) {
        this.enquadramentoIpi = enquadramentoIpi;
    }

    public String getCnpjProdutor() {
        return cnpjProdutor;
    }

    public void setCnpjProdutor(String cnpjProdutor) {
        this.cnpjProdutor = cnpjProdutor;
    }

    public String getCodigoSeloIpi() {
        return codigoSeloIpi;
    }

    public void setCodigoSeloIpi(String codigoSeloIpi) {
        this.codigoSeloIpi = codigoSeloIpi;
    }

    public Integer getQuantidadeSeloIpi() {
        return quantidadeSeloIpi;
    }

    public void setQuantidadeSeloIpi(Integer quantidadeSeloIpi) {
        this.quantidadeSeloIpi = quantidadeSeloIpi;
    }

    public String getEnquadramentoLegalIpi() {
        return enquadramentoLegalIpi;
    }

    public void setEnquadramentoLegalIpi(String enquadramentoLegalIpi) {
        this.enquadramentoLegalIpi = enquadramentoLegalIpi;
    }

    public String getCstIpi() {
        return cstIpi;
    }

    public void setCstIpi(String cstIpi) {
        this.cstIpi = cstIpi;
    }

    public BigDecimal getValorBaseCalculoIpi() {
        return valorBaseCalculoIpi;
    }

    public void setValorBaseCalculoIpi(BigDecimal valorBaseCalculoIpi) {
        this.valorBaseCalculoIpi = valorBaseCalculoIpi;
    }

    public BigDecimal getAliquotaIpi() {
        return aliquotaIpi;
    }

    public void setAliquotaIpi(BigDecimal aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }

    public BigDecimal getQuantidadeUnidadeTributavel() {
        return quantidadeUnidadeTributavel;
    }

    public void setQuantidadeUnidadeTributavel(BigDecimal quantidadeUnidadeTributavel) {
        this.quantidadeUnidadeTributavel = quantidadeUnidadeTributavel;
    }

    public BigDecimal getValorUnidadeTributavel() {
        return valorUnidadeTributavel;
    }

    public void setValorUnidadeTributavel(BigDecimal valorUnidadeTributavel) {
        this.valorUnidadeTributavel = valorUnidadeTributavel;
    }

    public BigDecimal getValorIpi() {
        return valorIpi;
    }

    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }



}
