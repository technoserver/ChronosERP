/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Feriados;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class FeriadosControll extends AbstractControll<Feriados> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Feriados> getClazz() {
        return Feriados.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FERIADOS";
    }
    
}
