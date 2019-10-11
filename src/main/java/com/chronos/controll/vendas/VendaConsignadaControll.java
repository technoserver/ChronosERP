package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.ItemVendaConsignadaService;
import com.chronos.service.comercial.VendaConsignadaService;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class VendaConsignadaControll extends AbstractControll<VendaConsignadaCabecalho> implements Serializable {

    @Inject
    private Repository<PessoaCliente> clienteRepository;
    @Inject
    private Repository<Vendedor> vendedorRepository;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoesPagamentoRepository;

    @Inject
    private ProdutoService produtoService;
    @Inject
    private ItemVendaConsignadaService itemService;
    @Inject
    private VendaConsignadaService service;

    private PessoaCliente pessoaCliente;
    private ProdutoDTO produto;


    private VendaConsignadaDetalhe item;
    private VendaConsignadaDetalhe itemSelecionado;
    private boolean podeAlterarPreco = true;
    private String tipoDesconto = "RS";
    private BigDecimal desconto = BigDecimal.ZERO;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        podeAlterarPreco = usuario.getAdministrador().equals("S") || restricao.getAlteraPrecoNaVenda().equals("S");
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);

    }

    @Override
    public void doEdit() {
        super.doEdit();
        VendaConsignadaCabecalho venda = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(venda);
        pessoaCliente = new PessoaCliente();
        pessoaCliente.setNome(getObjeto().getCliente().getPessoa().getNome());
    }

    @Override
    public void salvar() {
        try {
            VendaConsignadaCabecalho venda = service.salvar(getObjeto());
            setObjeto(venda);
            Mensagem.addInfoMessage("Consignação salva");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro salvar o pedido venda", ex);
            }
        }
    }

    public void buscarVenda() {
        VendaConsignadaCabecalho venda = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(venda);
    }

    public void encerrar() {
        try {
            service.encerrar(getObjeto());
            Mensagem.addInfoMessage("Consignação Encerrada");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                throw new RuntimeException("Erro salvar o pedido venda", ex);
            }
        }
    }

    public void gerarVenda() {
        try {
            service.encerrar(getObjeto());
            Mensagem.addInfoMessage("Consignação salva");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                throw new RuntimeException("Erro salvar o pedido venda", ex);
            }
        }
    }

    public void addItem() {
        item = new VendaConsignadaDetalhe();
        item.setVendaConsignadaCabecalho(getObjeto());
        produto = new ProdutoDTO();
    }

    public void definirValorProduto(SelectEvent event) {
        produto = (ProdutoDTO) event.getObject();
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        item.setValorUnitario(precoVenda);
        item.setUn(produto.getUnidade());
    }

    public void definirValorAtacado() {
        produto.setQuantidadeVenda(item.getQuantidade());
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        item.setValorUnitario(precoVenda);
    }

    public void alterarTipoDesconto() {
        tipoDesconto = tipoDesconto.equals("%") ? "R$" : "%";
    }

    public void SalvarItem() {


        try {

            item.setProduto(new Produto(produto.getId(), produto.getNome()));

            itemService.verificarRestricao(item);

            if (itemService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogVendaDetalhe').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                service.addItem(getObjeto(), item, tipoDesconto, desconto);
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao add o item da venda", ex);
            }
        }

    }

    public void excluirItem() {

        getObjeto().getListaVendaConsignadaDetalhe().remove(itemSelecionado);
        getObjeto().calcularValorTotal();
        getObjeto().setExcludoItem(true);

    }


    public List<ProdutoDTO> getListaProduto(String nome) {
        List<ProdutoDTO> listaProduto = new ArrayList<>();

        try {

            listaProduto = produtoService.getListaProdutoDTO(empresa, nome, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;

    }


    public List<PessoaCliente> getListaCliente(String nome) {
        List<PessoaCliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clienteRepository.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public void definirEnderecoEntrega(SelectEvent event) {
        PessoaCliente pessoaCliente = (PessoaCliente) event.getObject();
        Cliente cliente = new Cliente();
        cliente.setId(pessoaCliente.getId());
        getObjeto().setCliente(cliente);
        String endereco = "End: " + pessoaCliente.getLogradouro() + "," + pessoaCliente.getNumero() + " ";
        endereco += "Bairro :" + pessoaCliente.getBairro() + " Cep: " + pessoaCliente.getCep() + " - ";
        endereco += pessoaCliente.getCidade() + "/" + pessoaCliente.getUf();
        getObjeto().setLocalEntrega(endereco);
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

    public void definirTaxaComissao(SelectEvent event) {
        Vendedor vendedor = (Vendedor) event.getObject();
        getObjeto().setTaxaComissao(vendedor.getComissao());
    }

    public List<VendaCondicoesPagamento> getListaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            Object[] join = new Object[]{""};
            listaVendaCondicoesPagamento = condicoesPagamentoRepository.getEntitys(VendaCondicoesPagamento.class, "nome", nome, new Object[]{"nome", "vistaPrazo"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaCondicoesPagamento;
    }



    @Override
    protected Class<VendaConsignadaCabecalho> getClazz() {
        return VendaConsignadaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_CONSIGNADA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public boolean isPodeAlterarPreco() {
        return podeAlterarPreco;
    }

    public String getTipoDesconto() {
        return tipoDesconto;
    }

    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public VendaConsignadaDetalhe getItem() {
        return item;
    }

    public void setItem(VendaConsignadaDetalhe item) {
        this.item = item;
    }

    public VendaConsignadaDetalhe getItemSelecionado() {
        return itemSelecionado;
    }

    public void setItemSelecionado(VendaConsignadaDetalhe itemSelecionado) {
        this.itemSelecionado = itemSelecionado;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }
}
