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

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 15/08/17.
 */
@Named
@ViewScoped
public class FinMovimentoCaixaBancoControll extends AbstractControll<ViewFinMovimentoCaixaBancoID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ViewFinMovimentoCaixaBancoID> movimentosBanco;
    @Inject
    private Repository<FinFechamentoCaixaBanco> fechamentos;
    @Inject
    private Repository<ContaCaixa> contaCaixaDao;
    @Inject
    private Repository<ViewFinChequeNaoCompensadoID> cheques;
    @Inject
    private Repository<ContaCaixa> contaRepository;

    private FinFechamentoCaixaBanco fechamentoCaixaBanco;

    private Date periodo;
    private List<ViewFinMovimentoCaixaBancoID> listaMovimentoCaixaBanco;

    private int idconta;
    private Map<String, Integer> contaCaixa;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        contaCaixa = new LinkedHashMap<>();


        contaCaixa.putAll(contaRepository.getEntitys(ContaCaixa.class, new Object[]{"nome"}).stream()
                .collect(Collectors.toMap(ContaCaixa::getNome, ContaCaixa::getId)));
    }

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
                filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.dataLancamento", Filtro.MAIOR_OU_IGUAL, Biblioteca.getDataInicial(periodo)));
                filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.dataLancamento", Filtro.MENOR_OU_IGUAL, Biblioteca.ultimoDiaMes(periodo)));
                if (idconta > 0) {
                    filtros.add(new Filtro(Filtro.AND, "viewFinMovimentoCaixaBanco.idContaCaixa", Filtro.IGUAL, idconta));
                }
                listaMovimentoCaixaBanco = movimentosBanco.getEntitys(ViewFinMovimentoCaixaBancoID.class, filtros);
                if (listaMovimentoCaixaBanco.isEmpty()) {
                    Mensagem.addInfoMessage("Não existe movimento no periodo informado");
                } else {
                    setTelaGrid(false);
                    setObjeto(new ViewFinMovimentoCaixaBancoID());
                    buscaDadosFechamento();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao buscar os dados!", e);

        }
    }



    private void buscaDadosFechamento() throws Exception {
        DecimalFormat formatoMes = new DecimalFormat("00");
        Calendar dataFechamento = Calendar.getInstance();
        dataFechamento.setTime(Biblioteca.getDataInicial(periodo));
        int mes = Integer.valueOf(formatoMes.format(dataFechamento.get(Calendar.MONTH) + 1));
        int ano = dataFechamento.get(Calendar.YEAR);



        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "contaCaixa.id", Filtro.IGUAL, idconta));
        filtros.add(new Filtro(Filtro.AND, "mes", Filtro.IGUAL, String.valueOf(mes)));
        filtros.add(new Filtro(Filtro.AND, "ano", Filtro.IGUAL, String.valueOf(ano)));

        fechamentoCaixaBanco = fechamentos.get(FinFechamentoCaixaBanco.class, filtros);
        if (fechamentoCaixaBanco == null) {
            fechamentoCaixaBanco = new FinFechamentoCaixaBanco();
            fechamentoCaixaBanco.setContaCaixa(new ContaCaixa(idconta));
        }

        //busca saldo anterior
        mes -= 1;
        if (mes == 0) {
            mes = 12;
            ano -= 1;
        }
        filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "contaCaixa.id", Filtro.IGUAL, idconta));
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
        filtros.add(new Filtro(Filtro.AND, "viewFinChequeNaoCompensado.idContaCaixa", Filtro.IGUAL, idconta));

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

    @Override
    protected boolean auditar() {
        return false;
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



    public FinFechamentoCaixaBanco getFechamentoCaixaBanco() {
        return fechamentoCaixaBanco;
    }

    public void setFechamentoCaixaBanco(FinFechamentoCaixaBanco fechamentoCaixaBanco) {
        this.fechamentoCaixaBanco = fechamentoCaixaBanco;
    }

    public int getIdconta() {
        return idconta;
    }

    public void setIdconta(int idconta) {
        this.idconta = idconta;
    }

    public Map<String, Integer> getContaCaixa() {
        return contaCaixa;
    }

    public void setContaCaixa(Map<String, Integer> contaCaixa) {
        this.contaCaixa = contaCaixa;
    }
}
