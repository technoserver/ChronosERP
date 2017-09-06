package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    @Override
    public ERPLazyDataModel<ProdutoSubGrupo> getDataModel() {
        if(dataModel==null){
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(ProdutoSubGrupo.class);
            dataModel.setDao(dao);

        }
        Object[] joins = new Object[]{"produtoGrupo"};
        dataModel.setJoinFetch(joins);

        return  dataModel;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        buscaGrupoProdutos();
    }

    @Override
    public void salvar() {
        salvarCalculos();

    }

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
            BigDecimal encargo = Biblioteca.divide(this.markup, BigDecimal.valueOf(100));
            BigDecimal markup1 = Biblioteca.divide(this.encargos, BigDecimal.valueOf(100));
            for (Produto p : listaProduto) {
                if (p.getValorCompra() != null) {
                    valorVenda = p.getValorCompra().multiply(BigDecimal.ONE.add(markup1)).divide(BigDecimal.ONE.subtract(encargo), 2, RoundingMode.DOWN);

                    p.setValorVenda(valorVenda);
                    p.setMarkup(markup1);
                    p.setEncargosVenda(encargos);
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
            joinFetch = new Object[]{"unidadeProduto","produtoSubGrupo"};
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("produtoSubGrupo.id",getObjeto().getId()));
            atributos = new Object[]{"nome","valorCompra","valorVenda","markup"};
            listaProduto = produtos.getEntitys(Produto.class,filtros,atributos);
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
                    List<Filtro> filtros = new ArrayList<>();
                    filtros.add(new Filtro("id",p.getId()));
                    Map<String,Object> atributos = new HashMap();
                    atributos.put("markup",p.getMarkup());
                    atributos.put("valor_venda",p.getValorVenda());
                    produtos.updateNativo(Produto.class,filtros,atributos);

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

    @Override
    protected boolean auditar() {
        return false;
    }


    public List<Produto> getListaProduto() {
        return listaProduto;
    }

    public BigDecimal getEncargos() {
        return encargos;
    }

    public void setEncargos(BigDecimal encargos) {
        this.encargos = encargos;
    }

    public BigDecimal getMarkup() {
        return markup;
    }

    public void setMarkup(BigDecimal markup) {
        this.markup = markup;
    }
}
