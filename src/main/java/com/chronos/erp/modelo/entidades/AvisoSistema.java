package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "aviso_sistema")
public class AvisoSistema {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "aviso", length = 2147483647)
    private String aviso;
    @Column(name = "confirmado")
    private String confirmado;
    @Column(name = "confirmado_por")
    private String confirmadoPor;
    @Temporal(value = TemporalType.DATE)
    @Column(name = "data_confirmacao")
    private Date dataConfirmacao;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAviso() {
        return aviso;
    }

    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    public String getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(String confirmado) {
        this.confirmado = confirmado;
    }

    public String getConfirmadoPor() {
        return confirmadoPor;
    }

    public void setConfirmadoPor(String confirmadoPor) {
        this.confirmadoPor = confirmadoPor;
    }

    public Date getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(Date dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvisoSistema)) return false;
        AvisoSistema that = (AvisoSistema) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
