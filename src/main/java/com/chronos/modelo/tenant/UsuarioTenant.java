package com.chronos.modelo.tenant;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 26/12/17.
 */
@Entity
@Table(name = "usuario_tenant")
public class UsuarioTenant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "login")
    public String login;
    @Column(name = "senha")
    public String senha;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @JoinColumn(name = "id_tenant", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tenant tenant;

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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioTenant)) return false;

        UsuarioTenant that = (UsuarioTenant) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
