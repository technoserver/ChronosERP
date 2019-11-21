package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.FormaPagamento;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FormaPagamentoServiceTest {

    private FormaPagamentoService service;

    @Before
    public void setUp() {

        service = new FormaPagamentoService();
    }

    @Test
    public void teste() {
        FormaPagamento forma = new FormaPagamento();
        forma.setValor(BigDecimal.ONE);

        List<FormaPagamento> pagamentos = new ArrayList<>();

        pagamentos.add(forma);

        BigDecimal totalReceber = BigDecimal.ZERO;
        BigDecimal saldoRestante = BigDecimal.ZERO;

        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal valorPago = BigDecimal.ZERO;


    }
}
