package com.chronos.controll.vendas;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.VendaDevolucao;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DevolucaoVendaControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private ERPLazyDataModel<VendaDevolucao> dataModel;

    @Inject
    private Repository<VendaDevolucao> repository;

    public ERPLazyDataModel<VendaDevolucao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(repository);
            dataModel.setClazz(VendaDevolucao.class);
        }

        return dataModel;
    }
}
