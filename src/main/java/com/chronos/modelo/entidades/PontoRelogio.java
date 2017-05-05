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
@Table(name = "PONTO_RELOGIO")
public class PontoRelogio implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "LOCALIZACAO")
   private String localizacao;
   @Column(name = "MARCA")
   private String marca;
   @Column(name = "FABRICANTE")
   private String fabricante;
   @Column(name = "NUMERO_SERIE")
   private String numeroSerie;
   @Column(name = "UTILIZACAO")
   private String utilizacao;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public PontoRelogio() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getLocalizacao() {
      return localizacao;
   }

   public void setLocalizacao(String localizacao) {
      this.localizacao = localizacao;
   }

   public String getMarca() {
      return marca;
   }

   public void setMarca(String marca) {
      this.marca = marca;
   }

   public String getFabricante() {
      return fabricante;
   }

   public void setFabricante(String fabricante) {
      this.fabricante = fabricante;
   }

   public String getNumeroSerie() {
      return numeroSerie;
   }

   public void setNumeroSerie(String numeroSerie) {
      this.numeroSerie = numeroSerie;
   }

   public String getUtilizacao() {
      return utilizacao;
   }

   public void setUtilizacao(String utilizacao) {
      this.utilizacao = utilizacao;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return "PontoRelogio{" + "id=" + id + '}';
   }

}
