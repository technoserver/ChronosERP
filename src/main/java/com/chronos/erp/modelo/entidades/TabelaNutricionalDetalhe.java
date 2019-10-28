package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "tabela_nutricional_detalhe")
public class TabelaNutricionalDetalhe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    private BigDecimal quantidade;
    private BigDecimal vd;
    @JoinColumn(name = "id_nutriente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private Nutriente nutriente;
    @JoinColumn(name = "id_tabela_nutricional_cabecalho", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @NotNull
    private TabelaNutricionalCabecalho tabelaNutricional;

    public TabelaNutricionalDetalhe() {
    }

    public TabelaNutricionalDetalhe(Nutriente nutriente, BigDecimal quantidade, BigDecimal vd) {
        this.quantidade = quantidade;
        this.vd = vd;
        this.nutriente = nutriente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getVd() {
        return vd;
    }

    public void setVd(BigDecimal vd) {
        this.vd = vd;
    }

    public Nutriente getNutriente() {
        return nutriente;
    }

    public void setNutriente(Nutriente nutriente) {
        this.nutriente = nutriente;
    }

    public TabelaNutricionalCabecalho getTabelaNutricional() {
        return tabelaNutricional;
    }

    public void setTabelaNutricional(TabelaNutricionalCabecalho tabelaNutricional) {
        this.tabelaNutricional = tabelaNutricional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabelaNutricionalDetalhe)) return false;

        TabelaNutricionalDetalhe that = (TabelaNutricionalDetalhe) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
