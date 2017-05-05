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
@Table(name = "FOLHA_PLANO_SAUDE")
public class FolhaPlanoSaude implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_INICIO")
   private Date dataInicio;
   @Column(name = "BENEFICIARIO")
   private String beneficiario;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @JoinColumn(name = "ID_OPERADORA_PLANO_SAUDE", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private OperadoraPlanoSaude operadoraPlanoSaude;

   public FolhaPlanoSaude() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataInicio() {
      return dataInicio;
   }

   public void setDataInicio(Date dataInicio) {
      this.dataInicio = dataInicio;
   }

   public String getBeneficiario() {
      return beneficiario;
   }

   public void setBeneficiario(String beneficiario) {
      this.beneficiario = beneficiario;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public OperadoraPlanoSaude getOperadoraPlanoSaude() {
      return operadoraPlanoSaude;
   }

   public void setOperadoraPlanoSaude(OperadoraPlanoSaude operadoraPlanoSaude) {
      this.operadoraPlanoSaude = operadoraPlanoSaude;
   }

   @Override
   public String toString() {
      return "FolhaPlanoSaude{" + "id=" + id + '}';
   }

}
