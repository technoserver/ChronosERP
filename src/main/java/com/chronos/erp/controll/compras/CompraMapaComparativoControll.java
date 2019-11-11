package com.chronos.erp.controll.compras;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraMapaComparativoControll extends AbstractControll<CompraCotacao> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<CompraCotacaoDetalhe> cotacoes;
    @Inject
    private Repository<CompraTipoPedido> tipos;
    @Inject
    private Repository<CompraPedido> pedidos;
    @Inject
    private Repository<CompraCotacaoPedidoDetalhe> compraCotacaoPedidoDetalheDao;

    private List<CompraCotacaoDetalhe> listaCompraCotacaoDetalhe;

    @Override
    public void doEdit() {

        try {
            super.doEdit();
            CompraCotacao cotacao = dataModel.getRowData(getObjeto().getId().toString());
            setObjeto(cotacao);
            buscaListaCompraCotacaoDetalhe();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os detalhes da cotação!", e);

        }
    }

    private void buscaListaCompraCotacaoDetalhe() throws Exception {


        listaCompraCotacaoDetalhe = cotacoes.getEntitys(CompraCotacaoDetalhe.class, "compraFornecedorCotacao.compraCotacao", getObjeto());
    }


    @Override
    public void salvar() {
        try {
            buscaListaCompraCotacaoDetalhe();

            boolean produtoPedido = false;
            for (CompraCotacaoDetalhe d : listaCompraCotacaoDetalhe) {
                if (d.getQuantidadePedida() != null) {
                    if (d.getQuantidadePedida().compareTo(BigDecimal.ZERO) == 1) {
                        produtoPedido = true;
                    }
                }
            }
            if (!produtoPedido) {
                throw new Exception("Nenhum produto com quantidade pedida maior que 0(zero)!");
            }

            //Pedido vindo de cotação sempre será marcado como Normal
            CompraTipoPedido tipoPedido = tipos.get(1, CompraTipoPedido.class);
            if (tipoPedido == null) {
                throw new Exception("Tipo de Pedido não cadastrado!");
            }

            List<CompraPedido> listaPedido = new ArrayList<>();
            List<CompraCotacaoPedidoDetalhe> listaCotacaoPedidoDetalhe = new ArrayList<>();
            CompraPedido pedido;
            Date dataPedido = new Date();
            boolean incluiPedido;
            BigDecimal subTotal;
            BigDecimal totalDesconto;
            BigDecimal totalGeral;
            BigDecimal totalBaseCalculoIcms;
            BigDecimal totalIcms;
            BigDecimal totalIpi;
            for (CompraFornecedorCotacao f : getObjeto().getListaCompraFornecedorCotacao()) {
                pedido = new CompraPedido();
                pedido.setCompraTipoPedido(tipoPedido);
                pedido.setDataPedido(dataPedido);
                pedido.setFornecedor(f.getFornecedor());
                pedido.setListaCompraPedidoDetalhe(new HashSet<>());
                incluiPedido = false;
                subTotal = BigDecimal.ZERO;
                totalDesconto = BigDecimal.ZERO;
                totalGeral = BigDecimal.ZERO;
                totalBaseCalculoIcms = BigDecimal.ZERO;
                totalIcms = BigDecimal.ZERO;
                totalIpi = BigDecimal.ZERO;
                //inclui os itens no pedido
                for (CompraCotacaoDetalhe d : listaCompraCotacaoDetalhe) {
                    if (d.getCompraFornecedorCotacao().getId().intValue() == f.getId().intValue()) {
                        if (d.getQuantidadePedida() != null) {
                            if (d.getQuantidadePedida().compareTo(BigDecimal.ZERO) == 1) {
                                if (d.getValorUnitario() == null) {
                                    throw new Exception("Valor unitário do produto '" + d.getProduto().getNome() + " não informado!");
                                }
                                incluiPedido = true;

                                CompraPedidoDetalhe pedidoDetalhe = new CompraPedidoDetalhe();
                                pedidoDetalhe.setCompraPedido(pedido);
                                pedidoDetalhe.setProduto(d.getProduto());
                                pedidoDetalhe.setQuantidade(d.getQuantidadePedida());
                                pedidoDetalhe.setValorUnitario(d.getValorUnitario());
                                pedidoDetalhe.setValorSubtotal(d.getValorSubtotal());
                                pedidoDetalhe.setTaxaDesconto(d.getTaxaDesconto());
                                pedidoDetalhe.setValorDesconto(d.getValorDesconto());
                                pedidoDetalhe.setValorTotal(d.getValorTotal());
                                pedidoDetalhe.setBaseCalculoIcms(pedidoDetalhe.getValorTotal());
                                pedidoDetalhe.setAliquotaIcms(d.getProduto().getAliquotaIcmsPaf());
                                if (pedidoDetalhe.getAliquotaIcms() != null && pedidoDetalhe.getBaseCalculoIcms() != null) {
                                    pedidoDetalhe.setValorIcms(pedidoDetalhe.getBaseCalculoIcms().multiply(pedidoDetalhe.getAliquotaIcms().divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)));
                                } else {
                                    pedidoDetalhe.setAliquotaIcms(BigDecimal.ZERO);
                                    pedidoDetalhe.setValorIcms(BigDecimal.ZERO);
                                }
                                pedidoDetalhe.setAliquotaIpi(BigDecimal.ZERO);
                                if (pedidoDetalhe.getAliquotaIpi() != null) {
                                    pedidoDetalhe.setValorIpi(pedidoDetalhe.getValorTotal().multiply(pedidoDetalhe.getAliquotaIpi().divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN)));
                                } else {
                                    pedidoDetalhe.setAliquotaIpi(BigDecimal.ZERO);
                                    pedidoDetalhe.setValorIpi(BigDecimal.ZERO);
                                }
                                pedido.getListaCompraPedidoDetalhe().add(pedidoDetalhe);

                                subTotal = subTotal.add(pedidoDetalhe.getValorSubtotal());
                                totalDesconto = totalDesconto.add(pedidoDetalhe.getValorDesconto());
                                totalGeral = totalGeral.add(pedidoDetalhe.getValorTotal());
                                totalBaseCalculoIcms = totalBaseCalculoIcms.add(pedidoDetalhe.getBaseCalculoIcms());
                                totalIcms = totalIcms.add(pedidoDetalhe.getValorIcms());
                                totalIpi = totalIpi.add(pedidoDetalhe.getValorIpi());

                                CompraCotacaoPedidoDetalhe cotacaoPedidoDetalhe = new CompraCotacaoPedidoDetalhe();
                                cotacaoPedidoDetalhe.setCompraPedido(pedido);
                                cotacaoPedidoDetalhe.setCompraCotacaoDetalhe(d);
                                cotacaoPedidoDetalhe.setQuantidadePedida(d.getQuantidadePedida());
                                listaCotacaoPedidoDetalhe.add(cotacaoPedidoDetalhe);
                            }
                        }
                    }
                }
                pedido.setValorSubtotal(subTotal);
                pedido.setValorDesconto(totalDesconto);
                pedido.setValorTotalPedido(totalGeral);
                pedido.setBaseCalculoIcms(totalBaseCalculoIcms);
                pedido.setValorIcms(totalIcms);
                pedido.setValorTotalProdutos(totalGeral);
                pedido.setValorIpi(totalIpi);
                pedido.setValorTotalNf(totalGeral);

                if (incluiPedido) {
                    listaPedido.add(pedido);
                }
            }

            for (CompraPedido c : listaPedido) {
                pedidos.salvar(c);
            }

            for (CompraCotacaoPedidoDetalhe c : listaCotacaoPedidoDetalhe) {
                compraCotacaoPedidoDetalheDao.salvar(c);
            }

            getObjeto().setSituacao("F");
            dao.atualizar(getObjeto());

            voltar();
            Mensagem.addInfoMessage("Pedido realizado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }

    public void alteraItemFornecedor(CellEditEvent event) {
        try {
            DataTable dataTable = (DataTable) event.getSource();
            CompraCotacaoDetalhe detalhe = (CompraCotacaoDetalhe) dataTable.getRowData();
            BigDecimal quantidadePedida = (BigDecimal) event.getNewValue();
            if (quantidadePedida != null) {
                if (quantidadePedida.compareTo(detalhe.getQuantidade()) == 1) {
                    throw new Exception("Quantidade pedida do produto '" + detalhe.getProduto().getNome() + "' é maior que a quantidade cotada!");
                } else if (detalhe.getQuantidadePedida().compareTo(BigDecimal.ZERO) == 1) {
                    detalhe.setQuantidadePedida((BigDecimal) event.getNewValue());

                    cotacoes.atualizar(detalhe);
                    Mensagem.addInfoMessage("Dados salvos com sucesso!");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", e);

        }
    }


    @Override
    protected Class<CompraCotacao> getClazz() {
        return CompraCotacao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_MAPA_COMPARATIVO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<CompraCotacaoDetalhe> getListaCompraCotacaoDetalhe() {
        return listaCompraCotacaoDetalhe;
    }

    public void setListaCompraCotacaoDetalhe(List<CompraCotacaoDetalhe> listaCompraCotacaoDetalhe) {
        this.listaCompraCotacaoDetalhe = listaCompraCotacaoDetalhe;
    }
}
