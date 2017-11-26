package com.chronos.controll.vendas.relatorios;

import com.chronos.controll.AbstractRelatorioControll;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by john on 18/09/17.
 */
@Named
@RequestScoped
public class VendaRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date dataInicial;
    private Date dataFinal;

    public void imprimirPedido(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "venda.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirPedidoCupom(int id) {
        parametros = new HashMap<>();
        parametros.put("idvenda", id);

        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "vendaCupom.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "pedidoVenda.pdf");
    }

    public void imprimirRelacaoVendas() {
        parametros = new HashMap<>();
        parametros.put("dataPedidoInicial", dataInicial);
        parametros.put("dataPedidoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "relacaoVendas.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "realcaoVendas.pdf");
    }

    public void imprimirRanckProdutos() {
        parametros = new HashMap<>();
        parametros.put("dataInicial", dataInicial);
        parametros.put("dataFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/vendas";
        String nomeRelatorio = "rankProdutos.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "ranckProdutos.pdf");
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }
}
