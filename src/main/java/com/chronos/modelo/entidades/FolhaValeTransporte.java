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
@Table(name = "FOLHA_VALE_TRANSPORTE")
public class FolhaValeTransporte implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "QUANTIDADE")
   private Integer quantidade;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @JoinColumn(name = "ID_EMPRESA_TRANSP_ITIN", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private EmpresaTransporteItinerario empresaTransporteItinerario;

   public FolhaValeTransporte() {
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

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public EmpresaTransporteItinerario getEmpresaTransporteItinerario() {
      return empresaTransporteItinerario;
   }

   public void setEmpresaTransporteItinerario(EmpresaTransporteItinerario empresaTransporteItinerario) {
      this.empresaTransporteItinerario = empresaTransporteItinerario;
   }

   @Override
   public String toString() {
      return "FolhaValeTransporte{" + "id=" + id + '}';
   }

}
