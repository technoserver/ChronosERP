package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.FinLancamentoReceberCartao;
import com.chronos.modelo.entidades.OperadoraCartao;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 15/03/18.
 */
@Named
@ViewScoped
public class FinLancamentoReceberCartaoControll extends AbstractControll<FinLancamentoReceberCartao> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public ERPLazyDataModel<FinLancamentoReceberCartao> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(FinLancamentoReceberCartao.class);
            dataModel.setDao(dao);
        }

        return dataModel;
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

//            OperadoraCartao operadoraCartao = getObjeto().getOperadoraCartao();
//            OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(new ArrayList<>(operadoraCartao.getListaOperadoraCartaoTaxas()), qtdParcelas);
//            BigDecimal taxa = operadoraCartaoTaxa.getTaxaAdm();
//            FinParcelaReceberCartao parcelaReceber;
//            int number = 1;
//            int qtdParcelas = getObjeto().getIntervaloEntreParcelas();
//            BigDecimal valor = getObjeto().getValorBruto();
//            BigDecimal valorParcela = valor.divide(BigDecimal.valueOf(qtdParcelas), BigDecimal.ROUND_DOWN);
//            for (int i = 0; i < qtdParcelas; i++) {
//                parcelaReceber = new FinParcelaReceberCartao();
//                parcelaReceber.setFinLancamentoReceberCartao(getObjeto());
//                parcelaReceber.setNumeroParcela(number++);
//                parcelaReceber.setPago(false);
//                parcelaReceber.setContaCaixa( getObjeto().getOperadoraCartao().getContaCaixa());
//                parcelaReceber.setDataEmissao(getObjeto().getDataLancamento());
//                parcelaReceber.setDataVencimento(Biblioteca.addDay(getObjeto().getDataLancamento(), 30));
//
//                parcelaReceber.setValorBruto(valorParcela);
//                parcelaReceber.setTaxaAplicada(taxa);
//                BigDecimal valorEcargosParcela = Biblioteca.calcTaxa(valorParcela, taxa);
//                parcelaReceber.setValorEncargos(valorEcargosParcela);
//                parcelaReceber.setValorLiquido(valorParcela.subtract(valorEcargos));
//
//                getObjeto().getListaFinParcelaReceberCartao().add(parcelaReceber);
//            }

            dao.salvar(getObjeto());
        } catch (Exception ex) {

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
