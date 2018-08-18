package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FinLancamentoReceberCartaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OperadoraCartaoService operadoraCartaoService;

    @Inject
    private Repository<FinLancamentoReceberCartao> lancamentoReceberCartaoRepository;



    public FinLancamentoReceberCartao gerarLancamento(FinLancamentoReceberCartao lancamento, OperadoraCartaoTaxa operadoraCartaoTaxa) {


        int number = 1;
        int qtdParcelas = lancamento.getQuantidadeParcela();
        int intervalo = lancamento.getIntervaloEntreParcelas();
        OperadoraCartao operadoraCartao = lancamento.getOperadoraCartao();
        BigDecimal valor = lancamento.getValorBruto();
        BigDecimal valorParcela = Biblioteca.divide(valor, BigDecimal.valueOf(qtdParcelas));
        BigDecimal valorEcargos = BigDecimal.ZERO;
        BigDecimal sumValorLiquidoParcelas = BigDecimal.ZERO;
        Calendar primeiroVencimento = Calendar.getInstance();
        primeiroVencimento.setTime(lancamento.getPrimeiroVencimento());


        BigDecimal taxa = operadoraCartaoTaxa.getTaxaAdm();
        BigDecimal valorEcargosParcela = Biblioteca.calcularValorPercentual(valorParcela, taxa);
        BigDecimal valorLiquido = Biblioteca.subtrai(valorParcela, valorEcargosParcela);
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal residuo;
        FinParcelaReceberCartao parcelaReceber;

        lancamento.setListaFinParcelaReceberCartao(new ArrayList<>());
        for (int i = 0; i < qtdParcelas; i++) {


            valorEcargos = Biblioteca.soma(valorEcargos, valorEcargosParcela);


            parcelaReceber = new FinParcelaReceberCartao();
            parcelaReceber.setFinLancamentoReceberCartao(lancamento);
            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setPago(false);
            parcelaReceber.setContaCaixa(operadoraCartao.getContaCaixa());
            parcelaReceber.setDataEmissao(lancamento.getDataLancamento());

            if (i > 0) {
                primeiroVencimento.add(Calendar.DAY_OF_MONTH, intervalo);
            }

            parcelaReceber.setDataVencimento(Biblioteca.addDay(primeiroVencimento.getTime(), intervalo));

            parcelaReceber.setValorBruto(valorParcela);
            parcelaReceber.setTaxaAplicada(taxa);

            parcelaReceber.setValorEncargos(valorEcargosParcela);
            parcelaReceber.setValorLiquido(valorLiquido);


            somaParcelas = somaParcelas.add(valorParcela);

            if (i == qtdParcelas - 1) {
                residuo = valor.subtract(somaParcelas);
                valorParcela = valorParcela.add(residuo);
                parcelaReceber.setValorBruto(valorParcela);
                valorEcargosParcela = Biblioteca.calcularValorPercentual(valorParcela, taxa);
                valorLiquido = valorParcela.subtract(valorEcargosParcela);
                parcelaReceber.setValorLiquido(valorLiquido);
            }

            sumValorLiquidoParcelas = Biblioteca.soma(sumValorLiquidoParcelas, valorLiquido);
            lancamento.getListaFinParcelaReceberCartao().add(parcelaReceber);
        }

        lancamento.setValorEncargos(valorEcargos);
        lancamento.setValorLiquido(sumValorLiquidoParcelas);
        lancamento.setTaxaAplicada(taxa);
        return lancamento;
    }

    public FinLancamentoReceberCartao gerarLancamento(int id, BigDecimal valor, OperadoraCartao operadoraCartao, OperadoraCartaoTaxa operadoraCartaoTaxa, int qtdParcelas, String codModulo, Empresa empresa, String identificador) {

        String numDoc = "E" + empresa.getId()
                + "M" + codModulo
                + "V" + id
                + "O" + operadoraCartao.getId()
                + "Q" + qtdParcelas;

        if (!StringUtils.isEmpty(identificador)) {
            numDoc += "NSU" + identificador;
        }


        FinLancamentoReceberCartao lancamento = new FinLancamentoReceberCartao();


        int intervalo = operadoraCartaoTaxa.getCreditoEm();

        lancamento.setValorBruto(valor);


        lancamento.setDataLancamento(lancamento.getDataLancamento());
        lancamento.setNumeroDocumento(numDoc);
        lancamento.setCodigoModuloLcto(codModulo);
        lancamento.setEmpresa(empresa);
        lancamento.setOperadoraCartao(operadoraCartao);

        // pega o primeiro vencimento
        lancamento.setPrimeiroVencimento(new Date());
        lancamento.setDataLancamento(new Date());
        lancamento.setIntervaloEntreParcelas(intervalo);
        lancamento.setQuantidadeParcela(qtdParcelas);

        lancamento = gerarLancamento(lancamento, operadoraCartaoTaxa);

        return lancamento;

    }


}
