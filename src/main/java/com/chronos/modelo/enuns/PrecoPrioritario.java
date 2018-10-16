package com.chronos.modelo.enuns;

public enum PrecoPrioritario {

    VALOR_VENDA("Valor de venda"),
    PRECO_ATACADO("Valor em atacado"),
    TABELA_PRECO("Tabela de preço"),
    PRODUTO_PROMOCIONAL("Valor promocional"),
    MENOR_PRECO("Menor preço");

    private String descricao;

    PrecoPrioritario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}
