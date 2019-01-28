package com.chronos.dto;

public class ProdutoGradeDTO {

    private Integer idproduto;
    private Integer idatributo;
    private Integer idatributoDetalhe;
    private String codigoGrade;
    private String sigla;
    private String nomeAtributo;
    private String nomeProduto;
    private String nomeGrade;


    public Integer getIdproduto() {
        return idproduto;
    }

    public void setIdproduto(Integer idproduto) {
        this.idproduto = idproduto;
    }

    public Integer getIdatributo() {
        return idatributo;
    }

    public void setIdatributo(Integer idatributo) {
        this.idatributo = idatributo;
    }

    public Integer getIdatributoDetalhe() {
        return idatributoDetalhe;
    }

    public void setIdatributoDetalhe(Integer idatributoDetalhe) {
        this.idatributoDetalhe = idatributoDetalhe;
    }

    public String getCodigoGrade() {
        return codigoGrade;
    }

    public void setCodigoGrade(String codigoGrade) {
        this.codigoGrade = codigoGrade;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNomeAtributo() {
        return nomeAtributo;
    }

    public void setNomeAtributo(String nomeAtributo) {
        this.nomeAtributo = nomeAtributo;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }


    public String getNomeGrade() {
        return nomeGrade;
    }

    public void setNomeGrade(String nomeGrade) {
        this.nomeGrade = nomeGrade;
    }

    public String montarNome() {
        this.nomeGrade = this.nomeProduto + " " + this.sigla + "=" + this.nomeAtributo + "; ";
        return this.nomeGrade;
    }

    public String montarCodigo() {
        this.codigoGrade = this.idproduto + "." + this.idatributo + "." + this.idatributoDetalhe;
        return this.codigoGrade;
    }

}
