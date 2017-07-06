/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Cliente;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ClienteControll extends AbstractControll<Cliente> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Cliente> getClazz() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getFuncaoBase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
