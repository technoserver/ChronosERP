package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@Entity
@Table(name = "NFE_DETALHE_IMPOSTO_COFINS")
public class NfeDetalheImpostoCofins implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CST_COFINS")
    private String cstCofins;
    @Column(name = "QUANTIDADE_VENDIDA")
    private BigDecimal quantidadeVendida;
    @Column(name = "BASE_CALCULO_COFINS")
    private BigDecimal baseCalculoCofins;
    @Column(name = "ALIQUOTA_COFINS_PERCENTUAL")
    private BigDecimal aliquotaCofinsPercentual;
    @Column(name = "ALIQUOTA_COFINS_REAIS")
    private BigDecimal aliquotaCofinsReais;
    @Column(name = "VALOR_COFINS")
    private BigDecimal valorCofins;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetalheImpostoCofins() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCstCofins() {
        return cstCofins;
    }

    public void setCstCofins(String cstCofins) {
        this.cstCofins = cstCofins;
    }

    public BigDecimal getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(BigDecimal quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }

    public BigDecimal getBaseCalculoCofins() {
        return Optional.ofNullable(baseCalculoCofins).orElse(BigDecimal.ZERO);
    }

    public void setBaseCalculoCofins(BigDecimal baseCalculoCofins) {
        this.baseCalculoCofins = baseCalculoCofins;
    }

    public BigDecimal getAliquotaCofinsPercentual() {
        return aliquotaCofinsPercentual;
    }

    public void setAliquotaCofinsPercentual(BigDecimal aliquotaCofinsPercentual) {
        this.aliquotaCofinsPercentual = aliquotaCofinsPercentual;
    }

    public BigDecimal getAliquotaCofinsReais() {
        return aliquotaCofinsReais;
    }

    public void setAliquotaCofinsReais(BigDecimal aliquotaCofinsReais) {
        this.aliquotaCofinsReais = aliquotaCofinsReais;
    }

    public BigDecimal getValorCofins() {
        return Optional.ofNullable(valorCofins).orElse(BigDecimal.ZERO);
    }

    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

}
