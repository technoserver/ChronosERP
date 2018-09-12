/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.enuns;

/**
 * @author john
 */
public enum TipoCst {

    ICMS("ICMS"),
    IPI("IPI"),
    PIS("PIS"),
    COFINS("COFINS");

    private final String descricao;

    TipoCst(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
