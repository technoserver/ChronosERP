
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "CLIENTE")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DESDE")
    private Date desde;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CADASTRO")
    private Date dataCadastro;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ALTERACAO")
    private Date dataAlteracao;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @Column(name = "CONTA_TOMADOR")
    private String contaTomador;
    @Column(name = "GERA_FINANCEIRO")
    private String geraFinanceiro;
    @Column(name = "INDICADOR_PRECO")
    private String indicadorPreco;
    @Column(name = "PORCENTO_DESCONTO")
    private BigDecimal porcentoDesconto;
    @Column(name = "FORMA_DESCONTO")
    private String formaDesconto;
    @Column(name = "LIMITE_CREDITO")
    private BigDecimal limiteCredito;
    @Column(name = "TIPO_FRETE")
    private String tipoFrete;
    @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
    private String classificacaoContabilConta;
    @Column(name = "BLOQUEADO")
    private String bloqueado;
    @Column(name = "DIAS_BLOQUEIO")
    private Integer diasBloqueio;
    @JoinColumn(name = "ID_SITUACAO_FOR_CLI", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SituacaoForCli situacaoForCli;
    @JoinColumn(name = "ID_ATIVIDADE_FOR_CLI", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private AtividadeForCli atividadeForCli;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Pessoa pessoa;
    @JoinColumn(name = "ID_OPERACAO_FISCAL", referencedColumnName = "ID")
    @ManyToOne
    private TributOperacaoFiscal tributOperacaoFiscal;
    @JoinColumn(name = "ID_CONVENIO", referencedColumnName = "ID")
    @ManyToOne
    private Convenio convenio;
    @JoinColumn(name = "ID_TABELA_PRECO", referencedColumnName = "ID")
    @ManyToOne
    private TabelaPreco tabelaPreco;
    @JoinColumn(name = "ID_REGIAO", referencedColumnName = "ID")
    @ManyToOne
    private Regiao regiao;

    public Cliente() {
    }

    public Cliente(Integer id, String nome) {
        this.id = id;
        this.pessoa = new Pessoa(nome);
    }


    public Cliente(Integer id, Integer idpessoa, String nome) {
        this.id = id;
        this.pessoa = new Pessoa(idpessoa, nome);
    }


    public Cliente(Integer id, String nome, String email) {
        this.id = id;
        this.pessoa = new Pessoa(nome, email);
    }

    public Cliente(Integer id, String nome, String situacao, String bloquear) {
        this.id = id;
        this.pessoa = new Pessoa(nome);
        this.situacaoForCli = new SituacaoForCli(situacao, bloquear);
    }

    @PreUpdate
    @PrePersist
    private void preUpdate() {
        this.dataAlteracao = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * Conta vinculada ao plano de contas contábil.
     *
     * @return
     */
    public String getContaTomador() {
        return contaTomador;
    }

    /**
     * Conta vinculada ao plano de contas contábil.
     *
     * @param contaTomador
     */
    public void setContaTomador(String contaTomador) {
        this.contaTomador = contaTomador;
    }

    /**
     * Indicar se o cliente terá direito de comprar a prazo faturado e parcelado
     *
     * @return
     */

    public String getGeraFinanceiro() {
        return geraFinanceiro;
    }

    /**
     * Indicar se o cliente terá direito de comprar a prazo faturado e parcelado
     *
     * @param geraFinanceiro
     */
    public void setGeraFinanceiro(String geraFinanceiro) {
        this.geraFinanceiro = geraFinanceiro;
    }

    /**
     * T=tabela, P=Ultimo pedido: Indicar se os preços deste cliente será pela tabela informada ou ultimo pedido de venda
     *
     * @return
     */
    public String getIndicadorPreco() {
        return indicadorPreco;
    }

    /**
     * T=tabela, P=Ultimo pedido: Indicar se os preços deste cliente será pela tabela informada ou ultimo pedido de venda
     *
     * @param indicadorPreco
     */
    public void setIndicadorPreco(String indicadorPreco) {
        this.indicadorPreco = indicadorPreco;
    }

    public BigDecimal getPorcentoDesconto() {
        return porcentoDesconto;
    }

    public void setPorcentoDesconto(BigDecimal porcentoDesconto) {
        this.porcentoDesconto = porcentoDesconto;
    }

    /**
     * P=produto, F= fim do pedido
     *
     * @return
     */
    public String getFormaDesconto() {
        return formaDesconto;
    }

    /**
     * P=produto, F= fim do pedido
     *
     * @param formaDesconto
     */
    public void setFormaDesconto(String formaDesconto) {
        this.formaDesconto = formaDesconto;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    /**
     * E=Emitente | D=Destinatario | S=Sem frete | T=Terceiros
     *
     * @return
     */
    public String getTipoFrete() {
        return tipoFrete;
    }

    /**
     * E=Emitente | D=Destinatario | S=Sem frete | T=Terceiros
     *
     * @param tipoFrete
     */
    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public SituacaoForCli getSituacaoForCli() {
        return situacaoForCli;
    }

    public void setSituacaoForCli(SituacaoForCli situacaoForCli) {
        this.situacaoForCli = situacaoForCli;
    }

    public AtividadeForCli getAtividadeForCli() {
        return atividadeForCli;
    }

    public void setAtividadeForCli(AtividadeForCli atividadeForCli) {
        this.atividadeForCli = atividadeForCli;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    public String getClassificacaoContabilConta() {
        return classificacaoContabilConta;
    }

    public void setClassificacaoContabilConta(String classificacaoContabilConta) {
        this.classificacaoContabilConta = classificacaoContabilConta;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    /**
     * Cliente bloqueado - inadimplente
     *
     * @return
     */
    public String getBloqueado() {
        return bloqueado;
    }

    /**
     * Cliente bloqueado - inadimplente
     *
     * @param bloqueado
     */
    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }

    /**
     * Caso inadimplente, será bloqueado essa quantidade de dias dias contando a partir do vencimento
     *
     * @return
     */
    public Integer getDiasBloqueio() {
        return diasBloqueio;
    }

    /**
     * Caso inadimplente, será bloqueado essa quantidade de dias dias contando a partir do vencimento
     *
     * @param diasBloqueio
     */
    public void setDiasBloqueio(Integer diasBloqueio) {
        this.diasBloqueio = diasBloqueio;
    }

    public TabelaPreco getTabelaPreco() {
        return tabelaPreco;
    }

    public void setTabelaPreco(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    public Regiao getRegiao() {
        return regiao;
    }

    public void setRegiao(Regiao regiao) {
        this.regiao = regiao;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Cliente other = (Cliente) obj;
        return Objects.equals(this.id, other.id);
    }

}
