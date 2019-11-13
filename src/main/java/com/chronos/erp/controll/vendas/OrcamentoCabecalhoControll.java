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
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Transportadora> transportadoras;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<Produto> produtos;

    @Inject
    private VendedorService vendedorService;

    @Inject
    private OrcamentoService service;
    @Inject
    private EstoqueRepository estoqueRepository;

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

    private int tipoDesconto;
    private BigDecimal desconto;

    private boolean podeAlterarPreco = true;

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

    }

    @Override
    public ERPLazyDataModel<OrcamentoCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(OrcamentoCabecalho.class);
            dataModel.setDao(dao);
        }

        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("tipo", Optional.ofNullable(tipo).orElse("O"), Filtro.IGUAL);
        }


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
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaOrcamentoDetalhe(new ArrayList<>());
        getObjeto().setSituacao(SituacaoOrcamentoPedido.PENDENTE.getCodigo());
        getObjeto().setTipoFrete(TipoFrete.CIF.getCodigo());
        getObjeto().setDataCadastro(new Date());
        getObjeto().setDataEntrega(new Date());
        getObjeto().setTipo(Optional.ofNullable(tipo).orElse("O"));
    }

    @Override
    public void doEdit() {
        super.doEdit();

        OrcamentoCabecalho orcamento = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(orcamento);

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


    public void gerarPedido() {
        OrcamentoCabecalho orcamento = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        orcamento.setTipo("P");
        orcamento.setSituacao("A");
        String codigo = orcamento.getTipo().equals("P") ? "#PE" : "#OE";
        codigo += StringUtils.leftPad(orcamento.getId().toString(), 3, "0");
        orcamento.setCodigo(codigo);
        dao.atualizar(orcamento);
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


    public void incluirVendaOrcamentoDetalhe() {
        orcamentoDetalhe = new OrcamentoDetalhe();
        orcamentoDetalhe.setOrcamentoCabecalho(getObjeto());
        orcamentoDetalhe.setQuantidade(BigDecimal.ONE);
        desconto = BigDecimal.ZERO;
    }

    public void alterarVendaOrcamentoDetalhe() {
        orcamentoDetalhe = orcamentoDetalheSelecionado;
    }

    public void salvarVendaOrcamentoDetalhe() {

        try {

            service.salvarItem(getObjeto(), orcamentoDetalhe, desconto, tipoDesconto);
            desconto = BigDecimal.ZERO;

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
        if (orcamentoDetalhe.getProduto() != null) {

        }

        BigDecimal valor = Optional.ofNullable(orcamentoDetalhe.getProduto())
                .map(Produto::getValorVenda).orElse(null);
        orcamentoDetalhe.setValorUnitario(valor);
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
            List<ProdutoDTO> list = estoqueRepository.getProdutoDTO(nome, empresa, "N");
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


}
