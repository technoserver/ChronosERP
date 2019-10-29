
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author john
 */
@Entity
@Table(name = "venda_condicoes_parcelas")
public class VendaCondicoesParcelas implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "parcela")
    private Integer parcela;
    @Column(name = "dias")
    private Integer dias;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxa", precision = 18, scale = 6)
    private BigDecimal taxa;
    @JoinColumn(name = "id_venda_condicoes_pagamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VendaCondicoesPagamento vendaCondicoesPagamento;

    public VendaCondicoesParcelas() {
        this.dias = 0;
        this.parcela = 0;
        this.taxa = BigDecimal.ZERO;

    }

    public VendaCondicoesParcelas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Número da parcela
     *
     * @return
     */
    public Integer getParcela() {
        return parcela;
    }

    /**
     * Número da parcela
     *
     * @param parcela
     */
    public void setParcela(Integer parcela) {
        this.parcela = parcela;
    }

    /**
     * Quantidade de dias a partir da data da venda
     *
     * @return
     */
    public Integer getDias() {
        return dias;
    }

    /**
     * Quantidade de dias a partir da data da venda
     *
     * @param dias
     */
    public void setDias(Integer dias) {
        this.dias = dias;
    }

    /**
     * Taxa percentual referente à parcela
     *
     * @return
     */
    public BigDecimal getTaxa() {
        return taxa;
    }

    /**
     * Taxa percentual referente à parcela
     *
     * @param taxa
     */
    public void setTaxa(BigDecimal taxa) {
        this.taxa = taxa;
    }

    public VendaCondicoesPagamento getVendaCondicoesPagamento() {
        return vendaCondicoesPagamento;
    }

    public void setVendaCondicoesPagamento(VendaCondicoesPagamento vendaCondicoesPagamento) {
        this.vendaCondicoesPagamento = vendaCondicoesPagamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.dias);
        hash = 53 * hash + Objects.hashCode(this.taxa);
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
        final VendaCondicoesParcelas other = (VendaCondicoesParcelas) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dias, other.dias)) {
            return false;
        }
        return Objects.equals(this.taxa, other.taxa);
    }


    @Override
    public String toString() {
        return "VendaCondicoesParcelas[ id=" + id + " ]";
    }

}
