package com.chronos.dto;

import com.chronos.modelo.entidades.NfeConfiguracao;
import com.chronos.modelo.entidades.PdvConfiguracao;

/**
 * Created by john on 09/10/17.
 */
public class ConfiguracaoEmissorDTO {

    private String certificadoDigitalSerie;

    private String certificadoDigitalCaminho;

    private String certificadoDigitalSenha;

    private Integer tipoEmissao;

    private Integer formatoImpressaoDanfe;

    private Integer processoEmissao;

    private String versaoProcessoEmissao;

    private String caminhoLogomarca;

    private String salvarXml;

    private String caminhoSalvarXml;

    private String caminhoSchemas;

    private String caminhoArquivoDanfe;

    private String caminhoSalvarPdf;

    private String webserviceUf;

    private Integer webserviceAmbiente;

    private String webserviceProxyHost;

    private Integer webserviceProxyPorta;

    private String webserviceProxyUsuario;

    private String webserviceProxySenha;

    private String webserviceVisualizar;

    private String emailServidorSmtp;

    private Integer emailPorta;

    private String emailUsuario;

    private String emailSenha;

    private String emailAssunto;

    private String emailAutenticaSsl;

    private String emailTexto;

    private String csc;

    private String observacaoPadrao;

    private int modelo;


    public ConfiguracaoEmissorDTO() {
    }

    public ConfiguracaoEmissorDTO(NfeConfiguracao configuracao) {
        this.certificadoDigitalSenha = configuracao.getCertificadoDigitalSenha();
        this.certificadoDigitalCaminho = configuracao.getCertificadoDigitalCaminho();
        this.caminhoLogomarca = configuracao.getCaminhoLogomarca();
        this.caminhoSchemas = configuracao.getCaminhoSchemas();
        this.observacaoPadrao = configuracao.getObservacaoPadrao();
        this.webserviceAmbiente = configuracao.getWebserviceAmbiente();
        this.processoEmissao = configuracao.getProcessoEmissao();
        this.modelo = 65;

    }

    public ConfiguracaoEmissorDTO(PdvConfiguracao configuracao) {
        this.certificadoDigitalSenha = configuracao.getCertificadoDigitalSenha();
        this.certificadoDigitalCaminho = configuracao.getCertificadoDigitalCaminho();
        this.caminhoLogomarca = configuracao.getCaminhoLogomarca();
        this.caminhoSchemas = configuracao.getCaminhoSchemas();
        this.observacaoPadrao = configuracao.getObservacaoPadrao();
        this.webserviceAmbiente = configuracao.getWebserviceAmbiente();
        this.processoEmissao = configuracao.getProcessoEmissao();
        this.csc = configuracao.getCodigoCsc();

        this.modelo = 55;
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

    public Integer getTipoEmissao() {
        return tipoEmissao;
    }

    public void setTipoEmissao(Integer tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

    public Integer getFormatoImpressaoDanfe() {
        return formatoImpressaoDanfe;
    }

    public void setFormatoImpressaoDanfe(Integer formatoImpressaoDanfe) {
        this.formatoImpressaoDanfe = formatoImpressaoDanfe;
    }

    public Integer getProcessoEmissao() {
        return processoEmissao;
    }

    public void setProcessoEmissao(Integer processoEmissao) {
        this.processoEmissao = processoEmissao;
    }

    public String getVersaoProcessoEmissao() {
        return versaoProcessoEmissao;
    }

    public void setVersaoProcessoEmissao(String versaoProcessoEmissao) {
        this.versaoProcessoEmissao = versaoProcessoEmissao;
    }

    public String getCaminhoLogomarca() {
        return caminhoLogomarca;
    }

    public void setCaminhoLogomarca(String caminhoLogomarca) {
        this.caminhoLogomarca = caminhoLogomarca;
    }

    public String getSalvarXml() {
        return salvarXml;
    }

    public void setSalvarXml(String salvarXml) {
        this.salvarXml = salvarXml;
    }

    public String getCaminhoSalvarXml() {
        return caminhoSalvarXml;
    }

    public void setCaminhoSalvarXml(String caminhoSalvarXml) {
        this.caminhoSalvarXml = caminhoSalvarXml;
    }

    public String getCaminhoSchemas() {
        return caminhoSchemas;
    }

    public void setCaminhoSchemas(String caminhoSchemas) {
        this.caminhoSchemas = caminhoSchemas;
    }

    public String getCaminhoArquivoDanfe() {
        return caminhoArquivoDanfe;
    }

    public void setCaminhoArquivoDanfe(String caminhoArquivoDanfe) {
        this.caminhoArquivoDanfe = caminhoArquivoDanfe;
    }

    public String getCaminhoSalvarPdf() {
        return caminhoSalvarPdf;
    }

    public void setCaminhoSalvarPdf(String caminhoSalvarPdf) {
        this.caminhoSalvarPdf = caminhoSalvarPdf;
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

    public String getWebserviceProxyHost() {
        return webserviceProxyHost;
    }

    public void setWebserviceProxyHost(String webserviceProxyHost) {
        this.webserviceProxyHost = webserviceProxyHost;
    }

    public Integer getWebserviceProxyPorta() {
        return webserviceProxyPorta;
    }

    public void setWebserviceProxyPorta(Integer webserviceProxyPorta) {
        this.webserviceProxyPorta = webserviceProxyPorta;
    }

    public String getWebserviceProxyUsuario() {
        return webserviceProxyUsuario;
    }

    public void setWebserviceProxyUsuario(String webserviceProxyUsuario) {
        this.webserviceProxyUsuario = webserviceProxyUsuario;
    }

    public String getWebserviceProxySenha() {
        return webserviceProxySenha;
    }

    public void setWebserviceProxySenha(String webserviceProxySenha) {
        this.webserviceProxySenha = webserviceProxySenha;
    }

    public String getWebserviceVisualizar() {
        return webserviceVisualizar;
    }

    public void setWebserviceVisualizar(String webserviceVisualizar) {
        this.webserviceVisualizar = webserviceVisualizar;
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

    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public int getModelo() {
        return modelo;
    }
}
