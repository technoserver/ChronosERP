/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Cest;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class CestControll extends AbstractControll<Cest> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Cest> getClazz() {
        return Cest.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CEST";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
