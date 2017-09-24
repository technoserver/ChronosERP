
package com.chronos.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;


@Entity
@Table(name = "NFE_DETALHE_IMPOSTO_ICMS")
public class NfeDetalheImpostoIcms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ORIGEM_MERCADORIA")
    private Integer origemMercadoria;
    @Column(name = "CST_ICMS")
    private String cstIcms;
    @Column(name = "CSOSN")
    private String csosn;
    @Column(name = "MODALIDADE_BC_ICMS")
    private Integer modalidadeBcIcms;
    @Column(name = "TAXA_REDUCAO_BC_ICMS")
    private BigDecimal taxaReducaoBcIcms;
    @Column(name = "BASE_CALCULO_ICMS")
    private BigDecimal baseCalculoIcms;
    @Column(name = "ALIQUOTA_ICMS")
    private BigDecimal aliquotaIcms;
    @Column(name = "VALOR_ICMS")
    private BigDecimal valorIcms;
    @Column(name = "VALOR_ICMS_OPERACAO")
    private BigDecimal valorIcmsOperacao;
    @Column(name = "PERCENTUAL_DIFERIMENTO")
    private BigDecimal percentualDiferimento;
    @Column(name = "VALOR_ICMS_DIFERIDO")
    private BigDecimal valorIcmsDiferido;
    @Column(name = "MOTIVO_DESONERACAO_ICMS")
    private Integer motivoDesoneracaoIcms;
    @Column(name = "VALOR_ICMS_DESONERADO")
    private BigDecimal valorIcmsDesonerado;
    @Column(name = "MODALIDADE_BC_ICMS_ST")
    private Integer modalidadeBcIcmsSt;
    @Column(name = "PERCENTUAL_MVA_ICMS_ST")
    private BigDecimal percentualMvaIcmsSt;
    @Column(name = "PERCENTUAL_REDUCAO_BC_ICMS_ST")
    private BigDecimal percentualReducaoBcIcmsSt;
    @Column(name = "VALOR_BASE_CALCULO_ICMS_ST")
    private BigDecimal valorBaseCalculoIcmsSt;
    @Column(name = "ALIQUOTA_ICMS_ST")
    private BigDecimal aliquotaIcmsSt;
    @Column(name = "VALOR_ICMS_ST")
    private BigDecimal valorIcmsSt;
    @Column(name = "VALOR_BC_ICMS_ST_RETIDO")
    private BigDecimal valorBcIcmsStRetido;
    @Column(name = "VALOR_ICMS_ST_RETIDO")
    private BigDecimal valorIcmsStRetido;
    @Column(name = "VALOR_BC_ICMS_ST_DESTINO")
    private BigDecimal valorBcIcmsStDestino;
    @Column(name = "VALOR_ICMS_ST_DESTINO")
    private BigDecimal valorIcmsStDestino;
    @Column(name = "ALIQUOTA_CREDITO_ICMS_SN")
    private BigDecimal aliquotaCreditoIcmsSn;
    @Column(name = "VALOR_CREDITO_ICMS_SN")
    private BigDecimal valorCreditoIcmsSn;
    @Column(name = "PERCENTUAL_BC_OPERACAO_PROPRIA")
    private BigDecimal percentualBcOperacaoPropria;
    @Column(name = "UF_ST")
    private String ufSt;
    @Column(name = "VALOR_BC_FCP")
    private BigDecimal valorBcFcp;
    @Column(name = "PERCENTUAL_FCP")
    private BigDecimal percentualFcp;
    @Column(name = "VALOR_FCP")
    private BigDecimal valorFcp;
    @Column(name = "VALOR_BC_FCP_ST")
    private BigDecimal valorBcFcpSt;
    @Column(name = "PERCENTUAL_BC_FCP_ST")
    private BigDecimal percentualBcFcpSt;
    @Column(name = "VALOR_FCP_ST")
    private BigDecimal valorFcpSt;
    @Column(name = "FCP_CONSUMIDOR_FINAL")
    private BigDecimal fpcConsumidorFinal;
    @Column(name = "VALOR_BC_FCP_RETIDO")
    private BigDecimal valorBcFcpRetido;
    @Column(name = "PERCENTUAL_BC_FCP_RETIDO")
    private BigDecimal percentualBcFcpRetido;
    @Column(name = "VALOR_FCP_RETIDO")
    private BigDecimal valorFcpRetido;
    @Column(name = "VALOR_BC_FCP_UF_DESTINO")
    private BigDecimal valorBcFcpUfDestino;
    @Column(name = "VALOR_BC_ICMS_UF_DESTINO")
    private BigDecimal valorBsIcmsUfDestino;
    @Column(name = "PERCENTUAL_FCP_UF_DESTINO")
    private BigDecimal percentualFcpUfDestino;
    @Column(name = "ALIQUOTA_INTERNA_UF_DESTINO")
    private BigDecimal aliquotaInternaUfDestino;
    @Column(name = "ALIQUOTA_INTERESTADUAL_UFS")
    private BigDecimal aliquotaInterestadualUfs;
    @Column(name = "PERCENTUAL_PROVISORIO_PARTILHA")
    private BigDecimal percentualProvisorioPartilha;
    @Column(name = "VALOR_ICMS_FCP_UF_DESTINO")
    private BigDecimal valorIcmsFcpUfDestino;
    @Column(name = "VALOR_ICMS_INTER_UF_DESTINO")
    private BigDecimal valorIcmsInterUfDestino;
    @Column(name = "VALOR_ICMS_INTER_UF_REMETENTE")
    private BigDecimal valorIcmsInterUfRemetente;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetalheImpostoIcms() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrigemMercadoria() {
        return origemMercadoria;
    }

    public void setOrigemMercadoria(Integer origemMercadoria) {
        this.origemMercadoria = origemMercadoria;
    }

    public String getCstIcms() {
        return cstIcms;
    }

    public void setCstIcms(String cstIcms) {
        this.cstIcms = cstIcms;
    }

    public String getCsosn() {
        return csosn;
    }

    public void setCsosn(String csosn) {
        this.csosn = csosn;
    }

    public Integer getModalidadeBcIcms() {
        return modalidadeBcIcms;
    }

    public void setModalidadeBcIcms(Integer modalidadeBcIcms) {
        this.modalidadeBcIcms = modalidadeBcIcms;
    }

    public BigDecimal getTaxaReducaoBcIcms() {
        return taxaReducaoBcIcms;
    }

    public void setTaxaReducaoBcIcms(BigDecimal taxaReducaoBcIcms) {
        this.taxaReducaoBcIcms = taxaReducaoBcIcms;
    }

    public BigDecimal getBaseCalculoIcms() {
        return Optional.ofNullable(baseCalculoIcms).orElse(BigDecimal.ZERO);
    }

    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }

    public BigDecimal getAliquotaIcms() {
        return aliquotaIcms;
    }

    public void setAliquotaIcms(BigDecimal aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    public BigDecimal getValorIcms() {
        return Optional.ofNullable(valorIcms).orElse(BigDecimal.ZERO);
    }

    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    public BigDecimal getValorIcmsOperacao() {
        return valorIcmsOperacao;
    }

    public void setValorIcmsOperacao(BigDecimal valorIcmsOperacao) {
        this.valorIcmsOperacao = valorIcmsOperacao;
    }

    public BigDecimal getPercentualDiferimento() {
        return percentualDiferimento;
    }

    public void setPercentualDiferimento(BigDecimal percentualDiferimento) {
        this.percentualDiferimento = percentualDiferimento;
    }

    public BigDecimal getValorIcmsDiferido() {
        return valorIcmsDiferido;
    }

    public void setValorIcmsDiferido(BigDecimal valorIcmsDiferido) {
        this.valorIcmsDiferido = valorIcmsDiferido;
    }

    public Integer getMotivoDesoneracaoIcms() {
        return motivoDesoneracaoIcms;
    }

    public void setMotivoDesoneracaoIcms(Integer motivoDesoneracaoIcms) {
        this.motivoDesoneracaoIcms = motivoDesoneracaoIcms;
    }

    public BigDecimal getValorIcmsDesonerado() {
        return valorIcmsDesonerado;
    }

    public void setValorIcmsDesonerado(BigDecimal valorIcmsDesonerado) {
        this.valorIcmsDesonerado = valorIcmsDesonerado;
    }

    public Integer getModalidadeBcIcmsSt() {
        return modalidadeBcIcmsSt;
    }

    public void setModalidadeBcIcmsSt(Integer modalidadeBcIcmsSt) {
        this.modalidadeBcIcmsSt = modalidadeBcIcmsSt;
    }

    public BigDecimal getPercentualMvaIcmsSt() {
        return percentualMvaIcmsSt;
    }

    public void setPercentualMvaIcmsSt(BigDecimal percentualMvaIcmsSt) {
        this.percentualMvaIcmsSt = percentualMvaIcmsSt;
    }

    public BigDecimal getPercentualReducaoBcIcmsSt() {
        return percentualReducaoBcIcmsSt;
    }

    public void setPercentualReducaoBcIcmsSt(BigDecimal percentualReducaoBcIcmsSt) {
        this.percentualReducaoBcIcmsSt = percentualReducaoBcIcmsSt;
    }

    public BigDecimal getValorBaseCalculoIcmsSt() {
        return Optional.ofNullable(valorBaseCalculoIcmsSt).orElse(BigDecimal.ZERO);
    }

    public void setValorBaseCalculoIcmsSt(BigDecimal valorBaseCalculoIcmsSt) {
        this.valorBaseCalculoIcmsSt = valorBaseCalculoIcmsSt;
    }

    public BigDecimal getAliquotaIcmsSt() {
        return aliquotaIcmsSt;
    }

    public void setAliquotaIcmsSt(BigDecimal aliquotaIcmsSt) {
        this.aliquotaIcmsSt = aliquotaIcmsSt;
    }

    public BigDecimal getValorIcmsSt() {
        return Optional.ofNullable(valorIcmsSt).orElse(BigDecimal.ZERO);
    }

    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }

    public BigDecimal getValorBcIcmsStRetido() {
        return valorBcIcmsStRetido;
    }

    public void setValorBcIcmsStRetido(BigDecimal valorBcIcmsStRetido) {
        this.valorBcIcmsStRetido = valorBcIcmsStRetido;
    }

    public BigDecimal getValorIcmsStRetido() {
        return valorIcmsStRetido;
    }

    public void setValorIcmsStRetido(BigDecimal valorIcmsStRetido) {
        this.valorIcmsStRetido = valorIcmsStRetido;
    }

    public BigDecimal getValorBcIcmsStDestino() {
        return valorBcIcmsStDestino;
    }

    public void setValorBcIcmsStDestino(BigDecimal valorBcIcmsStDestino) {
        this.valorBcIcmsStDestino = valorBcIcmsStDestino;
    }

    public BigDecimal getValorIcmsStDestino() {
        return valorIcmsStDestino;
    }

    public void setValorIcmsStDestino(BigDecimal valorIcmsStDestino) {
        this.valorIcmsStDestino = valorIcmsStDestino;
    }

    public BigDecimal getAliquotaCreditoIcmsSn() {
        return aliquotaCreditoIcmsSn;
    }

    public void setAliquotaCreditoIcmsSn(BigDecimal aliquotaCreditoIcmsSn) {
        this.aliquotaCreditoIcmsSn = aliquotaCreditoIcmsSn;
    }

    public BigDecimal getValorCreditoIcmsSn() {
        return valorCreditoIcmsSn;
    }

    public void setValorCreditoIcmsSn(BigDecimal valorCreditoIcmsSn) {
        this.valorCreditoIcmsSn = valorCreditoIcmsSn;
    }

    public BigDecimal getPercentualBcOperacaoPropria() {
        return percentualBcOperacaoPropria;
    }

    public void setPercentualBcOperacaoPropria(BigDecimal percentualBcOperacaoPropria) {
        this.percentualBcOperacaoPropria = percentualBcOperacaoPropria;
    }

    public String getUfSt() {
        return ufSt;
    }

    public void setUfSt(String ufSt) {
        this.ufSt = ufSt;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public BigDecimal getValorBcFcp() {
        return valorBcFcp;
    }

    public void setValorBcFcp(BigDecimal valorBcFcp) {
        this.valorBcFcp = valorBcFcp;
    }

    public BigDecimal getPercentualFcp() {
        return percentualFcp;
    }

    public void setPercentualFcp(BigDecimal percentualFcp) {
        this.percentualFcp = percentualFcp;
    }

    public BigDecimal getValorFcp() {
        return valorFcp;
    }

    public void setValorFcp(BigDecimal valorFcp) {
        this.valorFcp = valorFcp;
    }

    public BigDecimal getValorBcFcpSt() {
        return valorBcFcpSt;
    }

    public void setValorBcFcpSt(BigDecimal valorBcFcpSt) {
        this.valorBcFcpSt = valorBcFcpSt;
    }

    public BigDecimal getPercentualBcFcpSt() {
        return percentualBcFcpSt;
    }

    public void setPercentualBcFcpSt(BigDecimal percentualBcFcpSt) {
        this.percentualBcFcpSt = percentualBcFcpSt;
    }

    public BigDecimal getValorFcpSt() {
        return valorFcpSt;
    }

    public void setValorFcpSt(BigDecimal valorFcpSt) {
        this.valorFcpSt = valorFcpSt;
    }

    public BigDecimal getFcpConsumidorFinal() {
        return fpcConsumidorFinal;
    }

    public void setFcpConsumidorFinal(BigDecimal fpcConsumidorFinal) {
        this.fpcConsumidorFinal = fpcConsumidorFinal;
    }

    public BigDecimal getValorBcFcpRetido() {
        return valorBcFcpRetido;
    }

    public void setValorBcFcpRetido(BigDecimal valorBcFcpRetido) {
        this.valorBcFcpRetido = valorBcFcpRetido;
    }

    public BigDecimal getPercentualBcFcpRetido() {
        return percentualBcFcpRetido;
    }

    public void setPercentualBcFcpRetido(BigDecimal percentualBcFcpRetido) {
        this.percentualBcFcpRetido = percentualBcFcpRetido;
    }

    public BigDecimal getValorFcpRetido() {
        return valorFcpRetido;
    }

    public void setValorFcpRetido(BigDecimal valorFcpRetido) {
        this.valorFcpRetido = valorFcpRetido;
    }

    public BigDecimal getValorBcFcpUfDestino() {
        return valorBcFcpUfDestino;
    }

    public void setValorBcFcpUfDestino(BigDecimal valorBcFcpUfDestino) {
        this.valorBcFcpUfDestino = valorBcFcpUfDestino;
    }

    public BigDecimal getValorBsIcmsUfDestino() {
        return valorBsIcmsUfDestino;
    }

    public void setValorBsIcmsUfDestino(BigDecimal valorBsIcmsUfDestino) {
        this.valorBsIcmsUfDestino = valorBsIcmsUfDestino;
    }

    public BigDecimal getPercentualFcpUfDestino() {
        return percentualFcpUfDestino;
    }

    public void setPercentualFcpUfDestino(BigDecimal percentualFcpUfDestino) {
        this.percentualFcpUfDestino = percentualFcpUfDestino;
    }

    public BigDecimal getAliquotaInternaUfDestino() {
        return aliquotaInternaUfDestino;
    }

    public void setAliquotaInternaUfDestino(BigDecimal aliquotaInternaUfDestino) {
        this.aliquotaInternaUfDestino = aliquotaInternaUfDestino;
    }

    public BigDecimal getAliquotaInterestadualUfs() {
        return aliquotaInterestadualUfs;
    }

    public void setAliquotaInterestadualUfs(BigDecimal aliquotaInterestadualUfs) {
        this.aliquotaInterestadualUfs = aliquotaInterestadualUfs;
    }

    public BigDecimal getPercentualProvisorioPartilha() {
        return percentualProvisorioPartilha;
    }

    public void setPercentualProvisorioPartilha(BigDecimal percentualProvisorioPartilha) {
        this.percentualProvisorioPartilha = percentualProvisorioPartilha;
    }

    public BigDecimal getValorIcmsFcpUfDestino() {
        return valorIcmsFcpUfDestino;
    }

    public void setValorIcmsFcpUfDestino(BigDecimal valorIcmsFcpUfDestino) {
        this.valorIcmsFcpUfDestino = valorIcmsFcpUfDestino;
    }

    public BigDecimal getValorIcmsInterUfDestino() {
        return valorIcmsInterUfDestino;
    }

    public void setValorIcmsInterUfDestino(BigDecimal valorIcmsInterUfDestino) {
        this.valorIcmsInterUfDestino = valorIcmsInterUfDestino;
    }

    public BigDecimal getValorIcmsInterUfRemetente() {
        return valorIcmsInterUfRemetente;
    }

    public void setValorIcmsInterUfRemetente(BigDecimal valorIcmsInterUfRemetente) {
        this.valorIcmsInterUfRemetente = valorIcmsInterUfRemetente;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NfeDetalheImpostoIcms)) return false;
        NfeDetalheImpostoIcms that = (NfeDetalheImpostoIcms) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
