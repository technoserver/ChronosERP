package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.FinTipoPagamento;
import com.chronos.modelo.entidades.FinTipoRecebimento;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinTipoRecebimentoControll extends AbstractControll<FinTipoRecebimento> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ContaCaixa> contas;

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listContas = new LinkedList<>();
        try {
            listContas = contas.getEntitys(ContaCaixa.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listContas;
    }

    @Override
    protected Class<FinTipoRecebimento> getClazz() {
        return FinTipoRecebimento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_TIPO_RECEBIMENTO";
    }
}
