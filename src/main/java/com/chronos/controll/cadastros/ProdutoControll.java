/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Almoxarifado;
import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.ProdutoMarca;
import com.chronos.modelo.entidades.ProdutoSubGrupo;
import com.chronos.modelo.entidades.TributGrupoTributario;
import com.chronos.modelo.entidades.TributIcmsCustomCab;
import com.chronos.modelo.entidades.UnidadeProduto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ProdutoControll extends AbstractControll<Produto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;
    @Inject
    private Repository<TributIcmsCustomCab> icmsCustomizados;
    @Inject
    private Repository<UnidadeProduto> unidades;
    @Inject
    private Repository<Almoxarifado> almoxarifados;
    @Inject
    private Repository<TributGrupoTributario> gruposTributarios;
    @Inject
    private Repository<ProdutoMarca> marcas;

    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.
        getObjeto().setExcluido("N");
        getObjeto().setDataCadastro(new Date());

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
                Produto p = dao.get(Produto.class,filtros);
                if (p != null) {
                    Mensagem.addWarnMessage("Este GTIN já está sendo utilizado por outro produto.");
                } else {
                    super.salvar(null);
                }
            }

        } catch (Exception ex) {
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", ex);
        }

        super.salvar(); //To change body of generated methods, choose Tools | Templates.
    }

    public void alteraTributacao() {
        getObjeto().setTributIcmsCustomCab(null);
        getObjeto().setTributGrupoTributario(null);
    }

    public List<ProdutoSubGrupo> getListaSubgrupo(String nome) {
        List<ProdutoSubGrupo> listaSubgrupo = new ArrayList<>();
        try {

            listaSubgrupo = subGrupos.getEntitys(ProdutoSubGrupo.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSubgrupo;
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
            listaMarcaProduto = marcas.getEntitys(ProdutoMarca.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaMarcaProduto;
    }

    @Override
    protected Class<Produto> getClazz() {
        return Produto.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO";
    }

}
