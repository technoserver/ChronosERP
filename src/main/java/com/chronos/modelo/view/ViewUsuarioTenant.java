package com.chronos.modelo.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by john on 01/02/18.
 */
@Entity
@Table(name = "view_usuario_tenant")
public class ViewUsuarioTenant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "login")
    private String login;
    @Column(name = "senha")
    private String senha;
    @Column(name = "id_tenant")
    private Integer idtenant;
    @Column(name = "nome_tenant")
    private String nomeTenant;
    @Column(name = "sistema")
    private String sistema;
    @Column(name = "status")
    private String status;

    public ViewUsuarioTenant() {
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

    public Integer getIdtenant() {
        return idtenant;
    }

    public void setIdtenant(Integer idtenant) {
        this.idtenant = idtenant;
    }

    public String getNomeTenant() {
        return nomeTenant;
    }

    public void setNomeTenant(String nomeTenant) {
        this.nomeTenant = nomeTenant;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewUsuarioTenant)) return false;

        ViewUsuarioTenant that = (ViewUsuarioTenant) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
