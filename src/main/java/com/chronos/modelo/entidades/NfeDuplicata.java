package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "NFE_DUPLICATA")
public class NfeDuplicata implements Serializable, Comparable<NfeDuplicata> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO")
    private String numero;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO")
    private Date dataVencimento;
    @Column(name = "VALOR")
    private BigDecimal valor;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCabecalho nfeCabecalho;

    @Transient
    private ContaCaixa contaCaixa;

    public NfeDuplicata() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NfeDuplicata)) return false;
        NfeDuplicata that = (NfeDuplicata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, numero);
    }

    @Override
    public int compareTo(NfeDuplicata o) {
        if (this.getNumero().equals(o.getNumero())) {
            return -1;
        }
        if (!this.getNumero().equals(o.getNumero())) {
            return 1;
        }

        return 0;
    }
}
