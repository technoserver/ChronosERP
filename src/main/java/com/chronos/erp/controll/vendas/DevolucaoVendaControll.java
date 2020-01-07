package com.chronos.erp.controll.vendas;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.PdvVendaCabecalho;
import com.chronos.erp.modelo.entidades.VendaCabecalho;
import com.chronos.erp.modelo.entidades.VendaDevolucao;
import com.chronos.erp.modelo.entidades.VendaDevolucaoItem;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.comercial.VendaDevolucaoService;
import com.chronos.erp.util.jsf.Mensagem;
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
    private VendaDevolucaoService service;

    @Inject
    private Repository<VendaDevolucaoItem> itemRepository;
    @Inject
    private Repository<VendaCabecalho> vendaCabecalhoRepository;
    @Inject
    private Repository<PdvVendaCabecalho> pdvVendaCabecalhoRepository;

    private String cliente;

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

            if (devolucao.getCodigoModulo().equals(Modulo.VENDA.getCodigo())) {
                VendaCabecalho venda = vendaCabecalhoRepository.get(devolucao.getIdVenda(), VendaCabecalho.class);
                if (venda != null) {
                    cliente = venda.getCliente().getPessoa().getNome();
                }
            } else if (devolucao.getCodigoModulo().equals(Modulo.PDV.getCodigo())) {
                PdvVendaCabecalho venda = pdvVendaCabecalhoRepository.get(devolucao.getIdVenda(), PdvVendaCabecalho.class);
                if (venda != null) {
                    cliente = venda.getNomeCliente();
                }
            }

            itens = itemRepository.getEntitys(VendaDevolucaoItem.class, "vendaDevolucao.id", devolucao.getId());
        }
    }

    public void gerarCreditoCliente(int id) {
        try {
            service.gerarCredito(id);
            Mensagem.addInfoMessage("Crédito gerado com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar crédito", ex);
            }
        }
    }

    public List<VendaDevolucaoItem> getItens() {
        return itens;
    }

    public String getCliente() {
        return cliente;
    }
}
