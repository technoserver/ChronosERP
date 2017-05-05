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
@Table(name = "FOLHA_PPP_CAT")
public class FolhaPppCat implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "NUMERO_CAT")
   private Integer numeroCat;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_AFASTAMENTO")
   private Date dataAfastamento;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_REGISTRO")
   private Date dataRegistro;
   @JoinColumn(name = "ID_FOLHA_PPP", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private FolhaPpp folhaPpp;

   public FolhaPppCat() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Integer getNumeroCat() {
      return numeroCat;
   }

   public void setNumeroCat(Integer numeroCat) {
      this.numeroCat = numeroCat;
   }

   public Date getDataAfastamento() {
      return dataAfastamento;
   }

   public void setDataAfastamento(Date dataAfastamento) {
      this.dataAfastamento = dataAfastamento;
   }

   public Date getDataRegistro() {
      return dataRegistro;
   }

   public void setDataRegistro(Date dataRegistro) {
      this.dataRegistro = dataRegistro;
   }

   public FolhaPpp getFolhaPpp() {
      return folhaPpp;
   }

   public void setFolhaPpp(FolhaPpp folhaPpp) {
      this.folhaPpp = folhaPpp;
   }

   @Override
   public String toString() {
      return "FolhaPppCat{" + "id=" + id + '}';
   }

}
