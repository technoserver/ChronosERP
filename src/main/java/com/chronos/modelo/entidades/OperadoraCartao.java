/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 Chronosinfo.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: chronosinfo.com@gmail.com
*
* @author John Vanderson M Lima (chronosinfo.com)
* @version 2.0
*/
package com.chronos.modelo.entidades;


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
 //   @NotEmpty(message = "Nome Obrigatório")
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

    public OperadoraCartao() {
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
