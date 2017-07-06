/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Municipio;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class MunicipioControll extends AbstractControll<Municipio> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public ERPLazyDataModel<Municipio> getDataModel() {

        if(dataModel==null){
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        joinFetch = new Object[]{"uf"};
        dataModel.setJoinFetch(joinFetch);
        return dataModel;
    }

    @Override
    protected Class<Municipio> getClazz() {
        return Municipio.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "MUNICIPIO";
    }
    
}
