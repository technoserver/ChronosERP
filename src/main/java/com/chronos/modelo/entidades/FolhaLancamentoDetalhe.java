package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "FOLHA_LANCAMENTO_DETALHE")
public class FolhaLancamentoDetalhe implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "ORIGEM")
   private BigDecimal origem;
   @Column(name = "PROVENTO")
   private BigDecimal provento;
   @Column(name = "DESCONTO")
   private BigDecimal desconto;
   @JoinColumn(name = "ID_FOLHA_LANCAMENTO_CABECALHO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private FolhaLancamentoCabecalho folhaLancamentoCabecalho;
   @JoinColumn(name = "ID_FOLHA_EVENTO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private FolhaEvento folhaEvento;

   public FolhaLancamentoDetalhe() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public BigDecimal getOrigem() {
      return origem;
   }

   public void setOrigem(BigDecimal origem) {
      this.origem = origem;
   }

   public BigDecimal getProvento() {
      return provento;
   }

   public void setProvento(BigDecimal provento) {
      this.provento = provento;
   }

   public BigDecimal getDesconto() {
      return desconto;
   }

   public void setDesconto(BigDecimal desconto) {
      this.desconto = desconto;
   }

   public FolhaLancamentoCabecalho getFolhaLancamentoCabecalho() {
      return folhaLancamentoCabecalho;
   }

   public void setFolhaLancamentoCabecalho(FolhaLancamentoCabecalho folhaLancamentoCabecalho) {
      this.folhaLancamentoCabecalho = folhaLancamentoCabecalho;
   }

   public FolhaEvento getFolhaEvento() {
      return folhaEvento;
   }

   public void setFolhaEvento(FolhaEvento folhaEvento) {
      this.folhaEvento = folhaEvento;
   }

   @Override
   public String toString() {
      return "FolhaLancamentoDetalhe{" + "id=" + id + '}';
   }

}
