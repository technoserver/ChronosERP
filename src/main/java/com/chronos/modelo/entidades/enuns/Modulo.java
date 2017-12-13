package com.chronos.modelo.entidades.enuns;

/**
 * Created by john on 16/10/17.
 */
public enum Modulo {
    VENDA("210"), OS("220");

    Modulo(String codigo) {
        this.codigo = codigo;
    }


    private String codigo;


    public String getCodigo() {
        return codigo;
    }
}

