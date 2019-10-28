package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.CentroResultado;
import com.chronos.erp.modelo.entidades.CtResultadoNtFinanceira;
import com.chronos.erp.modelo.entidades.NaturezaFinanceira;
import com.chronos.erp.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 25/12/17.
 */
@Named
@ViewScoped
public class CentroResultadoNaturezaControll extends AbstractControll<CtResultadoNtFinanceira> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<NaturezaFinanceira> financeiraRepository;
    @Inject
    private Repository<CentroResultado> centroRepository;


    @Override
    public ERPLazyDataModel<CtResultadoNtFinanceira> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(CtResultadoNtFinanceira.class);
        }


        dataModel.setAtributos(new Object[]{"naturezaFinanceira.id", "naturezaFinanceira.descricao", "centroResultado.id", "centroResultado.descricao"});
        return dataModel;
    }

    public List<NaturezaFinanceira> getListnaturezaFinanceira(String nome) {

        List<NaturezaFinanceira> naturezas = new ArrayList<>();

        try {
            naturezas = financeiraRepository.getEntitys(NaturezaFinanceira.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return naturezas;
    }

    public List<CentroResultado> getListCentroResultado(String nome) {

        List<CentroResultado> centros = new ArrayList<>();

        try {
            centros = centroRepository.getEntitys(CentroResultado.class, "descricao", nome, new Object[]{"descricao"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return centros;
    }

    @Override
    protected Class<CtResultadoNtFinanceira> getClazz() {
        return CtResultadoNtFinanceira.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CENTRO_RESULTADO_NATUREZA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
