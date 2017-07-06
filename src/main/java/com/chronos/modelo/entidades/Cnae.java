/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "cnae")
public class Cnae implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(nullable = false)
   private Integer id;
   @Column(name = "CODIGO")
   private String codigo;
   @Size(max = 2147483647)
   @Column(length = 2147483647)
   private String denominacao;

   public Cnae() {
   }

   public Cnae(Integer id) {
      this.id = id;
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

   public String getDenominacao() {
      return denominacao;
   }

   public void setDenominacao(String denominacao) {
      this.denominacao = denominacao;
   }

}
