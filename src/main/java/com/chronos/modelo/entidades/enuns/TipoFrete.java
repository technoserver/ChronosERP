package com.chronos.modelo.entidades.enuns;

/**
 * Created by john on 07/09/17.
 */
public enum TipoFrete {
    CIF("C"), FOB("F");

    private String codigo;

    private TipoFrete(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}