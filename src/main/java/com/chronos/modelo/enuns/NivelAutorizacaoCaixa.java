package com.chronos.modelo.enuns;

/**
 * Created by john on 04/10/17.
 */
public enum NivelAutorizacaoCaixa {


    /**
     * G=GERENTE
     */
    GERENTE("G"),

    /**
     * S=SUPERVISOR
     */
    SUPERVISOR("S"),

    /**
     * O=OPERADOR
     */
    OPERADOR("O");

    private String codigo;


    NivelAutorizacaoCaixa(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
