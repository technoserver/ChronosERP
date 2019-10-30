package com.chronos.erp.controll.financeiro;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ContaCaixa;
import com.chronos.erp.modelo.entidades.OperadoraCartao;
import com.chronos.erp.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.erp.service.cadastros.ContaCaixaService;
import com.chronos.erp.service.financeiro.OperadoraCartaoService;
import com.chronos.erp.util.jsf.Mensagem;

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

    private OperadoraCartaoTaxa taxa;
    private OperadoraCartaoTaxa taxaSelecionada;


    @Override
    public void doCreate() {
        super.doCreate();
        operadoraCartaoTaxas = new ArrayList<>();
        taxa = new OperadoraCartaoTaxa();
        taxa.setOperadoraCartao(getObjeto());

    }

    @Override
    public void doEdit() {

        OperadoraCartao operadoraCartao = dataModel.getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(operadoraCartao);
        taxa = new OperadoraCartaoTaxa();
        taxa.setOperadoraCartao(getObjeto());
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


    public void onAddNew() {
        try {


            operadoraCartaoTaxas = service.validarIntevalo(operadoraCartaoTaxas, taxa);
            taxa = new OperadoraCartaoTaxa();
            taxa.setOperadoraCartao(getObjeto());

        } catch (Exception ex) {
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void removerIntervalo() {
        operadoraCartaoTaxas.remove(taxaSelecionada);
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

    public OperadoraCartaoTaxa getTaxa() {
        return taxa;
    }

    public void setTaxa(OperadoraCartaoTaxa taxa) {
        this.taxa = taxa;
    }

    public OperadoraCartaoTaxa getTaxaSelecionada() {
        return taxaSelecionada;
    }

    public void setTaxaSelecionada(OperadoraCartaoTaxa taxaSelecionada) {
        this.taxaSelecionada = taxaSelecionada;
    }
}
