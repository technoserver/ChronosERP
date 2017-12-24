package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by john on 18/12/17.
 */
@Entity
@Table(name = "AGENDA_COMPROMISSO")
public class AgendaCompromisso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_COMPROMISSO")
    private Date dataCompromisso;
    @Column(name = "HORA")
    private String hora;
    @Column(name = "DURACAO")
    private Integer duracao;
    @Column(name = "ONDE")
    private String onde;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "TIPO")
    private Integer tipo;
    @JoinColumn(name = "ID_AGENDA_CATEGORIA_COMPROMISSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AgendaCategoriaCompromisso agendaCategoriaCompromisso;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;

    @Transient
    private String nomeColaborador;
    @Transient
    private String categoria;

    public AgendaCompromisso() {
    }

    public AgendaCompromisso(Integer id, String descricao, Date dataCompromisso, String cor) {
        this.id = id;
        this.dataCompromisso = dataCompromisso;
        this.descricao = descricao;
        this.categoria = cor;
    }

    public AgendaCompromisso(Integer id, String descricao, Date dataCompromisso, String hora, String onde) {
        this.id = id;
        this.dataCompromisso = dataCompromisso;
        this.descricao = descricao;
        this.hora = hora;
        this.onde = onde;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCompromisso() {
        return dataCompromisso;
    }

    public void setDataCompromisso(Date dataCompromisso) {
        this.dataCompromisso = dataCompromisso;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public String getOnde() {
        return onde;
    }

    public void setOnde(String onde) {
        this.onde = onde;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public AgendaCategoriaCompromisso getAgendaCategoriaCompromisso() {
        return agendaCategoriaCompromisso;
    }

    public void setAgendaCategoriaCompromisso(AgendaCategoriaCompromisso agendaCategoriaCompromisso) {
        this.agendaCategoriaCompromisso = agendaCategoriaCompromisso;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }


    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgendaCompromisso)) return false;

        AgendaCompromisso that = (AgendaCompromisso) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
