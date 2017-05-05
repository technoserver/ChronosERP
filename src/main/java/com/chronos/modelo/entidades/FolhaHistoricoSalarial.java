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
@Table(name = "FOLHA_HISTORICO_SALARIAL")
public class FolhaHistoricoSalarial implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "COMPETENCIA")
   private String competencia;
   @Column(name = "SALARIO_ATUAL")
   private BigDecimal salarioAtual;
   @Column(name = "PERCENTUAL_AUMENTO")
   private BigDecimal percentualAumento;
   @Column(name = "SALARIO_NOVO")
   private BigDecimal salarioNovo;
   @Column(name = "VALIDO_A_PARTIR")
   private String validoAPartir;
   @Column(name = "MOTIVO")
   private String motivo;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;

   public FolhaHistoricoSalarial() {
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

   public BigDecimal getSalarioAtual() {
      return salarioAtual;
   }

   public void setSalarioAtual(BigDecimal salarioAtual) {
      this.salarioAtual = salarioAtual;
   }

   public BigDecimal getPercentualAumento() {
      return percentualAumento;
   }

   public void setPercentualAumento(BigDecimal percentualAumento) {
      this.percentualAumento = percentualAumento;
   }

   public BigDecimal getSalarioNovo() {
      return salarioNovo;
   }

   public void setSalarioNovo(BigDecimal salarioNovo) {
      this.salarioNovo = salarioNovo;
   }

   public String getValidoAPartir() {
      return validoAPartir;
   }

   public void setValidoAPartir(String validoAPartir) {
      this.validoAPartir = validoAPartir;
   }

   public String getMotivo() {
      return motivo;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   @Override
   public String toString() {
      return "FolhaHistoricoSalarial{" + "id=" + id + '}';
   }

}
