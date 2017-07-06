/*
* The MIT License
* 
* Copyright: Copyright (C) 2014 chronosinfo.COM
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
* 
* The author may be contacted at: chronosinfo.com@gmail.com
*
* @author John Vanderson M Lima (T2Ti.com)
* @version 2.0
*/
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
