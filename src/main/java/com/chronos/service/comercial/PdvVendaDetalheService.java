package com.chronos.service.comercial;

import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.entidades.PdvVendaDetalhe;
import com.chronos.modelo.entidades.Produto;
import com.chronos.service.AbstractService;

import java.math.BigDecimal;
import java.util.Optional;

public class PdvVendaDetalheService extends AbstractService<PdvVendaDetalhe> {


    public PdvVendaCabecalho addProduto(PdvVendaCabecalho venda, PdvVendaDetalhe item) {
        Optional<PdvVendaDetalhe> itemOpt = getItemVenda(venda, item.getProduto());
        BigDecimal quantidade = item.getQuantidade();
        if (itemOpt.isPresent()) {
            quantidade = quantidade.add(itemOpt.get().getQuantidade());
            item = itemOpt.get();
            item.setQuantidade(quantidade);
        } else {
            venda.getListaPdvVendaDetalhe().add(0, item);
        }
        venda.calcularValorTotal();
        return venda;
    }

    public Optional<PdvVendaDetalhe> getItemVenda(PdvVendaCabecalho venda, Produto produto) {
        return venda.getListaPdvVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

    }

    public boolean verificarRestricao(PdvVendaDetalhe item) throws Exception {
        this.objeto = item;
        return verificarRestricao();
    }

    @Override
    protected Class<PdvVendaDetalhe> getClazz() {
        return PdvVendaDetalhe.class;
    }


}
