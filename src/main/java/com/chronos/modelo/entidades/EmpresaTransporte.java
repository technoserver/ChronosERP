package com.chronos.modelo.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPRESA_TRANSPORTE")
public class EmpresaTransporte implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "UF")
   private String uf;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "CLASSIFICACAO_CONTABIL_CONTA")
   private String classificacaoContabilConta;

   public EmpresaTransporte() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getUf() {
      return uf;
   }

   public void setUf(String uf) {
      this.uf = uf;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getClassificacaoContabilConta() {
      return classificacaoContabilConta;
   }

   public void setClassificacaoContabilConta(String classificacaoContabilConta) {
      this.classificacaoContabilConta = classificacaoContabilConta;
   }

   @Override
   public String toString() {
      return nome;
   }

}
