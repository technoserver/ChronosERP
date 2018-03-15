
package com.chronos.modelo.view;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class ViewFinMovimentoCaixaBanco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "ID")
    private String id;
    @Column(name = "ID_CONTA_CAIXA")
    private Integer idContaCaixa;
    @Column(name = "NOME_CONTA_CAIXA")
    private String nomeContaCaixa;
    @Column(name = "NOME_PESSOA")
    private String nomePessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_LANCAMENTO")
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PAGO_RECEBIDO")
    private Date dataPagoRecebido;
    @Column(name = "HISTORICO")
    private String historico;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Column(name = "DESCRICAO_DOCUMENTO_ORIGEM")
    private String descricaoDocumentoOrigem;
    @Column(name = "OPERACAO")
    private String operacao;

    public ViewFinMovimentoCaixaBanco() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdContaCaixa() {
        return idContaCaixa;
    }

    public void setIdContaCaixa(Integer idContaCaixa) {
        this.idContaCaixa = idContaCaixa;
    }

    public String getNomeContaCaixa() {
        return nomeContaCaixa;
    }

    public void setNomeContaCaixa(String nomeContaCaixa) {
        this.nomeContaCaixa = nomeContaCaixa;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public Date getDataPagoRecebido() {
        return dataPagoRecebido;
    }

    public void setDataPagoRecebido(Date dataPagoRecebido) {
        this.dataPagoRecebido = dataPagoRecebido;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricaoDocumentoOrigem() {
        return descricaoDocumentoOrigem;
    }

    public void setDescricaoDocumentoOrigem(String descricaoDocumentoOrigem) {
        this.descricaoDocumentoOrigem = descricaoDocumentoOrigem;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewFinMovimentoCaixaBanco)) return false;

        ViewFinMovimentoCaixaBanco that = (ViewFinMovimentoCaixaBanco) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
