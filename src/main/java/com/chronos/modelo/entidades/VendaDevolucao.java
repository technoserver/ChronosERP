package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "venda_devolucao")
public class VendaDevolucao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "id_venda_utilizacao")
    private Integer idVendaUtilizacao;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "data_devolucao")
    private Date dataDevolucao;

    @Column(name = "valor_credito")
    @NotNull
    private BigDecimal valorCredito;

    @NotNull
    @Column(name = "total_parcial")
    private String totalParcial;

    @NotNull
    @Column(name = "credito_utilizado")
    private String creditoUtilizado;

    @JoinColumn(name = "ID_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private VendaCabecalho vendaCabecalho;


    public VendaDevolucao() {
        this.dataDevolucao = new Date();
        this.creditoUtilizado = "N";
        this.totalParcial = "T";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVendaUtilizacao() {
        return idVendaUtilizacao;
    }

    public void setIdVendaUtilizacao(Integer idVendaUtilizacao) {
        this.idVendaUtilizacao = idVendaUtilizacao;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public BigDecimal getValorCredito() {
        return valorCredito;
    }

    public void setValorCredito(BigDecimal valorCredito) {
        this.valorCredito = valorCredito;
    }

    public String getTotalParcial() {
        return totalParcial;
    }

    public void setTotalParcial(String totalParcial) {
        this.totalParcial = totalParcial;
    }

    public String getCreditoUtilizado() {
        return creditoUtilizado;
    }

    public void setCreditoUtilizado(String creditoUtilizado) {
        this.creditoUtilizado = creditoUtilizado;
    }

    public VendaCabecalho getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendaDevolucao that = (VendaDevolucao) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
