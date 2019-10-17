package com.chronos.controll.os;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.OsProdutoServicoService;
import com.chronos.service.comercial.OsService;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsAberturaControll extends AbstractControll<OsAbertura> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<OsStatus> statusRepository;
    @Inject
    private Repository<Tecnico> tecnicoRepository;
    @Inject
    private Repository<Cliente> clienteRepository;
    @Inject
    private Repository<OsEquipamento> equipamentoRepository;
    @Inject
    private Repository<VendaCondicoesPagamento> pagamentoRepository;
    @Inject
    private Repository<Vendedor> vendedorRepository;
    @Inject
    private Repository<VendaOrcamentoCabecalho> orcamentoRepository;
    @Inject
    private OsService osService;
    @Inject
    private OsProdutoServicoService produtoServicoService;
    @Inject
    private ProdutoService produtoService;
    @Inject
    private MovimentoService movimentoService;


    private OsAberturaEquipamento osAberturaEquipamento;
    private OsAberturaEquipamento osAberturaEquipamentoSelecionado;

    private OsProdutoServico osProdutoServico;
    private OsProdutoServico osProdutoServicoSelecionado;

    private OsEvolucao osEvolucao;
    private OsEvolucao osEvolucaoSelecionado;

    private TipoPagamento tipoPagamento;
    private List<TipoPagamento> listTipoPagamento;
    private VendaCondicoesPagamento condicaoPagamento;
    private List<VendaCondicoesPagamento> condicoesPagamentos;
    private OsFormaPagamento formaPagamentoSelecionado;


    private PdvMovimento movimento;

    private boolean temProduto;
    private boolean emailValido;
    private boolean exibirCondicoes;

    private String numero;
    private String cliente;
    private Date dataInicial;
    private Date dataFinal;
    private String justificativa;
    private int statusOs;

    private Map<String, Integer> status;

    private boolean podeAlterarPreco = true;

    private Integer idorcamento;

    private BigDecimal valorPago;
    private BigDecimal saldoRestante;
    private BigDecimal totalRecebido;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private BigDecimal desconto = BigDecimal.ZERO;

    private String tipoDesconto = "RS";


    @PostConstruct
    @Override
    public void init() {
        super.init();

        status = getOsStatus().entrySet().stream()
                .filter(x -> !x.getValue().equals(11) || !x.getValue().equals(12) || !x.getValue().equals(13))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        this.podeAlterarPreco = FacesUtil.getUsuarioSessao().getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");

        listTipoPagamento = definirTipoPagament();
    }

    @Override
    public ERPLazyDataModel<OsAbertura> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        dataModel.setAtributos(new Object[]{"numero", "dataInicio", "dataPrevisao", "dataFim", "valorTotal", "cliente.id", "cliente.pessoa.nome", "status", "idnfeCabecalho"});

        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        }

        return dataModel;
    }


    public void pesquisar() {

        dataModel.getFiltros().clear();
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);


        if (!StringUtils.isEmpty(cliente)) {
            dataModel.addFiltro("cliente.pessoa.nome", cliente, Filtro.LIKE);
        }

        if (!StringUtils.isEmpty(numero)) {
            dataModel.addFiltro("numero", numero, Filtro.LIKE);
        }


        if (dataInicial != null) {
            dataModel.addFiltro("dataInicio", dataInicial, Filtro.MAIOR_OU_IGUAL);
        }

        if (dataFinal != null) {
            dataModel.addFiltro("dataInicio", dataFinal, Filtro.MENOR_OU_IGUAL);
        }

        if (statusOs > 0) {
            dataModel.addFiltro("status", statusOs, Filtro.IGUAL);
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


    @Override
    public void doCreate() {
        try {
            PdvMovimento movimento = verificarMovimento();
            if (parametro.getOsGerarMovimentoCaixa().equals("S") && movimento == null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/modulo/comercial/caixa/movimentos.xhtml");
                return;
            } else {
                super.doCreate();
                iniciarValoresPagamento();
                getObjeto().setDataInicio(new Date());
                getObjeto().setHoraInicio(new SimpleDateFormat("HH:mm:ss").format(new Date()));


                getObjeto().setListaOsAberturaEquipamento(new HashSet<>());
                getObjeto().setListaOsProdutoServico(new ArrayList<>());
                getObjeto().setListaOsEvolucao(new HashSet<>());
                getObjeto().setListaFormaPagamento(new HashSet<>());
                getObjeto().setStatus(1);
                getObjeto().setEmpresa(empresa);
                getObjeto().setValorTotal(BigDecimal.ZERO);

                if (parametro.getOsGerarMovimentoCaixa().equals("S")) {
                    getObjeto().setMovimento(movimento);
                }

                temProduto = false;
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao iniciar uma nova os", ex);
            } else {
                throw new RuntimeException("Erro ao iniciar uma nova os", ex);
            }
        }


    }

    @Override
    public void doEdit() {
        try {
            PdvMovimento movimento = verificarMovimento();
            if (parametro.getOsGerarMovimentoCaixa().equals("S") && movimento == null) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/modulo/comercial/caixa/movimentos.xhtml");
                return;
            } else {
                super.doEdit();
                OsAbertura os = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
                os.getTecnico().setNome(os.getTecnico().getColaborador().getPessoa().getNome());
                if (os.getVendedor() != null) {
                    os.getVendedor().setNome(os.getVendedor().getColaborador().getPessoa().getNome());
                }


                setObjeto(os);
                totalReceber = os.getValorTotal();
                verificaSaldoRestante();
                temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
            }
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao iniciar uma nova os", ex);
            } else {
                throw new RuntimeException("Erro ao iniciar uma nova os", ex);
            }
        }


    }

    public void gerarOsDoOrcamento() {
        if (idorcamento != null) {

            VendaOrcamentoCabecalho orcamento = orcamentoRepository.getJoinFetch(idorcamento, VendaOrcamentoCabecalho.class);
            if (orcamento != null) {
                doCreate();
                osService.gerarOSDoOrcamento(orcamento, getObjeto());
            }

        }
    }

    @Override
    public void salvar() {
        try {
            osService.salvar(getObjeto());
            setTelaGrid(false);
            Mensagem.addInfoMessage("OS salvar com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao salvar o servico", e);
        }
    }


    public void encerrar() {
        try {
            OsAbertura os = isTelaGrid() ? dataModel.getRowData(getObjetoSelecionado().getId().toString()) : getObjeto();

            if (os.getListaOsProdutoServico().isEmpty()) {
                throw new ChronosException("Não foram informado produtos ou serviço para a OS");
            }
            osService.encerrar(os);
            setTelaGrid(true);
            Mensagem.addInfoMessage("OS Encerrada com sucesso");


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao faturar o servico", ex);
        }
    }

    public void faturar(String codigoModelo) {

        try {

            OsAbertura os = isTelaGrid() ? dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class) : getObjeto();

            if (os.getListaOsProdutoServico().isEmpty()) {
                throw new ChronosException("Não foram informado produtos ou serviço para a OS");
            }

            ModeloDocumento modelo = codigoModelo.equals("65") ? ModeloDocumento.NFCE : ModeloDocumento.NFE;

            gerarNFe(os, modelo);

            setTelaGrid(true);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gerar faturamento", ex);
            }
        }
    }

    public void gerarNFe(OsAbertura os, ModeloDocumento modelo) throws Exception {

        boolean estoque = isTemAcesso("ESTOQUE");
        osService.transmitirNFe(os, modelo, estoque);
    }

    public void danfe() {
        try {
            osService.gerarDanfe(getObjetoSelecionado());
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao gera o danfe ", ex);
        }
    }

    public void cancelar() {
        try {

            boolean estoque = isTemAcesso("ESTOQUE");
            OsAbertura os = dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class);


            if (os.getStatus().equals(13)) {
                justificativa = "";
                PrimeFaces.current().executeScript("PF('dialogOutrasTelas4').show();");
            } else {
                osService.cancelarOs(os, estoque, "");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao cancelar servico", ex);
        }
    }

    public void cancelarNFe() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            OsAbertura os = dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class);
            osService.cancelarOs(os, estoque, justificativa);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }


    public void incluirOsProdutoServico() {
        osProdutoServico = new OsProdutoServico();
        osProdutoServico.setOsAbertura(getObjeto());
        osProdutoServico.setQuantidade(BigDecimal.ONE);
        desconto = BigDecimal.ZERO;

    }

    public void alterarOsProdutoServico() {
        osProdutoServico = osProdutoServicoSelecionado;
    }

    public void salvarOsProdutoServico() {
        try {

            produtoServicoService.verificarRestricao(osProdutoServico);

            if (produtoServicoService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogOsProdutoServico').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {

                setObjeto(osService.salvarItem(getObjeto(), osProdutoServico, tipoDesconto, desconto));
                temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
                setActiveTabIndex(1);
            }
            totalReceber = getObjeto().getValorTotal();
            verificaSaldoRestante();

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto !", e);

        }
    }

    public void alterarTipoDesconto() {
        tipoDesconto = tipoDesconto.equals("%") ? "R$" : "%";
    }


    @Override
    public boolean autorizacaoSupervisor() {

        try {
            if (osService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                setObjeto(osService.salvarItem(getObjeto(), osProdutoServico, tipoDesconto, desconto));
                Mensagem.addInfoMessage("Produto " + osProdutoServico.getProduto().getNome() + " add com sucesso !");

                temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
                setActiveTabIndex(1);
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

    public void excluirOsProdutoServico() {
        getObjeto().getListaOsProdutoServico().remove(osProdutoServicoSelecionado);
        getObjeto().calcularValores();
        setObjeto(dao.atualizar(getObjeto()));
        totalReceber = getObjeto().getValorTotal();
        verificaSaldoRestante();
        Mensagem.addInfoMessage("Servi/Produto excluído com sucesso!");
        temProduto = getObjeto().getListaOsProdutoServico().size() > 0;

    }


    public void incluirOsAberturaEquipamento() {
        osAberturaEquipamento = new OsAberturaEquipamento();
        osAberturaEquipamento.setOsAbertura(getObjeto());
    }

    public void alterarOsAberturaEquipamento() {
        osAberturaEquipamento = osAberturaEquipamentoSelecionado;
    }

    public void salvarOsAberturaEquipamento() {
        if (osAberturaEquipamento.getId() == null) {
            getObjeto().getListaOsAberturaEquipamento().add(osAberturaEquipamento);
        }
        salvar("Registro salvo com sucesso!");
        setTelaGrid(false);
        setActiveTabIndex(3);
    }

    public void excluirOsAberturaEquipamento() {
        getObjeto().getListaOsAberturaEquipamento().remove(osAberturaEquipamentoSelecionado);
        setObjeto(dao.atualizar(getObjeto()));
        Mensagem.addInfoMessage("Equipamento excluído com sucesso!");
        setTelaGrid(false);
    }

    public void incluirOsEvolucao() {
        osEvolucao = new OsEvolucao();
        osEvolucao.setOsAbertura(getObjeto());
    }

    public void alterarOsEvolucao() {
        osEvolucao = osEvolucaoSelecionado;
    }

    public void salvarOsEvolucao() {
        if (osEvolucao.getId() == null) {
            getObjeto().getListaOsEvolucao().add(osEvolucao);
        }
        setActiveTabIndex(2);
        salvar("Registro salvo com sucesso!");
        setTelaGrid(false);
        if (emailValido && osEvolucao.getEnviarEmail().equals("S")) {
            enviarEmail(getObjeto().getCliente().getPessoa().getEmail(), osEvolucao.getObservacao());
        }

    }


    public void excluirOsEvolucao() {

        getObjeto().getListaOsEvolucao().remove(osEvolucaoSelecionado);
        setObjeto(dao.atualizar(getObjeto()));
        Mensagem.addInfoMessage("Evolução excluído com sucesso!");

    }

    public void lancaPagamento() {
        try {
            boolean update = true;

            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addErrorMessage("Todos os valores já foram recebidos. Finalize a venda.");
            } else {

                incluiPagamento(tipoPagamento, valorPago);

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
        Optional<OsFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent() && tipoPagamento.getPermiteTroco().equals("S")) {
            Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já incluso");
        } else {
            if (totalReceber.compareTo(valorPago) < 0 && tipoPagamento.getPermiteTroco().equals("N")) {
                Mensagem.addInfoMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            } else {
                OsFormaPagamento formaPagamento = new OsFormaPagamento();
                formaPagamento.setOsAbertura(getObjeto());
                formaPagamento.setTipoPagamento(tipoPagamento);
                formaPagamento.setValor(valor);
                formaPagamento.setForma(tipoPagamento.getCodigo());
                formaPagamento.setEstorno("N");

                if (tipoPagamento.getGeraParcelas().equals("S")) {
                    formaPagamento.setCondicao(condicaoPagamento);
                }

                totalRecebido = Biblioteca.soma(totalRecebido, valor);
                troco = Biblioteca.subtrai(totalRecebido, totalReceber);
                if (troco.compareTo(BigDecimal.ZERO) == -1) {
                    troco = BigDecimal.ZERO;
                }
                formaPagamento.setTroco(troco);
                getObjeto().getListaFormaPagamento().add(formaPagamento);
                verificaSaldoRestante();


            }

        }

    }

    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            getObjeto().getListaFormaPagamento().remove(formaPagamentoSelecionado);

            verificaSaldoRestante();
        }
    }

    private Optional<OsFormaPagamento> bucarTipoPagamento(TipoPagamento tipoPagamento) {
        return getObjeto().getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getTipoPagamento().equals(tipoPagamento))
                .findAny();
    }


    public List<OsStatus> getListaOsStatus(String nome) {
        List<OsStatus> listaOsStatus = new ArrayList<>();
        try {
            listaOsStatus = statusRepository.getEntitys(OsStatus.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOsStatus;
    }

    public List<Tecnico> getListaTecnico(String nome) {
        List<Tecnico> tecnicos = new ArrayList<>();
        try {
            tecnicos = tecnicoRepository.getEntitys(Tecnico.class, "colaborador.pessoa.nome", nome, new Object[]{"colaborador.pessoa.nome", "comissao"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return tecnicos;
    }

    public List<Vendedor> getListaVendedor(String nome) {
        List<Vendedor> listaVendedor = new ArrayList<>();
        try {
            listaVendedor = vendedorRepository.getEntitys(Vendedor.class, "colaborador.pessoa.nome", nome, new Object[]{"colaborador.pessoa.nome", "comissao"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVendedor;
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("pessoa.cliente", "S"));
            atributos = new Object[]{"pessoa.nome", "situacaoForCli.nome", "situacaoForCli.bloquear"};
            listaCliente = clienteRepository.getEntitys(Cliente.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCliente;
    }

    public void clienteSelecionado(SelectEvent event) {
        Cliente cliente = (Cliente) event.getObject();
        getObjeto().setCliente(cliente);
    }


    public List<OsEquipamento> getListaOsEquipamento(String nome) {
        List<OsEquipamento> listaOsEquipamento = new ArrayList<>();
        try {
            listaOsEquipamento = equipamentoRepository.getEntitys(OsEquipamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOsEquipamento;
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtoService.getListProdutoVenda(nome, empresa, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public List<VendaCondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            listaVendaCondicoesPagamento = pagamentoRepository.getEntitys(VendaCondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();

        }
        return listaVendaCondicoesPagamento;
    }

    public void selecionaValorProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        osProdutoServico.setValorUnitario(produto.getValorVenda());
    }

    public void definirCondicoess() {
        exibirCondicoes = tipoPagamento.getGeraParcelas().equals("S") && !tipoPagamento.getCodigo().equals("02");


        if (exibirCondicoes) {
            condicoesPagamentos = pagamentoRepository.getEntitys(VendaCondicoesPagamento.class, "vistaPrazo", "1", new Object[]{"nome", "vistaPrazo", "tipoRecebimento"});
        }

    }

    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        for (OsFormaPagamento p : getObjeto().getListaFormaPagamento()) {
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

    private void enviarEmail(String emailEnvio, String msg) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<TipoPagamento> definirTipoPagament() {
        List<TipoPagamento> pagamentos = new ArrayList<>();
        pagamentos.add(new TipoPagamento(1, "01", "DINHEIRO", "S", "N"));
        pagamentos.add(new TipoPagamento(2, "02", "CHEQUE", "N", "N"));
        pagamentos.add(new TipoPagamento(3, "03", "CARTAO DE CREDITO", "N", "N"));
        pagamentos.add(new TipoPagamento(4, "04", "CARTAO DE DEBITO", "N", "N"));
        pagamentos.add(new TipoPagamento(5, "05", "CREDITO NA LOJA", "N", "N"));
        pagamentos.add(new TipoPagamento(6, "14", "DUPLICATA", "N", "S"));

        return pagamentos;
    }

    private void iniciarValoresPagamento() {
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;
        valorPago = BigDecimal.ZERO;
    }

    @Override
    protected Class<OsAbertura> getClazz() {
        return OsAbertura.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OS_ABERTURA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public OsEvolucao getOsEvolucao() {
        return osEvolucao;
    }

    public void setOsEvolucao(OsEvolucao osEvolucao) {
        this.osEvolucao = osEvolucao;
    }

    public OsEvolucao getOsEvolucaoSelecionado() {
        return osEvolucaoSelecionado;
    }

    public void setOsEvolucaoSelecionado(OsEvolucao osEvolucaoSelecionado) {
        this.osEvolucaoSelecionado = osEvolucaoSelecionado;
    }

    public OsAberturaEquipamento getOsAberturaEquipamento() {
        return osAberturaEquipamento;
    }

    public void setOsAberturaEquipamento(OsAberturaEquipamento osAberturaEquipamento) {
        this.osAberturaEquipamento = osAberturaEquipamento;
    }

    public OsAberturaEquipamento getOsAberturaEquipamentoSelecionado() {
        return osAberturaEquipamentoSelecionado;
    }

    public void setOsAberturaEquipamentoSelecionado(OsAberturaEquipamento osAberturaEquipamentoSelecionado) {
        this.osAberturaEquipamentoSelecionado = osAberturaEquipamentoSelecionado;
    }

    public OsProdutoServico getOsProdutoServico() {
        return osProdutoServico;
    }

    public void setOsProdutoServico(OsProdutoServico osProdutoServico) {
        this.osProdutoServico = osProdutoServico;
    }

    public OsProdutoServico getOsProdutoServicoSelecionado() {
        return osProdutoServicoSelecionado;
    }

    public void setOsProdutoServicoSelecionado(OsProdutoServico osProdutoServicoSelecionado) {
        this.osProdutoServicoSelecionado = osProdutoServicoSelecionado;
    }

    public boolean isTemProduto() {
        return temProduto;
    }

    public void setTemProduto(boolean temProduto) {
        this.temProduto = temProduto;
    }

    public boolean isEmailValido() {
        emailValido = false;
        emailValido = !StringUtils.isEmpty(empresa.getEmail())
                && Biblioteca.validarEmail(empresa.getEmail())
                && getObjeto() != null
                && getObjeto().getCliente() != null
                && !StringUtils.isEmpty(getObjeto().getCliente().getPessoa().getEmail());
        if (!emailValido) {
            return emailValido;
        }
        String email = getObjeto().getCliente().getPessoa().getEmail();
        emailValido = Biblioteca.validarEmail(email);
        return emailValido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public Map<String, Integer> getStatus() {
        return status;
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

    public Integer getIdorcamento() {
        return idorcamento;
    }

    public void setIdorcamento(Integer idorcamento) {
        this.idorcamento = idorcamento;
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

    public boolean isExibirCondicoes() {
        return exibirCondicoes;
    }

    public List<VendaCondicoesPagamento> getCondicoesPagamentos() {
        return condicoesPagamentos;
    }

    public VendaCondicoesPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(VendaCondicoesPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0;
    }

    public OsFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(OsFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public String getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public int getStatusOs() {
        return statusOs;
    }

    public void setStatusOs(int statusOs) {
        this.statusOs = statusOs;
    }
}
