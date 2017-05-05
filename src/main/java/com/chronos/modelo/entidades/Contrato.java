package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CONTRATO")
public class Contrato implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "NUMERO")
   private String numero;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "DESCRICAO")
   private String descricao;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_CADASTRO")
   private Date dataCadastro;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_INICIO_VIGENCIA")
   private Date dataInicioVigencia;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_FIM_VIGENCIA")
   private Date dataFimVigencia;
   @Column(name = "DIA_FATURAMENTO")
   private String diaFaturamento;
   @Column(name = "VALOR")
   private BigDecimal valor;
   @Column(name = "QUANTIDADE_PARCELAS")
   private Integer quantidadeParcelas;
   @Column(name = "INTERVALO_ENTRE_PARCELAS")
   private Integer intervaloEntreParcelas;
   @Column(name = "OBSERVACAO")
   private String observacao;
   @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
   private String classificacaoContabilConta;
   @JoinColumn(name = "ID_TIPO_CONTRATO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private TipoContrato tipoContrato;
   @JoinColumn(name = "ID_SOLICITACAO_SERVICO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private ContratoSolicitacaoServico contratoSolicitacaoServico;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<ContratoHistFaturamento> listaContratoHistFaturamento;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<ContratoPrevFaturamento> listaContratoPrevFaturamento;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<ContratoHistoricoReajuste> listaContratoHistoricoReajuste;

   public Contrato() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getNumero() {
      return numero;
   }

   public void setNumero(String numero) {
      this.numero = numero;
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

   public Date getDataCadastro() {
      return dataCadastro;
   }

   public void setDataCadastro(Date dataCadastro) {
      this.dataCadastro = dataCadastro;
   }

   public Date getDataInicioVigencia() {
      return dataInicioVigencia;
   }

   public void setDataInicioVigencia(Date dataInicioVigencia) {
      this.dataInicioVigencia = dataInicioVigencia;
   }

   public Date getDataFimVigencia() {
      return dataFimVigencia;
   }

   public void setDataFimVigencia(Date dataFimVigencia) {
      this.dataFimVigencia = dataFimVigencia;
   }

   public String getDiaFaturamento() {
      return diaFaturamento;
   }

   public void setDiaFaturamento(String diaFaturamento) {
      this.diaFaturamento = diaFaturamento;
   }

   public BigDecimal getValor() {
      return valor;
   }

   public void setValor(BigDecimal valor) {
      this.valor = valor;
   }

   public Integer getQuantidadeParcelas() {
      return quantidadeParcelas;
   }

   public void setQuantidadeParcelas(Integer quantidadeParcelas) {
      this.quantidadeParcelas = quantidadeParcelas;
   }

   public Integer getIntervaloEntreParcelas() {
      return intervaloEntreParcelas;
   }

   public void setIntervaloEntreParcelas(Integer intervaloEntreParcelas) {
      this.intervaloEntreParcelas = intervaloEntreParcelas;
   }

   public String getObservacao() {
      return observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public String getClassificacaoContabilConta() {
      return classificacaoContabilConta;
   }

   public void setClassificacaoContabilConta(String classificacaoContabilConta) {
      this.classificacaoContabilConta = classificacaoContabilConta;
   }

   public TipoContrato getTipoContrato() {
      return tipoContrato;
   }

   public void setTipoContrato(TipoContrato tipoContrato) {
      this.tipoContrato = tipoContrato;
   }

   public ContratoSolicitacaoServico getContratoSolicitacaoServico() {
      return contratoSolicitacaoServico;
   }

   public void setContratoSolicitacaoServico(ContratoSolicitacaoServico contratoSolicitacaoServico) {
      this.contratoSolicitacaoServico = contratoSolicitacaoServico;
   }


   public Set<ContratoHistoricoReajuste> getListaContratoHistoricoReajuste() {
      return listaContratoHistoricoReajuste;
   }

   public void setListaContratoHistoricoReajuste(Set<ContratoHistoricoReajuste> listaContratoHistoricoReajuste) {
      this.listaContratoHistoricoReajuste = listaContratoHistoricoReajuste;
   }

   public Set<ContratoPrevFaturamento> getListaContratoPrevFaturamento() {
      return listaContratoPrevFaturamento;
   }

   public void setListaContratoPrevFaturamento(Set<ContratoPrevFaturamento> listaContratoPrevFaturamento) {
      this.listaContratoPrevFaturamento = listaContratoPrevFaturamento;
   }

   public Set<ContratoHistFaturamento> getListaContratoHistFaturamento() {
      return listaContratoHistFaturamento;
   }

   public void setListaContratoHistFaturamento(Set<ContratoHistFaturamento> listaContratoHistFaturamento) {
      this.listaContratoHistFaturamento = listaContratoHistFaturamento;
   }

   @Override
   public String toString() {
      return nome ;
   }
   
   

}
