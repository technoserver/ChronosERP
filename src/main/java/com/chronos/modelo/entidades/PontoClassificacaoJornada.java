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
@Table(name = "PONTO_CLASSIFICACAO_JORNADA")
public class PontoClassificacaoJornada implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "CODIGO")
   private String codigo;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "DESCRICAO")
   private String descricao;
   @Column(name = "PADRAO")
   private String padrao;
   @Column(name = "DESCONTAR_HORAS")
   private String descontarHoras;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public PontoClassificacaoJornada() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getCodigo() {
      return codigo;
   }

   public void setCodigo(String codigo) {
      this.codigo = codigo;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getDescricao() {
      return descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public String getPadrao() {
      return padrao;
   }

   public void setPadrao(String padrao) {
      this.padrao = padrao;
   }

   public String getDescontarHoras() {
      return descontarHoras;
   }

   public void setDescontarHoras(String descontarHoras) {
      this.descontarHoras = descontarHoras;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return nome ;
   }

}
