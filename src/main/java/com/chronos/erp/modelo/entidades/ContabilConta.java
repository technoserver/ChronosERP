
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "CONTABIL_CONTA")
public class ContabilConta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CLASSIFICACAO")
    private String classificacao;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;
    @Column(name = "SITUACAO")
    private String situacao;
    @Column(name = "NATUREZA")
    private String natureza;
    @Column(name = "PATRIMONIO_RESULTADO")
    private String patrimonioResultado;
    @Column(name = "LIVRO_CAIXA")
    private String livroCaixa;
    @Column(name = "DFC")
    private String dfc;
    @Column(name = "ORDEM")
    private String ordem;
    @Column(name = "CODIGO_REDUZIDO")
    private String codigoReduzido;
    @Column(name = "CODIGO_EFD")
    private String codigoEfd;
    @JoinColumn(name = "ID_PLANO_CONTA_REF_SPED", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PlanoContaRefSped planoContaRefSped;
    @JoinColumn(name = "ID_CONTABIL_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private ContabilConta contabilConta;
    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PlanoConta planoConta;

    public ContabilConta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getPatrimonioResultado() {
        return patrimonioResultado;
    }

    public void setPatrimonioResultado(String patrimonioResultado) {
        this.patrimonioResultado = patrimonioResultado;
    }

    public String getLivroCaixa() {
        return livroCaixa;
    }

    public void setLivroCaixa(String livroCaixa) {
        this.livroCaixa = livroCaixa;
    }

    public String getDfc() {
        return dfc;
    }

    public void setDfc(String dfc) {
        this.dfc = dfc;
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public String getCodigoReduzido() {
        return codigoReduzido;
    }

    public void setCodigoReduzido(String codigoReduzido) {
        this.codigoReduzido = codigoReduzido;
    }

    public String getCodigoEfd() {
        return codigoEfd;
    }

    public void setCodigoEfd(String codigoEfd) {
        this.codigoEfd = codigoEfd;
    }

    public PlanoContaRefSped getPlanoContaRefSped() {
        return planoContaRefSped;
    }

    public void setPlanoContaRefSped(PlanoContaRefSped planoContaRefSped) {
        this.planoContaRefSped = planoContaRefSped;
    }

    public ContabilConta getContabilConta() {
        return contabilConta;
    }

    public void setContabilConta(ContabilConta contabilConta) {
        this.contabilConta = contabilConta;
    }

    public PlanoConta getPlanoConta() {
        return planoConta;
    }

    public void setPlanoConta(PlanoConta planoConta) {
        this.planoConta = planoConta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContabilConta that = (ContabilConta) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
