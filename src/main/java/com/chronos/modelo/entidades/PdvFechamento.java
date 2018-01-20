
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "PDV_FECHAMENTO")
public class PdvFechamento implements Serializable, Comparable<PdvFechamento> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO_PAGAMENTO")
    private String tipoPagamento;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @JoinColumn(name = "ID_NFCE_MOVIMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvMovimento pdvMovimento;

    public PdvFechamento() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public PdvMovimento getPdvMovimento() {
        return pdvMovimento;
    }

    public void setPdvMovimento(PdvMovimento pdvMovimento) {
        this.pdvMovimento = pdvMovimento;
    }

    @Override
    public String toString() {
        return tipoPagamento;
    }

    @Override
    public int compareTo(PdvFechamento outro) {
        if (this.getTipoPagamento().equals(outro.getTipoPagamento())) {
            return -1;
        }
        if (!this.getTipoPagamento().equals(outro.getTipoPagamento())) {
            return 1;
        }

        return 0;
    }
}
