
package com.chronos.erp.modelo.entidades;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "NFE_DETALHE_IMPOSTO_ISSQN")
public class NfeDetalheImpostoIssqn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "BASE_CALCULO_ISSQN")
    private BigDecimal baseCalculoIssqn;
    @Column(name = "ALIQUOTA_ISSQN")
    private BigDecimal aliquotaIssqn;
    @Column(name = "VALOR_ISSQN")
    private BigDecimal valorIssqn;
    @Column(name = "MUNICIPIO_ISSQN")
    private Integer municipioIssqn;
    @Column(name = "ITEM_LISTA_SERVICOS")
    private Integer itemListaServicos;
    @Column(name = "VALOR_DEDUCAO")
    private BigDecimal valorDeducao;
    @Column(name = "VALOR_OUTRAS_RETENCOES")
    private BigDecimal valorOutrasRetencoes;
    @Column(name = "VALOR_DESCONTO_INCONDICIONADO")
    private BigDecimal valorDescontoIncondicionado;
    @Column(name = "VALOR_DESCONTO_CONDICIONADO")
    private BigDecimal valorDescontoCondicionado;
    @Column(name = "VALOR_RETENCAO_ISS")
    private BigDecimal valorRetencaoIss;
    @Column(name = "INDICADOR_EXIGIBILIDADE_ISS")
    private Integer indicadorExigibilidadeIss;
    @Column(name = "CODIGO_SERVICO")
    private String codigoServico;
    @Column(name = "MUNICIPIO_INCIDENCIA")
    private Integer municipioIncidencia;
    @Column(name = "PAIS_SEVICO_PRESTADO")
    private Integer paisSevicoPrestado;
    @Column(name = "NUMERO_PROCESSO")
    private String numeroProcesso;
    @Column(name = "INDICADOR_INCENTIVO_FISCAL")
    private Integer indicadorIncentivoFiscal;
    @JoinColumn(name = "ID_NFE_DETALHE", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private NfeDetalhe nfeDetalhe;

    public NfeDetalheImpostoIssqn() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * vBC - Valor da Base de Cálculo do  ISSQN
     *
     * @return
     */
    public BigDecimal getBaseCalculoIssqn() {
        return baseCalculoIssqn;
    }

    /**
     * vBC - Valor da Base de Cálculo do  ISSQN
     *
     * @param baseCalculoIssqn
     */
    public void setBaseCalculoIssqn(BigDecimal baseCalculoIssqn) {
        this.baseCalculoIssqn = baseCalculoIssqn;
    }

    /**
     * vAliq - Alíquota do ISSQN
     *
     * @return
     */
    public BigDecimal getAliquotaIssqn() {
        return aliquotaIssqn;
    }

    /**
     * vAliq - Alíquota do ISSQN
     *
     * @param aliquotaIssqn
     */
    public void setAliquotaIssqn(BigDecimal aliquotaIssqn) {
        this.aliquotaIssqn = aliquotaIssqn;
    }

    /**
     * vISSQN - Valor do ISSQN
     *
     * @return
     */
    public BigDecimal getValorIssqn() {
        return valorIssqn;
    }

    /**
     * vISSQN - Valor do ISSQN
     *
     * @param valorIssqn
     */
    public void setValorIssqn(BigDecimal valorIssqn) {
        this.valorIssqn = valorIssqn;
    }

    /**
     * cMunFG - Código do município de  ocorrência do fato gerador do  ISSQN
     *
     * @return
     */
    public Integer getMunicipioIssqn() {
        return municipioIssqn;
    }

    /**
     * cMunFG - Código do município de  ocorrência do fato gerador do  ISSQN
     *
     * @param municipioIssqn
     */
    public void setMunicipioIssqn(Integer municipioIssqn) {
        this.municipioIssqn = municipioIssqn;
    }

    /**
     * cListServ - Informar o Item da lista de  serviços da LC 116/03 em que  se classifica o serviço.
     *
     * @return
     */
    public Integer getItemListaServicos() {
        return itemListaServicos;
    }

    /**
     * cListServ - Informar o Item da lista de  serviços da LC 116/03 em que  se classifica o serviço.
     *
     * @param itemListaServicos
     */
    public void setItemListaServicos(Integer itemListaServicos) {
        this.itemListaServicos = itemListaServicos;
    }

    /**
     * vDeducao - Valor dedução para redução da Base   de Cálculo
     *
     * @return
     */
    public BigDecimal getValorDeducao() {
        return valorDeducao;
    }

    /**
     * vDeducao - Valor dedução para redução da Base   de Cálculo
     *
     * @param valorDeducao
     */
    public void setValorDeducao(BigDecimal valorDeducao) {
        this.valorDeducao = valorDeducao;
    }

    /**
     * vOutro - Valor outras retenções
     *
     * @return
     */
    public BigDecimal getValorOutrasRetencoes() {
        return valorOutrasRetencoes;
    }

    /**
     * vOutro - Valor outras retenções
     *
     * @param valorOutrasRetencoes
     */
    public void setValorOutrasRetencoes(BigDecimal valorOutrasRetencoes) {
        this.valorOutrasRetencoes = valorOutrasRetencoes;
    }

    /**
     * vDescIncondo - Valor desconto incondicionado
     *
     * @return
     */
    public BigDecimal getValorDescontoIncondicionado() {
        return valorDescontoIncondicionado;
    }

    /**
     * vDescIncondo - Valor desconto incondicionado
     *
     * @param valorDescontoIncondicionado
     */
    public void setValorDescontoIncondicionado(BigDecimal valorDescontoIncondicionado) {
        this.valorDescontoIncondicionado = valorDescontoIncondicionado;
    }

    /**
     * vDescCond - Valor desconto condicionado
     *
     * @return
     */
    public BigDecimal getValorDescontoCondicionado() {
        return valorDescontoCondicionado;
    }

    /**
     * vDescCond - Valor desconto condicionado
     *
     * @param valorDescontoCondicionado
     */
    public void setValorDescontoCondicionado(BigDecimal valorDescontoCondicionado) {
        this.valorDescontoCondicionado = valorDescontoCondicionado;
    }

    /**
     * vISSRet - Valor retenção ISS
     *
     * @return
     */
    public BigDecimal getValorRetencaoIss() {
        return valorRetencaoIss;
    }

    /**
     * vISSRet - Valor retenção ISS
     *
     * @param valorRetencaoIss
     */
    public void setValorRetencaoIss(BigDecimal valorRetencaoIss) {
        this.valorRetencaoIss = valorRetencaoIss;
    }

    /**
     * indISS - 1=Exigível, 2=Não incidência; 3=Isenção; 4=Exportação;   5=Imunidade; 6=Exigibilidade Suspensa por Decisão   Judicial; 7=Exigibilidade Suspensa por Processo   Administrativo
     *
     * @return
     */
    public Integer getIndicadorExigibilidadeIss() {
        return indicadorExigibilidadeIss;
    }

    /**
     * indISS - 1=Exigível, 2=Não incidência; 3=Isenção; 4=Exportação;   5=Imunidade; 6=Exigibilidade Suspensa por Decisão   Judicial; 7=Exigibilidade Suspensa por Processo   Administrativo
     *
     * @param indicadorExigibilidadeIss
     */
    public void setIndicadorExigibilidadeIss(Integer indicadorExigibilidadeIss) {
        this.indicadorExigibilidadeIss = indicadorExigibilidadeIss;
    }

    /**
     * cServico - Código do serviço prestado dentro do   município
     *
     * @return
     */
    public String getCodigoServico() {
        return codigoServico;
    }

    /**
     * cServico - Código do serviço prestado dentro do   município
     *
     * @param codigoServico
     */
    public void setCodigoServico(String codigoServico) {
        this.codigoServico = codigoServico;
    }

    /**
     * Código do Município de incidência do   imposto
     *
     * @return
     */
    public Integer getMunicipioIncidencia() {
        return municipioIncidencia;
    }

    /**
     * Código do Município de incidência do   imposto
     *
     * @param municipioIncidencia
     */
    public void setMunicipioIncidencia(Integer municipioIncidencia) {
        this.municipioIncidencia = municipioIncidencia;
    }

    /**
     * Código do País onde o serviço foi   prestado
     *
     * @return
     */
    public Integer getPaisSevicoPrestado() {
        return paisSevicoPrestado;
    }

    /**
     * Código do País onde o serviço foi   prestado
     *
     * @param paisSevicoPrestado
     */
    public void setPaisSevicoPrestado(Integer paisSevicoPrestado) {
        this.paisSevicoPrestado = paisSevicoPrestado;
    }

    /**
     * nProcesso - Número do processo judicial ou   administrativo de suspensão da   exigibilidade
     *
     * @return
     */
    public String getNumeroProcesso() {
        return numeroProcesso;
    }

    /**
     * nProcesso - Número do processo judicial ou   administrativo de suspensão da   exigibilidade
     *
     * @param numeroProcesso
     */
    public void setNumeroProcesso(String numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    /**
     * indIncentivo - Indicador de incentivo Fiscal - 1=Sim; 2=Não
     *
     * @return
     */
    public Integer getIndicadorIncentivoFiscal() {
        return indicadorIncentivoFiscal;
    }

    /**
     * indIncentivo - Indicador de incentivo Fiscal - 1=Sim; 2=Não
     *
     * @param indicadorIncentivoFiscal
     */
    public void setIndicadorIncentivoFiscal(Integer indicadorIncentivoFiscal) {
        this.indicadorIncentivoFiscal = indicadorIncentivoFiscal;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }


}
