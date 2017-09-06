/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AtividadeForCli;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.modelo.entidades.SituacaoForCli;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class FornecedorControll  extends AbstractControll<Fornecedor> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<AtividadeForCli> atividades;
    @Inject
    private Repository<SituacaoForCli> situacoes;


    @Override
    public void salvar() {
        super.salvar();
    }

    public List<AtividadeForCli> getListaAtividadeForCli(String nome) {
        List<AtividadeForCli> listaAtividadeForCli = new ArrayList<>();
        try {
            listaAtividadeForCli = atividades.getEntitys(AtividadeForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAtividadeForCli;
    }

    public List<SituacaoForCli> getListaSituacaoForCli(String nome) {
        List<SituacaoForCli> listaSituacaoForCli = new ArrayList<>();
        try {
            listaSituacaoForCli = situacoes.getEntitys(SituacaoForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoForCli;
    }

    @Override
    protected Class<Fornecedor> getClazz() {
        return Fornecedor.class;
    }

    @Override
    protected String getFuncaoBase() {

        return "FORNECEDOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
    
}
