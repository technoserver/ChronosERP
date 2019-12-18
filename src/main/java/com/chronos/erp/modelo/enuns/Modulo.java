package com.chronos.erp.modelo.enuns;

/**
 * Created by john on 16/10/17.
 */
public enum Modulo {
    VENDA("210"), PDV("200"), OS("220"), NFCe("240"), NFe("230"), DEVOLUCAO_PDV("250");

    private String codigo;


    Modulo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}

