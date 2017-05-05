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
@Table(name = "PRODUTO_LOTE")
public class ProdutoLote implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "CODIGO")
   private String codigo;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_CADASTRO")
   private Date dataCadastro;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_COMPRA")
   private Date dataCompra;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_FABRICACAO")
   private Date dataFabricacao;
   @Temporal(TemporalType.DATE)
   @Column(name = "DATA_VALIDADE")
   private Date dataValidade;
   @Column(name = "QUANTIDADE")
   private BigDecimal quantidade;
   @Column(name = "PRECO_MAXIMO_CONSUMIDOR")
   private BigDecimal precoMaximoConsumidor;
   @Column(name = "OBSERVACAO")
   private String observacao;
   @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Produto produto;

   public ProdutoLote() {
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

   public Date getDataCadastro() {
      return dataCadastro;
   }

   public void setDataCadastro(Date dataCadastro) {
      this.dataCadastro = dataCadastro;
   }

   public Date getDataCompra() {
      return dataCompra;
   }

   public void setDataCompra(Date dataCompra) {
      this.dataCompra = dataCompra;
   }

   public Date getDataFabricacao() {
      return dataFabricacao;
   }

   public void setDataFabricacao(Date dataFabricacao) {
      this.dataFabricacao = dataFabricacao;
   }

   public Date getDataValidade() {
      return dataValidade;
   }

   public void setDataValidade(Date dataValidade) {
      this.dataValidade = dataValidade;
   }

   public BigDecimal getQuantidade() {
      return quantidade;
   }

   public void setQuantidade(BigDecimal quantidade) {
      this.quantidade = quantidade;
   }

   public BigDecimal getPrecoMaximoConsumidor() {
      return precoMaximoConsumidor;
   }

   public void setPrecoMaximoConsumidor(BigDecimal precoMaximoConsumidor) {
      this.precoMaximoConsumidor = precoMaximoConsumidor;
   }

   public String getObservacao() {
      return observacao;
   }

   public void setObservacao(String observacao) {
      this.observacao = observacao;
   }

   public Produto getProduto() {
      return produto;
   }

   public void setProduto(Produto produto) {
      this.produto = produto;
   }

   @Override
   public String toString() {
      return "ProdutoLote{" + "id=" + id + '}';
   }

}
