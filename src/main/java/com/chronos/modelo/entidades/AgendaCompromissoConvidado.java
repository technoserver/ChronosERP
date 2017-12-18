package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 18/12/17.
 */
@Entity
@Table(name = "AGENDA_COMPROMISSO_CONVIDADO")
public class AgendaCompromissoConvidado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "ID_AGENDA_COMPROMISSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AgendaCompromisso agendaCompromisso;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;

    public AgendaCompromissoConvidado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AgendaCompromisso getAgendaCompromisso() {
        return agendaCompromisso;
    }

    public void setAgendaCompromisso(AgendaCompromisso agendaCompromisso) {
        this.agendaCompromisso = agendaCompromisso;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgendaCompromissoConvidado)) return false;

        AgendaCompromissoConvidado that = (AgendaCompromissoConvidado) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
