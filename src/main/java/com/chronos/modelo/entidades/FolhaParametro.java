package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "FOLHA_PARAMETRO")
public class FolhaParametro implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "COMPETENCIA")
   private String competencia;
   @Column(name = "CONTRIBUI_PIS")
   private String contribuiPis;
   @Column(name = "ALIQUOTA_PIS")
   private BigDecimal aliquotaPis;
   @Column(name = "DISCRIMINAR_DSR")
   private String discriminarDsr;
   @Column(name = "DIA_PAGAMENTO")
   private String diaPagamento;
   @Column(name = "CALCULO_PROPORCIONALIDADE")
   private String calculoProporcionalidade;
   @Column(name = "DESCONTAR_FALTAS_13")
   private String descontarFaltas13;
   @Column(name = "PAGAR_ADICIONAIS_13")
   private String pagarAdicionais13;
   @Column(name = "PAGAR_ESTAGIARIOS_13")
   private String pagarEstagiarios13;
   @Column(name = "MES_ADIANTAMENTO_13")
   private String mesAdiantamento13;
   @Column(name = "PERCENTUAL_ADIANTAM_13")
   private BigDecimal percentualAdiantam13;
   @Column(name = "FERIAS_DESCONTAR_FALTAS")
   private String feriasDescontarFaltas;
   @Column(name = "FERIAS_PAGAR_ADICIONAIS")
   private String feriasPagarAdicionais;
   @Column(name = "FERIAS_ADIANTAR_13")
   private String feriasAdiantar13;
   @Column(name = "FERIAS_PAGAR_ESTAGIARIOS")
   private String feriasPagarEstagiarios;
   @Column(name = "FERIAS_CALC_JUSTA_CAUSA")
   private String feriasCalcJustaCausa;
   @Column(name = "FERIAS_MOVIMENTO_MENSAL")
   private String feriasMovimentoMensal;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public FolhaParametro() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getCompetencia() {
      return competencia;
   }

   public void setCompetencia(String competencia) {
      this.competencia = competencia;
   }

   public String getContribuiPis() {
      return contribuiPis;
   }

   public void setContribuiPis(String contribuiPis) {
      this.contribuiPis = contribuiPis;
   }

   public BigDecimal getAliquotaPis() {
      return aliquotaPis;
   }

   public void setAliquotaPis(BigDecimal aliquotaPis) {
      this.aliquotaPis = aliquotaPis;
   }

   public String getDiscriminarDsr() {
      return discriminarDsr;
   }

   public void setDiscriminarDsr(String discriminarDsr) {
      this.discriminarDsr = discriminarDsr;
   }

   public String getDiaPagamento() {
      return diaPagamento;
   }

   public void setDiaPagamento(String diaPagamento) {
      this.diaPagamento = diaPagamento;
   }

   public String getCalculoProporcionalidade() {
      return calculoProporcionalidade;
   }

   public void setCalculoProporcionalidade(String calculoProporcionalidade) {
      this.calculoProporcionalidade = calculoProporcionalidade;
   }

   public String getDescontarFaltas13() {
      return descontarFaltas13;
   }

   public void setDescontarFaltas13(String descontarFaltas13) {
      this.descontarFaltas13 = descontarFaltas13;
   }

   public String getPagarAdicionais13() {
      return pagarAdicionais13;
   }

   public void setPagarAdicionais13(String pagarAdicionais13) {
      this.pagarAdicionais13 = pagarAdicionais13;
   }

   public String getPagarEstagiarios13() {
      return pagarEstagiarios13;
   }

   public void setPagarEstagiarios13(String pagarEstagiarios13) {
      this.pagarEstagiarios13 = pagarEstagiarios13;
   }

   public String getMesAdiantamento13() {
      return mesAdiantamento13;
   }

   public void setMesAdiantamento13(String mesAdiantamento13) {
      this.mesAdiantamento13 = mesAdiantamento13;
   }

   public BigDecimal getPercentualAdiantam13() {
      return percentualAdiantam13;
   }

   public void setPercentualAdiantam13(BigDecimal percentualAdiantam13) {
      this.percentualAdiantam13 = percentualAdiantam13;
   }

   public String getFeriasDescontarFaltas() {
      return feriasDescontarFaltas;
   }

   public void setFeriasDescontarFaltas(String feriasDescontarFaltas) {
      this.feriasDescontarFaltas = feriasDescontarFaltas;
   }

   public String getFeriasPagarAdicionais() {
      return feriasPagarAdicionais;
   }

   public void setFeriasPagarAdicionais(String feriasPagarAdicionais) {
      this.feriasPagarAdicionais = feriasPagarAdicionais;
   }

   public String getFeriasAdiantar13() {
      return feriasAdiantar13;
   }

   public void setFeriasAdiantar13(String feriasAdiantar13) {
      this.feriasAdiantar13 = feriasAdiantar13;
   }

   public String getFeriasPagarEstagiarios() {
      return feriasPagarEstagiarios;
   }

   public void setFeriasPagarEstagiarios(String feriasPagarEstagiarios) {
      this.feriasPagarEstagiarios = feriasPagarEstagiarios;
   }

   public String getFeriasCalcJustaCausa() {
      return feriasCalcJustaCausa;
   }

   public void setFeriasCalcJustaCausa(String feriasCalcJustaCausa) {
      this.feriasCalcJustaCausa = feriasCalcJustaCausa;
   }

   public String getFeriasMovimentoMensal() {
      return feriasMovimentoMensal;
   }

   public void setFeriasMovimentoMensal(String feriasMovimentoMensal) {
      this.feriasMovimentoMensal = feriasMovimentoMensal;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return "FolhaParametro{" + "id=" + id + '}';
   }

}
