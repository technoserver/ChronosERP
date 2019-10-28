package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "ESTOQUE_REAJUSTE_CABECALHO")
public class EstoqueReajusteCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_REAJUSTE")
    private Date dataReajuste;
    @Column(name = "PORCENTAGEM")
    private BigDecimal porcentagem;
    @Column(name = "TIPO_REAJUSTE")
    private String tipoReajuste;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estoqueReajusteCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EstoqueReajusteDetalhe> listaEstoqueReajusteDetalhe;

    public EstoqueReajusteCabecalho() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataReajuste() {
        return dataReajuste;
    }

    public void setDataReajuste(Date dataReajuste) {
        this.dataReajuste = dataReajuste;
    }

    public BigDecimal getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(BigDecimal porcentagem) {
        this.porcentagem = porcentagem;
    }

    public String getTipoReajuste() {
        return tipoReajuste;
    }

    public void setTipoReajuste(String tipoReajuste) {
        this.tipoReajuste = tipoReajuste;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }


    public Set<EstoqueReajusteDetalhe> getListaEstoqueReajusteDetalhe() {
        return listaEstoqueReajusteDetalhe;
    }

    public void setListaEstoqueReajusteDetalhe(Set<EstoqueReajusteDetalhe> listaEstoqueReajusteDetalhe) {
        this.listaEstoqueReajusteDetalhe = listaEstoqueReajusteDetalhe;
    }

    @Override
    public String toString() {
        return "EstoqueReajusteCabecalho{" + "id=" + id + '}';
    }


}
