package com.chronos.modelo.entidades.enuns;

/**
 * Created by john on 01/02/18.
 */
public enum TipoLancamento {

    CREDITO("C"), DEBITO("D");

    private String codigo;


    TipoLancamento(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
