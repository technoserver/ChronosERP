
package com.chronos.modelo.entidades;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "TRIBUT_OPERACAO_FISCAL")
public class TributOperacaoFiscal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotBlank
    @Column(name = "DESCRICAO")
    private String descricao;
    @NotBlank
    @Column(name = "DESCRICAO_NA_NF")
    private String descricaoNaNf;
    @Column(name = "CFOP")
    @Min(message = "CFOP invalido", value = 1000)
    @Max(message = "CFOP invalido", value = 9999)
    private Integer cfop;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Empresa empresa;
   
    
    public TributOperacaoFiscal() {
    }

    public TributOperacaoFiscal(Integer id) {
        this.id = id;

    }

    public TributOperacaoFiscal(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoNaNf() {
        return descricaoNaNf;
    }

    public void setDescricaoNaNf(String descricaoNaNf) {
        this.descricaoNaNf = descricaoNaNf;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

 

   @Override
   public String toString() {
      return descricao ;
   }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final TributOperacaoFiscal other = (TributOperacaoFiscal) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

  

}
