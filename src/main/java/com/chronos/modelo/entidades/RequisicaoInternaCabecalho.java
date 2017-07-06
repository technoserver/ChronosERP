package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "REQUISICAO_INTERNA_CABECALHO")
public class RequisicaoInternaCabecalho implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_REQUISICAO")
   private Date dataRequisicao;
   @Column(name = "SITUACAO")
   private String situacao;
   @JoinColumn(name = "ID_COLABORADOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Colaborador colaborador;
   @OneToMany(fetch = FetchType.LAZY, mappedBy = "requisicaoInternaCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
   private Set<RequisicaoInternaDetalhe> listaRequisicaoInternaDetalhe;

   public RequisicaoInternaCabecalho() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public Date getDataRequisicao() {
      return dataRequisicao;
   }

   public void setDataRequisicao(Date dataRequisicao) {
      this.dataRequisicao = dataRequisicao;
   }

   public String getSituacao() {
      return situacao;
   }

   public void setSituacao(String situacao) {
      this.situacao = situacao;
   }

   public Colaborador getColaborador() {
      return colaborador;
   }

   public void setColaborador(Colaborador colaborador) {
      this.colaborador = colaborador;
   }

   public Set<RequisicaoInternaDetalhe> getListaRequisicaoInternaDetalhe() {
      return listaRequisicaoInternaDetalhe;
   }

   public void setListaRequisicaoInternaDetalhe(Set<RequisicaoInternaDetalhe> listaRequisicaoInternaDetalhe) {
      this.listaRequisicaoInternaDetalhe = listaRequisicaoInternaDetalhe;
   }

   @Override
   public String toString() {
      return "RequisicaoInternaCabecalho{" + "id=" + id + '}';
   }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequisicaoInternaCabecalho other = (RequisicaoInternaCabecalho) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
   
   
   

}
