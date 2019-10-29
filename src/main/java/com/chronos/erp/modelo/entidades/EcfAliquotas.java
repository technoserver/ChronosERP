
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "ECF_ALIQUOTAS")
public class EcfAliquotas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TOTALIZADOR_PARCIAL")
    private String totalizadorParcial;
    @Column(name = "ECF_ICMS_ST")
    private String ecfIcmsSt;
    @Column(name = "PAF_P_ST")
    private String pafPSt;

    public EcfAliquotas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTotalizadorParcial() {
        return totalizadorParcial;
    }

    public void setTotalizadorParcial(String totalizadorParcial) {
        this.totalizadorParcial = totalizadorParcial;
    }

    public String getEcfIcmsSt() {
        return ecfIcmsSt;
    }

    public void setEcfIcmsSt(String ecfIcmsSt) {
        this.ecfIcmsSt = ecfIcmsSt;
    }

    public String getPafPSt() {
        return pafPSt;
    }

    public void setPafPSt(String pafPSt) {
        this.pafPSt = pafPSt;
    }


    @Override
    public String toString() {
        return "com.t2tierp.pafecf.java.EcfAliquotasVO[id=" + id + "]";
    }

}
