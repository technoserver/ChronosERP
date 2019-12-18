package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    @NotNull
    @Column(name = "id_venda")
    private Integer idVenda;

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

    @NotNull
    @Column(name = "gerado_credito")
    private String geradoCredito;
    @NotNull
    @Column(name = "codigo_modulo")
    private String codigoModulo;

    @OneToMany(mappedBy = "vendaDevolucao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaDevolucaoItem> listaVendaDevolucaoItem;
    @Transient
    private BigDecimal valorVenda;

    public VendaDevolucao() {
        this.dataDevolucao = new Date();
        this.creditoUtilizado = "N";
        this.totalParcial = "T";
        this.geradoCredito = "N";
    }

    public VendaDevolucao(Integer id, Date dataDevolucao, BigDecimal valorCredito, String totalParcial, String creditoUtilizado, Integer idvenda) {
        this.id = id;
        this.dataDevolucao = dataDevolucao;
        this.valorCredito = valorCredito;
        this.totalParcial = totalParcial;
        this.creditoUtilizado = creditoUtilizado;
        this.idVenda = idvenda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    public String getGeradoCredito() {
        return geradoCredito;
    }

    public void setGeradoCredito(String geradoCredito) {
        this.geradoCredito = geradoCredito;
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

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public List<VendaDevolucaoItem> getListaVendaDevolucaoItem() {
        return listaVendaDevolucaoItem;
    }

    public void setListaVendaDevolucaoItem(List<VendaDevolucaoItem> listaVendaDevolucaoItem) {
        this.listaVendaDevolucaoItem = listaVendaDevolucaoItem;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public void calcularValorCredito() {
        valorCredito = listaVendaDevolucaoItem
                .stream()
                .map(i -> (i.getQuantidade().multiply(i.getValor())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
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
