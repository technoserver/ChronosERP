
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "NFCE_FECHAMENTO")
public class NfceFechamento implements Serializable, Comparable<NfceFechamento> {

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
    private NfceMovimento nfceMovimento;

    public NfceFechamento() {
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

    public NfceMovimento getNfceMovimento() {
        return nfceMovimento;
    }

    public void setNfceMovimento(NfceMovimento nfceMovimento) {
        this.nfceMovimento = nfceMovimento;
    }

    @Override
    public String toString() {
        return tipoPagamento;
    }

    @Override
    public int compareTo(NfceFechamento outro) {
        if (this.getTipoPagamento().equals(outro.getTipoPagamento())) {
            return -1;
        }
        if (!this.getTipoPagamento().equals(outro.getTipoPagamento())) {
            return 1;
        }

        return 0;
    }
}
