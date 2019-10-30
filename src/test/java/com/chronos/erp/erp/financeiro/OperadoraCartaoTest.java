/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.erp.financeiro;

import com.chronos.erp.modelo.entidades.OperadoraCartao;
import com.chronos.erp.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.OperadoraCartaoService;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author john
 */
public class OperadoraCartaoTest {
    private List<OperadoraCartaoTaxa> taxas;
    private OperadoraCartaoTaxa taxa;
    private OperadoraCartaoService service;

    public OperadoraCartaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        taxas = new ArrayList<>();
        taxas.add(new OperadoraCartaoTaxa());
        taxa = new OperadoraCartaoTaxa();
        service = new OperadoraCartaoService();
    }

    @After
    public void tearDown() {
    }


    @Test
    public void intervaloInicialMaior() {
        boolean valido = true;
        taxa.setIntervaloInicial(2);
        taxa.setIntervaloFinal(1);
        try {
            service.validarIntevalo(taxas, taxa);
        } catch (ChronosException e) {
            valido = false;
        }
        assertFalse(valido);
    }

    @Test
    public void intervaloInicialExistente() {
        boolean valido = true;
        try {
            taxa.setTaxaAdm(BigDecimal.ONE);
            service.validarIntevalo(taxas, taxa);
        } catch (ChronosException e) {
            valido = false;
        }
        assertFalse(valido);
    }

    @Test
    public void intervaloFinalExistente() {
        boolean valido = true;
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.ONE));
        taxa.setIntervaloInicial(3);
        taxa.setIntervaloFinal(6);
        try {
            service.validarIntevalo(taxas, taxa);
        } catch (ChronosException e) {
            valido = false;
        }
        assertFalse(valido);
    }

    @Test
    public void conflitoIntervalo() {
        boolean valido = true;
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.ZERO));
        taxa.setIntervaloInicial(3);
        taxa.setIntervaloFinal(5);
        try {
            service.validarIntevalo(taxas, taxa);
        } catch (ChronosException e) {
            valido = false;
        }
        assertFalse(valido);
    }

    @Test
    public void addnovoIntervalo() {
        boolean valido = true;
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.ZERO));


        taxa = service.addTaxa(taxas);

        assertEquals(taxa.getIntervaloInicial(), Integer.valueOf(7));
        assertEquals(taxa.getIntervaloFinal(), Integer.valueOf(8));
    }

    @Test
    public void qtdMaximaParcelasListaVazia() {

        OperadoraCartao operadoraCartao = new OperadoraCartao();
        int qtd = service.quantidadeMaximaParcelas(operadoraCartao);

        assertEquals(qtd, 0);

    }

    @Test
    public void qtdMaximaParcelas6() {

        OperadoraCartao operadoraCartao = new OperadoraCartao();
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.ZERO));
        operadoraCartao.setListaOperadoraCartaoTaxas(new HashSet<>(taxas));
        int qtd = service.quantidadeMaximaParcelas(operadoraCartao);

        assertEquals(qtd, 6);

    }

    @Test
    public void getTaxaIdeal4Parcelas() {

        OperadoraCartao operadoraCartao = new OperadoraCartao();
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.valueOf(4.0)));
        taxas.add(new OperadoraCartaoTaxa(7, 10, BigDecimal.valueOf(5.0)));
        taxas.add(new OperadoraCartaoTaxa(11, 12, BigDecimal.valueOf(5.5)));
        BigDecimal taxa = service.getTaxa(taxas, 4);


        assertEquals(taxa, BigDecimal.valueOf(4.0));

    }

    @Test
    public void getTaxaIdeal7Parcelas() {

        OperadoraCartao operadoraCartao = new OperadoraCartao();
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.valueOf(4.0)));
        taxas.add(new OperadoraCartaoTaxa(7, 10, BigDecimal.valueOf(5.0)));
        taxas.add(new OperadoraCartaoTaxa(11, 12, BigDecimal.valueOf(5.5)));
        BigDecimal taxa = service.getTaxa(taxas, 8);


        assertEquals(taxa, BigDecimal.valueOf(5.0));

    }


}
