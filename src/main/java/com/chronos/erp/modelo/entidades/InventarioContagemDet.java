
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "INVENTARIO_CONTAGEM_DET")
public class InventarioContagemDet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CONTAGEM01")
    private BigDecimal contagem01;
    @Column(name = "CONTAGEM02")
    private BigDecimal contagem02;
    @Column(name = "CONTAGEM03")
    private BigDecimal contagem03;
    @Column(name = "FECHADO_CONTAGEM")
    private String fechadoContagem;
    @Column(name = "QUANTIDADE_SISTEMA")
    private BigDecimal quantidadeSistema;
    @Column(name = "ACURACIDADE")
    private BigDecimal acuracidade;
    @Column(name = "DIVERGENCIA")
    private BigDecimal divergencia;
    @JoinColumn(name = "ID_INVENTARIO_CONTAGEM_CAB", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private InventarioContagemCab inventarioContagemCab;
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto produto;
    @Transient
    private Integer contagem;

    public InventarioContagemDet() {
        this.contagem = 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getContagem01() {
        return contagem01;
    }

    public void setContagem01(BigDecimal contagem01) {
        this.contagem01 = contagem01;
    }

    public BigDecimal getContagem02() {
        return contagem02;
    }

    public void setContagem02(BigDecimal contagem02) {
        this.contagem02 = contagem02;
    }

    public BigDecimal getContagem03() {
        return contagem03;
    }

    public void setContagem03(BigDecimal contagem03) {
        this.contagem03 = contagem03;
    }

    public String getFechadoContagem() {
        return fechadoContagem;
    }

    public void setFechadoContagem(String fechadoContagem) {
        this.fechadoContagem = fechadoContagem;
    }

    public BigDecimal getQuantidadeSistema() {
        return quantidadeSistema;
    }

    public void setQuantidadeSistema(BigDecimal quantidadeSistema) {
        this.quantidadeSistema = quantidadeSistema;
    }

    public BigDecimal getAcuracidade() {
        return acuracidade;
    }

    public void setAcuracidade(BigDecimal acuracidade) {
        this.acuracidade = acuracidade;
    }

    public BigDecimal getDivergencia() {
        return divergencia;
    }

    public void setDivergencia(BigDecimal divergencia) {
        this.divergencia = divergencia;
    }

    public InventarioContagemCab getInventarioContagemCab() {
        return inventarioContagemCab;
    }

    public void setInventarioContagemCab(InventarioContagemCab inventarioContagemCab) {
        this.inventarioContagemCab = inventarioContagemCab;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getContagem() {
        return contagem;
    }

    public void setContagem(Integer contagem) {
        this.contagem = contagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventarioContagemDet that = (InventarioContagemDet) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
