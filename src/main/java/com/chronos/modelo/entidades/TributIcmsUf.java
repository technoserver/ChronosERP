package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "TRIBUT_ICMS_UF")
public class TributIcmsUf implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "UF_DESTINO")
    private String ufDestino;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "CSOSN_B")
    private String csosnB;
    @Column(name = "CST_B")
    private String cstB;
    @Column(name = "MODALIDADE_BC")
    private String modalidadeBc;
    @Column(name = "ALIQUOTA")
    private BigDecimal aliquota;
    @Column(name = "VALOR_PAUTA")
    private BigDecimal valorPauta;
    @Column(name = "VALOR_PRECO_MAXIMO")
    private BigDecimal valorPrecoMaximo;
    @Column(name = "MVA")
    private BigDecimal mva;
    @Column(name = "PORCENTO_BC")
    private BigDecimal porcentoBc;
    @Column(name = "MODALIDADE_BC_ST")
    private String modalidadeBcSt;
    @Column(name = "ALIQUOTA_INTERNA_ST")
    private BigDecimal aliquotaInternaSt;
    @Column(name = "ALIQUOTA_INTERESTADUAL_ST")
    private BigDecimal aliquotaInterestadualSt;
    @Column(name = "PORCENTO_BC_ST")
    private BigDecimal porcentoBcSt;
    @Column(name = "ALIQUOTA_ICMS_ST")
    private BigDecimal aliquotaIcmsSt;
    @Column(name = "VALOR_PAUTA_ST")
    private BigDecimal valorPautaSt;
    @Column(name = "VALOR_PRECO_MAXIMO_ST")
    private BigDecimal valorPrecoMaximoSt;


    public TributIcmsUf() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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



    @Override
    public String toString() {
        return "TributIcmsUf{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final TributIcmsUf other = (TributIcmsUf) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
