package com.chronos.erp.service.comercial;


import com.chronos.erp.modelo.entidades.FormaPagamento;
import com.chronos.erp.modelo.entidades.TipoPagamento;
import com.chronos.erp.util.Biblioteca;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class FormaPagamentoService {


    public BigDecimal verificaSaldoRestante(List<FormaPagamento> pagamentos, BigDecimal totalReceber, BigDecimal valorPago) {

        BigDecimal recebidoAteAgora = BigDecimal.ZERO;
        for (FormaPagamento p : pagamentos) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        BigDecimal saldoRestante = Biblioteca.subtrai(totalReceber, recebidoAteAgora);
        valorPago = saldoRestante;

        if (valorPago.compareTo(BigDecimal.ZERO) < 0) {
            valorPago = BigDecimal.ZERO;
        }
        if (saldoRestante.compareTo(BigDecimal.ZERO) < 0) {
            saldoRestante = BigDecimal.ZERO;
        }

        return saldoRestante;
    }

    public Optional<FormaPagamento> bucarTipoPagamento(List<FormaPagamento> pagamentos, TipoPagamento tipoPagamento) {
        return pagamentos
                .stream()
                .filter(fp -> fp.getTipoPagamento().equals(tipoPagamento))
                .findAny();
    }
}
