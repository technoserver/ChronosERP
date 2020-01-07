package com.chronos.erp.controll.os;

import com.chronos.erp.modelo.entidades.OsAberturaEquipamento;
import com.chronos.erp.modelo.entidades.OsProdutoServico;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsHistoricoEquipamentoControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<OsAberturaEquipamento> repository;
    @Inject
    private Repository<OsProdutoServico> produtoServicoRepository;

    private List<OsProdutoServico> listaOsProdutoServico;

    private List<OsAberturaEquipamento> lista;

    private String cliente;
    private String numeroSerie;
    private Date dataInicial;
    private Date dataFinal;
    private boolean telaGrid;

    @PostConstruct
    public void init() {
        telaGrid = true;
    }

    public void pesquisar() {
        List<Filtro> filtros = new ArrayList<>();
        if (!StringUtils.isEmpty(cliente)) {
            filtros.add(new Filtro("osAbertura.cliente.pessoa.nome", Filtro.LIKE, cliente));
        }

        if (!StringUtils.isEmpty(numeroSerie)) {
            filtros.add(new Filtro("numeroSerie", Filtro.LIKE, numeroSerie));
        }

        if (dataInicial != null) {
            filtros.add(new Filtro("osAbertura.dataInicio", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }

        if (dataFinal != null) {
            filtros.add(new Filtro("osAbertura.dataInicio", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        lista = repository.getEntitys(OsAberturaEquipamento.class, filtros, new Object[]{"numeroSerie", "tipoCobertura", "osEquipamento.nome", "osAbertura.id", "osAbertura.dataInicio"});
    }


    public void doEdit() {


        try {
            List<OsAberturaEquipamento> listaAberturaEquipamento = repository.getEntitys(OsAberturaEquipamento.class, "numeroSerie", "");

            listaOsProdutoServico = new ArrayList<>();
            for (OsAberturaEquipamento a : listaAberturaEquipamento) {
                listaOsProdutoServico.addAll(produtoServicoRepository.getEntitys(OsProdutoServico.class, "osAbertura", a.getOsAbertura()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }
    }


    public List<OsAberturaEquipamento> getLista() {
        return lista;
    }

    public List<OsProdutoServico> getListaOsProdutoServico() {
        return listaOsProdutoServico;
    }

    public void setListaOsProdutoServico(List<OsProdutoServico> listaOsProdutoServico) {
        this.listaOsProdutoServico = listaOsProdutoServico;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
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

    public boolean isTelaGrid() {
        return telaGrid;
    }
}
