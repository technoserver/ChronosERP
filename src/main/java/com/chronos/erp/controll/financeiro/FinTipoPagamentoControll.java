package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ContaCaixa;
import com.chronos.erp.modelo.entidades.FinTipoPagamento;
import com.chronos.erp.repository.Repository;

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
public class FinTipoPagamentoControll extends AbstractControll<FinTipoPagamento> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<ContaCaixa> contas;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

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
    protected Class<FinTipoPagamento> getClazz() {
        return FinTipoPagamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TIPO_PAGAMENTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
