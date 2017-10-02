/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.controll.cadastros.datamodel.ProdutoEmpresaDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewProdutoEmpresa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author john
 */
@Named
@ViewScoped
public class ProdutoControll extends AbstractControll<Produto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;
    @Inject
    private Repository<ProdutoGrupo> grupos;
    @Inject
    private Repository<TributIcmsCustomCab> icmsCustomizados;
    @Inject
    private Repository<UnidadeProduto> unidades;
    @Inject
    private Repository<Almoxarifado> almoxarifados;
    @Inject
    private Repository<TributGrupoTributario> gruposTributarios;
    @Inject
    private Repository<ViewProdutoEmpresa> produtos;
    @Inject
    private Repository<EmpresaProduto> produtosEmpresa;
    @Inject
    private Repository<ProdutoMarca> marcas;
    @Inject
    private Repository<ProdutoAlteracaoItem> produtosAlterado;
    private ProdutoGrupo grupo;
    private ProdutoEmpresaDataModel produtoDataModel;
    private List<EmpresaProduto> listProdutoEmpresa;
    private ViewProdutoEmpresa produtoSelecionado;

    private String produto;
    private String strGrupo;
    private String strSubGrupo;
    private String inativo;
    private String nomeProdutoOld;

    public void pesquisar() {
        produtoDataModel.getFiltros().clear();
        produtoDataModel.addFiltro("nome", produto);
        produtoDataModel.addFiltro("grupo", strGrupo);
        produtoDataModel.addFiltro("subgrupo", strSubGrupo);
        produtoDataModel.addFiltro("inativo", inativo, Filtro.IGUAL);
        produtoDataModel.addFiltro("excluido", "N", Filtro.IGUAL);
    }

    @Override
    public ERPLazyDataModel<Produto> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
            joinFetch = new Object[]{"produtoMarca"};
            Object[] atribs = new Object[]{"nome", "valorVenda", "quantidadeEstoque", "unidadeProduto"};
            dataModel.setAtributos(atribs);
            dataModel.setJoinFetch(joinFetch);

        }
        dataModel.addFiltro("excluido", "N", Filtro.IGUAL);
        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("inativo", "N", Filtro.IGUAL);
        }

        return dataModel;
    }

    public ProdutoEmpresaDataModel getProdutoDataModel() {
        if (produtoDataModel == null) {
            produtoDataModel = new ProdutoEmpresaDataModel();
            produtoDataModel.setClazz(ViewProdutoEmpresa.class);
            produtoDataModel.setDao(produtos);
        }

        if (produtoDataModel.getFiltros().isEmpty()) {
            produtoDataModel.addFiltro("inativo", "N", Filtro.IGUAL);
            produtoDataModel.addFiltro("excluido", "N", Filtro.IGUAL);
        }
        return produtoDataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.
        getObjeto().setExcluido("N");
        getObjeto().setInativo("N");
        getObjeto().setDataCadastro(new Date());
        grupo = new ProdutoGrupo();


    }

    @Override
    public void doEdit() {
        super.doEdit();
        Produto produto = dao.getJoinFetch(produtoSelecionado.getId(), Produto.class);
        setObjeto(produto);
        grupo = getObjeto().getProdutoSubGrupo().getProdutoGrupo();
        nomeProdutoOld = getObjeto().getNome();
    }

    @Override
    public void remover() {
        getObjeto().setExcluido("S");
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Exclusao realizado com sucesso");
    }

    @Override
    public void salvar() {
        try {

            if (getObjeto().getTributGrupoTributario() == null && getObjeto().getTributIcmsCustomCab() == null) {
                Mensagem.addWarnMessage("É necesário informar o Grupo Tributário OU o ICMS Customizado.");
            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "gtin", Filtro.IGUAL, getObjeto().getGtin()));
                if (getObjeto().getId() != null) {
                    filtros.add(new Filtro(Filtro.AND, "id", Filtro.DIFERENTE, getObjeto().getId()));
                }
                Produto p = dao.get(Produto.class, filtros);
                if (p != null) {
                    Mensagem.addWarnMessage("Este GTIN já está sendo utilizado por outro produto.");
                } else {
                    super.salvar(null);
                    if (getObjeto().getServico().equals("N")) {
                        EmpresaProduto produtoEmpresa = new EmpresaProduto();
                        produtoEmpresa.setEmpresa(empresa);
                        produtoEmpresa.setProduto(getObjeto());
                        produtoEmpresa.setQuantidadeEstoque(BigDecimal.ZERO);
                        produtosEmpresa.salvar(produtoEmpresa);
                    }
                    //TODO verificar o fluxo de salva produt alterado.
                    if (!nomeProdutoOld.equals(getObjeto().getNome())) {
                        ProdutoAlteracaoItem produtoAlteracao = new ProdutoAlteracaoItem();

                    }
                }
            }

        } catch (Exception ex) {
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", ex);
        }


    }

    public void alteraTributacao() {
        getObjeto().setTributIcmsCustomCab(null);
        getObjeto().setTributGrupoTributario(null);
    }

    public List<ProdutoSubGrupo> getListaSubgrupo(String nome) {
        List<ProdutoSubGrupo> listaSubgrupo = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            if (grupo.getId() != null) {
                filtros.add(new Filtro("produtoGrupo.id", grupo.getId()));
            }
            listaSubgrupo = subGrupos.getEntitys(ProdutoSubGrupo.class, filtros);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSubgrupo;
    }

    public List<ProdutoGrupo> getListaGrupo(String nome) {
        List<ProdutoGrupo> listaGrupo = new ArrayList<>();
        try {

            listaGrupo = grupos.getEntitys(ProdutoGrupo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupo;
    }

    public List<TributIcmsCustomCab> getListaTributIcmsCustomCab(String nome) {
        List<TributIcmsCustomCab> listaTributIcmsCustomCab = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaTributIcmsCustomCab = icmsCustomizados.getEntitys(TributIcmsCustomCab.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributIcmsCustomCab;
    }

    public List<UnidadeProduto> getListaUnidadeProduto(String nome) {
        List<UnidadeProduto> listaUnidadeProduto = new ArrayList<>();
        try {

            listaUnidadeProduto = unidades.getEntitys(UnidadeProduto.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaUnidadeProduto;
    }

    public List<Almoxarifado> getListaAlmoxarifado(String nome) {
        List<Almoxarifado> listaAlmoxarifado = new ArrayList<>();
        try {

            listaAlmoxarifado = almoxarifados.getEntitys(Almoxarifado.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAlmoxarifado;
    }

    public List<TributGrupoTributario> getListaGrupoTributario(String nome) {
        List<TributGrupoTributario> listaGrupoTributario = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaGrupoTributario = gruposTributarios.getEntitys(TributGrupoTributario.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupoTributario;
    }

    public List<ProdutoMarca> getListaMarcaProduto(String nome) {
        List<ProdutoMarca> listaMarcaProduto = new ArrayList<>();
        try {
            listaMarcaProduto = marcas.getEntitys(ProdutoMarca.class, "nome", nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMarcaProduto;
    }

    public void buscarProdutoEmpresas(ToggleEvent event) {


        try {
            if (event.getVisibility() == Visibility.VISIBLE) {
                ViewProdutoEmpresa view = (ViewProdutoEmpresa) event.getData();
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro("produto.id", Filtro.IGUAL, view.getId()));
                atributos = new Object[]{"empresa.id", "empresa.razaoSocial", "quantidadeEstoque"};

                listProdutoEmpresa = produtosEmpresa.getEntitys(EmpresaProduto.class, filtros, atributos);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Não foi possivel buscar as informações das empresas!", ex);
        }

    }

    @Override
    protected Class<Produto> getClazz() {
        return Produto.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoGrupo getGrupo() {
        return grupo;
    }

    public void setGrupo(ProdutoGrupo grupo) {
        this.grupo = grupo;
    }


    public List<EmpresaProduto> getListProdutoEmpresa() {
        return listProdutoEmpresa;
    }

    public void setListProdutoEmpresa(List<EmpresaProduto> listProdutoEmpresa) {
        this.listProdutoEmpresa = listProdutoEmpresa;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getStrGrupo() {
        return strGrupo;
    }

    public void setStrGrupo(String strGrupo) {
        this.strGrupo = strGrupo;
    }

    public String getStrSubGrupo() {
        return strSubGrupo;
    }

    public void setStrSubGrupo(String strSubGrupo) {
        this.strSubGrupo = strSubGrupo;
    }

    public String getInativo() {
        return inativo;
    }

    public void setInativo(String inativo) {
        this.inativo = inativo;
    }

    public ViewProdutoEmpresa getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public void setProdutoSelecionado(ViewProdutoEmpresa produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }
}
