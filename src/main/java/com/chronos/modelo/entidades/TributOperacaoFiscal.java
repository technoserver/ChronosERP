
package com.chronos.modelo.entidades;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "TRIBUT_OPERACAO_FISCAL")
public class TributOperacaoFiscal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotBlank
    @Column(name = "DESCRICAO")
    private String descricao;
    @NotBlank
    @Column(name = "DESCRICAO_NA_NF")
    private String descricaoNaNf;
    @Column(name = "CFOP")
    @Min(message = "CFOP invalido", value = 1000)
    @Max(message = "CFOP invalido", value = 9999)
    private Integer cfop;
    @Column(name = "estoque")
    private Boolean estoque;
    @Column(name = "estoque_verificado")
    private Boolean estoqueVerificado;
    @Column(name = "contabilidade")
    private Boolean contabilidade;
    @Column(name = "obrigacao_fiscal")
    private Boolean obrigacaoFiscal;
    @Column(name = "destaca_ipi")
    private Boolean destacaIpi;
    @Column(name = "destaca_pis_cofins")
    private Boolean destacaPisCofins;
    @Column(name = "calculo_inss")
    private Boolean calculoInss;
    @Column(name = "calculo_issqn")
    private Boolean calculoIssqn;
    @Column(name = "calculo_pis_cofins_retido")
    private Boolean calculoPisCofinsRetido;
    @Column(name = "calculo_irrf")
    private Boolean calculoIrrf;
    @Column(name = "OBSERVACAO")
    private String observacao;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tributOperacaoFiscal", cascade = CascadeType.ALL)
    private TributPisCodApuracao tributPisCodApuracao;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tributOperacaoFiscal", cascade = CascadeType.ALL)
    private TributCofinsCodApuracao tributCofinsCodApuracao;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tributOperacaoFiscal", cascade = CascadeType.ALL)
    private TributIpiDipi tributIpiDipi;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tributOperacaoFiscal", cascade = CascadeType.ALL)
    private TributIss tributIss;

    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    private Empresa empresa;
   
    
    public TributOperacaoFiscal() {
    }

    public TributOperacaoFiscal(Integer id, String descricao, Integer cfop, Boolean obrigacaoFiscal, Boolean destacaIpi, Boolean destacaPisCofins, Boolean calculoIssqn) {
        this.id = id;
        this.descricao = descricao;
        this.cfop = cfop;
        this.obrigacaoFiscal = obrigacaoFiscal;
        this.destacaIpi = destacaIpi;
        this.destacaPisCofins = destacaPisCofins;
        this.calculoIssqn = calculoIssqn;
    }

    public TributOperacaoFiscal(Integer id) {
        this.id = id;

    }

    public TributOperacaoFiscal(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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

    public String getDescricaoNaNf() {
        return descricaoNaNf;
    }

    public void setDescricaoNaNf(String descricaoNaNf) {
        this.descricaoNaNf = descricaoNaNf;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    public Boolean getEstoque() {
        return estoque;
    }

    public void setEstoque(Boolean estoque) {
        this.estoque = estoque;
    }

    public Boolean getEstoqueVerificado() {
        return estoqueVerificado;
    }

    public void setEstoqueVerificado(Boolean estoqueVerificado) {
        this.estoqueVerificado = estoqueVerificado;
    }

    public Boolean getContabilidade() {
        return contabilidade;
    }

    public void setContabilidade(Boolean contabilidade) {
        this.contabilidade = contabilidade;
    }

    public Boolean getObrigacaoFiscal() {
        return obrigacaoFiscal;
    }

    public void setObrigacaoFiscal(Boolean obrigacaoFiscal) {
        this.obrigacaoFiscal = obrigacaoFiscal;
    }

    public Boolean getDestacaIpi() {
        return destacaIpi;
    }

    public void setDestacaIpi(Boolean destacaIpi) {
        this.destacaIpi = destacaIpi;
    }

    public Boolean getDestacaPisCofins() {
        return destacaPisCofins;
    }

    public void setDestacaPisCofins(Boolean destacaPisCofins) {
        this.destacaPisCofins = destacaPisCofins;
    }

    public Boolean getCalculoInss() {
        return calculoInss;
    }

    public void setCalculoInss(Boolean calculoInss) {
        this.calculoInss = calculoInss;
    }

    public Boolean getCalculoIssqn() {
        return calculoIssqn;
    }

    public void setCalculoIssqn(Boolean calculoIssqn) {
        this.calculoIssqn = calculoIssqn;
    }

    public Boolean getCalculoPisCofinsRetido() {
        return calculoPisCofinsRetido;
    }

    public void setCalculoPisCofinsRetido(Boolean calculoPisCofinsRetido) {
        this.calculoPisCofinsRetido = calculoPisCofinsRetido;
    }

    public Boolean getCalculoIrrf() {
        return calculoIrrf;
    }

    public void setCalculoIrrf(Boolean calculoIrrf) {
        this.calculoIrrf = calculoIrrf;
    }

    public TributPisCodApuracao getTributPisCodApuracao() {
        return tributPisCodApuracao;
    }

    public void setTributPisCodApuracao(TributPisCodApuracao tributPisCodApuracao) {
        this.tributPisCodApuracao = tributPisCodApuracao;
    }

    public TributCofinsCodApuracao getTributCofinsCodApuracao() {
        return tributCofinsCodApuracao;
    }

    public void setTributCofinsCodApuracao(TributCofinsCodApuracao tributCofinsCodApuracao) {
        this.tributCofinsCodApuracao = tributCofinsCodApuracao;
    }

    public TributIpiDipi getTributIpiDipi() {
        return tributIpiDipi;
    }

    public void setTributIpiDipi(TributIpiDipi tributIpiDipi) {
        this.tributIpiDipi = tributIpiDipi;
    }

    public TributIss getTributIss() {
        return tributIss;
    }

    public void setTributIss(TributIss tributIss) {
        this.tributIss = tributIss;
    }

    @Override
   public String toString() {
      return descricao ;
   }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TributOperacaoFiscal other = (TributOperacaoFiscal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

  

}
