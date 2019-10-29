package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NFE_DET_ESPECIFICO_ARMAMENTO")
public class NfeDetEspecificoArmamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO_ARMA")
    private Integer tipoArma;
    @Column(name = "NUMERO_SERIE_ARMA")
    private String numeroSerieArma;
    @Column(name = "NUMERO_SERIE_CANO")
    private String numeroSerieCano;
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoArmamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTipoArma() {
        return tipoArma;
    }

    public void setTipoArma(Integer tipoArma) {
        this.tipoArma = tipoArma;
    }

    public String getNumeroSerieArma() {
        return numeroSerieArma;
    }

    public void setNumeroSerieArma(String numeroSerieArma) {
        this.numeroSerieArma = numeroSerieArma;
    }

    public String getNumeroSerieCano() {
        return numeroSerieCano;
    }

    public void setNumeroSerieCano(String numeroSerieCano) {
        this.numeroSerieCano = numeroSerieCano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final NfeDetEspecificoArmamento other = (NfeDetEspecificoArmamento) obj;
        return Objects.equals(this.id, other.id);
    }

}
