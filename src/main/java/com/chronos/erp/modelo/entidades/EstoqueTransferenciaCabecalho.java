package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "estoque_transferencia_cabecalho")
public class EstoqueTransferenciaCabecalho implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "id_nfe_cabecalho")
    private Integer idnfecabeclaho;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor_total", precision = 18, scale = 6)
    private BigDecimal valorTotal;
    private Character status;
    @Column(length = 2147483647)
    private String observacao;
    @JoinColumn(name = "id_colaborador", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @JoinColumn(name = "id_empresa_destino", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresaDestino;
    @JoinColumn(name = "id_empresa_origem", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Empresa empresaOrigem;
    @JoinColumn(name = "id_tribut_grupo_tributario", referencedColumnName = "id")
    @ManyToOne
    private TributGrupoTributario tributGrupoTributario;
    @JoinColumn(name = "id_tribut_operacao_fiscal", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private TributOperacaoFiscal tributOperacaoFiscal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estoqueTransferenciaCabecalho", orphanRemoval = true)
    private List<EstoqueTransferenciaDetalhe> listEstoqueTransferenciaDetalhe;

    public EstoqueTransferenciaCabecalho() {
        this.data = new Date();
        this.status = 'A';
        this.listEstoqueTransferenciaDetalhe = new ArrayList<>();
    }

    public EstoqueTransferenciaCabecalho(Integer id) {
        this.id = id;
    }

    public EstoqueTransferenciaCabecalho(Integer id, Date data) {
        this.id = id;
        this.data = data;
    }

    public EstoqueTransferenciaCabecalho(Integer id, String empresaOrigem, String empresaDestino, Date data, BigDecimal valorTotal) {
        this.id = id;
        this.empresaOrigem = new Empresa(0, empresaOrigem);
        this.empresaDestino = new Empresa(0, empresaDestino);
        this.data = data;
        this.valorTotal = valorTotal;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdnfecabeclaho() {
        return idnfecabeclaho;
    }

    public void setIdnfecabeclaho(Integer idnfecabeclaho) {
        this.idnfecabeclaho = idnfecabeclaho;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Empresa getEmpresaDestino() {
        return empresaDestino;
    }

    public void setEmpresaDestino(Empresa empresaDestino) {
        this.empresaDestino = empresaDestino;
    }

    public Empresa getEmpresaOrigem() {
        return empresaOrigem;
    }

    public void setEmpresaOrigem(Empresa empresaOrigem) {
        this.empresaOrigem = empresaOrigem;
    }


    public TributGrupoTributario getTributGrupoTributario() {
        return tributGrupoTributario;
    }

    public void setTributGrupoTributario(TributGrupoTributario tributGrupoTributario) {
        this.tributGrupoTributario = tributGrupoTributario;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    public List<EstoqueTransferenciaDetalhe> getListEstoqueTransferenciaDetalhe() {
        return listEstoqueTransferenciaDetalhe;
    }

    public void setListEstoqueTransferenciaDetalhe(List<EstoqueTransferenciaDetalhe> listEstoqueTransferenciaDetalhe) {
        this.listEstoqueTransferenciaDetalhe = listEstoqueTransferenciaDetalhe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstoqueTransferenciaCabecalho)) {
            return false;
        }
        EstoqueTransferenciaCabecalho other = (EstoqueTransferenciaCabecalho) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "model.EstoqueTransferenciaCabecalho[ id=" + id + " ]";
    }
}
