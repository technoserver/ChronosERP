package com.chronos.erp.modelo.enuns;

/**
 * Created by john on 07/09/17.
 */
public enum TipoFrete {
    CIF("C"), FOB("F");

    private String codigo;

    TipoFrete(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
