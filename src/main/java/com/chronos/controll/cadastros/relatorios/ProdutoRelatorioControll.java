package com.chronos.controll.cadastros.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.ProdutoGrupo;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.repository.Repository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 14/09/17.
 */
@Named
@RequestScoped
public class ProdutoRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ProdutoGrupo> grupos;
    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;

    private String produto;
    private String subgrupo;
    private String inativo;
    private String tipoProduto;
    private ProdutoGrupo grupo;
    private ProdutoSubGrupo subGrupo;
    private List<ProdutoSubGrupo> listSubGrupos;
    private List<String> tstStrings;


    public void executarRelatorio() {
        empresa = getEmpresaUsuario();
        parametros = new HashMap<>();
        parametros.put("produto", retornaValorPadrao(produto));
        parametros.put("subgrupo", retornaValorPadrao(getSubGrupo().getNome()));
        parametros.put("inativo", inativo);
        parametros.put("tipoProduto", tipoProduto);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoProdutos.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoProdutos.pdf");
    }

    public List<ProdutoGrupo> getGrupos() {
        List<ProdutoGrupo> list = new LinkedList<>();
        try {
            list = grupos.getEntitys(ProdutoGrupo.class, "nome", "%", new Object[]{"nome"});
        } catch (Exception ex) {

        }
        list.add(0, new ProdutoGrupo(0, "Selecione"));
        return list;
    }

    public void buscarSubGrupo() {

        listSubGrupos = new ArrayList<>();
        try {
            grupo = grupo == null ? new ProdutoGrupo(0, "") : grupo;
            tstStrings = new ArrayList<>();
            tstStrings.add("teste");
            listSubGrupos = subGrupos.getEntitys(ProdutoSubGrupo.class, "produtoGrupo.id", grupo.getId(), new Object[]{"nome"});
        } catch (Exception ex) {

        }

        listSubGrupos.stream().forEach(s -> {
            s.setProdutoGrupo(grupo);
        });

    }

    public void teste() {
        subGrupo = subGrupo == null ? new ProdutoSubGrupo() : subGrupo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(String subgrupo) {
        this.subgrupo = subgrupo;
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

    public ProdutoSubGrupo getSubGrupo() {
        return subGrupo == null ? new ProdutoSubGrupo() : subGrupo;
    }

    public void setSubGrupo(ProdutoSubGrupo subGrupo) {
        this.subGrupo = subGrupo;
    }

    public List<ProdutoSubGrupo> getListSubGrupos() {
        return listSubGrupos;
    }

    public void setListSubGrupos(List<ProdutoSubGrupo> listSubGrupos) {
        this.listSubGrupos = listSubGrupos;
    }

    public List<String> getTstStrings() {

        return tstStrings;
    }

    public void setTstStrings(List<String> tstStrings) {
        this.tstStrings = tstStrings;
    }
}
