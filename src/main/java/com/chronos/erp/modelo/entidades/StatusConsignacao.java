package com.chronos.erp.modelo.entidades;

public enum StatusConsignacao {
    EDICAO("Edicao"),
    CONSIGNADO("Em consiguinação"),
    RECOLHIDO("Recolhido"),
    RECOLHIDO_PARCIALMENTE("Recolhido parcialmente"),
    VENDIDO("Vendido"),
    ENCERRADO("Encerrado");

    public String descricao;

    StatusConsignacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
