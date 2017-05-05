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
@Table(name = "FOLHA_AFASTAMENTO")
public class FolhaAfastamento implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_INICIO")
   private Date dataInicio;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_FIM")
   private Date dataFim;
   @Column(name = "DIAS_AFASTADO")
   private Integer diasAfastado;
   @JoinColumn(name = "ID_FOLHA_TIPO_AFASTAMENTO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private FolhaTipoAfastamento folhaTipoAfastamento;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;

   public FolhaAfastamento() {
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

   public Date getDataFim() {
      return dataFim;
   }

   public void setDataFim(Date dataFim) {
      this.dataFim = dataFim;
   }

   public Integer getDiasAfastado() {
      return diasAfastado;
   }

   public void setDiasAfastado(Integer diasAfastado) {
      this.diasAfastado = diasAfastado;
   }

   public FolhaTipoAfastamento getFolhaTipoAfastamento() {
      return folhaTipoAfastamento;
   }

   public void setFolhaTipoAfastamento(FolhaTipoAfastamento folhaTipoAfastamento) {
      this.folhaTipoAfastamento = folhaTipoAfastamento;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   @Override
   public String toString() {
      return "FolhaAfastamento{" + "id=" + id + '}';
   }

   

}
