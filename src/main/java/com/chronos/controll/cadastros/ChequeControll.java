/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Cheque;
import com.chronos.modelo.entidades.TalonarioCheque;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ChequeControll extends AbstractControll<Cheque> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TalonarioCheque> talonarioDao;

    public List<TalonarioCheque> getListaTalao(String nome) {
        List<TalonarioCheque> talonarios = new LinkedList<>();
        try {
            Object[] atributos = new Object[]{"talao"};
            talonarios = talonarioDao.getEntitys(TalonarioCheque.class, "talao", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return talonarios;
    }

    @Override
    protected Class<Cheque> getClazz() {
        return Cheque.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CHEQUE";
    }

}
