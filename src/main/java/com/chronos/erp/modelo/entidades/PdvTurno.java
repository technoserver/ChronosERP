
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "PDV_TURNO")
public class PdvTurno implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "HORA_INICIO")
    private String horaInicio;
    @Column(name = "HORA_FIM")
    private String horaFim;

    public PdvTurno() {
    }

    public PdvTurno(Integer id) {
        this.id = id;
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

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvTurno)) return false;

        PdvTurno pdvTurno = (PdvTurno) o;

        return getId() != null ? getId().equals(pdvTurno.getId()) : pdvTurno.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
