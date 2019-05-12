package com.chronos.util;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class BibliotecaTest {

    @Test
    public void devemos_garantir_que_os_valores_somando_fechem() {

        BigDecimal valor1 = new BigDecimal("2.86267800");
        BigDecimal valor2 = new BigDecimal("0.99360000");
        BigDecimal valor3 = new BigDecimal("0.75036000");
        BigDecimal valor4 = new BigDecimal("0.12420000");
        BigDecimal valor5 = new BigDecimal("0.12");

        BigDecimal valorEsperado = new BigDecimal("4.84");

        BigDecimal total = BigDecimal.ZERO;

        total = Biblioteca.soma(valor1, total);
        total = Biblioteca.soma(valor2, total);
        total = Biblioteca.soma(valor3, total);
        total = Biblioteca.soma(valor4, total);
        total = Biblioteca.soma(valor5, total);

        int result = total.compareTo(valorEsperado);


        assertTrue(result == 0);
    }
}
