package com.chronos.util;

import com.chronos.service.ChronosException;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
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

    @Test(expected = ChronosException.class)
    public void devemos_garantir_que_nao_seje_informado_zero_para_uma_divisao() throws ChronosException {
        BigDecimal valor1 = BigDecimal.TEN;
        BigDecimal valor2 = BigDecimal.ZERO;

        Biblioteca.divide(valor1, valor2);
    }

    @Test
    public void devemos_garaantir_que_seja_calculado_o_percentual_em_cima_de_um_valor_de_desonto() throws ChronosException {

        BigDecimal percentual = Biblioteca.calcularPercentual(BigDecimal.valueOf(100), BigDecimal.valueOf(10));
        BigDecimal percentual2 = Biblioteca.calcularPercentual(BigDecimal.valueOf(155), BigDecimal.valueOf(15.5));
        BigDecimal percentual3 = Biblioteca.calcularPercentual(BigDecimal.valueOf(155), BigDecimal.valueOf(23.25));

        assertEquals(percentual, BigDecimal.valueOf(10).setScale(2));
        assertEquals(percentual2, BigDecimal.valueOf(10).setScale(2));
        assertEquals(percentual3, BigDecimal.valueOf(15).setScale(2));
    }

    @Test
    public void devemos_garantir_que_sejca_calculado_o_valor_do_desconto_em_cima_de_um_percentual() throws ChronosException {
        BigDecimal valor = Biblioteca.calcularValorPercentual(BigDecimal.valueOf(100), BigDecimal.TEN);

        assertEquals(valor, BigDecimal.valueOf(10).setScale(2));
    }


}
