package com.chronos.erp.modelo.entidades;


import com.chronos.erp.modelo.enuns.EventoNfe;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "nfe_evento")
public class NfeEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @NotNull
    @Column(name = "id_nfe_cabecalho")
    private Integer idnfecabecalho;
    @NotNull
    @Column(name = "data_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    @NotNull
    @Column(name = "tipo")
    private EventoNfe tipo;
    @Column(name = "justificativa")
    private String justificativa;
    @NotNull
    @Column(name = "sequencia")
    private Integer sequencia;
    @Column(name = "protocolo")
    private String protocolo;

    public NfeEvento() {
    }

    public NfeEvento(Integer idnfecabecalho, Date dataHora, EventoNfe tipo, Integer sequencia) {
        this.idnfecabecalho = idnfecabecalho;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.sequencia = sequencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdnfecabecalho() {
        return idnfecabecalho;
    }

    public void setIdnfecabecalho(Integer idnfecabecalho) {
        this.idnfecabecalho = idnfecabecalho;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public EventoNfe getTipo() {
        return tipo;
    }

    public void setTipo(EventoNfe tipo) {
        this.tipo = tipo;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public void atualizarSequencia() {
        this.sequencia = this.sequencia + 1;
    }


    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }


    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NfeEvento)) return false;
        NfeEvento nfeEvento = (NfeEvento) o;
        return Objects.equals(id, nfeEvento.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
