package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
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
public class FinParcelaRecebimentoControll extends AbstractControll<FinParcelaReceber> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Repository<FinParcelaReceber> recebimentos;
    private Repository<FinTipoRecebimento> tipos;
    private Repository<FinChequeRecebido> chequesRecebidos;
    private Repository<ContaCaixa> contas;
    private Repository<Cheque> cheques;
    private Repository<FinStatusParcela> status;
    private Repository<Cliente> clinetes;
    private Repository<AdmParametro> parametros;

    private FinParcelaRecebimento finParcelaRecebimento;
    private FinParcelaRecebimento finParcelaRecebimentoSelecionado;

    private FinChequeRecebido finChequeRecebido;

    private String strTipoBaixa;
    private List<FinParcelaReceber> parcelasSelecionadas;
    private boolean recebimentoCheque;
    private String historico;
    private Cliente cliente;


    @Override
    public void doCreate() {
        super.doCreate();
        if (parcelasSelecionadas != null) {
            if (parcelasSelecionadas.isEmpty()) {
                Mensagem.addWarnMessage("Nenhuma parcela foi selecionada!");

            } else if (parcelasSelecionadas.size() == 1) {
                setObjetoSelecionado(parcelasSelecionadas.get(0));
                super.doCreate();
                novoRecebimento();
            } else if (parcelasSelecionadas.size() > 1) {
                iniciaRecebimentoCheque();
            }
        }
    }

    @Override
    public void salvar() {
        iniciaRecebimento();
    }

    private void novoRecebimento() {
        finParcelaRecebimento = new FinParcelaRecebimento();
        finParcelaRecebimento.setFinParcelaReceber(getObjeto());
        finParcelaRecebimento.setDataRecebimento(new Date());

        cliente = new Cliente();

        strTipoBaixa = "T";
        historico = null;
    }

    public void iniciaRecebimentoCheque() {
        Date dataAtual = new Date();
        BigDecimal totalParcelas = BigDecimal.ZERO;
        finChequeRecebido = new FinChequeRecebido();
        if (parcelasSelecionadas.size() > 1) {
            for (FinParcelaReceber p : parcelasSelecionadas) {
                if (p.getFinStatusParcela().getSituacao().equals("02")) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já quitada.");

                    return;
                }
                if (p.getDataVencimento().before(dataAtual)) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já vencida.");

                    return;
                }
                if (p.getValor() != null) {
                    totalParcelas = totalParcelas.add(p.getValor());
                }
            }
            finChequeRecebido.setValor(totalParcelas);
        }
        recebimentoCheque = true;
        RequestContext.getCurrentInstance().update("formOutrasTelas:dialogFinChequeRecebido");
    }

    public void cancelaRecebimentoCheque() {
        recebimentoCheque = false;
        cliente = new Cliente();
    }

    public void finalizaRecebimentoCheque() {
        recebimentoCheque = false;
        incluirRecebimento();
    }

    public void iniciaRecebimento() {
        if (finParcelaRecebimento.getFinTipoRecebimento().getTipo().equals("02")) {
            iniciaRecebimentoCheque();
        } else {
            incluirRecebimento();
        }
    }

    private void incluirRecebimento() {
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

            if (parcelasSelecionadas.size() == 1 && !finParcelaRecebimento.getFinTipoRecebimento().getTipo().equals("02")) {
                calculaTotalRecebido();

                FinParcelaRecebimento recebimento = new FinParcelaRecebimento();
                recebimento.setFinParcelaReceber(finParcelaRecebimento.getFinParcelaReceber());
                recebimento.setContaCaixa(finParcelaRecebimento.getContaCaixa());
                recebimento.setDataRecebimento(finParcelaRecebimento.getDataRecebimento());
                recebimento.setFinTipoRecebimento(finParcelaRecebimento.getFinTipoRecebimento());
                recebimento.setHistorico(finParcelaRecebimento.getHistorico());
                recebimento.setTaxaDesconto(finParcelaRecebimento.getTaxaDesconto());
                recebimento.setTaxaJuro(finParcelaRecebimento.getTaxaJuro());
                recebimento.setTaxaMulta(finParcelaRecebimento.getTaxaMulta());
                recebimento.setValorDesconto(finParcelaRecebimento.getValorDesconto());
                recebimento.setValorJuro(finParcelaRecebimento.getValorJuro());
                recebimento.setValorMulta(finParcelaRecebimento.getValorMulta());
                recebimento.setValorRecebido(finParcelaRecebimento.getValorRecebido());

                if (strTipoBaixa.equals("P")) {
                    statusParcela = status.get(admParametro.getFinParcelaQuitadoParcial(), FinStatusParcela.class);
                    if (statusParcela == null) {
                        throw new Exception("O status de parcela 'Quitado Parcial' não está cadastrado.\nEntre em contato com a Software House.");
                    }
                }

                getObjeto().setFinStatusParcela(statusParcela);
                getObjeto().getListaFinParcelaRecebimento().add(recebimento);
                salvar("Recebimento incluído com sucesso!");
                novoRecebimento();
            } else {
                FinTipoRecebimento tipoRecebimento = tipos.get(FinTipoRecebimento.class, "tipo", "02");
                if (tipoRecebimento == null) {
                    throw new Exception("Tipo de recebimento 'CHEQUE' não está cadastrado.\nEntre em contato com a Software House.");
                }

                chequesRecebidos.salvar(finChequeRecebido);

                for (FinParcelaReceber p : parcelasSelecionadas) {
                    FinParcelaRecebimento recebimento = new FinParcelaRecebimento();
                    recebimento.setFinTipoRecebimento(tipoRecebimento);
                    recebimento.setFinParcelaReceber(p);
                    recebimento.setFinChequeRecebido(finChequeRecebido);
                    recebimento.setContaCaixa(finChequeRecebido.getContaCaixa());
                    recebimento.setDataRecebimento(finChequeRecebido.getBomPara());
                    recebimento.setHistorico(historico);
                    recebimento.setValorRecebido(p.getValor());

                    p.setFinStatusParcela(statusParcela);
                    p.getListaFinParcelaRecebimento().add(recebimento);

                    p = dao.atualizar(p);
                }

                if (parcelasSelecionadas.size() == 1) {
                    setObjeto(dao.getEntityJoinFetch(getObjeto().getId(), FinParcelaReceber.class));
                    novoRecebimento();
                }
                Mensagem.addInfoMessage("Recebimento efetuado com sucesso!");

            }
        } catch (Exception e) {
            Mensagem.addErrorMessage("Erro ao incluir o pagamento!", e);

        }
    }

    public void calculaTotalRecebido() throws Exception {
        BigDecimal valorJuro = BigDecimal.ZERO;
        BigDecimal valorMulta = BigDecimal.ZERO;
        BigDecimal valorDesconto = BigDecimal.ZERO;
        if (finParcelaRecebimento.getTaxaJuro() != null && finParcelaRecebimento.getDataRecebimento() != null) {
            Calendar dataRecebimento = Calendar.getInstance();
            dataRecebimento.setTime(finParcelaRecebimento.getDataRecebimento());
            Calendar dataVencimento = Calendar.getInstance();
            dataVencimento.setTime(finParcelaRecebimento.getFinParcelaReceber().getDataVencimento());
            if (dataVencimento.before(dataRecebimento)) {
                long diasAtraso = (dataRecebimento.getTimeInMillis() - dataVencimento.getTimeInMillis()) / 86400000l;
                //valorJuro = valor * ((taxaJuro / 30) / 100) * diasAtraso
                finParcelaRecebimento.setValorJuro(finParcelaRecebimento.getFinParcelaReceber().getValor().multiply(finParcelaRecebimento.getTaxaJuro().divide(BigDecimal.valueOf(30), RoundingMode.HALF_DOWN)).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(diasAtraso)));
                valorJuro = finParcelaRecebimento.getValorJuro();
            }
        }
        finParcelaRecebimento.setValorJuro(valorJuro);

        if (finParcelaRecebimento.getTaxaMulta() != null) {
            finParcelaRecebimento.setValorMulta(finParcelaRecebimento.getFinParcelaReceber().getValor().multiply(finParcelaRecebimento.getTaxaMulta()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
            valorMulta = finParcelaRecebimento.getValorMulta();
        } else {
            finParcelaRecebimento.setValorMulta(valorMulta);
        }

        if (finParcelaRecebimento.getTaxaDesconto() != null) {
            finParcelaRecebimento.setValorDesconto(finParcelaRecebimento.getFinParcelaReceber().getValor().multiply(finParcelaRecebimento.getTaxaDesconto()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
            valorDesconto = finParcelaRecebimento.getValorDesconto();
        } else {
            finParcelaRecebimento.setValorDesconto(valorDesconto);
        }

        finParcelaRecebimento.setValorRecebido(finParcelaRecebimento.getFinParcelaReceber().getValor().add(valorJuro).add(valorMulta).subtract(valorDesconto));
    }

    public void excluirFinParcelaRecebimento() {

        getObjeto().getListaFinParcelaRecebimento().remove(finParcelaRecebimentoSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    public void onSelecionaPessoa(SelectEvent event) {
        finChequeRecebido.setNome(finChequeRecebido.getPessoa().getNome());
        if (finChequeRecebido.getPessoa().getPessoaFisica() != null) {
            finChequeRecebido.setCpfCnpj(finChequeRecebido.getPessoa().getPessoaFisica().getCpf());
        }
        if (finChequeRecebido.getPessoa().getPessoaJuridica() != null) {
            finChequeRecebido.setCpfCnpj(finChequeRecebido.getPessoa().getPessoaJuridica().getCnpj());
        }
    }

    public List<FinParcelaReceber> getListaFinParcelaReceber(String nome) {
        List<FinParcelaReceber> listaFinParcelaReceber = new ArrayList<>();
        try {
            listaFinParcelaReceber = recebimentos.getEntitys(FinParcelaReceber.class,"nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinParcelaReceber;
    }

    public List<FinTipoRecebimento> getListaFinTipoRecebimento(String nome) {
        List<FinTipoRecebimento> listaFinTipoRecebimento = new ArrayList<>();
        try {
            listaFinTipoRecebimento = tipos.getEntitys(FinTipoRecebimento.class,"descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinTipoRecebimento;
    }

    public List<FinChequeRecebido> getListaFinChequeRecebido(String nome) {
        List<FinChequeRecebido> listaFinChequeRecebido = new ArrayList<>();
        try {
            listaFinChequeRecebido = chequesRecebidos.getEntitys(FinChequeRecebido.class,"nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinChequeRecebido;
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

    public List<Pessoa> getListaCliente(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<Cliente> listaCliente = clinetes.getEntitys(Cliente.class,"pessoa.nome", nome);
            for (Cliente c : listaCliente) {
                listaPessoa.add(c.getPessoa());
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPessoa;
    }


    @Override
    protected Class<FinParcelaReceber> getClazz() {
        return FinParcelaReceber.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "FIN_PARCELA_RECEBIMENTO";
    }

    public FinParcelaRecebimento getFinParcelaRecebimento() {
        return finParcelaRecebimento;
    }

    public void setFinParcelaRecebimento(FinParcelaRecebimento finParcelaRecebimento) {
        this.finParcelaRecebimento = finParcelaRecebimento;
    }

    public FinParcelaRecebimento getFinParcelaRecebimentoSelecionado() {
        return finParcelaRecebimentoSelecionado;
    }

    public void setFinParcelaRecebimentoSelecionado(FinParcelaRecebimento finParcelaRecebimentoSelecionado) {
        this.finParcelaRecebimentoSelecionado = finParcelaRecebimentoSelecionado;
    }

    public FinChequeRecebido getFinChequeRecebido() {
        return finChequeRecebido;
    }

    public void setFinChequeRecebido(FinChequeRecebido finChequeRecebido) {
        this.finChequeRecebido = finChequeRecebido;
    }

    public String getStrTipoBaixa() {
        return strTipoBaixa;
    }

    public void setStrTipoBaixa(String strTipoBaixa) {
        this.strTipoBaixa = strTipoBaixa;
    }

    public List<FinParcelaReceber> getParcelasSelecionadas() {
        return parcelasSelecionadas;
    }

    public void setParcelasSelecionadas(List<FinParcelaReceber> parcelasSelecionadas) {
        this.parcelasSelecionadas = parcelasSelecionadas;
    }

    public boolean isRecebimentoCheque() {
        return recebimentoCheque;
    }

    public void setRecebimentoCheque(boolean recebimentoCheque) {
        this.recebimentoCheque = recebimentoCheque;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
