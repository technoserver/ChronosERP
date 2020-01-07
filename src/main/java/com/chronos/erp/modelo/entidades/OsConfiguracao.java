
package com.chronos.erp.modelo.entidades;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "OS_CONFIGURACAO")
public class OsConfiguracao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "qtd_dias_uteis_para_entrega")
    private Integer qtdDiasUteisParaEntrega;
    @Column(name = "observacao_padrao")
    private String observacaoPadrao;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    @NotNull
    private Empresa empresa;

    public OsConfiguracao() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQtdDiasUteisParaEntrega() {
        return qtdDiasUteisParaEntrega;
    }

    public void setQtdDiasUteisParaEntrega(Integer qtdDiasUteisParaEntrega) {
        this.qtdDiasUteisParaEntrega = qtdDiasUteisParaEntrega;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final OsConfiguracao other = (OsConfiguracao) obj;
        return Objects.equals(this.id, other.id);
    }
}
