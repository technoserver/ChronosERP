package com.chronos.controll.vendas;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ProdutoDTO;
import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.OperadoraCartaoRepository;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.comercial.VendaPdvService;
import com.chronos.service.comercial.VendedorService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.service.financeiro.OperadoraCartaoService;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private Repository<EmpresaProduto> produtos;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<VendaComissao> comissoes;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private OperadoraCartaoRepository operadoraCartaoRepository;
    @Inject
    private OperadoraCartaoService operadoraCartaoService;

    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoRepository;
    @Inject
    private Repository<PdvTipoPagamento> tipoPagamentoRepository;
    @Inject
    private Repository<AdmParametro> parametroRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;

    @Inject
    private UsuarioService userService;
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


    private ERPLazyDataModel<PdvVendaCabecalho> dataModel;

    private AdmParametro parametro;
    private PdvVendaCabecalho venda;
    private PdvVendaDetalhe item;
    private PdvVendaDetalhe itemSelecionado;
    private PdvTipoPagamento tipoPagamento;
    private List<PdvTipoPagamento> listTipoPagamento;
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


    @PostConstruct
    private void init() {

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();

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
                telaGrid = false;
                telaVenda = true;
                telaPagamentos = false;
                venda = new PdvVendaCabecalho();
                venda.setEmpresa(empresa);
                venda.setListaPdvVendaDetalhe(new ArrayList<>());
                vendedor = instanciarVendedor(usuario);
                venda.setVendedor(vendedor);
                venda.setPdvMovimento(movimento);
                item = new PdvVendaDetalhe();
                desconto = BigDecimal.ZERO;
                telaPagamentos = false;
                telaVenda = true;
                telaImpressao = false;
                exibirCondicoes = false;
                instanciarParametro();
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

    public void cancelarVenda() {

    }

    public void cancelar() {
        telaVenda = true;
        telaPagamentos = false;
        novaVenda();
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


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Produto">
    public List<ProdutoDTO> getListProduto(String nome) {
        List<ProdutoDTO> listaProduto = new ArrayList<>();

        try {

            listaProduto = produtoService.getListaProdutoDTO(empresa, nome, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public void selecionarProduto(SelectEvent event) {
        ProdutoDTO produtoSelecionado = (ProdutoDTO) event.getObject();
        desconto = BigDecimal.ZERO;
        item = new PdvVendaDetalhe();
        item.setPdvVendaCabecalho(venda);
        item.setValorUnitario(produtoSelecionado.getProduto().getValorVenda());
        item.setQuantidade(BigDecimal.ONE);
        item.setProduto(produtoSelecionado.getProduto());
        item.setTaxaComissao(vendedor.getComissao());
        item.setTaxaDesconto(BigDecimal.ZERO);
        item.setValorDesconto(BigDecimal.ZERO);
    }

    public void calcularDesconto() {
        desconto = desconto == null ? BigDecimal.ZERO : desconto;
        if (tipoDesconto.equals("P")) {
            item.setTaxaDesconto(desconto);
        } else {
            BigDecimal subTotal = item.getValorSubtotal();
            BigDecimal razao = subTotal.subtract(desconto);
            razao = razao.divide(subTotal, MathContext.DECIMAL64);
            razao = razao.multiply(BigDecimal.valueOf(100));
            BigDecimal cem = BigDecimal.valueOf(100);
            BigDecimal percentual = cem.subtract(razao);

            item.setTaxaDesconto(percentual);
        }
    }

    public void addProduto() {
        Optional<PdvVendaDetalhe> itemOpt = getItemVenda(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();
        if (itemOpt.isPresent()) {
            item = itemOpt.get();
            item.setQuantidade(quantidade);
        } else {
            venda.getListaPdvVendaDetalhe().add(0, item);
        }

        venda.calcularValorTotal();
        item = new PdvVendaDetalhe();
        produto = new ProdutoDTO();

    }

    public void alterarQuantidade(Produto produto, BigDecimal quantidade) {
        Optional<PdvVendaDetalhe> itemOpt = getItemVenda(produto);
        if (itemOpt.isPresent()) {
            itemOpt.get().setQuantidade(quantidade);
        }
    }


    public void excluir() {
        venda.getListaPdvVendaDetalhe().remove(itemSelecionado);
        venda.calcularValorTotal();
    }

    private Optional<PdvVendaDetalhe> getItemVenda(Produto produto) {
        return venda.getListaPdvVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos cliente">
    public List<Cliente> getListCliente(String nome) {
        List<Cliente> list = new ArrayList<>();
        try {

            list = clientes.getEntitys(Cliente.class, "pessoa.nome", nome, new Object[]{"pessoa.nome"});
        } catch (Exception ex) {

        }
        return list;
    }

    public void buscarCliente() {
        cliente = new Cliente();
    }

    public void selecionarCliente() {
        if (cliente.getId() != null) {
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
            listTipoPagamento = tipoPagamentoRepository.getAll(PdvTipoPagamento.class);
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

    private void incluiPagamento(PdvTipoPagamento tipoPagamento, BigDecimal valor) {
        Optional<PdvFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent() && tipoPagamento.getPermiteTroco().equals("S")) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            if (totalReceber.compareTo(valorPago) < 0 && tipoPagamento.getPermiteTroco().equals("N")) {
                Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            } else {
                PdvFormaPagamento formaPagamento = new PdvFormaPagamento();
                formaPagamento.setPdvVendaCabecalho(venda);
                formaPagamento.setPdvTipoPagamento(tipoPagamento);
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

    private Optional<PdvFormaPagamento> bucarTipoPagamento(PdvTipoPagamento tipoPagamento) {
        return venda.getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getPdvTipoPagamento().equals(tipoPagamento))
                .findAny();
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            venda.getListaFormaPagamento().remove(formaPagamentoSelecionado);
            verificaSaldoRestante();
        }
        Mensagem.addInfoMessage("Forma de pagamento removida");
    }

    public void finalizarVenda() {
        try {
            verificaSaldoRestante();
            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                venda.setTroco(troco);
                venda = service.finalizarVenda(venda);
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
            condicoesPagamentos = condicoes.getEntitys(VendaCondicoesPagamento.class, new Object[]{"nome", "vistaPrazo", "tipoRecebimento"});
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

    //</editor-fold>


    public void selecionarVendedor() {
        venda.setVendedor(vendedor);
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


    @NotNull
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

    public PdvTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(PdvTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public List<PdvTipoPagamento> getListTipoPagamento() {
        return listTipoPagamento;
    }

    public void setListTipoPagamento(List<PdvTipoPagamento> listTipoPagamento) {
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


}
