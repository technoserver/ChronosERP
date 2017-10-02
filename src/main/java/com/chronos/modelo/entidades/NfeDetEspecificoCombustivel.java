package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NFE_DET_ESPECIFICO_COMBUSTIVEL")
public class NfeDetEspecificoCombustivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CODIGO_ANP")
    private Integer codigoAnp;
    @Column(name = "CODIF")
    private String codif;
    @Column(name = "QUANTIDADE_TEMP_AMBIENTE")
    private BigDecimal quantidadeTempAmbiente;
    @Column(name = "UF_CONSUMO")
    private String ufConsumo;
    @Column(name = "BASE_CALCULO_CIDE")
    private BigDecimal baseCalculoCide;
    @Column(name = "ALIQUOTA_CIDE")
    private BigDecimal aliquotaCide;
    @Column(name = "VALOR_CIDE")
    private BigDecimal valorCide;
    @Column(name = "DESCRICAO_PRODUTO_ANP")
    private String descricaoProdutoAnp;
    @Column(name = "PERCENTUAL_PETROLEO")
    private BigDecimal percentualPetroleo;
    @Column(name = "PERCENTUAL_NACIONAL")
    private BigDecimal percentualNacional;
    @Column(name = "PERCENTUAL_IMPORTADO")
    private BigDecimal percentualImportado;
    @Column(name = "VALOR_PARTIDA")
    private BigDecimal valorPartida;
    @Column(name = "NUMERO_IDENTIFICACAO_BICO")
    private Integer numeroIdentificacaoBico;
    @Column(name = "NUMERO_IDENTIFICACAO_BOMBA")
    private Integer numeroIdentificacaoBomba;
    @Column(name = "NUMERO_IDENTIFICACAO_TANQUE")
    private Integer numeroIdentificacaoTanque;
    @Column(name = "VALOR_ENCERRANTE_INICIO")
    private BigDecimal valorEncerranteInicio;
    @Column(name = "VALOR_ENCERRANTE_FIM")
    private BigDecimal valorEncerranteFim;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetEspecificoCombustivel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigoAnp() {
        return codigoAnp;
    }

    public void setCodigoAnp(Integer codigoAnp) {
        this.codigoAnp = codigoAnp;
    }

    public String getDescricaoProdutoAnp() {
        return descricaoProdutoAnp;
    }

    public void setDescricaoProdutoAnp(String descricaoProdutoAnp) {
        this.descricaoProdutoAnp = descricaoProdutoAnp;
    }

    public BigDecimal getPercentualPetroleo() {
        return percentualPetroleo;
    }

    public void setPercentualPetroleo(BigDecimal percentualPetroleo) {
        this.percentualPetroleo = percentualPetroleo;
    }

    public BigDecimal getPercentualNacional() {
        return percentualNacional;
    }

    public void setPercentualNacional(BigDecimal percentualNacional) {
        this.percentualNacional = percentualNacional;
    }

    public BigDecimal getPercentualImportado() {
        return percentualImportado;
    }

    public void setPercentualImportado(BigDecimal percentualImportado) {
        this.percentualImportado = percentualImportado;
    }

    public BigDecimal getValorPartida() {
        return valorPartida;
    }

    public void setValorPartida(BigDecimal valorPartida) {
        this.valorPartida = valorPartida;
    }

    public Integer getNumeroIdentificacaoBico() {
        return numeroIdentificacaoBico;
    }

    public void setNumeroIdentificacaoBico(Integer numeroIdentificacaoBico) {
        this.numeroIdentificacaoBico = numeroIdentificacaoBico;
    }

    public Integer getNumeroIdentificacaoBomba() {
        return numeroIdentificacaoBomba;
    }

    public void setNumeroIdentificacaoBomba(Integer numeroIdentificacaoBomba) {
        this.numeroIdentificacaoBomba = numeroIdentificacaoBomba;
    }

    public Integer getNumeroIdentificacaoTanque() {
        return numeroIdentificacaoTanque;
    }

    public void setNumeroIdentificacaoTanque(Integer numeroIdentificacaoTanque) {
        this.numeroIdentificacaoTanque = numeroIdentificacaoTanque;
    }

    public BigDecimal getValorEncerranteInicio() {
        return valorEncerranteInicio;
    }

    public void setValorEncerranteInicio(BigDecimal valorEncerranteInicio) {
        this.valorEncerranteInicio = valorEncerranteInicio;
    }

    public BigDecimal getValorEncerranteFim() {
        return valorEncerranteFim;
    }

    public void setValorEncerranteFim(BigDecimal valorEncerranteFim) {
        this.valorEncerranteFim = valorEncerranteFim;
    }

    public String getCodif() {
        return codif;
    }

    public void setCodif(String codif) {
        this.codif = codif;
    }

    public BigDecimal getQuantidadeTempAmbiente() {
        return quantidadeTempAmbiente;
    }

    public void setQuantidadeTempAmbiente(BigDecimal quantidadeTempAmbiente) {
        this.quantidadeTempAmbiente = quantidadeTempAmbiente;
    }

    public String getUfConsumo() {
        return ufConsumo;
    }

    public void setUfConsumo(String ufConsumo) {
        this.ufConsumo = ufConsumo;
    }

    public BigDecimal getBaseCalculoCide() {
        return baseCalculoCide;
    }

    public void setBaseCalculoCide(BigDecimal baseCalculoCide) {
        this.baseCalculoCide = baseCalculoCide;
    }

    public BigDecimal getAliquotaCide() {
        return aliquotaCide;
    }

    public void setAliquotaCide(BigDecimal aliquotaCide) {
        this.aliquotaCide = aliquotaCide;
    }

    public BigDecimal getValorCide() {
        return valorCide;
    }

    public void setValorCide(BigDecimal valorCide) {
        this.valorCide = valorCide;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

}
