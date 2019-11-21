package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PCP_OP_CABECALHO")
public class PcpOpCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO")
    private Date inicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "PREVISAO_ENTREGA")
    private Date previsaoEntrega;
    @Temporal(TemporalType.DATE)
    @Column(name = "TERMINO")
    private Date termino;
    @Column(name = "CUSTO_TOTAL_PREVISTO")
    private BigDecimal custoTotalPrevisto;
    @Column(name = "CUSTO_TOTAL_REALIZADO")
    private BigDecimal custoTotalRealizado;
    @Column(name = "PORCENTO_VENDA")
    private BigDecimal porcentoVenda;
    @Column(name = "PORCENTO_ESTOQUE")
    private BigDecimal porcentoEstoque;
    @Transient
    private String status;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pcpOpCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PcpOpDetalhe> listaPcpOpDetalhe;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pcpOpCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PcpInstrucaoOp> listaPcpInstrucaoOp;

    public PcpOpCabecalho() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(Date previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public BigDecimal getCustoTotalPrevisto() {
        return custoTotalPrevisto;
    }

    public void setCustoTotalPrevisto(BigDecimal custoTotalPrevisto) {
        this.custoTotalPrevisto = custoTotalPrevisto;
    }

    public BigDecimal getCustoTotalRealizado() {
        return custoTotalRealizado;
    }

    public void setCustoTotalRealizado(BigDecimal custoTotalRealizado) {
        this.custoTotalRealizado = custoTotalRealizado;
    }

    public BigDecimal getPorcentoVenda() {
        return porcentoVenda;
    }

    public void setPorcentoVenda(BigDecimal porcentoVenda) {
        this.porcentoVenda = porcentoVenda;
    }

    public BigDecimal getPorcentoEstoque() {
        return porcentoEstoque;
    }

    public void setPorcentoEstoque(BigDecimal porcentoEstoque) {
        this.porcentoEstoque = porcentoEstoque;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Set<PcpOpDetalhe> getListaPcpOpDetalhe() {
        return listaPcpOpDetalhe;
    }

    public void setListaPcpOpDetalhe(Set<PcpOpDetalhe> listaPcpOpDetalhe) {
        this.listaPcpOpDetalhe = listaPcpOpDetalhe;
    }

    public Set<PcpInstrucaoOp> getListaPcpInstrucaoOp() {
        return listaPcpInstrucaoOp;
    }

    public void setListaPcpInstrucaoOp(Set<PcpInstrucaoOp> listaPcpInstrucaoOp) {
        this.listaPcpInstrucaoOp = listaPcpInstrucaoOp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PcpOpCabecalho other = (PcpOpCabecalho) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
