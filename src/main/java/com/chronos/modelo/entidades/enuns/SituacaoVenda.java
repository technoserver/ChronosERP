package com.chronos.modelo.entidades.enuns;

/**
 * Created by john on 07/09/17.
 */
public enum SituacaoVenda {
    Digitacao("D"),
    Producao("P"),
    Expedicao("X"),
    Faturado("F"),
    Entregue("E"),
    Devolucao("V"),
    NotaFiscal("N");

    private String codigo;

    private SituacaoVenda(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
