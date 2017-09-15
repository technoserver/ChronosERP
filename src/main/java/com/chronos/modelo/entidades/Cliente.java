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
@Table(name = "CLIENTE")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DESDE")
    private Date desde;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @Column(name = "CONTA_TOMADOR")
    private String contaTomador;
    @Column(name = "GERA_FINANCEIRO")
    private String geraFinanceiro;
    @Column(name = "INDICADOR_PRECO")
    private String indicadorPreco;
    @Column(name = "PORCENTO_DESCONTO")
    private BigDecimal porcentoDesconto;
    @Column(name = "FORMA_DESCONTO")
    private String formaDesconto;
    @Column(name = "LIMITE_CREDITO")
    private BigDecimal limiteCredito;
    @Column(name = "TIPO_FRETE")
    private String tipoFrete;
    @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
    private String classificacaoContabilConta;
    @Column(name = "BLOQUEADO")
    private String bloqueado;
    @Column(name = "DIAS_BLOQUEIO")
    private Integer diasBloqueio;
    @JoinColumn(name = "ID_SITUACAO_FOR_CLI", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SituacaoForCli situacaoForCli;
    @JoinColumn(name = "ID_ATIVIDADE_FOR_CLI", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AtividadeForCli atividadeForCli;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Pessoa pessoa;
    @JoinColumn(name = "ID_OPERACAO_FISCAL", referencedColumnName = "ID")
    @ManyToOne    
    private TributOperacaoFiscal tributOperacaoFiscal;   
    @JoinColumn(name = "ID_CONVENIO", referencedColumnName = "ID")
    @ManyToOne
    private Convenio convenio;
    @JoinColumn(name = "ID_TABELA_PRECO", referencedColumnName = "ID")
    @ManyToOne
    private TabelaPreco tabelaPreco;
    @JoinColumn(name = "ID_REGIAO", referencedColumnName = "ID")
    @ManyToOne
    private Regiao regiao;

    public Cliente() {
    }

    public Cliente(Integer id,String nome) {
        this.id= id;
        this.pessoa = new Pessoa(nome);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getContaTomador() {
        return contaTomador;
    }

    public void setContaTomador(String contaTomador) {
        this.contaTomador = contaTomador;
    }

    public String getGeraFinanceiro() {
        return geraFinanceiro;
    }

    public void setGeraFinanceiro(String geraFinanceiro) {
        this.geraFinanceiro = geraFinanceiro;
    }

    public String getIndicadorPreco() {
        return indicadorPreco;
    }

    public void setIndicadorPreco(String indicadorPreco) {
        this.indicadorPreco = indicadorPreco;
    }

    public BigDecimal getPorcentoDesconto() {
        return porcentoDesconto;
    }

    public void setPorcentoDesconto(BigDecimal porcentoDesconto) {
        this.porcentoDesconto = porcentoDesconto;
    }

    public String getFormaDesconto() {
        return formaDesconto;
    }

    public void setFormaDesconto(String formaDesconto) {
        this.formaDesconto = formaDesconto;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public SituacaoForCli getSituacaoForCli() {
        return situacaoForCli;
    }

    public void setSituacaoForCli(SituacaoForCli situacaoForCli) {
        this.situacaoForCli = situacaoForCli;
    }

    public AtividadeForCli getAtividadeForCli() {
        return atividadeForCli;
    }

    public void setAtividadeForCli(AtividadeForCli atividadeForCli) {
        this.atividadeForCli = atividadeForCli;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    public String getClassificacaoContabilConta() {
        return classificacaoContabilConta;
    }

    public void setClassificacaoContabilConta(String classificacaoContabilConta) {
        this.classificacaoContabilConta = classificacaoContabilConta;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Integer getDiasBloqueio() {
        return diasBloqueio;
    }

    public void setDiasBloqueio(Integer diasBloqueio) {
        this.diasBloqueio = diasBloqueio;
    }

    public TabelaPreco getTabelaPreco() {
        return tabelaPreco;
    }

    public void setTabelaPreco(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
