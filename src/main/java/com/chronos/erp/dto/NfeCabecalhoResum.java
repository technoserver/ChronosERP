package com.chronos.erp.dto;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

public class NfeCabecalhoResum {

    private Integer id;
    private String numero;
    private String chaveAcesso;
    private String destinarario;
    private Date dataEmissao;
    private BigDecimal total;
    private Integer status;

    public NfeCabecalhoResum(Integer id, String numero, String chaveAcesso, String digistoVerificadorChaveAcesso, String destinarario, Date dataEmissao, BigDecimal total, Integer status) {
        this.id = id;
        this.numero = numero;
        this.chaveAcesso = StringUtils.isEmpty(chaveAcesso) ? "" : chaveAcesso + digistoVerificadorChaveAcesso;
        this.destinarario = StringUtils.isEmpty(destinarario) ? "Cliente não identificado" : destinarario;
        this.dataEmissao = dataEmissao;
        this.total = total;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getChaveAcesso() {
        return chaveAcesso;
    }

    public String getDestinarario() {
        return destinarario;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Integer getStatus() {
        return status;
    }
}
