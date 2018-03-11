package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.Auditoria;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AuditoriaControll extends AbstractControll<Auditoria> implements Serializable {


    @Override
    public ERPLazyDataModel<Auditoria> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(Auditoria.class);
        }

        Object[] atributos = new Object[]{"dataRegistro", "horaRegistro", "janelaController", "acao", "conteudo", "usuario.login"};

        dataModel.setAtributos(atributos);
        return dataModel;
    }

    @Override
    protected Class<Auditoria> getClazz() {
        return Auditoria.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "AUDITORIA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
