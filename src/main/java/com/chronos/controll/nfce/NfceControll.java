package com.chronos.controll.nfce;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.dto.UsuarioDTO;
import com.chronos.exception.EmissorException;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.view.ViewNfceCliente;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.comercial.VendedorService;
import com.chronos.util.Biblioteca;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.context.RequestContext;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by john on 04/10/17.
 */
@Named
@ViewScoped
public class NfceControll implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(NfceControll.class);

    private static final long serialVersionUID = 1L;
    @Inject
    protected FacesContext facesContext;
    @Inject
    private Repository<PdvMovimento> movimentos;
    @Inject
    private Repository<PdvTurno> turnos;
    @Inject
    private Repository<PdvOperador> operadores;
    @Inject
    private Repository<PdvSuprimento> suprimentos;
    @Inject
    private Repository<PdvConfiguracao> configuracoes;
    @Inject
    private Repository<ViewNfceCliente> clientes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<PdvTipoPagamento> nfceTipoPagamentoRepository;
    @Inject
    private Repository<PdvFechamento> nfceFechamentoRepository;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private Repository<NfeCabecalho> nfeRepositoy;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;
    @Inject
    private NfeService nfeService;
    @Inject
    private VendedorService vendedorService;

    private PdvMovimento movimento;
    private List<PdvTurno> listTurno;


    private NfeDetalhe item;
    private NfeDetalhe itemSelecionado;
    private PdvTipoPagamento tipoPagamento;
    private List<PdvTipoPagamento> listTipoPagamento;
    private NfeFormaPagamento formaPagamentoSelecionado;
    private PdvConfiguracao configuracao;
    private NfeCabecalho venda;
    private NfeCabecalho vendaSelecionada;
    private Empresa empresa;
    private UsuarioDTO usuario;
    private ViewNfceCliente cliente;
    private Vendedor vendedor;
    private List<Vendedor> listVendedores;
    private TributOperacaoFiscal operacao;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;
    private boolean telaImpressao;
    private boolean telaCaixa;
    private boolean telaGrid;
    private boolean encontro;

    private BigDecimal valorSuprimento;
    private BigDecimal desconto;



    private BigDecimal totalVenda;
    private BigDecimal acrescimo;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private BigDecimal totalRecebido;
    private BigDecimal saldoRestante;
    private BigDecimal valorPago;

    private String userGerente;
    private String senhaGerente;
    private String userOperador;
    private String senhaOperador;

    private ExternalContext context;
    private String nomeCupom;

    private String justificativa;

    private ERPLazyDataModel dataModel;


    @PostConstruct
    private void init() {

        // verificarMovimento();

        empresa = FacesUtil.getEmpresaUsuario();
        usuario = FacesUtil.getUsuarioSessao();
        vendedor = instanciarVendedor(usuario);
        context = facesContext.getExternalContext();
        listVendedores = new ArrayList<>();
        operacao = operacaoFiscalRepository.get(1, TributOperacaoFiscal.class);
        telaGrid = true;

    }

    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel();
            dataModel.setDao(nfeRepositoy);
            dataModel.setClazz(NfeCabecalho.class);
        }
        dataModel.setAtributos(new Object[]{"serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo", "qrcode"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "65", Filtro.IGUAL);
        return dataModel;
    }

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Caixa">

    public void verificarMovimento() {
        try {
            movimento = movimentos.get(PdvMovimento.class, "statusMovimento", "A");
            if (movimento == null) {

            } else {
                telaVenda = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }








    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Venda">

    public void novaVenda() {
        try {
            configuracao = getConfiguraNfce();
            telaVenda = true;
            telaGrid = false;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = false;
            venda = new NfeCabecalho();
            venda.setTributOperacaoFiscal(operacao);
            item = new NfeDetalhe();


            nfeService.dadosPadroes(venda, ModeloDocumento.NFCE, empresa, new ConfiguracaoEmissorDTO(configuracao));
            desconto = BigDecimal.ZERO;

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().validationFailed();
            logger.error("Erro ao iniciar nova venda", ex);
            Mensagem.addErrorMessage("", ex);
        }

    }


    public void cancelarVenda() {
        novaVenda();
        Mensagem.addInfoMessage("Venda cancelada");
    }


    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pagamentos ">
    public void iniciarPagamentosVenda() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;

        if (venda.getListaNfeDetalhe().isEmpty()) {
            Mensagem.addInfoMessage("Não foram informados itens nessa venda");
        } else {
            telaVenda = false;
            telaImpressao = false;
            telaCaixa = false;
            telaPagamentos = true;
            listTipoPagamento = nfceTipoPagamentoRepository.getAll(PdvTipoPagamento.class);
        }

        totalVenda = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        acrescimo = BigDecimal.ZERO;
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;

        //guarda valores para calculo
        totalVenda = venda.getValorTotalProdutos().add(venda.getValorServicos());
        desconto = venda.getValorDesconto();
        acrescimo = venda.getValorDespesasAcessorias();
        totalReceber = Biblioteca.soma(totalVenda, acrescimo);
        totalReceber = Biblioteca.subtrai(totalReceber, desconto);
        saldoRestante = totalReceber;

        valorPago = saldoRestante;
        venda.getListaNfeFormaPagamento().clear();
    }

    public void lancaPagamento() {
        try {

            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addInfoMessage("Todos os valores já foram recebidos. Finalize a venda.");
            } else {
                if (tipoPagamento.getGeraParcelas().equals("S")) {

                } else {

                    incluiPagamento(tipoPagamento, valorPago);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }


    }

    private void incluiPagamento(PdvTipoPagamento tipoPagamento, BigDecimal valor) throws Exception {
        Optional<NfeFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent()) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            NfeFormaPagamento formaPagamento = new NfeFormaPagamento();
            formaPagamento.setNfeCabecalho(venda);
            formaPagamento.setPdvTipoPagamento(tipoPagamento);
            formaPagamento.setValor(valor);
            formaPagamento.setForma(tipoPagamento.getCodigo());
            formaPagamento.setEstorno("N");

            venda.getListaNfeFormaPagamento().add(formaPagamento);


            totalRecebido = Biblioteca.soma(totalRecebido, valor);
            troco = Biblioteca.subtrai(totalRecebido, totalReceber);
            if (troco.compareTo(BigDecimal.ZERO) == -1) {
                troco = BigDecimal.ZERO;
            }
            verificaSaldoRestante();
        }

    }

    private Optional<NfeFormaPagamento> bucarTipoPagamento(PdvTipoPagamento tipoPagamento) {
        Optional<NfeFormaPagamento> formaPagamentoOpt = venda.getListaNfeFormaPagamento()
                .stream()
                .filter(fp -> fp.getPdvTipoPagamento().equals(tipoPagamento))
                .findAny();
        return formaPagamentoOpt;
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            venda.getListaNfeFormaPagamento().remove(formaPagamentoSelecionado);
            verificaSaldoRestante();
        }
        Mensagem.addInfoMessage("Forma de pagamento removida");
    }

    public void finalizarVenda() {
        verificaSaldoRestante();
        if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
            venda.setTroco(troco);
            transmitirNfe();
        } else {
            Mensagem.addInfoMessage("Valores informados não são suficientes para finalizar a venda.");
        }
    }

    public void lancaMovimentos() {

        venda.getListaNfeFormaPagamento().stream()
                .forEach(p -> {
                    PdvFechamento fechamento = new PdvFechamento();
                    fechamento.setPdvMovimento(movimento);
                    fechamento.setTipoPagamento(p.getPdvTipoPagamento().getDescricao());
                    fechamento.setValor(p.getValor());
                    nfceFechamentoRepository.salvar(fechamento);

                });
        movimento.setTotalVenda(Biblioteca.soma(movimento.getTotalVenda(), totalReceber));
        movimento.calcularTotalFinal();
        movimentos.atualizar(movimento);
    }

    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        Set<NfeFormaPagamento> listaPagamento = venda.getListaNfeFormaPagamento();
        for (NfeFormaPagamento p : listaPagamento) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        saldoRestante = Biblioteca.subtrai(totalReceber, recebidoAteAgora);
        totalRecebido = recebidoAteAgora;
        valorPago = saldoRestante;
        if (valorPago.compareTo(BigDecimal.ZERO) == -1) {
            valorPago = BigDecimal.ZERO;
        }
        if (saldoRestante.compareTo(BigDecimal.ZERO) == -1) {
            saldoRestante = BigDecimal.ZERO;
        }
    }

    public void cancelarPagamento() {
        telaVenda = true;
        telaImpressao = false;
        telaCaixa = false;
        telaPagamentos = false;
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Venda item ">

    public void selecionarProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        novoItem();
        item.setProduto(produto);
        item.setValorUnitarioComercial(Optional.ofNullable(produto.getValorVenda()).orElse(BigDecimal.ZERO));
//        item.setListaArmamento(new HashSet<>());
//        item.setListaDeclaracaoImportacao(new HashSet<>());
//        item.setListaMedicamento(new HashSet<>());

    }


    public void novoItem() {
        desconto = BigDecimal.ZERO;
        item = new NfeDetalhe();
        item.setNfeCabecalho(venda);
        item.setQuantidadeComercial(BigDecimal.ONE);
    }

    public void salvaProduto() {
        try {
            if (!validarItem()) {
                return;
            }
            realizaCalculosItem();

            Optional<NfeDetalhe> itemNfeOptional = buscarItemPorProduto(item.getProduto());
            NfeDetalhe it = null;
            if (itemNfeOptional.isPresent()) {
                it = itemNfeOptional.get();
                it = item;
            } else {
                venda.getListaNfeDetalhe().add(0, item);
            }
            venda = nfeService.atualizarTotais(venda);
            novoItem();
            Mensagem.addInfoMessage("Registro incluído!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto\n", e);
        }

    }

    private Optional<NfeDetalhe> buscarItemPorProduto(Produto produto) {
        return venda.getListaNfeDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    public void excluirItem() {

        try {
            Produto produto = itemSelecionado.getProduto();
            int indice = IntStream.range(0, venda.getListaNfeDetalhe().size())
                    .filter(i -> venda.getListaNfeDetalhe().get(i).getProduto().equals(produto))
                    .findAny().getAsInt();
            venda.getListaNfeDetalhe().remove(indice);
            venda = nfeService.atualizarTotais(venda);
            Mensagem.addInfoMessage("Produto exluso com sucesso");
        } catch (Exception ex) {
            logger.error("erro ao exluir item", ex);
            Mensagem.addErrorMessage("", ex);
        }
    }

    /**
     * setar os valores do item que está sendo salvo
     *
     * @return
     * @throws Exception
     */
    private NfeDetalhe realizaCalculosItem() throws Exception {

        if (item.getProduto().getNcm() == null) {
            throw new Exception("Não existe NCM para o produto informado. Operação não realizada.");
        }

        BigDecimal valorVenda = item.getValorUnitarioComercial();
        valorVenda = valorVenda == null ? item.getProduto().getValorVenda() : valorVenda;
        item.setValorUnitarioComercial(valorVenda);
        item.setEntraTotal(1);
        item.pegarInfoProduto();
        item.calcularValorTotalProduto();
        item = nfeService.definirTributacao(item, venda.getTributOperacaoFiscal(), venda.getDestinatario());

        return item;
    }

    public boolean validarItem() {
        String msg = "";
        boolean valido = true;
        if (item == null) {
            msg = "Venda de produto não iniciada";
            valido = false;
        } else if (item.getProduto() == null) {
            msg = "Produto não definido";
            valido = false;
        } else if (item.getProduto().getValorVenda().doubleValue() <= 0) {
            msg = "Produto não pode ser vendido com valor zerado ou negativo.";
            valido = false;
        } else if (StringUtils.isEmpty(item.getProduto().getUnidadeProduto().getPodeFracionar())) {
            msg = "A unidade de medida possue valores não informado : 'Pode Fracionar'.";
            valido = false;
        } else if (item.getProduto().getUnidadeProduto().getPodeFracionar().equals("N") && (item.getQuantidadeComercial().doubleValue() % 1) != 0) {
            msg = "Este produto não pode ser vendido em quantidade fracionada.";
            valido = false;
        }
        if (!StringUtils.isEmpty(msg)) {
            Mensagem.addInfoMessage(msg);
        }

        return valido;
    }


    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos NFce">

    private PdvConfiguracao getConfiguraNfce() throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        configuracao = configuracao == null ? configuracoes.get(PdvConfiguracao.class, filtros) : configuracao;
        if (configuracao == null) {
            throw new Exception("NFC-e não configurada !!!");
        }
        return configuracao;
    }

    private void definirNumeroItens() {
        int i = 0;
        for (NfeDetalhe item : venda.getListaNfeDetalhe()) item.setNumeroItem(++i);

    }

    @Transactional
    private void transmitirNfe() {
        try {


            configuracao = getConfiguraNfce();
            definirNumeroItens();
            venda.setCsc(configuracao.getCodigoCsc());
            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            StatusTransmissao status = nfeService.transmitirNFe(venda, new ConfiguracaoEmissorDTO(configuracao), estoque);
            if (status == StatusTransmissao.AUTORIZADA) {
                gerarCupom();
                Mensagem.addInfoMessage("NFe transmitida com sucesso");
                RequestContext.getCurrentInstance().addCallbackParam("vendaFinalizada", true);
            }


        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out")) {
                try {
                    venda.setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    nfeRepositoy.atualizar(venda);
                } catch (Exception ex1) {

                }
            }
            logger.error("erro de time out", ex.getCause());
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        } catch (Exception ex) {
            logger.error("Erro ao transmitir", ex.getCause());

            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        }
    }

    public void cancelarNfce() {

        try {
            venda = nfeRepositoy.getJoinFetch(vendaSelecionada.getId(), NfeCabecalho.class);
            venda.setJustificativaCancelamento(justificativa);
            configuracao = getConfiguraNfce();
            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            boolean cancelado = nfeService.cancelarNFe(venda, new ConfiguracaoEmissorDTO(configuracao), estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFCe cancelada com sucesso");
            }

        } catch (Exception e) {
            logger.error("erro ao cancelar NFCe", e.getCause());
            Mensagem.addErrorMessage("Ocorreu um erro ao cancelar a NFC-e!\n", e);
        }

    }


    private void gerarCupom() throws Exception {

        try {
            configuracao = getConfiguraNfce();
            nfeService.gerarDanfe(venda, new ConfiguracaoEmissorDTO(configuracao));
            nomeCupom = "cupom" + venda.getNumero() + ".pdf";
        } catch (Exception ex) {
            logger.error("erro ao gerar cupom", ex.getCause());
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void danfe() {


        try {
            configuracao = getConfiguraNfce();
            nfeService.danfe(vendaSelecionada, new ConfiguracaoEmissorDTO(configuracao));

        } catch (Exception ex) {
            logger.error("erro ao gerar danfe", ex.getMessage());
            Mensagem.addErrorMessage("", ex);
        }
    }


    //</editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Cliente">


    public void indentificarCliente() {
        cliente = new ViewNfceCliente();
    }

    public void vincularCliente() {
        localizarCliente();
        if (cliente.getId() != null) {
            Cliente c = new Cliente();
            c.setId(cliente.getId());
            venda.setCliente(c);

            preencheDadosDestinatario();
        } else {
            venda.setCliente(null);
            preencheDadosDestinatario();
        }
    }

    private void localizarCliente() {
        String nome = cliente.getNome();
        String email = cliente.getEmail();
        String cpf = cliente.getCpf();
        cliente = clientes.get(ViewNfceCliente.class, "cpf", cpf);
        if (cliente == null) {
            cliente = new ViewNfceCliente();
            cliente.setCpf(cpf);
            cliente.setNome(nome);
            cliente.setEmail(email);
        }
    }

    private void preencheDadosDestinatario() {
        NfeDestinatario destinatario = new NfeDestinatario();
        destinatario.setNfeCabecalho(venda);
        destinatario.setNome(cliente.getNome());
        destinatario.setCpfCnpj(cliente.getCpf());

        venda.setDestinatario(destinatario);
    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Vendedor">

    public void buscarVendedores() {
        listVendedores = listVendedores.isEmpty() ? vendedores.getEntitys(Vendedor.class) : listVendedores;
    }

    private Vendedor instanciarVendedor(UsuarioDTO usuario) {
        return vendedorService.instaciarVendedor(usuario.getIdcolaborador());


    }

    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Pesquisas">

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto = new ArrayList<>();

        try {
            List<ProdutoDTO> list = nfeService.getListaProdutoDTO(descricao,true);
            listaProduto = list.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao lista os produtos", e.getMessage());
        }
        return listaProduto;
    }


    //</editor-fold>


    public String formatarValor(BigDecimal valor) {
        return FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
    }

    public boolean podeConsultar() {

        return FacesUtil.isUserInRole("NFCE_CONSULTA")
                || FacesUtil.isUserInRole("ADMIN");
    }

    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0;
    }

    public boolean isPodeFinalzarVenda() {
        return saldoRestante.signum() == 0;
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

    public boolean isTelaCaixa() {
        return telaCaixa;
    }

    public void setTelaCaixa(boolean telaCaixa) {
        this.telaCaixa = telaCaixa;
    }

    public List<PdvTurno> getListTurno() {
        return listTurno;
    }

    public void setListTurno(List<PdvTurno> listTurno) {
        this.listTurno = listTurno;
    }



    public BigDecimal getValorSuprimento() {
        return valorSuprimento;
    }

    public void setValorSuprimento(BigDecimal valorSuprimento) {
        this.valorSuprimento = valorSuprimento;
    }

    public String getUserGerente() {
        return userGerente;
    }

    public void setUserGerente(String userGerente) {
        this.userGerente = userGerente;
    }

    public String getSenhaGerente() {
        return senhaGerente;
    }

    public void setSenhaGerente(String senhaGerente) {
        this.senhaGerente = senhaGerente;
    }

    public String getUserOperador() {
        return userOperador;
    }

    public void setUserOperador(String userOperador) {
        this.userOperador = userOperador;
    }

    public String getSenhaOperador() {
        return senhaOperador;
    }

    public void setSenhaOperador(String senhaOperador) {
        this.senhaOperador = senhaOperador;
    }



    public ViewNfceCliente getCliente() {
        return cliente;
    }

    public void setCliente(ViewNfceCliente cliente) {
        this.cliente = cliente;
    }

    public List<Vendedor> getListVendedores() {
        return listVendedores;
    }

    public void setListVendedores(List<Vendedor> listVendedores) {
        this.listVendedores = listVendedores;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public NfeCabecalho getVenda() {
        return venda;
    }

    public void setVenda(NfeCabecalho venda) {
        this.venda = venda;
    }

    public NfeDetalhe getItem() {
        return item;
    }

    public void setItem(NfeDetalhe item) {
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

    public boolean isTelaGrid() {
        return telaGrid;
    }

    public void setTelaGrid(boolean telaGrid) {
        this.telaGrid = telaGrid;
    }



    public List<PdvTipoPagamento> getListTipoPagamento() {
        return listTipoPagamento;
    }

    public void setListTipoPagamento(List<PdvTipoPagamento> listTipoPagamento) {
        this.listTipoPagamento = listTipoPagamento;
    }

    public PdvTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(PdvTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }


    public BigDecimal getTotalVenda() {
        return totalVenda;
    }

    public BigDecimal getAcrescimo() {
        return acrescimo;
    }

    public BigDecimal getTotalReceber() {
        return totalReceber;
    }

    public BigDecimal getTroco() {
        return troco;
    }

    public BigDecimal getTotalRecebido() {
        return totalRecebido;
    }


    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public NfeFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(NfeFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }

    public String getNomeCupom() {
        return nomeCupom;
    }

    public void setNomeCupom(String nomeCupom) {
        this.nomeCupom = nomeCupom;
    }

    public NfeCabecalho getVendaSelecionada() {
        return vendaSelecionada;
    }

    public void setVendaSelecionada(NfeCabecalho vendaSelecionada) {
        this.vendaSelecionada = vendaSelecionada;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public NfeDetalhe getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(NfeDetalhe itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }
}
