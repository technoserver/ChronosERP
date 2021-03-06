package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "NFE_DET_ESPECIFICO_MEDICAMENTO")
public class NfeDetEspecificoMedicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO_PRODUTO_ANVISA")
    private String codigoProdutoAnvisa;
    @Column(name = "PRECO_MAXIMO_CONSUMIDOR")
    private BigDecimal precoMaximoConsumidor;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoMedicamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoProdutoAnvisa() {
        return codigoProdutoAnvisa;
    }

    public void setCodigoProdutoAnvisa(String codigoProdutoAnvisa) {
        this.codigoProdutoAnvisa = codigoProdutoAnvisa;
    }

    public BigDecimal getPrecoMaximoConsumidor() {
        return precoMaximoConsumidor;
    }

    public void setPrecoMaximoConsumidor(BigDecimal precoMaximoConsumidor) {
        this.precoMaximoConsumidor = precoMaximoConsumidor;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final NfeDetEspecificoMedicamento other = (NfeDetEspecificoMedicamento) obj;
        return Objects.equals(this.id, other.id);
    }

}
