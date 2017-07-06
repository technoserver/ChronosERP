
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "OS_ABERTURA_EQUIPAMENTO")
public class OsAberturaEquipamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @NotBlank(message =  "Número de serie origatório")
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    @Column(name = "TIPO_COBERTURA")
    private Integer tipoCobertura;
    @JoinColumn(name = "ID_OS_ABERTURA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OsAbertura osAbertura;
    @JoinColumn(name = "ID_OS_EQUIPAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OsEquipamento osEquipamento;

    public OsAberturaEquipamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Integer getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(Integer tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }

    public OsAbertura getOsAbertura() {
        return osAbertura;
    }

    public void setOsAbertura(OsAbertura osAbertura) {
        this.osAbertura = osAbertura;
    }

    public OsEquipamento getOsEquipamento() {
        return osEquipamento;
    }

    public void setOsEquipamento(OsEquipamento osEquipamento) {
        this.osEquipamento = osEquipamento;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
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
        final OsAberturaEquipamento other = (OsAberturaEquipamento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OS"+ numeroSerie ;
    }

    
   
}
