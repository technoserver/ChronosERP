
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "FISCAL_LIVRO")
public class FiscalLivro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fiscalLivro", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FiscalTermo> listaFiscalTermo;

    public FiscalLivro() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }


    public Set<FiscalTermo> getListaFiscalTermo() {
        return listaFiscalTermo;
    }

    public void setListaFiscalTermo(Set<FiscalTermo> listaFiscalTermo) {
        this.listaFiscalTermo = listaFiscalTermo;
    }

}
