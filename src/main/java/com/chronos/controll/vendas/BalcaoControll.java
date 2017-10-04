package com.chronos.controll.vendas;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.FormaPagamento;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.modelo.entidades.enuns.TipoFrete;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.UsuarioService;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.util.Constantes;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by john on 19/09/17.
 */
@Named
@ViewScoped
public class BalcaoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<VendaCabecalho> vendas;
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
    private UsuarioService userService;
    @Inject
    private FinLancamentoReceberService recebimentoService;
    @Inject
    private NfeService nfeService;

    private VendaCabecalho venda;
    private VendaDetalhe item;
    private Empresa empresa;
    private Usuario usuario;
    private Vendedor vendedor;
    private Cliente cliente;
    private EmpresaProduto produto;
    private List<Vendedor> listVendedores;
    private List<VendaCondicoesPagamento> condicoesPagamentos;
    private BigDecimal desconto;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;
    private boolean telaImpressao;

    @PostConstruct
    private void init() {
        novaVenda();
    }

    public void novaVenda() {
        telaVenda = true;
        telaPagamentos = false;
        empresa = userService.getEmpresaUsuario();
        usuario = userService.getUsuarioLogado();
        venda = new VendaCabecalho();
        venda.setEmpresa(empresa);
        venda.setListaVendaDetalhe(new ArrayList<>());
        venda.setDataVenda(new Date());
        venda.setSituacao(SituacaoVenda.Digitacao.getCodigo());
        venda.setTipoFrete(TipoFrete.CIF.getCodigo());
        vendedor = instanciarVendedor(usuario);
        cliente = null;
        venda.setVendedor(vendedor);
        item = new VendaDetalhe();
        desconto = BigDecimal.ZERO;
        telaPagamentos = false;
        telaVenda = true;
        telaImpressao = false;
    }

    public void finalizarVenda() {

        if (venda.getListaVendaDetalhe().isEmpty()) {
            Mensagem.addInfoMessage("Não foi adcionado nenhum item");
        } else if (cliente == null) {
            Mensagem.addInfoMessage("É preciso informar o cliente");
        } else {
            telaVenda = false;
            telaPagamentos = true;
        }

        if (condicoesPagamentos == null) {
            condicoesPagamentos = condicoes.getAll(VendaCondicoesPagamento.class);
        }

    }

    public void faturarVenda() {

        try {
            telaVenda = false;
            telaPagamentos = false;
            telaImpressao = true;

            if (venda.getCondicoesPagamento() != null) {
                venda.setFormaPagamento(venda.getCondicoesPagamento().getVistaPrazo().equals("V")
                        ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo());
            }
            venda.setSituacao(SituacaoVenda.Faturado.getCodigo());
            venda = vendas.atualizar(venda);
            if (venda.getId() == null) {
                venda.setNumeroFatura(venda.getId());
                vendas.atualizar(venda);
            }
            recebimentoService.gerarLancamento(venda.getValorTotal(), venda.getCliente(),
                    new DecimalFormat("VD0000000").format(venda.getId()), venda.getCondicoesPagamento(), "210", Constantes.FIN.NATUREZA_VENDA, empresa);

            gerarComissao();
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    private void gerarComissao() {
        VendaComissao comissao = new VendaComissao();
        comissao.setDataLancamento(new Date());
        comissao.setSituacao("A");
        comissao.setTipoContabil("C");
        comissao.setValorComissao(venda.getValorComissao());
        comissao.setValorVenda(venda.getValorTotal());
        comissao.setVendaCabecalho(venda);
        comissao.setVendedor(venda.getVendedor());
        comissoes.salvar(comissao);
    }

    public void cancelar() {
        telaVenda = true;
        telaPagamentos = false;
    }

    public void gerarNfce() {


        try {
            VendaToNFe vendaNfe = new VendaToNFe(ModeloDocumento.NFCE, venda);
            NfeCabecalho nfe = vendaNfe.gerarNfe();
            nfeService.transmitirNFe(nfe, new NfeConfiguracao());
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Produto">
    public List<EmpresaProduto> getListProduto(String nome) {
        List<EmpresaProduto> list = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("empresa.id", empresa.getId()));
            filtros.add(new Filtro("produto.nome", Filtro.LIKE, nome));
            list = produtos.getEntitys(EmpresaProduto.class, filtros);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void selecionarProduto(SelectEvent event) {
        EmpresaProduto produtoSelecionado = (EmpresaProduto) event.getObject();
        desconto = BigDecimal.ZERO;
        item = new VendaDetalhe();
        item.setVendaCabecalho(venda);
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
        Optional<VendaDetalhe> itemOpt = getItemVenda(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();
        if (itemOpt.isPresent()) {
            item = itemOpt.get();
            item.setQuantidade(quantidade);
        } else {
            venda.getListaVendaDetalhe().add(0, item);
        }

        venda.calcularValorTotal();
        item = new VendaDetalhe();
        produto = new EmpresaProduto();

    }

    public void alterarQuantidade(Produto produto, BigDecimal quantidade) {
        Optional<VendaDetalhe> itemOpt = getItemVenda(produto);
        if (itemOpt.isPresent()) {
            itemOpt.get().setQuantidade(quantidade);
        }
    }

    public void excluir(Produto produto) {

        int indice = IntStream.range(0, venda.getListaVendaDetalhe().size()).filter(i -> venda.getListaVendaDetalhe().get(i).getProduto().equals(produto)).findAny()
                .getAsInt();
        venda.getListaVendaDetalhe().remove(indice);
    }

    private Optional<VendaDetalhe> getItemVenda(Produto produto) {
        return venda.getListaVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

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
        venda.setCliente(cliente);
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos vendedor">
    private Vendedor instanciarVendedor(Usuario usuario) {
        Vendedor vendedor = vendedores.get(Vendedor.class, "colaborador.id", usuario.getColaborador().getId());

        if (vendedor == null) {
            vendedor = new Vendedor();
            vendedor.setGerente('N');
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setMetaVendas(BigDecimal.ZERO);
            vendedor.setComissao(BigDecimal.ZERO);
            vendedor.setColaborador(usuario.getColaborador());
            vendedor = vendedores.atualizar(vendedor);
        }

        return vendedor;
    }

    public void buscarVendedores() {
        listVendedores = vendedores.getEntitys(Vendedor.class);
    }
    // </editor-fold>

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

    public EmpresaProduto getProduto() {
        return produto;
    }

    public void setProduto(EmpresaProduto produto) {
        this.produto = produto;
    }

    public VendaDetalhe getItem() {
        return item;
    }

    public void setItem(VendaDetalhe item) {
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

    public VendaCabecalho getVenda() {
        return venda;
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
}
