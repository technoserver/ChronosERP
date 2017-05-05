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
@Table(name = "PONTO_ABONO")
public class PontoAbono implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "QUANTIDADE")
   private Integer quantidade;
   @Column(name = "UTILIZADO")
   private Integer utilizado;
   @Column(name = "SALDO")
   private Integer saldo;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_CADASTRO")
   private Date dataCadastro;
   @Temporal(TemporalType.DATE)
   @Column(name = "INICIO_UTILIZACAO")
   private Date inicioUtilizacao;
   @Column(name = "OBSERVACAO")
   private String observacao;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "pontoAbono", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<PontoAbonoUtilizacao> listaPontoAbonoUtilizacao;

   public PontoAbono() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Integer getQuantidade() {
      return quantidade;
   }

   public void setQuantidade(Integer quantidade) {
      this.quantidade = quantidade;
   }

   public Integer getUtilizado() {
      return utilizado;
   }

   public void setUtilizado(Integer utilizado) {
      this.utilizado = utilizado;
   }

   public Integer getSaldo() {
      return saldo;
   }

   public void setSaldo(Integer saldo) {
      this.saldo = saldo;
   }

   public Date getDataCadastro() {
      return dataCadastro;
   }

   public void setDataCadastro(Date dataCadastro) {
      this.dataCadastro = dataCadastro;
   }

   public Date getInicioUtilizacao() {
      return inicioUtilizacao;
   }

   public void setInicioUtilizacao(Date inicioUtilizacao) {
      this.inicioUtilizacao = inicioUtilizacao;
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

 
   public Set<PontoAbonoUtilizacao> getListaPontoAbonoUtilizacao() {
      return listaPontoAbonoUtilizacao;
   }

   public void setListaPontoAbonoUtilizacao(Set<PontoAbonoUtilizacao> listaPontoAbonoUtilizacao) {
      this.listaPontoAbonoUtilizacao = listaPontoAbonoUtilizacao;
   }

   @Override
   public String toString() {
      return "PontoAbono{" + "id=" + id + '}';
   }
   
   

}
