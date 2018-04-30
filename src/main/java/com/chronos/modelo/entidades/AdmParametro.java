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
* @author John Vanderson M lima (chronosinfo.com)
* @version 2.0
*/
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "ADM_PARAMETRO")
public class AdmParametro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FIN_PARCELA_ABERTO")
    private Integer finParcelaAberto;
    @Column(name = "FIN_PARCELA_QUITADO")
    private Integer finParcelaQuitado;
    @Column(name = "FIN_PARCELA_QUITADO_PARCIAL")
    private Integer finParcelaQuitadoParcial;
    @Column(name = "FIN_TIPO_RECEBIMENTO_EDI")
    private Integer finTipoRecebimentoEdi;
    @Column(name = "COMPRA_FIN_DOC_ORIGEM")
    private Integer compraFinDocOrigem;
    @Column(name = "COMPRA_CONTA_CAIXA")
    private Integer compraContaCaixa;
    @Column(name = "tribut_operacao_fiscal_padrao")
    private Integer tributOperacaoFiscalPadrao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;

    public AdmParametro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFinParcelaAberto() {
        return finParcelaAberto;
    }

    public void setFinParcelaAberto(Integer finParcelaAberto) {
        this.finParcelaAberto = finParcelaAberto;
    }

    public Integer getFinParcelaQuitado() {
        return finParcelaQuitado;
    }

    public void setFinParcelaQuitado(Integer finParcelaQuitado) {
        this.finParcelaQuitado = finParcelaQuitado;
    }

    public Integer getFinParcelaQuitadoParcial() {
        return finParcelaQuitadoParcial;
    }

    public void setFinParcelaQuitadoParcial(Integer finParcelaQuitadoParcial) {
        this.finParcelaQuitadoParcial = finParcelaQuitadoParcial;
    }

    public Integer getFinTipoRecebimentoEdi() {
        return finTipoRecebimentoEdi;
    }

    public void setFinTipoRecebimentoEdi(Integer finTipoRecebimentoEdi) {
        this.finTipoRecebimentoEdi = finTipoRecebimentoEdi;
    }

    public Integer getCompraFinDocOrigem() {
        return compraFinDocOrigem;
    }

    public void setCompraFinDocOrigem(Integer compraFinDocOrigem) {
        this.compraFinDocOrigem = compraFinDocOrigem;
    }

    public Integer getCompraContaCaixa() {
        return compraContaCaixa;
    }

    public void setCompraContaCaixa(Integer compraContaCaixa) {
        this.compraContaCaixa = compraContaCaixa;
    }

    public Integer getTributOperacaoFiscalPadrao() {
        return tributOperacaoFiscalPadrao;
    }

    public void setTributOperacaoFiscalPadrao(Integer tributOperacaoFiscalPadrao) {
        this.tributOperacaoFiscalPadrao = tributOperacaoFiscalPadrao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final AdmParametro other = (AdmParametro) obj;
        return Objects.equals(this.id, other.id);
    }

    
}
