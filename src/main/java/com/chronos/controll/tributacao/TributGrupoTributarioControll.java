package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.TributGrupoTributario;
import com.chronos.repository.Filtro;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributGrupoTributarioControll extends AbstractControll<TributGrupoTributario> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public ERPLazyDataModel<TributGrupoTributario> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(TributGrupoTributario.class);
        }

        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        }

        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<TributGrupoTributario> getClazz() {
        return TributGrupoTributario.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "GRUPO_TRIBUTARIO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
