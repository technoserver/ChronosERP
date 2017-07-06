
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "NFE_CUPOM_FISCAL_REFERENCIADO")
public class NfeCupomFiscalReferenciado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MODELO_DOCUMENTO_FISCAL")
    private String modeloDocumentoFiscal;
    @Column(name = "NUMERO_ORDEM_ECF")
    private Integer numeroOrdemEcf;
    @Column(name = "COO")
    private Integer coo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_EMISSAO_CUPOM")
    private Date dataEmissaoCupom;
    @Column(name = "NUMERO_CAIXA")
    private Integer numeroCaixa;
    @Column(name = "NUMERO_SERIE_ECF")
    private String numeroSerieEcf;
    @JoinColumn(name = "ID_NFE_CABECALHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private NfeCabecalho nfeCabecalho;

    public NfeCupomFiscalReferenciado() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModeloDocumentoFiscal() {
        return modeloDocumentoFiscal;
    }

    public void setModeloDocumentoFiscal(String modeloDocumentoFiscal) {
        this.modeloDocumentoFiscal = modeloDocumentoFiscal;
    }

    public Integer getNumeroOrdemEcf() {
        return numeroOrdemEcf;
    }

    public void setNumeroOrdemEcf(Integer numeroOrdemEcf) {
        this.numeroOrdemEcf = numeroOrdemEcf;
    }

    public Integer getCoo() {
        return coo;
    }

    public void setCoo(Integer coo) {
        this.coo = coo;
    }

    public Date getDataEmissaoCupom() {
        return dataEmissaoCupom;
    }

    public void setDataEmissaoCupom(Date dataEmissaoCupom) {
        this.dataEmissaoCupom = dataEmissaoCupom;
    }

    public Integer getNumeroCaixa() {
        return numeroCaixa;
    }

    public void setNumeroCaixa(Integer numeroCaixa) {
        this.numeroCaixa = numeroCaixa;
    }

    public String getNumeroSerieEcf() {
        return numeroSerieEcf;
    }

    public void setNumeroSerieEcf(String numeroSerieEcf) {
        this.numeroSerieEcf = numeroSerieEcf;
    }

    public NfeCabecalho getNfeCabecalho() {
        return nfeCabecalho;
    }

    public void setNfeCabecalho(NfeCabecalho nfeCabecalho) {
        this.nfeCabecalho = nfeCabecalho;
    }


}
