package com.chronos.erp.modelo.enuns;

/**
 * Created by john on 28/09/17.
 */
public enum StatusTransmissao {

    EDICAO(0, "Em Edição"),
    SALVA(1, "Salva"),
    VALIDADA(2, "Validada"),
    ASSINADA(3, "Assinada"),
    ENVIADA(4, "Enviada"),
    AUTORIZADA(5, "Autorizada"),
    CANCELADA(6, "Cancelada"),
    ENCERRADO(7, "Encerrada"),
    DUPLICIDADE(8, "Duplicidade"),
    SCHEMA_INVALIDO(9, "Schema invalido");

    private int codigo;
    private String nomeExibicao;


    StatusTransmissao(int codigo, String nomeExibicao) {
        this.codigo = codigo;
        this.nomeExibicao = nomeExibicao;
    }

    public static StatusTransmissao valueOfCodigo(final int codigo) {
        for (final StatusTransmissao status : StatusTransmissao.values()) {
            if (status.getCodigo() == codigo) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format("Status não definido."));
    }


    public static boolean isAutorizado(int status) {
        return valueOfCodigo(status) == StatusTransmissao.AUTORIZADA;
    }

    public static boolean isCancelada(int status) {
        return valueOfCodigo(status) == StatusTransmissao.CANCELADA;
    }

    public static boolean isEncerrada(int status) {
        return valueOfCodigo(status) == StatusTransmissao.ENCERRADO;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }
}
