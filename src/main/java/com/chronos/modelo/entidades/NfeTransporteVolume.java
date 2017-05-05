
package com.chronos.modelo.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "NFE_TRANSPORTE_VOLUME")
public class NfeTransporteVolume implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "QUANTIDADE")
    private Integer quantidade;
    @Column(name = "ESPECIE")
    private String especie;
    @Column(name = "MARCA")
    private String marca;
    @Column(name = "NUMERACAO")
    private String numeracao;
    @Column(name = "PESO_LIQUIDO")
    private BigDecimal pesoLiquido;
    @Column(name = "PESO_BRUTO")
    private BigDecimal pesoBruto;
    @JoinColumn(name = "ID_NFE_TRANSPORTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeTransporte nfeTransporte;
    @OneToMany(mappedBy = "nfeTransporteVolume", orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<NfeTransporteVolumeLacre> listaTransporteVolumeLacre;

    public NfeTransporteVolume() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNumeracao() {
        return numeracao;
    }

    public void setNumeracao(String numeracao) {
        this.numeracao = numeracao;
    }

    public BigDecimal getPesoLiquido() {
        return pesoLiquido;
    }

    public void setPesoLiquido(BigDecimal pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    public BigDecimal getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(BigDecimal pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public NfeTransporte getNfeTransporte() {
        return nfeTransporte;
    }

    public void setNfeTransporte(NfeTransporte nfeTransporte) {
        this.nfeTransporte = nfeTransporte;
    }

    public Set<NfeTransporteVolumeLacre> getListaTransporteVolumeLacre() {
		return listaTransporteVolumeLacre;
	}

	public void setListaTransporteVolumeLacre(Set<NfeTransporteVolumeLacre> listaTransporteVolumeLacre) {
		this.listaTransporteVolumeLacre = listaTransporteVolumeLacre;
	}



}
