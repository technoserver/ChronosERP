package com.chronos.modelo.entidades.enuns;

/**
 * Created by john on 09/09/17.
 */
public enum FormaPagamento {
    AVISTA("0"), APRAZO("1"), OUTROS("2");

    private String codigo;

    FormaPagamento(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
