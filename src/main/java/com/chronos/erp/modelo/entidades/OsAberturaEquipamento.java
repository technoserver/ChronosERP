
package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
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
    @NotBlank(message = "Número de serie origatório")
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    private String modelo;
    @Column(name = "TIPO_COBERTURA")
    private Integer tipoCobertura;
    @JoinColumn(name = "ID_OS_ABERTURA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OsAbertura osAbertura;
    @JoinColumn(name = "ID_OS_EQUIPAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OsEquipamento osEquipamento;
    @Transient
    private String tipoAtendimento;

    public OsAberturaEquipamento() {
    }

    public OsAberturaEquipamento(Integer id, String numeroSerie, Integer tipoCobertura, String equipamento, Integer idos, Date dataInicio, String tipoAtendimento) {
        this.id = id;
        this.numeroSerie = numeroSerie;
        this.tipoCobertura = tipoCobertura;
        this.osEquipamento = new OsEquipamento(0, equipamento);
        this.osAbertura = new OsAbertura(idos, dataInicio);
        this.tipoAtendimento = tipoAtendimento;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
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

    public String getTipoAtendimento() {
        return tipoAtendimento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsAberturaEquipamento that = (OsAberturaEquipamento) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(numeroSerie, that.numeroSerie) &&
                Objects.equals(modelo, that.modelo) &&
                Objects.equals(tipoCobertura, that.tipoCobertura) &&
                Objects.equals(osEquipamento, that.osEquipamento) &&
                Objects.equals(tipoAtendimento, that.tipoAtendimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numeroSerie, modelo, tipoCobertura, osEquipamento, tipoAtendimento);
    }

    @Override
    public String toString() {
        return "OS" + numeroSerie;
    }


}
