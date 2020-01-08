
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "OS_EVOLUCAO")
public class OsEvolucao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_REGISTRO")
    private Date dataRegistro;
    @Column(name = "HORA_REGISTRO")
    private String horaRegistro;
    private String modelo;
    private String responsavel;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @Column(name = "ENVIAR_EMAIL")
    private String enviarEmail;
    @JoinColumn(name = "ID_OS_ABERTURA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private OsAbertura osAbertura;

    public OsEvolucao() {
        this.dataRegistro = new Date();
        this.enviarEmail = "N";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getHoraRegistro() {
        return horaRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getEnviarEmail() {
        return enviarEmail;
    }

    public void setEnviarEmail(String enviarEmail) {
        this.enviarEmail = enviarEmail;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public OsAbertura getOsAbertura() {
        return osAbertura;
    }

    public void setOsAbertura(OsAbertura osAbertura) {
        this.osAbertura = osAbertura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OsEvolucao that = (OsEvolucao) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dataRegistro, that.dataRegistro) &&
                Objects.equals(horaRegistro, that.horaRegistro) &&
                Objects.equals(modelo, that.modelo) &&
                Objects.equals(responsavel, that.responsavel) &&
                Objects.equals(observacao, that.observacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataRegistro, horaRegistro, modelo, responsavel, observacao);
    }

    @Override
    public String toString() {
        return "OsEvolucao{" + "id=" + id + '}';
    }


}
