package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PONTO_BANCO_HORAS")
public class PontoBancoHoras implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_TRABALHO")
   private Date dataTrabalho;
   @Column(name = "QUANTIDADE")
   private String quantidade;
   @Column(name = "SITUACAO")
   private String situacao;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "pontoBancoHoras", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<PontoBancoHorasUtilizacao> listaPontoBancoHorasUtilizacao;

   public PontoBancoHoras() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataTrabalho() {
      return dataTrabalho;
   }

   public void setDataTrabalho(Date dataTrabalho) {
      this.dataTrabalho = dataTrabalho;
   }

   public String getQuantidade() {
      return quantidade;
   }

   public void setQuantidade(String quantidade) {
      this.quantidade = quantidade;
   }

   public String getSituacao() {
      return situacao;
   }

   public void setSituacao(String situacao) {
      this.situacao = situacao;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public Set<PontoBancoHorasUtilizacao> getListaPontoBancoHorasUtilizacao() {
      return listaPontoBancoHorasUtilizacao;
   }

   public void setListaPontoBancoHorasUtilizacao(Set<PontoBancoHorasUtilizacao> listaPontoBancoHorasUtilizacao) {
      this.listaPontoBancoHorasUtilizacao = listaPontoBancoHorasUtilizacao;
   }

   @Override
   public String toString() {
      return "PontoBancoHoras{" + "id=" + id + '}';
   }

}
