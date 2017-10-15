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

    public static SituacaoVenda valueOfCodigo(String codigo) {
        for (final SituacaoVenda situacao : SituacaoVenda.values()) {
            if (situacao.getCodigo().equals(codigo)) {
                return situacao;
            }
        }
        throw new IllegalArgumentException(String.format("Situação de venda não definida."));
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
