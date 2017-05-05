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
@Table(name = "PONTO_ABONO_UTILIZACAO")
public class PontoAbonoUtilizacao implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_UTILIZACAO")
   private Date dataUtilizacao;
   @Column(name = "OBSERVACAO")
   private String observacao;
   @JoinColumn(name = "ID_PONTO_ABONO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private PontoAbono pontoAbono;

   public PontoAbonoUtilizacao() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataUtilizacao() {
      return dataUtilizacao;
   }

   public void setDataUtilizacao(Date dataUtilizacao) {
      this.dataUtilizacao = dataUtilizacao;
   }

   public String getObservacao() {
      return observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public PontoAbono getPontoAbono() {
      return pontoAbono;
   }

   public void setPontoAbono(PontoAbono pontoAbono) {
      this.pontoAbono = pontoAbono;
   }

   @Override
   public String toString() {
      return "PontoAbonoUtilizacao{" + "id=" + id + '}';
   }

}
