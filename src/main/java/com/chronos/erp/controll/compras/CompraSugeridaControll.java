package com.chronos.erp.controll.compras;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.dto.EstoqueIdealDTO;
import com.chronos.erp.modelo.entidades.CompraPedido;
import com.chronos.erp.modelo.entidades.CompraPedidoDetalhe;
import com.chronos.erp.modelo.entidades.CompraRequisicaoDetalhe;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.util.jsf.Mensagem;

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
    private EstoqueRepository estoqueRepository;

    private String tipoCompraSugerida;

    public String confirma() {
        try {

            List<EstoqueIdealDTO> listaProduto = getItensCompraSugerida();
            if (listaProduto.isEmpty()) {
                throw new Exception("Nenhum produto com estoque menor que o mínimo!");
            }
            if (tipoCompraSugerida.equals("Requisicao")) {
                return "compraRequisicao.xhtml?faces-redirect=true&compraSugerida=true";
            } else if (tipoCompraSugerida.equals("Pedido")) {
                return "compraPedido.xhtml?faces-redirect=true&compraSugerida=true";
            }

            return null;
        } catch (Exception e) {
            //e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao buscar os itens!", e);

        }
        return null;
    }

    public Set<CompraRequisicaoDetalhe> geraRequisicao() throws Exception {

        List<EstoqueIdealDTO> listaProduto = getItensCompraSugerida();
        Set<CompraRequisicaoDetalhe> listaRequisicaoDetalhe = new HashSet<>();
        for (EstoqueIdealDTO p : listaProduto) {
            CompraRequisicaoDetalhe requisicaoDetalhe = new CompraRequisicaoDetalhe();
            requisicaoDetalhe.setProduto(p.getProduto());
            requisicaoDetalhe.setQuantidade(p.getEstoqueIdeal().subtract(p.getControle()));
            requisicaoDetalhe.setQuantidadeCotada(BigDecimal.ZERO);
            requisicaoDetalhe.setItemCotado("N");

            listaRequisicaoDetalhe.add(requisicaoDetalhe);
        }

        return listaRequisicaoDetalhe;
    }

    public Set<CompraPedidoDetalhe> geraPedido() throws Exception {

        List<EstoqueIdealDTO> listaProduto = getItensCompraSugerida();
        Set<CompraPedidoDetalhe> listaPedidoDetalhe = new HashSet<>();
        for (EstoqueIdealDTO p : listaProduto) {
            CompraPedidoDetalhe pedidoDetalhe = new CompraPedidoDetalhe();
            pedidoDetalhe.setProduto(p.getProduto());
            pedidoDetalhe.setQuantidade(p.getEstoqueIdeal().subtract(p.getControle()));
            pedidoDetalhe.setValorUnitario(p.getValorCompra());

            listaPedidoDetalhe.add(pedidoDetalhe);
        }
        return listaPedidoDetalhe;
    }

    public List<EstoqueIdealDTO> getItensCompraSugerida() {
        List<EstoqueIdealDTO> listaProduto = new ArrayList<>();
        try {

            listaProduto = estoqueRepository.getItensCompraSugerida(empresa);

        } catch (Exception ex) {
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

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getTipoCompraSugerida() {
        return tipoCompraSugerida;
    }

    public void setTipoCompraSugerida(String tipoCompraSugerida) {
        this.tipoCompraSugerida = tipoCompraSugerida;
    }
}
