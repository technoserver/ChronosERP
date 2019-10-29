
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "INVENTARIO_CONTAGEM_CAB")
public class InventarioContagemCab implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CONTAGEM")
    private Date dataContagem;
    @Column(name = "ESTOQUE_ATUALIZADO")
    private String estoqueAtualizado;
    @Column(name = "TIPO")
    private String tipo;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventarioContagemCab", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventarioContagemDet> listaInventarioContagemDet;

    public InventarioContagemCab() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataContagem() {
        return dataContagem;
    }

    public void setDataContagem(Date dataContagem) {
        this.dataContagem = dataContagem;
    }

    public String getEstoqueAtualizado() {
        return estoqueAtualizado;
    }

    public void setEstoqueAtualizado(String estoqueAtualizado) {
        this.estoqueAtualizado = estoqueAtualizado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    public List<InventarioContagemDet> getListaInventarioContagemDet() {
        return listaInventarioContagemDet;
    }

    public void setListaInventarioContagemDet(List<InventarioContagemDet> listaInventarioContagemDet) {
        this.listaInventarioContagemDet = listaInventarioContagemDet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventarioContagemCab that = (InventarioContagemCab) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
