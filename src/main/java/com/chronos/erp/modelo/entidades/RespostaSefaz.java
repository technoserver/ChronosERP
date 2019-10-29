
package com.chronos.erp.modelo.entidades;

import java.io.Serializable;

public class RespostaSefaz implements Serializable {

    private static final long serialVersionUID = 1L;
    private String resposta;
    private boolean autorizado;
    private boolean cancelado;
    private String numeroRecibo;
    private String xmlEnviNfe;
    private String codigoUf;
    private String ambiente;

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getNumeroRecibo() {
        return numeroRecibo;
    }

    public void setNumeroRecibo(String numeroRecibo) {
        this.numeroRecibo = numeroRecibo;
    }

    public String getXmlEnviNfe() {
        return xmlEnviNfe;
    }

    public void setXmlEnviNfe(String xmlEnviNfe) {
        this.xmlEnviNfe = xmlEnviNfe;
    }

    public String getCodigoUf() {
        return codigoUf;
    }

    public void setCodigoUf(String codigoUf) {
        this.codigoUf = codigoUf;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }
}
