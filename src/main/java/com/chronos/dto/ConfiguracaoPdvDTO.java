package com.chronos.dto;

/**
 * Created by john on 16/07/18.
 */
public class ConfiguracaoPdvDTO {


    private String certificadoDigitalCaminho;
    private String certificadoDigitalSenha;
    private String caminhoLogomarca;
    private String caminhoSchemas;
    private String webserviceUf;
    private Integer webserviceAmbiente;
    private String csc;
    private String idTokenCsc;
    private String mensagemCupom;
    private String observacaoPadrao;


    public ConfiguracaoPdvDTO() {
    }

    public ConfiguracaoPdvDTO(String certificadoDigitalCaminho, String certificadoDigitalSenha, String caminhoLogomarca, String caminhoSchemas, String webserviceUf, Integer webserviceAmbiente, String csc, String idTokenCsc, String mensagemCupom, String observacaoPadrao) {
        this.certificadoDigitalCaminho = certificadoDigitalCaminho;
        this.certificadoDigitalSenha = certificadoDigitalSenha;
        this.caminhoLogomarca = caminhoLogomarca;
        this.caminhoSchemas = caminhoSchemas;
        this.webserviceUf = webserviceUf;
        this.webserviceAmbiente = webserviceAmbiente;
        this.csc = csc;
        this.idTokenCsc = idTokenCsc;
        this.mensagemCupom = mensagemCupom;
        this.observacaoPadrao = observacaoPadrao;
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


    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public String getIdTokenCsc() {
        return idTokenCsc;
    }

    public void setIdTokenCsc(String idTokenCsc) {
        this.idTokenCsc = idTokenCsc;
    }

    public String getMensagemCupom() {
        return mensagemCupom;
    }

    public void setMensagemCupom(String mensagemCupom) {
        this.mensagemCupom = mensagemCupom;
    }

    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
    }
}
