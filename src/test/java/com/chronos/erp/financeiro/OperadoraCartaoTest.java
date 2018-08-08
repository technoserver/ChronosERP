/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.financeiro;

import com.chronos.modelo.entidades.OperadoraCartaoTaxa;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.OperadoraCartaoService;
import org.junit.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
            service.validarIntevalo(taxas, taxa);
        } catch (ChronosException e) {
            valido = false;
        }
        assertFalse(valido);
    }

    @Test
    public void intervaloFinalExistente() {
        boolean valido = true;
        taxas.add(new OperadoraCartaoTaxa(2, 6, BigDecimal.ZERO));
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

}
