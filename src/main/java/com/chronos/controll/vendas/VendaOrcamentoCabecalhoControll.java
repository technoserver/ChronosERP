package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.modelo.entidades.enuns.TipoFrete;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

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

    private VendaOrcamentoDetalhe vendaOrcamentoDetalhe;
    private VendaOrcamentoDetalhe vendaOrcamentoDetalheSelecionado;
    private String tipo;

    @Override
    public ERPLazyDataModel<VendaOrcamentoCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(VendaOrcamentoCabecalho.class);
            dataModel.setDao(dao);
            dataModel.getFiltros().clear();
            dataModel.addFiltro("tipo", Optional.ofNullable(tipo).orElse("O"), Filtro.IGUAL);
        }
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaVendaOrcamentoDetalhe(new HashSet<>());
        getObjeto().setSituacao(SituacaoVenda.Digitacao.getCodigo());
        getObjeto().setTipoFrete(TipoFrete.CIF.getCodigo());
        getObjeto().setTipo(Optional.ofNullable(tipo).orElse("O"));
        getObjeto().setDataCadastro(new Date());
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
            setTelaGrid(false);
        } catch (Exception e) {
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

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
        vendaOrcamentoDetalhe.setValorUnitario(vendaOrcamentoDetalhe.getProduto().getValorVenda());
        if (vendaOrcamentoDetalhe.getId() == null) {
            getObjeto().getListaVendaOrcamentoDetalhe().add(vendaOrcamentoDetalhe);
        }
        try {
            getObjeto().calcularValorTotal();
            salvar("Registro salvo com sucesso!");
            setTelaGrid(false);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public void excluirVendaOrcamentoDetalhe() {

        try {
            getObjeto().getListaVendaOrcamentoDetalhe().remove(vendaOrcamentoDetalheSelecionado);
            getObjeto().calcularValorTotal();
            salvar("Registro excluído com sucesso!");
            setTelaGrid(false);
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
}
