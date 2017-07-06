package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PATRIM_GRUPO_BEM")
public class PatrimGrupoBem implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Basic(optional = false)
   @Column(name = "ID")
   private Integer id;
   @Column(name = "CODIGO")
   private String codigo;
   @Column(name = "NOME")
   private String nome;
   @Column(name = "DESCRICAO")
   private String descricao;
   @Column(name = "CONTA_ATIVO_IMOBILIZADO")
   private String contaAtivoImobilizado;
   @Column(name = "CONTA_DEPRECIACAO_ACUMULADA")
   private String contaDepreciacaoAcumulada;
   @Column(name = "CONTA_DESPESA_DEPRECIACAO")
   private String contaDespesaDepreciacao;
   @Column(name = "CODIGO_HISTORICO")
   private Integer codigoHistorico;
   @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
   @ManyToOne(optional = false)
   private Empresa empresa;

   public PatrimGrupoBem() {
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

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getDescricao() {
      return descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

   public String getContaAtivoImobilizado() {
      return contaAtivoImobilizado;
   }

   public void setContaAtivoImobilizado(String contaAtivoImobilizado) {
      this.contaAtivoImobilizado = contaAtivoImobilizado;
   }

   public String getContaDepreciacaoAcumulada() {
      return contaDepreciacaoAcumulada;
   }

   public void setContaDepreciacaoAcumulada(String contaDepreciacaoAcumulada) {
      this.contaDepreciacaoAcumulada = contaDepreciacaoAcumulada;
   }

   public String getContaDespesaDepreciacao() {
      return contaDespesaDepreciacao;
   }

   public void setContaDespesaDepreciacao(String contaDespesaDepreciacao) {
      this.contaDespesaDepreciacao = contaDespesaDepreciacao;
   }

   public Integer getCodigoHistorico() {
      return codigoHistorico;
   }

   public void setCodigoHistorico(Integer codigoHistorico) {
      this.codigoHistorico = codigoHistorico;
   }

   public Empresa getEmpresa() {
      return empresa;
   }

   public void setEmpresa(Empresa empresa) {
      this.empresa = empresa;
   }

   @Override
   public String toString() {
      return nome;
   }

}
