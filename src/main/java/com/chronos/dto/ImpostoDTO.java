/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.dto;

import com.chronos.modelo.entidades.NfeDetalheImpostoIcms;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * @author john
 */
public class ImpostoDTO {

    private String origem;
    private String cst;
    private String modalidadeBaseCalculo;
    private String modalidadeBaseCalculoST;
    private BigDecimal aliquotaIcms;
    private BigDecimal aliquotaIcmsST;
    private BigDecimal baseCalculoIcms;
    private BigDecimal baseCalculoIcmsST;
    private BigDecimal reducaoBaseCalculoIcms;
    private BigDecimal reducaoBaseCalculoIcmsST;
    private BigDecimal valorIcms;
    private BigDecimal valorIcmsST;
    private BigDecimal mva;

    private BigDecimal aliquotaCreditoIcms;
    private BigDecimal valorCreditoIcms;

    public ImpostoDTO() {
    }

    public ImpostoDTO(NfeDetalheImpostoIcms icms) {
        this.origem = icms.getOrigemMercadoria().toString();
        this.cst = icms.getCstIcms() != null ? icms.getCstIcms() : icms.getCsosn();
        this.modalidadeBaseCalculo = icms.getModalidadeBcIcms() != null ? icms.getModalidadeBcIcms().toString() : null;
        this.modalidadeBaseCalculoST = icms.getModalidadeBcIcmsSt() != null ? icms.getModalidadeBcIcmsSt().toString() : null;
        this.aliquotaIcms = icms.getAliquotaIcms();
        this.aliquotaIcmsST = icms.getAliquotaIcmsSt();
        this.baseCalculoIcms = icms.getBaseCalculoIcms();
        this.baseCalculoIcmsST = icms.getValorBaseCalculoIcmsSt();
        this.reducaoBaseCalculoIcms = icms.getTaxaReducaoBcIcms();
        this.reducaoBaseCalculoIcmsST = icms.getPercentualReducaoBcIcmsSt();
        this.valorIcms = icms.getValorIcms();
        this.valorIcmsST = icms.getValorIcmsSt();
        this.mva = icms.getPercentualMvaIcmsSt();
        this.aliquotaCreditoIcms = icms.getAliquotaCreditoIcmsSn();
        this.valorCreditoIcms = icms.getValorCreditoIcmsSn();
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getCst() {
        return cst;
    }

    public void setCst(String cst) {
        this.cst = cst;
    }


    public String getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(String modalidadeBaseCalculo) {
        this.modalidadeBaseCalculo = modalidadeBaseCalculo;
    }

    public String getModalidadeBaseCalculoST() {
        return modalidadeBaseCalculoST;
    }

    public void setModalidadeBaseCalculoST(String modalidadeBaseCalculoST) {
        this.modalidadeBaseCalculoST = modalidadeBaseCalculoST;
    }

    public BigDecimal getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public void setAliquotaIcms(String aliquotaIcms) {
        this.aliquotaIcms = StringUtils.isEmpty(aliquotaIcms) ? BigDecimal.ZERO : new BigDecimal(aliquotaIcms);
    }

    public BigDecimal getAliquotaIcmsST() {
        return aliquotaIcmsST;
    }

    public void setAliquotaIcmsST(BigDecimal aliquotaIcmsST) {
        this.aliquotaIcmsST = aliquotaIcmsST;
    }

    public void setAliquotaIcmsST(String aliquotaIcmsST) {
        this.aliquotaIcmsST = StringUtils.isEmpty(aliquotaIcmsST) ? BigDecimal.ZERO : new BigDecimal(aliquotaIcmsST);
    }

    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }

    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }

    public void setBaseCalculoIcms(String baseCalculoIcms) {
        this.baseCalculoIcms = StringUtils.isEmpty(baseCalculoIcms) ? BigDecimal.ZERO : new BigDecimal(baseCalculoIcms);
    }

    public BigDecimal getBaseCalculoIcmsST() {
        return baseCalculoIcmsST;
    }

    public void setBaseCalculoIcmsST(BigDecimal baseCalculoIcmsST) {
        this.baseCalculoIcmsST = baseCalculoIcmsST;
    }

    public void setBaseCalculoIcmsST(String baseCalculoIcmsST) {
        this.baseCalculoIcmsST = StringUtils.isEmpty(baseCalculoIcmsST) ? BigDecimal.ZERO : new BigDecimal(baseCalculoIcmsST);
    }

    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public void setValorIcms(String valorIcms) {
        this.valorIcms = StringUtils.isEmpty(valorIcms) ? BigDecimal.ZERO : new BigDecimal(valorIcms);
    }

    public BigDecimal getValorIcmsST() {
        return valorIcmsST;
    }

    public void setValorIcmsST(BigDecimal valorIcmsST) {
        this.valorIcmsST = valorIcmsST;
    }

    public void setValorIcmsST(String valorIcmsST) {
        this.valorIcmsST = StringUtils.isEmpty(valorIcmsST) ? BigDecimal.ZERO : new BigDecimal(valorIcmsST);
    }

    public BigDecimal getReducaoBaseCalculoIcms() {
        return reducaoBaseCalculoIcms;
    }

    public void setReducaoBaseCalculoIcms(BigDecimal reducaoBaseCalculoIcms) {
        this.reducaoBaseCalculoIcms = reducaoBaseCalculoIcms;
    }

    public void setReducaoBaseCalculoIcms(String reducaoBaseCalculoIcms) {
        this.reducaoBaseCalculoIcms = StringUtils.isEmpty(reducaoBaseCalculoIcms) ? BigDecimal.ZERO : new BigDecimal(reducaoBaseCalculoIcms);
    }

    public BigDecimal getReducaoBaseCalculoIcmsST() {
        return reducaoBaseCalculoIcmsST;
    }

    public void setReducaoBaseCalculoIcmsST(BigDecimal reducaoBaseCalculoIcmsST) {
        this.reducaoBaseCalculoIcmsST = reducaoBaseCalculoIcmsST;
    }

    public void setReducaoBaseCalculoIcmsST(String reducaoBaseCalculoIcmsST) {
        this.reducaoBaseCalculoIcmsST = StringUtils.isEmpty(reducaoBaseCalculoIcmsST) ? BigDecimal.ZERO : new BigDecimal(reducaoBaseCalculoIcmsST);
    }

    public BigDecimal getMva() {
        return mva;
    }

    public void setMva(BigDecimal mva) {
        this.mva = mva;
    }

    public void setMva(String mva) {
        this.mva = StringUtils.isEmpty(mva) ? BigDecimal.ZERO : new BigDecimal(mva);
    }

    public BigDecimal getAliquotaCreditoIcms() {
        return aliquotaCreditoIcms;
    }

    public void setAliquotaCreditoIcms(BigDecimal aliquotaCreditoIcms) {
        this.aliquotaCreditoIcms = aliquotaCreditoIcms;
    }

    public void setAliquotaCreditoIcms(String aliquotaCreditoIcms) {
        this.aliquotaCreditoIcms = StringUtils.isEmpty(aliquotaCreditoIcms) ? BigDecimal.ZERO : new BigDecimal(aliquotaCreditoIcms);
    }

    public BigDecimal getValorCreditoIcms() {
        return valorCreditoIcms;
    }

    public void setValorCreditoIcms(BigDecimal valorCreditoIcms) {
        this.valorCreditoIcms = valorCreditoIcms;
    }

    public void setValorCreditoIcms(String valorCreditoIcms) {
        this.valorCreditoIcms = StringUtils.isEmpty(valorCreditoIcms) ? BigDecimal.ZERO : new BigDecimal(valorCreditoIcms);
    }


}
