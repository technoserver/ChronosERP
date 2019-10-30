/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.Cargo;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class CargoControll extends AbstractControll<Cargo> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public ERPLazyDataModel<Cargo> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(Cargo.class);
            dataModel.setDao(dao);

        }
        Object[] atributos = new Object[]{"nome", "descricao", "salario", "cbo1994", "cbo2002", "empresa.id"};
        dataModel.setAtributos(atributos);
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<Cargo> getClazz() {
        return Cargo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CARGO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
