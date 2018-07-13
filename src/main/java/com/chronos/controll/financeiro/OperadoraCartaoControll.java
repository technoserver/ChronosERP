package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.OperadoraCartao;
import com.chronos.service.cadastros.ContaCaixaService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 14/03/18.
 */
@Named
@ViewScoped
public class OperadoraCartaoControll extends AbstractControll<OperadoraCartao> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private ContaCaixaService caixaService;




    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> contas = new LinkedList<>();
        try {
            contas = caixaService.getListaContaCaixaComAgencia(nome);
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
