package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.modelo.enuns.SituacaoVenda;
import com.chronos.erp.modelo.view.PessoaCliente;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.ProdutoService;
import com.chronos.erp.service.comercial.ItemVendaService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.service.comercial.VendaService;
import com.chronos.erp.service.financeiro.FinLancamentoReceberService;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaCabecalhoControll extends AbstractControll<VendaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<OrcamentoCabecalho> orcamentos;
    @Inject
    private Repository<CondicoesPagamento> condicoes;
    @Inject
    private Repository<Transportadora> transportadora;
    @Inject
    private Repository<NotaFiscalTipo> tipos;
    @Inject
    private Repository<PessoaCliente> clientes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;
    @Inject
    private NfeService nfeService;

    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private VendaService vendaService;
    @Inject
    private ItemVendaService itemService;
    @Inject
    private ProdutoService produtoService;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private Repository<SituacaoForCli> situacaoForCliRepository;
    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;
    @Inject
    private Repository<CondicoesPagamento> pagamentoRepository;

    private ProdutoDTO produto;


    private VendaDetalhe vendaDetalhe;
    private VendaDetalhe vendaDetalheSelecionado;

    private PessoaCliente pessoaCliente;


    private Produto produto2;

    private String justificativa;

    private Date dataInicial, dataFinal;
    private Date dataEntregaInicial, dataEntregaFinal;
    private String nome;
    private String situacao;
    private Integer idmepresaFiltro;
    private Map<String, String> status;

    private String tipoAutorizacao = "P";

    private TributOperacaoFiscal operacaoFiscal;

    private int tipoDesconto;
    private BigDecimal desconto;
    private boolean podeAlterarPreco = true;

    private EstoqueCor cor;
    private EstoqueTamanho tamanho;
    private List<EstoqueCor> cores;
    private List<EstoqueTamanho> tamanhos;
    private List<EstoqueGrade> grades;
    private boolean exibirGrade = false;

    private TipoPagamento tipoPagamento;
    private List<TipoPagamento> listTipoPagamento;
    private CondicoesPagamento condicaoPagamento;
    private List<CondicoesPagamento> condicoesPagamentos;
    private VendaFormaPagamento formaPagamentoSelecionado;
    private boolean exibirCondicoes;

    private BigDecimal valorPago;
    private BigDecimal saldoRestante;
    private BigDecimal totalRecebido;
    private BigDecimal totalReceber;
    private BigDecimal troco;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        idmepresaFiltro = empresa.getId();
        status = new LinkedHashMap<>();
        status.put("Todos", "");
        status.put("Faturada", "F");
        status.put("Encerrada", "E");
        status.put("Cancelada", "C");

        this.podeAlterarPreco = FacesUtil.getUsuarioSessao().getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");

        pesquisarEmpresas();

    }

    @Override
    public ERPLazyDataModel<VendaCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(VendaCabecalho.class);
        }

        dataModel.setAtributos(new Object[]{"dataVenda", "numeroFatura", "valorTotal", "situacao", "cliente.pessoa.nome"});
        dataModel.getFiltros().clear();
        pesquisar();
        return dataModel;
    }


    public void pesquisar() {

        if (dataInicial != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "dataVenda", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }
        if (dataFinal != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "dataVenda", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (dataEntregaInicial != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "vendaOrcamentoCabecalho.dataEntrega", Filtro.MAIOR_OU_IGUAL, dataEntregaInicial));
        }
        if (dataEntregaFinal != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "vendaOrcamentoCabecalho.dataEntrega", Filtro.MENOR_OU_IGUAL, dataEntregaFinal));
        }

        if (!StringUtils.isEmpty(nome)) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "cliente.pessoa.nome", Filtro.LIKE, nome));
        }

        if (!StringUtils.isEmpty(situacao) && !situacao.equals("T")) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "situacao", Filtro.IGUAL, situacao));
        }
        idmepresaFiltro = idmepresaFiltro == null || idmepresaFiltro == 0 ? empresa.getId() : idmepresaFiltro;
        dataModel.getFiltros().add(new Filtro("empresa.id", idmepresaFiltro));

    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        pessoaCliente = null;
        incluirVendaDetalhe();
        listTipoPagamento = definirTipoPagament();
        iniciarValoresPagamento();
    }

    @Override
    public void doEdit() {
        super.doEdit();
        VendaCabecalho venda = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(venda);
        pessoaCliente = new PessoaCliente();
        pessoaCliente.setNome(getObjeto().getCliente().getPessoa().getNome());
        incluirVendaDetalhe();
        listTipoPagamento = definirTipoPagament();

        for (VendaDetalhe item : venda.getListaVendaDetalhe()) {
            if (item.getIdgrade() != null) {
                EstoqueGrade grade = estoqueGradeRepository.get(item.getIdgrade(), EstoqueGrade.class);

                String nome = item.getProduto().getNome() + " COR " + grade.getEstoqueCor().getNome() + " TAM " + grade.getEstoqueTamanho().getNome();
                item.getProduto().setNome(nome);


            }
        }
    }

    @Override
    public void salvar() {

        try {

            VendaCabecalho venda = vendaService.salvar(getObjeto());
            setObjeto(venda);
            Mensagem.addInfoMessage("Pedido de venda salvo");

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro salvar o pedido venda", ex);
            }
        }
    }

    public void incluirVendaDetalhe() {
        vendaDetalhe = new VendaDetalhe();
        vendaDetalhe.setVendaCabecalho(getObjeto());
        vendaDetalhe.setQuantidade(BigDecimal.ONE);
        produto = new ProdutoDTO();
        exibirGrade = false;
    }

    public void definirValorProduto(SelectEvent event) {
        produto = (ProdutoDTO) event.getObject();
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        vendaDetalhe.setValorUnitario(precoVenda);

        exibirGrade = false;
        if (produto != null && produto.getPossuiGrade()) {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("idproduto", produto.getId()));
            filtros.add(new Filtro("idempresa", empresa.getId()));

            grades = estoqueGradeRepository.getEntitys(EstoqueGrade.class, filtros);

            if (!grades.isEmpty()) {
                cores = grades.stream().map(g -> g.getEstoqueCor()).collect(Collectors.toList());
                exibirGrade = true;
            }

        }
    }

    public void definirTamanhos() {
        if (cor != null) {
            tamanhos = grades.stream()
                    .filter(g -> g.getEstoqueCor().getId().equals(cor.getId()))
                    .map(t -> t.getEstoqueTamanho()).collect(Collectors.toList());
        }
    }

    public void definirValorAtacado() {
        produto.setQuantidadeVenda(vendaDetalhe.getQuantidade());
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        vendaDetalhe.setValorUnitario(precoVenda);
    }

    public void alterarTipoDesconto() {
        tipoDesconto = tipoDesconto == 0 ? 1 : 0;
    }

    public void alterarVendaDetalhe() {
        vendaDetalhe = vendaDetalheSelecionado;
    }

    public void salvarVendaDetalhe() {


        try {
            vendaDetalhe.setProduto(new Produto(produto.getId(), produto.getNome()));

            if (exibirGrade) {
                Optional<EstoqueGrade> first = grades.stream()
                        .filter(g -> g.getEstoqueCor().getId().equals(cor.getId()) && g.getEstoqueTamanho().getId().equals(tamanho.getId()))
                        .findFirst();

                if (!first.isPresent()) {
                    throw new ChronosException("Grade não localizada");
                }

                String nome = vendaDetalhe.getProduto().getNome() + " COR " + cor.getNome() + " TAM " + tamanho.getNome();
                vendaDetalhe.getProduto().setNome(nome);
                vendaDetalhe.setIdgrade(first.get().getId());
            }


            itemService.verificarRestricao(vendaDetalhe);

            if (itemService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogVendaDetalhe').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                vendaService.addItem(getObjeto(), vendaDetalhe, desconto, tipoDesconto);
                incluirVendaDetalhe();
                totalReceber = getObjeto().getValorTotal();
                verificaSaldoRestante();
                desconto = BigDecimal.ZERO;
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao add o item da venda", ex);
            }
        }

    }

    public void excluirVendaDetalhe() {

        getObjeto().getListaVendaDetalhe().remove(vendaDetalheSelecionado);
        getObjeto().calcularValorTotal();
        getObjeto().setExcludoItem(true);

    }


    public void definirCondicoess() {
        exibirCondicoes = tipoPagamento.getGeraParcelas().equals("S") && !tipoPagamento.getCodigo().equals("02");


        if (exibirCondicoes) {
            condicoesPagamentos = pagamentoRepository.getEntitys(CondicoesPagamento.class, "vistaPrazo", "1", new Object[]{"nome", "vistaPrazo", "tipoRecebimento"});
        }

    }

    public void lancaPagamento() {
        try {


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


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            getObjeto().getListaFormaPagamento().remove(formaPagamentoSelecionado);

            verificaSaldoRestante();
        }
    }


    @Override
    public boolean autorizacaoSupervisor() {

        try {
            if (vendaService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                switch (tipoAutorizacao) {
                    case "P":
                        vendaService.addItem(getObjeto(), vendaDetalhe, desconto, tipoDesconto);
                        break;

                    default:
                        vendaService.salvar(getObjeto());
                }
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

    public void buscarEncerrarVenda() {

        try {
            VendaCabecalho venda = dataModel.getRowData(getObjetoSelecionado().getId().toString());

            if (venda.getListaVendaDetalhe().isEmpty()) {
                throw new ChronosException("Não foram informado produtos para essa venda");
            }

            setObjeto(venda);
            encerrarVenda();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao encerrar a venda", ex);
            }

        }


    }

    public void encerrarVenda() {
        try {
            vendaService.faturarVenda(getObjeto());
            Mensagem.addInfoMessage("Venda encerrada com sucesso");
            setTelaGrid(true);
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao encerrar a venda", ex);
            }


        }
    }


    public void faturarVenda() {
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!getObjeto().getListaVendaDetalhe().isEmpty()) {


            for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                if (item.getIdgrade() != null) {
                    EstoqueGrade grade = estoqueGradeRepository.get(item.getIdgrade(), EstoqueGrade.class);

                    String nome = item.getProduto().getNome() + " COR " + grade.getEstoqueCor().getNome() + " TAM " + grade.getEstoqueTamanho().getNome();
                    item.getProduto().setNome(nome);


                }
            }

            vendaService.transmitirNFe(getObjeto(), ModeloDocumento.NFE, estoque);
        }
    }

    public void gerarNFe() {
        ModeloDocumento modelo = ModeloDocumento.NFE;
        VendaCabecalho venda = dao.getJoinFetch(getObjetoSelecionado().getId(), VendaCabecalho.class);
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!venda.getListaVendaDetalhe().isEmpty()) {
            vendaService.transmitirNFe(venda, modelo, estoque);
        }

    }


    public void gerarNfce() {
        ModeloDocumento modelo = ModeloDocumento.NFCE;
        VendaCabecalho venda = dao.getJoinFetch(getObjetoSelecionado().getId(), VendaCabecalho.class);
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!venda.getListaVendaDetalhe().isEmpty()) {
            vendaService.transmitirNFe(venda, modelo, estoque);
        }
    }

    public void danfe() {
        try {
            int idnfe = getObjetoSelecionado().getNumeroFatura();
            NfeCabecalho nfe = nfeRepository.get(idnfe, NfeCabecalho.class);

            nfeService.danfe(nfe);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    @Transactional
    public void cancelarVenda() {


        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(getObjetoSelecionado().getSituacao());
            VendaCabecalho venda = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
            if (situacao == SituacaoVenda.Encerrado) {

                setObjeto(venda);
                getObjeto().setSituacao(SituacaoVenda.CANCELADA.getCodigo());
                finLancamentoReceberService.excluirFinanceiro(new DecimalFormat("VD0000000").format(getObjetoSelecionado().getId()), Modulo.VENDA);
                salvar();
                for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                }
                auditoriaService.gerarLog(AcaoLog.CANCELAR_VENDA, "Venda cancelada", " VENDAS");
            } else if (situacao == SituacaoVenda.Faturado) {
                setObjeto(venda);
                NfeCabecalho nfe = nfeRepository.get(getObjeto().getNumeroFatura(), NfeCabecalho.class);
                nfe.setJustificativaCancelamento(justificativa);

                boolean estoque = isTemAcesso("ESTOQUE");
                boolean cancelada = nfeService.cancelarNFe(nfe, estoque);
                if (cancelada) {
                    finLancamentoReceberService.excluirFinanceiro(new DecimalFormat("VD0000000").format(getObjetoSelecionado().getId()), Modulo.VENDA);
                    getObjeto().setSituacao(SituacaoVenda.CANCELADA.getCodigo());
                    Map<String, Object> atributos = new HashMap<>();
                    atributos.put("situacao", SituacaoVenda.CANCELADA.getCodigo());
                    List<Filtro> filtros = new LinkedList<>();
                    filtros.add(new Filtro("id", getObjeto().getId()));
                    dao.updateNativo(VendaCabecalho.class, filtros, atributos);
                    if (estoque) {
                        for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                            estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                        }
                    }

                    auditoriaService.gerarLog(AcaoLog.CANCELAR_VENDA, "Venda cancelada por " + justificativa, " VENDAS");

                }

            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao cancelar a venda", ex);
            }
        }
    }

    public void exibirDevolucao() {
        VendaCabecalho venda = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(venda);
        operacaoFiscal = null;

        getObjeto().getListaVendaDetalhe().forEach(i -> i.setQuantidadeDevolvida(i.getQuantidade()));
    }

    public void definirQuantidadeDevolvida() {

    }


    public void gerarDevolucao() {

        try {


            vendaService.gerarDevolucao(getObjeto(), operacaoFiscal);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                throw new RuntimeException("erro ao gerar a devolucao", ex);
            }
        }

    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("descricao", Filtro.LIKE, descricao));
            listaTributOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, filtros, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoInss", "estoque", "estoqueVerificado"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }


    public List<OrcamentoCabecalho> getListaVendaOrcamentoCabecalho(String nome) {
        List<OrcamentoCabecalho> listaOrcamentoCabecalho = new ArrayList<>();
        try {
            listaOrcamentoCabecalho = orcamentos.getEntitys(OrcamentoCabecalho.class, "codigo", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOrcamentoCabecalho;
    }

    public List<CondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<CondicoesPagamento> listaCondicoesPagamento = new ArrayList<>();
        try {
            Object[] join = new Object[]{""};
            listaCondicoesPagamento = condicoes.getEntitys(CondicoesPagamento.class, "nome", nome, new Object[]{"nome", "vistaPrazo"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCondicoesPagamento;
    }

    public List<Transportadora> getListaTransportadora(String nome) {
        List<Transportadora> listaTransportadora = new ArrayList<>();
        try {
            listaTransportadora = transportadora.getEntitys(Transportadora.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTransportadora;
    }

    public List<NotaFiscalTipo> getListaTipoNotaFiscal(String nome) {
        List<NotaFiscalTipo> listaTipoNotaFiscal = new ArrayList<>();
        try {
            listaTipoNotaFiscal = tipos.getEntitys(NotaFiscalTipo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoNotaFiscal;
    }

    public List<PessoaCliente> getListaCliente(String nome) {
        List<PessoaCliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public void clienteSelecionado(SelectEvent event) {
        Cliente cliente = (Cliente) event.getObject();
        getObjeto().setCliente(cliente);
        pessoaCliente = new PessoaCliente();
        pessoaCliente.setNome(cliente.getPessoa().getNome());
    }

    public List<Vendedor> getListaVendedor(String nome) {
        List<Vendedor> listaVendedor = new ArrayList<>();
        try {
            listaVendedor = vendedores.getEntitys(Vendedor.class, "colaborador.pessoa.nome", nome, new Object[]{"colaborador.pessoa.nome", "comissao"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVendedor;
    }

    public List<ProdutoDTO> getListaProduto(String nome) {
        List<ProdutoDTO> listaProduto = new ArrayList<>();

        try {

            listaProduto = produtoService.getListaProdutoDTO(getObjeto().getEmpresa(), nome, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;

    }


    public void definirEnderecoEntrega(SelectEvent event) {
        PessoaCliente pessoaCliente = (PessoaCliente) event.getObject();
        SituacaoForCli situacao = situacaoForCliRepository.get(pessoaCliente.getIdSituacaoForCli(), SituacaoForCli.class);
        Cliente cliente = new Cliente();
        cliente.setSituacaoForCli(situacao);
        cliente.setId(pessoaCliente.getId());
        getObjeto().setCliente(cliente);
        String endereco = "End: " + pessoaCliente.getLogradouro() + "," + pessoaCliente.getNumero() + " ";
        endereco += "Bairro :" + pessoaCliente.getBairro() + " Cep: " + pessoaCliente.getCep() + " - ";
        endereco += pessoaCliente.getCidade() + "/" + pessoaCliente.getUf();
        getObjeto().setLocalEntrega(endereco);
        getObjeto().setLocalCobranca(endereco);
    }


    public void definirTaxaComissao(SelectEvent event) {
        Vendedor vendedor = (Vendedor) event.getObject();
        getObjeto().setTaxaComissao(vendedor.getComissao());
    }


    public void aplicarDesconto() {

        try {
            vendaService.aplicarDesconto(getObjeto(), tipoDesconto, desconto);
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
        vendaService.removerDesconto(getObjeto());
        desconto = BigDecimal.ZERO;
    }

    private void incluiPagamento(TipoPagamento tipoPagamento, BigDecimal valor) throws ChronosException {
        Optional<VendaFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent() && tipoPagamento.getPermiteTroco().equals("S")) {
            Mensagem.addErrorMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já inclusa");
        } else {
            if (totalReceber.compareTo(valorPago) < 0 && tipoPagamento.getPermiteTroco().equals("N")) {
                Mensagem.addErrorMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            } else {
                VendaFormaPagamento formaPagamento = new VendaFormaPagamento();
                formaPagamento.setVendaCabecalho(getObjeto());
                formaPagamento.setTipoPagamento(tipoPagamento);
                formaPagamento.setValor(valor);
                formaPagamento.setForma(tipoPagamento.getCodigo());
                formaPagamento.setEstorno("N");

                if (formaPagamento.getForma().equals("14")) {
                    formaPagamento.setCondicoesPagamento(condicaoPagamento);
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

    private Optional<VendaFormaPagamento> bucarTipoPagamento(TipoPagamento tipoPagamento) {
        return getObjeto().getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getTipoPagamento().equals(tipoPagamento))
                .findAny();
    }

    private void verificaSaldoRestante() {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        for (VendaFormaPagamento p : getObjeto().getListaFormaPagamento()) {
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

    private void iniciarValoresPagamento() {
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;
        valorPago = BigDecimal.ZERO;
    }


    @Override
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public VendaDetalhe getVendaDetalhe() {
        return vendaDetalhe;
    }

    public void setVendaDetalhe(VendaDetalhe vendaDetalhe) {
        this.vendaDetalhe = vendaDetalhe;
    }

    public VendaDetalhe getVendaDetalheSelecionado() {
        return vendaDetalheSelecionado;
    }

    public void setVendaDetalheSelecionado(VendaDetalhe vendaDetalheSelecionado) {
        this.vendaDetalheSelecionado = vendaDetalheSelecionado;
    }

    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public Produto getProduto2() {
        return produto2;
    }

    public void setProduto2(Produto produto2) {
        this.produto2 = produto2;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public TributOperacaoFiscal getOperacaoFiscal() {
        return operacaoFiscal;
    }

    public void setOperacaoFiscal(TributOperacaoFiscal operacaoFiscal) {
        this.operacaoFiscal = operacaoFiscal;
    }

    public int getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(int tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public boolean isPodeAlterarPreco() {
        return podeAlterarPreco;
    }

    public Date getDataEntregaInicial() {
        return dataEntregaInicial;
    }

    public void setDataEntregaInicial(Date dataEntregaInicial) {
        this.dataEntregaInicial = dataEntregaInicial;
    }

    public Date getDataEntregaFinal() {
        return dataEntregaFinal;
    }

    public void setDataEntregaFinal(Date dataEntregaFinal) {
        this.dataEntregaFinal = dataEntregaFinal;
    }

    public Integer getIdmepresaFiltro() {
        return idmepresaFiltro;
    }

    public void setIdmepresaFiltro(Integer idmepresaFiltro) {
        this.idmepresaFiltro = idmepresaFiltro;
    }

    public EstoqueCor getCor() {
        return cor;
    }

    public void setCor(EstoqueCor cor) {
        this.cor = cor;
    }

    public EstoqueTamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(EstoqueTamanho tamanho) {
        this.tamanho = tamanho;
    }

    public List<EstoqueCor> getCores() {
        return cores;
    }

    public List<EstoqueTamanho> getTamanhos() {
        return tamanhos;
    }

    public boolean isExibirGrade() {
        return exibirGrade;
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

    public CondicoesPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicoesPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public List<CondicoesPagamento> getCondicoesPagamentos() {
        return condicoesPagamentos;
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

    public VendaFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(VendaFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
    }
}
