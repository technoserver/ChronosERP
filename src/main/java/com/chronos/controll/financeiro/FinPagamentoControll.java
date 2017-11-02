package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewFinLancamentoPagar;
import com.chronos.repository.Filtro;
import com.chronos.repository.ParcelaPagarRepository;
import com.chronos.repository.Repository;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.context.RequestContext;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 01/11/17.
 */
@Named
@ViewScoped
public class FinPagamentoControll extends AbstractControll<FinParcelaPagar> implements Serializable {

    @Inject
    private Repository<ViewFinLancamentoPagar> repository;
    @Inject
    private ParcelaPagarRepository parcelaPagarRepository;
    @Inject
    private Repository<FinTipoPagamento> finTipoPagamentoRepository;
    @Inject
    private Repository<FinParcelaPagamento> parcelaPagamentoRepository;
    @Inject
    private Repository<Cheque> cheques;
    @Inject
    private Repository<FinChequeEmitido> chequesEmitidos;

    private List<ViewFinLancamentoPagar> parcelas;

    private List<ViewFinLancamentoPagar> parcelasSelecionada;
    private FinTipoPagamento tipoPagamento;
    private FinChequeEmitido finChequeEmitido;
    private String fornecedor;
    private String cnpjCpf;
    private Date dataInicial;
    private Date dataFinal;
    private String numDocumento;
    private boolean pagamentoCheque;

    private int qtdParcelasVencida;
    private int qtdParcelasAvencer;
    private BigDecimal totalVencidas;
    private BigDecimal totalAVencer;


    private String observacao;

    public void buscarParcelas() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("idStatusParcela", Filtro.DIFERENTE, 2));
        if (dataInicial != null) {
            filtros.add(new Filtro("dataVencimento", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }
        if (dataFinal != null) {
            filtros.add(new Filtro("dataVencimento", Filtro.MENOR_OU_IGUAL, dataFinal));
        }
        filtros.add(new Filtro("numeroDocumento", Filtro.LIKE, Optional.ofNullable(numDocumento).orElse("")));
        filtros.add(new Filtro("cnpjCpf", Filtro.LIKE, Optional.ofNullable(cnpjCpf).orElse("")));
        filtros.add(new Filtro("nomeFornecedor", Filtro.LIKE, Optional.ofNullable(fornecedor).orElse("")));
        parcelas = repository.getEntitys(ViewFinLancamentoPagar.class, filtros);
        totalVencidas = BigDecimal.ZERO;
        totalAVencer = BigDecimal.ZERO;
        qtdParcelasVencida = 0;
        qtdParcelasAvencer = 0;
        parcelas.stream().forEach(p -> {

            if (p.isVencido()) {
                totalVencidas = totalVencidas.add(p.getValorParcela());
                qtdParcelasVencida++;
                if (p.getIdStatusParcela() != 4) {
                    parcelaPagarRepository.atualizarStatusParcela(p.getIdParcelaPagar(), 4);
                    p.setDescricaoSituacaoParcela("Vencido");
                }
            } else {
                totalAVencer = totalAVencer.add(p.getValorParcela());
                qtdParcelasAvencer++;
            }
        });

    }

    @Transactional
    public void baixarParcelas() {
        if (parcelasSelecionada == null || parcelasSelecionada.isEmpty()) {
            Mensagem.addInfoMessage("É preciso seleionar ao menos 1 parcela");
        } else {

            if (tipoPagamento.getTipo().equals("02") && !pagamentoCheque) {
                iniciaPagamentoCheque();
            } else {

                if (finChequeEmitido != null && pagamentoCheque) {
                    chequesEmitidos.salvar(finChequeEmitido);
                    finChequeEmitido.getCheque().setStatusCheque("U");
                    cheques.atualizar(finChequeEmitido.getCheque());
                }
                for (ViewFinLancamentoPagar p : parcelasSelecionada) {
                    FinParcelaPagamento pagamento = new FinParcelaPagamento();
                    pagamento.setFinParcelaPagar(new FinParcelaPagar(p.getIdParcelaPagar()));

                    pagamento.setDataPagamento(new Date());
                    pagamento.setFinTipoPagamento(tipoPagamento);
                    pagamento.setHistorico(observacao);
                    pagamento.setTaxaDesconto(BigDecimal.ZERO);
                    pagamento.setTaxaJuro(BigDecimal.ZERO);
                    pagamento.setTaxaMulta(BigDecimal.ZERO);
                    pagamento.setValorDesconto(BigDecimal.ZERO);
                    pagamento.setValorJuro(BigDecimal.ZERO);
                    pagamento.setValorMulta(BigDecimal.ZERO);
                    pagamento.setValorPago(p.getValorParcela());
                    if (finChequeEmitido != null && pagamentoCheque) {
                        pagamento.setFinChequeEmitido(finChequeEmitido);
                        pagamento.setContaCaixa(finChequeEmitido.getCheque().getTalonarioCheque().getContaCaixa());
                        pagamento.setDataPagamento(finChequeEmitido.getBomPara());
                    } else {
                        pagamento.setContaCaixa(new ContaCaixa(p.getIdContaCaixa()));
                    }

                    parcelaPagamentoRepository.salvar(pagamento);
                    parcelaPagarRepository.atualizarStatusParcela(p.getIdParcelaPagar(), 2);


                }

                buscarParcelas();
            }
        }
    }

    public void iniciaPagamentoCheque() {
        Date dataAtual = new Date();
        BigDecimal totalParcelas = BigDecimal.ZERO;
        finChequeEmitido = new FinChequeEmitido();
        if (parcelasSelecionada.size() > 1) {
            for (ViewFinLancamentoPagar p : parcelasSelecionada) {
                if (p.getDescricaoSituacaoParcela().equals("02")) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já quitada.");

                    return;
                }
                if (p.getDataVencimento().before(dataAtual)) {
                    Mensagem.addWarnMessage("Foi selecionado parcela já vencida.");

                    return;
                }
//                if (p.getSofreRetencao() != null && p.getSofreRetencao().equals("S")) {
//                    Mensagem.addWarnMessage("Procedimento não permitido. Fornecedor sofre retenção.");
//
//                    return;
//                }
                totalParcelas = totalParcelas.add(Optional.ofNullable(p.getValorParcela()).orElse(BigDecimal.ZERO));

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
        baixarParcelas();
        pagamentoCheque = false;

    }

    public List<FinTipoPagamento> getListaFinTipoRecebimento(String nome) {
        List<FinTipoPagamento> listaFinTipoPagamento = new ArrayList<>();
        try {
            listaFinTipoPagamento = finTipoPagamentoRepository.getEntitys(FinTipoPagamento.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFinTipoPagamento;
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

    public String formatarValor(BigDecimal valor) {
        return FormatValor.getInstance().formatoDecimal("V", valor.doubleValue());
    }

    @Override
    protected Class<FinParcelaPagar> getClazz() {
        return null;
    }

    @Override
    protected String getFuncaoBase() {
        return null;
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getCnpjCpf() {
        return cnpjCpf;
    }

    public void setCnpjCpf(String cnpjCpf) {
        this.cnpjCpf = cnpjCpf;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public List<ViewFinLancamentoPagar> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<ViewFinLancamentoPagar> parcelas) {
        this.parcelas = parcelas;
    }

    public List<ViewFinLancamentoPagar> getParcelasSelecionada() {
        return Optional.ofNullable(parcelasSelecionada).orElse(new ArrayList<>());
    }

    public void setParcelasSelecionada(List<ViewFinLancamentoPagar> parcelasSelecionada) {
        this.parcelasSelecionada = parcelasSelecionada;
    }

    public int getQtdParcelasVencida() {
        return qtdParcelasVencida;
    }

    public void setQtdParcelasVencida(int qtdParcelasVencida) {
        this.qtdParcelasVencida = qtdParcelasVencida;
    }

    public int getQtdParcelasAvencer() {
        return qtdParcelasAvencer;
    }

    public void setQtdParcelasAvencer(int qtdParcelasAvencer) {
        this.qtdParcelasAvencer = qtdParcelasAvencer;
    }

    public BigDecimal getTotalVencidas() {
        return Optional.ofNullable(totalVencidas).orElse(BigDecimal.ZERO);
    }

    public void setTotalVencidas(BigDecimal totalVencidas) {
        this.totalVencidas = totalVencidas;
    }

    public BigDecimal getTotalAVencer() {
        return Optional.ofNullable(totalAVencer).orElse(BigDecimal.ZERO);
    }

    public void setTotalAVencer(BigDecimal totalAVencer) {
        this.totalAVencer = totalAVencer;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public FinTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(FinTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
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

    public BigDecimal getSaldoDevedor() {
        totalAVencer = Optional.ofNullable(totalAVencer).orElse(BigDecimal.ZERO);
        return totalAVencer.add(getTotalVencidas());
    }

    public BigDecimal getValorAPagar() {
        BigDecimal valor = getParcelasSelecionada().stream()
                .map(ViewFinLancamentoPagar::getValorParcela)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return valor;
    }


}
