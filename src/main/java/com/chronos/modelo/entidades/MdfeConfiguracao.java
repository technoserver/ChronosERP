package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by john on 19/06/18.
 */
@Entity
@Table(name = "MDFE_CONFIGURACAO")
public class MdfeConfiguracao implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "rntrc")
    private String rntrc;
    @Column(name = "CERTIFICADO_DIGITAL_SERIE")
    private String certificadoDigitalSerie;
    @Column(name = "CERTIFICADO_DIGITAL_CAMINHO")
    private String certificadoDigitalCaminho;
    @Column(name = "CERTIFICADO_DIGITAL_SENHA")
    private String certificadoDigitalSenha;
    @Column(name = "CAMINHO_LOGOMARCA")
    private String caminhoLogomarca;
    @Column(name = "CAMINHO_SCHEMAS")
    private String caminhoSchemas;
    @Column(name = "WEBSERVICE_UF")
    private String webserviceUf;
    @Column(name = "WEBSERVICE_AMBIENTE")
    private Integer webserviceAmbiente;
    @Column(name = "EMAIL_SERVIDOR_SMTP")
    private String emailServidorSmtp;
    @Column(name = "EMAIL_PORTA")
    private Integer emailPorta;
    @Column(name = "EMAIL_USUARIO")
    private String emailUsuario;
    @Column(name = "EMAIL_SENHA")
    private String emailSenha;
    @Column(name = "EMAIL_ASSUNTO")
    private String emailAssunto;
    @Column(name = "EMAIL_AUTENTICA_SSL")
    private String emailAutenticaSsl;
    @Column(name = "EMAIL_TEXTO")
    private String emailTexto;
    @Column(name = "observacao_padrao")
    private String observacaoPadrao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }

    public String getCertificadoDigitalSerie() {
        return certificadoDigitalSerie;
    }

    public void setCertificadoDigitalSerie(String certificadoDigitalSerie) {
        this.certificadoDigitalSerie = certificadoDigitalSerie;
    }

    public String getCertificadoDigitalCaminho() {
        return certificadoDigitalCaminho;
    }

    public void setCertificadoDigitalCaminho(String certificadoDigitalCaminho) {
        this.certificadoDigitalCaminho = certificadoDigitalCaminho;
    }

    public String getCertificadoDigitalSenha() {
        return certificadoDigitalSenha;
    }

    public void setCertificadoDigitalSenha(String certificadoDigitalSenha) {
        this.certificadoDigitalSenha = certificadoDigitalSenha;
    }

    public String getCaminhoLogomarca() {
        return caminhoLogomarca;
    }

    public void setCaminhoLogomarca(String caminhoLogomarca) {
        this.caminhoLogomarca = caminhoLogomarca;
    }

    public String getCaminhoSchemas() {
        return caminhoSchemas;
    }

    public void setCaminhoSchemas(String caminhoSchemas) {
        this.caminhoSchemas = caminhoSchemas;
    }

    public String getWebserviceUf() {
        return webserviceUf;
    }

    public void setWebserviceUf(String webserviceUf) {
        this.webserviceUf = webserviceUf;
    }

    public Integer getWebserviceAmbiente() {
        return webserviceAmbiente;
    }

    public void setWebserviceAmbiente(Integer webserviceAmbiente) {
        this.webserviceAmbiente = webserviceAmbiente;
    }

    public String getEmailServidorSmtp() {
        return emailServidorSmtp;
    }

    public void setEmailServidorSmtp(String emailServidorSmtp) {
        this.emailServidorSmtp = emailServidorSmtp;
    }

    public Integer getEmailPorta() {
        return emailPorta;
    }

    public void setEmailPorta(Integer emailPorta) {
        this.emailPorta = emailPorta;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getEmailSenha() {
        return emailSenha;
    }

    public void setEmailSenha(String emailSenha) {
        this.emailSenha = emailSenha;
    }

    public String getEmailAssunto() {
        return emailAssunto;
    }

    public void setEmailAssunto(String emailAssunto) {
        this.emailAssunto = emailAssunto;
    }

    public String getEmailAutenticaSsl() {
        return emailAutenticaSsl;
    }

    public void setEmailAutenticaSsl(String emailAutenticaSsl) {
        this.emailAutenticaSsl = emailAutenticaSsl;
    }

    public String getEmailTexto() {
        return emailTexto;
    }

    public void setEmailTexto(String emailTexto) {
        this.emailTexto = emailTexto;
    }

    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
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
        if (!(o instanceof MdfeConfiguracao)) return false;

        MdfeConfiguracao that = (MdfeConfiguracao) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
