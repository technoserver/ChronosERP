/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Almoxarifado;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class AlmoxarifadoControll extends AbstractControll<Almoxarifado> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.
        getObjeto().setEmpresa(empresa);
    }
    
    
    
    

    @Override
    protected Class<Almoxarifado> getClazz() {
        return Almoxarifado.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "ALMOXARIFADO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
    
}
