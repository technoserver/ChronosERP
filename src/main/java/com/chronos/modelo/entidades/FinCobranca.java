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
 * @author Claudio de Barros (chronosinfo.com)
 * @version 2.0
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "FIN_COBRANCA")
public class FinCobranca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CONTATO")
    private Date dataContato;
    @Column(name = "HORA_CONTATO")
    private String horaContato;
    @Column(name = "TELEFONE_CONTATO")
    private String telefoneContato;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_ACERTO_PAGAMENTO")
    private Date dataAcertoPagamento;
    @Column(name = "TOTAL_RECEBER_NA_DATA")
    private BigDecimal totalReceberNaData;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "finCobranca", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FinCobrancaParcelaReceber> listaFinCobrancaParcelaReceber;
    @Transient
    private BigDecimal totalJuros;
    @Transient
    private BigDecimal totalMulta;
    @Transient
    private BigDecimal totalAtrasado;

    public FinCobranca() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataContato() {
        return dataContato;
    }

    public void setDataContato(Date dataContato) {
        this.dataContato = dataContato;
    }

    public String getHoraContato() {
        return horaContato;
    }

    public void setHoraContato(String horaContato) {
        this.horaContato = horaContato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public Date getDataAcertoPagamento() {
        return dataAcertoPagamento;
    }

    public void setDataAcertoPagamento(Date dataAcertoPagamento) {
        this.dataAcertoPagamento = dataAcertoPagamento;
    }

    public BigDecimal getTotalReceberNaData() {
        return totalReceberNaData;
    }

    public void setTotalReceberNaData(BigDecimal totalReceberNaData) {
        this.totalReceberNaData = totalReceberNaData;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
  

    public Set<FinCobrancaParcelaReceber> getListaFinCobrancaParcelaReceber() {
        return listaFinCobrancaParcelaReceber;
    }

    public void setListaFinCobrancaParcelaReceber(Set<FinCobrancaParcelaReceber> listaFinCobrancaParcelaReceber) {
        this.listaFinCobrancaParcelaReceber = listaFinCobrancaParcelaReceber;
    }

    public BigDecimal getTotalJuros() {
        return totalJuros;
    }

    public void setTotalJuros(BigDecimal totalJuros) {
        this.totalJuros = totalJuros;
    }

    public BigDecimal getTotalAtrasado() {
        return totalAtrasado;
    }

    public void setTotalAtrasado(BigDecimal totalAtrasado) {
        this.totalAtrasado = totalAtrasado;
    }

    public BigDecimal getTotalMulta() {
        return totalMulta;
    }

    public void setTotalMulta(BigDecimal totalMulta) {
        this.totalMulta = totalMulta;
    }

    @Override
    public String toString() {
        return "FinCobranca{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final FinCobranca other = (FinCobranca) obj;
        return Objects.equals(this.id, other.id);
    }
    
    

}
