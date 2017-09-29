
package com.chronos.modelo.entidades.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class ViewSpedC190 implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "ID")
    private int id;
    @Column(name = "CST_ICMS")
    private String cstIcms;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "ALIQUOTA_ICMS")
    private BigDecimal aliquotaIcms;
    @Column(name = "DATA_HORA_EMISSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmissao;
    @Column(name = "SOMA_VALOR_OPERACAO")
    private BigDecimal somaValorOperacao;
    @Column(name = "SOMA_BASE_CALCULO_ICMS")
    private BigDecimal somaBaseCalculoIcms;
    @Column(name = "SOMA_VALOR_ICMS")
    private BigDecimal somaValorIcms;
    @Column(name = "SOMA_BASE_CALCULO_ICMS_ST")
    private BigDecimal somaBaseCalculoIcmsSt;
    @Column(name = "SOMA_VALOR_ICMS_ST")
    private BigDecimal somaValorIcmsSt;
    @Column(name = "SOMA_VL_RED_BC")
    private BigDecimal somaVlRedBc;
    @Column(name = "SOMA_VALOR_IPI")
    private BigDecimal somaValorIpi;

    public ViewSpedC190() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCstIcms() {
        return cstIcms;
    }

    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public BigDecimal getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public BigDecimal getSomaValorOperacao() {
        return somaValorOperacao;
    }

    public void setSomaValorOperacao(BigDecimal somaValorOperacao) {
        this.somaValorOperacao = somaValorOperacao;
    }

    public BigDecimal getSomaBaseCalculoIcms() {
        return somaBaseCalculoIcms;
    }

    public void setSomaBaseCalculoIcms(BigDecimal somaBaseCalculoIcms) {
        this.somaBaseCalculoIcms = somaBaseCalculoIcms;
    }

    public BigDecimal getSomaValorIcms() {
        return somaValorIcms;
    }

    public void setSomaValorIcms(BigDecimal somaValorIcms) {
        this.somaValorIcms = somaValorIcms;
    }

    public BigDecimal getSomaBaseCalculoIcmsSt() {
        return somaBaseCalculoIcmsSt;
    }

    public void setSomaBaseCalculoIcmsSt(BigDecimal somaBaseCalculoIcmsSt) {
        this.somaBaseCalculoIcmsSt = somaBaseCalculoIcmsSt;
    }

    public BigDecimal getSomaValorIcmsSt() {
        return somaValorIcmsSt;
    }

    public void setSomaValorIcmsSt(BigDecimal somaValorIcmsSt) {
        this.somaValorIcmsSt = somaValorIcmsSt;
    }

    public BigDecimal getSomaVlRedBc() {
        return somaVlRedBc;
    }

    public void setSomaVlRedBc(BigDecimal somaVlRedBc) {
        this.somaVlRedBc = somaVlRedBc;
    }

    public BigDecimal getSomaValorIpi() {
        return somaValorIpi;
    }

    public void setSomaValorIpi(BigDecimal somaValorIpi) {
        this.somaValorIpi = somaValorIpi;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final ViewSpedC190 other = (ViewSpedC190) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
