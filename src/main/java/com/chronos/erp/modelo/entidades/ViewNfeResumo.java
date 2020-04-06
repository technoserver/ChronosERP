package com.chronos.erp.modelo.entidades;

import com.chronos.erp.modelo.enuns.StatusTransmissao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "view_nfe_resumo")
public class ViewNfeResumo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_empresa")
    private Integer idempresa;
    @Column(name = "numero")
    private String numero;
    @Column(name = "codigo_modelo")
    private String codigoModelo;
    @Column(name = "data_hora_emissao")
    private Date dataHoraEmissao;
    @Column(name = "chave_acesso")
    private String chaveAcesso;
    @Column(name = "digito_chave_acesso")
    private String digitoChaveAcesso;
    @Column(name = "chave_acesso_completa")
    private String chaveAcessoCompleta;
    @Column(name = "destinatario")
    private String destinatario;
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Column(name = "status_nota")
    private Integer statusNota;
    @Column(name = "finalidade_emissao")
    private Integer finalidadeEmissao;
    @Column(name = "tipo_operacao")
    private Integer tipoOperacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoModelo() {
        return codigoModelo;
    }

    public void setCodigoModelo(String codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    public Date getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    public void setDataHoraEmissao(Date dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getDigitoChaveAcesso() {
        return digitoChaveAcesso;
    }

    public void setDigitoChaveAcesso(String digitoChaveAcesso) {
        this.digitoChaveAcesso = digitoChaveAcesso;
    }

    public String getChaveAcessoCompleta() {
        return chaveAcessoCompleta;
    }

    public void setChaveAcessoCompleta(String chaveAcessoCompleta) {
        this.chaveAcessoCompleta = chaveAcessoCompleta;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getStatusNota() {
        return statusNota;
    }

    public void setStatusNota(Integer statusNota) {
        this.statusNota = statusNota;
    }

    public Integer getFinalidadeEmissao() {
        return finalidadeEmissao;
    }

    public void setFinalidadeEmissao(Integer finalidadeEmissao) {
        this.finalidadeEmissao = finalidadeEmissao;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }

    public Integer getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(Integer tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public boolean isPodeExcluir() {
        StatusTransmissao status = StatusTransmissao.valueOfCodigo(statusNota);
        return (status != StatusTransmissao.AUTORIZADA) && (status != StatusTransmissao.CANCELADA) && (status != StatusTransmissao.ENVIADA);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewNfeResumo that = (ViewNfeResumo) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
