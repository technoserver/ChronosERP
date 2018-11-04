/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ProdutoGrupo;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ProdutoGrupoControll  extends AbstractControll<ProdutoGrupo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void salvar() {

        boolean existe = dao.existeRegisro(ProdutoGrupo.class, "nome", getObjeto().getNome());

        if (existe) {
            Mensagem.addErrorMessage("Já existe grupo com esse nome");
        } else {
            super.salvar();
        }


    }

    @Override
    protected Class<ProdutoGrupo> getClazz() {
        return ProdutoGrupo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "GRUPO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
    
}
