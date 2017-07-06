package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "EMPRESA_TRANSPORTE_ITINERARIO")
public class EmpresaTransporteItinerario implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "TARIFA")
   private BigDecimal tarifa;
   @Column(name = "TRAJETO")
   private String trajeto;
   @JoinColumn(name = "ID_EMPRESA_TRANSPORTE", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private EmpresaTransporte empresaTransporte;

   public EmpresaTransporteItinerario() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public BigDecimal getTarifa() {
      return tarifa;
   }

   public void setTarifa(BigDecimal tarifa) {
      this.tarifa = tarifa;
   }

   public String getTrajeto() {
      return trajeto;
   }

   public void setTrajeto(String trajeto) {
      this.trajeto = trajeto;
   }

   public EmpresaTransporte getEmpresaTransporte() {
      return empresaTransporte;
   }

   public void setEmpresaTransporte(EmpresaTransporte empresaTransporte) {
      this.empresaTransporte = empresaTransporte;
   }

   @Override
   public String toString() {
      return nome;
   }

}
