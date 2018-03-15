package com.chronos.dto;

import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.tenant.Tenant;

import java.time.LocalDate;

public class UsuarioDTO {

    private Integer id;
    private String login;
    private String senha;
    private String administrador;
    private String nome;
    private String foto;
    private Tenant tenant;
    private LocalDate dataVencimento;
    private Integer idcolaborador;
    private String cargo;
    private Empresa empresa;


    public UsuarioDTO(Integer id, String login, String senha, String administrador, String nome, String foto, Integer idcolaborador, String cargo, Empresa empresa) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.administrador = administrador;
        this.nome = nome;
        this.foto = foto;
        this.idcolaborador = idcolaborador;
        this.cargo = cargo;
        this.empresa = empresa;
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
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
}
