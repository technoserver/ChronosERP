/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.ProdutoGrupo;
import com.chronos.erp.modelo.entidades.ProdutoSubGrupo;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author john
 */
@Named
@ViewScoped
public class ProdutoSubgrupoControll extends AbstractControll<ProdutoSubGrupo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoGrupo> grupos;

    @Override
    public ERPLazyDataModel<ProdutoSubGrupo> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        joinFetch = new Object[]{"produtoGrupo"};
        dataModel.setJoinFetch(joinFetch);

        return dataModel;

    }

    @Override
    public void salvar() {
        boolean existe = dao.existeRegisro(ProdutoSubGrupo.class, "nome", getObjeto().getNome());

        if (existe) {
            Mensagem.addErrorMessage("Já existe subgrupo com esse nome");
        } else {
            super.salvar();
        }

    }

    public List<ProdutoGrupo> getListaProdutoGrupo(String nome) {
        List<ProdutoGrupo> listaGrupo = new ArrayList<>();
        try {
            listaGrupo = grupos.getEntitys(ProdutoGrupo.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupo;
    }

    @Override
    protected Class<ProdutoSubGrupo> getClazz() {
        return ProdutoSubGrupo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "SUBGRUPO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
