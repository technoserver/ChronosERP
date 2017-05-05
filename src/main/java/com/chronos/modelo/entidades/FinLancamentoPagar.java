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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "FIN_LANCAMENTO_PAGAR")
public class FinLancamentoPagar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PAGAMENTO_COMPARTILHADO")
    private String pagamentoCompartilhado;
    @Column(name = "QUANTIDADE_PARCELA")
    @Min(1)
    private Integer quantidadeParcela;
    @NotNull
    @Column(name = "VALOR_TOTAL")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorTotal;
    @NotNull
    @Column(name = "VALOR_A_PAGAR")
    @DecimalMin(value = "0.01", message = "O valor  deve ser maior que R$0,01")
    @DecimalMax(value = "9999999.99", message = "O valor  deve ser menor que R$9.999.999,99")
    private BigDecimal valorAPagar;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO")
    private Date dataLancamento;
    @NotNull
    @NotBlank
    @Column(name = "NUMERO_DOCUMENTO")
    private String numeroDocumento;
    @Column(name = "IMAGEM_DOCUMENTO")
    private String imagemDocumento;
    @Temporal(TemporalType.DATE)
    @Column(name = "PRIMEIRO_VENCIMENTO")
    private Date primeiroVencimento;
    @Column(name = "CODIGO_MODULO_LCTO")
    private String codigoModuloLcto;
    @Column(name = "INTERVALO_ENTRE_PARCELAS")
    private Integer intervaloEntreParcelas;
    @Column(name = "MESCLADO_PARA")
    private Integer mescladoPara;
    @NotNull
    @JoinColumn(name = "ID_FIN_DOCUMENTO_ORIGEM", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FinDocumentoOrigem finDocumentoOrigem;
    @NotNull
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Fornecedor fornecedor;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finLancamentoPagar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinParcelaPagar> listaFinParcelaPagar;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finLancamentoPagar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinLctoPagarNtFinanceira> listaFinLctoPagarNtFinanceira;

    public FinLancamentoPagar() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPagamentoCompartilhado() {
        return pagamentoCompartilhado;
    }
    /**
     *  S=SIM | N=NÃO
     * @param pagamentoCompartilhado 
     */
    public void setPagamentoCompartilhado(String pagamentoCompartilhado) {
        this.pagamentoCompartilhado = pagamentoCompartilhado;
    }
    /**
     *  S=SIM | N=NÃO
     * @return 
     */
    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorAPagar() {
        return valorAPagar;
    }

    public void setValorAPagar(BigDecimal valorAPagar) {
        this.valorAPagar = valorAPagar;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
    /**
     * Número do documento que deu origem ao lançamento
     * @return 
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    /**
     * Número do documento que deu origem ao lançamento
     * @param numeroDocumento 
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    /**
     * Armazena o caminho da imagem do documento, caso seja scaneado
     * @return 
     */
    public String getImagemDocumento() {
        return imagemDocumento;
    }
    /**
     * Armazena o caminho da imagem do documento, caso seja scaneado
     * @param imagemDocumento 
     */
    public void setImagemDocumento(String imagemDocumento) {
        this.imagemDocumento = imagemDocumento;
    }

    public Date getPrimeiroVencimento() {
        return primeiroVencimento;
    }

    public void setPrimeiroVencimento(Date primeiroVencimento) {
        this.primeiroVencimento = primeiroVencimento;
    }
    /**
     * Código do módulo que realizou o lançamento
     * @return 
     */
    public String getCodigoModuloLcto() {
        return codigoModuloLcto;
    }
    /**
     * Código do módulo que realizou o lançamento
     * @param codigoModuloLcto 
     */
    public void setCodigoModuloLcto(String codigoModuloLcto) {
        this.codigoModuloLcto = codigoModuloLcto;
    }
    /**
     * Para calcular o intervalo entre as parcelas
     * @return 
     */
    public Integer getIntervaloEntreParcelas() {
        return intervaloEntreParcelas;
    }
    /**
     * Para calcular o intervalo entre as parcelas
     * @param intervaloEntreParcelas 
     */
    public void setIntervaloEntreParcelas(Integer intervaloEntreParcelas) {
        this.intervaloEntreParcelas = intervaloEntreParcelas;
    }
    /**
     * Armazena o ID do lançamento que foi gerado para mesclar esse registro com outro(s)
     * @return 
     */
    public Integer getMescladoPara() {
        return mescladoPara;
    }
    /**
     * Armazena o ID do lançamento que foi gerado para mesclar esse registro com outro(s)
     * @param mescladoPara 
     */
    public void setMescladoPara(Integer mescladoPara) {
        this.mescladoPara = mescladoPara;
    }

    public FinDocumentoOrigem getFinDocumentoOrigem() {
        return finDocumentoOrigem;
    }

    public void setFinDocumentoOrigem(FinDocumentoOrigem finDocumentoOrigem) {
        this.finDocumentoOrigem = finDocumentoOrigem;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Set<FinParcelaPagar> getListaFinParcelaPagar() {
        return listaFinParcelaPagar;
    }

    public void setListaFinParcelaPagar(Set<FinParcelaPagar> listaFinParcelaPagar) {
        this.listaFinParcelaPagar = listaFinParcelaPagar;
    }

    public Set<FinLctoPagarNtFinanceira> getListaFinLctoPagarNtFinanceira() {
        return listaFinLctoPagarNtFinanceira;
    }

    public void setListaFinLctoPagarNtFinanceira(Set<FinLctoPagarNtFinanceira> listaFinLctoPagarNtFinanceira) {
        this.listaFinLctoPagarNtFinanceira = listaFinLctoPagarNtFinanceira;
    }

    @Override
    public String toString() {
        return numeroDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final FinLancamentoPagar other = (FinLancamentoPagar) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
