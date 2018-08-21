package com.chronos.service.financeiro;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class FinLancamentoReceberCartaoServiceTest {

    private OperadoraCartao operadoraCartao;
    private FinLancamentoReceberCartao lancamento;
    private List<OperadoraCartaoTaxa> taxas;
    private OperadoraCartaoTaxa taxa;
    private Empresa empresa;


    private FinLancamentoReceberCartaoService service;
    private OperadoraCartaoService operadoraCartaoService;


    @Before
    public void setUp() {
        taxas = new ArrayList<>();
        taxas.add(new OperadoraCartaoTaxa());
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.valueOf(4.5)));
        taxas.add(new OperadoraCartaoTaxa(7, 10, BigDecimal.valueOf(5)));
        taxa = new OperadoraCartaoTaxa();
        operadoraCartao = new OperadoraCartao(1);
        operadoraCartao.setListaOperadoraCartaoTaxas(new HashSet<>(taxas));
        operadoraCartao.setContaCaixa(new ContaCaixa());
        lancamento = new FinLancamentoReceberCartao();
        service = new FinLancamentoReceberCartaoService();
        operadoraCartaoService = new OperadoraCartaoService();
        empresa = new Empresa(1);
    }

    @Test
    public void gerarLancamento10x() {
        lancamento.setQuantidadeParcela(10);
        lancamento.setIntervaloEntreParcelas(30);
        lancamento.setValorBruto(BigDecimal.valueOf(100));
        lancamento.setPrimeiroVencimento(new Date());
        lancamento.setOperadoraCartao(operadoraCartao);
        OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(taxas, 10);

        lancamento = service.gerarLancamento(lancamento, operadoraCartaoTaxa);

        assertEquals(lancamento.getValorLiquido(), BigDecimal.valueOf(95.00).setScale(2));
        assertEquals(lancamento.getValorEncargos(), BigDecimal.valueOf(5).setScale(2));
        assertEquals(lancamento.getTaxaAplicada(), BigDecimal.valueOf(5));
    }

    @Test
    public void gerarLancamento6x() {
        lancamento.setQuantidadeParcela(6);
        lancamento.setIntervaloEntreParcelas(30);
        lancamento.setValorBruto(BigDecimal.valueOf(100));
        lancamento.setPrimeiroVencimento(new Date());
        lancamento.setOperadoraCartao(operadoraCartao);
        OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(taxas, 6);

        lancamento = service.gerarLancamento(lancamento, operadoraCartaoTaxa);
        BigDecimal total = lancamento.getValorLiquido().add(lancamento.getValorEncargos());
        assertEquals(lancamento.getValorLiquido(), BigDecimal.valueOf(95.50).setScale(2));
        assertEquals(lancamento.getValorEncargos(), BigDecimal.valueOf(4.5).setScale(2));
        assertEquals(lancamento.getTaxaAplicada(), BigDecimal.valueOf(4.50));
        assertEquals(total, lancamento.getValorBruto().setScale(2));
    }

    @Test
    public void gerarLancamentoPdv() {
        OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(taxas, 6);
        lancamento = service.gerarLancamento(1, BigDecimal.valueOf(100), operadoraCartao, operadoraCartaoTaxa, 6, Modulo.VENDA.getCodigo(), empresa, "1020");
        BigDecimal total = lancamento.getValorLiquido().add(lancamento.getValorEncargos());
        String numDoc = "E1M210V1O1Q6NSU1020";
        assertEquals(lancamento.getValorLiquido(), BigDecimal.valueOf(95.50).setScale(2));
        assertEquals(lancamento.getValorEncargos(), BigDecimal.valueOf(4.5).setScale(2));
        assertEquals(lancamento.getTaxaAplicada(), BigDecimal.valueOf(4.50));
        assertEquals(total, lancamento.getValorBruto().setScale(2));
        assertEquals(numDoc, lancamento.getNumeroDocumento());
    }

}