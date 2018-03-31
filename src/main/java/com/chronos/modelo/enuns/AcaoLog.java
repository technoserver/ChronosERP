package com.chronos.modelo.enuns;

/**
 * Created by john on 16/10/17.
 */
public enum AcaoLog {

    SELECT("SELECIONAR"),
    INSERT("INSERIR"),
    UPDATE("ATUALIZAR"),
    DELETE("EXCLUIR"),
    CANCELAR("CANCELAR"),
    INUTILIZAR("INUTILIZAR"),
    TRANSMITIR_NFE("TRANSMITIR NFE"),
    TRANSMITIR_NFCE("TRANSMITIR NFCE"),
    TRANSMITIR_MDFE("TRANSMITIR NDFE"),
    TRANSMITIR_CTE("TRANSMITIR CTE"),
    LOGIN("LOGIN"),
    BAIXA_PARCELA("BAIXA PARCELA");

    public String nome;


    AcaoLog(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}