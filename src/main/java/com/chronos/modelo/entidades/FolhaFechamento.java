package com.chronos.modelo.entidades;

import java.io.Serializable;
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
@Table(name = "FOLHA_FECHAMENTO")
public class FolhaFechamento implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "FECHAMENTO_ATUAL")
   private String fechamentoAtual;
   @Column(name = "PROXIMO_FECHAMENTO")
   private String proximoFechamento;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public FolhaFechamento() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getFechamentoAtual() {
      return fechamentoAtual;
   }

   public void setFechamentoAtual(String fechamentoAtual) {
      this.fechamentoAtual = fechamentoAtual;
   }

   public String getProximoFechamento() {
      return proximoFechamento;
   }

   public void setProximoFechamento(String proximoFechamento) {
      this.proximoFechamento = proximoFechamento;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return "FolhaFechamento{" + "id=" + id + '}';
   }

}
