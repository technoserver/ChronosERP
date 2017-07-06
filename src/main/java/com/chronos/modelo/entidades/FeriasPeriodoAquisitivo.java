package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FERIAS_PERIODO_AQUISITIVO")
public class FeriasPeriodoAquisitivo implements Serializable {

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
   @Column(name = "SITUACAO")
   private String situacao;
   @Temporal(TemporalType.DATE)
   @Column(name = "LIMITE_PARA_GOZO")
   private Date limiteParaGozo;
   @Column(name = "DESCONTAR_FALTAS")
   private String descontarFaltas;
   @Column(name = "DESCONSIDERAR_AFASTAMENTO")
   private String desconsiderarAfastamento;
   @Column(name = "AFASTAMENTO_PREVIDENCIA")
   private Integer afastamentoPrevidencia;
   @Column(name = "AFASTAMENTO_SEM_REMUN")
   private Integer afastamentoSemRemun;
   @Column(name = "AFASTAMENTO_COM_REMUN")
   private Integer afastamentoComRemun;
   @Column(name = "DIAS_DIREITO")
   private Integer diasDireito;
   @Column(name = "DIAS_GOZADOS")
   private Integer diasGozados;
   @Column(name = "DIAS_FALTAS")
   private Integer diasFaltas;
   @Column(name = "DIAS_RESTANTES")
   private Integer diasRestantes;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;

   public FeriasPeriodoAquisitivo() {
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

   public String getSituacao() {
      return situacao;
   }

   public void setSituacao(String situacao) {
      this.situacao = situacao;
   }

   public Date getLimiteParaGozo() {
      return limiteParaGozo;
   }

   public void setLimiteParaGozo(Date limiteParaGozo) {
      this.limiteParaGozo = limiteParaGozo;
   }

   public String getDescontarFaltas() {
      return descontarFaltas;
   }

   public void setDescontarFaltas(String descontarFaltas) {
      this.descontarFaltas = descontarFaltas;
   }

   public String getDesconsiderarAfastamento() {
      return desconsiderarAfastamento;
   }

   public void setDesconsiderarAfastamento(String desconsiderarAfastamento) {
      this.desconsiderarAfastamento = desconsiderarAfastamento;
   }

   public Integer getAfastamentoPrevidencia() {
      return afastamentoPrevidencia;
   }

   public void setAfastamentoPrevidencia(Integer afastamentoPrevidencia) {
      this.afastamentoPrevidencia = afastamentoPrevidencia;
   }

   public Integer getAfastamentoSemRemun() {
      return afastamentoSemRemun;
   }

   public void setAfastamentoSemRemun(Integer afastamentoSemRemun) {
      this.afastamentoSemRemun = afastamentoSemRemun;
   }

   public Integer getAfastamentoComRemun() {
      return afastamentoComRemun;
   }

   public void setAfastamentoComRemun(Integer afastamentoComRemun) {
      this.afastamentoComRemun = afastamentoComRemun;
   }

   public Integer getDiasDireito() {
      return diasDireito;
   }

   public void setDiasDireito(Integer diasDireito) {
      this.diasDireito = diasDireito;
   }

   public Integer getDiasGozados() {
      return diasGozados;
   }

   public void setDiasGozados(Integer diasGozados) {
      this.diasGozados = diasGozados;
   }

   public Integer getDiasFaltas() {
      return diasFaltas;
   }

   public void setDiasFaltas(Integer diasFaltas) {
      this.diasFaltas = diasFaltas;
   }

   public Integer getDiasRestantes() {
      return diasRestantes;
   }

   public void setDiasRestantes(Integer diasRestantes) {
      this.diasRestantes = diasRestantes;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   @Override
   public String toString() {
      return "FeriasPeriodoAquisitivo{" + "id=" + id + '}';
   }

}
