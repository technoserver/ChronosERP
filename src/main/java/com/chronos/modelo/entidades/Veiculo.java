package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "VEICULO")
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO_INTERNO")
    private String codigoInterno;
    @Column(name = "RENAVAN")
    private String renavam;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "TARA")
    private Integer tara;
    @Column(name = "CAPACIDADE_KG")
    private Integer capacidadeKg;
    @Column(name = "CAPACIDADE_M3")
    private Integer capacidadeM3;
    @Column(name = "TIPO_PROPRIEDADE")
    private String tipoPropriedade;
    @Column(name = "TIPO_VEICULO")
    private Integer tipoVeiculo;
    @Column(name = "TIPO_RODADO")
    private String tipoRodado;
    @Column(name = "TIPO_CARROCERIA")
    private String tipoCarroceria;
    @Column(name = "UF")
    private String uf;
    @JoinColumn(name = "ID_PROPRIETARIO_VEICULO", referencedColumnName = "ID")
    @OneToOne(optional = true)
    private ProprietarioVeiculo proprietarioVeiculo;

    public Veiculo() {
    }

    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        placa = placa != null ? placa.replaceAll("-", "") : "";
        placa = placa.toUpperCase();


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getTara() {
        return tara;
    }

    public void setTara(Integer tara) {
        this.tara = tara;
    }

    public Integer getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(Integer capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public Integer getCapacidadeM3() {
        return capacidadeM3;
    }

    public void setCapacidadeM3(Integer capacidadeM3) {
        this.capacidadeM3 = capacidadeM3;
    }

    public String getTipoPropriedade() {
        return tipoPropriedade;
    }

    public void setTipoPropriedade(String tipoPropriedade) {
        this.tipoPropriedade = tipoPropriedade;
    }

    public Integer getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(Integer tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getTipoRodado() {
        return tipoRodado;
    }

    public void setTipoRodado(String tipoRodado) {
        this.tipoRodado = tipoRodado;
    }

    public String getTipoCarroceria() {
        return tipoCarroceria;
    }

    public void setTipoCarroceria(String tipoCarroceria) {
        this.tipoCarroceria = tipoCarroceria;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public ProprietarioVeiculo getProprietarioVeiculo() {
        return proprietarioVeiculo;
    }

    public void setProprietarioVeiculo(ProprietarioVeiculo proprietarioVeiculo) {
        this.proprietarioVeiculo = proprietarioVeiculo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Veiculo other = (Veiculo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
