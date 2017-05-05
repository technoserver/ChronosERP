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
@Table(name = "FOLHA_PPP_EXAME_MEDICO")
public class FolhaPppExameMedico implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_ULTIMO")
   private Date dataUltimo;
   @Column(name = "TIPO")
   private String tipo;
   @Column(name = "NATUREZA")
   private String natureza;
   @Column(name = "EXAME")
   private String exame;
   @Column(name = "INDICACAO_RESULTADOS")
   private String indicacaoResultados;
   @JoinColumn(name = "ID_FOLHA_PPP", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private FolhaPpp folhaPpp;

   public FolhaPppExameMedico() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataUltimo() {
      return dataUltimo;
   }

   public void setDataUltimo(Date dataUltimo) {
      this.dataUltimo = dataUltimo;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getNatureza() {
      return natureza;
   }

   public void setNatureza(String natureza) {
      this.natureza = natureza;
   }

   public String getExame() {
      return exame;
   }

   public void setExame(String exame) {
      this.exame = exame;
   }

   public String getIndicacaoResultados() {
      return indicacaoResultados;
   }

   public void setIndicacaoResultados(String indicacaoResultados) {
      this.indicacaoResultados = indicacaoResultados;
   }

   public FolhaPpp getFolhaPpp() {
      return folhaPpp;
   }

   public void setFolhaPpp(FolhaPpp folhaPpp) {
      this.folhaPpp = folhaPpp;
   }

   @Override
   public String toString() {
      return "FolhaPppExameMedico{" + "id=" + id + '}';
   }

}
