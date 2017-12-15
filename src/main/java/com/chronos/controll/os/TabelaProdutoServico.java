package com.chronos.controll.os;

import com.chronos.modelo.entidades.OsAbertura;
import com.chronos.modelo.entidades.OsProdutoServico;
import com.chronos.modelo.entidades.Produto;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

/**
 * Created by john on 28/09/17.
 */
public class TabelaProdutoServico {

    private final Set<OsProdutoServico> itens;
    private final OsAbertura os;

    public TabelaProdutoServico(OsAbertura os) {
        this.os = os;
        this.itens = os.getListaOsProdutoServico();
    }

    public BigDecimal getValorTotal() {
        return itens.stream()
                .map(OsProdutoServico::getValorTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void salvarItem(OsProdutoServico item, BigDecimal quantidade) {
        Optional<OsProdutoServico> itemOptional = buscarItem(item.getProduto());

        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            item.setQuantidade(item.getQuantidade().add(quantidade));
        } else {
            item.setQuantidade(quantidade);
            itens.add(item);
        }
        item.setTipo(item.getProduto().getServico() != null && item.getProduto().getServico().equals("S") ? 1 : 0);
        os.calcularValores();
    }

    public void excluirItem(OsProdutoServico item) {
//        int indice = IntStream.range(0, itens.size())
//                .filter(i -> itens.get(i).get.equals(item.getProduto()))
//                .findAny().getAsInt();
//        itens.remove(indice);
    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }
}
