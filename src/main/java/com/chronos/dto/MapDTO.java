package com.chronos.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 26/10/17.
 */
public class MapDTO implements Serializable, Comparable<MapDTO> {

    private String descricao;
    private BigDecimal valor;

    public MapDTO(String descricao, BigDecimal valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public int compareTo(MapDTO outro) {
        if (this.getDescricao().equals(outro.getDescricao())) {
            return -1;
        }
        if (!this.getDescricao().equals(outro.getDescricao())) {
            return 1;
        }

        return 0;
    }
}
