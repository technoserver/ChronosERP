package com.chronos.erp.dto;

/**
 * Created by john on 15/06/18.
 */
public class ConfEmissorDTO {

    private Integer codigoUf;
    private String caminhoSchemas;
    private String caminhoCertificado;
    private String senhaCertificado;
    private Integer webserviceAmbiente;
    private String versao;

    public ConfEmissorDTO(Integer codigoUf, String caminhoSchemas, String caminhoCertificado, String senhaCertificado, Integer webserviceAmbiente, String versao) {
        this.codigoUf = codigoUf;
        this.caminhoSchemas = caminhoSchemas;
        this.caminhoCertificado = caminhoCertificado;
        this.senhaCertificado = senhaCertificado;
        this.webserviceAmbiente = webserviceAmbiente;
        this.versao = versao;
    }

    public Integer getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(Integer codigoUf) {
        this.codigoUf = codigoUf;
    }

    public String getCaminhoSchemas() {
        return caminhoSchemas;
    }

    public void setCaminhoSchemas(String caminhoSchemas) {
        this.caminhoSchemas = caminhoSchemas;
    }

    public String getCaminhoCertificado() {
        return caminhoCertificado;
    }

    public void setCaminhoCertificado(String caminhoCertificado) {
        this.caminhoCertificado = caminhoCertificado;
    }

    public String getSenhaCertificado() {
        return senhaCertificado;
    }

    public void setSenhaCertificado(String senhaCertificado) {
        this.senhaCertificado = senhaCertificado;
    }

    public Integer getWebserviceAmbiente() {
        return webserviceAmbiente;
    }

    public void setWebserviceAmbiente(Integer webserviceAmbiente) {
        this.webserviceAmbiente = webserviceAmbiente;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }
}
