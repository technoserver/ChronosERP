package com.chronos.controll.estoque;

import com.chronos.bo.nfe.ImportaXMLNFe;
import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoImportacaoXml;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.FornecedorService;
import com.chronos.service.cadastros.ProdutoFornecedorService;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.estoque.EntradaNotaFiscalService;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import org.springframework.util.StringUtils;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class EntradaNotaFiscalControll extends AbstractControll<NfeCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<ProdutoMarca> marcas;
    @Inject
    private Repository<Almoxarifado> almoxarifados;
    @Inject
    private Repository<UnidadeProduto> unidades;
    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;
    @Inject
    private Repository<TributOperacaoFiscal> operacoesFiscais;
    @Inject
    private Repository<Produto> produtos;
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<NaturezaFinanceira> naturezas;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<TributGrupoTributario> grupoTributarioRepository;
    @Inject
    private Repository<VendaCondicoesParcelas> parcelasRepository;

    @Inject
    private Repository<TributOperacaoFiscal> operacoes;
    @Inject
    private Repository<VendaCondicoesPagamento> pagamentoRepository;
    @Inject
    private Repository<ContaCaixa> contaCaixaRepository;
    @Inject
    private Repository<UnidadeConversao> conversaoRepository;

    @Inject
    private EntradaNotaFiscalService entradaService;
    @Inject
    private FornecedorService fornecedorService;
    @Inject
    private ProdutoFornecedorService produtoFornecedorService;
    @Inject
    private ProdutoService produtoService;


    private NfeDetalhe nfeDetalhe;
    private NfeDetalhe nfeDetalheSelecionado;
    private NfeDetalheImpostoIcms nfeDetalheImpostoIcms;
    private NfeDetalheImpostoIpi nfeDetalheImpostoIpi;
    private NfeDetalheImpostoCofins nfeDetalheImpostoCofins;
    private NfeDetalheImpostoPis nfeDetalheImpostoPis;
    private NfeDuplicata duplicata;
    private NfeDuplicata duplicataSelecionada;

    private Produto produto;
    private UnidadeConversao unidadeConversaoSelecionada;
    private UnidadeProduto unidadeProduto;
    private Fornecedor fornecedor;
    private VendaCondicoesPagamento condicao;


    private BigDecimal valorTotalFrete;
    private BigDecimal valorTotalDesconto;
    private BigDecimal valorTotalProdutos;
    private BigDecimal valorTotalBaseCalcIcms;
    private BigDecimal valorTotalIcms;
    private BigDecimal valorTotalBaseCalcIcmsST;
    private BigDecimal valorTotalIcmsST;
    private BigDecimal valorTotalBaseCalcPis;
    private BigDecimal valorTotalPis;
    private BigDecimal valorTotalBaseCalcCofins;
    private BigDecimal valorTotalCofins;
    private BigDecimal valorTotalIpi;
    private BigDecimal valorTotalNF;
    private BigDecimal fator;


    private String acao;

    private int tipoCstIcms;
    private boolean valoresValido;
    private boolean podeIncluirProduto;


    private NaturezaFinanceira naturezaFinanceira;
    private ContaCaixa contaCaixa;

    private List<NfeDuplicata> duplicatas;

    private boolean importado;

    private String nomeFornecedor;
    private String numero;
    private Date dataInicial;
    private Date dataFinal;


    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        dataModel.setSortOrder(SortOrder.DESCENDING);
        dataModel.setOrdernarPor("dataHoraEntradaSaida");
        Object[] atribut = new Object[]{"fornecedor", "serie", "numero", "dataHoraEntradaSaida", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota"};
        dataModel.setAtributos(atribut);

        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("tipoOperacao", 0, Filtro.IGUAL);
            dataModel.addFiltro("finalidadeEmissao", 1, Filtro.IGUAL);
        }

        return dataModel;
    }


    public void pesquisar() {
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 0, Filtro.IGUAL);
        dataModel.addFiltro("finalidadeEmissao", 1, Filtro.IGUAL);


        if (dataInicial != null) {
            dataModel.addFiltro("dataHoraEntradaSaida", dataInicial, Filtro.MAIOR_OU_IGUAL);
        }

        if (dataFinal != null) {
            dataModel.addFiltro("dataHoraEntradaSaida", dataFinal, Filtro.MENOR_OU_IGUAL);
        }

        if (!StringUtils.isEmpty(numero)) {
            dataModel.addFiltro("numero", numero, Filtro.LIKE);
        }

        if (!StringUtils.isEmpty(nomeFornecedor)) {
            dataModel.addFiltro("fornecedor.pessoa.nome", nomeFornecedor, Filtro.LIKE);
        }

    }

    @Override
    public void doCreate() {
        setObjeto(entradaService.iniciar(empresa));
        setTelaGrid(false);
        iniciarValorsValidacao();
        duplicatas = new ArrayList<>();

    }


    @Override
    public void doEdit() {
        super.doEdit();
        NfeCabecalho nfe = dataModel.getRowData(getObjeto().getId().toString());
        nfe.getListaNfeDetalhe().forEach(i -> i.setProdutoCadastrado(true));
        setObjeto(nfe);
        duplicatas = getObjeto().getDuplicatas();
    }


    public void salvarEntrada() {
        try {
            getObjeto().setListaDuplicata(new HashSet<>(duplicatas));
            dao.atualizar(getObjeto());
            Mensagem.addInfoMessage("Registro salvo com sucesso");
            setTelaGrid(true);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao finzalizer ", ex);
            }
        }

    }

    public void finalizar() {
        try {
            getObjeto().setListaDuplicata(new HashSet<>(duplicatas));
            entradaService.finalizar(getObjeto(), contaCaixa, naturezaFinanceira);
            setTelaGrid(true);
            Mensagem.addInfoMessage("NF fiscal finalizada não será mais possivel fazer a edição");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao finzalizer ", ex);
            }
        }
    }


    public void validar() {

        try {
            fornecedor = getObjeto().getFornecedor();
            verificarSeExisteEntrada();

            Optional<NfeDetalhe> first = getObjeto().getListaNfeDetalhe().stream().filter(p -> !p.isProdutoCadastrado()).findFirst();

            if (first.isPresent()) {
                throw new ChronosException("Existem produtos não cadastrados");
            }

            if (!getObjeto().getListaDuplicata().isEmpty()) {


                for (NfeDuplicata duplicata : getObjeto().getListaDuplicata()) {

                    if (duplicata.getId() == null) {
                        if (contaCaixa == null) {
                            throw new ChronosException("Para geração de contas a pagar é preciso informa uma conta caixa");
                        }

                        if (naturezaFinanceira == null) {
                            throw new ChronosException("Para geração de contas a pagar é preciso informa uma natureza financeira");
                        }
                    }
                }


            }

            iniciarValorsValidacao();
            calcularTotais();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao validar entrada");
            }
        }

    }

    public void gerarValores(NfeDetalhe item) {
        item.calcularValorTotalProduto();
        valorTotalFrete = Biblioteca.soma(valorTotalFrete, item.getValorFrete());
        valorTotalDesconto = Biblioteca.soma(valorTotalDesconto, item.getValorDesconto());
        valorTotalProdutos = Biblioteca.soma(valorTotalProdutos, item.getValorSubtotal());

        valorTotalNF = Biblioteca.soma(valorTotalNF, item.getValorTotal());

        calcularValorSobreImposto(item);
    }



    public void calcularTotais() {

        getObjeto().calcularValorTotal();

        valorTotalFrete = getObjeto().getValorFrete();
        valorTotalDesconto = getObjeto().getValorDesconto();
        valorTotalProdutos = getObjeto().getValorTotalProdutos();

        valorTotalNF = getObjeto().getListaNfeDetalhe()
                .stream()
                .map(NfeDetalhe::getValorTotal)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        getObjeto().getListaNfeDetalhe().forEach(item -> {
            item.calcularValorTotalProduto();
            calcularValorSobreImposto(item);
        });

    }

    private void calcularValorSobreImposto(NfeDetalhe item) {

        if (item.getNfeDetalheImpostoIcms() != null) {
            valorTotalBaseCalcIcms = Biblioteca.soma(valorTotalBaseCalcIcms, item.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
            valorTotalIcms = Biblioteca.soma(valorTotalIcms, item.getNfeDetalheImpostoIcms().getValorIcms());
            valorTotalBaseCalcIcmsST = Biblioteca.soma(valorTotalBaseCalcIcmsST, item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
            valorTotalIcmsST = Biblioteca.soma(valorTotalIcmsST, item.getNfeDetalheImpostoIcms().getValorIcmsSt());
            valorTotalNF = Biblioteca.soma(valorTotalNF, item.getNfeDetalheImpostoIcms().getValorIcmsSt());
        }
        if (item.getNfeDetalheImpostoIpi() != null) {
            valorTotalIpi = Biblioteca.soma(valorTotalIpi, item.getNfeDetalheImpostoIpi().getValorIpi());
            valorTotalNF = Biblioteca.soma(valorTotalNF, item.getNfeDetalheImpostoIpi().getValorIpi());
        }
        if (item.getNfeDetalheImpostoPis() != null) {
            valorTotalBaseCalcPis = Biblioteca.soma(valorTotalBaseCalcPis, item.getNfeDetalheImpostoPis().getValorBaseCalculoPis());
            valorTotalPis = Biblioteca.soma(valorTotalPis, item.getNfeDetalheImpostoPis().getValorPis());
        }
        if (item.getNfeDetalheImpostoPis() != null) {
            valorTotalBaseCalcCofins = Biblioteca.soma(valorTotalBaseCalcCofins, item.getNfeDetalheImpostoCofins().getBaseCalculoCofins());
            valorTotalCofins = Biblioteca.soma(valorTotalCofins, item.getNfeDetalheImpostoCofins().getValorCofins());
        }
    }

    private void cadastrarFornecedor(NfeEmitente emitente) throws Exception {
        fornecedor = fornecedorService.cadastrarFornecedor(emitente, empresa);
        getObjeto().setFornecedor(fornecedor);
    }


    private void iniciarValorsValidacao() {
        valorTotalFrete = BigDecimal.ZERO;
        valorTotalDesconto = BigDecimal.ZERO;
        valorTotalProdutos = BigDecimal.ZERO;
        valorTotalBaseCalcIcms = BigDecimal.ZERO;
        valorTotalIcms = BigDecimal.ZERO;
        valorTotalBaseCalcIcmsST = BigDecimal.ZERO;
        valorTotalIcmsST = BigDecimal.ZERO;
        valorTotalBaseCalcPis = BigDecimal.ZERO;
        valorTotalPis = BigDecimal.ZERO;
        valorTotalBaseCalcCofins = BigDecimal.ZERO;
        valorTotalCofins = BigDecimal.ZERO;
        valorTotalIpi = BigDecimal.ZERO;
        valorTotalNF = BigDecimal.ZERO;

    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos para importação do xml">

    public void importaXML(FileUploadEvent event) {
        try {
            UploadedFile arquivoUpload = event.getFile();

            File file = File.createTempFile("nfe", ".xml");
            FileUtils.copyInputStreamToFile(arquivoUpload.getInputstream(), file);
            iniciarValorsValidacao();
            if (file != null) {
                ImportaXMLNFe importaXml = new ImportaXMLNFe(TipoImportacaoXml.ENTRADA);
                Map map = importaXml.importarXmlNFe(file);
                // tipoDoc = "NFE";
                setObjeto((NfeCabecalho) map.get("cabecalho"));
                getObjeto().setEmpresa(empresa);
                getObjeto().setEmitente((NfeEmitente) map.get("emitente"));

                if (!getObjeto().getDestinatario().getCpfCnpj().equals(empresa.getCnpj()) && !Constantes.DESENVOLVIMENTO) {
                    throw new ChronosException("NF-e não emitida pra o cnpj da empresa");
                }

                getObjeto().setListaNfeDetalhe((ArrayList) map.get("detalhe"));

                getObjeto().setListaDuplicata((HashSet) map.get("duplicata"));
                getObjeto().setDataHoraEntradaSaida(new Date());
                verificarFornecedor();
                verificarSeExisteEntrada();
                verificaProdutoNaoCadastrado(true);
                getObjeto().setDataHoraEntradaSaida(new Date());
                importado = true;
                duplicatas = getObjeto().getDuplicatas();
                Mensagem.addInfoMessage("XML importados com sucesso!");
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao importat XML", ex);
            }

        }
    }

    private void verificarSeExisteEntrada() throws ChronosException {
        List<Filtro> filtro = new LinkedList<>();
        filtro.add(new Filtro(Filtro.AND, "fornecedor.id", Filtro.IGUAL, fornecedor.getId()));
        filtro.add(new Filtro(Filtro.AND, "numero", Filtro.IGUAL, getObjeto().getNumero()));

        NfeCabecalho nfe = dao.get(NfeCabecalho.class, filtro, new Object[]{"numero"});
        if (nfe != null && !nfe.equals(getObjeto())) {
            doCreate();
            throw new ChronosException("Essa nota já foi  digitada pra esse fornecedor !");
        }
    }


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Crud Fornecedor">

    private void verificarFornecedor() throws Exception {


        String cpfCnpj = getObjeto().getEmitente().getCpfCnpj();
        fornecedor = fornecedorService.getFornecedor(cpfCnpj);
        if (fornecedor == null) {
            cadastrarFornecedor(getObjeto().getEmitente());
            getObjeto().setFornecedor(fornecedor);
        } else {
            getObjeto().setFornecedor(fornecedor);
        }

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Crud Produto">
    public void incluiProduto() {
        if (getObjeto().getTributOperacaoFiscal() == null) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("Antes de incluir produtos selecione a Operação Fiscal.");

        } else if (StringUtils.isEmpty(empresa.getCrt())) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("CRT da empresa não definido.");
        } else {
            tipoCstIcms = Integer.valueOf(empresa.getCrt());
            nfeDetalhe = new NfeDetalhe();

            nfeDetalhe.setNfeCabecalho(getObjeto());

            nfeDetalhe.setCfop(getObjeto().getTributOperacaoFiscal().getCfop());

            if (getObjeto().getTributOperacaoFiscal().getObrigacaoFiscal()) {
                nfeDetalheImpostoIcms = new NfeDetalheImpostoIcms();

                nfeDetalheImpostoIcms.setNfeDetalhe(nfeDetalhe);
                nfeDetalheImpostoIcms.setBaseCalculoIcms(BigDecimal.ZERO);
                nfeDetalheImpostoIcms.setValorIcms(BigDecimal.ZERO);
                nfeDetalheImpostoIcms.setAliquotaIcms(BigDecimal.ZERO);
                nfeDetalheImpostoIcms.setValorBaseCalculoIcmsSt(BigDecimal.ZERO);
                nfeDetalheImpostoIcms.setValorIcmsSt(BigDecimal.ZERO);
                nfeDetalheImpostoIcms.setAliquotaIcmsSt(BigDecimal.ZERO);
            }

            if (getObjeto().getTributOperacaoFiscal().getDestacaIpi()) {
                nfeDetalheImpostoIpi = new NfeDetalheImpostoIpi();

                nfeDetalheImpostoIpi.setNfeDetalhe(nfeDetalhe);
                nfeDetalheImpostoIpi.setAliquotaIpi(BigDecimal.ZERO);
                nfeDetalheImpostoIpi.setEnquadramentoIpi("99");
                nfeDetalheImpostoIpi.setValorBaseCalculoIpi(BigDecimal.ZERO);
                nfeDetalheImpostoIpi.setValorIpi(BigDecimal.ZERO);
            }

            if (getObjeto().getTributOperacaoFiscal().getDestacaPisCofins()) {
                nfeDetalheImpostoCofins = new NfeDetalheImpostoCofins();
                nfeDetalheImpostoPis = new NfeDetalheImpostoPis();

                nfeDetalheImpostoPis.setNfeDetalhe(nfeDetalhe);
                nfeDetalheImpostoPis.setAliquotaPisPercentual(BigDecimal.ZERO);
                nfeDetalheImpostoPis.setValorBaseCalculoPis(BigDecimal.ZERO);
                nfeDetalheImpostoPis.setValorPis(BigDecimal.ZERO);

                nfeDetalheImpostoCofins.setNfeDetalhe(nfeDetalhe);
                nfeDetalheImpostoCofins.setAliquotaCofinsPercentual(BigDecimal.ZERO);
                nfeDetalheImpostoCofins.setBaseCalculoCofins(BigDecimal.ZERO);
                nfeDetalheImpostoCofins.setValorCofins(BigDecimal.ZERO);
            }


            nfeDetalhe.setNfeDetalheImpostoCofins(nfeDetalheImpostoCofins);
            nfeDetalhe.setNfeDetalheImpostoIcms(nfeDetalheImpostoIcms);
            nfeDetalhe.setNfeDetalheImpostoIpi(nfeDetalheImpostoIpi);
            nfeDetalhe.setNfeDetalheImpostoPis(nfeDetalheImpostoPis);


            podeIncluirProduto = true;
        }
    }

    public void alteraProduto() {
        nfeDetalhe = nfeDetalheSelecionado;
        nfeDetalheImpostoIcms = nfeDetalhe.getNfeDetalheImpostoIcms();
        nfeDetalheImpostoIpi = nfeDetalhe.getNfeDetalheImpostoIpi();
        nfeDetalheImpostoCofins = nfeDetalhe.getNfeDetalheImpostoCofins();
        nfeDetalheImpostoPis = nfeDetalhe.getNfeDetalheImpostoPis();
        podeIncluirProduto = true;
    }

    public void excluirProduto() {

        getObjeto().getListaNfeDetalhe().remove(nfeDetalheSelecionado);
        iniciarValorsValidacao();
        getObjeto().getListaNfeDetalhe().stream().forEach(item -> {
            gerarValores(item);
        });
        Mensagem.addInfoMessage("Registro excluido!");

    }

    public void salvaProduto() {
        try {

            if ((nfeDetalhe.getQuantidadeComercial() == null || nfeDetalhe.getQuantidadeComercial().signum() <= 0) ||
                    (nfeDetalhe.getValorUnitarioComercial() == null || nfeDetalhe.getValorUnitarioComercial().signum() <= 0)) {
                throw new ChronosException("Valores invalido");

            }

            Optional<NfeDetalhe> itemNfeOptional = buscarItemPorProduto(nfeDetalhe.getProduto());
            if (!itemNfeOptional.isPresent()) {
                nfeDetalhe.setValorSubtotal(nfeDetalhe.calcularSubTotalProduto());
                nfeDetalhe.setValorTotal(nfeDetalhe.calcularValorTotalProduto());
                nfeDetalhe.setCodigoProduto(nfeDetalhe.getProduto().getId().toString());
                nfeDetalhe.setNomeProduto(nfeDetalhe.getProduto().getNome());
                nfeDetalhe.setNcm(nfeDetalhe.getProduto().getNcm());
                nfeDetalhe.setCest(nfeDetalhe.getProduto().getCest());
                nfeDetalhe.setUnidadeComercial(nfeDetalhe.getProduto().getUnidadeProduto().getSigla());
                nfeDetalhe.setValorBrutoProduto(nfeDetalhe.getValorUnitarioComercial());
                nfeDetalhe.setUnidadeTributavel(nfeDetalhe.getUnidadeComercial());
                nfeDetalhe.setValorUnitarioTributavel(nfeDetalhe.getValorUnitarioComercial());
                nfeDetalhe.setProdutoCadastrado(true);
                getObjeto().getListaNfeDetalhe().add(nfeDetalhe);


                Mensagem.addInfoMessage("Produto salvo!");
            }
            nfeDetalhe.setNomeProduto(nfeDetalhe.getProduto().getNome());
            gerarValores(nfeDetalhe);
            nfeDetalhe.calcularValorTotalProduto();
            calcularTotais();

            podeIncluirProduto = false;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().validationFailed();
            if (e instanceof ChronosException) {
                Mensagem.addErrorMessage("", e);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o produto", e);
            }


        }
    }

    private Optional<NfeDetalhe> buscarItemPorProduto(Produto produto) {
        return getObjeto().getListaNfeDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    public void cadastrarProduto() {
        produto = new Produto();
        nfeDetalhe = nfeDetalheSelecionado;
        produto.setGtin(nfeDetalhe.getGtin());
        produto.setDataCadastro(new Date());
        produto.setCustoUnitario(nfeDetalhe.getValorUnitarioComercial());
        produto.setDescricao(nfeDetalhe.getNomeProduto());
        produto.setNcm(nfeDetalhe.getNcm());
        produto.setCest(nfeDetalhe.getCest());
        produto.setNome(nfeDetalhe.getNomeProduto());
        produto.setValorCompra(nfeDetalhe.getValorUnitarioComercial());
        produto.setExcluido("N");
        produto.setInativo("N");
        produto.setServico("N");
        unidadeProduto = null;
        fator = BigDecimal.ZERO;

        UnidadeProduto sigla = unidades.get(UnidadeProduto.class, "sigla", nfeDetalhe.getUnidadeComercial(), new Object[]{"sigla"});

        if (sigla != null) {
            produto.setUnidadeProduto(sigla);
        }
    }

    public void exibirVinculoProduto() {
        produto = new Produto();
        nfeDetalhe = nfeDetalheSelecionado;
    }

    public void vincularProduto() {
        try {
            salvarNovoProduto();
            Mensagem.addInfoMessage("Produto vinculado com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao vincular o produto", ex);
            }


        }
    }

    public void salvarNovoProduto() {
        try {

            if (produto.getTipo().equals("V") && (produto.getValorVenda() == null || produto.getValorVenda().signum() <= 0)) {
                FacesContext.getCurrentInstance().validationFailed();
                throw new ChronosException("Valor de venda orbigatório");
            } else {

                if (unidadeProduto != null) {
                    produto = produtoService.addConversaoUnidade(produto, unidadeProduto, fator, acao);
                }

                FornecedorProduto forProd = produtoFornecedorService.salvar(produto, fornecedor, empresa, nfeDetalhe.getValorUnitarioComercial(), nfeDetalhe.getCodigoProduto());
                nfeDetalhe.setProduto(forProd.getProduto());
                nfeDetalhe.setNomeProduto(forProd.getProduto().getNome());
                nfeDetalhe.setProdutoCadastrado(true);


                if (forProd.getProduto().getUnidadeConversao() != null) {
                    definirQuantidadeConvertida(nfeDetalhe, forProd.getProduto().getUnidadeConversao());
                }

                unidadeProduto = null;
                fator = BigDecimal.ZERO;
                Mensagem.addInfoMessage("Produto cadastro e vinculado com sucesso");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                FacesContext.getCurrentInstance().validationFailed();
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao cadastra o produto", ex);
            }


        }


    }

    private boolean verificaProdutoNaoCadastrado(boolean importacao) {
        boolean produtoNaoCadastrado = false;

        try {
            if (importacao) {
                for (NfeDetalhe d : getObjeto().getListaNfeDetalhe()) {
                    boolean existeProduto = !StringUtils.isEmpty(d.getGtin()) && produtos.existeRegisro(Produto.class, "gtin", d.getGtin());
                    boolean existeProdutoFornecedor = produtoFornecedorService.existeProduto(d.getCodigoProduto());

                    if (existeProduto) {
                        Produto produto = produtos.get(Produto.class, "gtin", d.getGtin(), new Object[]{"nome"});
                        d.setProduto(produto);
                        d.setProdutoCadastrado(true);

                        if (!existeProdutoFornecedor) {
                            produtoFornecedorService.salvar(produto, fornecedor, empresa, d.getValorUnitarioComercial(), d.getCodigoProduto());
                        }

                        UnidadeConversao unidadeConversao = conversaoRepository.get(UnidadeConversao.class, "produto.id", produto.getId());


                        if (unidadeConversao != null) {
                            definirQuantidadeConvertida(d, unidadeConversao);
                        }


                    } else if (existeProdutoFornecedor) {
                        Produto produto = produtoFornecedorService.getProduto(d.getCodigoProduto());
                        d.setProduto(produto);
                        d.setProdutoCadastrado(true);
                    } else {
                        d.setProdutoCadastrado(false);
                    }


                    gerarValores(d);

                }
            } else {
                for (NfeDetalhe item : getObjeto().getListaNfeDetalhe()) {
                    int id = item.getProduto() == null || item.getProduto().getId() == null ? 0 : item.getProduto().getId();
                    produtoNaoCadastrado = true;//!produtoDao.existeRegisro(Produto.class, "id", id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os dados do produto.", e);
        }
        return produtoNaoCadastrado;
    }


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Crud Duplicatas">
    public void incluirDuplicata() {

        duplicata = new NfeDuplicata();
        duplicata.setValor(getObjeto().getValorTotal());

        if (naturezaFinanceira == null) {
            FacesContext.getCurrentInstance().validationFailed();
            Mensagem.addErrorMessage("É preciso seleciona a natureza financeira !!!");
        } else if (contaCaixa == null) {
            FacesContext.getCurrentInstance().validationFailed();
            Mensagem.addErrorMessage("É preciso seleciona a conta caixa !!!");
        } else if (StringUtils.isEmpty(getObjeto().getNumero())) {
            FacesContext.getCurrentInstance().validationFailed();
            Mensagem.addErrorMessage("Numero da NFe não definido !!!");
        } else if (getObjeto().getValorTotal() == null || getObjeto().getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            FacesContext.getCurrentInstance().validationFailed();
            Mensagem.addErrorMessage("É preciso informar o valor total!!!");
        } else {
            PrimeFaces.current().executeScript("PF('dialogDuplicata').show()");
        }

    }

    public void alterarDuplicata() {
        duplicata = duplicataSelecionada;
    }

    public void salvarDuplicata() {
        try {
            if (condicao != null) {
                duplicatas.clear();
                gerarDuplicatas();
            } else {
                BigDecimal valorTotalDuplicata = BigDecimal.ZERO;
                for (NfeDuplicata d : duplicatas) {
                    valorTotalDuplicata = Biblioteca.soma(valorTotalDuplicata, d.getValor());
                }
                valorTotalDuplicata = Biblioteca.soma(valorTotalDuplicata, duplicata.getValor());
                if (valorTotalDuplicata.compareTo(getObjeto().getValorTotal()) > 0) {
                    Mensagem.addInfoMessage("Valor acima do valor total da nota");
                } else if (!duplicatas.contains(duplicata)) {
                    Mensagem.addInfoMessage("Registro alterado!");
                    duplicatas.add(duplicata);
                } else {
                    duplicatas.add(duplicata);
                    int i = 0;

                    for (NfeDuplicata d : duplicatas) {
                        i++;
                        d.setNumero(getObjeto().getNumero() + "/" + i);
                    }
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    private void gerarDuplicatas() throws Exception {
        getObjeto().setIndicadorFormaPagamento(1);

        int numero = 0;
        if (condicao == null || condicao.getId() == null) {
            throw new Exception("Condição de pagamento não definida");
        }

        List<VendaCondicoesParcelas> parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicao.getId());
        condicao.setParcelas(parcelas);
        for (VendaCondicoesParcelas p : condicao.getParcelas()) {

            numero++;

            NfeDuplicata d = new NfeDuplicata();
            d.setContaCaixa(contaCaixa);
            d.setNfeCabecalho(getObjeto());
            d.setNumero(Integer.valueOf(getObjeto().getNumero()) + "/" + String.valueOf(numero));
            d.setValor(Biblioteca.multiplica(getObjeto().getValorTotal(), p.getTaxa()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
            d.setDataVencimento(Biblioteca.dataPagamento(p.getDias()));

            duplicatas.add(d);
        }
        getObjeto().setListaDuplicata(new HashSet<>(duplicatas));

    }

    public void removerDuplicata() {
        duplicatas.remove(duplicataSelecionada);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Diversos">

    public boolean validarValor(BigDecimal valor1, BigDecimal valor2) {
        if (valor1 == null || valor2 == null) {
            return true;
        }
        valoresValido = valor1.compareTo(valor2) == 0;
        return valoresValido;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Buscas">


    public List<TributGrupoTributario> getListaGrupoTributario(String nome) {
        List<TributGrupoTributario> listaGrupoTributario = new ArrayList<>();
        try {
            listaGrupoTributario = grupoTributarioRepository.getEntitys(TributGrupoTributario.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupoTributario;
    }


    public List<VendaCondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            listaVendaCondicoesPagamento = condicoes.getEntitys(VendaCondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaCondicoesPagamento;
    }

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto = new ArrayList<>();

        try {
            listaProduto = produtos.getEntitys(Produto.class, "descricaoPdv", descricao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public List<ProdutoSubGrupo> getListaSubGrupo(String nome) {
        List<ProdutoSubGrupo> listaProdutoSubGrupo = new ArrayList<>();

        try {
            listaProdutoSubGrupo = subGrupos.getEntitys(ProdutoSubGrupo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProdutoSubGrupo;
    }

    public List<UnidadeProduto> getListaUnidadeProduto(String sigla) {
        List<UnidadeProduto> listaUnidadeProduto = new ArrayList<>();

        try {
            listaUnidadeProduto = unidades.getEntitys(UnidadeProduto.class, "sigla", sigla);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaUnidadeProduto;
    }

    public List<Almoxarifado> getListaAlmoxarifado(String nome) {
        List<Almoxarifado> listaAlmoxarifado = new ArrayList<>();

        try {
            listaAlmoxarifado = almoxarifados.getEntitys(Almoxarifado.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAlmoxarifado;
    }

    public List<ProdutoMarca> getListaProdutoMarca(String nome) {
        List<ProdutoMarca> listaProdutoMarca = new ArrayList<>();

        try {
            listaProdutoMarca = marcas.getEntitys(ProdutoMarca.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProdutoMarca;
    }

    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();

        try {
            listaFornecedor = fornecedores.getEntitys(Fornecedor.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFornecedor;
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("descricao", Filtro.LIKE, descricao));
            filtros.add(new Filtro("cfop", Filtro.MENOR, 3000));
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, filtros, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoInss", "estoque", "estoqueVerificado"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<NaturezaFinanceira> getListaNaturezaFinanceira(String nome) {
        List<NaturezaFinanceira> listaNaturezaFinanceira = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaNaturezaFinanceira = naturezas.getEntitys(NaturezaFinanceira.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaNaturezaFinanceira;
    }


    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listaContaCaixa = new ArrayList<>();
        try {
            atributos = new Object[]{"nome"};
            listaContaCaixa = contaCaixaRepository.getEntitys(ContaCaixa.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaContaCaixa;
    }

    // </editor-fold>


    private void validarDados() throws Exception {

        if (getObjeto().getListaNfeDetalhe().isEmpty()) {
            throw new Exception("Itens da NFe não informado");
        }

        for (NfeDetalhe item : getObjeto().getListaNfeDetalhe()) {

            if (item.getCfop() == null) {
                throw new Exception("Para o item " + item.getNomeProduto() + " não foi informado CFOP");
            }
            if (item.getNfeDetalheImpostoIcms() != null) {
                if (StringUtils.isEmpty(item.getNfeDetalheImpostoIcms().getCstIcms()) && StringUtils.isEmpty(item.getNfeDetalheImpostoIcms().getCsosn())) {
                    throw new Exception("Para o item " + item.getNomeProduto() + " não foi informado CST/CSOSN");
                }
            }
        }

    }

    private void definirQuantidadeConvertida(NfeDetalhe d, UnidadeConversao unidadeConversao) {
        d.setUnidadeComercial(unidadeConversao.getProduto().getUnidadeProduto().getSigla());

        BigDecimal quantidade = unidadeConversao.getAcao().equals("M")
                ? Biblioteca.multiplica(d.getQuantidadeComercial(), unidadeConversao.getFatorConversao())
                : Biblioteca.divide(d.getQuantidadeComercial(), unidadeConversao.getFatorConversao());

        BigDecimal valorTotal = d.getValorSubtotal();
        BigDecimal valorUnt = valorTotal.divide(quantidade, MathContext.DECIMAL64).setScale(5, RoundingMode.HALF_DOWN);
        valorUnt = valorUnt.setScale(3, RoundingMode.DOWN);
        d.setQuantidadeComercial(quantidade);
        d.setValorUnitarioComercial(valorUnt);
    }

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE_ENTRADA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    @Override
    public boolean somenteConsulta(Object value) {
        NfeCabecalho nfe = (NfeCabecalho) value;
        return nfe.getStatusNota().equals(StatusTransmissao.ENCERRADO.getCodigo());
    }

    public VendaCondicoesPagamento getCondicao() {
        return condicao;
    }

    public void setCondicao(VendaCondicoesPagamento condicao) {
        this.condicao = condicao;
    }

    public NfeDuplicata getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(NfeDuplicata duplicata) {
        this.duplicata = duplicata;
    }

    public NfeDuplicata getDuplicataSelecionada() {
        return duplicataSelecionada;
    }

    public void setDuplicataSelecionada(NfeDuplicata duplicataSelecionada) {
        this.duplicataSelecionada = duplicataSelecionada;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public BigDecimal getValorTotalFrete() {
        return valorTotalFrete;
    }

    public void setValorTotalFrete(BigDecimal valorTotalFrete) {
        this.valorTotalFrete = valorTotalFrete;
    }

    public BigDecimal getValorTotalDesconto() {
        return valorTotalDesconto;
    }

    public void setValorTotalDesconto(BigDecimal valorTotalDesconto) {
        this.valorTotalDesconto = valorTotalDesconto;
    }

    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }

    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }

    public BigDecimal getValorTotalBaseCalcIcms() {
        return valorTotalBaseCalcIcms;
    }

    public void setValorTotalBaseCalcIcms(BigDecimal valorTotalBaseCalcIcms) {
        this.valorTotalBaseCalcIcms = valorTotalBaseCalcIcms;
    }

    public BigDecimal getValorTotalIcms() {
        return valorTotalIcms;
    }

    public void setValorTotalIcms(BigDecimal valorTotalIcms) {
        this.valorTotalIcms = valorTotalIcms;
    }

    public BigDecimal getValorTotalBaseCalcIcmsST() {
        return valorTotalBaseCalcIcmsST;
    }

    public void setValorTotalBaseCalcIcmsST(BigDecimal valorTotalBaseCalcIcmsST) {
        this.valorTotalBaseCalcIcmsST = valorTotalBaseCalcIcmsST;
    }

    public BigDecimal getValorTotalIcmsST() {
        return valorTotalIcmsST;
    }

    public void setValorTotalIcmsST(BigDecimal valorTotalIcmsST) {
        this.valorTotalIcmsST = valorTotalIcmsST;
    }

    public BigDecimal getValorTotalBaseCalcPis() {
        return valorTotalBaseCalcPis;
    }

    public void setValorTotalBaseCalcPis(BigDecimal valorTotalBaseCalcPis) {
        this.valorTotalBaseCalcPis = valorTotalBaseCalcPis;
    }

    public BigDecimal getValorTotalPis() {
        return valorTotalPis;
    }

    public void setValorTotalPis(BigDecimal valorTotalPis) {
        this.valorTotalPis = valorTotalPis;
    }

    public BigDecimal getValorTotalBaseCalcCofins() {
        return valorTotalBaseCalcCofins;
    }

    public void setValorTotalBaseCalcCofins(BigDecimal valorTotalBaseCalcCofins) {
        this.valorTotalBaseCalcCofins = valorTotalBaseCalcCofins;
    }

    public BigDecimal getValorTotalCofins() {
        return valorTotalCofins;
    }

    public void setValorTotalCofins(BigDecimal valorTotalCofins) {
        this.valorTotalCofins = valorTotalCofins;
    }

    public BigDecimal getValorTotalIpi() {
        return valorTotalIpi;
    }

    public void setValorTotalIpi(BigDecimal valorTotalIpi) {
        this.valorTotalIpi = valorTotalIpi;
    }

    public BigDecimal getValorTotalNF() {
        return valorTotalNF;
    }

    public void setValorTotalNF(BigDecimal valorTotalNF) {
        this.valorTotalNF = valorTotalNF;
    }

    public NfeDetalheImpostoIcms getNfeDetalheImpostoIcms() {
        return nfeDetalheImpostoIcms;
    }

    public void setNfeDetalheImpostoIcms(NfeDetalheImpostoIcms nfeDetalheImpostoIcms) {
        this.nfeDetalheImpostoIcms = nfeDetalheImpostoIcms;
    }

    public NfeDetalheImpostoIpi getNfeDetalheImpostoIpi() {
        return nfeDetalheImpostoIpi;
    }

    public void setNfeDetalheImpostoIpi(NfeDetalheImpostoIpi nfeDetalheImpostoIpi) {
        this.nfeDetalheImpostoIpi = nfeDetalheImpostoIpi;
    }

    public NfeDetalheImpostoCofins getNfeDetalheImpostoCofins() {
        return nfeDetalheImpostoCofins;
    }

    public void setNfeDetalheImpostoCofins(NfeDetalheImpostoCofins nfeDetalheImpostoCofins) {
        this.nfeDetalheImpostoCofins = nfeDetalheImpostoCofins;
    }

    public NfeDetalheImpostoPis getNfeDetalheImpostoPis() {
        return nfeDetalheImpostoPis;
    }

    public void setNfeDetalheImpostoPis(NfeDetalheImpostoPis nfeDetalheImpostoPis) {
        this.nfeDetalheImpostoPis = nfeDetalheImpostoPis;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public NfeDetalhe getNfeDetalheSelecionado() {
        return nfeDetalheSelecionado;
    }

    public void setNfeDetalheSelecionado(NfeDetalhe nfeDetalheSelecionado) {
        this.nfeDetalheSelecionado = nfeDetalheSelecionado;
    }

    public int getTipoCstIcms() {
        return tipoCstIcms;
    }

    public void setTipoCstIcms(int tipoCstIcms) {
        this.tipoCstIcms = tipoCstIcms;
    }

    public boolean isValoresValido() {
        return valoresValido;
    }

    public void setValoresValido(boolean valoresValido) {
        this.valoresValido = valoresValido;
    }

    public boolean isPodeIncluirProduto() {
        return podeIncluirProduto;
    }

    public void setPodeIncluirProduto(boolean podeIncluirProduto) {
        this.podeIncluirProduto = podeIncluirProduto;
    }

    public NaturezaFinanceira getNaturezaFinanceira() {
        return naturezaFinanceira;
    }

    public void setNaturezaFinanceira(NaturezaFinanceira naturezaFinanceira) {
        this.naturezaFinanceira = naturezaFinanceira;
    }

    public ContaCaixa getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(ContaCaixa contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public boolean isNaturezaFinaxeiraRequirida() {
        return !getObjeto().getListaDuplicata().isEmpty();
    }

    public boolean isImportado() {
        return importado;
    }

    public List<NfeDuplicata> getDuplicatas() {
        return duplicatas;
    }

    public void setDuplicatas(List<NfeDuplicata> duplicatas) {
        this.duplicatas = duplicatas;
    }


    public UnidadeConversao getUnidadeConversaoSelecionada() {
        return unidadeConversaoSelecionada;
    }

    public void setUnidadeConversaoSelecionada(UnidadeConversao unidadeConversaoSelecionada) {
        this.unidadeConversaoSelecionada = unidadeConversaoSelecionada;
    }

    public UnidadeProduto getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(UnidadeProduto unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public BigDecimal getFator() {
        return fator;
    }

    public void setFator(BigDecimal fator) {
        this.fator = fator;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}
