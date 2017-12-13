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
 * @author Claudio de Barros (T2Ti.com)
 * @version 2.0
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "FIN_PARCELA_RECEBER")
public class FinParcelaReceber implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO_PARCELA")
    private Integer numeroParcela;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO")
    private Date dataEmissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO")
    private Date dataVencimento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DESCONTO_ATE")
    private Date descontoAte;
    @Column(name = "VALOR")
    private BigDecimal valor;
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
    @Column(name = "EMITIU_BOLETO")
    private String emitiuBoleto;
    @Column(name = "BOLETO_NOSSO_NUMERO")
    private String boletoNossoNumero;
    @JoinColumn(name = "ID_FIN_LANCAMENTO_RECEBER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinLancamentoReceber finLancamentoReceber;
    @JoinColumn(name = "ID_FIN_STATUS_PARCELA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinStatusParcela finStatusParcela;
    @JoinColumn(name = "ID_CONTA_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaCaixa contaCaixa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finParcelaReceber", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinParcelaRecebimento> listaFinParcelaRecebimento;
    @Transient
    private String numParcelaDocumento;
    @Transient
    private BigDecimal valorAPagar;

    public FinParcelaReceber() {
    }

    public FinParcelaReceber(Integer id) {
        this.id = id;
    }

    public FinParcelaReceber(Integer id, Integer idfinLancamentoReceber) {
        this.id = id;
        this.finLancamentoReceber = new FinLancamentoReceber(idfinLancamentoReceber);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDescontoAte() {
        return descontoAte;
    }

    public void setDescontoAte(Date descontoAte) {
        this.descontoAte = descontoAte;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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

    public String getEmitiuBoleto() {
        return emitiuBoleto;
    }

    public void setEmitiuBoleto(String emitiuBoleto) {
        this.emitiuBoleto = emitiuBoleto;
    }

    public String getBoletoNossoNumero() {
        return boletoNossoNumero;
    }

    public void setBoletoNossoNumero(String boletoNossoNumero) {
        this.boletoNossoNumero = boletoNossoNumero;
    }

    public FinLancamentoReceber getFinLancamentoReceber() {
        return finLancamentoReceber;
    }

    public void setFinLancamentoReceber(FinLancamentoReceber finLancamentoReceber) {
        this.finLancamentoReceber = finLancamentoReceber;
    }

    public FinStatusParcela getFinStatusParcela() {
        return finStatusParcela;
    }

    public void setFinStatusParcela(FinStatusParcela finStatusParcela) {
        this.finStatusParcela = finStatusParcela;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public Set<FinParcelaRecebimento> getListaFinParcelaRecebimento() {
        return listaFinParcelaRecebimento;
    }

    public void setListaFinParcelaRecebimento(Set<FinParcelaRecebimento> listaFinParcelaRecebimento) {
        this.listaFinParcelaRecebimento = listaFinParcelaRecebimento;
    }

    public String getNumParcelaDocumento() {
        return finLancamentoReceber.getNumeroDocumento() + "/" + numeroParcela;
    }

    public void setNumParcelaDocumento(String numParcelaDocumento) {
        this.numParcelaDocumento = numParcelaDocumento;
    }

    public BigDecimal getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public Calendar getDataRecebimento() {
        Calendar dataRecebimento = Calendar.getInstance();
        dataRecebimento.setTime(new Date());
        return dataRecebimento;
    }
    
    public Calendar getVencimento() {
        Calendar vencimento= Calendar.getInstance();
        vencimento.setTime(dataVencimento);
        return vencimento;
    }

    @Override
    public String toString() {
        return boletoNossoNumero;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.numeroParcela);
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
        final FinParcelaReceber other = (FinParcelaReceber) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.numeroParcela, other.numeroParcela);
    }

}
