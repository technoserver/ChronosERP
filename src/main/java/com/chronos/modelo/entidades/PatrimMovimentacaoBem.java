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
@Table(name = "PATRIM_MOVIMENTACAO_BEM")
public class PatrimMovimentacaoBem implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_MOVIMENTACAO")
   private Date dataMovimentacao;
   @Column(name = "RESPONSAVEL")
   private String responsavel;
   @JoinColumn(name = "ID_PATRIM_TIPO_MOVIMENTACAO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private PatrimTipoMovimentacao patrimTipoMovimentacao;
   @JoinColumn(name = "ID_PATRIM_BEM", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private PatrimBem patrimBem;

   public PatrimMovimentacaoBem() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataMovimentacao() {
      return dataMovimentacao;
   }

   public void setDataMovimentacao(Date dataMovimentacao) {
      this.dataMovimentacao = dataMovimentacao;
   }

   public String getResponsavel() {
      return responsavel;
   }

   public void setResponsavel(String responsavel) {
      this.responsavel = responsavel;
   }

   public PatrimTipoMovimentacao getPatrimTipoMovimentacao() {
      return patrimTipoMovimentacao;
   }

   public void setPatrimTipoMovimentacao(PatrimTipoMovimentacao patrimTipoMovimentacao) {
      this.patrimTipoMovimentacao = patrimTipoMovimentacao;
   }

   public PatrimBem getPatrimBem() {
      return patrimBem;
   }

   public void setPatrimBem(PatrimBem patrimBem) {
      this.patrimBem = patrimBem;
   }

   @Override
   public String toString() {
      return "PatrimMovimentacaoBem{" + "id=" + id + '}';
   }

}
