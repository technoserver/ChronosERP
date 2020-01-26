package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.SituacaoOrcamentoPedido;
import com.chronos.erp.modelo.enuns.TipoFrete;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.OrcamentoService;
import com.chronos.erp.service.comercial.VendedorService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class OrcamentoCabecalhoControll extends AbstractControll<OrcamentoCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<CondicoesPagamento> condicoes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Transportadora> transportadoras;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<Produto> produtos;

    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;

    @Inject
    private VendedorService vendedorService;

    @Inject
    private OrcamentoService service;

    @Inject
    private EstoqueRepository estoqueRepository;
    @Inject
    private Repository<CondicoesPagamento> pagamentoRepository;


    private OrcamentoDetalhe orcamentoDetalhe;
    private OrcamentoDetalhe orcamentoDetalheSelecionado;
    private String tipo;

    private Date dataInicial;
    private Date dataFinal;
    private String cliente;
    private String codigo;
    private String status;
    private Integer idvendedor;
    private Map<String, Integer> listaVendedor;
    private Map<String, String> situacoes;
    private Integer idmepresaFiltro;

    private int tipoDesconto;
    private BigDecimal desconto;

    private boolean podeAlterarPreco = true;

    private TipoPagamento tipoPagamento;
    private List<TipoPagamento> listTipoPagamento;
    private CondicoesPagamento condicaoPagamento;
    private List<CondicoesPagamento> condicoesPagamentos;
    private OrcamentoFormaPagamento formaPagamentoSelecionado;
    private boolean exibirCondicoes;

    private BigDecimal valorPago;
    private BigDecimal saldoRestante;
    private BigDecimal totalRecebido;
    private BigDecimal totalReceber;
    private BigDecimal troco;
    private boolean exibirGrade = false;


    private EstoqueCor cor;
    private EstoqueTamanho tamanho;
    private List<EstoqueCor> cores;
    private List<EstoqueTamanho> tamanhos;
    private List<EstoqueGrade> grades;



    @PostConstruct
    @Override
    public void init() {
        super.init();
        idvendedor = 0;
        listaVendedor = vendedorService.getMapVendedores();

        situacoes = new LinkedHashMap<>();
        situacoes.put("Todos", "");
        situacoes.putAll(getOrcamentoSituacao());

        this.podeAlterarPreco = FacesUtil.getUsuarioSessao().getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");

        pesquisarEmpresas();

    }

    @Override
    public ERPLazyDataModel<OrcamentoCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(OrcamentoCabecalho.class);
            dataModel.setDao(dao);
        }

        pesquisar();
        return dataModel;
    }

    public void pesquisar() {
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipo", Optional.ofNullable(tipo).orElse("O"), Filtro.IGUAL);

        if (dataInicial != null) {
            dataModel.addFiltro("dataCadastro", dataInicial, Filtro.MAIOR_OU_IGUAL);
        }

        if (dataFinal != null) {
            dataModel.addFiltro("dataCadastro", dataInicial, Filtro.MENOR_OU_IGUAL);
        }

        if (!org.springframework.util.StringUtils.isEmpty(cliente)) {
            dataModel.addFiltro("cliente.pessoa.nome", cliente, Filtro.LIKE);
        }

        if (idvendedor > 0) {
            dataModel.addFiltro("vendedor.id", idvendedor, Filtro.IGUAL);
        }

        if (!org.springframework.util.StringUtils.isEmpty(codigo)) {
            dataModel.addFiltro("codigo", codigo, Filtro.IGUAL);
        }

        if (!org.springframework.util.StringUtils.isEmpty(status)) {
            dataModel.addFiltro("situacao", status, Filtro.IGUAL);
        }
        idmepresaFiltro = idmepresaFiltro == null || idmepresaFiltro == 0 ? empresa.getId() : idmepresaFiltro;
        dataModel.getFiltros().add(new Filtro("empresa.id", idmepresaFiltro));
    }

    @Override
    public void doCreate() {
        super.doCreate();
        iniciarValoresPagamento();
        listTipoPagamento = definirTipoPagament();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaOrcamentoDetalhe(new ArrayList<>());
        getObjeto().setListaFormaPagamento(new HashSet<>());
        getObjeto().setSituacao(SituacaoOrcamentoPedido.PENDENTE.getCodigo());
        getObjeto().setTipoFrete(TipoFrete.CIF.getCodigo());
        getObjeto().setDataCadastro(new Date());
        getObjeto().setDataEntrega(new Date());
        getObjeto().setTipo(Optional.ofNullable(tipo).orElse("O"));

        incluirVendaOrcamentoDetalhe();
    }

    @Override
    public void doEdit() {
        super.doEdit();
        listTipoPagamento = definirTipoPagament();
        OrcamentoCabecalho orcamento = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(orcamento);
        incluirVendaOrcamentoDetalhe();
        for (OrcamentoDetalhe item : orcamento.getListaOrcamentoDetalhe()) {
            if (item.getIdgrade() != null) {
                EstoqueGrade grade = estoqueGradeRepository.get(item.getIdgrade(), EstoqueGrade.class);

                String nome = item.getProduto().getNome() + " COR " + grade.getEstoqueCor().getNome() + " TAM " + grade.getEstoqueTamanho().getNome();
                item.getProduto().setNome(nome);


            }
        }
        totalReceber = orcamento.getValorTotal();
        verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));
    }

    @Override
    public void salvar() {
        try {

            OrcamentoCabecalho orcamento = service.salvar(getObjeto());
            setObjeto(orcamento);
            Mensagem.addInfoMessage("Registro salvo com sucesso");
            setTelaGrid(false);
        } catch (Exception e) {

            if (e instanceof ChronosException) {
                Mensagem.addErrorMessage("", e);
            } else {
                throw new RuntimeException("Ocorreu um erro ao tenta salvar", e);
            }

        }
    }

    @Override
    public void remover() {
        try {
            service.remover(getObjetoSelecionado());
            Mensagem.addInfoMessage("Registro removido com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao tenta remover", ex);
            }

        }
    }

    public void gerarPedido() {
        OrcamentoCabecalho orcamento = isTelaGrid() ? dataModel.getRowData(getObjetoSelecionado().getId().toString()) : getObjeto();
        orcamento.setTipo("P");
        orcamento.setSituacao("A");
        String codigo = orcamento.getTipo().equals("P") ? "#PE" : "#OE";
        codigo += StringUtils.leftPad(orcamento.getId().toString(), 3, "0");
        orcamento.setCodigo(codigo);
        dao.atualizar(orcamento);
        setTelaGrid(true);
    }

    public void aprovarPedido() {

        OrcamentoCabecalho orcamento;

        if (isTelaGrid()) {
            orcamento = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        } else {
            orcamento = getObjeto();
        }

        orcamento.setSituacao(SituacaoOrcamentoPedido.APROVADO.getCodigo());

        dao.atualizar(orcamento);
    }

    public void gerarVenda() {
        try {
            OrcamentoCabecalho orc = getObjeto() != null ? getObjeto() : dataModel.getRowData(getObjetoSelecionado().getId().toString());

            if (orc.getListaOrcamentoDetalhe() == null || orc.getListaOrcamentoDetalhe().isEmpty()) {
                throw new ChronosException("Itens do orcamento não definidos");
            }

            OrcamentoCabecalho orcamento = service.conveterEmVenda(orc);
            setObjeto(orcamento);
            setTelaGrid(true);
            Mensagem.addInfoMessage("Venda gerada com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao gerar a venda", ex);
            }
        }
    }


    public void duplicar() {
        try {
            OrcamentoCabecalho orc = dataModel.getRowData(getObjetoSelecionado().getId().toString());
            OrcamentoCabecalho novoOrc = service.duplicar(orc);
            String msg = novoOrc.getTipo().equals("O") ? "Orçamento " : "Pedido ";
            msg += "duplicado com sucesso";
            Mensagem.addInfoMessage(msg);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao gerar a venda", ex);
            }
        }
    }


    public void incluirVendaOrcamentoDetalhe() {
        orcamentoDetalhe = new OrcamentoDetalhe();
        orcamentoDetalhe.setOrcamentoCabecalho(getObjeto());
        orcamentoDetalhe.setQuantidade(BigDecimal.ONE);
        desconto = BigDecimal.ZERO;
        exibirGrade = false;
    }

    public void alterarVendaOrcamentoDetalhe() {
        orcamentoDetalhe = orcamentoDetalheSelecionado;
    }

    public void salvarVendaOrcamentoDetalhe() {

        try {

            if (exibirGrade) {
                Optional<EstoqueGrade> first = grades.stream()
                        .filter(g -> g.getEstoqueCor().getId().equals(cor.getId()) && g.getEstoqueTamanho().getId().equals(tamanho.getId()))
                        .findFirst();

                if (!first.isPresent()) {
                    throw new ChronosException("Grade não localizada");
                }

                String nome = orcamentoDetalhe.getProduto().getNome() + " COR " + cor.getNome() + " TAM " + tamanho.getNome();
                orcamentoDetalhe.getProduto().setNome(nome);
                orcamentoDetalhe.setIdgrade(first.get().getId());
            }

            service.salvarItem(getObjeto(), orcamentoDetalhe, desconto, tipoDesconto);
            desconto = BigDecimal.ZERO;
            incluirVendaOrcamentoDetalhe();
            totalReceber = getObjeto().getValorTotal();
            verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o item", ex);
            }
        }


    }


    public void alterarTipoDesconto() {
        tipoDesconto = tipoDesconto == 0 ? 1 : 0;
    }


    public void excluirVendaOrcamentoDetalhe() {


        getObjeto().getListaOrcamentoDetalhe().remove(orcamentoDetalheSelecionado);
        getObjeto().calcularValorTotal();


    }

    public void definirValorVenda() {


        BigDecimal valor = Optional.ofNullable(orcamentoDetalhe.getProduto())
                .map(Produto::getValorVenda).orElse(null);
        orcamentoDetalhe.setValorUnitario(valor);

        exibirGrade = false;
        if (orcamentoDetalhe.getProduto() != null && orcamentoDetalhe.getProduto().getPossuiGrade() != null && orcamentoDetalhe.getProduto().getPossuiGrade()) {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("idproduto", orcamentoDetalhe.getProduto().getId()));
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

    public void lancaPagamento() {
        try {


            if (saldoRestante.compareTo(BigDecimal.ZERO) <= 0) {
                Mensagem.addErrorMessage("Todos os valores já foram recebidos. Finalize o orçamento.");
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

    public void definirCondicoess() {
        exibirCondicoes = tipoPagamento.getGeraParcelas().equals("S") && !tipoPagamento.getCodigo().equals("02");


        if (exibirCondicoes) {
            condicoesPagamentos = pagamentoRepository.getEntitys(CondicoesPagamento.class, "vistaPrazo", "1", new Object[]{"nome", "vistaPrazo", "tipoRecebimento"});
        }

    }

    private void incluiPagamento(TipoPagamento tipoPagamento, BigDecimal valor) {
        Optional<OrcamentoFormaPagamento> formaPagamentoOpt = bucarTipoPagamento(tipoPagamento);
        if (formaPagamentoOpt.isPresent() && tipoPagamento.getPermiteTroco().equals("S")) {
            Mensagem.addErrorMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " já inclusa");
        } else {
            if (totalReceber.compareTo(valorPago) < 0 && tipoPagamento.getPermiteTroco().equals("N")) {
                Mensagem.addErrorMessage("Forma de pagamento " + tipoPagamento.getDescricao() + " não permite troco");
            } else {
                OrcamentoFormaPagamento formaPagamento = new OrcamentoFormaPagamento();

                FormaPagamento forma = new FormaPagamento();

                formaPagamento.setFormaPagamento(forma);

                formaPagamento.setOrcamentoCabecalho(getObjeto());
                forma.setTipoPagamento(tipoPagamento);
                forma.setValor(valor);
                forma.setForma(tipoPagamento.getCodigo());
                forma.setEstorno("N");

                if (forma.getForma().equals("14")) {
                    forma.setCondicoesPagamento(condicaoPagamento);
                }

                if (forma.getForma().equals("03") || forma.getForma().equals("04")) {
                    forma.setCartaoTipoIntegracao("2");
                }

                totalRecebido = Biblioteca.soma(totalRecebido, valor);
                troco = Biblioteca.subtrai(totalRecebido, totalReceber);
                if (troco.compareTo(BigDecimal.ZERO) == -1) {
                    troco = BigDecimal.ZERO;
                }
                forma.setTroco(troco);
                getObjeto().getListaFormaPagamento().add(formaPagamento);
                verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));


            }

        }

    }

    private void verificaSaldoRestante(List<FormaPagamento> pagamentos) {
        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        for (FormaPagamento p : pagamentos) {
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

    private Optional<OrcamentoFormaPagamento> bucarTipoPagamento(TipoPagamento tipoPagamento) {
        return getObjeto().getListaFormaPagamento()
                .stream()
                .filter(fp -> fp.getFormaPagamento().getTipoPagamento().equals(tipoPagamento))
                .findAny();
    }


    public void excluirPagamento() {
        if (formaPagamentoSelecionado != null) {
            getObjeto().getListaFormaPagamento().remove(formaPagamentoSelecionado);

            verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));
        }
    }


    public List<CondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<CondicoesPagamento> listaCondicoesPagamento = new ArrayList<>();
        try {
            listaCondicoesPagamento = condicoes.getEntitys(CondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCondicoesPagamento;
    }

    public List<Vendedor> getListaVendedor(String nome) {
        List<Vendedor> listaVendedor = new ArrayList<>();
        try {
            listaVendedor = vendedores.getEntitys(Vendedor.class, "colaborador.pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendedor;
    }

    public void definirTaxaComissao(SelectEvent event) {
        Vendedor vendedor = (Vendedor) event.getObject();
        getObjeto().setTaxaComissao(vendedor.getComissao());
    }


    public List<Transportadora> getListaTransportadora(String nome) {
        List<Transportadora> listaTransportadora = new ArrayList<>();
        try {
            listaTransportadora = transportadoras.getEntitys(Transportadora.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTransportadora;
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public void clienteSelecionado(SelectEvent event) {
        Cliente cliente = (Cliente) event.getObject();
        getObjeto().setCliente(cliente);
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            List<ProdutoDTO> list = estoqueRepository.getProdutoDTO(nome, empresa, "S");
            listaProduto = list.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }

    public void aplicarDesconto() {

        try {
            service.aplicarDesconto(getObjeto(), tipoDesconto, desconto);
            desconto = BigDecimal.ZERO;
            getObjeto().getListaFormaPagamento().clear();
            totalReceber = getObjeto().getValorTotal();
            verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));
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
        service.removerDesconto(getObjeto());
        desconto = BigDecimal.ZERO;
        getObjeto().getListaFormaPagamento().clear();
        totalReceber = getObjeto().getValorTotal();
        verificaSaldoRestante(getObjeto().getListaFormaPagamento().stream().map(f -> f.getFormaPagamento()).collect(Collectors.toList()));
    }

    private void iniciarValoresPagamento() {
        totalReceber = BigDecimal.ZERO;
        troco = BigDecimal.ZERO;
        totalRecebido = BigDecimal.ZERO;
        saldoRestante = BigDecimal.ZERO;
        valorPago = BigDecimal.ZERO;
    }

    @Override
    protected Class<OrcamentoCabecalho> getClazz() {
        return OrcamentoCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_ORCAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public boolean isPodeLancaPagamento() {
        return valorPago.signum() == 0;
    }

    public OrcamentoDetalhe getOrcamentoDetalhe() {
        return orcamentoDetalhe;
    }

    public void setOrcamentoDetalhe(OrcamentoDetalhe orcamentoDetalhe) {
        this.orcamentoDetalhe = orcamentoDetalhe;
    }

    public OrcamentoDetalhe getOrcamentoDetalheSelecionado() {
        return orcamentoDetalheSelecionado;
    }

    public void setOrcamentoDetalheSelecionado(OrcamentoDetalhe orcamentoDetalheSelecionado) {
        this.orcamentoDetalheSelecionado = orcamentoDetalheSelecionado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public Map<String, Integer> getListaVendedor() {
        return listaVendedor;
    }

    public Map<String, String> getSituacoes() {
        return situacoes;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public CondicoesPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicoesPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public List<CondicoesPagamento> getCondicoesPagamentos() {
        return condicoesPagamentos;
    }

    public void setCondicoesPagamentos(List<CondicoesPagamento> condicoesPagamentos) {
        this.condicoesPagamentos = condicoesPagamentos;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public OrcamentoFormaPagamento getFormaPagamentoSelecionado() {
        return formaPagamentoSelecionado;
    }

    public void setFormaPagamentoSelecionado(OrcamentoFormaPagamento formaPagamentoSelecionado) {
        this.formaPagamentoSelecionado = formaPagamentoSelecionado;
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

    public boolean isExibirCondicoes() {
        return exibirCondicoes;
    }

    public boolean isExibirGrade() {
        return exibirGrade;
    }

    public Integer getIdmepresaFiltro() {
        return idmepresaFiltro;
    }

    public void setIdmepresaFiltro(Integer idmepresaFiltro) {
        this.idmepresaFiltro = idmepresaFiltro;
    }
}
