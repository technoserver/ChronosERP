package com.chronos.modelo.entidades;

import java.io.Serializable;
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
@Table(name = "FOLHA_FERIAS_COLETIVAS")
public class FolhaFeriasColetivas implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_INICIO")
   private Date dataInicio;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_FIM")
   private Date dataFim;
   @Column(name = "DIAS_GOZO")
   private Integer diasGozo;
   @Temporal(TemporalType.DATE)
   @Column(name = "ABONO_PECUNIARIO_INICIO")
   private Date abonoPecuniarioInicio;
   @Temporal(TemporalType.DATE)
   @Column(name = "ABONO_PECUNIARIO_FIM")
   private Date abonoPecuniarioFim;
   @Column(name = "DIAS_ABONO")
   private Integer diasAbono;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_PAGAMENTO")
   private Date dataPagamento;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public FolhaFeriasColetivas() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataInicio() {
      return dataInicio;
   }

   public void setDataInicio(Date dataInicio) {
      this.dataInicio = dataInicio;
   }

   public Date getDataFim() {
      return dataFim;
   }

   public void setDataFim(Date dataFim) {
      this.dataFim = dataFim;
   }

   public Integer getDiasGozo() {
      return diasGozo;
   }

   public void setDiasGozo(Integer diasGozo) {
      this.diasGozo = diasGozo;
   }

   public Date getAbonoPecuniarioInicio() {
      return abonoPecuniarioInicio;
   }

   public void setAbonoPecuniarioInicio(Date abonoPecuniarioInicio) {
      this.abonoPecuniarioInicio = abonoPecuniarioInicio;
   }

   public Date getAbonoPecuniarioFim() {
      return abonoPecuniarioFim;
   }

   public void setAbonoPecuniarioFim(Date abonoPecuniarioFim) {
      this.abonoPecuniarioFim = abonoPecuniarioFim;
   }

   public Integer getDiasAbono() {
      return diasAbono;
   }

   public void setDiasAbono(Integer diasAbono) {
      this.diasAbono = diasAbono;
   }

   public Date getDataPagamento() {
      return dataPagamento;
   }

   public void setDataPagamento(Date dataPagamento) {
      this.dataPagamento = dataPagamento;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return "FolhaFeriasColetivas{" + "id=" + id + '}';
   }

}
