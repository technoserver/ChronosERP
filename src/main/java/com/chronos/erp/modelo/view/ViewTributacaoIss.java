/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.modelo.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author john
 */
@Entity
@Table(name = "view_tributacao_iss")
public class ViewTributacaoIss implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id")
    @Id
    private Integer id;
    @Column(name = "descricao", length = 100)
    private String descricao;
    @Column(name = "descricao_na_nf", length = 100)
    private String descricaoNaNf;
    @Column(name = "cfop")
    private Integer cfop;
    @Column(name = "modalidade_base_calculo")
    private Character modalidadeBaseCalculo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "porcento_base_calculo", precision = 18, scale = 6)
    private BigDecimal porcentoBaseCalculo;
    @Column(name = "aliquota_porcento", precision = 18, scale = 6)
    private BigDecimal aliquotaPorcento;
    @Column(name = "aliquota_unidade", precision = 18, scale = 6)
    private BigDecimal aliquotaUnidade;
    @Column(name = "valor_preco_maximo", precision = 18, scale = 6)
    private BigDecimal valorPrecoMaximo;
    @Column(name = "valor_pauta_fiscal", precision = 18, scale = 6)
    private BigDecimal valorPautaFiscal;
    @Column(name = "codigo_tributacao")
    private Character codigoTributacao;
    @Column(name = "indicador_exigibilidade")
    private Integer indicadorExigibilidade;
    @Column(name = "indicador_incentivo_fiscal")
    private Integer indicadorIncentivoFiscal;
    @Column(name = "id_tribut_grupo_tributario")
    private Integer idTributGrupoTributario;
    @Column(name = "id_tribut_operacao_fiscal")
    private Integer idTributOperacaoFiscal;

    public ViewTributacaoIss() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoNaNf() {
        return descricaoNaNf;
    }

    public void setDescricaoNaNf(String descricaoNaNf) {
        this.descricaoNaNf = descricaoNaNf;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public Character getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(Character modalidadeBaseCalculo) {
        this.modalidadeBaseCalculo = modalidadeBaseCalculo;
    }

    public BigDecimal getPorcentoBaseCalculo() {
        return porcentoBaseCalculo;
    }

    public void setPorcentoBaseCalculo(BigDecimal porcentoBaseCalculo) {
        this.porcentoBaseCalculo = porcentoBaseCalculo;
    }

    public BigDecimal getAliquotaPorcento() {
        return aliquotaPorcento;
    }

    public void setAliquotaPorcento(BigDecimal aliquotaPorcento) {
        this.aliquotaPorcento = aliquotaPorcento;
    }

    public BigDecimal getAliquotaUnidade() {
        return aliquotaUnidade;
    }

    public void setAliquotaUnidade(BigDecimal aliquotaUnidade) {
        this.aliquotaUnidade = aliquotaUnidade;
    }

    public BigDecimal getValorPrecoMaximo() {
        return valorPrecoMaximo;
    }

    public void setValorPrecoMaximo(BigDecimal valorPrecoMaximo) {
        this.valorPrecoMaximo = valorPrecoMaximo;
    }

    public BigDecimal getValorPautaFiscal() {
        return valorPautaFiscal;
    }

    public void setValorPautaFiscal(BigDecimal valorPautaFiscal) {
        this.valorPautaFiscal = valorPautaFiscal;
    }


    public Character getCodigoTributacao() {
        return codigoTributacao;
    }

    public void setCodigoTributacao(Character codigoTributacao) {
        this.codigoTributacao = codigoTributacao;
    }

    public Integer getIndicadorExigibilidade() {
        return indicadorExigibilidade;
    }

    public void setIndicadorExigibilidade(Integer indicadorExigibilidade) {
        this.indicadorExigibilidade = indicadorExigibilidade;
    }

    public Integer getIndicadorIncentivoFiscal() {
        return indicadorIncentivoFiscal;
    }

    public void setIndicadorIncentivoFiscal(Integer indicadorIncentivoFiscal) {
        this.indicadorIncentivoFiscal = indicadorIncentivoFiscal;
    }

    public Integer getIdTributGrupoTributario() {
        return idTributGrupoTributario;
    }

    public void setIdTributGrupoTributario(Integer idTributGrupoTributario) {
        this.idTributGrupoTributario = idTributGrupoTributario;
    }

    public Integer getIdTributOperacaoFiscal() {
        return idTributOperacaoFiscal;
    }

    public void setIdTributOperacaoFiscal(Integer idTributOperacaoFiscal) {
        this.idTributOperacaoFiscal = idTributOperacaoFiscal;
    }

}
