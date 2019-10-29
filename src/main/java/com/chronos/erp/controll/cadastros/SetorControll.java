/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Setor;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class SetorControll extends AbstractControll<Setor> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.

        getObjeto().setEmpresa(empresa);
    }


    @Override
    protected Class<Setor> getClazz() {
        return Setor.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "SETOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
