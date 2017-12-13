/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;

import com.chronos.modelo.entidades.Auditoria;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.Usuario;
import com.chronos.modelo.entidades.enuns.AcaoLog;
import com.chronos.modelo.entidades.enuns.Estados;
import com.chronos.repository.Repository;
import com.chronos.security.UsuarioLogado;
import com.chronos.security.UsuarioSistema;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author john
 * @param <T>
 */
public abstract class AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private T objetoSelecionado;
    private T objeto;
    private Auditoria log;
    protected ERPLazyDataModel<T> dataModel;

    private boolean telaGrid = true;
    @Inject
    protected Repository<T> dao;
    @Inject
    private Repository<Auditoria> auditoriaRepository;

    private String titulo;
    private int activeTabIndex;
    protected Usuario usuario;
    protected Empresa empresa;
    protected EmpresaEndereco enderecoEmpresa;
    protected Object[] atributos;
    protected Object[] joinFetch;

    private Map<String, String> simNao;
    private Map<String, String> naoSim;
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

    //Financeiro
    private Map<String, String> tipoNaturazaFinanceira;
    private Map<String, String> layoutRemessa;
    private Map<String, String> especieCobranca;
    private Map<String, String> tipoBaixa;

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
    private Map<String, String> vendaOrcamentoSituacao;
    private Map<String, String> formaPagamento;
    private Map<String, String> vendaResponsavelFrete;
    private Map<String, String> vendaRomaneioSituacao;

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


    protected abstract Class<T> getClazz();

    protected abstract String getFuncaoBase();

    protected abstract boolean auditar();

    @PostConstruct
    public void init() {
        dataModel = new ERPLazyDataModel<>();
        dataModel.setClazz(getClazz());
        dataModel.setDao(dao);
        usuario = getUsuarioLogado();
        empresa = getEmpresaUsuario();
        atributos = new Object[]{"nome"};

        //Cadastros
        simNao = new LinkedHashMap<>();
        simNao.put("Sim", "S");
        simNao.put("Não", "N");

        naoSim = new LinkedHashMap<>();
        naoSim.put("Não", "N");
        naoSim.put("Sim", "S");


        tipoPessoa = new LinkedHashMap<>();
        tipoPessoa.put("Fisica", "F");
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
        tipoRegimeEmpresa.put("Simples Nacional", "3");

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

        vendaOrcamentoSituacao = new LinkedHashMap<>();
        vendaOrcamentoSituacao.put("Digitacao", "D");
        vendaOrcamentoSituacao.put("Producao", "P");
        vendaOrcamentoSituacao.put("Expedicao", "X");
        vendaOrcamentoSituacao.put("Faturado", "F");
        vendaOrcamentoSituacao.put("Entregue", "E");
        vendaOrcamentoSituacao.put("Nota Emitida", "N");
        vendaOrcamentoSituacao.put("Cancelada", "C");

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
        indicadorExigibilidadeIss.put("Não incidência", 2);
        indicadorExigibilidadeIss.put("Isenção", 3);
        indicadorExigibilidadeIss.put("Exportação", 4);
        indicadorExigibilidadeIss.put("Imunidade", 5);
        indicadorExigibilidadeIss.put("Exigibilidade Suspensa por Decisão Judicial", 6);
        indicadorExigibilidadeIss.put("Exigibilidade Suspensa por Processo Administrativo", 7);

        indicadorIncentivoIss = new HashMap<>();
        indicadorIncentivoIss.put("Sim", 1);
        indicadorIncentivoIss.put("Não", 2);


        //NFe
        codigoModeloNfe = new LinkedHashMap<>();
        codigoModeloNfe.put("Nota Fiscal Eletrônica - NFe", "55");

        localDestinoNfe = new LinkedHashMap<>();
        localDestinoNfe.put("Operação Interna", 1);
        localDestinoNfe.put("Operação Interestadual", 2);
        localDestinoNfe.put("Operação com Exterior", 3);

        consumidorOperacaoNfe = new LinkedHashMap<>();
        consumidorOperacaoNfe.put("Normal", 0);
        consumidorOperacaoNfe.put("Consumidor Final", 1);

        consumidorPresencaNfe = new LinkedHashMap<>();
        consumidorPresencaNfe.put("Operação Presencial", "1");
        consumidorPresencaNfe.put("Operação não Presencial - Internet", "2");
        consumidorPresencaNfe.put("Operação não Presencial - Teleatendimento", "3");
        consumidorPresencaNfe.put("Operação não Presencial - Outros", "9");
        consumidorPresencaNfe.put("Não se aplica", "0");

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

        webserviceAmbiente = new HashMap<>();
        webserviceAmbiente.put("Produção", 1);
        webserviceAmbiente.put("Homologação", 2);

    }

    public void voltar() {
        objeto = null;
        objetoSelecionado = null;
        titulo = "Consultar";
        telaGrid = true;
    }
    
    public List<Estados> getEstado() {
        List<Estados> estados = new ArrayList<>();
        if (estados.isEmpty()) {
            estados = new LinkedList<>();
            estados.addAll(Arrays.asList(Estados.values()));
        }
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
        return "/privado/index?faces-redirect=true";
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
        salvar(null);

    }

    public void salvar(String mensagem) {
        try {
            objeto = dao.atualizar (objeto);
            telaGrid = true;
            Mensagem.addInfoMessage(mensagem != null ? mensagem : "Registro salvo com sucesso!");
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
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao excluir o registro!", e);
        }
    }

    public void gerarLog(AcaoLog acao, String conteudo, String janela) {
        try {
            Date agora = new Date();
            log = new Auditoria();
            log.setAcao(acao.getNome());
            log.setConteudo(conteudo);
            log.setDataRegistro(agora);
            log.setHoraRegistro(new SimpleDateFormat("hh:mm:ss").format(agora));
            log.setJanelaController(janela);
            log.setUsuario(getUsuarioLogado());
            auditoriaRepository.salvar(log);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Produces
    @UsuarioLogado
    public Usuario getUsuarioLogado() {
        UsuarioSistema user = null;

        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();

        if (usuario == null && auth != null && auth.getPrincipal() != null) {
            user = (UsuarioSistema) auth.getPrincipal();
            usuario = user.getUsuario();
        }

        return usuario;
    }

    public Empresa getEmpresaUsuario() {
        empresa = null;
        usuario = getUsuarioLogado();

        if (usuario != null) {
            usuario.getColaborador().getPessoa().getListaEmpresa().stream().forEach((e) -> {
                empresa = e;
            });
        }

        return empresa;
    }

    public HashMap getListaCsosnB() {
        HashMap<String, String> csosnb = new LinkedHashMap<>();

        csosnb.put("101 - Tributada pelo Simples Nacional com permissão de crédito", "101");
        csosnb.put("102 - Tributada pelo Simples Nacional sem permissão de crédito", "102");
        csosnb.put("103 - Isenção do ICMS no Simples Nacional para faixa de receita bruta", "103");
        csosnb.put("201 - Tributada pelo Simples Nacional com permissão de crédito e com cobrança do ICMS por substituição tributária", "201");
        csosnb.put("202 - Tributada pelo Simples Nacional sem permissão de crédito e com cobrança do ICMS por substituição tributária", "202");
        csosnb.put("203 - Isenção do ICMS no Simples Nacional para faixa de receita bruta e com cobrança do ICMS por substituição tributária", "203");
        csosnb.put("300 - Imune", "300");
        csosnb.put("400 - Não tributada pelo Simples Nacional", "400");
        csosnb.put("500 - ICMS cobrado anteriormente por substituição tributária (substituído) ou por antecipação", "500");
        csosnb.put("900 - Outros", "900");
        return csosnb;
    }

    public HashMap getListaCstB() {
        HashMap<String, String> cst = new LinkedHashMap<>();

        cst.put("00 - Tributada Integralmente", "00");
        cst.put("10 - Tributada e com Cobrança do ICMS  por Substituicao Tributária", "10");
        cst.put("20 - Com redução de Base de Calculo", "20");
        cst.put("30 - Isenta ou Não Tributada e com cobrança do ICMS por Substituição tributária", "30");
        cst.put("40 - Isenta", "40");
        cst.put("41 - Não Tributada", "41");
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
        cst.put("03 - Entrada Não-Tributada", "03");
        cst.put("04 - Entrada Imune", "04");
        cst.put("05 - Entrada com Suspensão", "05");
        cst.put("49 - Outras Entradas", "49");
        cst.put("50 - Saída Tributada", "50");
        cst.put("51 - Saída Tributável com aliquota Zero", "51");
        cst.put("52 - Saida Isenta", "52");
        cst.put("53 - Saida Não-Tributada", "53");
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
        // return false;
        boolean teste = FacesUtil.isUserInRole(getFuncaoBase() + "_CONSULTAR")
                || FacesUtil.isUserInRole("ADMIN");
        return teste;
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
        return simNao;
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

    public Map<String, String> getVendaOrcamentoSituacao() {
        return vendaOrcamentoSituacao;
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
}
