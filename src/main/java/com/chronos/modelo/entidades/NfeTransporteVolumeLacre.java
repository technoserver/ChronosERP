
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NFE_TRANSPORTE_VOLUME_LACRE")
public class NfeTransporteVolumeLacre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NUMERO")
    private String numero;
    @JoinColumn(name = "ID_NFE_TRANSPORTE_VOLUME", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeTransporteVolume nfeTransporteVolume;

    public NfeTransporteVolumeLacre() {
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

    public NfeTransporteVolume getNfeTransporteVolume() {
        return nfeTransporteVolume;
    }

    public void setNfeTransporteVolume(NfeTransporteVolume nfeTransporteVolume) {
        this.nfeTransporteVolume = nfeTransporteVolume;
    }



}
