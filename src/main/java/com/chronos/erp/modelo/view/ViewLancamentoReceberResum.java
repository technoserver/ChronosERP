package com.chronos.erp.modelo.view;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "view_fin_lancamento_receber_resum")
public class ViewLancamentoReceberResum implements Serializable {

    @Id
    @Basic
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_empresa")
    private Integer idempresa;
    @Column(name = "numero_documento")
    private String numDoc;
    @Column(name = "nome")
    private String cliente;
    @Column(name = "quantidade_parcela")
    private Integer qtdParcelas;
    @Column(name = "valor_total")
    private BigDecimal valorAReceber;
    private BigDecimal saldo;
    @Column(name = "data_lancamento")
    private Date dataLancamento;
    private String status;

    public ViewLancamentoReceberResum() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public BigDecimal getValorAReceber() {
        return valorAReceber;
    }

    public void setValorAReceber(BigDecimal valorAReceber) {
        this.valorAReceber = valorAReceber;
    }

    public BigDecimal getSaldo() {
        return Optional.ofNullable(saldo).orElse(valorAReceber);
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }
}
