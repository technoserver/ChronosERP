
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PESSOA_ENDERECO")
public class PessoaEndereco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;   
    @NotBlank
    @Column(name = "LOGRADOURO")
    private String logradouro;
    @NotBlank
    @Column(name = "NUMERO")
    private String numero;
    @Column(name = "COMPLEMENTO")
    private String complemento;
    @Column(name = "BAIRRO")
    @NotBlank
    private String bairro;
    @Column(name = "CIDADE")
    @NotBlank
    private String cidade;
    @Column(name = "CEP")
    @NotBlank
    private String cep;
    @Column(name = "FONE")
    private String fone;
    @Column(name = "MUNICIPIO_IBGE")
    private Integer municipioIbge;
    @Column(name = "UF")
    @NotBlank
    private String uf;
    @Column(name = "PRINCIPAL")
    private String principal;
    @Column(name = "ENTREGA")
    private String entrega;
    @Column(name = "COBRANCA")
    private String cobranca;
    @Column(name = "CORRESPONDENCIA")
    private String correspondencia;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Pessoa pessoa;

    public PessoaEndereco() {
    }
    @PrePersist
    @PreUpdate
    private void prePersist(){
      cep = cep!=null ? cep.replaceAll("\\D",""):"";
      fone = fone!=null ? fone.replaceAll("\\D",""):"";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
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

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getCobranca() {
        return cobranca;
    }

    public void setCobranca(String cobranca) {
        this.cobranca = cobranca;
    }

    public String getCorrespondencia() {
        return correspondencia;
    }

    public void setCorrespondencia(String correspondencia) {
        this.correspondencia = correspondencia;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
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
        final PessoaEndereco other = (PessoaEndereco) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.principal, other.principal)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return logradouro;
    }

}
