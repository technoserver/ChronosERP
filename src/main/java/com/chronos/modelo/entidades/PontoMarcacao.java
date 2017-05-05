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
@Table(name = "PONTO_MARCACAO")
public class PontoMarcacao implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "NSR")
   private Integer nsr;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_MARCACAO")
   private Date dataMarcacao;
   @Column(name = "HORA_MARCACAO")
   private String horaMarcacao;
   @Column(name = "TIPO_MARCACAO")
   private String tipoMarcacao;
   @Column(name = "TIPO_REGISTRO")
   private String tipoRegistro;
   @Column(name = "PAR_ENTRADA_SAIDA")
   private String parEntradaSaida;
   @Column(name = "JUSTIFICATIVA")
   private String justificativa;
   @JoinColumn(name = "ID_PONTO_RELOGIO", referencedColumnName = "ID")
   @ManyToOne
   private PontoRelogio pontoRelogio;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;

   public PontoMarcacao() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Integer getNsr() {
      return nsr;
   }

   public void setNsr(Integer nsr) {
      this.nsr = nsr;
   }

   public Date getDataMarcacao() {
      return dataMarcacao;
   }

   public void setDataMarcacao(Date dataMarcacao) {
      this.dataMarcacao = dataMarcacao;
   }

   public String getHoraMarcacao() {
      return horaMarcacao;
   }

   public void setHoraMarcacao(String horaMarcacao) {
      this.horaMarcacao = horaMarcacao;
   }

   public String getTipoMarcacao() {
      return tipoMarcacao;
   }

   public void setTipoMarcacao(String tipoMarcacao) {
      this.tipoMarcacao = tipoMarcacao;
   }

   public String getTipoRegistro() {
      return tipoRegistro;
   }

   public void setTipoRegistro(String tipoRegistro) {
      this.tipoRegistro = tipoRegistro;
   }

   public String getParEntradaSaida() {
      return parEntradaSaida;
   }

   public void setParEntradaSaida(String parEntradaSaida) {
      this.parEntradaSaida = parEntradaSaida;
   }

   public String getJustificativa() {
      return justificativa;
   }

   public void setJustificativa(String justificativa) {
      this.justificativa = justificativa;
   }

   public PontoRelogio getPontoRelogio() {
      return pontoRelogio;
   }

   public void setPontoRelogio(PontoRelogio pontoRelogio) {
      this.pontoRelogio = pontoRelogio;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   @Override
   public String toString() {
      return "PontoMarcacao{" + "id=" + id + '}';
   }

}
