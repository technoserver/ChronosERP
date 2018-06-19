/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.enuns;

import java.util.Objects;

/**
 * @author john
 */
public enum TipoEmitente {

    PRESTADOR_SERVICO(1, "Prestador de serviço de transporte"),
    CARGA_PROPRIA(2, "Transportador de Carga Própria");

    private Integer codigo;
    private String nomeExibicao;

    private TipoEmitente(Integer codigo, String nomeExibicao) {
        this.codigo = codigo;
        this.nomeExibicao = nomeExibicao;
    }

    public static TipoEmitente valueOfCodigo(final Integer codigo) {
        for (final TipoEmitente tipo : TipoEmitente.values()) {
            if (Objects.equals(tipo.getCodigo(), codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException(String.format("Tipo de emitente não definido"));
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
