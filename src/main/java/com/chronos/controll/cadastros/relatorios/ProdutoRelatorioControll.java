package com.chronos.controll.cadastros.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.ProdutoGrupo;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    private String subGrupo;
    private String inativo;
    private String tipoProduto;
    private ProdutoGrupo grupo;

    private List<String> listSubGrupos = new ArrayList<>();



    public void executarRelatorio() {


        parametros = new HashMap<>();
        parametros.put("produto", retornaValorPadrao(produto));
        parametros.put("subgrupo", subGrupo == null || subGrupo.equals("selecione") ? "%" : subGrupo);
        parametros.put("inativo", inativo);
        parametros.put("tipoProduto", tipoProduto);
        parametros.put("idempresa", empresa.getId());
        parametros.put("estoqueVerificado", grupo.getId() == 999);

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoProdutos.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutos.pdf");
    }

    public List<ProdutoGrupo> getGrupos() {
        List<ProdutoGrupo> list = new LinkedList<>();
        try {
            list = grupos.getEntitys(ProdutoGrupo.class, "nome", "%", new Object[]{"nome"});

            if (listSubGrupos.isEmpty()) {
                listSubGrupos.add("selecione");
            }


        } catch (Exception ex) {

        }
        list.add(0, new ProdutoGrupo(0, "Selecione"));
        list.add(new ProdutoGrupo(999, "Sem controle de saldo"));
        return list;
    }

    public void buscarSubGrupo() {

        listSubGrupos = new ArrayList<>();
        try {
            grupo = grupo == null ? new ProdutoGrupo(0, "") : grupo;
            listSubGrupos = subGrupos.getEntitys(ProdutoSubGrupo.class, "produtoGrupo.id", grupo.getId(), new Object[]{"nome"})
                    .stream()
                    .map(ProdutoSubGrupo::getNome)
                    .collect(Collectors.toList());
            listSubGrupos.add(0, "Selecione");
            produto = "teste";
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


    public String getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo = subGrupo;
    }

    public List<String> getListSubGrupos() {
        return listSubGrupos;
    }

    public void setListSubGrupos(List<String> listSubGrupos) {
        this.listSubGrupos = listSubGrupos;
    }
}
