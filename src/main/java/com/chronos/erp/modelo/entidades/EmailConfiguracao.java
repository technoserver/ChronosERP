package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 16/07/18.
 */
@Entity
@Table(name = "email_configuracao")
public class EmailConfiguracao implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "SERVIDOR_SMTP")
    private String servidorSmtp;
    @Column(name = "PORTA")
    private Integer porta;
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "SENHA")
    private String senha;
    @Column(name = "ASSUNTO")
    private String assunto;
    @Column(name = "AUTENTICA_SSL")
    private String autenticaSsl;
    @Column(name = "usa_tls")
    private String usaTls;
    @Column(name = "TEXTO")
    private String texto;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;


    public EmailConfiguracao() {
    }

    public EmailConfiguracao(Integer id, String assunto, String texto) {
        this.id = id;
        this.assunto = assunto;
        this.texto = texto;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServidorSmtp() {
        return servidorSmtp;
    }

    public void setServidorSmtp(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
    }

    public Integer getPorta() {
        return porta;
    }

    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getAutenticaSsl() {
        return autenticaSsl;
    }

    public void setAutenticaSsl(String autenticaSsl) {
        this.autenticaSsl = autenticaSsl;
    }

    public String getUsaTls() {
        return usaTls;
    }

    public void setUsaTls(String usaTls) {
        this.usaTls = usaTls;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmailConfiguracao)) return false;

        EmailConfiguracao that = (EmailConfiguracao) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
