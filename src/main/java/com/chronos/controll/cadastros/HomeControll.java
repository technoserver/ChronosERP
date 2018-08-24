package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgendaCompromisso;
import com.chronos.modelo.entidades.AvisoSistema;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.modelo.view.ViewResumoContas;
import com.chronos.modelo.view.ViewResumoContasID;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.google.gson.Gson;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class HomeControll extends AbstractControll<Colaborador> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<ViewResumoContasID> contasRepository;

    @Inject
    private Repository<AgendaCompromisso> compromissoRepository;

    @Inject
    private Repository<AvisoSistema> avisoSistemaRepository;

    private List<AgendaCompromisso> compromissos;

    private List<ViewResumoContasID> listContas;
    private ViewResumoContas resumoPagar;
    private ViewResumoContas resumoReceber;

    private AvisoSistema avisoConfirmado;

    private String jsonDespesas;
    private String jsonReceitas;
    private String jsonResumoContas;

    private List<AvisoSistema> avisos;

    private boolean possueAviso;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("colaborador.id", usuario.getIdcolaborador()));
        filtros.add(new Filtro("dataCompromisso", Filtro.BETWEEN, new Object[]{Biblioteca.getDataInicial(new Date()), Biblioteca.ultimoDiaMes(new Date())}));
        listContas = contasRepository.getAll(ViewResumoContasID.class);
        compromissos = compromissoRepository.getEntitys(AgendaCompromisso.class, filtros, new Object[]{"descricao", "dataCompromisso", "hora", "onde"});
        jsonResumoContas = new Gson().toJson(listContas);

        avisos = avisoSistemaRepository.getEntitys(AvisoSistema.class, "confirmado", "N");

        possueAviso = !avisos.isEmpty();
    }

    public void confirmarAviso() {
        avisoConfirmado.setDataConfirmacao(new Date());
        avisoConfirmado.setConfirmadoPor(usuario.getNome());
        avisoConfirmado.setConfirmado("S");
        avisoSistemaRepository.atualizar(avisoConfirmado);
        avisos = avisoSistemaRepository.getEntitys(AvisoSistema.class, "confirmado", "N");
        possueAviso = !avisos.isEmpty();
    }

    @Override
    protected Class<Colaborador> getClazz() {
        return Colaborador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "HOME";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public String getJsonDespesas() {
        return jsonDespesas;
    }

    public void setJsonDespesas(String jsonDespesas) {
        this.jsonDespesas = jsonDespesas;
    }

    public String getJsonReceitas() {
        return jsonReceitas;
    }

    public void setJsonReceitas(String jsonReceitas) {
        this.jsonReceitas = jsonReceitas;
    }

    public String getJsonResumoContas() {
        return jsonResumoContas;
    }

    public void setJsonResumoContas(String jsonResumoContas) {
        this.jsonResumoContas = jsonResumoContas;
    }

    public List<AgendaCompromisso> getCompromissos() {
        return compromissos;
    }

    public void setCompromissos(List<AgendaCompromisso> compromissos) {
        this.compromissos = compromissos;
    }

    public List<AvisoSistema> getAvisos() {
        return avisos;
    }

    public boolean isPossueAviso() {
        return possueAviso;
    }

    public AvisoSistema getAvisoConfirmado() {
        return avisoConfirmado;
    }

    public void setAvisoConfirmado(AvisoSistema avisoConfirmado) {
        this.avisoConfirmado = avisoConfirmado;
    }
}

