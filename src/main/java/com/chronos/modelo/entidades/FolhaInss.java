package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "FOLHA_INSS")
public class FolhaInss implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "COMPETENCIA")
   private String competencia;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaInss", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaInssRetencao> listaFolhaInssRetencao;

   public FolhaInss() {
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

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   public Set<FolhaInssRetencao> getListaFolhaInssRetencao() {
      return listaFolhaInssRetencao;
   }

   public void setListaFolhaInssRetencao(Set<FolhaInssRetencao> listaFolhaInssRetencao) {
      this.listaFolhaInssRetencao = listaFolhaInssRetencao;
   }

   @Override
   public String toString() {
      return "FolhaInss{" + "id=" + id + '}';
   }

}
