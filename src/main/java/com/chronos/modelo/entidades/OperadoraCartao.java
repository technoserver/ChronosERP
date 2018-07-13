
package com.chronos.modelo.entidades;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "OPERADORA_CARTAO")
public class OperadoraCartao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "BANDEIRA")
    private String bandeira;
    @NotBlank(message = "Nome Obrigatório")
    @Column(name = "NOME")
    private String nome;
    @Column(name = "TAXA_ADM")
    private BigDecimal taxaAdm;
    @Column(name = "TAXA_ADM_DEBITO")
    private BigDecimal taxaAdmDebito;
    @Column(name = "VALOR_ALUGUEL_POS_PIN")
    private BigDecimal valorAluguelPosPin;
    @Column(name = "VENCIMENTO_ALUGUEL")
    private Integer vencimentoAluguel;
    @Column(name = "FONE1")
    private String fone1;
    @Column(name = "FONE2")
    private String fone2;
    @NotNull(message = "Conta Obrigatória")
    @Valid
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaCaixa contaCaixa;
    @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
    private String classificacaoContabilConta;
    @Transient
    private String nsu;

    public OperadoraCartao() {
    }

    public OperadoraCartao(Integer id, String nome, BigDecimal taxaAdm, Integer idconta) {
        this.id = id;
        this.nome = nome;
        this.taxaAdm = taxaAdm;
        this.contaCaixa = new ContaCaixa(idconta);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getTaxaAdm() {
        return taxaAdm;
    }

    public void setTaxaAdm(BigDecimal taxaAdm) {
        this.taxaAdm = taxaAdm;
    }

    public BigDecimal getTaxaAdmDebito() {
        return taxaAdmDebito;
    }

    public void setTaxaAdmDebito(BigDecimal taxaAdmDebito) {
        this.taxaAdmDebito = taxaAdmDebito;
    }

    public BigDecimal getValorAluguelPosPin() {
        return valorAluguelPosPin;
    }

    public void setValorAluguelPosPin(BigDecimal valorAluguelPosPin) {
        this.valorAluguelPosPin = valorAluguelPosPin;
    }

    public Integer getVencimentoAluguel() {
        return vencimentoAluguel;
    }

    public void setVencimentoAluguel(Integer vencimentoAluguel) {
        this.vencimentoAluguel = vencimentoAluguel;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public String getClassificacaoContabilConta() {
        return classificacaoContabilConta;
    }

    public void setClassificacaoContabilConta(String classificacaoContabilConta) {
        this.classificacaoContabilConta = classificacaoContabilConta;
    }


    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
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
        final OperadoraCartao other = (OperadoraCartao) obj;
        return Objects.equals(this.id, other.id);
    }
    
    

}
