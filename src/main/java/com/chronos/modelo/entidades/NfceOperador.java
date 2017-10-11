
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NFCE_OPERADOR")
public class NfceOperador implements Serializable {

    private static final long serialVersionUID = 1L;
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


    public NfceOperador() {
    }

    public NfceOperador(Integer id) {
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


}
