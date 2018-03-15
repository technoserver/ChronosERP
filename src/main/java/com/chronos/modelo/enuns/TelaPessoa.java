package com.chronos.modelo.enuns;

/**
 * Created by john on 25/07/17.
 */
public enum TelaPessoa {

    CLIENTE("C","Cliente"),
    FORNECEDOR("F","Fornecedor"),
    TRANSPORTADORA("T","Transportadora"),
    COLABORADOR("CL","Colaborador");


    private String nome;
    private String codigo;

    TelaPessoa(String codigo, String nome ) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
