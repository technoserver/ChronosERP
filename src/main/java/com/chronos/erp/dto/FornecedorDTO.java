package com.chronos.erp.dto;

public class FornecedorDTO {

    private Integer id;
    private String razaoSocial;
    private String cpfCnpj;
    private String tipo;
    private String cidade;
    private String uf;
    private String fone;


    public FornecedorDTO(Integer id, String razaoSocial, String cpfCnpj, String tipo, String cidade, String uf, String fone) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cpfCnpj = cpfCnpj;
        this.tipo = tipo;
        this.cidade = cidade;
        this.uf = uf;
        this.fone = fone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
}