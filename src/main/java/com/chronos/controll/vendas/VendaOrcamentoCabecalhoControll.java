package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    private VendaOrcamentoDetalhe vendaOrcamentoDetalhe;
    private VendaOrcamentoDetalhe vendaOrcamentoDetalheSelecionado;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaVendaOrcamentoDetalhe(new HashSet<>());
        getObjeto().setSituacao("D");
    }

    @Override
    public void salvar() {
        try {
            String situacao = getObjeto().getSituacao();
            if (!situacao.equals("D")) {
                String mensagem = "Este registro não pode ser alterado.\n";
                if (situacao.equals("P")) {
                    mensagem += "Situação: Em Produção";
                }
                if (situacao.equals("X")) {
                    mensagem += "Situação: Em Expedição";
                }
                if (situacao.equals("F")) {
                    mensagem += "Situação: Faturado";
                }
                if (situacao.equals("E")) {
                    mensagem += "Situação: Entregue";
                }
                throw new Exception(mensagem);
            }
            super.salvar();
        } catch (Exception e) {
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }


    private void calculaTotais() throws Exception {
        VendaOrcamentoCabecalho orcamentoCabecalho = getObjeto();
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalDesconto = BigDecimal.ZERO;
        for (VendaOrcamentoDetalhe d : getObjeto().getListaVendaOrcamentoDetalhe()) {
            d.setValorSubtotal(Biblioteca.multiplica(d.getQuantidade(), d.getValorUnitario()));
            subTotal = Biblioteca.soma(subTotal, d.getValorSubtotal());
            if (d.getTaxaDesconto() != null) {
                d.setValorDesconto(Biblioteca.multiplica(d.getValorSubtotal(), Biblioteca.divide(d.getTaxaDesconto(), BigDecimal.valueOf(100))));
            }
            if (d.getValorDesconto() != null) {
                totalDesconto = Biblioteca.soma(totalDesconto, d.getValorDesconto());
                d.setValorTotal(Biblioteca.subtrai(d.getValorSubtotal(), d.getValorDesconto()));
            } else {
                d.setValorTotal(d.getValorSubtotal());
            }
        }
        orcamentoCabecalho.setValorSubtotal(subTotal);
        if (totalDesconto.compareTo(BigDecimal.ZERO) != 0) {
            orcamentoCabecalho.setValorDesconto(totalDesconto);
            orcamentoCabecalho.setTaxaDesconto(Biblioteca.multiplica(Biblioteca.divide(totalDesconto, subTotal), BigDecimal.valueOf(100)));
        }

        orcamentoCabecalho.setValorTotal(subTotal);
        if (orcamentoCabecalho.getValorFrete() != null) {
            orcamentoCabecalho.setValorTotal(Biblioteca.soma(orcamentoCabecalho.getValorTotal(), orcamentoCabecalho.getValorFrete()));
        }
        if (orcamentoCabecalho.getValorDesconto() != null) {
            orcamentoCabecalho.setValorTotal(Biblioteca.subtrai(orcamentoCabecalho.getValorTotal(), orcamentoCabecalho.getValorDesconto()));
        }

        if (orcamentoCabecalho.getTaxaComissao() != null) {
            orcamentoCabecalho.setValorComissao(Biblioteca.multiplica(Biblioteca.subtrai(subTotal, totalDesconto), Biblioteca.divide(orcamentoCabecalho.getTaxaComissao(), BigDecimal.valueOf(100))));
        }
        atualizaTotais();
    }

    public void atualizaTotais() throws Exception {
        if (getObjeto().getValorSubtotal() != null) {
            if (getObjeto().getTaxaDesconto() != null) {
                getObjeto().setValorDesconto(Biblioteca.multiplica(getObjeto().getValorSubtotal(), Biblioteca.divide(getObjeto().getTaxaDesconto(), BigDecimal.valueOf(100))));
                getObjeto().setValorTotal(Biblioteca.subtrai(getObjeto().getValorSubtotal(), getObjeto().getValorDesconto()));
            }
            if (getObjeto().getValorFrete() != null) {
                if (getObjeto().getValorTotal() == null) {
                    getObjeto().setValorTotal(getObjeto().getValorSubtotal());
                }
                getObjeto().setValorTotal(Biblioteca.soma(getObjeto().getValorTotal(), getObjeto().getValorFrete()));
            }
        }
    }

    public void incluirVendaOrcamentoDetalhe() {
        vendaOrcamentoDetalhe = new VendaOrcamentoDetalhe();
        vendaOrcamentoDetalhe.setVendaOrcamentoCabecalho(getObjeto());
    }

    public void alterarVendaOrcamentoDetalhe() {
        vendaOrcamentoDetalhe = vendaOrcamentoDetalheSelecionado;
    }

    public void salvarVendaOrcamentoDetalhe() {
        vendaOrcamentoDetalhe.setValorUnitario(vendaOrcamentoDetalhe.getProduto().getValorVenda());
        if (vendaOrcamentoDetalhe.getId() == null) {
            getObjeto().getListaVendaOrcamentoDetalhe().add(vendaOrcamentoDetalhe);
        }
        try {
            calculaTotais();
            salvar("Registro salvo com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public void excluirVendaOrcamentoDetalhe() {

        try {
            getObjeto().getListaVendaOrcamentoDetalhe().remove(vendaOrcamentoDetalheSelecionado);
            calculaTotais();
            salvar("Registro excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }

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
            listaVendedor = vendedores.getEntitys(Vendedor.class ,"colaborador.pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendedor;
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
            listaProduto = produtos.getEntitys(Produto.class ,"nome", nome);
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
        return "VENDA_ORCAMENTO_CABECALHO";
    }
}
