package com.chronos.controll.agenda;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgendaCategoriaCompromisso;
import com.chronos.modelo.entidades.AgendaCompromisso;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class AgendaCompromissoControll extends AbstractControll<AgendaCompromisso> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<AgendaCategoriaCompromisso> categoriaRepository;


    private Integer recorrente;
    private Integer quantidadeOcorrencia;

    private AgendaCategoriaCompromisso categoria;


    private ScheduleModel eventModel;

    private ScheduleEvent eventoAdicionado;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        eventModel = new DefaultScheduleModel();
    }

    @Override
    public void doCreate() {
        super.doCreate();
        recorrente = 0;
        getObjeto().setColaborador(usuario.getColaborador());

    }

    @Override
    public void salvar() {
        super.salvar();

        try {
            incluiCompromissoConvidado();
            incluiCompromissoRecorrente();
            atualizaCalendario();
            setTelaGrid(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incluirCategoria() {
        categoria = new AgendaCategoriaCompromisso();
    }

    public void salvarCategoria() {
        categoriaRepository.salvar(categoria);
    }

    public void excluirCategoria() {
        categoriaRepository.excluir(getObjeto().getAgendaCategoriaCompromisso());
        getObjeto().setAgendaCategoriaCompromisso(null);
    }


    private void incluiCompromissoConvidado() throws Exception {
//        for (AgendaCompromissoConvidado c : getObjeto().getListaAgendaCompromissoConvidado()) {
//            AgendaCompromisso compromisso = new AgendaCompromisso();
//            compromisso.setColaborador(c.getColaborador());
//            compromisso.setAgendaCategoriaCompromisso(getObjeto().getAgendaCategoriaCompromisso());
//            compromisso.setDataCompromisso(getObjeto().getDataCompromisso());
//            compromisso.setDescricao(getObjeto().getDescricao());
//            compromisso.setDuracao(getObjeto().getDuracao());
//            compromisso.setHora(getObjeto().getHora());
//            compromisso.setOnde(getObjeto().getOnde());
//            compromisso.setTipo(getObjeto().getTipo());
//
//            dao.atualizar(compromisso);
//        }
    }

    private void incluiCompromissoRecorrente() throws Exception {
        if (quantidadeOcorrencia != null && recorrente != null) {
            int campoSomar = 0;
            switch (recorrente) {
                case 1: {
                    campoSomar = Calendar.DAY_OF_MONTH;
                    break;
                }
                case 2: {
                    campoSomar = Calendar.WEEK_OF_MONTH;
                    break;
                }
                case 3: {
                    campoSomar = Calendar.MONTH;
                    break;
                }
                case 4: {
                    campoSomar = Calendar.YEAR;
                    break;
                }
                default: {
                    break;
                }
            }

            if (campoSomar != 0) {
                Calendar dataCompromisso = Calendar.getInstance();
                dataCompromisso.setTime(getObjeto().getDataCompromisso());

                for (int i = 0; i < quantidadeOcorrencia; i++) {
                    AgendaCompromisso compromisso = new AgendaCompromisso();
                    compromisso.setColaborador(getObjeto().getColaborador());
                    compromisso.setAgendaCategoriaCompromisso(getObjeto().getAgendaCategoriaCompromisso());
                    compromisso.setDescricao(getObjeto().getDescricao());
                    compromisso.setDuracao(getObjeto().getDuracao());
                    compromisso.setHora(getObjeto().getHora());
                    compromisso.setOnde(getObjeto().getOnde());
                    compromisso.setTipo(getObjeto().getTipo());

                    dataCompromisso.add(campoSomar, 1);
                    compromisso.setDataCompromisso(dataCompromisso.getTime());

                    dao.atualizar(compromisso);
                }
            }
        }
    }

    public void atualizaCalendario() throws Exception {
        eventModel.clear();
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "colaborador", Filtro.IGUAL, getObjeto().getColaborador()));
        List<AgendaCompromisso> compromissos = dao.getEntitys(AgendaCompromisso.class, filtros);
        for (AgendaCompromisso c : compromissos) {
            String styleClass;
            switch (c.getAgendaCategoriaCompromisso().getCor()) {
                case "Amarelo": {
                    styleClass = "eventoCalendarioAmarelo";
                    break;
                }
                case "Azul": {
                    styleClass = "eventoCalendarioAzul";
                    break;
                }
                case "Branco": {
                    styleClass = "eventoCalendarioBranco";
                    break;
                }
                case "Verde": {
                    styleClass = "eventoCalendarioVerde";
                    break;
                }
                case "Vermelho": {
                    styleClass = "eventoCalendarioVermelho";
                    break;
                }
                default: {
                    styleClass = "eventoCalendarioPreto";
                    break;
                }
            }

            ScheduleEvent event = new DefaultScheduleEvent(c.getDescricao(), c.getDataCompromisso(), c.getDataCompromisso(), styleClass);

            eventModel.addEvent(event);
        }
    }

    public void adicionaEvento() {
        eventModel.addEvent(eventoAdicionado);

        eventoAdicionado = new DefaultScheduleEvent();
    }

    public List<AgendaCategoriaCompromisso> getListaAgendaCategoriaCompromisso(String nome) {
        List<AgendaCategoriaCompromisso> listaAgendaCategoriaCompromisso = new ArrayList<>();
        try {
            listaAgendaCategoriaCompromisso = categoriaRepository.getEntitys(AgendaCategoriaCompromisso.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAgendaCategoriaCompromisso;
    }

    public void onDateSelect(SelectEvent selectEvent) {
        eventoAdicionado = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    @Override
    protected Class<AgendaCompromisso> getClazz() {
        return AgendaCompromisso.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "AGENDA_COMPROMISSO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ScheduleEvent getEventoAdicionado() {
        return eventoAdicionado;
    }

    public void setEventoAdicionado(ScheduleEvent eventoAdicionado) {
        this.eventoAdicionado = eventoAdicionado;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public Integer getRecorrente() {
        return recorrente;
    }

    public void setRecorrente(Integer recorrente) {
        this.recorrente = recorrente;
    }

    public Integer getQuantidadeOcorrencia() {
        return quantidadeOcorrencia;
    }

    public void setQuantidadeOcorrencia(Integer quantidadeOcorrencia) {
        this.quantidadeOcorrencia = quantidadeOcorrencia;
    }

    public AgendaCategoriaCompromisso getCategoria() {
        return categoria;
    }

    public void setCategoria(AgendaCategoriaCompromisso categoria) {
        this.categoria = categoria;
    }
}
