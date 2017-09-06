/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.OperadoraCartao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class OperadoraCartaoControll extends AbstractControll<OperadoraCartao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ContaCaixa> contasCaixa;

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> contas = new LinkedList<>();
        try {
            Object[] atributos = new Object[]{"nome"};
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, nome));
            filtros.add(new Filtro(Filtro.AND, "agenciaBanco", Filtro.NAO_NULO, ""));
            contas = contasCaixa.getEntitys(ContaCaixa.class, filtros, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return contas;
    }

    @Override
    protected Class<OperadoraCartao> getClazz() {
        return OperadoraCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OPERADORA_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
