package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.VendaDevolucao;
import com.chronos.erp.modelo.entidades.VendaDevolucaoItem;
import com.chronos.erp.repository.Repository;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class DevolucaoVendaControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private ERPLazyDataModel<VendaDevolucao> dataModel;

    private List<VendaDevolucaoItem> itens;

    @Inject
    private Repository<VendaDevolucao> repository;

    @Inject
    private Repository<VendaDevolucaoItem> itemRepository;

    public ERPLazyDataModel<VendaDevolucao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(repository);
            dataModel.setClazz(VendaDevolucao.class);
        }

        return dataModel;
    }

    public void buscarItens(ToggleEvent event) {

        if (event.getVisibility() == Visibility.VISIBLE) {
            VendaDevolucao devolucao = (VendaDevolucao) event.getData();


            itens = itemRepository.getEntitys(VendaDevolucaoItem.class, "vendaDevolucao.id", devolucao.getId());
        }
    }

    public List<VendaDevolucaoItem> getItens() {
        return itens;
    }
}
