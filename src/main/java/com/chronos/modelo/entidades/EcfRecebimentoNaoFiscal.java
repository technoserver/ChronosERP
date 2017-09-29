
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "ECF_RECEBIMENTO_NAO_FISCAL")
public class EcfRecebimentoNaoFiscal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME_CAIXA")
    private String nomeCaixa;
    @Column(name = "ID_GERADO_CAIXA")
    private Integer idGeradoCaixa;
    @Column(name = "ID_EMPRESA")
    private Integer idEmpresa;
    @Column(name = "ID_ECF_MOVIMENTO")
    private Integer idEcfMovimento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_RECEBIMENTO")
    private Date dataRecebimento;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SINCRONIZACAO")
    private Date dataSincronizacao;
    @Column(name = "HORA_SINCRONIZACAO")
    private String horaSincronizacao;

    public EcfRecebimentoNaoFiscal() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeCaixa() {
        return nomeCaixa;
    }

    public void setNomeCaixa(String nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    public Integer getIdGeradoCaixa() {
        return idGeradoCaixa;
    }

    public void setIdGeradoCaixa(Integer idGeradoCaixa) {
        this.idGeradoCaixa = idGeradoCaixa;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdEcfMovimento() {
        return idEcfMovimento;
    }

    public void setIdEcfMovimento(Integer idEcfMovimento) {
        this.idEcfMovimento = idEcfMovimento;
    }

    public Date getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(Date dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDataSincronizacao() {
        return dataSincronizacao;
    }

    public void setDataSincronizacao(Date dataSincronizacao) {
        this.dataSincronizacao = dataSincronizacao;
    }

    public String getHoraSincronizacao() {
        return horaSincronizacao;
    }

    public void setHoraSincronizacao(String horaSincronizacao) {
        this.horaSincronizacao = horaSincronizacao;
    }

    @Override
    public String toString() {
        return "com.t2tierp.model.bean.pafecf.EcfRecebimentoNaoFiscal[id=" + id + "]";
    }

}
