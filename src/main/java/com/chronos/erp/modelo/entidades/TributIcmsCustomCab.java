package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TRIBUT_ICMS_CUSTOM_CAB")
public class TributIcmsCustomCab implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "ORIGEM_MERCADORIA")
    private String origemMercadoria;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tributIcmsCustomCab", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TributIcmsCustomDet> listaTributIcmsCustomDet;

    public TributIcmsCustomCab() {
    }

    public TributIcmsCustomCab(Integer id) {
        this.id = id;
    }

    public TributIcmsCustomCab(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getOrigemMercadoria() {
        return origemMercadoria;
    }

    public void setOrigemMercadoria(String origemMercadoria) {
        this.origemMercadoria = origemMercadoria;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Set<TributIcmsCustomDet> getListaTributIcmsCustomDet() {
        return listaTributIcmsCustomDet;
    }

    public void setListaTributIcmsCustomDet(Set<TributIcmsCustomDet> listaTributIcmsCustomDet) {
        this.listaTributIcmsCustomDet = listaTributIcmsCustomDet;
    }

    @Override
    public String toString() {
        return "TributIcmsCustomCab{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final TributIcmsCustomCab other = (TributIcmsCustomCab) obj;
        return Objects.equals(this.id, other.id);
    }

}
