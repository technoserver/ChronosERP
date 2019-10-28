package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ORCAMENTO_FLUXO_CAIXA")
public class OrcamentoFluxoCaixa implements Serializable {

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
    @JoinColumn(name = "ID_ORC_FLUXO_CAIXA_PERIODO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private OrcamentoFluxoCaixaPeriodo orcamentoFluxoCaixaPeriodo;
    @OrderBy
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orcamentoFluxoCaixa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoFluxoCaixaDetalhe> listaOrcamentoFluxoCaixaDetalhe;
    @Transient
    private PlanoNaturezaFinanceira planoNaturezaFinanceira;

    public OrcamentoFluxoCaixa() {
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

    public OrcamentoFluxoCaixaPeriodo getOrcamentoFluxoCaixaPeriodo() {
        return orcamentoFluxoCaixaPeriodo;
    }

    public void setOrcamentoFluxoCaixaPeriodo(OrcamentoFluxoCaixaPeriodo orcamentoFluxoCaixaPeriodo) {
        this.orcamentoFluxoCaixaPeriodo = orcamentoFluxoCaixaPeriodo;
    }

    public PlanoNaturezaFinanceira getPlanoNaturezaFinanceira() {
        return planoNaturezaFinanceira;
    }

    public void setPlanoNaturezaFinanceira(PlanoNaturezaFinanceira planoNaturezaFinanceira) {
        this.planoNaturezaFinanceira = planoNaturezaFinanceira;
    }

    public List<OrcamentoFluxoCaixaDetalhe> getListaOrcamentoFluxoCaixaDetalhe() {
        return listaOrcamentoFluxoCaixaDetalhe;
    }

    public void setListaOrcamentoFluxoCaixaDetalhe(List<OrcamentoFluxoCaixaDetalhe> listaOrcamentoFluxoCaixaDetalhe) {
        this.listaOrcamentoFluxoCaixaDetalhe = listaOrcamentoFluxoCaixaDetalhe;
    }

    @Override
    public String toString() {
        return "OrcamentoFluxoCaixa{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final OrcamentoFluxoCaixa other = (OrcamentoFluxoCaixa) obj;
        return Objects.equals(this.id, other.id);
    }


}
