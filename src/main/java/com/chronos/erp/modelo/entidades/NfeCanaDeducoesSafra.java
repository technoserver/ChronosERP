
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "NFE_CANA_DEDUCOES_SAFRA")
public class NfeCanaDeducoesSafra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DECRICAO")
    private String decricao;
    @Column(name = "VALOR_DEDUCAO")
    private BigDecimal valorDeducao;
    @Column(name = "VALOR_FORNECIMENTO")
    private BigDecimal valorFornecimento;
    @Column(name = "VALOR_TOTAL_DEDUCAO")
    private BigDecimal valorTotalDeducao;
    @Column(name = "VALOR_LIQUIDO_FORNECIMENTO")
    private BigDecimal valorLiquidoFornecimento;
    @JoinColumn(name = "ID_NFE_CANA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCana nfeCana;

    public NfeCanaDeducoesSafra() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDecricao() {
        return decricao;
    }

    public void setDecricao(String decricao) {
        this.decricao = decricao;
    }

    public BigDecimal getValorDeducao() {
        return valorDeducao;
    }

    public void setValorDeducao(BigDecimal valorDeducao) {
        this.valorDeducao = valorDeducao;
    }

    public BigDecimal getValorFornecimento() {
        return valorFornecimento;
    }

    public void setValorFornecimento(BigDecimal valorFornecimento) {
        this.valorFornecimento = valorFornecimento;
    }

    public BigDecimal getValorTotalDeducao() {
        return valorTotalDeducao;
    }

    public void setValorTotalDeducao(BigDecimal valorTotalDeducao) {
        this.valorTotalDeducao = valorTotalDeducao;
    }

    public BigDecimal getValorLiquidoFornecimento() {
        return valorLiquidoFornecimento;
    }

    public void setValorLiquidoFornecimento(BigDecimal valorLiquidoFornecimento) {
        this.valorLiquidoFornecimento = valorLiquidoFornecimento;
    }

    public NfeCana getNfeCana() {
        return nfeCana;
    }

    public void setNfeCana(NfeCana nfeCana) {
        this.nfeCana = nfeCana;
    }


}
