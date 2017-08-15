package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.modelo.entidades.FinFechamentoCaixaBanco;
import com.chronos.modelo.entidades.view.ViewFinChequeNaoCompensado;
import com.chronos.modelo.entidades.view.ViewFinChequeNaoCompensadoID;
import com.chronos.modelo.entidades.view.ViewFinMovimentoCaixaBanco;
import com.chronos.modelo.entidades.view.ViewFinMovimentoCaixaBancoID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinMovimentoCaixaBancoControll extends AbstractControll<ViewFinMovimentoCaixaBancoID> implements Serializable {

    private static final long serialVersionUID = 1L;


    private Repository<ViewFinMovimentoCaixaBancoID> movimentosBanco;
    private Repository<FinFechamentoCaixaBanco> fechamentos;
    private Repository<ContaCaixa> contaCaixaDao;
    private Repository<ViewFinChequeNaoCompensadoID> cheques;

    private FinFechamentoCaixaBanco fechamentoCaixaBanco;

    private Date periodo;
    private List<ViewFinMovimentoCaixaBancoID> listaMovimentoCaixaBanco;
    private List<ViewFinMovimentoCaixaBancoID> listaMovimentoCaixaBancoDetalhe;


    @Override
    public void doEdit() {
        super.doEdit();

        buscaDados();
    }

    public void buscaDados() {
        try {
            if (periodo == null) {
                Mensagem.addInfoMessage("Necessário informar o período!");

            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.dataLancamento", Filtro.MAIOR_OU_IGUAL, getDataInicial()));
                filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.dataLancamento", Filtro.MENOR_OU_IGUAL, ultimoDiaMes()));

                if (isTelaGrid()) {
                    listaMovimentoCaixaBanco = movimentosBanco.getEntitys(ViewFinMovimentoCaixaBancoID.class, filtros);
                } else {
                    filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.idContaCaixa", Filtro.IGUAL, getObjeto().getViewFinMovimentoCaixaBanco().getIdContaCaixa()));
                    listaMovimentoCaixaBancoDetalhe = movimentosBanco.getEntitys(ViewFinMovimentoCaixaBancoID.class,filtros);
                    buscaDadosFechamento();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!", e);

        }
    }

    private Date getDataInicial() {
        try {
            if (periodo == null) {
                return null;
            }
            Calendar dataValida = Calendar.getInstance();
            dataValida.setTime(periodo);
            dataValida.setLenient(false);

            dataValida.set(Calendar.DAY_OF_MONTH, 1);

            dataValida.getTime();

            return dataValida.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    private Date ultimoDiaMes() {
        Calendar dataF = Calendar.getInstance();
        dataF.setTime(periodo);
        dataF.setLenient(false);
        dataF.set(Calendar.DAY_OF_MONTH, dataF.getActualMaximum(Calendar.DAY_OF_MONTH));

        return dataF.getTime();
    }

    private void buscaDadosFechamento() throws Exception {
        DecimalFormat formatoMes = new DecimalFormat("00");
        Calendar dataFechamento = Calendar.getInstance();
        dataFechamento.setTime(getDataInicial());
        int mes = Integer.valueOf(formatoMes.format(dataFechamento.get(Calendar.MONTH) + 1));
        int ano = dataFechamento.get(Calendar.YEAR);

        ContaCaixa contaCaixa = contaCaixaDao.get(getObjeto().getViewFinMovimentoCaixaBanco().getIdContaCaixa(),ContaCaixa.class);

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "contaCaixa", Filtro.IGUAL, contaCaixa));
        filtros.add(new Filtro(Filtro.AND, "mes", Filtro.IGUAL, String.valueOf(mes)));
        filtros.add(new Filtro(Filtro.AND, "ano", Filtro.IGUAL, String.valueOf(ano)));

        fechamentoCaixaBanco = fechamentos.get(FinFechamentoCaixaBanco.class, filtros);
        if (fechamentoCaixaBanco == null) {
            fechamentoCaixaBanco = new FinFechamentoCaixaBanco();
            fechamentoCaixaBanco.setContaCaixa(contaCaixa);
        }

        //busca saldo anterior
        mes -= 1;
        if (mes == 0) {
            mes = 12;
            ano -= 1;
        }
        filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "contaCaixa", Filtro.IGUAL, contaCaixa));
        filtros.add(new Filtro(Filtro.AND, "mes", Filtro.IGUAL, String.valueOf(mes)));
        filtros.add(new Filtro(Filtro.AND, "ano", Filtro.IGUAL, String.valueOf(ano)));

        FinFechamentoCaixaBanco fechamentoAnterior = fechamentos.get(FinFechamentoCaixaBanco.class, filtros);

        if (fechamentoAnterior != null && fechamentoAnterior.getSaldoAnterior() != null) {
            fechamentoCaixaBanco.setSaldoAnterior(fechamentoAnterior.getSaldoAnterior());
        } else {
            fechamentoCaixaBanco.setSaldoAnterior(BigDecimal.ZERO);
        }

        //calcula totais
        BigDecimal recebimentos = BigDecimal.ZERO;
        BigDecimal pagamentos = BigDecimal.ZERO;
        BigDecimal chequesNaoCompensados = BigDecimal.ZERO;
        for (ViewFinMovimentoCaixaBancoID v : listaMovimentoCaixaBanco) {
            ViewFinMovimentoCaixaBanco movimento = (ViewFinMovimentoCaixaBanco) Biblioteca.nullToEmpty(v.getViewFinMovimentoCaixaBanco(), false);

            if (movimento.getOperacao().equals("E")) {
                recebimentos = recebimentos.add(movimento.getValor());
            }
            if (movimento.getOperacao().equals("S")) {
                pagamentos = pagamentos.add(movimento.getValor());
            }
        }
        fechamentoCaixaBanco.setPagamentos(pagamentos);
        fechamentoCaixaBanco.setRecebimentos(recebimentos);
        fechamentoCaixaBanco.setSaldoConta(fechamentoCaixaBanco.getSaldoAnterior().subtract(pagamentos).add(recebimentos));

        //busca os cheques não compensados
        filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "viewFinChequeNaoCompensado.idContaCaixa", Filtro.IGUAL, contaCaixa.getId()));

        List<ViewFinChequeNaoCompensadoID> listaChequeNaoCompensado = cheques.getEntitys(ViewFinChequeNaoCompensadoID.class, filtros);
        for (ViewFinChequeNaoCompensadoID c : listaChequeNaoCompensado) {
            ViewFinChequeNaoCompensado cheque = (ViewFinChequeNaoCompensado) Biblioteca.nullToEmpty(c.getViewFinChequeNaoCompensado(), false);

            chequesNaoCompensados = chequesNaoCompensados.add(cheque.getValor());
        }

        fechamentoCaixaBanco.setChequeNaoCompensado(chequesNaoCompensados);
        fechamentoCaixaBanco.setSaldoDisponivel(fechamentoCaixaBanco.getSaldoConta().subtract(fechamentoCaixaBanco.getChequeNaoCompensado()));
    }

    public void processaFechamento() {
        try {
            if (fechamentoCaixaBanco.getId() == null) {
                fechamentos.salvar(fechamentoCaixaBanco);
            } else {
                fechamentos.atualizar(fechamentoCaixaBanco);
            }
            Mensagem.addInfoMessage("Fechamento processado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao processar o fechamento!", e);

        }
    }

    @Override
    protected Class<ViewFinMovimentoCaixaBancoID> getClazz() {
        return ViewFinMovimentoCaixaBancoID.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_MOVIMENTO_CAIXA_BANCO";
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }

    public List<ViewFinMovimentoCaixaBancoID> getListaMovimentoCaixaBanco() {
        return listaMovimentoCaixaBanco;
    }

    public void setListaMovimentoCaixaBanco(List<ViewFinMovimentoCaixaBancoID> listaMovimentoCaixaBanco) {
        this.listaMovimentoCaixaBanco = listaMovimentoCaixaBanco;
    }

    public List<ViewFinMovimentoCaixaBancoID> getListaMovimentoCaixaBancoDetalhe() {
        return listaMovimentoCaixaBancoDetalhe;
    }

    public void setListaMovimentoCaixaBancoDetalhe(List<ViewFinMovimentoCaixaBancoID> listaMovimentoCaixaBancoDetalhe) {
        this.listaMovimentoCaixaBancoDetalhe = listaMovimentoCaixaBancoDetalhe;
    }

    public FinFechamentoCaixaBanco getFechamentoCaixaBanco() {
        return fechamentoCaixaBanco;
    }

    public void setFechamentoCaixaBanco(FinFechamentoCaixaBanco fechamentoCaixaBanco) {
        this.fechamentoCaixaBanco = fechamentoCaixaBanco;
    }

}
