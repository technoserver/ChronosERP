/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Banco;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class BancoControll extends AbstractControll<Banco> implements Serializable {

    private static final long serialVersionUID = 1L;

    public BancoControll() {
    }

    @Override
    public Class<Banco> getClazz() {
        return Banco.class;
    }

    @Override
    public String getFuncaoBase() {
        return "BANCO";
    }

   

}
