package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.FinParcelaReceberCartao;
import com.chronos.repository.Filtro;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.FinRecebimentoCartaoService;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class FinRecebimentoCartaoControll extends AbstractControll<FinParcelaReceberCartao> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private FinRecebimentoCartaoService service;

    private List<FinParcelaReceberCartao> parcelas;

    private List<FinParcelaReceberCartao> parcelasSelecionada;

    private BigDecimal valorTotalBruto;
    private BigDecimal valorTotalLiquido;
    private BigDecimal valorTotalEncargos;

    private BigDecimal valorTotalBrutoVencido;
    private BigDecimal valorTotalLiquidoVencido;
    private BigDecimal valorTotalEncargosVencido;


    private int qtdVencida;
    private int qtdAVencer;

    private Date dataInicial;
    private Date dataFinal;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        zerarValores();

    }

    @Override
    public ERPLazyDataModel<FinParcelaReceberCartao> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(FinParcelaReceberCartao.class);
        }

        Object atribustos[] = {"numeroParcela", "dataEmissao", "dataVencimento", "dataRecebimento", "valorBruto", "taxaAplicada", "valorEncargos", "valorLiquido", "pago", "contaCaixa.id,inLancamentoReceberCartao.id"};
        dataModel.setAtributos(atributos);
        return dataModel;
    }

    public void pesquisar() {
        try {
            zerarValores();
            if (dataInicial == null) {
                dataInicial = new Date();
            }

            if (dataFinal == null) {
                dataFinal = new Date();
            }
            Object atributos[] = {"numeroParcela", "dataEmissao", "dataVencimento", "dataRecebimento", "valorBruto", "taxaAplicada", "valorEncargos", "valorLiquido", "pago", "contaCaixa.id,finLancamentoReceberCartao.id"};
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pago", false));
            filtros.add(new Filtro("dataVencimento", Filtro.MAIOR_OU_IGUAL, dataInicial));
            filtros.add(new Filtro("dataVencimento", Filtro.MENOR_OU_IGUAL, dataFinal));
            parcelas = dao.getEntitys(FinParcelaReceberCartao.class, filtros, atributos);
            calcular();

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao buscar parcelas", ex);
        }
    }


    public void receber() {

        try {
            service.receber(parcelasSelecionada);
            pesquisar();
            Mensagem.addInfoMessage("Baixa realizada com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao realizar a baixa", ex);
            } else {
                throw new RuntimeException("Erro ao realizar a baixa", ex);
            }
        }
    }


    private void calcular() {


        for (FinParcelaReceberCartao p : parcelas) {
            valorTotalBrutoVencido = valorTotalBrutoVencido.add(p.isVencido() ? p.getValorBruto() : BigDecimal.ZERO);
            valorTotalBruto = valorTotalBruto.add(!p.isVencido() ? p.getValorBruto() : BigDecimal.ZERO);

            valorTotalEncargosVencido = valorTotalEncargosVencido.add(p.isVencido() ? p.getValorEncargos() : BigDecimal.ZERO);
            valorTotalEncargos = valorTotalEncargos.add(!p.isVencido() ? p.getValorEncargos() : BigDecimal.ZERO);

            valorTotalLiquidoVencido = valorTotalLiquidoVencido.add(p.isVencido() ? p.getValorLiquido() : BigDecimal.ZERO);
            valorTotalLiquido = valorTotalLiquido.add(!p.isVencido() ? p.getValorLiquido() : BigDecimal.ZERO);

            qtdVencida = p.isVencido() ? qtdVencida + 1 : qtdVencida;
            qtdAVencer = p.isVencido() ? qtdAVencer : qtdAVencer + 1;
        }

    }

    private void zerarValores() {
        valorTotalBrutoVencido = BigDecimal.ZERO;
        valorTotalBruto = BigDecimal.ZERO;
        valorTotalLiquidoVencido = BigDecimal.ZERO;
        valorTotalLiquido = BigDecimal.ZERO;
        valorTotalEncargos = BigDecimal.ZERO;
        valorTotalEncargosVencido = BigDecimal.ZERO;
        parcelas = new ArrayList<>();
        qtdAVencer = 0;
        qtdVencida = 0;
    }


    @Override
    protected Class<FinParcelaReceberCartao> getClazz() {
        return FinParcelaReceberCartao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "RECEBIMENTO_CARTAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public List<FinParcelaReceberCartao> getParcelas() {
        return parcelas;
    }

    public List<FinParcelaReceberCartao> getParcelasSelecionada() {
        return parcelasSelecionada;
    }

    public void setParcelasSelecionada(List<FinParcelaReceberCartao> parcelasSelecionada) {
        this.parcelasSelecionada = parcelasSelecionada;
    }

    public BigDecimal getValorTotalBruto() {
        return valorTotalBruto;
    }

    public BigDecimal getValorTotalLiquido() {
        return valorTotalLiquido;
    }

    public BigDecimal getValorTotalEncargos() {
        return valorTotalEncargos;
    }

    public BigDecimal getValorTotalBrutoVencido() {
        return valorTotalBrutoVencido;
    }

    public BigDecimal getValorTotalLiquidoVencido() {
        return valorTotalLiquidoVencido;
    }

    public BigDecimal getValorTotalEncargosVencido() {
        return valorTotalEncargosVencido;
    }

    public int getQtdVencida() {
        return qtdVencida;
    }

    public int getQtdAVencer() {
        return qtdAVencer;
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
}
