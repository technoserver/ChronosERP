package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "VENDA_FRETE")
public class VendaFrete implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CONHECIMENTO")
    private Integer conhecimento;
    @Column(name = "RESPONSAVEL")
    private String responsavel;
    @Column(name = "PLACA")
    private String placa;
    @Column(name = "UF_PLACA")
    private String ufPlaca;
    @Column(name = "SELO_FISCAL")
    private Integer seloFiscal;
    @Column(name = "QUANTIDADE_VOLUME")
    private BigDecimal quantidadeVolume;
    @Column(name = "MARCA_VOLUME")
    private String marcaVolume;
    @Column(name = "ESPECIE_VOLUME")
    private String especieVolume;
    @Column(name = "PESO_BRUTO")
    private BigDecimal pesoBruto;
    @Column(name = "PESO_LIQUIDO")
    private BigDecimal pesoLiquido;
    @JoinColumn(name = "ID_VENDA_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private VendaCabecalho vendaCabecalho;
    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Transportadora transportadora;

    public VendaFrete() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(Integer conhecimento) {
        this.conhecimento = conhecimento;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getUfPlaca() {
        return ufPlaca;
    }

    public void setUfPlaca(String ufPlaca) {
        this.ufPlaca = ufPlaca;
    }

    public Integer getSeloFiscal() {
        return seloFiscal;
    }

    public void setSeloFiscal(Integer seloFiscal) {
        this.seloFiscal = seloFiscal;
    }

    public BigDecimal getQuantidadeVolume() {
        return quantidadeVolume;
    }

    public void setQuantidadeVolume(BigDecimal quantidadeVolume) {
        this.quantidadeVolume = quantidadeVolume;
    }

    public String getMarcaVolume() {
        return marcaVolume;
    }

    public void setMarcaVolume(String marcaVolume) {
        this.marcaVolume = marcaVolume;
    }

    public String getEspecieVolume() {
        return especieVolume;
    }

    public void setEspecieVolume(String especieVolume) {
        this.especieVolume = especieVolume;
    }

    public BigDecimal getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(BigDecimal pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public BigDecimal getPesoLiquido() {
        return pesoLiquido;
    }

    public void setPesoLiquido(BigDecimal pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    public VendaCabecalho getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final VendaFrete other = (VendaFrete) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "VendaFrete{" + "id=" + id + '}';
    }

}
