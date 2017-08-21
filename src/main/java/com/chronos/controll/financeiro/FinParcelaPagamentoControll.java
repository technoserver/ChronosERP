package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.context.RequestContext;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinParcelaPagamentoControll extends AbstractControll<FinParcelaPagar> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<FinChequeEmitido> chequesEmitidos;
    @Inject
    private Repository<FinTipoPagamento> tipos;
    @Inject
    private Repository<ContaCaixa> contas;
    @Inject
    private Repository<AdmParametro> parametros;
    @Inject
    private Repository<FinStatusParcela> status;
    @Inject
    private Repository<Cheque> cheques;

    private List<FinParcelaPagar> parcelasSelecionadas;
    private FinParcelaPagamento finParcelaPagamento;
    private FinParcelaPagamento finParcelaPagamentoSelecionado;
    private FinChequeEmitido finChequeEmitido;
    private String strTipoBaixa;
    private boolean pagamentoCheque;
    private String historico;

    @Override
    public void doEdit() {
        if (parcelasSelecionadas != null) {
            if (parcelasSelecionadas.isEmpty()) {
                Mensagem.addWarnMessage("Nenhuma parcela foi selecionada!");

            } else if (parcelasSelecionadas.size() == 1) {
                setObjetoSelecionado(parcelasSelecionadas.get(0));
                super.doEdit();
                novoPagamento();
            } else if (parcelasSelecionadas.size() > 1) {
                iniciaPagamentoCheque();
            }
        }
    }

    @Override
    public void salvar() {
        iniciaPagamento();
    }

    private void novoPagamento() {
        finParcelaPagamento = new FinParcelaPagamento();
        finParcelaPagamento.setFinParcelaPagar(getObjeto());
        finParcelaPagamento.setDataPagamento(new Date());

        strTipoBaixa = "T";
        historico = null;
    }

    public void iniciaPagamentoCheque() {
        Date dataAtual = new Date();
        BigDecimal totalParcelas = BigDecimal.ZERO;
        finChequeEmitido = new FinChequeEmitido();
        if (parcelasSelecionadas.size() > 1) {
            for (FinParcelaPagar p : parcelasSelecionadas) {
                if (p.getFinStatusParcela().getSituacao().equals("02")) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já quitada.");

                    return;
                }
                if (p.getDataVencimento().before(dataAtual)) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já vencida.");

                    return;
                }
                if (p.getSofreRetencao() != null && p.getSofreRetencao().equals("S")) {
                    Mensagem.addWarnMessage("Procedimento não permitido. Fornecedor sofre retenção.");

                    return;
                }
                if (p.getValor() != null) {
                    totalParcelas = totalParcelas.add(p.getValor());
                }
            }
            finChequeEmitido.setValor(totalParcelas);
        }
        pagamentoCheque = true;
        RequestContext.getCurrentInstance().update("formOutrasTelas:dialogFinChequeEmitido");
    }

    public void cancelaPagamentoCheque() {
        pagamentoCheque = false;
    }

    public void finalizaPagamentoCheque() {
        pagamentoCheque = false;
        incluirPagamento();
    }

    public void iniciaPagamento() {
        if (finParcelaPagamento.getFinTipoPagamento().getTipo().equals("02")) {
            iniciaPagamentoCheque();
        } else {
            incluirPagamento();
        }
    }

    private void incluirPagamento() {
        try {
            AdmParametro admParametro = parametros.get(AdmParametro.class, "empresa", empresa);

            FinStatusParcela statusParcela = null;
            if (admParametro == null) {
                throw new Exception("Parâmetros administrativos não encontrados. Entre em contato com a Software House.");
            }
            statusParcela = status.get(admParametro.getFinParcelaQuitado(), FinStatusParcela.class);
            if (statusParcela == null) {
                throw new Exception("O status de parcela 'Quitado' não está cadastrado.\nEntre em contato com a Software House.");
            }

            if (parcelasSelecionadas.size() == 1 && !finParcelaPagamento.getFinTipoPagamento().getTipo().equals("02")) {
                calculaTotalPago();

                FinParcelaPagamento pagamento = new FinParcelaPagamento();
                pagamento.setFinParcelaPagar(finParcelaPagamento.getFinParcelaPagar());
                pagamento.setContaCaixa(finParcelaPagamento.getContaCaixa());
                pagamento.setDataPagamento(finParcelaPagamento.getDataPagamento());
                pagamento.setFinTipoPagamento(finParcelaPagamento.getFinTipoPagamento());
                pagamento.setHistorico(finParcelaPagamento.getHistorico());
                pagamento.setTaxaDesconto(finParcelaPagamento.getTaxaDesconto());
                pagamento.setTaxaJuro(finParcelaPagamento.getTaxaJuro());
                pagamento.setTaxaMulta(finParcelaPagamento.getTaxaMulta());
                pagamento.setValorDesconto(finParcelaPagamento.getValorDesconto());
                pagamento.setValorJuro(finParcelaPagamento.getValorJuro());
                pagamento.setValorMulta(finParcelaPagamento.getValorMulta());
                pagamento.setValorPago(finParcelaPagamento.getValorPago());

                if (strTipoBaixa.equals("P")) {
                    statusParcela = status.get(admParametro.getFinParcelaQuitadoParcial(), FinStatusParcela.class);
                    if (statusParcela == null) {
                        throw new Exception("O status de parcela 'Quitado Parcial' não está cadastrado.\nEntre em contato com a Software House.");
                    }
                }

                getObjeto().setFinStatusParcela(statusParcela);
                getObjeto().getListaFinParcelaPagamento().add(pagamento);
                salvar("Pagamento incluído com sucesso!");
                novoPagamento();
            } else {
                FinTipoPagamento tipoPagamento = tipos.get(FinTipoPagamento.class, "tipo", "02");
                if (tipoPagamento == null) {
                    throw new Exception("Tipo de pagamento 'CHEQUE' não está cadastrado.\nEntre em contato com a Software House.");
                }

                chequesEmitidos.salvar(finChequeEmitido);
                finChequeEmitido.getCheque().setStatusCheque("U");
                cheques.atualizar(finChequeEmitido.getCheque());

                for (FinParcelaPagar p : parcelasSelecionadas) {
                    FinParcelaPagamento pagamento = new FinParcelaPagamento();
                    pagamento.setFinTipoPagamento(tipoPagamento);
                    pagamento.setFinParcelaPagar(p);
                    pagamento.setFinChequeEmitido(finChequeEmitido);
                    pagamento.setContaCaixa(finChequeEmitido.getCheque().getTalonarioCheque().getContaCaixa());
                    pagamento.setDataPagamento(finChequeEmitido.getBomPara());
                    pagamento.setHistorico(historico);
                    pagamento.setValorPago(p.getValor());

                    p.setFinStatusParcela(statusParcela);
                    p.getListaFinParcelaPagamento().add(pagamento);

                    p = dao.atualizar(p);
                }

                if (parcelasSelecionadas.size() == 1) {
                    setObjeto(dao.getEntityJoinFetch(getObjeto().getId(), FinParcelaPagar.class));
                    novoPagamento();
                }
                Mensagem.addInfoMessage("Pagamento efetuado com sucesso!");

            }
        } catch (Exception e) {

            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao incluir o pagamento!", e);
        }
    }

    public void calculaTotalPago() throws Exception {
        BigDecimal valorJuro = BigDecimal.ZERO;
        BigDecimal valorMulta = BigDecimal.ZERO;
        BigDecimal valorDesconto = BigDecimal.ZERO;
        if (finParcelaPagamento.getTaxaJuro() != null && finParcelaPagamento.getDataPagamento() != null) {
            Calendar dataPagamento = Calendar.getInstance();
            dataPagamento.setTime(finParcelaPagamento.getDataPagamento());
            Calendar dataVencimento = Calendar.getInstance();
            dataVencimento.setTime(finParcelaPagamento.getFinParcelaPagar().getDataVencimento());
            if (dataVencimento.before(dataPagamento)) {
                long diasAtraso = (dataPagamento.getTimeInMillis() - dataVencimento.getTimeInMillis()) / 86400000l;
                //valorJuro = valor * ((taxaJuro / 30) / 100) * diasAtraso
                finParcelaPagamento.setValorJuro(finParcelaPagamento.getFinParcelaPagar().getValor().multiply(finParcelaPagamento.getTaxaJuro().divide(BigDecimal.valueOf(30), RoundingMode.HALF_DOWN)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(diasAtraso)));
                valorJuro = finParcelaPagamento.getValorJuro();
            }
        }
        finParcelaPagamento.setValorJuro(valorJuro);

        if (finParcelaPagamento.getTaxaMulta() != null) {
            finParcelaPagamento.setValorMulta(finParcelaPagamento.getFinParcelaPagar().getValor().multiply(finParcelaPagamento.getTaxaMulta()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
            valorMulta = finParcelaPagamento.getValorMulta();
        } else {
            finParcelaPagamento.setValorMulta(valorMulta);
        }

        if (finParcelaPagamento.getTaxaDesconto() != null) {
            finParcelaPagamento.setValorDesconto(finParcelaPagamento.getFinParcelaPagar().getValor().multiply(finParcelaPagamento.getTaxaDesconto()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
            valorDesconto = finParcelaPagamento.getValorDesconto();
        } else {
            finParcelaPagamento.setValorDesconto(valorDesconto);
        }

        finParcelaPagamento.setValorPago(finParcelaPagamento.getFinParcelaPagar().getValor().add(valorJuro).add(valorMulta).subtract(valorDesconto));
    }

    public void excluirFinParcelaPagamento() {

        getObjeto().getListaFinParcelaPagamento().remove(finParcelaPagamentoSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public List<Cheque> getListaFinCheque(String nome) {
        List<Cheque> listaFinCheque = new ArrayList<>();
        try {
            listaFinCheque = cheques.getEntitys(Cheque.class, "statusCheque", "E");
        } catch (Exception e) {
             e.printStackTrace();
        }
        return listaFinCheque;
    }

    public List<FinTipoPagamento> getListaFinTipoPagamento(String nome) {
        List<FinTipoPagamento> listaFinTipoPagamento = new ArrayList<>();
        try {
            listaFinTipoPagamento = tipos.getEntitys(FinTipoPagamento.class,"descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinTipoPagamento;
    }

    public List<ContaCaixa> getListaContaCaixa(String nome) {
        List<ContaCaixa> listaContaCaixa = new ArrayList<>();
        try {
            listaContaCaixa = contas.getEntitys(ContaCaixa.class,"nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaContaCaixa;
    }


    @Override
    protected Class<FinParcelaPagar> getClazz() {
        return FinParcelaPagar.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_PARCELA_PAGAMENTO";
    }

    public List<FinParcelaPagar> getParcelasSelecionadas() {
        return parcelasSelecionadas;
    }

    public void setParcelasSelecionadas(List<FinParcelaPagar> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public FinParcelaPagamento getFinParcelaPagamento() {
        return finParcelaPagamento;
    }

    public void setFinParcelaPagamento(FinParcelaPagamento finParcelaPagamento) {
        this.finParcelaPagamento = finParcelaPagamento;
    }

    public FinParcelaPagamento getFinParcelaPagamentoSelecionado() {
        return finParcelaPagamentoSelecionado;
    }

    public void setFinParcelaPagamentoSelecionado(FinParcelaPagamento finParcelaPagamentoSelecionado) {
        this.finParcelaPagamentoSelecionado = finParcelaPagamentoSelecionado;
    }

    public String getStrTipoBaixa() {
        return strTipoBaixa;
    }

    public void setStrTipoBaixa(String strTipoBaixa) {
        this.strTipoBaixa = strTipoBaixa;
    }

    public FinChequeEmitido getFinChequeEmitido() {
        return finChequeEmitido;
    }

    public void setFinChequeEmitido(FinChequeEmitido finChequeEmitido) {
        this.finChequeEmitido = finChequeEmitido;
    }

    public boolean isPagamentoCheque() {
        return pagamentoCheque;
    }

    public void setPagamentoCheque(boolean pagamentoCheque) {
        this.pagamentoCheque = pagamentoCheque;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
