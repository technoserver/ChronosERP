/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.SituacaoForCli;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class SituacaoForClienteControll extends AbstractControll<SituacaoForCli> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public Class<SituacaoForCli> getClazz() {
        return SituacaoForCli.class;
    }

    @Override
    public String getFuncaoBase() {
        return "SITUACAO_FOR_CLIENTE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
