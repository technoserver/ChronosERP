package com.chronos.erp.controll.cadastros.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.modelo.entidades.ProdutoGrupo;
import com.chronos.erp.modelo.entidades.ProdutoSubGrupo;
import com.chronos.erp.repository.Repository;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 14/09/17.
 */
@Named
@ViewScoped
public class ProdutoRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ProdutoGrupo> grupos;
    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;

    private String produto;
    private Integer subGrupo;
    private String inativo;
    private String tipoProduto;
    private String estoque;
    private String resumido;
    private ProdutoGrupo grupo;

    private HashMap<String, Integer> listSubGrupos = new LinkedHashMap<>();

    private String ordernarPor;

    private int tipoOrdenacao;


    public void executarRelatorio() {


        boolean estoqueVerificado = grupo.getId() == 999;

        parametros = new HashMap<>();
        parametros.put("produto", retornaValorPadrao(produto));
        parametros.put("idsubgrupo", subGrupo);
        parametros.put("inativo", StringUtils.isEmpty(inativo) ? null : inativo);
        parametros.put("tipoProduto", StringUtils.isEmpty(tipoProduto) ? null : tipoProduto);
        parametros.put("idempresa", empresa.getId());
        parametros.put("estoqueVerificado", estoqueVerificado);


        String filtro = estoqueVerificado ? " ep.estoque_verificado" : " ep.quantidade_estoque";
        if (!StringUtils.isEmpty(estoque) && estoque.equals("P")) {
            parametros.put("filtro", "AND " + filtro + " >=0");
        } else if (!StringUtils.isEmpty(estoque) && estoque.equals("N")) {
            parametros.put("filtro", "AND " + filtro + "< 0");
        }

        String campo = "id";
        SortOrderEnum orderEnum = SortOrderEnum.ASCENDING;
        if (!ordernarPor.equals("id")) {
            campo = ordernarPor;
            if (estoqueVerificado && ordernarPor.equals("I")) {
                campo = "estoque_verificado";
            } else if (ordernarPor.equals("I")) {
                campo = "quantidade_estoque";
            }

            orderEnum = tipoOrdenacao == 1 ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
        }
        definirOrdenacao(parametros, campo, orderEnum);

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = resumido.equals("N") ? "relacaoProdutos.jasper" : "relacaoProdutosResumido.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutos.pdf");
    }

    public void executarRelatorioGrade() {


        boolean estoqueVerificado = grupo.getId() == 999;

        parametros = new HashMap<>();
        parametros.put("produto", retornaValorPadrao(produto));
        parametros.put("idsubgrupo", subGrupo);
        parametros.put("inativo", StringUtils.isEmpty(inativo) ? null : inativo);
        parametros.put("tipoProduto", StringUtils.isEmpty(tipoProduto) ? null : tipoProduto);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoProdutosGrade.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutosGrade.pdf");
    }

    public List<ProdutoGrupo> getGrupos() {
        List<ProdutoGrupo> list = new LinkedList<>();
        try {
            list = grupos.getEntitys(ProdutoGrupo.class, "nome", "%", new Object[]{"nome"});

            if (listSubGrupos.isEmpty()) {
                listSubGrupos.put("selecione", null);
            }


        } catch (Exception ex) {

        }
        list.add(0, new ProdutoGrupo(0, "Selecione"));
        list.add(new ProdutoGrupo(999, "Sem controle de saldo"));
        return list;
    }

    public void buscarSubGrupo() {

        listSubGrupos = new LinkedHashMap<>();
        try {
            grupo = grupo == null ? new ProdutoGrupo(0, "") : grupo;
            listSubGrupos.putAll(subGrupos.getEntitys(ProdutoSubGrupo.class, "produtoGrupo.id", grupo.getId(), new Object[]{"nome"})
                    .stream()
                    .collect(Collectors.toMap(ProdutoSubGrupo::getNome, ProdutoSubGrupo::getId)));


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    private void definirOrdenacao(Map paramentros, String campo, SortOrderEnum sort) {
        List<JRSortField> sortList = new ArrayList<JRSortField>();
        JRDesignSortField sortField = new JRDesignSortField();
        sortField.setName(campo);
        sortField.setOrder(sort);
        sortField.setType(SortFieldTypeEnum.FIELD);
        sortList.add(sortField);

        paramentros.put(JRParameter.SORT_FIELDS, sortList);

    }


    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }


    public String getInativo() {
        return inativo;
    }

    public void setInativo(String inativo) {
        this.inativo = inativo;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public ProdutoGrupo getGrupo() {
        return grupo;
    }

    public void setGrupo(ProdutoGrupo grupo) {
        this.grupo = grupo;
    }


    public Integer getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(Integer subGrupo) {
        this.subGrupo = subGrupo;
    }

    public HashMap<String, Integer> getListSubGrupos() {
        return listSubGrupos;
    }

    public void setListSubGrupos(HashMap<String, Integer> listSubGrupos) {
        this.listSubGrupos = listSubGrupos;
    }

    public String getEstoque() {
        return estoque;
    }

    public void setEstoque(String estoque) {
        this.estoque = estoque;
    }

    public String getOrdernarPor() {
        return ordernarPor;
    }

    public void setOrdernarPor(String ordernarPor) {
        this.ordernarPor = ordernarPor;
    }

    public int getTipoOrdenacao() {
        return tipoOrdenacao;
    }

    public void setTipoOrdenacao(int tipoOrdenacao) {
        this.tipoOrdenacao = tipoOrdenacao;
    }

    public String getResumido() {
        return resumido;
    }

    public void setResumido(String resumido) {
        this.resumido = resumido;
    }
}
