package com.chronos.controll.financeiro.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.dto.ReciboPagamentoDTO;
import com.chronos.repository.ParcelaPagarRepository;
import org.springframework.util.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by john on 03/11/17.
 */
@Named
@RequestScoped
public class ContasReceberRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ParcelaPagarRepository repository;

    private Date vencimentoDe;
    private Date vencimentoAte;
    private String numDocumento;
    private String situacao;
    private String cliente;

    public void gerarContasReceber() {


        try {
            repository.atualizarStatusParcela();
            parametros = new HashMap<>();
            parametros.put("vencimentoDe", Optional.ofNullable(vencimentoDe).orElse(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01")));
            parametros.put("vencimentoAte", Optional.ofNullable(vencimentoAte).orElse(new Date()));
            parametros.put("numDocumento", StringUtils.isEmpty(numDocumento) ? null : numDocumento);
            parametros.put("fornecedor", ("%" + cliente.trim().toLowerCase() + "%"));
            parametros.put("situacao", StringUtils.isEmpty(situacao) ? null : situacao);
            parametros.put("idempresa", empresa.getId());


            String caminhoRelatorio = "/relatorios/financeiro";
            String nomeRelatorio = "relacaoContasReceber.jasper";

            executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoContasPagar.pdf");
        } catch (Exception ex) {
            new RuntimeException("Erro ao tenta gera o relatorio", ex);
        }
    }

    public void gerarReciboRecebimento(ReciboPagamentoDTO recibo) {
        parametros = new HashMap<>();
        parametros.put("id_cliente", recibo.getIdcliente());
        parametros.put("id_tipo_recebimento", recibo.getIdtipoRecebimento());
        parametros.put("valor_pago", recibo.getValorPago());
        parametros.put("valor_extenso", recibo.getValorExtenso());
        parametros.put("ids_recebimento", recibo.getIdsrecebimento());
        parametros.put("id_empresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/financeiro";
        String nomeRelatorio = "reciboPagamento.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "reciboRecebimento.pdf");

    }

    public void gerarCarner(int id) {

        parametros = new HashMap<>();
        parametros.put("id", id);

        String caminhoRelatorio = "/relatorios/financeiro";
        String nomeRelatorio = "carner.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "carner.pdf");
    }


    public Date getVencimentoDe() {
        return vencimentoDe;
    }

    public void setVencimentoDe(Date vencimentoDe) {
        this.vencimentoDe = vencimentoDe;
    }

    public Date getVencimentoAte() {
        return vencimentoAte;
    }

    public void setVencimentoAte(Date vencimentoAte) {
        this.vencimentoAte = vencimentoAte;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
