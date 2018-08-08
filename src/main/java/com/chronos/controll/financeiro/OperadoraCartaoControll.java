package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.OperadoraCartao;
import com.chronos.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ContaCaixaService;
import com.chronos.service.financeiro.OperadoraCartaoService;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.RowEditEvent;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 14/03/18.
 */
@Named
@ViewScoped
public class OperadoraCartaoControll extends AbstractControll<OperadoraCartao> implements Serializable {


    private static final long serialVersionUID = 1L;


    @Inject
    private ContaCaixaService caixaService;
    @Inject
    private OperadoraCartaoService service;

    private List<OperadoraCartaoTaxa> operadoraCartaoTaxas;


    @Override
    public void doCreate() {
        super.doCreate();
        operadoraCartaoTaxas = new ArrayList<>();
        OperadoraCartaoTaxa taxa = new OperadoraCartaoTaxa();
        taxa.setOperadoraCartao(getObjeto());
        operadoraCartaoTaxas.add(taxa);
    }

    @Override
    public void doEdit() {
        OperadoraCartao operadoraCartao = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(operadoraCartao);
        operadoraCartaoTaxas = new ArrayList<>(operadoraCartao.getListaOperadoraCartaoTaxas());
        if (operadoraCartaoTaxas.isEmpty()) {
            OperadoraCartaoTaxa taxa = new OperadoraCartaoTaxa();
            taxa.setOperadoraCartao(getObjeto());
            operadoraCartaoTaxas.add(taxa);
        }
        setTelaGrid(false);
    }

    @Override
    public void salvar() {
        getObjeto().setListaOperadoraCartaoTaxas(new HashSet<>(operadoraCartaoTaxas));
        super.salvar();
    }

    public void onRowEdit(RowEditEvent event) {

        try {
            OperadoraCartaoTaxa cartaoTaxa = (OperadoraCartaoTaxa) event.getObject();
            service.validarIntevalo(operadoraCartaoTaxas, cartaoTaxa);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                FacesContext.getCurrentInstance().validationFailed();
                Mensagem.addErrorMessage("Erro ao alterar o intervalo de taxas", ex);
            } else {
                throw new RuntimeException("Erro ao alterar o intervalo de taxas", ex);
            }
        }
    }

    public void onRowCancel(RowEditEvent event) {
        OperadoraCartaoTaxa cartaoTaxa = (OperadoraCartaoTaxa) event.getObject();
        if (operadoraCartaoTaxas.size() > 1) {
            operadoraCartaoTaxas.remove(cartaoTaxa);
        } else {
            Mensagem.addErrorMessage("É preciso ao menos ter 1 intervalo definido");
        }

    }
    public void onAddNew() {
        OperadoraCartaoTaxa taxa = service.addTaxa(new ArrayList<>(operadoraCartaoTaxas));
        taxa.setOperadoraCartao(getObjeto());
        operadoraCartaoTaxas.add(taxa);
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> contas = new LinkedList<>();
        try {
            contas = caixaService.getListaContaCaixaComAgencia(nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return contas;
    }

    @Override
    protected Class<OperadoraCartao> getClazz() {
        return OperadoraCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OPERADORA_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public List<OperadoraCartaoTaxa> getOperadoraCartaoTaxas() {
        return operadoraCartaoTaxas;
    }

    public void setOperadoraCartaoTaxas(List<OperadoraCartaoTaxa> operadoraCartaoTaxas) {
        this.operadoraCartaoTaxas = operadoraCartaoTaxas;
    }
}
