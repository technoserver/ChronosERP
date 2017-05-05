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
@Table(name = "GUIAS_ACUMULADAS")
public class GuiasAcumuladas implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "GPS_TIPO")
   private String gpsTipo;
   @Column(name = "GPS_COMPETENCIA")
   private String gpsCompetencia;
   @Column(name = "GPS_VALOR_INSS")
   private BigDecimal gpsValorInss;
   @Column(name = "GPS_VALOR_OUTRAS_ENT")
   private BigDecimal gpsValorOutrasEnt;
   @Temporal(TemporalType.DATE)
   @Column(name = "GPS_DATA_PAGAMENTO")
   private Date gpsDataPagamento;
   @Column(name = "IRRF_COMPETENCIA")
   private String irrfCompetencia;
   @Column(name = "IRRF_CODIGO_RECOLHIMENTO")
   private Integer irrfCodigoRecolhimento;
   @Column(name = "IRRF_VALOR_ACUMULADO")
   private BigDecimal irrfValorAcumulado;
   @Temporal(TemporalType.DATE)
   @Column(name = "IRRF_DATA_PAGAMENTO")
   private Date irrfDataPagamento;
   @Column(name = "PIS_COMPETENCIA")
   private String pisCompetencia;
   @Column(name = "PIS_VALOR_ACUMULADO")
   private BigDecimal pisValorAcumulado;
   @Temporal(TemporalType.DATE)
   @Column(name = "PIS_DATA_PAGAMENTO")
   private Date pisDataPagamento;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public GuiasAcumuladas() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getGpsTipo() {
      return gpsTipo;
   }

   public void setGpsTipo(String gpsTipo) {
      this.gpsTipo = gpsTipo;
   }

   public String getGpsCompetencia() {
      return gpsCompetencia;
   }

   public void setGpsCompetencia(String gpsCompetencia) {
      this.gpsCompetencia = gpsCompetencia;
   }

   public BigDecimal getGpsValorInss() {
      return gpsValorInss;
   }

   public void setGpsValorInss(BigDecimal gpsValorInss) {
      this.gpsValorInss = gpsValorInss;
   }

   public BigDecimal getGpsValorOutrasEnt() {
      return gpsValorOutrasEnt;
   }

   public void setGpsValorOutrasEnt(BigDecimal gpsValorOutrasEnt) {
      this.gpsValorOutrasEnt = gpsValorOutrasEnt;
   }

   public Date getGpsDataPagamento() {
      return gpsDataPagamento;
   }

   public void setGpsDataPagamento(Date gpsDataPagamento) {
      this.gpsDataPagamento = gpsDataPagamento;
   }

   public String getIrrfCompetencia() {
      return irrfCompetencia;
   }

   public void setIrrfCompetencia(String irrfCompetencia) {
      this.irrfCompetencia = irrfCompetencia;
   }

   public Integer getIrrfCodigoRecolhimento() {
      return irrfCodigoRecolhimento;
   }

   public void setIrrfCodigoRecolhimento(Integer irrfCodigoRecolhimento) {
      this.irrfCodigoRecolhimento = irrfCodigoRecolhimento;
   }

   public BigDecimal getIrrfValorAcumulado() {
      return irrfValorAcumulado;
   }

   public void setIrrfValorAcumulado(BigDecimal irrfValorAcumulado) {
      this.irrfValorAcumulado = irrfValorAcumulado;
   }

   public Date getIrrfDataPagamento() {
      return irrfDataPagamento;
   }

   public void setIrrfDataPagamento(Date irrfDataPagamento) {
      this.irrfDataPagamento = irrfDataPagamento;
   }

   public String getPisCompetencia() {
      return pisCompetencia;
   }

   public void setPisCompetencia(String pisCompetencia) {
      this.pisCompetencia = pisCompetencia;
   }

   public BigDecimal getPisValorAcumulado() {
      return pisValorAcumulado;
   }

   public void setPisValorAcumulado(BigDecimal pisValorAcumulado) {
      this.pisValorAcumulado = pisValorAcumulado;
   }

   public Date getPisDataPagamento() {
      return pisDataPagamento;
   }

   public void setPisDataPagamento(Date pisDataPagamento) {
      this.pisDataPagamento = pisDataPagamento;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return "GuiasAcumuladas{" + "id=" + id + '}';
   }

}
