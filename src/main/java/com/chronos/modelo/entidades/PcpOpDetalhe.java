package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PCP_OP_DETALHE")
public class PcpOpDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE_PRODUZIR")
    private BigDecimal quantidadeProduzir;
    @Column(name = "QUANTIDADE_PRODUZIDA")
    private BigDecimal quantidadeProduzida;
    @Column(name = "QUANTIDADE_ENTREGUE")
    private BigDecimal quantidadeEntregue;
    @Column(name = "CUSTO_PREVISTO")
    private BigDecimal custoPrevisto;
    @Column(name = "CUSTO_REALIZADO")
    private BigDecimal custoRealizado;
    @JoinColumn(name = "ID_PCP_OP_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PcpOpCabecalho pcpOpCabecalho;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "pcpOpDetalhe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PcpServico> listaPcpServico;

    public PcpOpDetalhe() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidadeProduzir() {
        return quantidadeProduzir;
    }

    public void setQuantidadeProduzir(BigDecimal quantidadeProduzir) {
        this.quantidadeProduzir = quantidadeProduzir;
    }

    public BigDecimal getQuantidadeProduzida() {
        return quantidadeProduzida;
    }

    public void setQuantidadeProduzida(BigDecimal quantidadeProduzida) {
        this.quantidadeProduzida = quantidadeProduzida;
    }

    public BigDecimal getQuantidadeEntregue() {
        return quantidadeEntregue;
    }

    public void setQuantidadeEntregue(BigDecimal quantidadeEntregue) {
        this.quantidadeEntregue = quantidadeEntregue;
    }

    public BigDecimal getCustoPrevisto() {
        return custoPrevisto;
    }

    public void setCustoPrevisto(BigDecimal custoPrevisto) {
        this.custoPrevisto = custoPrevisto;
    }

    public BigDecimal getCustoRealizado() {
        return custoRealizado;
    }

    public void setCustoRealizado(BigDecimal custoRealizado) {
        this.custoRealizado = custoRealizado;
    }

    public PcpOpCabecalho getPcpOpCabecalho() {
        return pcpOpCabecalho;
    }

    public void setPcpOpCabecalho(PcpOpCabecalho pcpOpCabecalho) {
        this.pcpOpCabecalho = pcpOpCabecalho;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }


    public Set<PcpServico> getListaPcpServico() {
        return listaPcpServico;
    }

    public void setListaPcpServico(Set<PcpServico> listaPcpServico) {
        this.listaPcpServico = listaPcpServico;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final PcpOpDetalhe other = (PcpOpDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }


}
