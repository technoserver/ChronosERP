
package com.chronos.modelo.entidades;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "CHEQUE")
public class Cheque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO")    
    @NotNull(message = "Numero não pode ser nulo")
    private int numero;
    @Column(name = "STATUS_CHEQUE")
    @NotEmpty( message = "Status do cheque não pode está em branco")
    @NotNull(message = "Status do Cheque não pode ser nulo")
    private String statusCheque;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_STATUS")
    @NotNull(message = "Data não pode ser nula")
    private Date dataStatus;
    @JoinColumn(name = "ID_TALONARIO_CHEQUE", referencedColumnName = "ID")
    @ManyToOne(optional = false)    
    @NotNull(message = "Talonário Obrigatorio")    
    private TalonarioCheque talonarioCheque;

    public Cheque() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getStatusCheque() {
        return statusCheque;
    }

    public void setStatusCheque(String statusCheque) {
        this.statusCheque = statusCheque;
    }

    public Date getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Date dataStatus) {
        this.dataStatus = dataStatus;
    }

    public TalonarioCheque getTalonarioCheque() {
        return talonarioCheque;
    }

    public void setTalonarioCheque(TalonarioCheque talonarioCheque) {
        this.talonarioCheque = talonarioCheque;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.numero);
        hash = 17 * hash + Objects.hashCode(this.statusCheque);
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
        final Cheque other = (Cheque) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        return Objects.equals(this.statusCheque, other.statusCheque);
    }

    
    
    
    @Override
    public String toString() {
        return numero+"";
    }

    
}
