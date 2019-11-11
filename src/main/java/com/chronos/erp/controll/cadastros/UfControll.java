/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.Uf;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class UfControll extends AbstractControll<Uf> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public ERPLazyDataModel<Uf> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        joinFetch = new Object[]{"pais"};
        dataModel.setJoinFetch(joinFetch);
        return dataModel;
    }

    @Override
    protected Class<Uf> getClazz() {
        return Uf.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "UF";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
