
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NFE_TRANSPORTE_REBOQUE")
public class NfeTransporteReboque implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "UF")
    private String uf;
    @Column(name = "RNTC")
    private String rntc;
    @JoinColumn(name = "ID_NFE_TRANSPORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeTransporte nfeTransporte;

    public NfeTransporteReboque() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getRntc() {
        return rntc;
    }

    public void setRntc(String rntc) {
        this.rntc = rntc;
    }

    public NfeTransporte getNfeTransporte() {
        return nfeTransporte;
    }

    public void setNfeTransporte(NfeTransporte nfeTransporte) {
        this.nfeTransporte = nfeTransporte;
    }


}
