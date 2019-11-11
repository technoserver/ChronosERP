/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Ncm;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class NcmControll extends AbstractControll<Ncm> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Ncm> getClazz() {
        return Ncm.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NCM";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
