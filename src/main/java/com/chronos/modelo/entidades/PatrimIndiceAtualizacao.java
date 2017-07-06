package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PATRIM_INDICE_ATUALIZACAO")
public class PatrimIndiceAtualizacao implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_INDICE")
   private Date dataIndice;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "VALOR")
   private BigDecimal valor;
   @Column(name = "VALOR_ALTERNATIVO")
   private BigDecimal valorAlternativo;

   public PatrimIndiceAtualizacao() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataIndice() {
      return dataIndice;
   }

   public void setDataIndice(Date dataIndice) {
      this.dataIndice = dataIndice;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public BigDecimal getValor() {
      return valor;
   }

   public void setValor(BigDecimal valor) {
      this.valor = valor;
   }

   public BigDecimal getValorAlternativo() {
      return valorAlternativo;
   }

   public void setValorAlternativo(BigDecimal valorAlternativo) {
      this.valorAlternativo = valorAlternativo;
   }

   @Override
   public String toString() {
      return nome ;
   }

   
}
