package com.chronos.erp.controll.financeiro.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.repository.ParcelaPagarRepository;
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
public class ContasPagarRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ParcelaPagarRepository repository;

    private Date vencimentoDe;
    private Date vencimentoAte;
    private String numDocumento;
    private String situacao;
    private String fornecedor;

    public void gerarContasPagar() {


        try {
            repository.atualizarStatusParcela();
            parametros = new HashMap<>();
            parametros.put("vencimentoDe", Optional.ofNullable(vencimentoDe).orElse(new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01")));
            parametros.put("vencimentoAte", Optional.ofNullable(vencimentoAte).orElse(new Date()));
            parametros.put("numDocumento", StringUtils.isEmpty(numDocumento) ? null : numDocumento);
            parametros.put("fornecedor", ("%" + fornecedor.trim().toLowerCase() + "%"));
            parametros.put("situacao", StringUtils.isEmpty(situacao) ? null : situacao);
            parametros.put("idempresa", empresa.getId());


            String caminhoRelatorio = "/relatorios/financeiro";
            String nomeRelatorio = "relacaoContasPagar.jasper";

            executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoContasPagar.pdf");
        } catch (Exception ex) {
            new RuntimeException("Erro ao tenta gera o relatorio", ex.getCause());
        }
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

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }
}
