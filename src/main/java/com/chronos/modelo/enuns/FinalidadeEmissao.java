/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.enuns;

import java.util.Objects;

/**
 * @author Usuario
 */
public enum FinalidadeEmissao {

    NORMAL(1, "Normal"),
    DEVOLUCAO(4, "Devolução"),
    COMPLEMENTAR(2, "Complementar"),
    AJUSTE(3, "Ajuste");

    private Integer codigo;
    private String nomeExibicao;

    FinalidadeEmissao(Integer codigo, String nomeExibicao) {
        this.codigo = codigo;
        this.nomeExibicao = nomeExibicao;
    }

    public static FinalidadeEmissao valueOfCodigo(final Integer codigo) {
        for (final FinalidadeEmissao formato : FinalidadeEmissao.values()) {
            if (Objects.equals(formato.getCodigo(), codigo)) {
                return formato;
            }
        }
        throw new IllegalArgumentException(String.format("Finalidade de emissão não definida."));
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

}
