
package com.chronos.modelo.entidades;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "PDV_CONFIGURACAO")
public class PdvConfiguracao implements Serializable {

    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MENSAGEM_CUPOM")
    private String mensagemCupom;
    @Column(name = "TITULO_TELA_CAIXA")
    private String tituloTelaCaixa;
    @Column(name = "CAMINHO_IMAGENS_PRODUTOS")
    private String caminhoImagensProdutos;
    @Column(name = "CAMINHO_IMAGENS_MARKETING")
    private String caminhoImagensMarketing;
    @Column(name = "CAMINHO_IMAGENS_LAYOUT")
    private String caminhoImagensLayout;
    @Column(name = "COR_JANELAS_INTERNAS")
    private String corJanelasInternas;
    @Column(name = "MARKETING_ATIVO")
    private String marketingAtivo;
    @Column(name = "CFOP")
    private Integer cfop;
    @Column(name = "DECIMAIS_QUANTIDADE")
    private Integer decimaisQuantidade;
    @Column(name = "DECIMAIS_VALOR")
    private Integer decimaisValor;
    @Column(name = "QUANTIDADE_MAXIMA_PARCELA")
    private Integer quantidadeMaximaParcela;
    @Column(name = "IMPRIME_PARCELA")
    private String imprimeParcela;
    @Column(name = "CODIGO_CSC")
    private String codigoCsc;
    @Column(name = "ID_TOKEN_CSC")
    private String idTokenCsc;
    @Column(name = "TIPO_EMISSAO")
    private Integer tipoEmissao;
    @Column(name = "FORMATO_IMPRESSAO_DANFE")
    private Integer formatoImpressaoDanfe;
    @Column(name = "PROCESSO_EMISSAO")
    private Integer processoEmissao;
    @Column(name = "VERSAO_PROCESSO_EMISSAO")
    private String versaoProcessoEmissao;
    @Column(name = "CAMINHO_LOGOMARCA")
    private String caminhoLogomarca;
    @Column(name = "SALVAR_XML")
    private String salvarXml;
    @Column(name = "CAMINHO_SALVAR_XML")
    private String caminhoSalvarXml;
    @Column(name = "CAMINHO_SCHEMAS")
    private String caminhoSchemas;
    @Column(name = "CAMINHO_ARQUIVO_DANFE")
    private String caminhoArquivoDanfe;
    @Column(name = "CAMINHO_SALVAR_PDF")
    private String caminhoSalvarPdf;
    @Column(name = "WEBSERVICE_UF")
    private String webserviceUf;
    @Column(name = "WEBSERVICE_AMBIENTE")
    private Integer webserviceAmbiente;
    @Column(name = "WEBSERVICE_PROXY_HOST")
    private String webserviceProxyHost;
    @Column(name = "WEBSERVICE_PROXY_PORTA")
    private Integer webserviceProxyPorta;
    @Column(name = "WEBSERVICE_PROXY_USUARIO")
    private String webserviceProxyUsuario;
    @Column(name = "WEBSERVICE_PROXY_SENHA")
    private String webserviceProxySenha;
    @Column(name = "WEBSERVICE_VISUALIZAR")
    private String webserviceVisualizar;
    @Column(name = "observacao_padrao")
    private String observacaoPadrao;
    @JoinColumn(name = "ID_PDV_RESOLUCAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvResolucao pdvResolucao;
    @JoinColumn(name = "ID_PDV_CAIXA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PdvCaixa pdvCaixa;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa empresa;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pdvConfiguracao", orphanRemoval = true, cascade = CascadeType.ALL)
    private PdvConfiguracaoBalanca pdvConfiguracaoBalanca;



    public PdvConfiguracao() {
        setCaminhoArquivoDanfe("");
        setCaminhoSalvarPdf("S");
        setCaminhoSchemas("");
        setFormatoImpressaoDanfe(1);
        setProcessoEmissao(0);
        setSalvarXml("S");
    }

    public PdvConfiguracao(Integer id, String mensagemCupom) {
        this.id = id;
        this.mensagemCupom = mensagemCupom;
    }

    public PdvConfiguracao(Integer id, PdvCaixa pdvCaixa) {
        this.id = id;
        this.pdvCaixa = pdvCaixa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensagemCupom() {
        return mensagemCupom;
    }

    public void setMensagemCupom(String mensagemCupom) {
        this.mensagemCupom = mensagemCupom;
    }

    public String getTituloTelaCaixa() {
        return tituloTelaCaixa;
    }

    public void setTituloTelaCaixa(String tituloTelaCaixa) {
        this.tituloTelaCaixa = tituloTelaCaixa;
    }

    public String getCaminhoImagensProdutos() {
        return caminhoImagensProdutos;
    }

    public void setCaminhoImagensProdutos(String caminhoImagensProdutos) {
        this.caminhoImagensProdutos = caminhoImagensProdutos;
    }

    public String getCaminhoImagensMarketing() {
        return caminhoImagensMarketing;
    }

    public void setCaminhoImagensMarketing(String caminhoImagensMarketing) {
        this.caminhoImagensMarketing = caminhoImagensMarketing;
    }

    public String getCaminhoImagensLayout() {
        return caminhoImagensLayout;
    }

    public void setCaminhoImagensLayout(String caminhoImagensLayout) {
        this.caminhoImagensLayout = caminhoImagensLayout;
    }

    public String getCorJanelasInternas() {
        return corJanelasInternas;
    }

    public void setCorJanelasInternas(String corJanelasInternas) {
        this.corJanelasInternas = corJanelasInternas;
    }

    public String getMarketingAtivo() {
        return marketingAtivo;
    }

    public void setMarketingAtivo(String marketingAtivo) {
        this.marketingAtivo = marketingAtivo;
    }

    public Integer getCfop() {
        return cfop;
    }

    public void setCfop(Integer cfop) {
        this.cfop = cfop;
    }

    public Integer getDecimaisQuantidade() {
        return decimaisQuantidade;
    }

    public void setDecimaisQuantidade(Integer decimaisQuantidade) {
        this.decimaisQuantidade = decimaisQuantidade;
    }

    public Integer getDecimaisValor() {
        return decimaisValor;
    }

    public void setDecimaisValor(Integer decimaisValor) {
        this.decimaisValor = decimaisValor;
    }

    public Integer getQuantidadeMaximaParcela() {
        return quantidadeMaximaParcela;
    }

    public void setQuantidadeMaximaParcela(Integer quantidadeMaximaParcela) {
        this.quantidadeMaximaParcela = quantidadeMaximaParcela;
    }

    public String getImprimeParcela() {
        return imprimeParcela;
    }

    public void setImprimeParcela(String imprimeParcela) {
        this.imprimeParcela = imprimeParcela;
    }

    public String getCodigoCsc() {
        return codigoCsc;
    }

    public void setCodigoCsc(String codigoCsc) {
        this.codigoCsc = codigoCsc;
    }

    public String getIdTokenCsc() {
        return idTokenCsc;
    }

    public void setIdTokenCsc(String idTokenCsc) {
        this.idTokenCsc = idTokenCsc;
    }


    public Integer getTipoEmissao() {
        return tipoEmissao;
    }

    public void setTipoEmissao(Integer tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

    public Integer getFormatoImpressaoDanfe() {
        return formatoImpressaoDanfe;
    }

    public void setFormatoImpressaoDanfe(Integer formatoImpressaoDanfe) {
        this.formatoImpressaoDanfe = formatoImpressaoDanfe;
    }

    public Integer getProcessoEmissao() {
        return processoEmissao;
    }

    public void setProcessoEmissao(Integer processoEmissao) {
        this.processoEmissao = processoEmissao;
    }

    public String getVersaoProcessoEmissao() {
        return versaoProcessoEmissao;
    }

    public void setVersaoProcessoEmissao(String versaoProcessoEmissao) {
        this.versaoProcessoEmissao = versaoProcessoEmissao;
    }

    public String getCaminhoLogomarca() {
        return caminhoLogomarca;
    }

    public void setCaminhoLogomarca(String caminhoLogomarca) {
        this.caminhoLogomarca = caminhoLogomarca;
    }

    public String getSalvarXml() {
        return salvarXml;
    }

    public void setSalvarXml(String salvarXml) {
        this.salvarXml = salvarXml;
    }

    public String getCaminhoSalvarXml() {
        return caminhoSalvarXml;
    }

    public void setCaminhoSalvarXml(String caminhoSalvarXml) {
        this.caminhoSalvarXml = caminhoSalvarXml;
    }

    public String getCaminhoSchemas() {
        return caminhoSchemas;
    }

    public void setCaminhoSchemas(String caminhoSchemas) {
        this.caminhoSchemas = caminhoSchemas;
    }

    public String getCaminhoArquivoDanfe() {
        return caminhoArquivoDanfe;
    }

    public void setCaminhoArquivoDanfe(String caminhoArquivoDanfe) {
        this.caminhoArquivoDanfe = caminhoArquivoDanfe;
    }

    public String getCaminhoSalvarPdf() {
        return caminhoSalvarPdf;
    }

    public void setCaminhoSalvarPdf(String caminhoSalvarPdf) {
        this.caminhoSalvarPdf = caminhoSalvarPdf;
    }

    public String getWebserviceUf() {
        return webserviceUf;
    }

    public void setWebserviceUf(String webserviceUf) {
        this.webserviceUf = webserviceUf;
    }

    public Integer getWebserviceAmbiente() {
        return webserviceAmbiente;
    }

    public void setWebserviceAmbiente(Integer webserviceAmbiente) {
        this.webserviceAmbiente = webserviceAmbiente;
    }

    public String getWebserviceProxyHost() {
        return webserviceProxyHost;
    }

    public void setWebserviceProxyHost(String webserviceProxyHost) {
        this.webserviceProxyHost = webserviceProxyHost;
    }

    public Integer getWebserviceProxyPorta() {
        return webserviceProxyPorta;
    }

    public void setWebserviceProxyPorta(Integer webserviceProxyPorta) {
        this.webserviceProxyPorta = webserviceProxyPorta;
    }

    public String getWebserviceProxyUsuario() {
        return webserviceProxyUsuario;
    }

    public void setWebserviceProxyUsuario(String webserviceProxyUsuario) {
        this.webserviceProxyUsuario = webserviceProxyUsuario;
    }

    public String getWebserviceProxySenha() {
        return webserviceProxySenha;
    }

    public void setWebserviceProxySenha(String webserviceProxySenha) {
        this.webserviceProxySenha = webserviceProxySenha;
    }

    public String getWebserviceVisualizar() {
        return webserviceVisualizar;
    }

    public void setWebserviceVisualizar(String webserviceVisualizar) {
        this.webserviceVisualizar = webserviceVisualizar;
    }



    public PdvResolucao getPdvResolucao() {
        return pdvResolucao;
    }

    public void setPdvResolucao(PdvResolucao pdvResolucao) {
        this.pdvResolucao = pdvResolucao;
    }

    public PdvCaixa getPdvCaixa() {
        return pdvCaixa;
    }

    public void setPdvCaixa(PdvCaixa pdvCaixa) {
        this.pdvCaixa = pdvCaixa;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getObservacaoPadrao() {
        return observacaoPadrao;
    }

    public void setObservacaoPadrao(String observacaoPadrao) {
        this.observacaoPadrao = observacaoPadrao;
    }

    public PdvConfiguracaoBalanca getPdvConfiguracaoBalanca() {
        return pdvConfiguracaoBalanca;
    }

    public void setPdvConfiguracaoBalanca(PdvConfiguracaoBalanca pdvConfiguracaoBalanca) {
        this.pdvConfiguracaoBalanca = pdvConfiguracaoBalanca;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final PdvConfiguracao other = (PdvConfiguracao) obj;
        return Objects.equals(this.id, other.id);
    }

}
