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
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "FIN_PARCELA_RECEBIMENTO")
public class FinParcelaRecebimento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_RECEBIMENTO")
    private Date dataRecebimento;
    @Column(name = "TAXA_JURO")
    private BigDecimal taxaJuro;
    @Column(name = "TAXA_MULTA")
    private BigDecimal taxaMulta;
    @Column(name = "TAXA_DESCONTO")
    private BigDecimal taxaDesconto;
    @Column(name = "VALOR_JURO")
    private BigDecimal valorJuro;
    @Column(name = "VALOR_MULTA")
    private BigDecimal valorMulta;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_RECEBIDO")
    private BigDecimal valorRecebido;
    @Column(name = "HISTORICO")
    private String historico;
    @JoinColumn(name = "ID_FIN_PARCELA_RECEBER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinParcelaReceber finParcelaReceber;
    @JoinColumn(name = "ID_FIN_TIPO_RECEBIMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinTipoRecebimento finTipoRecebimento;
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaCaixa contaCaixa;
    @JoinColumn(name = "ID_FIN_CHEQUE_RECEBIDO", referencedColumnName = "ID")
    @ManyToOne
    private FinChequeRecebido finChequeRecebido;

    public FinParcelaRecebimento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public BigDecimal getTaxaJuro() {
        return taxaJuro;
    }

    public void setTaxaJuro(BigDecimal taxaJuro) {
        this.taxaJuro = taxaJuro;
    }

    public BigDecimal getTaxaMulta() {
        return taxaMulta;
    }

    public void setTaxaMulta(BigDecimal taxaMulta) {
        this.taxaMulta = taxaMulta;
    }

    public BigDecimal getTaxaDesconto() {
        return taxaDesconto;
    }

    public void setTaxaDesconto(BigDecimal taxaDesconto) {
        this.taxaDesconto = taxaDesconto;
    }

    public BigDecimal getValorJuro() {
        return valorJuro;
    }

    public void setValorJuro(BigDecimal valorJuro) {
        this.valorJuro = valorJuro;
    }

    public BigDecimal getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(BigDecimal valorMulta) {
        this.valorMulta = valorMulta;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public FinParcelaReceber getFinParcelaReceber() {
        return finParcelaReceber;
    }

    public void setFinParcelaReceber(FinParcelaReceber finParcelaReceber) {
        this.finParcelaReceber = finParcelaReceber;
    }

    public FinTipoRecebimento getFinTipoRecebimento() {
        return finTipoRecebimento;
    }

    public void setFinTipoRecebimento(FinTipoRecebimento finTipoRecebimento) {
        this.finTipoRecebimento = finTipoRecebimento;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public FinChequeRecebido getFinChequeRecebido() {
        return finChequeRecebido;
    }

    public void setFinChequeRecebido(FinChequeRecebido finChequeRecebido) {
        this.finChequeRecebido = finChequeRecebido;
    }


    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final FinParcelaRecebimento other = (FinParcelaRecebimento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}
