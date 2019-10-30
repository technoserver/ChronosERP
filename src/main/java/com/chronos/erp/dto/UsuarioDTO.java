package com.chronos.erp.dto;

import com.chronos.erp.modelo.entidades.Empresa;

public class UsuarioDTO {

    private Integer id;
    private Integer idempresa;
    private Integer idtenant;
    private Integer idcolaborador;
    private Integer idpessoa;
    private String login;
    private String senha;
    private String nome;
    private String administrador;
    private String cargo;
    private String foto;
    private String cnpjEmpresa;
    private String nomeEmpresa;
    private Integer idpapel;
    private String acessoCompleto;
    private String tenant;
    private String statusTenant;
    private Operador operador;
    private Empresa empresa;


    public UsuarioDTO() {
    }

    public UsuarioDTO(Integer id, Integer idempresa, Integer idcolaborador, Integer idpessoa, String login, String senha,
                      String nome, String foto, String administrador, String cargo, String nomeEmpresa, Integer idpapel,
                      String acessoCompleto, Integer idoperador, String operador, String nivelAcesso) {

        this.id = id;
        this.idcolaborador = idcolaborador;
        this.idpessoa = idpessoa;
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.foto = foto;
        this.administrador = administrador;
        this.cargo = cargo;
        this.idempresa = idempresa;
        this.nomeEmpresa = nomeEmpresa;
        this.idpapel = idpapel;
        this.acessoCompleto = acessoCompleto;
        this.operador = operador != null ? new Operador(idoperador, operador, nivelAcesso) : null;
    }


    public String getNomeOperador() {
        return this.operador == null ? null : operador.getNome();
    }

    public String getNivelAutorizacao() {
        return this.operador == null ? null : operador.getNivelAcesso();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(Integer idempresa) {
        this.idempresa = idempresa;
    }

    public Integer getIdtenant() {
        return idtenant;
    }

    public void setIdtenant(Integer idtenant) {
        this.idtenant = idtenant;
    }

    public Integer getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Integer idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public Integer getIdpessoa() {
        return idpessoa;
    }

    public void setIdpessoa(Integer idpessoa) {
        this.idpessoa = idpessoa;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAdministrador() {
        return administrador;
    }

    public void setAdministrador(String administrador) {
        this.administrador = administrador;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public Integer getIdpapel() {
        return idpapel;
    }

    public void setIdpapel(Integer idpapel) {
        this.idpapel = idpapel;
    }

    public String getAcessoCompleto() {
        return acessoCompleto;
    }

    public void setAcessoCompleto(String acessoCompleto) {
        this.acessoCompleto = acessoCompleto;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getStatusTenant() {
        return statusTenant;
    }

    public void setStatusTenant(String statusTenant) {
        this.statusTenant = statusTenant;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public class Operador {

        private Integer id;
        private String nome;
        private String nivelAcesso;

        public Operador(Integer id, String nome, String nivelAcesso) {
            this.id = id;
            this.nome = nome;
            this.nivelAcesso = nivelAcesso;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getNivelAcesso() {
            return nivelAcesso;
        }

        public void setNivelAcesso(String nivelAcesso) {
            this.nivelAcesso = nivelAcesso;
        }
    }


}
