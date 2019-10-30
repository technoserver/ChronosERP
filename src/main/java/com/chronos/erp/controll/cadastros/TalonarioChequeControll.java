/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ContaCaixa;
import com.chronos.erp.modelo.entidades.TalonarioCheque;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author john
 */
@Named
@ViewScoped
public class TalonarioChequeControll extends AbstractControll<TalonarioCheque> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ContaCaixa> contasCaixa;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> contas = new LinkedList<>();
        try {
            Object[] atributos = new Object[]{"nome"};
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, nome));
            filtros.add(new Filtro(Filtro.AND, "agenciaBanco", Filtro.NAO_NULO, ""));
            contas = contasCaixa.getEntitys(ContaCaixa.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contas;
    }

    @Override
    protected Class<TalonarioCheque> getClazz() {
        return TalonarioCheque.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TALONARIO_CHEQUE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
