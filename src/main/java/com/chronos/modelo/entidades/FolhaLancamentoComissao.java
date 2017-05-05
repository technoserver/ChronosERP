package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "FOLHA_LANCAMENTO_COMISSAO")
public class FolhaLancamentoComissao implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "COMPETENCIA")
   private String competencia;
   @Temporal(TemporalType.DATE)
   @Column(name = "VENCIMENTO")
   private Date vencimento;
   @Column(name = "BASE_CALCULO")
   private BigDecimal baseCalculo;
   @Column(name = "VALOR_COMISSAO")
   private BigDecimal valorComissao;
   @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Vendedor vendedor;

   public FolhaLancamentoComissao() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getCompetencia() {
      return competencia;
   }

   public void setCompetencia(String competencia) {
      this.competencia = competencia;
   }

   public Date getVencimento() {
      return vencimento;
   }

   public void setVencimento(Date vencimento) {
      this.vencimento = vencimento;
   }

   public BigDecimal getBaseCalculo() {
      return baseCalculo;
   }

   public void setBaseCalculo(BigDecimal baseCalculo) {
      this.baseCalculo = baseCalculo;
   }

   public BigDecimal getValorComissao() {
      return valorComissao;
   }

   public void setValorComissao(BigDecimal valorComissao) {
      this.valorComissao = valorComissao;
   }

   public Vendedor getVendedor() {
      return vendedor;
   }

   public void setVendedor(Vendedor vendedor) {
      this.vendedor = vendedor;
   }

   @Override
   public String toString() {
      return "FolhaLancamentoComissao{" + "id=" + id + '}';
   }

}
