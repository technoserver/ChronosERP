/*
 * The MIT License
 *
 * Copyright: Copyright (C) 2014 chronosinfo.COM
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
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "FIN_CHEQUE_RECEBIDO")
public class FinChequeRecebido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CPF_CNPJ")
    private String cpfCnpj;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "CODIGO_BANCO")
    private String codigoBanco;
    @Column(name = "CODIGO_AGENCIA")
    private String codigoAgencia;
    @Column(name = "CONTA")
    private String conta;
    @Column(name = "NUMERO")
    private Integer numero;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO")
    private Date dataEmissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "BOM_PARA")
    private Date bomPara;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_COMPENSACAO")
    private Date dataCompensacao;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Temporal(TemporalType.DATE)
    @Column(name = "CUSTODIA_DATA")
    private Date custodiaData;
    @Column(name = "CUSTODIA_TARIFA")
    private BigDecimal custodiaTarifa;
    @Column(name = "CUSTODIA_COMISSAO")
    private BigDecimal custodiaComissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DESCONTO_DATA")
    private Date descontoData;
    @Column(name = "DESCONTO_TARIFA")
    private BigDecimal descontoTarifa;
    @Column(name = "DESCONTO_COMISSAO")
    private BigDecimal descontoComissao;
    @Column(name = "VALOR_RECEBIDO")
    private BigDecimal valorRecebido;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaCaixa contaCaixa;
    @Transient
    private String historico;

    public FinChequeRecebido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public String getCodigoAgencia() {
        return codigoAgencia;
    }

    public void setCodigoAgencia(String codigoAgencia) {
        this.codigoAgencia = codigoAgencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getBomPara() {
        return bomPara;
    }

    public void setBomPara(Date bomPara) {
        this.bomPara = bomPara;
    }

    public Date getDataCompensacao() {
        return dataCompensacao;
    }

    public void setDataCompensacao(Date dataCompensacao) {
        this.dataCompensacao = dataCompensacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getCustodiaData() {
        return custodiaData;
    }

    public void setCustodiaData(Date custodiaData) {
        this.custodiaData = custodiaData;
    }

    public BigDecimal getCustodiaTarifa() {
        return custodiaTarifa;
    }

    public void setCustodiaTarifa(BigDecimal custodiaTarifa) {
        this.custodiaTarifa = custodiaTarifa;
    }

    public BigDecimal getCustodiaComissao() {
        return custodiaComissao;
    }

    public void setCustodiaComissao(BigDecimal custodiaComissao) {
        this.custodiaComissao = custodiaComissao;
    }

    public Date getDescontoData() {
        return descontoData;
    }

    public void setDescontoData(Date descontoData) {
        this.descontoData = descontoData;
    }

    public BigDecimal getDescontoTarifa() {
        return descontoTarifa;
    }

    public void setDescontoTarifa(BigDecimal descontoTarifa) {
        this.descontoTarifa = descontoTarifa;
    }

    public BigDecimal getDescontoComissao() {
        return descontoComissao;
    }

    public void setDescontoComissao(BigDecimal descontoComissao) {
        this.descontoComissao = descontoComissao;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }


    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final FinChequeRecebido other = (FinChequeRecebido) obj;
        return Objects.equals(this.id, other.id);
    }


}
