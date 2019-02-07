package com.chronos.modelo.enuns;

public enum SituacaoOrcamentoPedido {

    PENDENTE("P"),
    APROVADO("A"),
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
