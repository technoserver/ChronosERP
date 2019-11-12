package com.chronos.erp.controll.cadastros.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

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


    public void executarRelatorio() {

        parametros = new HashMap<>();
        parametros.put("uf", retornaValorPadrao(uf).replace("Selecione", ""));
        parametros.put("cidade", retornaValorPadrao(cidade));
        parametros.put("nome", retornaValorPadrao(cliente));
        parametros.put("tipo", retornaValorPadrao(tipo));
        parametros.put("cpf_cnpj", cpfCnpj);
        parametros.put("idempresa", empresa.getId());
        String caminhoRelatorio = "/relatorios/cadastros";
        String nomeRelatorio = "relacaoCliente.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoClientes.pdf");

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
}
