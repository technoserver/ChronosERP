package com.chronos.modelo.enuns;

/**
 * Created by john on 16/10/17.
 */
public enum Modulo {
    VENDA("210"), OS("220"), NFCe("240"), NFe("230");

    private String codigo;


    Modulo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}

