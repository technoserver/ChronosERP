package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FOLHA_RESCISAO")
public class FolhaRescisao implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_DEMISSAO")
   private Date dataDemissao;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_PAGAMENTO")
   private Date dataPagamento;
   @Column(name = "MOTIVO")
   private String motivo;
   @Column(name = "MOTIVO_ESOCIAL")
   private String motivoEsocial;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_AVISO_PREVIO")
   private Date dataAvisoPrevio;
   @Column(name = "DIAS_AVISO_PREVIO")
   private Integer diasAvisoPrevio;
   @Column(name = "COMPROVOU_NOVO_EMPREGO")
   private String comprovouNovoEmprego;
   @Column(name = "DISPENSOU_EMPREGADO")
   private String dispensouEmpregado;
   @Column(name = "PENSAO_ALIMENTICIA")
   private BigDecimal pensaoAlimenticia;
   @Column(name = "PENSAO_ALIMENTICIA_FGTS")
   private BigDecimal pensaoAlimenticiaFgts;
   @Column(name = "FGTS_VALOR_RESCISAO")
   private BigDecimal fgtsValorRescisao;
   @Column(name = "FGTS_SALDO_BANCO")
   private BigDecimal fgtsSaldoBanco;
   @Column(name = "FGTS_COMPLEMENTO_SALDO")
   private BigDecimal fgtsComplementoSaldo;
   @Column(name = "FGTS_CODIGO_AFASTAMENTO")
   private String fgtsCodigoAfastamento;
   @Column(name = "FGTS_CODIGO_SAQUE")
   private String fgtsCodigoSaque;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;

   public FolhaRescisao() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataDemissao() {
      return dataDemissao;
   }

   public void setDataDemissao(Date dataDemissao) {
      this.dataDemissao = dataDemissao;
   }

   public Date getDataPagamento() {
      return dataPagamento;
   }

   public void setDataPagamento(Date dataPagamento) {
      this.dataPagamento = dataPagamento;
   }

   public String getMotivo() {
      return motivo;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public String getMotivoEsocial() {
      return motivoEsocial;
   }

   public void setMotivoEsocial(String motivoEsocial) {
      this.motivoEsocial = motivoEsocial;
   }

   public Date getDataAvisoPrevio() {
      return dataAvisoPrevio;
   }

   public void setDataAvisoPrevio(Date dataAvisoPrevio) {
      this.dataAvisoPrevio = dataAvisoPrevio;
   }

   public Integer getDiasAvisoPrevio() {
      return diasAvisoPrevio;
   }

   public void setDiasAvisoPrevio(Integer diasAvisoPrevio) {
      this.diasAvisoPrevio = diasAvisoPrevio;
   }

   public String getComprovouNovoEmprego() {
      return comprovouNovoEmprego;
   }

   public void setComprovouNovoEmprego(String comprovouNovoEmprego) {
      this.comprovouNovoEmprego = comprovouNovoEmprego;
   }

   public String getDispensouEmpregado() {
      return dispensouEmpregado;
   }

   public void setDispensouEmpregado(String dispensouEmpregado) {
      this.dispensouEmpregado = dispensouEmpregado;
   }

   public BigDecimal getPensaoAlimenticia() {
      return pensaoAlimenticia;
   }

   public void setPensaoAlimenticia(BigDecimal pensaoAlimenticia) {
      this.pensaoAlimenticia = pensaoAlimenticia;
   }

   public BigDecimal getPensaoAlimenticiaFgts() {
      return pensaoAlimenticiaFgts;
   }

   public void setPensaoAlimenticiaFgts(BigDecimal pensaoAlimenticiaFgts) {
      this.pensaoAlimenticiaFgts = pensaoAlimenticiaFgts;
   }

   public BigDecimal getFgtsValorRescisao() {
      return fgtsValorRescisao;
   }

   public void setFgtsValorRescisao(BigDecimal fgtsValorRescisao) {
      this.fgtsValorRescisao = fgtsValorRescisao;
   }

   public BigDecimal getFgtsSaldoBanco() {
      return fgtsSaldoBanco;
   }

   public void setFgtsSaldoBanco(BigDecimal fgtsSaldoBanco) {
      this.fgtsSaldoBanco = fgtsSaldoBanco;
   }

   public BigDecimal getFgtsComplementoSaldo() {
      return fgtsComplementoSaldo;
   }

   public void setFgtsComplementoSaldo(BigDecimal fgtsComplementoSaldo) {
      this.fgtsComplementoSaldo = fgtsComplementoSaldo;
   }

   public String getFgtsCodigoAfastamento() {
      return fgtsCodigoAfastamento;
   }

   public void setFgtsCodigoAfastamento(String fgtsCodigoAfastamento) {
      this.fgtsCodigoAfastamento = fgtsCodigoAfastamento;
   }

   public String getFgtsCodigoSaque() {
      return fgtsCodigoSaque;
   }

   public void setFgtsCodigoSaque(String fgtsCodigoSaque) {
      this.fgtsCodigoSaque = fgtsCodigoSaque;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   @Override
   public String toString() {
      return "FolhaRescisao{" + "id=" + id + '}';
   }

}
