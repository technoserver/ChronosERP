package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.modelo.entidades.FinLancamentoReceberCartao;
import com.chronos.erp.modelo.entidades.OperadoraCartao;
import com.chronos.erp.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.erp.repository.OperadoraCartaoRepository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.FinLancamentoReceberCartaoService;
import com.chronos.erp.service.financeiro.OperadoraCartaoService;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 15/03/18.
 */
@Named
@ViewScoped
public class FinLancamentoReceberCartaoControll extends AbstractControll<FinLancamentoReceberCartao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OperadoraCartaoRepository operadoraCartaoRepository;
    @Inject
    private OperadoraCartaoService operadoraCartaoService;
    @Inject
    private FinLancamentoReceberCartaoService service;

    @Override
    public ERPLazyDataModel<FinLancamentoReceberCartao> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(FinLancamentoReceberCartao.class);
            dataModel.setDao(dao);
        }
        dataModel.setOrdernarPor("dataLancamento");
        dataModel.setSortOrder(SortOrder.DESCENDING);
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setDataLancamento(new Date());
    }

    @Override
    public void doEdit() {
        FinLancamentoReceberCartao lan = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(lan);
        setTelaGrid(false);
    }

    @Override
    public void salvar() {
        try {
            OperadoraCartao operadoraCartao = getObjeto().getOperadoraCartao();
            OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(new ArrayList<>(operadoraCartao.getListaOperadoraCartaoTaxas()), getObjeto().getQuantidadeParcela());
            FinLancamentoReceberCartao lancamento = service.gerarLancamento(getObjeto(), operadoraCartaoTaxa);
            setObjeto(lancamento);
            dao.atualizar(getObjeto());
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao gerar o lançamento", ex);
            } else {
                throw new RuntimeException("Erro ao gerar o lançamento", ex);
            }
        }
    }

    public List<OperadoraCartao> getListaOperadoraCartao(String nome) {
        List<OperadoraCartao> operadoras = new ArrayList<>();
        try {
            operadoras = operadoraCartaoRepository.getOperadoraResumidaComintervalo(nome.toLowerCase().trim());
        } catch (Exception ex) {

        }
        return operadoras;
    }

    public void selecionarOperadora(SelectEvent event) {
        OperadoraCartao operadoraCartao = (OperadoraCartao) event.getObject();

    }

    @Override
    protected Class<FinLancamentoReceberCartao> getClazz() {
        return FinLancamentoReceberCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "LANCAMENTO_RECEBER_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
