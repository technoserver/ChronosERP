package com.chronos.modelo.view;

import javax.persistence.*;

@Entity
@Table(name = "view_pessoa_fornecedor")
public class ViewPessoaFornecedor {

    @Id
    @Basic
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "nome")
    private String nome;
    @Basic
    @Column(name = "cpf_cnpj")
    private String cpfCnpj;
    @Basic
    @Column(name = "tipo")
    private String tipo;
    @Basic
    @Column(name = "logradouro")
    private String logradouro;
    @Basic
    @Column(name = "numero")
    private String numero;
    @Basic
    @Column(name = "complemento")
    private String complemento;
    @Basic
    @Column(name = "bairro")
    private String bairro;
    @Basic
    @Column(name = "cidade")
    private String cidade;
    @Basic
    @Column(name = "cep")
    private String cep;
    @Basic
    @Column(name = "municipio_ibge")
    private Integer municipioIbge;
    @Basic
    @Column(name = "uf")
    private String uf;
    @Basic
    @Column(name = "fone")
    private String fone;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "site")
    private String site;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Integer getMunicipioIbge() {
        return municipioIbge;
    }

    public void setMunicipioIbge(Integer municipioIbge) {
        this.municipioIbge = municipioIbge;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
