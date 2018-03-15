package com.chronos.modelo.enuns;

/**
 * Created by john on 04/10/17.
 */
public enum StatusMovimentoCaixa {


    ABERTO("A"),

    /**
     * F=FECHADO
     */
    FECHADO("F"),

    /**
     * T=FECHADO TEMPORARIAMENTE
     */
    FECHADO_TEMPORARIAMENTE("T");

    private String codigo;

    StatusMovimentoCaixa(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
