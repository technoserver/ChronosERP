package com.chronos.controll.compras;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.CompraPedido;
import com.chronos.modelo.entidades.CompraPedidoDetalhe;
import com.chronos.modelo.entidades.CompraRequisicaoDetalhe;
import com.chronos.modelo.entidades.Produto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class CompraSugeridaControll extends AbstractControll<CompraPedido> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Produto> produtos;

    private String tipoCompraSugerida;

    public String confirma() {
        try {

            List<Produto> listaProduto = getItensCompraSugerida();
            if (listaProduto.isEmpty()) {
                throw new Exception("Nenhum produto com estoque menor que o mínimo!");
            }
            if (tipoCompraSugerida.equals("Requisicao")) {
                return "compraRequisicao.jsf?faces-redirect=true&compraSugerida=true";
            } else if (tipoCompraSugerida.equals("Pedido")) {
                return "compraPedido.jsf?faces-redirect=true&compraSugerida=true";
            }

            return null;
        } catch (Exception e) {
            //e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os itens!", e);

        }
        return null;
    }

    public Set<CompraRequisicaoDetalhe> geraRequisicao() throws Exception {

        List<Produto> listaProduto = getItensCompraSugerida();
        Set<CompraRequisicaoDetalhe> listaRequisicaoDetalhe = new HashSet<>();
        for (Produto p : listaProduto) {
            CompraRequisicaoDetalhe requisicaoDetalhe = new CompraRequisicaoDetalhe();
            requisicaoDetalhe.setProduto(p);
            requisicaoDetalhe.setQuantidade(p.getEstoqueIdeal().subtract(p.getQuantidadeEstoque()));
            requisicaoDetalhe.setQuantidadeCotada(BigDecimal.ZERO);
            requisicaoDetalhe.setItemCotado("N");

            listaRequisicaoDetalhe.add(requisicaoDetalhe);
        }

        return listaRequisicaoDetalhe;
    }
    public Set<CompraPedidoDetalhe> geraPedido() throws Exception {

        List<Produto> listaProduto = getItensCompraSugerida();
        Set<CompraPedidoDetalhe> listaPedidoDetalhe = new HashSet<>();
        for (Produto p : listaProduto) {
            CompraPedidoDetalhe pedidoDetalhe = new CompraPedidoDetalhe();
            pedidoDetalhe.setProduto(p);
            pedidoDetalhe.setQuantidade(p.getEstoqueIdeal().subtract(p.getQuantidadeEstoque()));
            pedidoDetalhe.setValorUnitario(p.getValorCompra());

            listaPedidoDetalhe.add(pedidoDetalhe);
        }
        return listaPedidoDetalhe;
    }

    public List<Produto> getItensCompraSugerida(){
        List<Produto> listaProduto = new ArrayList<>();
        try{
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND,"quantidadeEstoque",Filtro.MENOR,"o.estoqueMinimo"));
            listaProduto = produtos.getEntitys(Produto.class,filtros);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return listaProduto;
    }

    @Override
    protected Class<CompraPedido> getClazz() {
        return CompraPedido.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COMPRA_SUGERIDA";
    }

    public String getTipoCompraSugerida() {
        return tipoCompraSugerida;
    }

    public void setTipoCompraSugerida(String tipoCompraSugerida) {
        this.tipoCompraSugerida = tipoCompraSugerida;
    }
}
