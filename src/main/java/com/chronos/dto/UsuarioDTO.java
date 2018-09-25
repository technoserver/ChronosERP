package com.chronos.dto;

import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Empresa;

public class UsuarioDTO {

    private Integer id;
    private Integer idempresa;
    private Integer idcolaborador;
    private Integer idpessoa;
    private String login;
    private String senha;
    private String administrador;
    private String nome;
    private String foto;
    private String cargo;
    private Empresa empresa;


    public UsuarioDTO(Integer id, Integer idempresa, Integer idcolaborador, Integer idpessoa, String login, String senha, String administrador, String nome, String foto, String cargo) {
        this.id = id;
        this.idempresa = idempresa;
        this.idcolaborador = idcolaborador;
        this.idpessoa = idpessoa;
        this.login = login;
        this.senha = senha;
        this.administrador = administrador;
        this.nome = nome;
        this.foto = foto;
        this.cargo = cargo;
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

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



    public Integer getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Integer idcolaborador) {
        this.idcolaborador = idcolaborador;
    }


    public Colaborador getColaborador() {
        return new Colaborador(this.idcolaborador);
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }

    public Integer getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Integer idpessoa) {
        this.idpessoa = idpessoa;
    }


    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
