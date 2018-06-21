package com.chronos.dto;

import com.chronos.modelo.entidades.MdfeCabecalho;
import com.chronos.modelo.entidades.NfeCabecalho;

/**
 * Created by john on 20/06/18.
 */
public class ChaveAcessoDTO {

    private String codigoUF;
    private String cnpj;
    private String serie;
    private String modelo;
    private String numero;
    private String codigoNumerico;


    public ChaveAcessoDTO(MdfeCabecalho mdfe) {
        this.codigoNumerico = mdfe.getCodigoNumerico();
        this.modelo = mdfe.getModelo();
        this.cnpj = mdfe.getMdfeEmitente().getCnpj();
        this.numero = mdfe.getNumeroMdfe();
        this.codigoUF = mdfe.getUf().toString();
    }

    public ChaveAcessoDTO(NfeCabecalho nfe) {
        this.codigoNumerico = nfe.getCodigoNumerico();
        this.modelo = nfe.getCodigoModelo();
        this.cnpj = nfe.getEmitente().getCpfCnpj();
        this.numero = nfe.getNumero();
        this.codigoUF = nfe.getUfEmitente().toString();
    }


    public String getCodigoUF() {
        return codigoUF;
    }

    public void setCodigoUF(String codigoUF) {
        this.codigoUF = codigoUF;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoNumerico() {
        return codigoNumerico;
    }

    public void setCodigoNumerico(String codigoNumerico) {
        this.codigoNumerico = codigoNumerico;
    }
}
