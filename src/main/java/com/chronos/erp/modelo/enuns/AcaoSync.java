package com.chronos.erp.modelo.enuns;

/**
 * Created by john on 04/07/18.
 */
public enum AcaoSync {
    ESTOQUE("010", "ESTOQUE");

    private String codigo;
    private String nome;

    AcaoSync(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
}
