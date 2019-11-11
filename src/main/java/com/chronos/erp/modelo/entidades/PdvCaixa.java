package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "PDV_CAIXA")
//@NamedQuery(name = "PdvCaixa.existe",query = "SELECT c from PdvCaixa c where c.codigo = ?1 or c.web = 'S'")
public class PdvCaixa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "codigo")
    @NotBlank
    private String codigo;
    @Column(name = "NOME")
    @NotBlank
    private String nome;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Column(name = "web")
    @NotBlank
    private String web;


    public PdvCaixa() {
        this.dataCadastro = new Date();
        this.web = "N";
    }

    public PdvCaixa(String codigo) {
        this.codigo = codigo;
    }

    public PdvCaixa(Integer id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public PdvCaixa(Integer id, String codigo, String nome) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
    }


    public PdvCaixa(Integer id) {
        this.id = id;
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

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PdvCaixa other = (PdvCaixa) obj;
        return Objects.equals(this.id, other.id);
    }

}
