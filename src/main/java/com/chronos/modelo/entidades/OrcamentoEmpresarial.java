package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "ORCAMENTO_EMPRESARIAL")
public class OrcamentoEmpresarial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIAL")
    private Date dataInicial;
    @Column(name = "NUMERO_PERIODOS")
    private Integer numeroPeriodos;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_BASE")
    private Date dataBase;
    @JoinColumn(name = "ID_ORCAMENTO_PERIODO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoPeriodo orcamentoPeriodo;
    @OrderBy
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orcamentoEmpresarial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoDetalhe> listaOrcamentoDetalhe;
    @Transient
    private PlanoNaturezaFinanceira planoNaturezaFinanceira;

    public OrcamentoEmpresarial() {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Integer getNumeroPeriodos() {
        return numeroPeriodos;
    }

    public void setNumeroPeriodos(Integer numeroPeriodos) {
        this.numeroPeriodos = numeroPeriodos;
    }

    public Date getDataBase() {
        return dataBase;
    }

    public void setDataBase(Date dataBase) {
        this.dataBase = dataBase;
    }

    public OrcamentoPeriodo getOrcamentoPeriodo() {
        return orcamentoPeriodo;
    }

    public void setOrcamentoPeriodo(OrcamentoPeriodo orcamentoPeriodo) {
        this.orcamentoPeriodo = orcamentoPeriodo;
    }

  
    public List<OrcamentoDetalhe> getListaOrcamentoDetalhe() {
        return listaOrcamentoDetalhe;
    }

    public void setListaOrcamentoDetalhe(List<OrcamentoDetalhe> listaOrcamentoDetalhe) {
        this.listaOrcamentoDetalhe = listaOrcamentoDetalhe;
    }

    public PlanoNaturezaFinanceira getPlanoNaturezaFinanceira() {
        return planoNaturezaFinanceira;
    }

    public void setPlanoNaturezaFinanceira(PlanoNaturezaFinanceira planoNaturezaFinanceira) {
        this.planoNaturezaFinanceira = planoNaturezaFinanceira;
    }

    @Override
    public String toString() {
        return "OrcamentoEmpresarial{" + "id=" + id + '}';
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final OrcamentoEmpresarial other = (OrcamentoEmpresarial) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
