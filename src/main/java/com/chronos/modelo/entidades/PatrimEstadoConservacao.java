package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PATRIM_ESTADO_CONSERVACAO")
public class PatrimEstadoConservacao implements Serializable {

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
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;
   @Transient
   private List<String> codigosPreCadastrados;

   public PatrimEstadoConservacao() {
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

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   public List<String> getCodigosPreCadastrados() {
      if (codigosPreCadastrados == null) {
         codigosPreCadastrados = new ArrayList<>();
         codigosPreCadastrados.add("01");
         codigosPreCadastrados.add("02");
         codigosPreCadastrados.add("03");
         codigosPreCadastrados.add("04");
         codigosPreCadastrados.add("05");
         codigosPreCadastrados.add("06");
         codigosPreCadastrados.add("07");
      }
      return codigosPreCadastrados;
   }

   @Override
   public String toString() {
      return  nome ;
   }
   
   
}
