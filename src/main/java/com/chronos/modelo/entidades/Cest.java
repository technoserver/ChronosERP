
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "CEST")
public class Cest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ITEM")
    private Integer item;
    @Column(name = "CEST")
    private String cest;
    @Column(name = "NCM_SH")
    private String ncmSh;
    @Column(name = "DESCRICAO_SEGMENTO")
    private String descricaoSegmento;
    @Column(name = "DESCRICAO_CEST")
    private String descricaoCest;
    @Column(name = "SIGLA_UNIDADE")
    private String siglaUnidade;
    @Column(name = "DESCRICAO_UNIDADE")
    private String descricaoUnidade;

    public Cest() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public String getNcmSh() {
        return ncmSh;
    }

    public void setNcmSh(String ncmSh) {
        this.ncmSh = ncmSh;
    }

    public String getDescricaoSegmento() {
        return descricaoSegmento;
    }

    public void setDescricaoSegmento(String descricaoSegmento) {
        this.descricaoSegmento = descricaoSegmento;
    }

    public String getDescricaoCest() {
        return descricaoCest;
    }

    public void setDescricaoCest(String descricaoCest) {
        this.descricaoCest = descricaoCest;
    }

    public String getSiglaUnidade() {
        return siglaUnidade;
    }

    public void setSiglaUnidade(String siglaUnidade) {
        this.siglaUnidade = siglaUnidade;
    }

    public String getDescricaoUnidade() {
        return descricaoUnidade;
    }

    public void setDescricaoUnidade(String descricaoUnidade) {
        this.descricaoUnidade = descricaoUnidade;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Cest other = (Cest) obj;
        return Objects.equals(this.id, other.id);
    }

   
}
