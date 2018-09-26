package com.chronos.controll.cadastros.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.ProdutoGrupo;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.repository.Repository;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
    private ProdutoGrupo grupo;

    private HashMap<String, Integer> listSubGrupos = new LinkedHashMap<>();



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


        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoProdutos.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutos.pdf");
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
}
