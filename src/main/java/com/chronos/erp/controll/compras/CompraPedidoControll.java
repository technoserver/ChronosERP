package com.chronos.erp.controll.compras;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraPedidoControll extends AbstractControll<CompraPedido> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<CompraTipoPedido> tipos;
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<Produto> produtos;

    private CompraPedidoDetalhe compraPedidoDetalhe;
    private CompraPedidoDetalhe compraPedidoDetalheSelecionado;

    private Boolean parametroCompraSugerida;
    @Inject
    private CompraSugeridaControll compraSugeridaController;
    private Set<CompraPedidoDetalhe> listaCompraSugerida;

    private List<CompraTipoPedido> tiposPedido;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        tiposPedido = new ArrayList<>();
        tiposPedido.add(new CompraTipoPedido(1, "NORMAL"));
        tiposPedido.add(new CompraTipoPedido(2, "PLANEJADO"));
        tiposPedido.add(new CompraTipoPedido(3, "ABERTO"));
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setDataPedido(new Date());
        getObjeto().setListaCompraPedidoDetalhe(new HashSet<>());
        getObjeto().setEmpresa(empresa);
    }

    @Override
    public void doEdit() {
        super.doEdit();

        CompraPedido pedido = dataModel.getRowData(getObjeto().getId().toString());

        setObjeto(pedido);

    }

    @Override
    public void salvar() {
        if (parametroCompraSugerida != null) {
            if (getObjeto().getId() == null) {
                for (CompraPedidoDetalhe d : listaCompraSugerida) {
                    d.setCompraPedido(getObjeto());
                }
                getObjeto().setListaCompraPedidoDetalhe(listaCompraSugerida);
                parametroCompraSugerida = null;
            }
        }
        atualizaTotaisItens();
        atualizaTotaisPedido();

        super.salvar();
        setTelaGrid(false);
    }

    public void incluirItem() {
        compraPedidoDetalhe = new CompraPedidoDetalhe();
        compraPedidoDetalhe.setCompraPedido(getObjeto());
        compraPedidoDetalhe.setQuantidade(BigDecimal.ONE);
    }

    public void alterarItem() {
        compraPedidoDetalhe = compraPedidoDetalheSelecionado;
    }

    //TODO Calcular a taxa de desconto do item e o subtotal
    public void salvarItem() {
        try {
            if (compraPedidoDetalhe.getId() == null) {
                getObjeto().getListaCompraPedidoDetalhe().add(compraPedidoDetalhe);
            }

            atualizaTotaisItens();
            atualizaTotaisPedido();

            salvar("Item salvo com sucesso!");
            setTelaGrid(false);
            setActiveTabIndex(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void excluirItem() {

        getObjeto().getListaCompraPedidoDetalhe().remove(compraPedidoDetalheSelecionado);
        atualizaTotaisPedido();
        salvar("Item excluído com sucesso!");
        setTelaGrid(false);
        setActiveTabIndex(1);

    }

    public void atualizaTotaisItens() {
        for (CompraPedidoDetalhe d : getObjeto().getListaCompraPedidoDetalhe()) {
            d.setValorSubtotal(d.getQuantidade().multiply(d.getValorUnitario()));
            if (d.getTaxaDesconto() != null) {
                d.setValorDesconto(d.getValorSubtotal().multiply(d.getTaxaDesconto().divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)));
            } else {
                d.setValorDesconto(BigDecimal.ZERO);
                d.setTaxaDesconto(BigDecimal.ZERO);
            }
            d.setValorTotal(d.getValorSubtotal().subtract(d.getValorDesconto()));
            d.setBaseCalculoIcms(d.getValorTotal());
            //icms
            if (d.getAliquotaIcms() != null) {
                d.setValorIcms(d.getBaseCalculoIcms().multiply(d.getAliquotaIcms().divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)));
            } else {
                d.setAliquotaIcms(BigDecimal.ZERO);
                d.setValorIcms(BigDecimal.ZERO);
            }
            //ipi
            if (d.getAliquotaIpi() != null) {
                d.setValorIpi(d.getValorTotal().multiply(d.getAliquotaIpi().divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)));
            } else {
                d.setAliquotaIpi(BigDecimal.ZERO);
                d.setValorIpi(BigDecimal.ZERO);
            }
        }
    }

    public void atualizaTotaisPedido() {
        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal totalDesconto = BigDecimal.ZERO;
        BigDecimal totalGeral = BigDecimal.ZERO;
        BigDecimal totalBaseCalculoIcms = BigDecimal.ZERO;
        BigDecimal totalIcms = BigDecimal.ZERO;
        BigDecimal totalIpi = BigDecimal.ZERO;
        for (CompraPedidoDetalhe d : getObjeto().getListaCompraPedidoDetalhe()) {
            subTotal = subTotal.add(d.getValorSubtotal());
            totalDesconto = totalDesconto.add(d.getValorDesconto());
            totalGeral = totalGeral.add(d.getValorTotal());
            totalBaseCalculoIcms = totalBaseCalculoIcms.add(d.getBaseCalculoIcms());
            totalIcms = totalIcms.add(d.getValorIcms());
            totalIpi = totalIpi.add(d.getValorIpi());
        }

        getObjeto().setValorSubtotal(subTotal);
        getObjeto().setValorDesconto(totalDesconto);
        getObjeto().setValorTotalPedido(totalGeral);
        getObjeto().setBaseCalculoIcms(totalBaseCalculoIcms);
        getObjeto().setValorIcms(totalIcms);
        getObjeto().setValorTotalProdutos(totalGeral);
        getObjeto().setValorIpi(totalIpi);
        getObjeto().setValorTotalNf(totalGeral.add(totalIcms));
    }

    public void carregaCompraSugerida() {
        try {
            if (parametroCompraSugerida != null) {
                listaCompraSugerida = compraSugeridaController.geraPedido();
                doCreate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao carregar a compra sugerida!", e);

        }
    }

    public List<CompraTipoPedido> getListaCompraTipoPedido(String nome) {
        List<CompraTipoPedido> listaCompraTipoPedido = new ArrayList<>();
        try {
            listaCompraTipoPedido = tipos.getEntitys(CompraTipoPedido.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCompraTipoPedido;
    }

    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();
        try {
            listaFornecedor = fornecedores.getEntitys(Fornecedor.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFornecedor;
    }

    public void selecionarFornecedor(SelectEvent event) {
        Fornecedor fornecedor = (Fornecedor) event.getObject();
        getObjeto().setQuantidadeParcelas(fornecedor.getQuantidadeParcelas());
        getObjeto().setDiasIntervalo(fornecedor.getNumDiasIntervalo());
        getObjeto().setDiasPrimeiroVencimento(fornecedor.getNumDiasPrimeiroVencimento());

        Date previsaoEntrega = Biblioteca.addDay(new Date(), Optional.ofNullable(fornecedor.getPrazoMedioEntrega()).orElse(0));
        getObjeto().setDataPrevistaEntrega(previsaoEntrega);
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
    protected Class<CompraPedido> getClazz() {
        return CompraPedido.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_PEDIDO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public CompraPedidoDetalhe getCompraPedidoDetalheSelecionado() {
        return compraPedidoDetalheSelecionado;
    }

    public void setCompraPedidoDetalheSelecionado(CompraPedidoDetalhe compraPedidoDetalheSelecionado) {
        this.compraPedidoDetalheSelecionado = compraPedidoDetalheSelecionado;
    }

    public CompraPedidoDetalhe getCompraPedidoDetalhe() {
        return compraPedidoDetalhe;
    }

    public void setCompraPedidoDetalhe(CompraPedidoDetalhe compraPedidoDetalhe) {
        this.compraPedidoDetalhe = compraPedidoDetalhe;
    }

    public Boolean getParametroCompraSugerida() {
        return parametroCompraSugerida;
    }

    public void setParametroCompraSugerida(Boolean parametroCompraSugerida) {
        this.parametroCompraSugerida = parametroCompraSugerida;
    }

    public List<CompraTipoPedido> getTiposPedido() {
        return tiposPedido;
    }

    public void setTiposPedido(List<CompraTipoPedido> tiposPedido) {
        this.tiposPedido = tiposPedido;
    }
}
