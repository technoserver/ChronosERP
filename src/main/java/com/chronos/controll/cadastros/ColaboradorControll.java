/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Colaborador;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */

@Named
@ViewScoped
public class ColaboradorControll extends AbstractControll<Colaborador> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String completo;

    @Override
    protected Class<Colaborador> getClazz() {
        return Colaborador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COLABORADOR";
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }
}
