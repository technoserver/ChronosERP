package com.chronos.erp.service.comercial;


import com.chronos.erp.modelo.entidades.FormaPagamento;
import com.chronos.erp.util.Biblioteca;

import java.math.BigDecimal;
import java.util.List;

public class FormaPagamentoService {

    private BigDecimal valorPago;
    private BigDecimal saldoRestante;
    private BigDecimal totalRecebido;
    private BigDecimal totalReceber;
    private BigDecimal troco;

    private void verificaSaldoRestante(List<FormaPagamento> pagamentos) {

        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        for (FormaPagamento p : pagamentos) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        saldoRestante = Biblioteca.subtrai(totalReceber, recebidoAteAgora);
        totalRecebido = recebidoAteAgora;
        valorPago = saldoRestante;
        if (valorPago.compareTo(BigDecimal.ZERO) < 0) {
            valorPago = BigDecimal.ZERO;
        }
        if (saldoRestante.compareTo(BigDecimal.ZERO) < 0) {
            saldoRestante = BigDecimal.ZERO;
        }
    }
}
