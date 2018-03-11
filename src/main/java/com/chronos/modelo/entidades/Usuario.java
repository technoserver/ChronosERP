package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "LOGIN")
    @Email
    private String login;
    @Column(name = "SENHA")
    private String senha;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Column(name = "ADMINISTRADOR")
    private String administrador;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Colaborador colaborador;
    @JoinColumn(name = "ID_PAPEL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Papel papel;

    public Usuario(Integer id, String login, String senha,Integer idpapel) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.papel = new Papel(idpapel);
    }

    public Usuario(Integer id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;

    }

    public Usuario(Integer id, String login) {
        this.id = id;
        this.login = login;

    }

    public Usuario(Integer id) {
        this.id = id;
    }

    
    public Usuario() {
    }

    @PrePersist
    @PreUpdate
    private void prePersist() {
        this.login = login.trim().toLowerCase();
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

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }
   
}
