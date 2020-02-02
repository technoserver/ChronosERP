package com.chronos.erp.dto;

import java.math.BigDecimal;

public class NomeValorDTO {
    private String nome;
    private BigDecimal valor;


    public NomeValorDTO(String nome, BigDecimal valor) {
        this.nome = nome;
        this.valor = valor;
    }
}
