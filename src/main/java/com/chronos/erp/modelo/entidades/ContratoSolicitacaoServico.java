
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "CONTRATO_SOLICITACAO_SERVICO")
public class ContratoSolicitacaoServico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_SOLICITACAO")
    private Date dataSolicitacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_DESEJADA_INICIO")
    private Date dataDesejadaInicio;
    @Column(name = "URGENTE")
    private String urgente;
    @Column(name = "STATUS_SOLICITACAO")
    private String statusSolicitacao;
    @Column(name = "DESCRICAO")
    private String descricao;
    @JoinColumn(name = "ID_CONTRATO_TIPO_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContratoTipoServico contratoTipoServico;
    @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Colaborador colaborador;
    @JoinColumn(name = "ID_SETOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Setor setor;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne
    private Fornecedor fornecedor;

    public ContratoSolicitacaoServico() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Date getDataDesejadaInicio() {
        return dataDesejadaInicio;
    }

    public void setDataDesejadaInicio(Date dataDesejadaInicio) {
        this.dataDesejadaInicio = dataDesejadaInicio;
    }

    public String getUrgente() {
        return urgente;
    }

    public void setUrgente(String urgente) {
        this.urgente = urgente;
    }

    public String getStatusSolicitacao() {
        return statusSolicitacao;
    }

    public void setStatusSolicitacao(String statusSolicitacao) {
        this.statusSolicitacao = statusSolicitacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ContratoTipoServico getContratoTipoServico() {
        return contratoTipoServico;
    }

    public void setContratoTipoServico(ContratoTipoServico contratoTipoServico) {
        this.contratoTipoServico = contratoTipoServico;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "ContratoSolicitacaoServico{" + "id=" + id + '}';
    }


}
