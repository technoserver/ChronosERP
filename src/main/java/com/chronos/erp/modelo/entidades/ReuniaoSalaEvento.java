package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 18/12/17.
 */
@Entity
@Table(name = "REUNIAO_SALA_EVENTO")
public class ReuniaoSalaEvento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_RESERVA")
    private Date dataReserva;
    @JoinColumn(name = "ID_REUNIAO_SALA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ReuniaoSala reuniaoSala;
    @JoinColumn(name = "ID_AGENDA_COMPROMISSO", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private AgendaCompromisso agendaCompromisso;

    public ReuniaoSalaEvento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public ReuniaoSala getReuniaoSala() {
        return reuniaoSala;
    }

    public void setReuniaoSala(ReuniaoSala reuniaoSala) {
        this.reuniaoSala = reuniaoSala;
    }

    public AgendaCompromisso getAgendaCompromisso() {
        return agendaCompromisso;
    }

    public void setAgendaCompromisso(AgendaCompromisso agendaCompromisso) {
        this.agendaCompromisso = agendaCompromisso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReuniaoSalaEvento)) return false;

        ReuniaoSalaEvento that = (ReuniaoSalaEvento) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
