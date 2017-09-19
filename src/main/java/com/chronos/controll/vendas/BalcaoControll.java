package com.chronos.controll.vendas;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.modelo.entidades.enuns.TipoFrete;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.UsuarioService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

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
    private UsuarioService userService;

    private VendaCabecalho venda;
    private VendaDetalhe item;
    private Empresa empresa;
    private Usuario usuario;
    private Vendedor vendedor;
    private Cliente cliente;
    private EmpresaProduto produto;
    private List<Vendedor> listVendedores;
    private BigDecimal desconto;


    private String tipoDesconto;

    private boolean telaVenda;
    private boolean telaPagamentos;

    @PostConstruct
    private void init() {
        telaVenda = true;
        telaPagamentos = false;
        empresa = userService.getEmpresaUsuario();
        usuario = userService.getUsuarioLogado();
        venda = new VendaCabecalho();
        venda.setEmpresa(empresa);
        venda.setListaVendaDetalhe(new HashSet<>());
        venda.setDataVenda(new Date());
        venda.setSituacao(SituacaoVenda.Digitacao.getCodigo());
        venda.setTipoFrete(TipoFrete.CIF.getCodigo());
        vendedor = instanciarVendedor(usuario);
        venda.setVendedor(vendedor);
        item = new VendaDetalhe();
        desconto = BigDecimal.ZERO;

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
        if (item.getId() == null) {
            venda.getListaVendaDetalhe().stream()
                    .filter(p -> p.getProduto().getId() == item.getProduto().getId())
                    .forEach(item -> {
                        item.setQuantidade(item.getQuantidade());
                        item.setValorUnitario(item.getValorUnitario());
                        item.setTaxaDesconto(item.getTaxaDesconto());
                    });
            boolean encontrou = venda.getListaVendaDetalhe().stream()
                    .filter(p -> p.getProduto().getId() == item.getProduto().getId())
                    .findFirst().isPresent();
            if (!encontrou) {
                venda.getListaVendaDetalhe().add(item);
            }

        }
        try {
            venda.calcularValorTotal();
            item = new VendaDetalhe();
            produto = new EmpresaProduto();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }
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
}
