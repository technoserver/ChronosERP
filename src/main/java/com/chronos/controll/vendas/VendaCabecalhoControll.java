package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaCabecalhoControll extends AbstractControll<VendaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<VendaOrcamentoCabecalho> orcamentos;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<Transportadora> transportadora;
    @Inject
    private Repository<NotaFiscalTipo> tipos;
    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Produto> produtos;

    private VendaDetalhe vendaDetalhe;
    private VendaDetalhe vendaDetalheSelecionado;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaVendaDetalhe(new HashSet<>());
    }

    public void incluirVendaDetalhe() {
        vendaDetalhe = new VendaDetalhe();
        vendaDetalhe.setVendaCabecalho(getObjeto());
    }

    public void alterarVendaDetalhe() {
        vendaDetalhe = vendaDetalheSelecionado;
    }

    public void salvarVendaDetalhe() {
        vendaDetalhe.setValorUnitario(vendaDetalhe.getProduto().getValorVenda());
        if (vendaDetalhe.getId() == null) {
            getObjeto().getListaVendaDetalhe().add(vendaDetalhe);
        }
        try {
            getObjeto().calcularValorTotal();
            salvar("Registro salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public void excluirVendaDetalhe() {
        try {
            getObjeto().getListaVendaDetalhe().remove(vendaDetalheSelecionado);
            getObjeto().calcularValorTotal();
            salvar("Registro excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }

    }

    public void faturarVenda() {

    }

    public void gerarNFe() {

    }

    public void cancelarVenda() {

    }

    public void carregaItensOrcamento(SelectEvent event) {
        try {
            VendaOrcamentoCabecalho orcamento = (VendaOrcamentoCabecalho) event.getObject();
            VendaDetalhe itemVenda;
            getObjeto().setListaVendaDetalhe(new HashSet<>());
            getObjeto().setVendaOrcamentoCabecalho(orcamentos.getEntityJoinFetch(orcamento.getId(), VendaOrcamentoCabecalho.class));
            for (VendaOrcamentoDetalhe d : getObjeto().getVendaOrcamentoCabecalho().getListaVendaOrcamentoDetalhe()) {
                itemVenda = new VendaDetalhe();
                itemVenda.setVendaCabecalho(getObjeto());
                itemVenda.setProduto(d.getProduto());
                itemVenda.setQuantidade(d.getQuantidade());
                itemVenda.setTaxaDesconto(d.getTaxaDesconto());
                itemVenda.setValorDesconto(d.getValorDesconto());
                itemVenda.setValorSubtotal(d.getValorSubtotal());
                itemVenda.setValorTotal(d.getValorTotal());
                itemVenda.setValorUnitario(d.getValorUnitario());

                getObjeto().getListaVendaDetalhe().add(itemVenda);
            }

            getObjeto().setCliente(orcamento.getCliente());
            getObjeto().setCondicoesPagamento(orcamento.getCondicoesPagamento());
            getObjeto().setTransportadora(orcamento.getTransportadora());
            getObjeto().setVendedor(orcamento.getVendedor());
            getObjeto().setTipoFrete(orcamento.getTipoFrete());
            getObjeto().setValorSubtotal(orcamento.getValorSubtotal());
            getObjeto().setValorFrete(orcamento.getValorFrete());
            getObjeto().setTaxaComissao(orcamento.getTaxaComissao());
            getObjeto().setValorComissao(orcamento.getValorComissao());
            getObjeto().setTaxaDesconto(orcamento.getValorDesconto());
            getObjeto().setValorTotal(orcamento.getValorTotal());
            getObjeto().setObservacao(orcamento.getObservacao());

            getObjeto().calcularValorTotal();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public List<VendaOrcamentoCabecalho> getListaVendaOrcamentoCabecalho(String nome) {
        List<VendaOrcamentoCabecalho> listaVendaOrcamentoCabecalho = new ArrayList<>();
        try {
            listaVendaOrcamentoCabecalho = orcamentos.getEntitys(VendaOrcamentoCabecalho.class, "codigo", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaOrcamentoCabecalho;
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

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(Cliente.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
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
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA_CABECALHO";
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
}
