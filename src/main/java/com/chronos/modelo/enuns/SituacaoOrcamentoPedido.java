package com.chronos.modelo.enuns;

public enum SituacaoOrcamentoPedido {

    DIGITACAO("D"),
    NAO_APROVADO("N"),
    PENDENTE("P"),
    APROVADO("A"),
    FATURADO("F"),
    CANCELADO("C");

    private String codigo;

    SituacaoOrcamentoPedido(String codigo) {
        this.codigo = codigo;
    }

    public static SituacaoOrcamentoPedido valueOfCodigo(String codigo) {
        for (final SituacaoOrcamentoPedido situacao : SituacaoOrcamentoPedido.values()) {
            if (situacao.getCodigo().equals(codigo)) {
                return situacao;
            }
        }
        throw new IllegalArgumentException(String.format("Situação não definida."));
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
