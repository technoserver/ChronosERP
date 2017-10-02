/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author John Vanderson M Lim
 */
@Entity
@Table(name = "view_tributacao_icms_custom")
public class ViewTributacaoIcmsCustom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "descricao", length = 100)
    private String descricao;
    @Column(name = "origem_mercadoria")
    private String origemMercadoria;
    @Column(name = "uf_destino", length = 2)
    private String ufDestino;
    @Column(name = "cfop")
    private Integer cfop;
    @Column(name = "csosn_b", length = 3)
    private String csosnB;
    @Column(name = "cst_b", length = 2)
    private String cstB;
    @Column(name = "modalidade_bc")
    private String modalidadeBc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "aliquota", precision = 18, scale = 6)
    private BigDecimal aliquota;
    @Column(name = "valor_pauta", precision = 18, scale = 6)
    private BigDecimal valorPauta;
    @Column(name = "valor_preco_maximo", precision = 18, scale = 6)
    private BigDecimal valorPrecoMaximo;
    @Column(name = "mva", precision = 18, scale = 6)
    private BigDecimal mva;
    @Column(name = "porcento_bc", precision = 18, scale = 6)
    private BigDecimal porcentoBc;
    @Column(name = "modalidade_bc_st")
    private String modalidadeBcSt;
    @Column(name = "aliquota_interna_st", precision = 18, scale = 6)
    private BigDecimal aliquotaInternaSt;
    @Column(name = "aliquota_interestadual_st", precision = 18, scale = 6)
    private BigDecimal aliquotaInterestadualSt;
    @Column(name = "porcento_bc_st", precision = 18, scale = 6)
    private BigDecimal porcentoBcSt;
    @Column(name = "aliquota_icms_st", precision = 18, scale = 6)
    private BigDecimal aliquotaIcmsSt;
    @Column(name = "valor_pauta_st", precision = 18, scale = 6)
    private BigDecimal valorPautaSt;
    @Column(name = "valor_preco_maximo_st", precision = 18, scale = 6)
    private BigDecimal valorPrecoMaximoSt;
    @Column(name = "aliquota_fcp", precision = 18, scale = 6)
    private BigDecimal aliquotaFcp;

    public ViewTributacaoIcmsCustom() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOrigemMercadoria() {
        return origemMercadoria;
    }

    public void setOrigemMercadoria(String origemMercadoria) {
        this.origemMercadoria = origemMercadoria;
    }

    public String getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(String ufDestino) {
        this.ufDestino = ufDestino;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public String getCsosnB() {
        return csosnB;
    }

    public void setCsosnB(String csosnB) {
        this.csosnB = csosnB;
    }

    public String getCstB() {
        return cstB;
    }

    public void setCstB(String cstB) {
        this.cstB = cstB;
    }

    public String getModalidadeBc() {
        return modalidadeBc;
    }

    public void setModalidadeBc(String modalidadeBc) {
        this.modalidadeBc = modalidadeBc;
    }

    public BigDecimal getAliquota() {
        return aliquota;
    }

    public void setAliquota(BigDecimal aliquota) {
        this.aliquota = aliquota;
    }

    public BigDecimal getValorPauta() {
        return valorPauta;
    }

    public void setValorPauta(BigDecimal valorPauta) {
        this.valorPauta = valorPauta;
    }

    public BigDecimal getValorPrecoMaximo() {
        return valorPrecoMaximo;
    }

    public void setValorPrecoMaximo(BigDecimal valorPrecoMaximo) {
        this.valorPrecoMaximo = valorPrecoMaximo;
    }

    public BigDecimal getMva() {
        return mva;
    }

    public void setMva(BigDecimal mva) {
        this.mva = mva;
    }

    public BigDecimal getPorcentoBc() {
        return porcentoBc;
    }

    public void setPorcentoBc(BigDecimal porcentoBc) {
        this.porcentoBc = porcentoBc;
    }

    public String getModalidadeBcSt() {
        return modalidadeBcSt;
    }

    public void setModalidadeBcSt(String modalidadeBcSt) {
        this.modalidadeBcSt = modalidadeBcSt;
    }

    public BigDecimal getAliquotaInternaSt() {
        return aliquotaInternaSt;
    }

    public void setAliquotaInternaSt(BigDecimal aliquotaInternaSt) {
        this.aliquotaInternaSt = aliquotaInternaSt;
    }

    public BigDecimal getAliquotaInterestadualSt() {
        return aliquotaInterestadualSt;
    }

    public void setAliquotaInterestadualSt(BigDecimal aliquotaInterestadualSt) {
        this.aliquotaInterestadualSt = aliquotaInterestadualSt;
    }

    public BigDecimal getPorcentoBcSt() {
        return porcentoBcSt;
    }

    public void setPorcentoBcSt(BigDecimal porcentoBcSt) {
        this.porcentoBcSt = porcentoBcSt;
    }

    public BigDecimal getAliquotaIcmsSt() {
        return aliquotaIcmsSt;
    }

    public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
        this.aliquotaIcmsSt = aliquotaIcmsSt;
    }

    public BigDecimal getValorPautaSt() {
        return valorPautaSt;
    }

    public void setValorPautaSt(BigDecimal valorPautaSt) {
        this.valorPautaSt = valorPautaSt;
    }

    public BigDecimal getValorPrecoMaximoSt() {
        return valorPrecoMaximoSt;
    }

    public void setValorPrecoMaximoSt(BigDecimal valorPrecoMaximoSt) {
        this.valorPrecoMaximoSt = valorPrecoMaximoSt;
    }

    public BigDecimal getAliquotaFcp() {
        return aliquotaFcp;
    }

    public void setAliquotaFcp(BigDecimal aliquotaFcp) {
        this.aliquotaFcp = aliquotaFcp;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ViewTributacaoIcmsCustom other = (ViewTributacaoIcmsCustom) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
