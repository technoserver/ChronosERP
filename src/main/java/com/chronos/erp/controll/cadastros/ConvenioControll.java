/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Convenio;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class ConvenioControll extends AbstractControll<Convenio> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Convenio> getClazz() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getFuncaoBase() {
        return "CONVENIO"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
