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
    DEVOLUCAO("DEVOLUÇÂO"),
    TRANSMITIR_NFE("TRANSMITIR NFE"),
    TRANSMITIR_NFCE("TRANSMITIR NFCE"),
    TRANSMITIR_MDFE("TRANSMITIR NDFE"),
    TRANSMITIR_CTE("TRANSMITIR CTE"),
    ENCERRAR_VENDA("ENCERRAR VENDA"),
    FATURAR_VENDA("FATURAR VENDA"),
    CANCELAR_VENDA("CANCELAR VENDA"),
    EXCLUIR_VENDA("EXCLUIR VENDA"),
    AJUSTE("AJUSTE"),
    LOGIN("LOGIN"),
    BAIXA_PARCELA("BAIXA PARCELA"),
    ENCERRAR_OS("ENCERRAR VENDA"),
    FATURAR_OS("ENCERRAR VENDA");

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
