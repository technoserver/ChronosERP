/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Cep;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class CepControll extends AbstractControll<Cep> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Cep> getClazz() {
        return Cep.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CEP";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
