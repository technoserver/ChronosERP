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
@Table(name = "FOLHA_PPP")
public class FolhaPpp implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "OBSERVACAO")
   private String observacao;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaPpp", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaPppCat> listaFolhaPppCat;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaPpp", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaPppAtividade> listaFolhaPppAtividade;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaPpp", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaPppFatorRisco> listaFolhaPppFatorRisco;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaPpp", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaPppExameMedico> listaFolhaPppExameMedico;

   public FolhaPpp() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getObservacao() {
      return observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public Set<FolhaPppCat> getListaFolhaPppCat() {
      return listaFolhaPppCat;
   }

   public void setListaFolhaPppCat(Set<FolhaPppCat> listaFolhaPppCat) {
      this.listaFolhaPppCat = listaFolhaPppCat;
   }

   public Set<FolhaPppAtividade> getListaFolhaPppAtividade() {
      return listaFolhaPppAtividade;
   }

   public void setListaFolhaPppAtividade(Set<FolhaPppAtividade> listaFolhaPppAtividade) {
      this.listaFolhaPppAtividade = listaFolhaPppAtividade;
   }

   public Set<FolhaPppFatorRisco> getListaFolhaPppFatorRisco() {
      return listaFolhaPppFatorRisco;
   }

   public void setListaFolhaPppFatorRisco(Set<FolhaPppFatorRisco> listaFolhaPppFatorRisco) {
      this.listaFolhaPppFatorRisco = listaFolhaPppFatorRisco;
   }

   public Set<FolhaPppExameMedico> getListaFolhaPppExameMedico() {
      return listaFolhaPppExameMedico;
   }

   public void setListaFolhaPppExameMedico(Set<FolhaPppExameMedico> listaFolhaPppExameMedico) {
      this.listaFolhaPppExameMedico = listaFolhaPppExameMedico;
   }

   @Override
   public String toString() {
      return "FolhaPpp{" + "id=" + id + '}';
   }

}
