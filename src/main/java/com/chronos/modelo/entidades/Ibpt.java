/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author john
 */
@Entity
@Table(name = "ibpt")
public class Ibpt implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(length = 8)
    private String ncm;
    @Column(length = 2)
    private String ex;
    private Character tipo;
    @Column(length = 2147483647)
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nacional_federal", precision = 18, scale = 6)
    private BigDecimal nacionalFederal;
    @Column(name = "importados_federal", precision = 18, scale = 6)
    private BigDecimal importadosFederal;
    @Column(precision = 18, scale = 6)
    private BigDecimal estadual;
    @Column(precision = 18, scale = 6)
    private BigDecimal municipal;
    @Column(name = "vigencia_inicio")
    @Temporal(TemporalType.DATE)
    private Date vigenciaInicio;
    @Column(name = "vigencia_fim")
    @Temporal(TemporalType.DATE)
    private Date vigenciaFim;
    @Column(length = 6)
    private String chave;
    @Column(length = 6)
    private String versao;
    @Column(length = 34)
    private String fonte;

   public Ibpt() {
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

//   public int getCodigo() {
//      return codigo;
//   }
//
//   public void setCodigo(int codigo) {
//      this.codigo = codigo;
//   }

   public String getEx() {
      return ex;
   }

   public void setEx(String ex) {
      this.ex = ex;
   }

//   public int getTabela() {
//      return tabela;
//   }
//
//   public void setTabela(int tabela) {
//      this.tabela = tabela;
//   }

   public String getDescricao() {
      return descricao;
   }

   public void setDescricao(String descricao) {
      this.descricao = descricao;
   }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getNacionalFederal() {
        return nacionalFederal;
    }

    public void setNacionalFederal(BigDecimal nacionalFederal) {
        this.nacionalFederal = nacionalFederal;
    }

    public BigDecimal getImportadosFederal() {
        return importadosFederal;
    }

    public void setImportadosFederal(BigDecimal importadosFederal) {
        this.importadosFederal = importadosFederal;
    }

    public BigDecimal getEstadual() {
        return estadual;
    }

    public void setEstadual(BigDecimal estadual) {
        this.estadual = estadual;
    }

    public BigDecimal getMunicipal() {
        return municipal;
    }

    public void setMunicipal(BigDecimal municipal) {
        this.municipal = municipal;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

   

   public Date getVigenciaInicio() {
      return vigenciaInicio;
   }

   public void setVigenciaInicio(Date vigenciaInicio) {
      this.vigenciaInicio = vigenciaInicio;
   }

   public Date getVigenciaFim() {
      return vigenciaFim;
   }

   public void setVigenciaFim(Date vigenciaFim) {
      this.vigenciaFim = vigenciaFim;
   }

 
}
