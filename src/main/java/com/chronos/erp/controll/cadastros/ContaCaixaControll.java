/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.AgenciaBanco;
import com.chronos.erp.modelo.entidades.ContaCaixa;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author john
 */
@Named
@ViewScoped
public class ContaCaixaControll extends AbstractControll<ContaCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<AgenciaBanco> agencias;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }


    @Override
    public void remover() {


        try {

            if (getObjetoSelecionado().getId() != null && getObjetoSelecionado().getId().equals(1)) {
                throw new ChronosException("Conta padrão do sistema não pode ser removida");
            }

            super.remover();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {

            }
        }
    }

    public List<AgenciaBanco> getListaAgenciaBanco(String nome) {
        List<AgenciaBanco> list = new LinkedList<>();
        try {
            list = agencias.getEntitys(AgenciaBanco.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return list;
    }

    @Override
    protected Class<ContaCaixa> getClazz() {
        return ContaCaixa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONTA_CAIXA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

}
