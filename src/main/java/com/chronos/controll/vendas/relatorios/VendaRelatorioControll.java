package com.chronos.controll.vendas.relatorios;

import com.chronos.controll.AbstractRelatorioControll;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by john on 18/09/17.
 */
@Named
@RequestScoped
public class VendaRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    public void imprimirPedido(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas/venda.jasper";


        executarRelatorio(caminhoRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirPedidoCupom(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas/vendaCupom.jasper";


        executarRelatorio(caminhoRelatorio, "pedidoVenda.pdf");
    }
}
