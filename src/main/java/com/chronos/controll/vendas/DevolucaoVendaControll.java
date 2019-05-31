package com.chronos.controll.vendas;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.VendaDevolucao;
import com.chronos.modelo.entidades.VendaDevolucaoItem;
import com.chronos.repository.Repository;
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

        dataModel.setAtributos(new Object[]{"dataDevolucao", "valorCredito", "totalParcial", "creditoUtilizado", "vendaCabecalho.id"});

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
