
package com.chronos.modelo.entidades;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "CENTRO_RESULTADO")
public class CentroResultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CLASSIFICACAO")
    private String classificacao;
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "SOFRE_RATEIRO")
    private String sofreRateiro;
    @JoinColumn(name = "ID_PLANO_CENTRO_RESULTADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull(message = "Plano de centor de resultado Obrigatório")
    @Valid
    private PlanoCentroResultado planoCentroResultado;

    public CentroResultado() {
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

    public String getSofreRateiro() {
        return sofreRateiro;
    }

    public void setSofreRateiro(String sofreRateiro) {
        this.sofreRateiro = sofreRateiro;
    }

    public PlanoCentroResultado getPlanoCentroResultado() {
        return planoCentroResultado;
    }

    public void setPlanoCentroResultado(PlanoCentroResultado planoCentroResultado) {
        this.planoCentroResultado = planoCentroResultado;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final CentroResultado other = (CentroResultado) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
