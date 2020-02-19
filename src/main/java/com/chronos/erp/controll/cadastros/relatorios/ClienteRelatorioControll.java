package com.chronos.erp.controll.cadastros.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by john on 14/09/17.
 */
@Named
@RequestScoped
public class ClienteRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    private String uf;
    private String cidade;
    private String cliente;
    private String tipo;
    private String cpfCnpj;
    private Date dataInicial;
    private Date dataFinal;
    private Integer mes = 1;
    private Map<String, Integer> meses;

    @PostConstruct
    @Override
    protected void init() {
        super.init();
        meses = new LinkedHashMap<>();
        meses.put("Janeiro", 1);
        meses.put("Fervereiro", 2);
        meses.put("Marco", 3);
        meses.put("Abril", 4);
        meses.put("Maio", 5);
        meses.put("Junho", 6);
        meses.put("Julho", 7);
        meses.put("Agosto", 8);
        meses.put("Setembro", 9);
        meses.put("Outubro", 10);
        meses.put("Novembro", 11);
        meses.put("Dezembro", 12);
    }

    public void executarRelatorio() {

        parametros = new HashMap<>();

        if (!uf.equals("Selecione")) {
            parametros.put("uf", uf);
        }

        if (!tipo.equals("%")) {
            parametros.put("tipo", tipo);
        }

        if (!StringUtils.isEmpty(cpfCnpj)) {
            parametros.put("cpf_cnpj", cpfCnpj);
        }

        parametros.put("cidade", retornaValorPadrao(cidade));
        parametros.put("nome", retornaValorPadrao(cliente));
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoCliente.jasper";
        String nome = "relacaoClientes." + tipoRelatorio;
        executarRelatorio(caminhoRelatorio, nomeRelatorio, nome);

    }

    public void gerarRelacaoAniversariantes() {

        parametros = new HashMap<>();


        parametros.put("mes", mes);
        parametros.put("idempresa", empresa.getId());

        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoAniversariante.jasper";
        String nome = "aniversariantes." + tipoRelatorio;
        executarRelatorio(caminhoRelatorio, nomeRelatorio, nome);

    }


    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Map<String, Integer> getMeses() {
        return meses;
    }
}
