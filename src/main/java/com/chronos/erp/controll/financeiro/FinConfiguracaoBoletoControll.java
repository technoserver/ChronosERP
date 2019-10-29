package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ContaCaixa;
import com.chronos.erp.modelo.entidades.FinConfiguracaoBoleto;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 14/08/17.
 */
@Named
@ViewScoped
public class FinConfiguracaoBoletoControll extends AbstractControll<FinConfiguracaoBoleto> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ContaCaixa> contas;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listaContaCaixa = new ArrayList<>();
        try {
            listaContaCaixa = contas.getEntitys(ContaCaixa.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaContaCaixa;
    }

    @Override
    protected Class<FinConfiguracaoBoleto> getClazz() {
        return FinConfiguracaoBoleto.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONFIGURACAO_BOLETO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
