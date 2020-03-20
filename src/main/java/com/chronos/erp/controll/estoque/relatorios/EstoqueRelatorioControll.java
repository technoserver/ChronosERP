package com.chronos.erp.controll.estoque.relatorios;

import com.chronos.erp.controll.AbstractRelatorioControll;
import com.chronos.erp.modelo.entidades.Fornecedor;
import com.chronos.erp.repository.Repository;
import org.springframework.util.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by john on 18/09/17.
 */
@Named
@RequestScoped
public class EstoqueRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Fornecedor> fornecedorRepository;

    private Date dataInicial;
    private Date dataFinal;
    private Fornecedor fornecedor;
    private String numero;

    private String detalhado = "N";


    public void imprimirRelacaoEntradas() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        if (fornecedor != null) {
            parametros.put("idfornecedor", fornecedor.getId());
        }

        if (!StringUtils.isEmpty(numero)) {
            parametros.put("numero", numero);
        }

        String caminhoRelatorio = "/relatorios/estoque";
        String nomeRelatorio = detalhado.equals("N") ? "relacaoNotaEntrada.jasper" : "relacaoNotaEntradaDetalhado.jasper";

        executarRelatorio(caminhoRelatorio, nomeRelatorio, "relacaoEntradas.pdf");
    }

    public List<Fornecedor> getListaFornecedor(String nome) {
        List<Fornecedor> listaFornecedor = new ArrayList<>();

        try {
            listaFornecedor = fornecedorRepository.getEntitys(Fornecedor.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaFornecedor;
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

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDetalhado() {
        return detalhado;
    }

    public void setDetalhado(String detalhado) {
        this.detalhado = detalhado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
