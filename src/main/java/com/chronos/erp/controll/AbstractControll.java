
package com.chronos.erp.controll;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Estados;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.repository.UsuarioRepository;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @param <T>
 * @author john
 */
public abstract class AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected ERPLazyDataModel<T> dataModel;
    @Inject
    protected Repository<T> dao;
    protected List<Empresa> listaEmpresas;
    protected List<Empresa> empresasSelecionada;
    protected UsuarioDTO usuario;
    protected Empresa empresa;
    protected EmpresaEndereco enderecoEmpresa;
    protected Object[] atributos;
    protected Object[] joinFetch;
    protected String usuarioSupervisor;
    protected String senhaSupervisor;
    protected AdmParametro parametro;
    protected RestricaoSistema restricao;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;
    @Inject
    private Repository<Empresa> empresaRepository;
    @Inject
    private Repository<Auditoria> auditoriaRepository;
    @Inject
    private UsuarioRepository usuarioRepository;

    private T objetoSelecionado;
    private T objeto;
    private Auditoria log;
    private boolean necessarioAutorizacaoSupervisor = false;
    private boolean restricaoLiberada = false;
    private boolean telaGrid = true;

    private String titulo;
    private int activeTabIndex;

    //cadastro base
    private Map<String, String> SimNao;
    private Map<String, String> naoSim;
    private HashMap<String, Integer> SimNaoInteiro;
    private Map<String, String> tipoPessoa;
    private Map<String, String> sexo;
    private Map<String, String> tipoSangue;
    private Map<String, String> racaCor;
    private Map<String, String> crt;
    private Map<String, String> tipoRegimeEmpresa;
    private Map<String, Integer> tipoTelefone;
    private Map<String, String> tipoSindicato;
    private Map<String, String> produtoClasse;
    private Map<String, String> tipoItemSped;
    private Map<String, String> produtoTipo;
    private Map<String, String> produtoIat;
    private Map<String, String> produtoIppt;
    private Map<String, String> clienteIndicadorPreco;
    private Map<String, String> clienteTipoFrete;
    private Map<String, String> clienteFormaDesconto;
    private Map<String, String> fornecedorLocalizacao;
    private Map<String, String> colaboradorFormaPagamento;
    private Map<String, String> talonarioChequeStatus;
    private Map<String, String> chequeStatus;
    private Map<String, String> tipoContaCaixa;
    private Map<String, String> tipoFeriado;
    private Map<String, String> feriadoAbrangencia;
    private Map<String, String> bandeiras;

    //Financeiro
    private Map<String, String> tipoNaturazaFinanceira;
    private Map<String, String> layoutRemessa;
    private Map<String, String> especieCobranca;
    private Map<String, String> tipoBaixa;
    private Map<String, String> tiposPagamento;

    //Estoque
    private Map<String, String> requisicaoInternaSituacao;
    private Map<String, String> tipoReajuste;


    //Tributacao
    private HashMap<String, String> origemMercadoria;
    private HashMap<String, String> issModalidadeBaseCalculo;
    private HashMap<String, String> issCodigoTributacao;
    private HashMap<String, String> tributIcmsStBaseCalculo;
    private HashMap<String, String> tributIcmsBaseCalculo;
    private HashMap<String, String> pisModalidadeBaseCalculo;
    private HashMap<String, String> tipoDepreciacao;
    private HashMap<String, Integer> indicadorExigibilidadeIss;
    private HashMap<String, Integer> indicadorIncentivoIss;


    //Compras
    private Map<String, String> compraSituacaoCotacao;
    private Map<String, String> compraFormaPagamento;
    private Map<String, String> compraTipoFrete;


    //vendas
    private Map<String, String> vendaOrcamentoTipo;
    private Map<String, String> vendaSituacao;
    private Map<String, String> formaPagamento;
    private Map<String, String> vendaResponsavelFrete;
    private Map<String, String> vendaRomaneioSituacao;
    private HashMap<String, String> metodoTabela;

    private Map<String, String> orcamentoSituacao;
    private Map<String, String> pedidoSituacao;

    //comissao
    private HashMap<String, String> tipoContagem;
    private HashMap<String, String> formaPagamentoComissao;

    // NFe
    private Map<String, Integer> origemMercadoriaNfe;
    private Map<String, String> codigoModeloNfe;
    private Map<String, Integer> localDestinoNfe;
    private Map<String, Integer> consumidorOperacaoNfe;
    private Map<String, String> consumidorPresencaNfe;
    private Map<String, Integer> tipoOperacaoNfe;
    private Map<String, Integer> tipoEmissaoNfe;
    private Map<String, Integer> finalidadeEmissaoNfe;
    private Map<String, Integer> formatoImpressaoDanfeNfe;
    private Map<String, Integer> modalidadeFreteNfe;
    private Map<String, Integer> statusNfe;
    private HashMap<String, Integer> webserviceAmbiente;

    // OS
    private HashMap<String, Integer> osTipoCobertura;
    private HashMap<String, Integer> osTipoProdutoServico;
    private HashMap<String, Integer> osStatus;

    //Agenda
    private HashMap<String, String> categoriaCompromissoCor;
    private HashMap<String, Integer> compromissoTipo;
    private HashMap<String, Integer> compromissoRecorrente;

    //pdv
    private HashMap<String, String> pdvCodigoTipoPagamento;
    private HashMap<String, String> pdvNivelAutorizacao;


    // CTe
    private HashMap<String, Integer> cteFormatoImpressaoDacte;
    private HashMap<String, Integer> cteTipoPeriodo;
    private HashMap<String, String> cteCodigoUnidadeMedida;
    private HashMap<String, Integer> cteFormaPagamento;
    private HashMap<String, Integer> cteTipo;
    private HashMap<String, Integer> cteTipoServico;
    private HashMap<String, Integer> cteTomador;
    private HashMap<String, String> cteModeloNota;

    private HashMap<String, String> veiculoTipoPropriedade;
    private HashMap<String, Integer> veiculoTipoVeiculo;
    private HashMap<String, String> veiculoTipoRodado;
    private HashMap<String, String> veiculoTipoCarroceria;
    private HashMap<String, String> proprietarioTipo;

    private HashMap<String, String> cteModal;
    private HashMap<String, Integer> cteTipoEmissao;

    // MDFe
    private HashMap<String, Integer> mdfeTipoEmitente;
    private HashMap<String, Integer> mdfeTipoTransportadora;
    private HashMap<String, String> mdfeCodigoUnidadeMedida;
    private HashMap<String, Integer> mdfeResponsavelSeguro;

    //contabil
    private HashMap<String, String> informarContaContabil;
    private HashMap<String, String> periodicidadeInciceContabil;
    private HashMap<String, String> tipoPlanoContaSped;
    private HashMap<String, String> situacaoContaContabil;
    private HashMap<String, String> naturezaContaContabil;
    private HashMap<String, String> patrimonioResultadoContaContabil;
    private HashMap<String, String> dfcContaContabil;
    private HashMap<String, String> criterioLancamentoFechamento;
    private HashMap<String, String> tipoLancamentoProgramado;
    private HashMap<String, String> tipoLancamento;
    private HashMap<String, String> formaCalculoDre;
    private HashMap<String, String> sinalDre;
    private HashMap<String, String> contabilLivroFormaEscrituracao;
    private HashMap<String, String> aberturaFechamento;

    protected abstract Class<T> getClazz();

    protected abstract String getFuncaoBase();

    protected abstract boolean auditar();


    @PostConstruct
    public void init() {
        dataModel = new ERPLazyDataModel<>();
        dataModel.setClazz(getClazz());
        dataModel.setDao(dao);
        usuario = FacesUtil.getUsuarioSessao();
        empresa = FacesUtil.getEmpresaUsuario();
        parametro = FacesUtil.getParamentos();
        restricao = FacesUtil.getRestricao();

        atributos = new Object[]{"nome"};

        //Cadastros
        SimNao = new LinkedHashMap<>();
        SimNao.put("SIM", "S");
        SimNao.put("NÂO", "N");

        naoSim = new LinkedHashMap<>();
        naoSim.put("NÂO", "N");
        naoSim.put("SIM", "S");

        SimNaoInteiro = new HashMap<>();
        SimNaoInteiro.put("NÂO", 0);
        SimNaoInteiro.put("SIM", 1);


        tipoPessoa = new LinkedHashMap<>();
        tipoPessoa.put("Física", "F");
        tipoPessoa.put("Jurídica", "J");

        sexo = new LinkedHashMap<>();
        sexo.put("Masculino", "M");
        sexo.put("Feminino", "F");

        tipoSangue = new LinkedHashMap<>();
        tipoSangue.put("A+", "A+");
        tipoSangue.put("B+", "B+");
        tipoSangue.put("O+", "O+");
        tipoSangue.put("AB+", "AB+");
        tipoSangue.put("A-", "A-");
        tipoSangue.put("B-", "B-");
        tipoSangue.put("AB-", "AB-");
        tipoSangue.put("O-", "O-");

        racaCor = new LinkedHashMap<>();
        racaCor.put("Branco", "B");
        racaCor.put("Negro", "N");
        racaCor.put("Pardo", "P");
        racaCor.put("Indio", "I");

        tipoSindicato = new LinkedHashMap<>();
        tipoSindicato.put("Patronal", "P");
        tipoSindicato.put("Empregados", "E");

        produtoClasse = new LinkedHashMap<>();
        produtoClasse.put("A", "A");
        produtoClasse.put("B", "B");
        produtoClasse.put("C", "C");

        produtoTipo = new LinkedHashMap<>();
        produtoTipo.put("Venda", "V");
        produtoTipo.put("Composição", "C");
        produtoTipo.put("Produção", "P");
        produtoTipo.put("Insumo", "I");
        produtoTipo.put("Uso Proprio", "U");

        produtoIat = new LinkedHashMap<>();
        produtoIat.put("Arredondamento", "A");
        produtoIat.put("Truncamento", "T");

        produtoIppt = new LinkedHashMap<>();
        produtoIppt.put("Próprio", "P");
        produtoIppt.put("Terceiro", "T");

        tipoItemSped = new LinkedHashMap<>();
        tipoItemSped.put("Mercadoria para Revenda", "00");
        tipoItemSped.put("Matéria-Prima", "01");
        tipoItemSped.put("Embalagem", "02");
        tipoItemSped.put("Produto em Processo", "03");
        tipoItemSped.put("Produto Acabado", "04");
        tipoItemSped.put("Subproduto", "05");
        tipoItemSped.put("Produto Intermediário", "06");
        tipoItemSped.put("Material de Uso e Consumo", "07");
        tipoItemSped.put("Ativo Imobilizado", "08");
        tipoItemSped.put("Serviços", "09");
        tipoItemSped.put("Outros Insumos", "10");
        tipoItemSped.put("Outras", "99");

        clienteIndicadorPreco = new LinkedHashMap<>();
        clienteIndicadorPreco.put("Tabela", "T");
        clienteIndicadorPreco.put("Último Pedido", "P");

        clienteTipoFrete = new LinkedHashMap<>();
        clienteTipoFrete.put("Emitente", "E");
        clienteTipoFrete.put("Destinatario", "D");
        clienteTipoFrete.put("Sem frete", "S");
        clienteTipoFrete.put("Terceiros", "T");

        clienteFormaDesconto = new LinkedHashMap<>();
        clienteFormaDesconto.put("Produto", "P");
        clienteFormaDesconto.put("Fim do Pedido", "F");

        crt = new LinkedHashMap<>();
        crt.put("1 - Simples Nacional", "1");
        crt.put("2 - Simples Nac - Excesso", "2");
        crt.put("3 - Regime Normal", "3");

        tipoRegimeEmpresa = new LinkedHashMap<>();
        tipoRegimeEmpresa.put("Lucro Real", "1");
        tipoRegimeEmpresa.put("Lucro Presumido", "2");
        tipoRegimeEmpresa.put("SIMples Nacional", "3");

        tipoTelefone = new LinkedHashMap<>();
        tipoTelefone.put("Residencial", 0);
        tipoTelefone.put("Comercial", 1);
        tipoTelefone.put("Celular", 2);
        tipoTelefone.put("Outro", 3);

        fornecedorLocalizacao = new LinkedHashMap<>();
        fornecedorLocalizacao.put("Nacional", "N");
        fornecedorLocalizacao.put("Exterior", "E");

        colaboradorFormaPagamento = new LinkedHashMap<>();
        colaboradorFormaPagamento.put("Dinheiro", "1");
        colaboradorFormaPagamento.put("Cheque", "2");
        colaboradorFormaPagamento.put("Conta", "3");

        talonarioChequeStatus = new LinkedHashMap<>();
        talonarioChequeStatus.put("Normal", "N");
        talonarioChequeStatus.put("Cancelado", "C");
        talonarioChequeStatus.put("Extraviado", "E");
        talonarioChequeStatus.put("Utilizado", "U");

        chequeStatus = new LinkedHashMap<>();
        chequeStatus.put("Em Ser", "E");
        chequeStatus.put("Baixado", "B");
        chequeStatus.put("Utilizado", "U");
        chequeStatus.put("Compensado", "C");
        chequeStatus.put("Cancelado", "N");

        tipoContaCaixa = new LinkedHashMap<>();
        tipoContaCaixa.put("Corrente", "C");
        tipoContaCaixa.put("Poupança", "P");
        tipoContaCaixa.put("Investimento", "I");
        tipoContaCaixa.put("Caixa Interno", "X");

        tipoFeriado = new LinkedHashMap<>();
        tipoFeriado.put("Fixo", "F");
        tipoFeriado.put("Móvel", "M");

        feriadoAbrangencia = new LinkedHashMap<>();
        feriadoAbrangencia.put("Federal", "F");
        feriadoAbrangencia.put("Estadual", "E");
        feriadoAbrangencia.put("Municipal", "M");

        bandeiras = new LinkedHashMap<>();
        bandeiras.put("Visa", "01");
        bandeiras.put("Mastercard", "02");
        bandeiras.put("American Express", "03");
        bandeiras.put("Sorocred", "04");
        bandeiras.put("Outros", "99");

        //Estoque
        requisicaoInternaSituacao = new LinkedHashMap<>();
        requisicaoInternaSituacao.put("Aberta", "A");
        requisicaoInternaSituacao.put("Deferida", "D");
        requisicaoInternaSituacao.put("Indeferida", "I");

        tipoReajuste = new LinkedHashMap<>();
        tipoReajuste.put("Aumentar", "A");
        tipoReajuste.put("Diminuir", "D");

        //Financeiro
        tipoNaturazaFinanceira = new LinkedHashMap<>();
        tipoNaturazaFinanceira.put("Receita", "R");
        tipoNaturazaFinanceira.put("Despesa", "D");

        layoutRemessa = new LinkedHashMap<>();
        layoutRemessa.put("240", "240");
        layoutRemessa.put("400", "400");

        especieCobranca = new LinkedHashMap<>();
        especieCobranca.put("Duplicata Mercantil", "DM");
        especieCobranca.put("Duplicata de Serviços", "DS");
        especieCobranca.put("Recibo", "RC");
        especieCobranca.put("Nota Promissória", "NP");

        tipoBaixa = new LinkedHashMap<>();
        tipoBaixa.put("Total", "T");
        tipoBaixa.put("Parcial", "P");

        tiposPagamento = new LinkedHashMap<>();
        tiposPagamento.put("Dinheiro", "01");
        tiposPagamento.put("Cheque", "02");
        tiposPagamento.put("Cartão", "03");
        tiposPagamento.put("Debito em Conta", "04");

        //Compras
        compraSituacaoCotacao = new LinkedHashMap<>();
        compraSituacaoCotacao.put("Aberta", "A");
        compraSituacaoCotacao.put("Confirmada", "C");
        compraSituacaoCotacao.put("Fechada", "F");

        compraFormaPagamento = new LinkedHashMap<>();
        compraFormaPagamento.put("A Vista", "0");
        compraFormaPagamento.put("A Prazo", "1");
        compraFormaPagamento.put("Outros", "2");

        compraTipoFrete = new LinkedHashMap<>();
        compraTipoFrete.put("CIF", "C");
        compraTipoFrete.put("FOB", "F");


        //Vendas
        vendaOrcamentoTipo = new LinkedHashMap<>();
        vendaOrcamentoTipo.put("Orçamento", "O");
        vendaOrcamentoTipo.put("Pedido", "P");

        vendaSituacao = new LinkedHashMap<>();
        vendaSituacao.put("Digitação", "D");
        vendaSituacao.put("Encerrado", "E");
        vendaSituacao.put("Faturado", "F");
        vendaSituacao.put("Cancelada", "C");
        vendaSituacao.put("Devolvido", "DV");
        vendaSituacao.put("Dev. Parcial", "DP");


        orcamentoSituacao = new LinkedHashMap<>();
        orcamentoSituacao.put("Pendente", "P");
        orcamentoSituacao.put("Aprovado", "A");
        orcamentoSituacao.put("Não Aprovado", "N");
        orcamentoSituacao.put("Cancelado", "C");
        orcamentoSituacao.put("Digitacao", "D");
        orcamentoSituacao.put("Faturado", "F");


        formaPagamento = new LinkedHashMap<>();
        formaPagamento.put("0 - A Vista", "0");
        formaPagamento.put("1 - A Prazo", "1");
        formaPagamento.put("2 - Outros", "2");

        vendaResponsavelFrete = new LinkedHashMap<>();
        vendaResponsavelFrete.put("Emitente", "1");
        vendaResponsavelFrete.put("Destinatário", "2");

        vendaRomaneioSituacao = new LinkedHashMap<>();
        vendaRomaneioSituacao.put("Aberto", "A");
        vendaRomaneioSituacao.put("Trânsito", "T");
        vendaRomaneioSituacao.put("Encerrado", "E");

        //comissao
        formaPagamentoComissao = new HashMap<>();
        formaPagamentoComissao.put("Fixo", "0");
        formaPagamentoComissao.put("Percentual", "1");

        // tributação
        origemMercadoria = new HashMap<>();
        origemMercadoria.put("Nacional", "0");
        origemMercadoria.put("Estrangeira - Importação direta", "1");
        origemMercadoria.put("Estrangeira - Adquirida no mercado interno", "2");
        origemMercadoria.put("Nacional, mercadoria ou bem com Conteúdo de Importação superior a 40%", "3");
        origemMercadoria.put("Nacional, cuja produção tenha sido feita em conformidade com os processos produtivos básicos ", "4");
        origemMercadoria.put("Nacional, mercadoria ou bem com Conteúdo de Importação inferior ou igual a 40%", "5");
        origemMercadoria.put("Estrangeira - Importação direta, sem SIMilar nacional, constante em lista de Resolução CAMEX", "6");
        origemMercadoria.put("Estrangeira - Adquirida no mercado interno, sem SIMilar nacional, constante em lista de Resolução CAMEX", "7");
        origemMercadoria.put("Nacional, mercadoria ou bem com Conteúdo de Importação superior a 70%", "8");

        issModalidadeBaseCalculo = new HashMap<>();
        issModalidadeBaseCalculo.put("Valor Operação", "0");
        issModalidadeBaseCalculo.put("Outros", "9");

        issCodigoTributacao = new HashMap<>();
        issCodigoTributacao.put("Normal", "N");
        issCodigoTributacao.put("Retida", "R");
        issCodigoTributacao.put("Substituta", "S");
        issCodigoTributacao.put("Isenta", "I");

        tributIcmsStBaseCalculo = new HashMap<>();
        tributIcmsStBaseCalculo.put("Preço tabelado ou máximo sugerido", "0");
        tributIcmsStBaseCalculo.put("Lista Negativa (valor)", "1");
        tributIcmsStBaseCalculo.put("Lista Positiva (valor)", "2");
        tributIcmsStBaseCalculo.put("Lista Neutra (valor)", "3");
        tributIcmsStBaseCalculo.put("Margem Valor Agregado(%)", "4");
        tributIcmsStBaseCalculo.put("Pauta (valor)", "5");

        tributIcmsBaseCalculo = new HashMap<>();
        tributIcmsBaseCalculo.put("Margem Valor Agregado (%)", "0");
        tributIcmsBaseCalculo.put("Pauta (Valor)", "1");
        tributIcmsBaseCalculo.put("Preço Tabelado Máx. (valor)", "2");
        tributIcmsBaseCalculo.put("Valor da Operação", "3");

        pisModalidadeBaseCalculo = new HashMap<>();
        pisModalidadeBaseCalculo.put("Percentual", "0");
        pisModalidadeBaseCalculo.put("Unidade", "1");

        indicadorExigibilidadeIss = new HashMap<>();
        indicadorExigibilidadeIss.put("Exigível", 1);
        indicadorExigibilidadeIss.put("NÂO incidência", 2);
        indicadorExigibilidadeIss.put("Isenção", 3);
        indicadorExigibilidadeIss.put("Exportação", 4);
        indicadorExigibilidadeIss.put("Imunidade", 5);
        indicadorExigibilidadeIss.put("Exigibilidade Suspensa por Decisão Judicial", 6);
        indicadorExigibilidadeIss.put("Exigibilidade Suspensa por Processo Administrativo", 7);

        indicadorIncentivoIss = new HashMap<>();
        indicadorIncentivoIss.put("SIM", 1);
        indicadorIncentivoIss.put("NÂO", 2);


        //NFe
        codigoModeloNfe = new LinkedHashMap<>();
        codigoModeloNfe.put("NF-e", "55");
        codigoModeloNfe.put("NFC-e", "65");

        localDestinoNfe = new LinkedHashMap<>();
        localDestinoNfe.put("Operação Interna", 1);
        localDestinoNfe.put("Operação Interestadual", 2);
        localDestinoNfe.put("Operação com Exterior", 3);

        consumidorOperacaoNfe = new LinkedHashMap<>();
        consumidorOperacaoNfe.put("Normal", 0);
        consumidorOperacaoNfe.put("Consumidor Final", 1);

        consumidorPresencaNfe = new LinkedHashMap<>();
        consumidorPresencaNfe.put("Operação Presencial", "1");
        consumidorPresencaNfe.put("Operação NÂO Presencial - Internet", "2");
        consumidorPresencaNfe.put("Operação NÂO Presencial - Teleatendimento", "3");
        consumidorPresencaNfe.put("Operação NÂO Presencial - Outros", "9");
        consumidorPresencaNfe.put("NÂO se aplica", "0");

        tipoOperacaoNfe = new LinkedHashMap<>();
        tipoOperacaoNfe.put("Entrada", 0);
        tipoOperacaoNfe.put("Saída", 1);

        tipoEmissaoNfe = new LinkedHashMap<>();
        tipoEmissaoNfe.put("Normal", 1);
        tipoEmissaoNfe.put("Contigência", 2);
        tipoEmissaoNfe.put("Contingência SCAN", 3);
        tipoEmissaoNfe.put("Contingência DPEC", 4);
        tipoEmissaoNfe.put("Contingência FS-DA", 5);

        finalidadeEmissaoNfe = new LinkedHashMap<>();
        finalidadeEmissaoNfe.put("Normal", 1);
        finalidadeEmissaoNfe.put("Complementar", 2);
        finalidadeEmissaoNfe.put("Ajuste", 3);
        finalidadeEmissaoNfe.put("Devolução", 4);

        formatoImpressaoDanfeNfe = new LinkedHashMap<>();
        formatoImpressaoDanfeNfe.put("Retrato", 1);
        formatoImpressaoDanfeNfe.put("Paisagem", 1);

        modalidadeFreteNfe = new LinkedHashMap<>();
        modalidadeFreteNfe.put("Conta Emitente", 0);
        modalidadeFreteNfe.put("Conta Destinatário", 1);
        modalidadeFreteNfe.put("Conta Terceiros", 2);
        modalidadeFreteNfe.put("Sem Frete", 9);

        statusNfe = new LinkedHashMap<>();
        statusNfe.put("Em Edição", 0);
        statusNfe.put("Salva", 1);
        statusNfe.put("Validada", 2);
        statusNfe.put("Assinada", 3);
        statusNfe.put("Enviada", 4);
        statusNfe.put("Autorizada", 5);
        statusNfe.put("Cancelada", 6);
        statusNfe.put("Encerrada", 7);

        webserviceAmbiente = new HashMap<>();
        webserviceAmbiente.put("Produção", 1);
        webserviceAmbiente.put("Homologação", 2);

        // OS
        osTipoCobertura = new HashMap<>();
        osTipoCobertura.put("NENHUM", 0);
        osTipoCobertura.put("GARANTIA", 1);
        osTipoCobertura.put("SEGURO", 2);
        osTipoCobertura.put("CONTRATO", 3);

        osTipoProdutoServico = new HashMap<>();
        osTipoProdutoServico.put("PRODUTO", 0);
        osTipoProdutoServico.put("SERVIÇO", 1);

        osStatus = new HashMap<>();

        osStatus.put("PENDENTE", 1);
        osStatus.put("EM ATENDIMENTO", 2);
        osStatus.put("AGUARDANDO", 3);
        osStatus.put("AGUARDANDO APROVAÇÃO", 4);
        osStatus.put("APROVADO", 5);
        osStatus.put("REPROVADO", 6);
        osStatus.put("EM TRÂNSITO", 7);
        osStatus.put("AGUARDANDO DIAGNÓSTICO", 8);
        osStatus.put("DIAGNÓSTICO PRONTO", 9);
        osStatus.put("SEM CONSERTO", 10);
        osStatus.put("CANCELADA", 11);
        osStatus.put("ENCERRADO", 12);
        osStatus.put("FATURADO", 13);

        //Agenda
        categoriaCompromissoCor = new HashMap<>();
        categoriaCompromissoCor.put("Amarelo", "Amarelo");
        categoriaCompromissoCor.put("Azul", "Azul");
        categoriaCompromissoCor.put("Branco", "Branco");
        categoriaCompromissoCor.put("Verde", "Verde");
        categoriaCompromissoCor.put("Vermelho", "Vermelho");

        compromissoTipo = new HashMap<>();
        compromissoTipo.put("Pessoal", 0);
        compromissoTipo.put("Gerencial", 1);

        compromissoRecorrente = new HashMap<>();
        compromissoRecorrente.put("NÂO", 0);
        compromissoRecorrente.put("Diário", 1);
        compromissoRecorrente.put("Semanal", 2);
        compromissoRecorrente.put("Mensal", 3);
        compromissoRecorrente.put("Anual", 4);

        //PDV

        pdvCodigoTipoPagamento = new LinkedHashMap<>();
        pdvCodigoTipoPagamento.put("DINHEIRO", "01");
        pdvCodigoTipoPagamento.put("CHEQUE", "012");
        pdvCodigoTipoPagamento.put("CARTAO DE CREDITO", "03");
        pdvCodigoTipoPagamento.put("CARTAO DE DEBITO", "04");
        pdvCodigoTipoPagamento.put("Credito Loja", "05");
        pdvCodigoTipoPagamento.put("Vale Alimentacao", "10");
        pdvCodigoTipoPagamento.put("Vale Refeicao", "11");
        pdvCodigoTipoPagamento.put("Vale Presente", "12");
        pdvCodigoTipoPagamento.put("Vale Combustivel", "13");
        pdvCodigoTipoPagamento.put("Duplicata Mercantil", "14");
        pdvCodigoTipoPagamento.put("Boleto Bancario", "15");
        pdvCodigoTipoPagamento.put("Sem pagamento", "90");
        pdvCodigoTipoPagamento.put("Outros", "99");


        pdvNivelAutorizacao = new LinkedHashMap<>();
        pdvNivelAutorizacao.put("OPERADOR", "O");
        pdvNivelAutorizacao.put("SUPERVISOR", "S");
        pdvNivelAutorizacao.put("GERENTE", "G");
        pdvNivelAutorizacao.put("ADMINISTRADOR", "A");

        // cte
        cteFormatoImpressaoDacte = new HashMap<>();
        cteFormatoImpressaoDacte.put("Retrato", 1);
        cteFormatoImpressaoDacte.put("Paisagem", 2);

        cteTipoPeriodo = new HashMap<>();
        cteTipoPeriodo.put("Sem data definida", 0);
        cteTipoPeriodo.put("Na data", 1);
        cteTipoPeriodo.put("Até a data", 2);
        cteTipoPeriodo.put("A partir da data", 3);
        cteTipoPeriodo.put("No período", 4);

        cteCodigoUnidadeMedida = new HashMap<>();
        cteCodigoUnidadeMedida.put("M3", "00");
        cteCodigoUnidadeMedida.put("KG", "01");
        cteCodigoUnidadeMedida.put("TON", "02");
        cteCodigoUnidadeMedida.put("UNIDADE", "03");
        cteCodigoUnidadeMedida.put("LITROS", "04");
        cteCodigoUnidadeMedida.put("MMBTU", "05");

        cteFormaPagamento = new HashMap<>();
        cteFormaPagamento.put("Pago", 0);
        cteFormaPagamento.put("A pagar", 1);
        cteFormaPagamento.put("Outros", 2);

        cteTipo = new HashMap<>();
        cteTipo.put("CT-e Normal", 0);
        cteTipo.put("CT-e Complementar", 1);
        cteTipo.put("Anulação", 2);
        cteTipo.put("Substituição", 3);

        cteTipoServico = new HashMap<>();
        cteTipoServico.put("Normal", 0);
        cteTipoServico.put("Subcontratação", 1);
        cteTipoServico.put("Redespacho", 2);
        cteTipoServico.put("Redespacho  Intermediário", 3);
        cteTipoServico.put("Serviço Vinculado a  Multimodal", 4);

        mdfeTipoEmitente = new HashMap<>();
        mdfeTipoEmitente.put("Prestador de Serviço", 1);
        mdfeTipoEmitente.put("Carga Própria", 2);

        mdfeResponsavelSeguro = new HashMap<>();
        mdfeResponsavelSeguro.put("Emitente do MDF-e", 1);
        mdfeResponsavelSeguro.put("Responsável pela Contratação", 2);

        mdfeTipoTransportadora = new HashMap<>();
        mdfeTipoTransportadora.put("ETC", 1);
        mdfeTipoTransportadora.put("TAC", 2);
        mdfeTipoTransportadora.put("CTC", 3);

        mdfeCodigoUnidadeMedida = new HashMap<>();
        mdfeCodigoUnidadeMedida.put("KG", "01");
        mdfeCodigoUnidadeMedida.put("TON ", "02");

        cteTomador = new HashMap<>();
        cteTomador.put("Remetente", 0);
        cteTomador.put("Expedidor", 1);
        cteTomador.put("Recebedor", 2);
        cteTomador.put("Destinatário", 3);
        cteTomador.put("Outros", 4);

        cteModeloNota = new HashMap<>();
        cteModeloNota.put("NF Modelo 01/1A e Avulsa", "01");
        cteModeloNota.put("NF de Produtor", "04");

        cteModal = new HashMap<>();
        cteModal.put("Rodoviário", "01");
        cteModal.put("Aéreo", "02");
        cteModal.put("Aquaviário", "03");
        cteModal.put("Ferroviário", "04");
        cteModal.put("Dutoviário", "05");
        cteModal.put("Multimodal", "06");

        cteTipoEmissao = new HashMap<>();
        cteTipoEmissao.put("Normal", 1);
        cteTipoEmissao.put("EPEC pela SVC", 4);
        cteTipoEmissao.put("Contingência FSDA", 5);
        cteTipoEmissao.put("Autorização pela SVC-RS", 7);
        cteTipoEmissao.put("Autorização pela SVC-SP", 8);

        veiculoTipoPropriedade = new HashMap<>();
        veiculoTipoPropriedade.put("Próprio", "0");
        veiculoTipoPropriedade.put("Terceiro", "1");

        veiculoTipoVeiculo = new HashMap<>();
        veiculoTipoVeiculo.put("Tração", 0);
        veiculoTipoVeiculo.put("Reboque", 1);

        veiculoTipoRodado = new HashMap<>();
        veiculoTipoRodado.put("Truck", "01");
        veiculoTipoRodado.put("Toco", "02");
        veiculoTipoRodado.put("Cavalo mecânico", "03");
        veiculoTipoRodado.put("Van", "04");
        veiculoTipoRodado.put("Utilitário", "05");
        veiculoTipoRodado.put("Outros", "06");

        veiculoTipoCarroceria = new HashMap<>();
        veiculoTipoCarroceria.put("NÂO Aplicável", "00");
        veiculoTipoCarroceria.put("Aberta", "01");
        veiculoTipoCarroceria.put("Baú Fechado", "02");
        veiculoTipoCarroceria.put("Graneleira", "03");
        veiculoTipoCarroceria.put("Porta Container", "04");
        veiculoTipoCarroceria.put("Sider", "05");

        proprietarioTipo = new HashMap<>();
        proprietarioTipo.put("Agregado", "0");
        proprietarioTipo.put("Independente", "1");
        proprietarioTipo.put("Outros", "2");


        informarContaContabil = new HashMap<>();
        informarContaContabil.put("Código", "C");
        informarContaContabil.put("Máscara", "M");

        periodicidadeInciceContabil = new HashMap<>();
        periodicidadeInciceContabil.put("Diário", "D");
        periodicidadeInciceContabil.put("Mensal", "M");

        tipoPlanoContaSped = new HashMap<>();
        tipoPlanoContaSped.put("Sintética", "S");
        tipoPlanoContaSped.put("Análitica", "A");

        situacaoContaContabil = new HashMap<>();
        situacaoContaContabil.put("Ativa", "A");
        situacaoContaContabil.put("Inativa", "I");

        naturezaContaContabil = new HashMap<>();
        naturezaContaContabil.put("Credora", "C");
        naturezaContaContabil.put("Devedora", "D");

        patrimonioResultadoContaContabil = new HashMap<>();
        patrimonioResultadoContaContabil.put("Patrimônio", "P");
        patrimonioResultadoContaContabil.put("Resultado", "R");

        dfcContaContabil = new HashMap<>();
        dfcContaContabil.put("Não participa", "N");
        dfcContaContabil.put("Atividades Operacionais", "O");
        dfcContaContabil.put("Atividades de Financiamento", "F");
        dfcContaContabil.put("Atividades de Investimento", "I");

        criterioLancamentoFechamento = new HashMap<>();
        criterioLancamentoFechamento.put("Livre", "L");
        criterioLancamentoFechamento.put("Avisar", "A");
        criterioLancamentoFechamento.put("Não permitir (para lançamentos efetuados fora do período informado)", "N");

        tipoLancamentoProgramado = new HashMap<>();
        tipoLancamentoProgramado.put("Um Débito para Vários Créditos", "UDVC");
        tipoLancamentoProgramado.put("Um Crédito para Vários Débitos", "UCVD");
        tipoLancamentoProgramado.put("Um Débito para Um Crédito", "UDUC");
        tipoLancamentoProgramado.put("Vários Débitos para Vários Créditos", "VDVC");

        tipoLancamento = new HashMap<>();
        tipoLancamento.put("Crédito", "C");
        tipoLancamento.put("Débito", "D");

        formaCalculoDre = new HashMap<>();
        formaCalculoDre.put("Sintética [soma contas filhas - sinal de mais ou de menos]", "S");
        formaCalculoDre.put("Vinculada [vinculada a conta do balancete - recupera o sinal da conta mãe]", "V");
        formaCalculoDre.put("Resultado de Operações da DRE [soma das operações - sinal de igual]", "R");

        sinalDre = new HashMap<>();
        sinalDre.put("+", "+");
        sinalDre.put("-", "-");
        sinalDre.put("=", "=");

        contabilLivroFormaEscrituracao = new HashMap<>();
        contabilLivroFormaEscrituracao.put("Diário Geral", "G");
        contabilLivroFormaEscrituracao.put("Diário com Escrituração Resumida", "R");
        contabilLivroFormaEscrituracao.put("Diário Auxiliar", "A");
        contabilLivroFormaEscrituracao.put("Razão Auxiliar", "Z");
        contabilLivroFormaEscrituracao.put("Livro de Balancetes Diários e Balanços", "B");

        aberturaFechamento = new HashMap<>();
        aberturaFechamento.put("Abertura", "A");
        aberturaFechamento.put("Fechamento", "F");


    }

    public void voltar() {
        objeto = null;
        objetoSelecionado = null;
        titulo = "Consultar";
        telaGrid = true;
    }

    public void pesquisarEmpresas() {


        listaEmpresas = new ArrayList<>();

        empresasSelecionada = new ArrayList<>();
        if (usuario.getAdministrador().equals("S")) {
            List<Empresa> empresas = empresaRepository.getEntitys(Empresa.class, new Object[]{"razaoSocial"});

            if (!empresas.isEmpty() && empresas.size() > 1) {
                empresas.forEach(e -> {
                    listaEmpresas.add(e);
                });
            }


        } else {
            List<EmpresaPessoa> empresaPessoas = empresaPessoaRepository.getEntitys(EmpresaPessoa.class, "pessoa.id", usuario.getIdpessoa(), new Object[]{"empresa.id, empresa.razaoSocial"});

            if (!empresaPessoas.isEmpty() && empresaPessoas.size() > 1) {

                for (EmpresaPessoa emp : empresaPessoas) {
                    listaEmpresas.add(emp.getEmpresa());
                }

            }
        }
    }

    public List<Estados> getEstado() {
        List<Estados> estados = new LinkedList<>();
        estados.addAll(Arrays.asList(Estados.values()));
        return estados;
    }

    public String keyFromValue(HashMap map, Object value) {
        for (Object o : map.keySet()) {
            if (map.get(o).equals(value)) {
                return String.valueOf(o);
            }
        }
        return null;
    }

    public void onTabChange(final TabChangeEvent event) {
        TabView tv = (TabView) event.getComponent();
        this.activeTabIndex = tv.getActiveIndex();
    }

    public String doIndex() {
        return "/index?faces-redirect=true";
    }

    public void doCreate() {
        try {
            objeto = getClazz().newInstance();
            telaGrid = false;
            titulo = "Cadastrar";
            activeTabIndex = 0;
        } catch (InstantiationException | IllegalAccessException e) {
            Mensagem.addErrorMessage("Ocorreu um erro ao iniciar a inclusão do registro!", e);
        }

    }

    public void doEdit() {
        objeto = objetoSelecionado;
        titulo = "Alteração";
        telaGrid = false;
        activeTabIndex = 0;
    }

    public void salvar() {
        salvar("Registro salvo com sucesso!");

    }

    public void salvar(String mensagem) {
        try {
            necessarioAutorizacaoSupervisor = false;
            //verificaRestricao();

            objeto = dao.atualizar(objeto);
            telaGrid = true;
            Mensagem.addInfoMessage(mensagem);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);
        }
    }


    public void remover() {
        try {

            Integer idObjeto = null;

            if (objetoSelecionado != null) {
                idObjeto = (Integer) getClazz().getMethod("getId").invoke(objetoSelecionado);
            }

            if (objetoSelecionado == null || idObjeto == null) {
                Mensagem.addWarnMessage("Nenhum registro selecionado!");
            } else {
                dao.excluir(objetoSelecionado, idObjeto);
                Mensagem.addInfoMessage("Registro excluído com sucesso!");
            }

        } catch (DataIntegrityViolationException ex) {
            Mensagem.addErrorMessage("Ocorreu um erro ao excluir o registro! o mesmo já está em uso");
        } catch (Exception e) {
            if (e.getCause().getClass().getName().contains("PersistenceException")) {
                Mensagem.addErrorMessage("Ocorreu um erro ao excluir o registro! o mesmo já possue movimento", e);
            } else {
                e.printStackTrace();
                Mensagem.addErrorMessage("Ocorreu um erro ao excluir o registro!", e);
            }

        }
    }

    protected void gerarLog(AcaoLog acao, String conteudo, String janela) {
        try {
            Date agora = new Date();
            log = new Auditoria();
            log.setAcao(acao.getNome());
            log.setConteudo(conteudo);
            log.setDataRegistro(agora);
            log.setHoraRegistro(new SimpleDateFormat("hh:mm:ss").format(agora));
            log.setJanelaController(janela);
            log.setUsuario(new Usuario(usuario.getId()));
            auditoriaRepository.salvar(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean autorizacaoSupervisor() {
        return true;
    }


    protected List<TipoPagamento> definirTipoPagament() {
        List<TipoPagamento> pagamentos = new ArrayList<>();
        pagamentos.add(new TipoPagamento(1, "01", "DINHEIRO", "S", "N"));
        pagamentos.add(new TipoPagamento(2, "02", "CHEQUE", "N", "N"));
        pagamentos.add(new TipoPagamento(3, "03", "CARTAO DE CREDITO", "N", "N"));
        pagamentos.add(new TipoPagamento(4, "04", "CARTAO DE DEBITO", "N", "N"));
        pagamentos.add(new TipoPagamento(5, "05", "CREDITO NA LOJA", "N", "N"));
        pagamentos.add(new TipoPagamento(6, "14", "DUPLICATA", "N", "S"));

        return pagamentos;
    }


    public HashMap getListaCsosnB() {
        HashMap<String, String> csosnb = new LinkedHashMap<>();

        csosnb.put("101 - Tributada pelo SIMples Nacional com permissão de crédito", "101");
        csosnb.put("102 - Tributada pelo SIMples Nacional sem permissão de crédito", "102");
        csosnb.put("103 - Isenção do ICMS no SIMples Nacional para faixa de receita bruta", "103");
        csosnb.put("201 - Tributada pelo SIMples Nacional com permissão de crédito e com cobrança do ICMS por substituição tributária", "201");
        csosnb.put("202 - Tributada pelo SIMples Nacional sem permissão de crédito e com cobrança do ICMS por substituição tributária", "202");
        csosnb.put("203 - Isenção do ICMS no SIMples Nacional para faixa de receita bruta e com cobrança do ICMS por substituição tributária", "203");
        csosnb.put("300 - Imune", "300");
        csosnb.put("400 - NÂO tributada pelo SIMples Nacional", "400");
        csosnb.put("500 - ICMS cobrado anteriormente por substituição tributária (substituído) ou por antecipação", "500");
        csosnb.put("900 - Outros", "900");
        return csosnb;
    }

    public HashMap getListaCstB() {
        HashMap<String, String> cst = new LinkedHashMap<>();

        cst.put("00 - Tributada Integralmente", "00");
        cst.put("10 - Tributada e com Cobrança do ICMS  por Substituicao Tributária", "10");
        cst.put("20 - Com redução de Base de Calculo", "20");
        cst.put("30 - Isenta ou NÂO Tributada e com cobrança do ICMS por Substituição tributária", "30");
        cst.put("40 - Isenta", "40");
        cst.put("41 - NÂO Tributada", "41");
        cst.put("50 - Suspensão", "50");
        cst.put("51 - Direrimento", "51");
        cst.put("60 - ICMS cobrado anteriormente por substituição tributária", "60");
        cst.put("70 - Com redução de base de cálculo e cobrança do ICMS por substituicão tributária", "70");
        cst.put("90 - Outros", "90");

        return cst;
    }

    public HashMap getListaCstIpi() {
        HashMap<String, String> cst = new LinkedHashMap<>();
        cst.put("00 - Entrada com Recuperação de Crédito", "00");
        cst.put("01 - Entrada Tributável com Aliquota Zero", "01");
        cst.put("02 - Entrada Isenta", "02");
        cst.put("03 - Entrada NÂO-Tributada", "03");
        cst.put("04 - Entrada Imune", "04");
        cst.put("05 - Entrada com Suspensão", "05");
        cst.put("49 - Outras Entradas", "49");
        cst.put("50 - Saída Tributada", "50");
        cst.put("51 - Saída Tributável com aliquota Zero", "51");
        cst.put("52 - Saida Isenta", "52");
        cst.put("53 - Saida NÂO-Tributada", "53");
        cst.put("54 - Saida Imune", "54");
        cst.put("55 - Saida com Suspensão", "55");
        cst.put("99 - Outras Saídas", "99");

        return cst;
    }


    public HashMap getListaCstPis() {
        HashMap<String, String> cst = new LinkedHashMap<>();

        cst.put("01 - Operacao Tributavel com Aliquota Basica", "01");
        cst.put("02 - Operacao Tributavel com Aliquota Diferenciada", "02");
        cst.put("03 - Operacao Tributavel com Aliquota por Unidade de Medida de Produto", "03");
        cst.put("04 - Operacao Tributavel Monofasica - Revenda a Aliquota Zero", "04");
        cst.put("05 - Operacao Tributavel por Substituicao Tributaria", "05");
        cst.put("06 - Operacao Tributavel a Aliquota Zero", "06");
        cst.put("07 - Operacao Isenta da Contribuicao", "07");
        cst.put("08 - Operacao sem Incidencia da Contribuicao", "08");
        cst.put("09 - Operacao com Suspensao da Contribuicao", "09");
        cst.put("49 - Outras Operacões de Saida", "49");
        cst.put("50 - Operacao com Direito a Credito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "50");
        cst.put("51 - Operacao com Direito a Credito – Vinculada Exclusivamente a Receita Nao Tributada no Mercado Interno", "51");
        cst.put("52 - Operacao com Direito a Credito - Vinculada Exclusivamente a Receita de Exportacao", "52");
        cst.put("53 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno", "53");
        cst.put("54 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportacao", "54");
        cst.put("55 - Operacao com Direito a Credito - Vinculada a Receitas Nao-Tributadas no Mercado Interno e de Exportacao", "55");
        cst.put("56 - Operacao com Direito a Credito - Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno, e de Exportacao", "56");
        cst.put("60 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita Tributada no Mercado Interno", "60");
        cst.put("61 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita Nao-Tributada no Mercado Interno", "61");
        cst.put("62 - Credito Presumido - Operacao de Aquisicao Vinculada Exclusivamente a Receita de Exportacao", "62");
        cst.put("63 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno", "63");
        cst.put("64 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas no Mercado Interno e de Exportacao", "64");
        cst.put("65 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Nao-Tributadas no Mercado Interno e de Exportacao", "65");
        cst.put("66 - Credito Presumido - Operacao de Aquisicao Vinculada a Receitas Tributadas e Nao-Tributadas no Mercado Interno, e de Exportacao", "66");
        cst.put("67 - Credito Presumido - Outras Operacões", "67");
        cst.put("70 - Operacao de Aquisicao sem Direito a Credito", "70");
        cst.put("71 - Operacao de Aquisicao com Isencao", "71");
        cst.put("72 - Operacao de Aquisicao com Suspensao", "72");
        cst.put("73 - Operacao de Aquisicao a Aliquota Zero", "73");
        cst.put("74 - Operacao de Aquisicao sem Incidencia da Contribuicao", "74");
        cst.put("75 - Operacao de Aquisicao por Substituicao Tributaria", "75");
        cst.put("98 - Outras Operacões de Entrada", "98");
        cst.put("99 - Outras Operacões", "99");

        return cst;
    }

    public boolean podeConsultar() {
        /* return false; */
        return FacesUtil.isUserInRole(getFuncaoBase() + "_CONSULTAR")
                || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeInserir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_INSERIR") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeAlterar() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_ALTERAR") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean podeExcluir() {
        return FacesUtil.isUserInRole(getFuncaoBase() + "_EXCLUIR") || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean somenteConsulta(Object value) {
        return false;
    }

    public boolean isTemAcesso(String modulo) {
        return FacesUtil.isUserInRole(modulo);
    }

    public T getObjetoSelecionado() {
        return objetoSelecionado;
    }

    public void setObjetoSelecionado(T objetoSelecionado) {
        this.objetoSelecionado = objetoSelecionado;
    }

    public T getObjeto() {
        return objeto;
    }

    public void setObjeto(T objeto) {
        this.objeto = objeto;
    }

    public ERPLazyDataModel<T> getDataModel() {
        return dataModel;
    }

    public void setDataModel(ERPLazyDataModel<T> dataModel) {
        this.dataModel = dataModel;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public Repository<T> getDao() {
        return dao;
    }

    public void setDao(Repository<T> dao) {
        this.dao = dao;
    }

    public String getTitulo() {
        return StringUtils.isEmpty(titulo) ? "Consultar" : titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(int activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public Map<String, String> getSimNao() {
        return SimNao;
    }

    public Map<String, String> getTipoSindicato() {
        return tipoSindicato;
    }

    public Map<String, String> getProdutoClasse() {
        return produtoClasse;
    }

    public Map<String, String> getTipoItemSped() {
        return tipoItemSped;
    }

    public Map<String, String> getProdutoTipo() {
        return produtoTipo;
    }

    public Map<String, String> getProdutoIat() {
        return produtoIat;
    }

    public Map<String, String> getProdutoIppt() {
        return produtoIppt;
    }

    public Map<String, String> getClienteIndicadorPreco() {
        return clienteIndicadorPreco;
    }

    public Map<String, String> getClienteTipoFrete() {
        return clienteTipoFrete;
    }

    public Map<String, String> getClienteFormaDesconto() {
        return clienteFormaDesconto;
    }

    public Map<String, String> getSexo() {
        return sexo;
    }

    public Map<String, String> getTipoSangue() {
        return tipoSangue;
    }

    public Map<String, String> getRacaCor() {
        return racaCor;
    }

    public Map<String, String> getCrt() {
        return crt;
    }

    public Map<String, String> getTipoRegimeEmpresa() {
        return tipoRegimeEmpresa;
    }

    public Map<String, Integer> getTipoTelefone() {
        return tipoTelefone;
    }

    public Map<String, String> getTipoPessoa() {
        return tipoPessoa;
    }

    public Map<String, String> getFornecedorLocalizacao() {
        return fornecedorLocalizacao;
    }

    public Map<String, String> getColaboradorFormaPagamento() {
        return colaboradorFormaPagamento;
    }

    public void setColaboradorFormaPagamento(Map<String, String> colaboradorFormaPagamento) {
        this.colaboradorFormaPagamento = colaboradorFormaPagamento;
    }

    public Map<String, String> getTalonarioChequeStatus() {
        return talonarioChequeStatus;
    }

    public Map<String, String> getChequeStatus() {
        return chequeStatus;
    }

    public Map<String, String> getTipoContaCaixa() {
        return tipoContaCaixa;
    }

    public Map<String, String> getTipoFeriado() {
        return tipoFeriado;
    }

    public Map<String, String> getFeriadoAbrangencia() {
        return feriadoAbrangencia;
    }

    public Map<String, String> getNaoSim() {
        return naoSim;
    }

    public Map<String, String> getRequisicaoInternaSituacao() {
        return requisicaoInternaSituacao;
    }

    public Map<String, String> getTipoReajuste() {
        return tipoReajuste;
    }

    public Map<String, Integer> getOrigemMercadoriaNfe() {
        return origemMercadoriaNfe;
    }

    public Map<String, String> getCodigoModeloNfe() {
        return codigoModeloNfe;
    }

    public Map<String, Integer> getLocalDestinoNfe() {
        return localDestinoNfe;
    }

    public Map<String, Integer> getConsumidorOperacaoNfe() {
        return consumidorOperacaoNfe;
    }

    public Map<String, String> getConsumidorPresencaNfe() {
        return consumidorPresencaNfe;
    }

    public Map<String, Integer> getTipoOperacaoNfe() {
        return tipoOperacaoNfe;
    }

    public Map<String, Integer> getTipoEmissaoNfe() {
        return tipoEmissaoNfe;
    }

    public Map<String, Integer> getFinalidadeEmissaoNfe() {
        return finalidadeEmissaoNfe;
    }

    public Map<String, Integer> getFormatoImpressaoDanfeNfe() {
        return formatoImpressaoDanfeNfe;
    }

    public Map<String, Integer> getModalidadeFreteNfe() {
        return modalidadeFreteNfe;
    }

    public Map<String, Integer> getStatusNfe() {
        return statusNfe;
    }

    public Map<String, String> getTipoNaturazaFinanceira() {
        return tipoNaturazaFinanceira;
    }

    public Map<String, String> getLayoutRemessa() {
        return layoutRemessa;
    }

    public Map<String, String> getEspecieCobranca() {
        return especieCobranca;
    }

    public Map<String, String> getTipoBaixa() {
        return tipoBaixa;
    }

    public Map<String, String> getVendaOrcamentoTipo() {
        return vendaOrcamentoTipo;
    }

    public Map<String, String> getFormaPagamento() {
        return formaPagamento;
    }

    public Map<String, String> getVendaResponsavelFrete() {
        return vendaResponsavelFrete;
    }

    public Map<String, String> getVendaRomaneioSituacao() {
        return vendaRomaneioSituacao;
    }

    public Map<String, String> getCompraSituacaoCotacao() {
        return compraSituacaoCotacao;
    }

    public Map<String, String> getCompraFormaPagamento() {
        return compraFormaPagamento;
    }

    public Map<String, String> getCompraTipoFrete() {
        return compraTipoFrete;
    }

    public HashMap<String, String> getFormaPagamentoComissao() {
        return formaPagamentoComissao;
    }

    public HashMap<String, String> getOrigemMercadoria() {
        return origemMercadoria;
    }

    public HashMap<String, String> getIssModalidadeBaseCalculo() {
        return issModalidadeBaseCalculo;
    }

    public HashMap<String, String> getIssCodigoTributacao() {
        return issCodigoTributacao;
    }

    public HashMap<String, String> getTributIcmsStBaseCalculo() {
        return tributIcmsStBaseCalculo;
    }

    public HashMap<String, String> getTributIcmsBaseCalculo() {
        return tributIcmsBaseCalculo;
    }

    public HashMap<String, String> getPisModalidadeBaseCalculo() {
        return pisModalidadeBaseCalculo;
    }

    public HashMap<String, String> getTipoDepreciacao() {
        return tipoDepreciacao;
    }

    public HashMap<String, Integer> getIndicadorExigibilidadeIss() {
        return indicadorExigibilidadeIss;
    }

    public HashMap<String, Integer> getIndicadorIncentivoIss() {
        return indicadorIncentivoIss;
    }

    public HashMap<String, String> getTipoContagem() {
        return tipoContagem;
    }

    public HashMap<String, Integer> getWebserviceAmbiente() {
        return webserviceAmbiente;
    }

    public HashMap<String, Integer> getOsTipoCobertura() {
        return osTipoCobertura;
    }

    public HashMap<String, Integer> getOsTipoProdutoServico() {
        return osTipoProdutoServico;
    }


    public HashMap<String, String> getCategoriaCompromissoCor() {
        return categoriaCompromissoCor;
    }

    public HashMap<String, Integer> getCompromissoTipo() {
        return compromissoTipo;
    }

    public HashMap<String, Integer> getCompromissoRecorrente() {
        return compromissoRecorrente;
    }

    public HashMap<String, String> getPdvCodigoTipoPagamento() {
        return pdvCodigoTipoPagamento;
    }

    public HashMap<String, String> getPdvNivelAutorizacao() {
        return pdvNivelAutorizacao;
    }


    public HashMap<String, Integer> getCteFormatoImpressaoDacte() {
        return cteFormatoImpressaoDacte;
    }

    public HashMap<String, Integer> getCteTipoPeriodo() {
        return cteTipoPeriodo;
    }

    public HashMap<String, String> getCteCodigoUnidadeMedida() {
        return cteCodigoUnidadeMedida;
    }

    public HashMap<String, Integer> getCteFormaPagamento() {
        return cteFormaPagamento;
    }

    public HashMap<String, Integer> getCteTipo() {
        return cteTipo;
    }

    public HashMap<String, Integer> getCteTipoServico() {
        return cteTipoServico;
    }

    public HashMap<String, Integer> getCteTomador() {
        return cteTomador;
    }

    public HashMap<String, String> getCteModeloNota() {
        return cteModeloNota;
    }

    public HashMap<String, String> getVeiculoTipoPropriedade() {
        return veiculoTipoPropriedade;
    }

    public HashMap<String, Integer> getVeiculoTipoVeiculo() {
        return veiculoTipoVeiculo;
    }

    public HashMap<String, String> getVeiculoTipoRodado() {
        return veiculoTipoRodado;
    }

    public HashMap<String, String> getVeiculoTipoCarroceria() {
        return veiculoTipoCarroceria;
    }

    public HashMap<String, String> getProprietarioTipo() {
        return proprietarioTipo;
    }

    public HashMap<String, String> getCteModal() {
        return cteModal;
    }

    public HashMap<String, Integer> getCteTipoEmissao() {
        return cteTipoEmissao;
    }

    public HashMap<String, Integer> getMdfeTipoEmitente() {
        return mdfeTipoEmitente;
    }

    public HashMap<String, Integer> getMdfeTipoTransportadora() {
        return mdfeTipoTransportadora;
    }

    public HashMap<String, String> getMdfeCodigoUnidadeMedida() {
        return mdfeCodigoUnidadeMedida;
    }

    public HashMap<String, Integer> getMdfeResponsavelSeguro() {
        return mdfeResponsavelSeguro;
    }

    public HashMap<String, Integer> getSimNaoInteiro() {
        return SimNaoInteiro;
    }

    public HashMap<String, String> getMetodoTabela() {
        return metodoTabela;
    }

    public Map<String, String> getBandeiras() {
        return bandeiras;
    }

    public String getUsuarioSupervisor() {
        return usuarioSupervisor;
    }

    public void setUsuarioSupervisor(String usuarioSupervisor) {
        this.usuarioSupervisor = usuarioSupervisor;
    }

    public String getSenhaSupervisor() {
        return senhaSupervisor;
    }

    public void setSenhaSupervisor(String senhaSupervisor) {
        this.senhaSupervisor = senhaSupervisor;
    }

    public Map<String, String> getOrcamentoSituacao() {
        return orcamentoSituacao;
    }

    public Map<String, String> getPedidoSituacao() {
        return pedidoSituacao;
    }

    public Map<String, String> getVendaSituacao() {
        return vendaSituacao;
    }

    public HashMap<String, Integer> getOsStatus() {
        return osStatus;
    }


    public HashMap<String, String> getInformarContaContabil() {
        return informarContaContabil;
    }

    public HashMap<String, String> getPeriodicidadeInciceContabil() {
        return periodicidadeInciceContabil;
    }

    public HashMap<String, String> getTipoPlanoContaSped() {
        return tipoPlanoContaSped;
    }

    public HashMap<String, String> getSituacaoContaContabil() {
        return situacaoContaContabil;
    }

    public HashMap<String, String> getNaturezaContaContabil() {
        return naturezaContaContabil;
    }

    public HashMap<String, String> getPatrimonioResultadoContaContabil() {
        return patrimonioResultadoContaContabil;
    }

    public HashMap<String, String> getDfcContaContabil() {
        return dfcContaContabil;
    }

    public HashMap<String, String> getCriterioLancamentoFechamento() {
        return criterioLancamentoFechamento;
    }

    public HashMap<String, String> getTipoLancamentoProgramado() {
        return tipoLancamentoProgramado;
    }

    public HashMap<String, String> getTipoLancamento() {
        return tipoLancamento;
    }

    public HashMap<String, String> getFormaCalculoDre() {
        return formaCalculoDre;
    }

    public HashMap<String, String> getSinalDre() {
        return sinalDre;
    }

    public HashMap<String, String> getContabilLivroFormaEscrituracao() {
        return contabilLivroFormaEscrituracao;
    }

    public HashMap<String, String> getAberturaFechamento() {
        return aberturaFechamento;
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public List<Empresa> getEmpresasSelecionada() {
        return empresasSelecionada;
    }

    public void setEmpresasSelecionada(List<Empresa> empresasSelecionada) {
        this.empresasSelecionada = empresasSelecionada;
    }

    public Map<String, String> getTiposPagamento() {
        return tiposPagamento;
    }
}
