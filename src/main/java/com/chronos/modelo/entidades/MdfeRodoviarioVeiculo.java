/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author john
 */
@Entity
@Table(name = "mdfe_rodoviario_veiculo")
public class MdfeRodoviarioVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Column(name = "codigo_interno", length = 10)
    private String codigoInterno;
    @Column(length = 7)
    private String placa;
    @Column(length = 11)
    private String renavam;
    private Integer tara;
    @Column(name = "capacidade_kg")
    private Integer capacidadeKg;
    @Column(name = "capacidade_m3")
    private Integer capacidadeM3;
    @Column(name = "tipo_rodado", length = 2)
    private String tipoRodado;
    @Column(name = "tipo_carroceria", length = 2)
    private String tipoCarroceria;
    @Column(name = "uf_licenciamento", length = 2)
    private String ufLicenciamento;
    @JoinColumn(name = "id_mdfe_rodoviario", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private MdfeRodoviario mdfeRodoviario;

    @OneToOne(mappedBy = "mdfeRodoviarioVeiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private MdfeRodoviarioProprietarioVeiculo mdfeRodoviarioProprietarioVeiculo;


    public MdfeRodoviarioVeiculo() {
    }

    public MdfeRodoviarioVeiculo(Integer id) {
        this.id = id;
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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
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

    public String getUfLicenciamento() {
        return ufLicenciamento;
    }

    public void setUfLicenciamento(String ufLicenciamento) {
        this.ufLicenciamento = ufLicenciamento;
    }


    public MdfeRodoviario getMdfeRodoviario() {
        return mdfeRodoviario;
    }

    public void setMdfeRodoviario(MdfeRodoviario mdfeRodoviario) {
        this.mdfeRodoviario = mdfeRodoviario;
    }


    public MdfeRodoviarioProprietarioVeiculo getMdfeRodoviarioProprietarioVeiculo() {
        return mdfeRodoviarioProprietarioVeiculo;
    }

    public void setMdfeRodoviarioProprietarioVeiculo(MdfeRodoviarioProprietarioVeiculo mdfeRodoviarioProprietarioVeiculo) {
        this.mdfeRodoviarioProprietarioVeiculo = mdfeRodoviarioProprietarioVeiculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof MdfeRodoviarioVeiculo)) {
            return false;
        }
        MdfeRodoviarioVeiculo other = (MdfeRodoviarioVeiculo) object;
        if ((this.placa == null && other.placa != null) || (this.placa != null && !this.placa.equals(other.placa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication1.MdfeRodoviarioVeiculo[ id=" + id + " ]";
    }
}
