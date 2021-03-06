/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.NivelFormacao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * @author john
 */
@Named
@ViewScoped
public class NivelFormacaoControll extends AbstractControll<NivelFormacao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<NivelFormacao> getClazz() {
        return NivelFormacao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NIVEl_FORMACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
