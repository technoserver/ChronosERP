package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.OperadoraCartaoRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.ProdutoService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.service.comercial.PdvVendaDetalheService;
import com.chronos.erp.service.comercial.VendaPdvService;
import com.chronos.erp.service.comercial.VendedorService;
import com.chronos.erp.service.financeiro.FinLancamentoReceberService;
import com.chronos.erp.service.financeiro.MovimentoService;
import com.chronos.erp.service.financeiro.OperadoraCartaoService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by john on 19/09/17.
 */
@Named
@ViewScoped
public class BalcaoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BalcaoControll.class);
    @Inject
    protected FacesContext facesContext;
    @Inject
    private Repository<PdvVendaCabecalho> vendas;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Cliente> clientes;

    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private OperadoraCartaoRepository operadoraCartaoRepository;
    @Inject
    private OperadoraCartaoService operadoraCartaoService;

    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private Repository<AdmParametro> parametroRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    @Inject
    private FinLancamentoReceberService recebimentoService;
    @Inject
    private NfeService nfeService;
    @Inject
    private VendaPdvService service;
    @Inject
    private VendaPdvService vendaService;
    @Inject
    private VendedorService vendedorService;
    @Inject
    private MovimentoService movimentoService;
    @Inject
    private ProdutoService produtoService;

    @Inject
    private PdvVendaDetalheService vendaDetalheService;

    private ERPLazyDataModel<PdvVendaCabecalho> dataModel;

    private AdmParametro parametro;
    private PdvVendaCabecalho venda;
    private PdvVendaDetalhe item;
    private PdvVendaDetalhe itemSelecionado;
    private TipoPagamento tipoPagamento;
    private List<TipoPagamento> listTipoPagamento;
    private PdvFormaPagamento formaPagamentoSelecionado;
    private PdvMovimento movimento;
    private Empresa empresa;
    private UsuarioDTO usuario;
    private Vendedor vendedor;
    private Cliente cliente;
    private ProdutoDTO produto;
    private List<Vendedor> listVendedores;
    private VendaCondicoesPagamento condicaoPagamento;
    private OperadoraCartao operadoraCartao;
    private List<VendaCondicoesPagamento> condicoesPagamentos;
    private List<OperadoraCartao> operadoras;
    private BigDecimal desconto;
    private List<FinParcelaReceber> parcelas;
    private BigDecimal valorParcelas;
    private BigDecimal diferecaParcelas;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;
    private boolean telaImpressao;
    private boolean telaCaixa;
    private boolean exibirCondicoes;
    private boolean exibirQtdParcelas;
    private boolean telaGrid = true;
    private BigDecimal totalVenda;
    private BigDecimal acrescimo;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private BigDecimal totalRecebido;
    private BigDecimal saldoRestante;
    private BigDecimal valorPago;
    private int id;
    private int qtdParcelas = 1;
    private int qtdMaxParcelas = 1;

    private String usuarioSupervisor;
    private String senhaSupervisor;

    private String filtro;
    private List<ProdutoDTO> listaProduto;
    private int tipoPesquisa;

    private boolean exibirDetalheProduto = true;
    private String msgListaProduto = "";

    private String justificativa;

    private boolean podeAlterarPreco = true;

    @PostConstruct
    private void init() {

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();

        this.podeAlterarPreco = usuario.getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");

    }

    public void definirTipoPesquisa() {
        Map<String, String> parameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String tipoPesquiaPDV = parameterMap.get("tipoPesquiaPDV");
        tipoPesquisa = StringUtils.isEmpty(tipoPesquiaPDV) || tipoPesquiaPDV.equals("1") ? 1 : 2;
        msgListaProduto = "";
    }


    public ERPLazyDataModel<PdvVendaCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(vendas);
            dataModel.setClazz(PdvVendaCabecalho.class);
        }
        Object[] atributos = new Object[]{"idnfe", "dataHoraVenda", "valorSubtotal", "valorDesconto", "valorTotal", "nomeCliente",
                "sicronizado", "statusVenda", "pdvMovimento.pdvCaixa.nome"};
        dataModel.setAtributos(atributos);
        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    public void novaVenda() {
        try {
            if (verificarMovimento() == null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/modulo/comercial/caixa/movimentos.xhtml");
                return;
            } else {
                vendedor = null;
                cliente = null;
                telaGrid = false;
                telaVenda = true;
                telaPagamentos = false;
                venda = new PdvVendaCabecalho();
                venda.setEmpresa(empresa);
                venda.setListaPdvVendaDetalhe(new ArrayList<>());
                vendedor = instanciarVendedor(usuario);
                venda.setVendedor(vendedor);
                venda.setTaxaComissao(vendedor.getComissao());
                venda.setPdvMovimento(movimento);
                item = new PdvVendaDetalhe();
                desconto = BigDecimal.ZERO;
                telaPagamentos = false;
                telaVenda = true;
                telaImpressao = false;
                exibirCondicoes = false;
                instanciarParametro();
                parcelas = new ArrayList<>();
                msgListaProduto = "";
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao iniciar uma nova venda", ex);
            } else {
                throw new RuntimeException("Erro ao iniciar uma nova venda", ex);
            }
        }


    }

    public void instanciarParametro() {
        parametro = FacesUtil.getParamentos();
        if (parametro == null) {
            parametro = parametroRepository.get(AdmParametro.class, "empresa.id", empresa.getId());

            if (parametro.getTributOperacaoFiscalPadrao() != null) {
                TributOperacaoFiscal operacaoFiscal = operacaoFiscalRepository.get(TributOperacaoFiscal.class, "id", parametro.getTributOperacaoFiscalPadrao(), new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn"});
                parametro.setOperacaoFiscal(operacaoFiscal);
            }

            FacesUtil.setParamtro(parametro);
        }
    }


    public PdvMovimento verificarMovimento() {
        try {
            movimento = movimentoService.verificarMovimento(empresa);
            return movimento;
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
            return null;
        }

    }

    public void cancelar() {
        telaVenda = true;
        telaPagamentos = false;
        novaVenda();
    }

    public void aplicarDesconto() {

        try {
            vendaService.aplicarDesconto(venda, tipoDesconto, desconto);
            desconto = BigDecimal.ZERO;
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                throw new RuntimeException("erro ao aplicar desconto", ex);
            }
        }

    }

    public void removerDesconto() {
        vendaService.removerDesconto(venda);
        desconto = BigDecimal.ZERO;
    }

    public void gerarNfce() {


        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            venda = vendas.getJoinFetch(venda.getId(), PdvVendaCabecalho.class);
            if (!venda.getListaPdvVendaDetalhe().isEmpty() && !venda.getListaFormaPagamento().isEmpty()) {
                vendaService.transmitirNFe(venda, estoque);
            } else {
                Mensagem.addInfoMessage("não foram encotrado itens para essa venda");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe \n", ex);
            } else if (ex instanceof UnknownHostException) {
                Mensagem.addErrorMessage("Erro ao gerar conexao com \n", ex);
            } else if (ex instanceof EmissorException) {
                Mensagem.addErrorMessage("Erro ao gera NFCe  \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar NFce", ex);
            }


        }
    }

    public void danfe() {
        try {

            NfeCabecalho nfe = nfeRepository.get(venda.getIdnfe(), NfeCabecalho.class);
            nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), false);
            nfeService.danfe(nfe);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gera Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao gerar Cupom", ex);
            }
        }
    }

    public void cancelarVenda() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            if (venda.getStatusVenda().equals("F")) {
                justificativa = "";
                PrimeFaces.current().executeScript("PF('dialogOutrasTelas4').show();");
            } else {
                service.cancelar(venda.getId(), estoque, null);
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }

    public void cancelarVendaNFCe() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            service.cancelar(venda.getId(), estoque, justificativa);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Produto">


    public void pesquisarProduto() {

        getListProduto(filtro);
        if (tipoPesquisa == 2 && listaProduto.size() == 1) {
            selecionarProduto(listaProduto.get(0));
            PrimeFaces.current().ajax().update("formCentro:mostra-produto");
        }

    }

    public List<ProdutoDTO> getListProduto(String nome) {
        listaProduto = new ArrayList<>();

        try {

            listaProduto = produtoService.getListaProdutoDTO(empresa, nome, true);
            exibirDetalheProduto = tipoPesquisa == 1 && !listaProduto.isEmpty();
            msgListaProduto = tipoPesquisa == 2 && listaProduto.isEmpty() ? "Nenhum produto encontrado" : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public void selecionarProduto(SelectEvent event) {
        ProdutoDTO produtoSelecionado = (ProdutoDTO) event.getObject();

        selecionarProduto(produtoSelecionado);

    }

    public void selecionarProduto(ProdutoDTO produtoSelecionado) {
        desconto = BigDecimal.ZERO;

        item = new PdvVendaDetalhe();
        item.setPdvVendaCabecalho(venda);
        item.setValorUnitario(produtoService.defnirPrecoVenda(produtoSelecionado));
        item.setQuantidade(BigDecimal.ONE);
        item.setProduto(produtoSelecionado.getProduto());
        item.setTaxaComissao(vendedor.getComissao());
        item.setTaxaDesconto(BigDecimal.ZERO);
        item.setValorDesconto(BigDecimal.ZERO);

        produto = produtoSelecionado;


        exibirDetalheProduto = true;
        listaProduto = new ArrayList<>();

    }

    public void calcularPrecoAtacado() {
        produto.setQuantidadeVenda(item.getQuantidade());
        BigDecimal valorVenda = produtoService.defnirPrecoVenda(produto);
        item.setValorUnitario(valorVenda);
        item.getValorSubtotal();
    }

    public void calcularDesconto() {

        try {
            desconto = desconto == null ? BigDecimal.ZERO : desconto;
            if (tipoDesconto.equals("P")) {

                if (desconto.compareTo(BigDecimal.valueOf(99.99)) >= 0) {
                    Mensagem.addErrorMessage("Desconto não permitido");
                } else {
                    item.setTaxaDesconto(desconto);
                    item.setValorDesconto(Biblioteca.calcularValorPercentual(item.getValorSubtotal(), desconto));
                }


            } else {
                BigDecimal subTotal = item.getValorSubtotal();

                if (desconto.compareTo(subTotal) >= 0) {
                    Mensagem.addErrorMessage("Desconto não permitido");
                } else {
                    BigDecimal razao = subTotal.subtract(desconto);
                    razao = razao.divide(subTotal, MathContext.DECIMAL64);
                    razao = razao.multiply(BigDecimal.valueOf(100));
                    BigDecimal cem = BigDecimal.valueOf(100);
                    BigDecimal percentual = cem.subtract(razao);
                    item.setValorDesconto(desconto);
                    item.setTaxaDesconto(percentual);
                }


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addProduto() {


        try {

            vendaDetalheService.verificarRestricao(item);

            if (vendaDetalheService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                venda = vendaDetalheService.addProduto(venda, item);
                item = new PdvVendaDetalhe();
                produto = new ProdutoDTO();
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao autoizar o procedimento", ex);
            }
        }


    }


    public void excluir() {


        int idx = IntStream.range(0, venda.getListaPdvVendaDetalhe().size())
                .filter(i -> venda.getListaPdvVendaDetalhe().get(i).getProduto().equals(itemSelecionado.getProduto()))
                .findAny().getAsInt();
        venda.getListaPdvVendaDetalhe().remove(idx);
        venda.calcularValorTotal();
    }


    public boolean autorizacaoSupervisor() {

        try {


            if (vendaDetalheService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                venda = vendaDetalheService.addProduto(venda, item);
                item = new PdvVendaDetalhe();
                produto = new ProdutoDTO();

            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao autoizar o procedimento", ex);
            }
        }
        return true;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos cliente">
    public List<Cliente> getListCliente(String nome) {
        List<Cliente> list = new ArrayList<>();
        try {

            List<Filtro> filtros = new ArrayList<>();

            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));

            int id = org.apache.commons.lang3.StringUtils.isNumeric(nome) ? Integer.parseInt(nome) : 0;

            filtros.add(new Filtro(Filtro.OR, "id", Filtro.IGUAL, id));

            list = clientes.getEntitys(Cliente.class, filtros, new Object[]{"pessoa.nome"});
        } catch (Exception ex) {

        }
        return list;
    }

    public void buscarCliente() {
        cliente = new Cliente();
    }

    public void selecionarCliente() {
        if (cliente != null && cliente.getId() != null) {
            venda.setCliente(cliente);
            venda.setNomeCliente(cliente.getPessoa().getNome());

        } else {

            if (StringUtils.isEmpty(venda.getCpfCnpjCliente())) {
                Mensagem.addErrorMessage("CPF/CNPJ obrigatorio");
                FacesContext.getCurrentInstance().validationFailed();
            } else if (StringUtils.isEmpty(venda.getNomeCliente())) {
                Mensagem.addErrorMessage("Nome obrigatorio");
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                boolean cpfValido = true;
                if (venda.getCpfCnpjCliente().length() == 14) {
                    cpfValido = Biblioteca.cnpjValido(venda.getCpfCnpjCliente());
                    if (!cpfValido) {
                        Mensagem.addErrorMessage("CNPJ invalido");
                        FacesContext.getCurrentInstance().validationFailed();
                    }

                } else {
                    cpfValido = Biblioteca.cpfValido(venda.getCpfCnpjCliente());
                    if (!cpfValido) {
                        Mensagem.addErrorMessage("CPF invalido");
                        FacesContext.getCurrentInstance().validationFailed();
                    }


                }
            }
            cliente = null;

        }

    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos vendedor">
    private Vendedor instanciarVendedor(UsuarioDTO usuario) {
        return vendedorService.instaciarVendedor(usuario.getIdcolaborador());
    }

    public void buscarVendedores() {
        listVendedores = vendedores.getEntitys(Vendedor.class);
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pagamentos ">
    public void iniciarPagamentosVenda() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;
        exibirCondicoes = false;
        exibirQtdParcelas = false;

        if (venda.getListaPdvVendaDetalhe().isEmpty()) {
            Mensagem.addInfoMessage("Não foram informados itens nessa venda");
        } else {
            telaVenda = false;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = true;
            listTipoPagamento = definirTipoPagament();
        }

        totalVenda = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        acrescimo = BigDecimal.ZERO;
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;

        //guarda valores para calculo
        totalVenda = venda.getValorTotal();
        desconto = venda.getValorDesconto();
        acrescimo = BigDecimal.ZERO;
        totalReceber = Biblioteca.soma(totalVenda, acrescimo);
        saldoRestante = totalReceber;

        valorPago = saldoRestante;
        venda.setListaFormaPagamento(new ArrayList<>());
    }

    public void lancaPagamento() {
        try {
            boolean update = true;

            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addErrorMessage("Todos os valores já foram recebidos. Finalize a venda.");
            } else {
                if (cliente == null && tipoPagamento.getGeraParcelas().equals("S")) {
                    Mensagem.addErrorMessage("Para gera contas a receber é preciso informar um cliente");
                } else if (cliente == null && tipoPagamento.getCodigo().equals("05")) {
                    Mensagem.addErrorMessage("Para pagamento com crédito é preciso informa um cliente");
                } else if (tipoPagamento.getCodigo().equals("03") && operadoraCartao == null) {
                    Mensagem.addErrorMessage("Para pagamento com Cartão de crédito é preciso informa uma operadora");
                } else {
                    incluiPagamento(tipoPagamento, valorPago);
                }
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao lança os pagamentos", ex);
            } else {
                throw new RuntimeException("Erro ao lança os pagamentos", ex);
            }
        }


    }

    private void incluiPagamento(TipoPagamento tipoPagamento, BigDecimal valor) throws ChronosException {
        Optional<PdvFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent() && tipoPagamento.getPermiteTroco().equals("S")) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            if (totalReceber.compareTo(valorPago) < 0 && tipoPagamento.getPermiteTroco().equals("N")) {
                Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            } else {
                PdvFormaPagamento formaPagamento = new PdvFormaPagamento();
                formaPagamento.setPdvVendaCabecalho(venda);
                formaPagamento.setTipoPagamento(tipoPagamento);
                formaPagamento.setValor(valor);
                formaPagamento.setForma(tipoPagamento.getCodigo());
                formaPagamento.setEstorno("N");

                if (tipoPagamento.getGeraParcelas().equals("S")) {
                    formaPagamento.setCondicao(condicaoPagamento);
                }

                if (tipoPagamento.getCodigo().equals("03")) {
                    formaPagamento.setQtdParcelas(qtdParcelas);
                    formaPagamento.setOperadoraCartao(operadoraCartao);
                }

                if (tipoPagamento.getCodigo().equals("14")) {
                    parcelas = recebimentoService.gerarParcelas(formaPagamento.getValor(), venda.getDataHoraVenda(), formaPagamento.getCondicao());
                }


                totalRecebido = Biblioteca.soma(totalRecebido, valor);
                troco = Biblioteca.subtrai(totalRecebido, totalReceber);
                if (troco.compareTo(BigDecimal.ZERO) == -1) {
                    troco = BigDecimal.ZERO;
                }
                formaPagamento.setTroco(troco);
                venda.getListaFormaPagamento().add(formaPagamento);
                venda.setTroco(troco);
                verificaSaldoRestante();


            }

        }

    }

    private Optional<PdvFormaPagamento> bucarTipoPagamento(TipoPagamento tipoPagamento) {
        return venda.getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getTipoPagamento().equals(tipoPagamento))
                .findAny();
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            venda.getListaFormaPagamento().remove(formaPagamentoSelecionado);

            if (formaPagamentoSelecionado.getForma().equals("14")) {
                parcelas = new ArrayList<>();
            }

            verificaSaldoRestante();
        }
        Mensagem.addInfoMessage("Forma de pagamento removida");
    }

    public void finalizarVenda() {
        try {
            verificaSaldoRestante();
            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                venda.setTroco(troco);
                venda = service.finalizarVenda(venda, parcelas);
                telaVenda = false;
                telaPagamentos = false;
                telaImpressao = true;
                id = venda.getId();

                if (parametro != null && parametro.getFaturarVenda()) {
                    gerarNfce();
                }

            } else {
                Mensagem.addInfoMessage("Valores informados não são suficientes para finalizar a venda.");
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao finalziar venda", ex);
            } else {
                throw new RuntimeException("Erro ao finalziar venda", ex);
            }

        }
    }


    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        List<PdvFormaPagamento> listaPagamento = venda.getListaFormaPagamento();
        for (PdvFormaPagamento p : listaPagamento) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        saldoRestante = Biblioteca.subtrai(totalReceber, recebidoAteAgora);
        totalRecebido = recebidoAteAgora;
        valorPago = saldoRestante;
        if (valorPago.compareTo(BigDecimal.ZERO) < 0) {
            valorPago = BigDecimal.ZERO;
        }
        if (saldoRestante.compareTo(BigDecimal.ZERO) < 0) {
            saldoRestante = BigDecimal.ZERO;
        }
    }

    public void cancelarPagamento() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;
        tipoPagamento = null;
    }

    public void definirCondicoess() {
        exibirCondicoes = tipoPagamento.getGeraParcelas().equals("S") && !tipoPagamento.getCodigo().equals("02");
        exibirQtdParcelas = tipoPagamento.getCodigo().equals("03");
        qtdParcelas = 1;


        if (exibirCondicoes) {
            condicoesPagamentos = condicoes.getEntitys(VendaCondicoesPagamento.class, "vistaPrazo", "1", new Object[]{"nome", "vistaPrazo", "tipoRecebimento"});
        }

        if (exibirQtdParcelas) {
            operadoras = operadoraCartaoRepository.getOperadoraResumidaComintervalo();
            if (operadoras.stream().findFirst().isPresent()) {
                operadoraCartao = operadoras.stream().findFirst().get();
                qtdMaxParcelas = operadoraCartaoService.quantidadeMaximaParcelas(operadoraCartao);
            } else {
                qtdMaxParcelas = 0;
            }
        }

    }

    public void selecionarOperadora() {
        qtdMaxParcelas = operadoraCartaoService.quantidadeMaximaParcelas(operadoraCartao);
    }

    public void definirQtdParcelas(int qtd) {
        this.qtdParcelas = qtd;
    }


    public List<TipoPagamento> definirTipoPagament() {
        List<TipoPagamento> pagamentos = new ArrayList<>();
        pagamentos.add(new TipoPagamento(1, "01", "DINHEIRO", "S", "N"));
        pagamentos.add(new TipoPagamento(2, "02", "CHEQUE", "N", "N"));
        pagamentos.add(new TipoPagamento(3, "03", "CARTAO DE CREDITO", "N", "N"));
        pagamentos.add(new TipoPagamento(4, "04", "CARTAO DE DEBITO", "N", "N"));
        pagamentos.add(new TipoPagamento(5, "05", "CREDITO NA LOJA", "N", "N"));
        pagamentos.add(new TipoPagamento(6, "14", "DUPLICATA", "N", "S"));

        return pagamentos;
    }

    public void editarLancamentoReceber() {
        valorParcelas = getParcelas().stream().map(p -> p.getValor()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        diferecaParcelas = formaPagamentoSelecionado == null ? BigDecimal.ZERO : Biblioteca.subtrai(formaPagamentoSelecionado.getValor(), getValorParcelas());
    }

    public void calcularDiferencaParcela() {
        valorParcelas = getParcelas().stream().map(p -> p.getValor()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        diferecaParcelas = formaPagamentoSelecionado == null ? BigDecimal.ZERO : Biblioteca.subtrai(formaPagamentoSelecionado.getValor(), getValorParcelas());
    }

    public void confimarLancamento() {

        if (diferecaParcelas.signum() != 0) {
            Mensagem.addErrorMessage("Existe diferença nos valores informado");
            FacesContext.getCurrentInstance().validationFailed();
        }

    }


    //</editor-fold>


    public BigDecimal getValorParcelas() {
        return valorParcelas;
    }

    public BigDecimal getDiferecaParcelas() {
        return diferecaParcelas;
    }


    public void selecionarVendedor() {
        venda.setVendedor(vendedor);
        venda.setTaxaComissao(vendedor.getComissao());
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<Vendedor> getListVendedores() {
        return listVendedores;
    }


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public PdvVendaDetalhe getItem() {
        return item;
    }

    public void setItem(PdvVendaDetalhe item) {
        this.item = item;
    }

    public String getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public PdvVendaCabecalho getVenda() {
        return venda;
    }

    public void setVenda(PdvVendaCabecalho venda) {
        this.venda = venda;
    }

    public boolean isTelaVenda() {
        return telaVenda;
    }

    public void setTelaVenda(boolean telaVenda) {
        this.telaVenda = telaVenda;
    }

    public boolean isTelaPagamentos() {
        return telaPagamentos;
    }

    public void setTelaPagamentos(boolean telaPagamentos) {
        this.telaPagamentos = telaPagamentos;
    }

    public boolean isTelaImpressao() {
        return telaImpressao;
    }

    public void setTelaImpressao(boolean telaImpressao) {
        this.telaImpressao = telaImpressao;
    }

    public List<VendaCondicoesPagamento> getCondicoesPagamentos() {
        return condicoesPagamentos;
    }

    public void setCondicoesPagamentos(List<VendaCondicoesPagamento> condicoesPagamentos) {
        this.condicoesPagamentos = condicoesPagamentos;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public List<TipoPagamento> getListTipoPagamento() {
        return listTipoPagamento;
    }

    public void setListTipoPagamento(List<TipoPagamento> listTipoPagamento) {
        this.listTipoPagamento = listTipoPagamento;
    }

    public boolean isTelaCaixa() {
        return telaCaixa;
    }

    public void setTelaCaixa(boolean telaCaixa) {
        this.telaCaixa = telaCaixa;
    }

    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public void setTotalVenda(BigDecimal totalVenda) {
        this.totalVenda = totalVenda;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public void setAcrescimo(BigDecimal acrescimo) {
        this.acrescimo = acrescimo;
    }

    public BigDecimal getTotalReceber() {
        return totalReceber;
    }

    public void setTotalReceber(BigDecimal totalReceber) {
        this.totalReceber = totalReceber;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public void setTroco(BigDecimal troco) {
        this.troco = troco;
    }

    public BigDecimal getTotalRecebido() {
        return totalRecebido;
    }

    public void setTotalRecebido(BigDecimal totalRecebido) {
        this.totalRecebido = totalRecebido;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(BigDecimal saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public PdvFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(PdvFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }

    public boolean isPodeFinalzarVenda() {
        return saldoRestante.signum() == 0;
    }

    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0 || (exibirQtdParcelas && qtdMaxParcelas == 0);
    }

    public boolean isExibirCondicoes() {
        return exibirCondicoes;
    }

    public void setExibirCondicoes(boolean exibirCondicoes) {
        this.exibirCondicoes = exibirCondicoes;
    }

    public boolean isExibirQtdParcelas() {
        return exibirQtdParcelas;
    }

    public VendaCondicoesPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(VendaCondicoesPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtdParcelas() {
        return qtdParcelas;
    }

    public OperadoraCartao getOperadoraCartao() {
        return operadoraCartao;
    }

    public void setOperadoraCartao(OperadoraCartao operadoraCartao) {
        this.operadoraCartao = operadoraCartao;
    }

    public List<OperadoraCartao> getOperadoras() {
        return operadoras;
    }

    public void setOperadoras(List<OperadoraCartao> operadoras) {
        this.operadoras = operadoras;
    }

    public PdvVendaDetalhe getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(PdvVendaDetalhe itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public int getQtdMaxParcelas() {
        return qtdMaxParcelas;
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

    public List<FinParcelaReceber> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<FinParcelaReceber> parcelas) {
        this.parcelas = parcelas;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public List<ProdutoDTO> getListaProduto() {
        return listaProduto;
    }

    public int getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(int tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public boolean isExibirDetalheProduto() {
        return exibirDetalheProduto;
    }

    public String getMsgListaProduto() {
        return msgListaProduto;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public boolean isPodeAlterarPreco() {
        return podeAlterarPreco;
    }
}