
package com.chronos.modelo.entidades;


import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "NATUREZA_FINANCEIRA")
public class NaturezaFinanceira implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @NotBlank
    @Column(name = "CLASSIFICACAO")
    private String classificacao;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "APLICACAO")
    private String aplicacao;
    @Column(name = "APARECE_A_PAGAR")
    private String apareceAPagar;
    @Column(name = "APARECE_A_RECEBER")
    private String apareceAReceber;
    @JoinColumn(name = "ID_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne
    private NaturezaFinanceira naturezaFinanceira;
    @JoinColumn(name = "ID_PLANO_NATUREZA_FINANCEIRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private PlanoNaturezaFinanceira planoNaturezaFinanceira;

    public NaturezaFinanceira() {
    }

    public NaturezaFinanceira(Integer id) {
        this.id = id;

    }

    public NaturezaFinanceira(Integer id,String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public NaturezaFinanceira(Integer id, String classificacao, String descricao, String tipo, int nivel) {
        this.id = id;
        this.classificacao = classificacao;
        this.descricao = descricao;
        this.tipo = tipo;
        this.planoNaturezaFinanceira = new PlanoNaturezaFinanceira(nivel);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    public String getApareceAPagar() {
        return apareceAPagar;
    }

    public void setApareceAPagar(String apareceAPagar) {
        this.apareceAPagar = apareceAPagar;
    }

    public String getApareceAReceber() {
        return apareceAReceber;
    }

    public void setApareceAReceber(String apareceAReceber) {
        this.apareceAReceber = apareceAReceber;
    }



    public PlanoNaturezaFinanceira getPlanoNaturezaFinanceira() {
        return planoNaturezaFinanceira;
    }

    public void setPlanoNaturezaFinanceira(PlanoNaturezaFinanceira planoNaturezaFinanceira) {
        this.planoNaturezaFinanceira = planoNaturezaFinanceira;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NaturezaFinanceira other = (NaturezaFinanceira) obj;
        return Objects.equals(this.id, other.id);
    }
    
    

}
