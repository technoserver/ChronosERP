package com.chronos.controll.estoque;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by john on 02/08/17.
 */
@Named
@ViewScoped
public class EstoqueReajusteCabecalhoControll extends AbstractControll<EstoqueReajusteCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Colaborador> colaboradores;
    @Inject
    private Repository<ProdutoSubGrupo> subgrupos;
    @Inject
    private Repository<Produto> produtos;

    private ProdutoSubGrupo produtoSubgrupo;




    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setListaEstoqueReajusteDetalhe(new HashSet<>());
        getObjeto().setDataReajuste(new Date());
        getObjeto().setColaborador(usuario.getColaborador());
        produtoSubgrupo = new ProdutoSubGrupo();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        EstoqueReajusteCabecalho r = getDataModel().getRowData(getObjeto().getId().toString());
        setObjeto(r);
    }

    @Override
    public void salvar() {
        efetuarCalculos();
        super.salvar();
    }

    public void buscaGrupoProdutos(SelectEvent event) {
        try {
            ProdutoSubGrupo subGrupo = (ProdutoSubGrupo) event.getObject();
            getObjeto().getListaEstoqueReajusteDetalhe().clear();
            atributos = new Object[]{"nome","valorVenda"};
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("produtoSubGrupo.id",subGrupo.getId()));
            List<Produto> listaProduto = produtos.getEntitys(Produto.class, filtros,atributos);

            if (listaProduto.isEmpty()) {
                Mensagem.addInfoMessage("Nenhum produto encontrado para o grupo selecionado.");
            }else{
                listaProduto.stream().forEach((p)->{
                    EstoqueReajusteDetalhe itemReajuste = new EstoqueReajusteDetalhe();
                    itemReajuste.setEstoqueReajusteCabecalho(getObjeto());
                    itemReajuste.setProduto(p);
                    itemReajuste.setValorOriginal(p.getValorVenda());

                    getObjeto().getListaEstoqueReajusteDetalhe().add(itemReajuste);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    public void efetuarCalculos() {
        try {
            if (getObjeto().getListaEstoqueReajusteDetalhe().isEmpty()) {
                throw new Exception("Nenhum item para calcular.");

            } else {
                String tipo = getObjeto().getTipoReajuste();
                getObjeto().getListaEstoqueReajusteDetalhe().stream().forEach(r->{
                    r.setValorReajuste(calcularReajuste(r.getValorOriginal(),tipo));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro.", e);
        }
    }

    private BigDecimal calcularReajuste(BigDecimal valorOriginal,String tipo) {
        if(!Optional.ofNullable(valorOriginal).isPresent()){
            return BigDecimal.ZERO;
        }
        BigDecimal reajuste = tipo.equals("A") ? valorOriginal.multiply(BigDecimal.ONE.subtract(getObjeto().getPorcentagem().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN))) : valorOriginal.multiply(BigDecimal.ONE.add(getObjeto().getPorcentagem().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)));

        return reajuste;
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            listaColaborador = colaboradores.getEntitys(Colaborador.class, "pessoa.nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    public List<ProdutoSubGrupo> getListaSubGrupo(String nome) {
        List<ProdutoSubGrupo> listaProdutoSubGrupo = new ArrayList<>();
        try {
            atributos = null;
            listaProdutoSubGrupo = subgrupos.getEntitys(ProdutoSubGrupo.class, "nome", nome,atributos);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return listaProdutoSubGrupo;
    }

    @Override
    protected Class<EstoqueReajusteCabecalho> getClazz() {
        return EstoqueReajusteCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "ESTOQUE_REAJUSTE_CABECALHO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoSubGrupo getProdutoSubgrupo() {
        return produtoSubgrupo;
    }

    public void setProdutoSubgrupo(ProdutoSubGrupo produtoSubgrupo) {
        this.produtoSubgrupo = produtoSubgrupo;
    }
}
