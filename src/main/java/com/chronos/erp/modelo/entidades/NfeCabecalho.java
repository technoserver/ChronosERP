package com.chronos.erp.modelo.entidades;

import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Entity
@Table(name = "NFE_CABECALHO")
@DynamicUpdate
public class NfeCabecalho implements Serializable {


    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "UF_EMITENTE")
    private Integer ufEmitente;
    @Column(name = "CODIGO_NUMERICO")
    @Size(max = 8)
    private String codigoNumerico;
    @Column(name = "NATUREZA_OPERACAO")
    private String naturezaOperacao;
    @Column(name = "INDICADOR_FORMA_PAGAMENTO")
    private Integer indicadorFormaPagamento;
    @Column(name = "CODIGO_MODELO")
    @Size(max = 2)
    private String codigoModelo;
    @Column(name = "SERIE")
    private String serie;
    @Column(name = "NUMERO")
    private String numero;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_HORA_EMISSAO")
    private Date dataHoraEmissao;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_HORA_ENTRADA_SAIDA")
    private Date dataHoraEntradaSaida;
    @Column(name = "TIPO_OPERACAO")
    private Integer tipoOperacao;
    @Column(name = "LOCAL_DESTINO")
    private Integer localDestino;
    @Column(name = "CODIGO_MUNICIPIO")
    private Integer codigoMunicipio;
    @Column(name = "FORMATO_IMPRESSAO_DANFE")
    private Integer formatoImpressaoDanfe;
    @Column(name = "TIPO_EMISSAO")
    private Integer tipoEmissao;
    @Column(name = "CHAVE_ACESSO")
    private String chaveAcesso;
    @Column(name = "DIGITO_CHAVE_ACESSO")
    private String digitoChaveAcesso;
    @Column(name = "AMBIENTE")
    private Integer ambiente;
    @Column(name = "FINALIDADE_EMISSAO")
    private Integer finalidadeEmissao;
    @Column(name = "CONSUMIDOR_OPERACAO")
    private Integer consumidorOperacao;
    @Column(name = "PROCESSO_EMISSAO")
    private Integer processoEmissao;
    @Column(name = "VERSAO_PROCESSO_EMISSAO")
    private String versaoProcessoEmissao;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ENTRADA_CONTINGENCIA")
    private Date dataEntradaContingencia;
    @Column(name = "JUSTIFICATIVA_CONTINGENCIA")
    private String justificativaContingencia;
    @Column(name = "BASE_CALCULO_ICMS")
    private BigDecimal baseCalculoIcms;
    @Column(name = "VALOR_ICMS")
    private BigDecimal valorIcms;
    @Column(name = "VALOR_ICMS_DESONERADO")
    private BigDecimal valorIcmsDesonerado;
    @Column(name = "BASE_CALCULO_ICMS_ST")
    private BigDecimal baseCalculoIcmsSt;
    @Column(name = "VALOR_ICMS_ST")
    private BigDecimal valorIcmsSt;
    @Column(name = "VALOR_TOTAL_PRODUTOS")
    private BigDecimal valorTotalProdutos;
    @Column(name = "VALOR_FRETE")
    private BigDecimal valorFrete;
    @Column(name = "VALOR_SEGURO")
    private BigDecimal valorSeguro;
    @Column(name = "VALOR_DESCONTO")
    private BigDecimal valorDesconto;
    @Column(name = "VALOR_IMPOSTO_IMPORTACAO")
    private BigDecimal valorImpostoImportacao;
    @Column(name = "VALOR_IPI")
    private BigDecimal valorIpi;
    @Column(name = "VALOR_PIS")
    private BigDecimal valorPis;
    @Column(name = "VALOR_COFINS")
    private BigDecimal valorCofins;
    @Column(name = "VALOR_DESPESAS_ACESSORIAS")
    private BigDecimal valorDespesasAcessorias;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "VALOR_SERVICOS")
    private BigDecimal valorServicos;
    @Column(name = "VALOR_TOTAL_TRIBUTOS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributos;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_FEDERAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosFederais;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_ESTADUAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosEstaduais;
    @Column(name = "VALOR_TOTAL_TRIBUTOS_MUNICIPAIS")
    @ColumnDefault(value = "0.00")
    private BigDecimal valorTotalTributosMunicipais;
    @Column(name = "BASE_CALCULO_ISSQN")
    private BigDecimal baseCalculoIssqn;
    @Column(name = "VALOR_ISSQN")
    private BigDecimal valorIssqn;
    @Column(name = "VALOR_PIS_ISSQN")
    private BigDecimal valorPisIssqn;
    @Column(name = "VALOR_COFINS_ISSQN")
    private BigDecimal valorCofinsIssqn;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PRESTACAO_SERVICO")
    private Date dataPrestacaoServico;
    @Column(name = "VALOR_DEDUCAO_ISSQN")
    private BigDecimal valorDeducaoIssqn;
    @Column(name = "OUTRAS_RETENCOES_ISSQN")
    private BigDecimal outrasRetencoesIssqn;
    @Column(name = "DESCONTO_INCONDICIONADO_ISSQN")
    private BigDecimal descontoIncondicionadoIssqn;
    @Column(name = "DESCONTO_CONDICIONADO_ISSQN")
    private BigDecimal descontoCondicionadoIssqn;
    @Column(name = "TOTAL_RETENCAO_ISSQN")
    private BigDecimal totalRetencaoIssqn;
    @Column(name = "REGIME_ESPECIAL_TRIBUTACAO")
    private Integer regimeEspecialTributacao;
    @Column(name = "VALOR_RETIDO_PIS")
    private BigDecimal valorRetidoPis;
    @Column(name = "VALOR_RETIDO_COFINS")
    private BigDecimal valorRetidoCofins;
    @Column(name = "VALOR_RETIDO_CSLL")
    private BigDecimal valorRetidoCsll;
    @Column(name = "BASE_CALCULO_IRRF")
    private BigDecimal baseCalculoIrrf;
    @Column(name = "VALOR_RETIDO_IRRF")
    private BigDecimal valorRetidoIrrf;
    @Column(name = "BASE_CALCULO_PREVIDENCIA")
    private BigDecimal baseCalculoPrevidencia;
    @Column(name = "VALOR_RETIDO_PREVIDENCIA")
    private BigDecimal valorRetidoPrevidencia;
    @Column(name = "TROCO")
    private BigDecimal troco;
    @Column(name = "COMEX_UF_EMBARQUE")
    private String comexUfEmbarque;
    @Column(name = "COMEX_LOCAL_EMBARQUE")
    private String comexLocalEmbarque;
    @Column(name = "COMEX_LOCAL_DESPACHO")
    private String comexLocalDespacho;
    @Column(name = "COMPRA_NOTA_EMPENHO")
    private String compraNotaEmpenho;
    @Column(name = "COMPRA_PEDIDO")
    private String compraPedido;
    @Column(name = "COMPRA_CONTRATO")
    private String compraContrato;
    @Column(name = "INFORMACOES_ADD_FISCO")
    private String informacoesAddFisco;
    @Column(name = "INFORMACOES_ADD_CONTRIBUINTE")
    private String informacoesAddContribuinte;
    @Column(name = "STATUS_NOTA")
    private Integer statusNota;
    @Column(name = "QUANTIDADE_IMPRESSAO_DANFE")
    private Integer quantidadeImpressaoDanfe;
    @Column(name = "INDICADOR_PRESENCA")
    private String indicadorPresenca;
    @Column(name = "VALOR_FCP")
    private BigDecimal valorFcp;
    @Column(name = "VALOR_FCP_ST")
    private BigDecimal valorFcpSt;
    @Column(name = "VALOR_FCP_ST_RETIDO")
    private BigDecimal valorFcpStRetido;
    @Column(name = "VALOR_IPI_DEVOLVIDO")
    private BigDecimal valorIpiDevolvido;
    @Column(name = "VERSAO_APLICATIVO")
    private String versaoAplicativo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_HORA_PROCESSAMENTO")
    private Date dataHoraProcessamento;
    @Column(name = "NUMERO_PROTOCOLO")
    private String numeroProtocolo;
    @Column(name = "DIGEST_VALUE")
    private String digestValue;
    @Column(name = "CODIGO_STATUS_RESPOSTA")
    private String codigoStatusResposta;
    @Column(name = "DESCRICAO_MOTIVO_RESPOSTA")
    private String descricaoMotivoResposta;
    @Column(name = "JUSTIFICATIVA_CANCELAMENTO")
    private String justificativaCancelamento;
    @Column(name = "VALOR_ICMS_FCP_UF_DESTINO")
    private BigDecimal valorIcmsFcpUfDestino;
    @Column(name = "VALOR_ICMS_INTER_UF_DESTINO")
    private BigDecimal valorIcmsInterUfDestino;
    @Column(name = "VALOR_ICMS_INTER_UF_REMETENTE")
    private BigDecimal valorIcmsInterUfRemetente;
    @Column(name = "QRCODE")
    private String qrcode;
    @Column(name = "URL_CHAVE")
    private String urlChave;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Fornecedor fornecedor;
    @JoinColumn(name = "ID_VENDA_CABECALHO", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.LAZY)
    private VendaCabecalho vendaCabecalho;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne()
    private Empresa empresa;
    @JoinColumn(name = "ID_TRIBUT_OPERACAO_FISCAL", referencedColumnName = "ID")
    @ManyToOne
    private TributOperacaoFiscal tributOperacaoFiscal;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeDestinatario destinatario;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeEmitente emitente;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeLocalEntrega localEntrega;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeLocalRetirada localRetirada;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeTransporte transporte;
    @OneToOne(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private NfeFatura fatura;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("numeroItem ASC")
    private List<NfeDetalhe> listaNfeDetalhe;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeCupomFiscalReferenciado> listaCupomFiscalReferenciado;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeDuplicata> listaDuplicata;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeReferenciada> listaNfeReferenciada;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeNfReferenciada> listaNfReferenciada;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeCteReferenciado> listaCteReferenciado;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeProdRuralReferenciada> listaProdRuralReferenciada;
    @OneToMany(mappedBy = "nfeCabecalho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<NfeFormaPagamento> listaNfeFormaPagamento;
    @Transient
    private String csc;
    @Transient
    private String tokenCsc;
    @Transient
    private OsAbertura os;
    @Transient
    private PdvVendaCabecalho pdv;
    @Transient
    private EstoqueTransferenciaCabecalho transferencia;


    public NfeCabecalho() {

        this.emitente = new NfeEmitente();
        this.emitente.setNfeCabecalho(this);
        this.transporte = new NfeTransporte();
        this.transporte.setNfeCabecalho(this);
        this.transporte.setModalidadeFrete(0);
        this.transporte.setListaTransporteVolume(new HashSet<>());


        this.listaNfeReferenciada = new HashSet<>();
        this.listaNfReferenciada = new HashSet<>();
        this.listaCteReferenciado = new HashSet<>();
        this.listaProdRuralReferenciada = new HashSet<>();
        this.listaCupomFiscalReferenciado = new HashSet<>();
        this.listaDuplicata = new LinkedHashSet<>();
        this.listaNfeDetalhe = new ArrayList<>();
        this.listaNfeFormaPagamento = new HashSet<>();


        this.consumidorOperacao = 1;

        this.tipoEmissao = 1;
        this.formatoImpressaoDanfe = 1;

        // this.TributOperacaoFiscal(new TributOperacaoFiscal());
        this.tipoOperacao = 1;
        this.statusNota = 0;
        // 0=Sem geracao de DANFE;
        // 1=DANFE normal,Retrato;
        // 2=DANFE normal,Paisagem;
        // 3=DANFE Simplificado;
        // 4=DANFE NFC-e;
        // 5=DANFE NFC-e em mensagem eletronica.

        // 1=Operacao presencial;
        // 4=NFC-e em operacao com entrega em domicilio;
        this.consumidorOperacao = 1;
        // 0=Nao;1=Consumidor final;

        // 1=Emissao normal (nao em contingencia);
        // 2=Contingencia FS-IA, com impressao do DANFE em formulario de seguranca;
        // 3=Contingencia SCAN (Sistema de Contingencia do Ambiente Nacional);
        // 4=Contingencia DPEC (Declaracao Previa da Emissao em Contingencia);
        // 5=Contingencia FS-DA, com impressão do DANFE em formulario de seguranca;
        // 6=Contingencia SVC-AN (SEFAZ Virtual de Contingencia do AN);
        // 7=Contingencia SVC-RS (SEFAZ
        // Virtual de Contingencia do RS);
        // 9=Contingencia off-line da NFC-e;
        this.tipoEmissao = 1;
        this.finalidadeEmissao = 1;

        // 1=Operacao interna;2=Operacao interestadual;3=Operacao com exterior.
        this.localDestino = 1;
        // this.getTransporte().setModalidadeFrete(0);

        this.naturezaOperacao = "VENDA";

        this.dataHoraEmissao = new Date();
        this.dataHoraEntradaSaida = new Date();

        this.baseCalculoIcms = BigDecimal.ZERO;
        this.valorIcms = BigDecimal.ZERO;
        this.valorTotalProdutos = BigDecimal.ZERO;
        this.baseCalculoIcmsSt = BigDecimal.ZERO;
        this.valorIcmsSt = BigDecimal.ZERO;
        this.valorIpi = BigDecimal.ZERO;
        this.valorPis = BigDecimal.ZERO;
        this.valorCofins = BigDecimal.ZERO;
        this.valorFrete = BigDecimal.ZERO;
        this.valorSeguro = BigDecimal.ZERO;
        this.valorDespesasAcessorias = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
        this.valorImpostoImportacao = BigDecimal.ZERO;
        this.baseCalculoIssqn = BigDecimal.ZERO;
        this.valorIssqn = BigDecimal.ZERO;
        this.valorPisIssqn = BigDecimal.ZERO;
        this.valorCofinsIssqn = BigDecimal.ZERO;
        this.valorServicos = BigDecimal.ZERO;
        this.valorRetidoPis = BigDecimal.ZERO;
        this.valorRetidoCofins = BigDecimal.ZERO;
        this.valorRetidoCsll = BigDecimal.ZERO;
        this.baseCalculoIrrf = BigDecimal.ZERO;
        this.valorRetidoIrrf = BigDecimal.ZERO;
        this.baseCalculoPrevidencia = BigDecimal.ZERO;
        this.valorRetidoPrevidencia = BigDecimal.ZERO;
        this.valorIcmsDesonerado = BigDecimal.ZERO;

        this.valorFcp = BigDecimal.ZERO;
        this.valorFcpSt = BigDecimal.ZERO;
        this.valorFcpStRetido = BigDecimal.ZERO;
        this.valorIpiDevolvido = BigDecimal.ZERO;

        this.statusNota = StatusTransmissao.EDICAO.getCodigo();
    }

    public NfeCabecalho(Integer id, String numero) {
        this.id = id;
        this.numero = numero;
    }

    public NfeCabecalho(Integer id, String chaveAcesso, String digitoChaveAcesso, Integer statusNota) {
        this.id = id;
        this.chaveAcesso = chaveAcesso;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.statusNota = statusNota;

    }


    public NfeCabecalho(Integer id, String destinatario, String serie, String numero, Date dataHoraEmissao, String chaveAcesso, String digitoChaveAcesso, BigDecimal valorTotal, Integer statusNota, String codigoModelo) {
        this.id = id;
        this.destinatario = new NfeDestinatario(destinatario);
        this.serie = serie;
        this.numero = numero;
        this.dataHoraEmissao = dataHoraEmissao;
        this.chaveAcesso = chaveAcesso;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.valorTotal = valorTotal;
        this.statusNota = statusNota;
        this.codigoModelo = codigoModelo;
    }


    public NfeCabecalho(Integer id, String serie, String numero, Date dataHoraEmissao, String chaveAcesso, String digitoChaveAcesso, BigDecimal valorTotal, Integer statusNota, String codigoModelo, String qrcode) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.dataHoraEmissao = dataHoraEmissao;
        this.chaveAcesso = chaveAcesso;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.valorTotal = valorTotal;
        this.statusNota = statusNota;
        this.codigoModelo = codigoModelo;
        this.qrcode = qrcode;
    }

    public NfeCabecalho(Integer id, Fornecedor fornecedor, String serie, String numero, Date dataHoraEntradaSaida, Date dataHoraEmissao, String chaveAcesso, String digitoChaveAcesso, BigDecimal valorTotal, Integer statusNota) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.serie = serie;
        this.numero = numero;
        this.dataHoraEmissao = dataHoraEmissao;
        this.dataHoraEntradaSaida = dataHoraEntradaSaida;
        this.chaveAcesso = chaveAcesso;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.valorTotal = valorTotal;
        this.statusNota = statusNota;
    }


    public NfeCabecalho(Integer id, Integer idempresa, String cnpjEmpresa, String digitoChaveAcesso, String chaveAcesso, String qrcode, String codigoModelo, String serie, Integer statusNota) {
        this.id = id;
        this.qrcode = qrcode;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.chaveAcesso = chaveAcesso;
        this.empresa = new Empresa(idempresa);
        this.empresa.setCnpj(cnpjEmpresa);
        this.codigoModelo = codigoModelo;
        this.serie = serie;
        this.statusNota = statusNota;

    }

    public NfeCabecalho(Integer id, String serie, String numero, Date dataHoraEmissao, String chaveAcesso, String digitoChaveAcesso, String codigoModelo, String numeroProtocolo, String justificativaCancelamento, Integer ambiente, Date dataHoraProcessamento, Integer statusNota) {
        this.id = id;
        this.serie = serie;
        this.numero = numero;
        this.dataHoraEmissao = dataHoraEmissao;
        this.chaveAcesso = chaveAcesso;
        this.digitoChaveAcesso = digitoChaveAcesso;
        this.codigoModelo = codigoModelo;
        this.numeroProtocolo = numeroProtocolo;
        this.justificativaCancelamento = justificativaCancelamento;
        this.ambiente = ambiente;
        this.dataHoraProcessamento = dataHoraProcessamento;
        this.statusNota = statusNota;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * cUF - Código da UF do emitente do Documento Fiscal. Utilizar a Tabela do IBGE de código de unidades da federação
     * (Anexo IX - Tabela de UF, Município e País)
     *
     * @return ufEmitente
     */
    public Integer getUfEmitente() {
        return ufEmitente;
    }

    /**
     * cUF - Código da UF do emitente do Documento Fiscal. Utilizar a Tabela do IBGE de código de unidades da federação
     * (Anexo IX - Tabela de UF, Município e País)
     *
     * @param ufEmitente
     */
    public void setUfEmitente(Integer ufEmitente) {
        this.ufEmitente = ufEmitente;
    }

    /**
     * cNF - Código numérico que compõe a Chave de Acesso. Número aleatório gerado pelo emitente para cada NF-e para
     * evitar acessos indevidos da NF-e. (v2.0)
     *
     * @return
     */
    public String getCodigoNumerico() {
        return codigoNumerico;
    }

    /**
     * cNF - Código numérico que compõe a Chave de Acesso. Número aleatório gerado pelo emitente para cada NF-e para
     * evitar acessos indevidos da NF-e. (v2.0)
     *
     * @param codigoNumerico
     */
    public void setCodigoNumerico(String codigoNumerico) {
        this.codigoNumerico = codigoNumerico;
    }

    /**
     * natOp - Informar a natureza da operação de que decorrer a saída ou a entrada, tais como: venda, compra,
     * transferência, devolução, importação, consignação, remessa (para fins de demonstração, de industrialização ou
     * outra), conforme previsto na alínea 'i', inciso I, art. 19 do CONVÊNIO S/Nº, de 15 de dezembro de 1970.
     *
     * @return
     */
    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }

    /**
     * natOp - Informar a natureza da operação de que decorrer a saída ou a entrada, tais como: venda, compra,
     * transferência, devolução, importação, consignação, remessa (para fins de demonstração, de industrialização ou
     * outra), conforme previsto na alínea 'i', inciso I, art. 19 do CONVÊNIO S/Nº, de 15 de dezembro de 1970.
     *
     * @param naturezaOperacao
     */
    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }

    /**
     * *
     * indPag - 0=pagamento à vista | 1=pagamento à prazo | 2=outros.
     *
     * @return
     */
    public Integer getIndicadorFormaPagamento() {
        return indicadorFormaPagamento;
    }

    /**
     * *
     * indPag - 0=pagamento à vista | 1=pagamento à prazo | 2=outros.
     *
     * @param indicadorFormaPagamento
     */
    public void setIndicadorFormaPagamento(Integer indicadorFormaPagamento) {
        this.indicadorFormaPagamento = indicadorFormaPagamento;
    }

    /**
     * *
     * mod - Utilizar o código 55 para identificação da NF-e, emitida em substituição ao modelo 1 ou 1A. 65=NFC-e,
     * utilizada nas operações de vendas no varejo, onde não for exigida a NF-e por dispositivo legal.
     */
    public String getCodigoModelo() {
        return codigoModelo;
    }

    /**
     * *
     * mod - Utilizar o código 55 para identificação da NF-e, emitida em substituição ao modelo 1 ou 1A. 65=NFC-e,
     * utilizada nas operações de vendas no varejo, onde não for exigida a NF-e por dispositivo legal.
     *
     * @param codigoModelo
     */
    public void setCodigoModelo(String codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    /**
     * serie - Série do Documento Fiscal, preencher com zeros na hipótese de a NF-e não possuir série. (v2.0) Série
     * 890-899 de uso exclusivo para emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do
     * site do Fisco (procEmi=2). (v2.0) Serie 900-999 – uso exclusivo de NF-e emitidas no SCAN. (v2.0)
     *
     * @return
     */
    public String getSerie() {
        return serie;
    }

    /**
     * serie - Série do Documento Fiscal, preencher com zeros na hipótese de a NF-e não possuir série. (v2.0) Série
     * 890-899 de uso exclusivo para emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do
     * site do Fisco (procEmi=2). (v2.0) Serie 900-999 – uso exclusivo de NF-e emitidas no SCAN. (v2.0)
     *
     * @param serie
     */
    public void setSerie(String serie) {
        this.serie = serie;
    }

    /**
     * nNF - Número do Documento Fiscal.
     *
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     * nNF - Número do Documento Fiscal.
     *
     * @param numero
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * dhEmi - Data e Hora de emissão do Documento Fiscal
     *
     * @return
     */
    public Date getDataHoraEmissao() {
        return dataHoraEmissao;
    }

    /**
     * dhEmi - Data e Hora de emissão do Documento Fiscal
     *
     * @param dataHoraEmissao
     */
    public void setDataHoraEmissao(Date dataHoraEmissao) {
        this.dataHoraEmissao = dataHoraEmissao;
    }

    /**
     * dhSaiEnt - Data e Hora de Saída da Mercadoria/Produto. No caso da NF de entrada, esta é a Data e Hora de entrada.
     *
     * @return
     */
    public Date getDataHoraEntradaSaida() {
        return dataHoraEntradaSaida;
    }

    /**
     * dhSaiEnt - Data e Hora de Saída da Mercadoria/Produto. No caso da NF de entrada, esta é a Data e Hora de entrada.
     *
     * @param dataHoraEntradaSaida
     */
    public void setDataHoraEntradaSaida(Date dataHoraEntradaSaida) {
        this.dataHoraEntradaSaida = dataHoraEntradaSaida;
    }

    /**
     * tpNF - 0-entrada / 1-saída
     *
     * @return
     */
    public Integer getTipoOperacao() {
        return tipoOperacao;
    }

    /**
     * tpNF - 0-entrada / 1-saída
     *
     * @param tipoOperacao
     */
    public void setTipoOperacao(Integer tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    /**
     * idDest - Identificador de local de destino da operação - 1=Operação interna; 2=Operação interestadual; 3=Operação
     * com exterior.
     *
     * @return
     */
    public Integer getLocalDestino() {
        return localDestino;
    }

    /**
     * idDest - Identificador de local de destino da operação - 1=Operação interna; 2=Operação interestadual; 3=Operação
     * com exterior.
     *
     * @param localDestino
     */
    public void setLocalDestino(Integer localDestino) {
        this.localDestino = localDestino;
    }

    /**
     * cMunFG - Informar o município de ocorrência do fato gerador do ICMS. Utilizar a Tabela do IBGE
     *
     * @return
     */
    public Integer getCodigoMunicipio() {
        return codigoMunicipio;
    }

    /**
     * cMunFG - Informar o município de ocorrência do fato gerador do ICMS. Utilizar a Tabela do IBGE
     *
     * @param codigoMunicipio
     */
    public void setCodigoMunicipio(Integer codigoMunicipio) {
        this.codigoMunicipio = codigoMunicipio;
    }

    /**
     * tpImp - 0=Sem geração de DANFE; 1=DANFE normal, Retrato; 2=DANFE normal, Paisagem; 3=DANFE Simplificado; 4=DANFE
     * NFC-e; 5=DANFE NFC-e em mensagem eletrônica. Nota: O envio de mensagem eletrônica pode ser feita de forma
     * simultânea com a impressão do DANFE. Usar o tpImp=5 na NFC-e quando esta for a única forma de disponibilização do
     * DANFE.
     *
     * @return
     */
    public Integer getFormatoImpressaoDanfe() {
        return formatoImpressaoDanfe;
    }

    /**
     * tpImp - 0=Sem geração de DANFE; 1=DANFE normal, Retrato; 2=DANFE normal, Paisagem; 3=DANFE Simplificado; 4=DANFE
     * NFC-e; 5=DANFE NFC-e em mensagem eletrônica. Nota: O envio de mensagem eletrônica pode ser feita de forma
     * simultânea com a impressão do DANFE. Usar o tpImp=5 na NFC-e quando esta for a única forma de disponibilização do
     * DANFE.
     *
     * @param formatoImpressaoDanfe
     */
    public void setFormatoImpressaoDanfe(Integer formatoImpressaoDanfe) {
        this.formatoImpressaoDanfe = formatoImpressaoDanfe;
    }

    /**
     * tpEmis - 1=Emissão normal (não em contingência); 2=Contingência FS-IA, com impressão do DANFE em formulário de
     * segurança; 3=Contingência SCAN (Sistema de Contingência do Ambiente Nacional); 4=Contingência DPEC (Declaração
     * Prévia da Emissão em Contingência); 5=Contingência FS-DA, com impressão do DANFE em formulário de segurança;
     * 6=Contingência SVC-AN (SEFAZ Virtual de Contingência do AN); 7=Contingência SVC-RS (SEFAZ Virtual de Contingência
     * do RS); 9=Contingência off-line da NFC-e; Nota: Para a NFC-e somente estão disponíveis e são válidas as opções de
     * contingência 5 e 9.
     *
     * @return
     */
    public Integer getTipoEmissao() {
        return tipoEmissao;
    }

    /**
     * tpEmis - 1=Emissão normal (não em contingência); 2=Contingência FS-IA, com impressão do DANFE em formulário de
     * segurança; 3=Contingência SCAN (Sistema de Contingência do Ambiente Nacional); 4=Contingência DPEC (Declaração
     * Prévia da Emissão em Contingência); 5=Contingência FS-DA, com impressão do DANFE em formulário de segurança;
     * 6=Contingência SVC-AN (SEFAZ Virtual de Contingência do AN); 7=Contingência SVC-RS (SEFAZ Virtual de Contingência
     * do RS); 9=Contingência off-line da NFC-e; Nota: Para a NFC-e somente estão disponíveis e são válidas as opções de
     * contingência 5 e 9.
     *
     * @param tipoEmissao
     */
    public void setTipoEmissao(Integer tipoEmissao) {
        this.tipoEmissao = tipoEmissao;
    }

    /**
     * Chave de acesso da NF-e composta por Código da UF + AAMM da emissão + CNPJ do Emitente + Modelo, Série e Número
     * da NFe + Código Numérico + DV.
     *
     * @return
     */
    public String getChaveAcesso() {
        return chaveAcesso;
    }

    /**
     * Chave de acesso da NF-e composta por Código da UF + AAMM da emissão + CNPJ do Emitente + Modelo, Série e Número
     * da NFe + Código Numérico + DV.
     *
     * @param chaveAcesso
     */
    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }

    public String getDigitoChaveAcesso() {
        return digitoChaveAcesso;
    }

    public void setDigitoChaveAcesso(String digitoChaveAcesso) {
        this.digitoChaveAcesso = digitoChaveAcesso;
    }

    /**
     * tpAmb - 1-Produção/ 2-Homologação
     *
     * @return
     */
    public Integer getAmbiente() {
        return ambiente;
    }

    /**
     * tpAmb - 1-Produção/ 2-Homologação
     *
     * @param ambiente
     */
    public void setAmbiente(Integer ambiente) {
        this.ambiente = ambiente;
    }

    /**
     * finNFe - 1=NF-e normal; 2=NF-e complementar; 3=NF-e de ajuste; 4=Devolução de mercadoria.
     *
     * @return
     */
    public Integer getFinalidadeEmissao() {
        return finalidadeEmissao;
    }

    /**
     * finNFe - 1=NF-e normal; 2=NF-e complementar; 3=NF-e de ajuste; 4=Devolução de mercadoria.
     *
     * @param finalidadeEmissao
     */
    public void setFinalidadeEmissao(Integer finalidadeEmissao) {
        this.finalidadeEmissao = finalidadeEmissao;
    }

    /**
     * indFinal - Indica operação com Consumidor final - 0=Normal; 1=Consumidor final;
     *
     * @return
     */
    public Integer getConsumidorOperacao() {
        return consumidorOperacao;
    }

    /**
     * indFinal - Indica operação com Consumidor final - 0=Normal; 1=Consumidor final;
     *
     * @param consumidorOperacao
     */
    public void setConsumidorOperacao(Integer consumidorOperacao) {
        this.consumidorOperacao = consumidorOperacao;
    }


    /**
     * procEmi - Identificador do processo de emissão da NF-e: 0 - emissão de NF-e com aplicativo do contribuinte; 1 -
     * emissão de NF-e avulsa pelo Fisco; 2 - emissão de NF-e avulsa, pelo contribuinte com seu certificado digital,
     * através do site do Fisco; 3- emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.
     *
     * @return
     */
    public Integer getProcessoEmissao() {
        return processoEmissao;
    }

    /**
     * procEmi - Identificador do processo de emissão da NF-e:
     * 0 - emissão de NF-e com aplicativo do contribuinte;
     * 1 -emissão de NF-e avulsa pelo Fisco;
     * 2 - emissão de NF-e avulsa, pelo contribuinte com seu certificado digital,
     * através do site do Fisco;
     * 3- emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco.
     *
     * @param processoEmissao
     */
    public void setProcessoEmissao(Integer processoEmissao) {
        this.processoEmissao = processoEmissao;
    }

    /**
     * verProc - Identificador da versão do processo de emissão (informar a versão do aplicativo emissor de NF-e)
     *
     * @return
     */
    public String getVersaoProcessoEmissao() {
        return versaoProcessoEmissao;
    }

    /**
     * verProc - Identificador da versão do processo de emissão (informar a versão do aplicativo emissor de NF-e).
     *
     * @param versaoProcessoEmissao
     */
    public void setVersaoProcessoEmissao(String versaoProcessoEmissao) {
        this.versaoProcessoEmissao = versaoProcessoEmissao;
    }

    /**
     * dhCont - Informar a data e hora de entrada em contingência no formato AAAA-MM-DDTHH:MM:SS
     *
     * @return
     */
    public Date getDataEntradaContingencia() {
        return dataEntradaContingencia;
    }

    /**
     * dhCont - Informar a data e hora de entrada em contingência no formato AAAA-MM-DDTHH:MM:SS
     *
     * @param dataEntradaContingencia
     */
    public void setDataEntradaContingencia(Date dataEntradaContingencia) {
        this.dataEntradaContingencia = dataEntradaContingencia;
    }

    /**
     * xJust - Justificativa da entrada em contingência
     *
     * @return
     */
    public String getJustificativaContingencia() {
        return justificativaContingencia;
    }

    /**
     * xJust - Justificativa da entrada em contingência
     *
     * @param justificativaContingencia
     */
    public void setJustificativaContingencia(String justificativaContingencia) {
        this.justificativaContingencia = justificativaContingencia;
    }

    /**
     * vBC - Base de Cálculo do ICMS
     *
     * @return
     */
    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }

    /**
     * vBC - Base de Cálculo do ICMS
     *
     * @param baseCalculoIcms
     */
    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }

    /**
     * vICMS - Valor Total do ICMS
     *
     * @return
     */
    public BigDecimal getValorIcms() {
        return valorIcms;
    }

    /**
     * vICMS - Valor Total do ICMS
     *
     * @param valorIcms
     */
    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }

    /**
     * vICMSDeson - Valor Total do ICMS desonerado
     *
     * @return
     */
    public BigDecimal getValorIcmsDesonerado() {
        return valorIcmsDesonerado;
    }

    /**
     * vICMSDeson - Valor Total do ICMS desonerado
     *
     * @param valorIcmsDesonerado
     */
    public void setValorIcmsDesonerado(BigDecimal valorIcmsDesonerado) {
        this.valorIcmsDesonerado = valorIcmsDesonerado;
    }

    /**
     * vBCST - Base de Cálculo do ICMS ST
     *
     * @return
     */
    public BigDecimal getBaseCalculoIcmsSt() {
        return baseCalculoIcmsSt;
    }

    /**
     * *
     * vBCST - Base de Cálculo do ICMS ST
     *
     * @param baseCalculoIcmsSt
     */
    public void setBaseCalculoIcmsSt(BigDecimal baseCalculoIcmsSt) {
        this.baseCalculoIcmsSt = baseCalculoIcmsSt;
    }

    /**
     * vST - Valor Total do ICMS ST
     *
     * @return
     */
    public BigDecimal getValorIcmsSt() {
        return Optional.ofNullable(valorIcmsSt).orElse(BigDecimal.ZERO);
    }

    /**
     * vST - Valor Total do ICMS ST
     *
     * @param valorIcmsSt
     */
    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }

    /**
     * vProd - Valor Total dos produtos e serviços
     *
     * @return
     */
    public BigDecimal getValorTotalProdutos() {
        return Optional.ofNullable(valorTotalProdutos).orElse(BigDecimal.ZERO);
    }

    /**
     * vProd - Valor Total dos produtos
     *
     * @param valorTotalProdutos
     */
    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    /**
     * vFrete - Valor Total do Frete
     *
     * @return
     */
    public BigDecimal getValorFrete() {
        return Optional.ofNullable(valorFrete).orElse(BigDecimal.ZERO);
    }

    /**
     * vFrete - Valor Total do Frete
     *
     * @param valorFrete
     */
    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    /**
     * vSeg - Valor Total do Seguro
     *
     * @return
     */
    public BigDecimal getValorSeguro() {
        return Optional.ofNullable(valorSeguro).orElse(BigDecimal.ZERO);
    }

    /**
     * vSeg - Valor Total do Seguro
     *
     * @param valorSeguro
     */
    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }

    /**
     * vDesc - Valor Total do Desconto
     *
     * @return
     */
    public BigDecimal getValorDesconto() {
        return Optional.ofNullable(valorDesconto).orElse(BigDecimal.ZERO);
    }

    /**
     * vDesc - Valor Total do Desconto
     *
     * @param valorDesconto
     */
    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    /**
     * vII - Valor Total do II
     *
     * @return
     */
    public BigDecimal getValorImpostoImportacao() {
        return Optional.ofNullable(valorImpostoImportacao).orElse(BigDecimal.ZERO);
    }

    /**
     * vII - Valor Total do II
     *
     * @param valorImpostoImportacao
     */
    public void setValorImpostoImportacao(BigDecimal valorImpostoImportacao) {
        this.valorImpostoImportacao = valorImpostoImportacao;
    }

    /**
     * vIPI - Valor Total do IPI
     *
     * @return
     */
    public BigDecimal getValorIpi() {
        return Optional.ofNullable(valorIpi).orElse(BigDecimal.ZERO);
    }

    /**
     * vIPI - Valor Total do IPI
     *
     * @param valorIpi
     */
    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }

    /**
     * vPIS - Valor do PIS
     *
     * @return
     */
    public BigDecimal getValorPis() {
        return valorPis;
    }

    /**
     * vPIS - Valor do PIS
     *
     * @param valorPis
     */
    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }

    /**
     * vCOFINS - Valor do COFINS - Valor do COFINS
     *
     * @return
     */
    public BigDecimal getValorCofins() {
        return valorCofins;
    }

    /**
     * vCOFINS - Valor do COFINS - Valor do COFINS
     *
     * @param valorCofins
     */
    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }

    /**
     * vOutro - Outras Despesas acessórias
     *
     * @return
     */
    public BigDecimal getValorDespesasAcessorias() {
        return Optional.ofNullable(valorDespesasAcessorias).orElse(BigDecimal.ZERO);
    }

    /**
     * vOutro - Outras Despesas acessórias
     *
     * @param valorDespesasAcessorias
     */
    public void setValorDespesasAcessorias(BigDecimal valorDespesasAcessorias) {
        this.valorDespesasAcessorias = valorDespesasAcessorias;
    }

    /**
     * vNF - Valor Total da NF-e [(+) vProd (id:W07) (-) vDesc (id:W10) (+) vICMSST (id:W06) (+) vFrete (id:W09) (+)
     * vSeg (id:W10) (+) vOutro (id:W15) (+) vII (id:W11) (+) vIPI (id:W12) (+) vServ (id:W19) (NT 2011/004)]
     *
     * @return
     */
    public BigDecimal getValorTotal() {
        return Optional.ofNullable(valorTotal).orElse(BigDecimal.ZERO);
    }

    /**
     * vNF - Valor Total da NF-e [(+) vProd (id:W07) (-) vDesc (id:W10) (+) vICMSST (id:W06) (+) vFrete (id:W09) (+)
     * vSeg (id:W10) (+) vOutro (id:W15) (+) vII (id:W11) (+) vIPI (id:W12) (+) vServ (id:W19) (NT 2011/004)]
     *
     * @param valorTotal
     */
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * vServ - Valor Total dos Serviços sob não-incidência ou não tributados pelo ICMS
     *
     * @return
     */
    public BigDecimal getValorServicos() {
        return Optional.ofNullable(valorServicos).orElse(BigDecimal.ZERO);
    }

    /**
     * vServ - Valor Total dos Serviços sob não-incidência ou não tributados pelo ICMS
     *
     * @param valorServicos
     */
    public void setValorServicos(BigDecimal valorServicos) {
        this.valorServicos = valorServicos;
    }

    /**
     * vBC - Base de Cálculo do ISS
     *
     * @return
     */
    public BigDecimal getBaseCalculoIssqn() {
        return baseCalculoIssqn;
    }

    /**
     * vBC - Base de Cálculo do ISS
     *
     * @param baseCalculoIssqn
     */
    public void setBaseCalculoIssqn(BigDecimal baseCalculoIssqn) {
        this.baseCalculoIssqn = baseCalculoIssqn;
    }

    /**
     * vISS - Valor Total do ISS
     *
     * @return
     */
    public BigDecimal getValorIssqn() {
        return valorIssqn;
    }

    /**
     * vISS - Valor Total do ISS
     *
     * @param valorIssqn
     */
    public void setValorIssqn(BigDecimal valorIssqn) {
        this.valorIssqn = valorIssqn;
    }

    /**
     * vPIS - Valor do PIS sobre serviços
     *
     * @return
     */
    public BigDecimal getValorPisIssqn() {
        return valorPisIssqn;
    }

    /**
     * vPIS - Valor do PIS sobre serviços
     *
     * @param valorPisIssqn
     */
    public void setValorPisIssqn(BigDecimal valorPisIssqn) {
        this.valorPisIssqn = valorPisIssqn;
    }

    /**
     * vCOFINS - Valor do COFINS sobre serviços
     *
     * @return
     */
    public BigDecimal getValorCofinsIssqn() {
        return valorCofinsIssqn;
    }

    /**
     * vCOFINS - Valor do COFINS sobre serviços
     *
     * @param valorCofinsIssqn
     */
    public void setValorCofinsIssqn(BigDecimal valorCofinsIssqn) {
        this.valorCofinsIssqn = valorCofinsIssqn;
    }

    /**
     * dCompet - Data da prestação do serviço
     *
     * @return
     */
    public Date getDataPrestacaoServico() {
        return dataPrestacaoServico;
    }

    /**
     * dCompet - Data da prestação do serviço
     *
     * @param dataPrestacaoServico
     */
    public void setDataPrestacaoServico(Date dataPrestacaoServico) {
        this.dataPrestacaoServico = dataPrestacaoServico;
    }

    /**
     * vOutro - Valor total outras retenções
     *
     * @return
     */
    public BigDecimal getValorDeducaoIssqn() {
        return valorDeducaoIssqn;
    }

    /**
     * vOutro - Valor total outras retenções
     *
     * @param valorDeducaoIssqn
     */
    public void setValorDeducaoIssqn(BigDecimal valorDeducaoIssqn) {
        this.valorDeducaoIssqn = valorDeducaoIssqn;
    }

    /**
     * vDescIncond - Valor total desconto incondicionado
     *
     * @return
     */
    public BigDecimal getOutrasRetencoesIssqn() {
        return outrasRetencoesIssqn;
    }

    /**
     * vDescIncond - Valor total desconto incondicionado
     *
     * @param outrasRetencoesIssqn
     */
    public void setOutrasRetencoesIssqn(BigDecimal outrasRetencoesIssqn) {
        this.outrasRetencoesIssqn = outrasRetencoesIssqn;
    }

    /**
     * vDescCond - Valor total desconto condicionado
     *
     * @return
     */
    public BigDecimal getDescontoIncondicionadoIssqn() {
        return descontoIncondicionadoIssqn;
    }

    /**
     * vDescCond - Valor total desconto incondicionado
     *
     * @param descontoIncondicionadoIssqn
     */
    public void setDescontoIncondicionadoIssqn(BigDecimal descontoIncondicionadoIssqn) {
        this.descontoIncondicionadoIssqn = descontoIncondicionadoIssqn;
    }

    /**
     * vDescCond - Valor total desconto incondicionado
     *
     * @return
     */
    public BigDecimal getDescontoCondicionadoIssqn() {
        return descontoCondicionadoIssqn;
    }

    /**
     * vDescCond - Valor total desconto condicionado
     *
     * @param descontoCondicionadoIssqn
     */
    public void setDescontoCondicionadoIssqn(BigDecimal descontoCondicionadoIssqn) {
        this.descontoCondicionadoIssqn = descontoCondicionadoIssqn;
    }

    /**
     * vISSRet - Valor total retenção ISS
     *
     * @return
     */
    public BigDecimal getTotalRetencaoIssqn() {
        return totalRetencaoIssqn;
    }

    /**
     * vISSRet - Valor total retenção ISS
     *
     * @param totalRetencaoIssqn
     */
    public void setTotalRetencaoIssqn(BigDecimal totalRetencaoIssqn) {
        this.totalRetencaoIssqn = totalRetencaoIssqn;
    }

    /**
     * cRegTrib - Código do Regime Especial de Tributação - 1=Microempresa Municipal; 2=Estimativa; 3=Sociedade de
     * Profissionais; 4=Cooperativa; 5=Microempresário Individual (MEI); 6=Microempresário e Empresa de Pequeno Porte
     * (ME/EPP)
     *
     * @return
     */
    public Integer getRegimeEspecialTributacao() {
        return regimeEspecialTributacao;
    }

    /**
     * cRegTrib - Código do Regime Especial de Tributação - 1=Microempresa Municipal; 2=Estimativa; 3=Sociedade de
     * Profissionais; 4=Cooperativa; 5=Microempresário Individual (MEI); 6=Microempresário e Empresa de Pequeno Porte
     * (ME/EPP)
     *
     * @param regimeEspecialTributacao
     */
    public void setRegimeEspecialTributacao(Integer regimeEspecialTributacao) {
        this.regimeEspecialTributacao = regimeEspecialTributacao;
    }

    /**
     * vRetPIS - Valor Retido de PIS
     *
     * @return
     */
    public BigDecimal getValorRetidoPis() {
        return valorRetidoPis;
    }

    /**
     * vRetPIS - Valor Retido de PIS
     *
     * @param valorRetidoPis
     */
    public void setValorRetidoPis(BigDecimal valorRetidoPis) {
        this.valorRetidoPis = valorRetidoPis;
    }

    /**
     * vRetCOFINS - Valor Retido de COFINS
     *
     * @return
     */
    public BigDecimal getValorRetidoCofins() {
        return valorRetidoCofins;
    }

    /**
     * vRetCOFINS - Valor Retido de COFINS
     *
     * @param valorRetidoCofins
     */
    public void setValorRetidoCofins(BigDecimal valorRetidoCofins) {
        this.valorRetidoCofins = valorRetidoCofins;
    }

    /**
     * vRetCSLL - Valor Retido de CSLL
     *
     * @return
     */
    public BigDecimal getValorRetidoCsll() {
        return valorRetidoCsll;
    }

    /**
     * vRetCSLL - Valor Retido de CSLL
     *
     * @param valorRetidoCsll
     */
    public void setValorRetidoCsll(BigDecimal valorRetidoCsll) {
        this.valorRetidoCsll = valorRetidoCsll;
    }

    /**
     * vBCIRRF - Base de Cálculo do IRRF
     *
     * @return
     */
    public BigDecimal getBaseCalculoIrrf() {
        return baseCalculoIrrf;
    }

    /**
     * vBCIRRF - Base de Cálculo do IRRF
     *
     * @param baseCalculoIrrf
     */
    public void setBaseCalculoIrrf(BigDecimal baseCalculoIrrf) {
        this.baseCalculoIrrf = baseCalculoIrrf;
    }

    /**
     * vIRRF - Valor Retido do IRRF
     *
     * @return
     */
    public BigDecimal getValorRetidoIrrf() {
        return valorRetidoIrrf;
    }

    /**
     * vIRRF - Valor Retido do IRRF
     *
     * @param valorRetidoIrrf
     */
    public void setValorRetidoIrrf(BigDecimal valorRetidoIrrf) {
        this.valorRetidoIrrf = valorRetidoIrrf;
    }

    /**
     * vBCRetPrev - Base de Cálculo da Retenção da Previdência Social
     *
     * @return
     */
    public BigDecimal getBaseCalculoPrevidencia() {
        return baseCalculoPrevidencia;
    }

    /**
     * vBCRetPrev - Base de Cálculo da Retenção da Previdência Social
     *
     * @param baseCalculoPrevidencia
     */
    public void setBaseCalculoPrevidencia(BigDecimal baseCalculoPrevidencia) {
        this.baseCalculoPrevidencia = baseCalculoPrevidencia;
    }

    /**
     * vRetPrev - Valor da Retenção da Previdência Social
     *
     * @return
     */
    public BigDecimal getValorRetidoPrevidencia() {
        return valorRetidoPrevidencia;
    }

    /**
     * vRetPrev - Valor da Retenção da Previdência Social
     *
     * @param valorRetidoPrevidencia
     */
    public void setValorRetidoPrevidencia(BigDecimal valorRetidoPrevidencia) {
        this.valorRetidoPrevidencia = valorRetidoPrevidencia;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    /**
     * UFSaidaPais - Sigla da UF de Embarque ou de transposição de fronteira (antigo UFEmbarq)
     *
     * @return
     */
    public String getComexUfEmbarque() {
        return comexUfEmbarque;
    }

    /**
     * UFSaidaPais - Sigla da UF de Embarque ou de transposição de fronteira (antigo UFEmbarq)
     *
     * @param comexUfEmbarque
     */
    public void setComexUfEmbarque(String comexUfEmbarque) {
        this.comexUfEmbarque = comexUfEmbarque;
    }

    /**
     * xLocExporta - Descrição do Local de Embarque ou de transposição de fronteira (antigo xLocEmbarq)
     *
     * @return
     */
    public String getComexLocalEmbarque() {
        return comexLocalEmbarque;
    }

    /**
     * xLocExporta - Descrição do Local de Embarque ou de transposição de fronteira (antigo xLocEmbarq)
     *
     * @param comexLocalEmbarque
     */
    public void setComexLocalEmbarque(String comexLocalEmbarque) {
        this.comexLocalEmbarque = comexLocalEmbarque;
    }

    /**
     * xLocDespacho - Descrição do local de despacho
     *
     * @return
     */
    public String getComexLocalDespacho() {
        return comexLocalDespacho;
    }

    /**
     * xLocDespacho - Descrição do local de despacho
     *
     * @param comexLocalDespacho
     */
    public void setComexLocalDespacho(String comexLocalDespacho) {
        this.comexLocalDespacho = comexLocalDespacho;
    }

    /**
     * xNEmp - Informar a identificação da Nota de Empenho, quando se tratar de compras públicas
     *
     * @return
     */
    public String getCompraNotaEmpenho() {
        return compraNotaEmpenho;
    }

    /**
     * xNEmp - Informar a identificação da Nota de Empenho, quando se tratar de compras públicas
     *
     * @param compraNotaEmpenho
     */
    public void setCompraNotaEmpenho(String compraNotaEmpenho) {
        this.compraNotaEmpenho = compraNotaEmpenho;
    }

    /**
     * xPed - Informar o pedido.
     *
     * @return
     */
    public String getCompraPedido() {
        return compraPedido;
    }

    /**
     * xPed - Informar o pedido.
     *
     * @param compraPedido
     */
    public void setCompraPedido(String compraPedido) {
        this.compraPedido = compraPedido;
    }

    public String getCompraContrato() {
        return compraContrato;
    }

    public void setCompraContrato(String compraContrato) {
        this.compraContrato = compraContrato;
    }

    public String getInformacoesAddFisco() {
        return informacoesAddFisco;
    }

    public void setInformacoesAddFisco(String informacoesAddFisco) {
        this.informacoesAddFisco = informacoesAddFisco;
    }

    public String getInformacoesAddContribuinte() {
        return informacoesAddContribuinte;
    }

    public void setInformacoesAddContribuinte(String informacoesAddContribuinte) {
        this.informacoesAddContribuinte = informacoesAddContribuinte;
    }

    public Integer getStatusNota() {
        return statusNota;
    }

    public void setStatusNota(Integer statusNota) {
        this.statusNota = statusNota;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public TributOperacaoFiscal getTributOperacaoFiscal() {
        return tributOperacaoFiscal;
    }

    public void setTributOperacaoFiscal(TributOperacaoFiscal tributOperacaoFiscal) {
        this.tributOperacaoFiscal = tributOperacaoFiscal;
    }

    public NfeDestinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(NfeDestinatario destinatario) {
        this.destinatario = destinatario;
    }

    public NfeLocalEntrega getLocalEntrega() {
        return localEntrega;
    }

    public void setLocalEntrega(NfeLocalEntrega localEntrega) {
        this.localEntrega = localEntrega;
    }

    public NfeLocalRetirada getLocalRetirada() {
        return localRetirada;
    }

    public void setLocalRetirada(NfeLocalRetirada localRetirada) {
        this.localRetirada = localRetirada;
    }

    public NfeTransporte getTransporte() {
        return transporte;
    }

    public void setTransporte(NfeTransporte transporte) {
        this.transporte = transporte;
    }

    public NfeFatura getFatura() {
        return fatura;
    }

    public void setFatura(NfeFatura fatura) {
        this.fatura = fatura;
    }

    public List<NfeDetalhe> getListaNfeDetalhe() {
        return Optional.ofNullable(listaNfeDetalhe).orElse(new ArrayList<>());
    }

    public void setListaNfeDetalhe(List<NfeDetalhe> listaNfeDetalhe) {
        this.listaNfeDetalhe = listaNfeDetalhe;
    }

    public Set<NfeCupomFiscalReferenciado> getListaCupomFiscalReferenciado() {
        return listaCupomFiscalReferenciado;
    }

    public void setListaCupomFiscalReferenciado(Set<NfeCupomFiscalReferenciado> listaCupomFiscalReferenciado) {
        this.listaCupomFiscalReferenciado = listaCupomFiscalReferenciado;
    }

    public Set<NfeDuplicata> getListaDuplicata() {
        return listaDuplicata;
    }

    public void setListaDuplicata(Set<NfeDuplicata> listaDuplicata) {
        this.listaDuplicata = listaDuplicata;
    }

    public Set<NfeReferenciada> getListaNfeReferenciada() {
        return listaNfeReferenciada;
    }

    public void setListaNfeReferenciada(Set<NfeReferenciada> listaNfeReferenciada) {
        this.listaNfeReferenciada = listaNfeReferenciada;
    }

    public Set<NfeNfReferenciada> getListaNfReferenciada() {
        return listaNfReferenciada;
    }

    public void setListaNfReferenciada(Set<NfeNfReferenciada> listaNfReferenciada) {
        this.listaNfReferenciada = listaNfReferenciada;
    }

    public Set<NfeCteReferenciado> getListaCteReferenciado() {
        return listaCteReferenciado;
    }

    public void setListaCteReferenciado(Set<NfeCteReferenciado> listaCteReferenciado) {
        this.listaCteReferenciado = listaCteReferenciado;
    }

    public Set<NfeProdRuralReferenciada> getListaProdRuralReferenciada() {
        return listaProdRuralReferenciada;
    }

    public void setListaProdRuralReferenciada(Set<NfeProdRuralReferenciada> listaProdRuralReferenciada) {
        this.listaProdRuralReferenciada = listaProdRuralReferenciada;
    }

    public Set<NfeFormaPagamento> getListaNfeFormaPagamento() {
        return listaNfeFormaPagamento;
    }

    public void setListaNfeFormaPagamento(Set<NfeFormaPagamento> listaNfeFormaPagamento) {
        this.listaNfeFormaPagamento = listaNfeFormaPagamento;
    }

    public NfeEmitente getEmitente() {
        return emitente;
    }

    public void setEmitente(NfeEmitente emitente) {
        this.emitente = emitente;
    }

    public VendaCabecalho getVendaCabecalho() {
        return vendaCabecalho;
    }

    public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
        this.vendaCabecalho = vendaCabecalho;
    }


    public Integer getQuantidadeImpressaoDanfe() {
        return quantidadeImpressaoDanfe;
    }

    public void setQuantidadeImpressaoDanfe(Integer quantidadeImpressaoDanfe) {
        this.quantidadeImpressaoDanfe = quantidadeImpressaoDanfe;
    }

    public String getIndicadorPresenca() {
        return indicadorPresenca;
    }

    public void setIndicadorPresenca(String indicadorPresenca) {
        this.indicadorPresenca = indicadorPresenca;
    }

    public BigDecimal getValorFcp() {
        return valorFcp;
    }

    public void setValorFcp(BigDecimal valorFcp) {
        this.valorFcp = valorFcp;
    }

    public BigDecimal getValorFcpSt() {
        return valorFcpSt;
    }

    public void setValorFcpSt(BigDecimal valorFcpSt) {
        this.valorFcpSt = valorFcpSt;
    }

    public BigDecimal getValorFcpStRetido() {
        return valorFcpStRetido;
    }

    public void setValorFcpStRetido(BigDecimal valorFcpStRetido) {
        this.valorFcpStRetido = valorFcpStRetido;
    }

    public BigDecimal getValorIpiDevolvido() {
        return valorIpiDevolvido;
    }

    public void setValorIpiDevolvido(BigDecimal valorIpiDevolvido) {
        this.valorIpiDevolvido = valorIpiDevolvido;
    }

    public String getVersaoAplicativo() {
        return versaoAplicativo;
    }

    public void setVersaoAplicativo(String versaoAplicativo) {
        this.versaoAplicativo = versaoAplicativo;
    }

    public Date getDataHoraProcessamento() {
        return dataHoraProcessamento;
    }

    public void setDataHoraProcessamento(Date dataHoraProcessamento) {
        this.dataHoraProcessamento = dataHoraProcessamento;
    }

    public String getNumeroProtocolo() {
        return numeroProtocolo;
    }

    public void setNumeroProtocolo(String numeroProtocolo) {
        this.numeroProtocolo = numeroProtocolo;
    }

    public String getDigestValue() {
        return digestValue;
    }

    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }

    public String getCodigoStatusResposta() {
        return codigoStatusResposta;
    }

    public void setCodigoStatusResposta(String codigoStatusResposta) {
        this.codigoStatusResposta = codigoStatusResposta;
    }

    public String getDescricaoMotivoResposta() {
        return descricaoMotivoResposta;
    }

    public void setDescricaoMotivoResposta(String descricaoMotivoResposta) {
        this.descricaoMotivoResposta = descricaoMotivoResposta;
    }

    public String getJustificativaCancelamento() {
        return justificativaCancelamento;
    }

    public void setJustificativaCancelamento(String justificativaCancelamento) {
        this.justificativaCancelamento = justificativaCancelamento;
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

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getUrlChave() {
        return urlChave;
    }

    public void setUrlChave(String urlChave) {
        this.urlChave = urlChave;
    }

    public BigDecimal getValorTotalTributos() {
        return valorTotalTributos;
    }

    public void setValorTotalTributos(BigDecimal valorTotalTributos) {
        this.valorTotalTributos = valorTotalTributos;
    }

    public BigDecimal getValorTotalTributosFederais() {
        return valorTotalTributosFederais;
    }

    public void setValorTotalTributosFederais(BigDecimal valorTotalTributosFederais) {
        this.valorTotalTributosFederais = valorTotalTributosFederais;
    }

    public BigDecimal getValorTotalTributosEstaduais() {
        return valorTotalTributosEstaduais;
    }

    public void setValorTotalTributosEstaduais(BigDecimal valorTotalTributosEstaduais) {
        this.valorTotalTributosEstaduais = valorTotalTributosEstaduais;
    }

    public BigDecimal getValorTotalTributosMunicipais() {
        return valorTotalTributosMunicipais;
    }

    public void setValorTotalTributosMunicipais(BigDecimal valorTotalTributosMunicipais) {
        this.valorTotalTributosMunicipais = valorTotalTributosMunicipais;
    }

    public String getCsc() {
        return csc;
    }

    public void setCsc(String csc) {
        this.csc = csc;
    }

    public String getTokenCsc() {
        return tokenCsc;
    }

    public void setTokenCsc(String tokenCsc) {
        this.tokenCsc = tokenCsc;
    }

    public OsAbertura getOs() {
        return os;
    }

    public void setOs(OsAbertura os) {
        this.os = os;
    }

    public PdvVendaCabecalho getPdv() {
        return pdv;
    }

    public void setPdv(PdvVendaCabecalho pdv) {
        this.pdv = pdv;
    }

    public EstoqueTransferenciaCabecalho getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(EstoqueTransferenciaCabecalho transferencia) {
        this.transferencia = transferencia;
    }

    /**
     * vNF - Valor Total da NF-e [(+) vProd (id:W07) (-) vDesc (id:W10) (+) vICMSST (id:W06) (+) vFrete (id:W09) (+)
     * vSeg (id:W10) (+) vOutro (id:W15) (+) vII (id:W11) (+) vIPI (id:W12) (+) vServ (id:W19) (NT 2011/004)]
     */

    public BigDecimal calcularValorTotal() {
        valorTotal = BigDecimal.ZERO;
        //  calcularValores();
        valorTotal = valorTotal.add(getValorTotalProdutos())
                .subtract(getValorDesconto())
                .add(getValorIcmsSt())
                .add(getValorFrete())
                .add(getValorSeguro())
                .add(getValorDespesasAcessorias())
                .add(getValorImpostoImportacao())
                .add(getValorIpi())
                .add(getValorServicos());
        return valorTotal;
    }

    public void calcularValores() {

        valorTotalProdutos = getListaNfeDetalhe()
                .stream().map(NfeDetalhe::getValorBrutoProduto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        valorDesconto = getListaNfeDetalhe()
                .stream().map(NfeDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        getListaNfeDetalhe().stream().forEach(item -> {
            if (item.getNfeDetalheImpostoIcms() != null && item.getNfeDetalheImpostoIcms().getValorIcmsSt() != null) {
                baseCalculoIcmsSt = getBaseCalculoIcmsSt().add(item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
                valorIcmsSt = valorIcmsSt.add(item.getNfeDetalheImpostoIcms().getValorIcmsSt());
            }

            if (item.getNfeDetalheImpostoIpi() != null && item.getNfeDetalheImpostoIpi().getValorIpi() != null) {
                valorIpi = valorIpi.add(item.getNfeDetalheImpostoIpi().getValorIpi());
            }

        });

        valorFrete = getListaNfeDetalhe()
                .stream().map(NfeDetalhe::getValorFrete)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        valorSeguro = getListaNfeDetalhe()
                .stream().map(NfeDetalhe::getValorSeguro)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        valorDespesasAcessorias = getListaNfeDetalhe()
                .stream().map(NfeDetalhe::getValorOutrasDespesas)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);


    }

    public String valorTotalFormatado() {
        return formatarValor(getValorTotal());
    }

    private String formatarValor(BigDecimal valor) {
        DecimalFormatSymbols simboloDecimal = DecimalFormatSymbols.getInstance();
        simboloDecimal.setDecimalSeparator('.');
        DecimalFormat formatar = new DecimalFormat("0.00", simboloDecimal);

        return formatar.format(Optional.ofNullable(valor).orElse(BigDecimal.ZERO));
    }


    public String getChaveAcessoCompleta() {
        return (this.chaveAcesso == null ? "" : this.chaveAcesso) + (this.digitoChaveAcesso == null ? "" : this.digitoChaveAcesso);
    }

    public String getNomeXml() {
        String nome = getChaveAcessoCompleta();
        nome += StatusTransmissao.isAutorizado(this.statusNota) ? "-nfeProc.xml" : "-nfeCanc.xml";
        return nome;
    }

    public String getNomePdf() {
        String nome = getChaveAcessoCompleta();
        nome += StatusTransmissao.isAutorizado(this.statusNota) ? "-nfeProc.pdf" : "-nfeCanc.pdf";
        return nome;
    }

    public ModeloDocumento getModeloDocumento() {
        return ModeloDocumento.getByCodigo(Integer.valueOf(this.codigoModelo));
    }

    public StatusTransmissao getStatusTransmissao() {
        return StatusTransmissao.valueOfCodigo(this.statusNota);
    }


    public boolean isPodeEnviar() {
        StatusTransmissao status = StatusTransmissao.valueOfCodigo(statusNota);
        return (status != StatusTransmissao.AUTORIZADA) && (status != StatusTransmissao.CANCELADA) && (status != StatusTransmissao.ENVIADA);
    }

    public boolean isPodeImprimir() {
        StatusTransmissao status = StatusTransmissao.valueOfCodigo(statusNota);
        return (status == StatusTransmissao.AUTORIZADA) || (status == StatusTransmissao.CANCELADA);
    }

    public boolean isPodeCancelar() {
        return StatusTransmissao.isAutorizado(statusNota);
    }

    public boolean isPodeExcluir() {
        StatusTransmissao status = StatusTransmissao.valueOfCodigo(statusNota);
        return (status != StatusTransmissao.AUTORIZADA) && (status != StatusTransmissao.CANCELADA) && (status != StatusTransmissao.ENVIADA);
    }

    public boolean isEnviada() {
        return StatusTransmissao.ENVIADA == StatusTransmissao.valueOfCodigo(statusNota);
    }

    public boolean isTemProduto() {
        return getListaNfeDetalhe().isEmpty();
    }

    public List<NfeDuplicata> getDuplicatas() {
        return new ArrayList<>(Optional.ofNullable(getListaDuplicata()).orElse(new HashSet<>()));
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
        final NfeCabecalho other = (NfeCabecalho) obj;
        return Objects.equals(this.id, other.id);
    }
}
