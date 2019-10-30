package com.chronos.erp.modelo.entidades;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by john on 01/02/18.
 */
@Entity
@Table(name = "movimento_conta_pessoa")
public class MovimentoContaPessoa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_movimento")
    @NotNull
    private Date dataMovimento;
    @Column(name = "codigo_modulo")
    @NotEmpty
    private String codigoModulo;
    @Column(name = "numero_documento")
    @NotEmpty
    private String numeroDocumento;
    @Column(name = "tipo_movimento")
    @NotEmpty
    private String tipoMovimento;
    @Column(name = "valor")
    @NotNull
    @DecimalMax(value = "9999999.99", message = "O valor deve ser menor que R$9.999.999,99")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que R$ 0,01")
    private BigDecimal valor;
    @JoinColumn(name = "id_conta_pessoa", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaPessoa contaPessoa;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(Date dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ContaPessoa getContaPessoa() {
        return contaPessoa;
    }

    public void setContaPessoa(ContaPessoa contaPessoa) {
        this.contaPessoa = contaPessoa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovimentoContaPessoa)) return false;

        MovimentoContaPessoa that = (MovimentoContaPessoa) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
