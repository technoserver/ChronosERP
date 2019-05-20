package com.chronos.controll.estoque.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.repository.Repository;

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


    public void imprimirRelacaoEntradas() {
        parametros = new HashMap<>();
        parametros.put("peridoInicial", dataInicial);
        parametros.put("peridoFinal", dataFinal);
        parametros.put("idempresa", empresa.getId());

        if (fornecedor != null) {
            parametros.put("idfornecedor", fornecedor.getId());
        }

        String caminhoRelatorio = "/relatorios/estoque";
        String nomeRelatorio = "relacaoNotaEntrada.jasper";

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
}
