package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class EstoqueFormacaoPrecoControll extends AbstractControll<ProdutoSubGrupo> {

    private static final long serialVersionUID = 1L;

    private BigDecimal encargos;
    private BigDecimal markup;
    private List<Produto> listaProduto;

    @Inject
    private Repository<Produto> produtos;




    public void efetuarCalculos() {
        try {
            /*
          C = Valor Compra
          E = % de encargos sobre vendas
          M = % markup  sobre o custo
          P = preço de venda

          P = C(1 + M) ÷ (1 - E)
             */
            if (encargos == null) {
                Mensagem.addInfoMessage("Informe os encargos da venda.");
                return;
            }
            if (markup == null) {
                Mensagem.addInfoMessage("Informe o markup.");
                return;
            }

            if (listaProduto.isEmpty()) {
                Mensagem.addInfoMessage("Nenhum item para formar preço.");
                return;
            }
            BigDecimal valorVenda;
            BigDecimal valorCompra;
            BigDecimal encargo = Biblioteca.divide(this.encargos, BigDecimal.valueOf(100));
            BigDecimal markup = Biblioteca.divide(this.markup, BigDecimal.valueOf(100));
            for (Produto p : listaProduto) {
                if (p.getValorCompra() != null) {
                    valorCompra = p.getValorCompra();
                    valorVenda = valorCompra.multiply(BigDecimal.ONE.add(markup));
                    valorVenda = valorCompra.divide(BigDecimal.ONE.subtract(encargo), 2, RoundingMode.DOWN);

                    p.setValorVenda(valorVenda);
                    p.setMarkup(markup);
                    p.setEncargosVenda(encargo);
                }else{
                    p.setValorVenda(BigDecimal.ZERO);
                    p.setMarkup(BigDecimal.ZERO);
                    p.setEncargosVenda(BigDecimal.ZERO);
                }
            }
            Mensagem.addInfoMessage("Cálculos Efetuados.");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void buscaGrupoProdutos() {
        try {
            listaProduto = produtos.getEntitys(Produto.class, "produtoSubgrupo", getObjeto(),atributos);
        } catch (Exception e) {
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void salvarCalculos() {
        try {
            if (listaProduto.isEmpty()) {
                Mensagem.addInfoMessage("Nenhum produto na lista");
            } else {
                for (Produto p : listaProduto) {
                    produtos.atualizar(p);
                }
            }
            Mensagem.addInfoMessage("Preços formados com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    @Override
    protected Class<ProdutoSubGrupo> getClazz() {
        return ProdutoSubGrupo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "ESTOQUE_FORMACAO_PRECO";
    }
}
