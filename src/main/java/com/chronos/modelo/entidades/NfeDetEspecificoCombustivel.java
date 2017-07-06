package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NFE_DET_ESPECIFICO_COMBUSTIVEL")
public class NfeDetEspecificoCombustivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO_ANP")
    private Integer codigoAnp;
    @Column(name = "PERCENTUAL_GAS_NATURAL")
    private BigDecimal percentualGasNatural;
    @Column(name = "CODIF")
    private String codif;
    @Column(name = "QUANTIDADE_TEMP_AMBIENTE")
    private BigDecimal quantidadeTempAmbiente;
    @Column(name = "UF_CONSUMO")
    private String ufConsumo;
    @Column(name = "BASE_CALCULO_CIDE")
    private BigDecimal baseCalculoCide;
    @Column(name = "ALIQUOTA_CIDE")
    private BigDecimal aliquotaCide;
    @Column(name = "VALOR_CIDE")
    private BigDecimal valorCide;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoCombustivel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigoAnp() {
        return codigoAnp;
    }

    public void setCodigoAnp(Integer codigoAnp) {
        this.codigoAnp = codigoAnp;
    }

    public BigDecimal getPercentualGasNatural() {
        return percentualGasNatural;
    }

    public void setPercentualGasNatural(BigDecimal percentualGasNatural) {
        this.percentualGasNatural = percentualGasNatural;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public BigDecimal getQuantidadeTempAmbiente() {
        return quantidadeTempAmbiente;
    }

    public void setQuantidadeTempAmbiente(BigDecimal quantidadeTempAmbiente) {
        this.quantidadeTempAmbiente = quantidadeTempAmbiente;
    }

    public String getUfConsumo() {
        return ufConsumo;
    }

    public void setUfConsumo(String ufConsumo) {
        this.ufConsumo = ufConsumo;
    }

    public BigDecimal getBaseCalculoCide() {
        return baseCalculoCide;
    }

    public void setBaseCalculoCide(BigDecimal baseCalculoCide) {
        this.baseCalculoCide = baseCalculoCide;
    }

    public BigDecimal getAliquotaCide() {
        return aliquotaCide;
    }

    public void setAliquotaCide(BigDecimal aliquotaCide) {
        this.aliquotaCide = aliquotaCide;
    }

    public BigDecimal getValorCide() {
        return valorCide;
    }

    public void setValorCide(BigDecimal valorCide) {
        this.valorCide = valorCide;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

}
