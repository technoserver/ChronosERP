
package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "CONTADOR")
public class Contador implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    @NotBlank
    private String nome;
    @Column(name = "CPF")
    private String cpf;
    @Column(name = "CNPJ")
    private String cnpj;
    @Column(name = "INSCRICAO_CRC", unique = true)
    private String inscricaoCrc;
    @Column(name = "UF_CRC")
    private String ufCrc;
    @Column(name = "FONE")
    private String fone;
    @Column(name = "FAX")
    private String fax;
    @Column(name = "LOGRADOURO")
    private String logradouro;
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "COMPLEMENTO")
    private String complemento;
    @Column(name = "BAIRRO")
    private String bairro;
    @Column(name = "CIDADE")
    private String cidade;
    @Column(name = "CEP")
    private String cep;
    @Column(name = "MUNICIPIO_IBGE")
    private Integer municipioIbge;
    @Column(name = "UF")
    private String uf;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "SITE")
    private String site;

    public Contador() {
        this.uf = "AL";
    }


    @PrePersist
    @PreUpdate
    private void prePersit() {
        this.cpf = cpf != null ? cpf.replaceAll("\\D", "") : "";
        this.cnpj = cnpj != null ? cnpj.replaceAll("\\D", "") : "";
        this.fone = fone != null ? fone.replaceAll("\\D", "") : "";
    }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoCrc() {
        return inscricaoCrc;
    }

    public void setInscricaoCrc(String inscricaoCrc) {
        this.inscricaoCrc = inscricaoCrc;
    }

    public String getUfCrc() {
        return ufCrc;
    }

    public void setUfCrc(String ufCrc) {
        this.ufCrc = ufCrc;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contador other = (Contador) obj;
        return Objects.equals(this.id, other.id);
    }


}
