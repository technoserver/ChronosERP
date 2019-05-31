package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.SituacaoOrcamentoPedido;
import com.chronos.modelo.enuns.TipoFrete;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.VendaOrcamentoService;
import com.chronos.service.comercial.VendedorService;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaOrcamentoCabecalhoControll extends AbstractControll<VendaOrcamentoCabecalho> implements Serializable {

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
    private VendaOrcamentoService service;

    private VendaOrcamentoDetalhe vendaOrcamentoDetalhe;
    private VendaOrcamentoDetalhe vendaOrcamentoDetalheSelecionado;
    private String tipo;

    private Date dataInicial;
    private Date dataFinal;
    private String cliente;
    private String codigo;
    private String status;
    private Integer idvendedor;
    private Map<String, Integer> listaVendedor;
    private Map<String, String> situacoes;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        idvendedor = 0;
        listaVendedor = vendedorService.getMapVendedores();

        situacoes = new LinkedHashMap<>();
        situacoes.put("Todos", "");
        situacoes.putAll(getOrcamentoSituacao());

    }

    @Override
    public ERPLazyDataModel<VendaOrcamentoCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(VendaOrcamentoCabecalho.class);
            dataModel.setDao(dao);
        }

        if (dataModel.getFiltros().size() == 0) {
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
        getObjeto().setListaVendaOrcamentoDetalhe(new ArrayList<>());
        getObjeto().setSituacao(SituacaoOrcamentoPedido.PENDENTE.getCodigo());
        getObjeto().setTipoFrete(TipoFrete.CIF.getCodigo());
        getObjeto().setDataCadastro(new Date());
        getObjeto().setTipo(Optional.ofNullable(tipo).orElse("O"));
    }

    @Override
    public void doEdit() {
        super.doEdit();

        VendaOrcamentoCabecalho orcamento = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(orcamento);

    }

    @Override
    public void salvar() {
        try {
            VendaOrcamentoCabecalho orcamento = service.salvar(getObjeto());
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
        VendaOrcamentoCabecalho orcamento = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        orcamento.setTipo("P");
        orcamento.setSituacao("A");
        String codigo = orcamento.getTipo().equals("P") ? "#PE" : "#OE";
        codigo += StringUtils.leftPad(orcamento.getId().toString(), 3, "0");
        orcamento.setCodigo(codigo);
        dao.atualizar(orcamento);
    }

    public void gerarVenda() {
        try {
            VendaOrcamentoCabecalho orc = getObjeto() != null ? getObjeto() : dataModel.getRowData(getObjetoSelecionado().getId().toString());
            if (orc.getListaVendaOrcamentoDetalhe() == null || orc.getListaVendaOrcamentoDetalhe().isEmpty()) {
                throw new ChronosException("Itens do orcamento não definidos");
            }
            VendaOrcamentoCabecalho orcamento = service.conveterEmVenda(orc);
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
        vendaOrcamentoDetalhe = new VendaOrcamentoDetalhe();
        vendaOrcamentoDetalhe.setVendaOrcamentoCabecalho(getObjeto());
        vendaOrcamentoDetalhe.setQuantidade(BigDecimal.ONE);
    }

    public void alterarVendaOrcamentoDetalhe() {
        vendaOrcamentoDetalhe = vendaOrcamentoDetalheSelecionado;
    }

    public void salvarVendaOrcamentoDetalhe() {

        if (vendaOrcamentoDetalhe.getId() == null) {
            getObjeto().getListaVendaOrcamentoDetalhe().add(vendaOrcamentoDetalhe);
        }

        getObjeto().calcularValorTotal();

    }

    public void excluirVendaOrcamentoDetalhe() {


        getObjeto().getListaVendaOrcamentoDetalhe().remove(vendaOrcamentoDetalheSelecionado);
        getObjeto().calcularValorTotal();


    }

    public void definirValorVenda() {
        if (vendaOrcamentoDetalhe.getProduto() != null) {

        }

        BigDecimal valor = Optional.ofNullable(vendaOrcamentoDetalhe.getProduto())
                .map(Produto::getValorVenda).orElse(null);
        vendaOrcamentoDetalhe.setValorUnitario(valor);
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

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtos.getEntitys(Produto.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }


    @Override
    protected Class<VendaOrcamentoCabecalho> getClazz() {
        return VendaOrcamentoCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_ORCAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public VendaOrcamentoDetalhe getVendaOrcamentoDetalhe() {
        return vendaOrcamentoDetalhe;
    }

    public void setVendaOrcamentoDetalhe(VendaOrcamentoDetalhe vendaOrcamentoDetalhe) {
        this.vendaOrcamentoDetalhe = vendaOrcamentoDetalhe;
    }

    public VendaOrcamentoDetalhe getVendaOrcamentoDetalheSelecionado() {
        return vendaOrcamentoDetalheSelecionado;
    }

    public void setVendaOrcamentoDetalheSelecionado(VendaOrcamentoDetalhe vendaOrcamentoDetalheSelecionado) {
        this.vendaOrcamentoDetalheSelecionado = vendaOrcamentoDetalheSelecionado;
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
}
