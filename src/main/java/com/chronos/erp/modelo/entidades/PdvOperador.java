
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "PDV_OPERADOR")
public class PdvOperador implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SENHA")
    private String senha;
    @Column(name = "NIVEL_AUTORIZACAO")
    private String nivelAutorizacao;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne
    @NotNull
    private Colaborador colaborador;


    public PdvOperador() {
    }

    public PdvOperador(Integer id, String login) {
        this.id = id;
        this.login = login;
    }

    public PdvOperador(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * -- G=GERENTE | S=SUPERVISOR | O=OPERADOR
     *
     * @return -- G=GERENTE | S=SUPERVISOR | O=OPERADOR
     */
    public String getNivelAutorizacao() {
        return nivelAutorizacao;
    }

    /**
     * -- G=GERENTE | S=SUPERVISOR | O=OPERADOR
     *
     * @param nivelAutorizacao
     */
    public void setNivelAutorizacao(String nivelAutorizacao) {
        this.nivelAutorizacao = nivelAutorizacao;
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
        if (!(o instanceof PdvOperador)) return false;

        PdvOperador that = (PdvOperador) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
