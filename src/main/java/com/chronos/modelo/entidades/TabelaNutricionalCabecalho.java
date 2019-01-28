package com.chronos.modelo.entidades;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tabela_nutricional_cabecalho")
public class TabelaNutricionalCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Size(max = 100)
    @Column(name = "nome", length = 100)
    @NotBlank
    private String nome;
    @NotNull
    private BigDecimal porcao;
    @NotBlank
    private String unidade;
    @Column(name = "parte_inteira_medida_caseria")
    private Integer parteInteiraMedidaCaseria;
    @Column(name = "parte_decimal_medida_caseria")
    private Integer parteDecimalMedidaCaseria;
    @Column(name = "medida_caseira_utilizada")
    private String medidaCaseiraUtilizada;
    @OneToMany(mappedBy = "tabelaNutricional", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TabelaNutricionalDetalhe> nutrientes;


    public TabelaNutricionalCabecalho() {
    }

    public TabelaNutricionalCabecalho(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPorcao() {
        return porcao;
    }

    public void setPorcao(BigDecimal porcao) {
        this.porcao = porcao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public List<TabelaNutricionalDetalhe> getNutrientes() {
        return nutrientes;
    }

    public void setNutrientes(List<TabelaNutricionalDetalhe> nutrientes) {
        this.nutrientes = nutrientes;
    }

    public Integer getParteInteiraMedidaCaseria() {
        return parteInteiraMedidaCaseria;
    }

    public void setParteInteiraMedidaCaseria(Integer parteInteiraMedidaCaseria) {
        this.parteInteiraMedidaCaseria = parteInteiraMedidaCaseria;
    }

    public Integer getParteDecimalMedidaCaseria() {
        return parteDecimalMedidaCaseria;
    }

    public void setParteDecimalMedidaCaseria(Integer parteDecimalMedidaCaseria) {
        this.parteDecimalMedidaCaseria = parteDecimalMedidaCaseria;
    }

    public String getMedidaCaseiraUtilizada() {
        return medidaCaseiraUtilizada;
    }

    public void setMedidaCaseiraUtilizada(String medidaCaseiraUtilizada) {
        this.medidaCaseiraUtilizada = medidaCaseiraUtilizada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabelaNutricionalCabecalho)) return false;

        TabelaNutricionalCabecalho that = (TabelaNutricionalCabecalho) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
