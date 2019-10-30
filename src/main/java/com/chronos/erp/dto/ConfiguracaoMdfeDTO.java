package com.chronos.erp.dto;

public class ConfiguracaoMdfeDTO {

    private String certificadoDigitalCaminho;
    private String certificadoDigitalSenha;
    private String caminhoLogomarca;
    private String caminhoSchemas;
    private String webserviceUf;
    private Integer webserviceAmbiente;
    private String observacaoPadrao;
    private String rntrc;


    public ConfiguracaoMdfeDTO() {
    }

    public ConfiguracaoMdfeDTO(String certificadoDigitalCaminho, String certificadoDigitalSenha, String caminhoLogomarca, String caminhoSchemas, String webserviceUf, Integer webserviceAmbiente, String observacaoPadrao, String rntrc) {
        this.certificadoDigitalCaminho = certificadoDigitalCaminho;
        this.certificadoDigitalSenha = certificadoDigitalSenha;
        this.caminhoLogomarca = caminhoLogomarca;
        this.caminhoSchemas = caminhoSchemas;
        this.webserviceUf = webserviceUf;
        this.webserviceAmbiente = webserviceAmbiente;
        this.observacaoPadrao = observacaoPadrao;
        this.rntrc = rntrc;
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

    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
    }

    public String getRntrc() {
        return rntrc;
    }

    public void setRntrc(String rntrc) {
        this.rntrc = rntrc;
    }
}
