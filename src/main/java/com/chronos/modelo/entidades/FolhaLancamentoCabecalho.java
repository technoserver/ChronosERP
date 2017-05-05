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
@Table(name = "FOLHA_LANCAMENTO_CABECALHO")
public class FolhaLancamentoCabecalho implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "COMPETENCIA")
   private String competencia;
   @Column(name = "TIPO")
   private String tipo;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "folhaLancamentoCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<FolhaLancamentoDetalhe> listaFolhaLancamentoDetalhe;

   public FolhaLancamentoCabecalho() {
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

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public Set<FolhaLancamentoDetalhe> getListaFolhaLancamentoDetalhe() {
      return listaFolhaLancamentoDetalhe;
   }

   public void setListaFolhaLancamentoDetalhe(Set<FolhaLancamentoDetalhe> listaFolhaLancamentoDetalhe) {
      this.listaFolhaLancamentoDetalhe = listaFolhaLancamentoDetalhe;
   }

   @Override
   public String toString() {
      return "FolhaLancamentoCabecalho{" + "id=" + id + '}';
   }

}
