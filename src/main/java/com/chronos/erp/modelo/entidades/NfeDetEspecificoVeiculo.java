
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "NFE_DET_ESPECIFICO_VEICULO")
public class NfeDetEspecificoVeiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO_OPERACAO")
    private String tipoOperacao;
    @Column(name = "CHASSI")
    private String chassi;
    @Column(name = "COR")
    private String cor;
    @Column(name = "DESCRICAO_COR")
    private String descricaoCor;
    @Column(name = "POTENCIA_MOTOR")
    private String potenciaMotor;
    @Column(name = "CILINDRADAS")
    private String cilindradas;
    @Column(name = "PESO_LIQUIDO")
    private String pesoLiquido;
    @Column(name = "PESO_BRUTO")
    private String pesoBruto;
    @Column(name = "NUMERO_SERIE")
    private String numeroSerie;
    @Column(name = "TIPO_COMBUSTIVEL")
    private String tipoCombustivel;
    @Column(name = "NUMERO_MOTOR")
    private String numeroMotor;
    @Column(name = "CAPACIDADE_MAXIMA_TRACAO")
    private String capacidadeMaximaTracao;
    @Column(name = "DISTANCIA_EIXOS")
    private String distanciaEixos;
    @Column(name = "ANO_MODELO")
    private String anoModelo;
    @Column(name = "ANO_FABRICACAO")
    private String anoFabricacao;
    @Column(name = "TIPO_PINTURA")
    private String tipoPintura;
    @Column(name = "TIPO_VEICULO")
    private String tipoVeiculo;
    @Column(name = "ESPECIE_VEICULO")
    private String especieVeiculo;
    @Column(name = "CONDICAO_VIN")
    private String condicaoVin;
    @Column(name = "CONDICAO_VEICULO")
    private String condicaoVeiculo;
    @Column(name = "CODIGO_MARCA_MODELO")
    private String codigoMarcaModelo;
    @Column(name = "CODIGO_COR")
    private String codigoCor;
    @Column(name = "LOTACAO")
    private Integer lotacao;
    @Column(name = "RESTRICAO")
    private String restricao;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoVeiculo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoOperacao() {
        return tipoOperacao;
    }

    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getDescricaoCor() {
        return descricaoCor;
    }

    public void setDescricaoCor(String descricaoCor) {
        this.descricaoCor = descricaoCor;
    }

    public String getPotenciaMotor() {
        return potenciaMotor;
    }

    public void setPotenciaMotor(String potenciaMotor) {
        this.potenciaMotor = potenciaMotor;
    }

    public String getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(String cilindradas) {
        this.cilindradas = cilindradas;
    }

    public String getPesoLiquido() {
        return pesoLiquido;
    }

    public void setPesoLiquido(String pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    public String getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(String pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public String getNumeroMotor() {
        return numeroMotor;
    }

    public void setNumeroMotor(String numeroMotor) {
        this.numeroMotor = numeroMotor;
    }

    public String getCapacidadeMaximaTracao() {
        return capacidadeMaximaTracao;
    }

    public void setCapacidadeMaximaTracao(String capacidadeMaximaTracao) {
        this.capacidadeMaximaTracao = capacidadeMaximaTracao;
    }

    public String getDistanciaEixos() {
        return distanciaEixos;
    }

    public void setDistanciaEixos(String distanciaEixos) {
        this.distanciaEixos = distanciaEixos;
    }

    public String getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(String anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(String anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getTipoPintura() {
        return tipoPintura;
    }

    public void setTipoPintura(String tipoPintura) {
        this.tipoPintura = tipoPintura;
    }

    public String getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(String tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public String getEspecieVeiculo() {
        return especieVeiculo;
    }

    public void setEspecieVeiculo(String especieVeiculo) {
        this.especieVeiculo = especieVeiculo;
    }

    public String getCondicaoVin() {
        return condicaoVin;
    }

    public void setCondicaoVin(String condicaoVin) {
        this.condicaoVin = condicaoVin;
    }

    public String getCondicaoVeiculo() {
        return condicaoVeiculo;
    }

    public void setCondicaoVeiculo(String condicaoVeiculo) {
        this.condicaoVeiculo = condicaoVeiculo;
    }

    public String getCodigoMarcaModelo() {
        return codigoMarcaModelo;
    }

    public void setCodigoMarcaModelo(String codigoMarcaModelo) {
        this.codigoMarcaModelo = codigoMarcaModelo;
    }

    public String getCodigoCor() {
        return codigoCor;
    }

    public void setCodigoCor(String codigoCor) {
        this.codigoCor = codigoCor;
    }

    public Integer getLotacao() {
        return lotacao;
    }

    public void setLotacao(Integer lotacao) {
        this.lotacao = lotacao;
    }

    public String getRestricao() {
        return restricao;
    }

    public void setRestricao(String restricao) {
        this.restricao = restricao;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }


}