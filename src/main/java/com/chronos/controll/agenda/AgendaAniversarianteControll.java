package com.chronos.controll.agenda;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgendaCompromisso;
import com.chronos.modelo.entidades.Colaborador;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.AgendaService;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class AgendaAniversarianteControll extends AbstractControll<Colaborador> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ScheduleModel eventModel;
    @Inject
    private Repository<AgendaCompromisso> compromissoRepository;

    @Inject
    private AgendaService service;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        eventModel = new DefaultScheduleModel();
        try {
            atualizaCalendario();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao localizar aniverssariantes", e);
        }
    }

    private void atualizaCalendario() throws Exception {
        eventModel.clear();
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "pessoa.pessoaFisica.dataNascimento", Filtro.NAO_NULO, ""));
        List<Colaborador> colaboradores = dao.getEntitys(Colaborador.class, filtros, new Object[]{"pessoa.nome", "pessoa.pessoaFisica.dataNascimento"});

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);

        for (int i = 0; i < 120; i++) {
            for (Colaborador c : colaboradores) {

                Calendar dataAniversario = Calendar.getInstance();
                dataAniversario.setTime(c.getDataNascimento());

                if ((dataAniversario.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
                        && (dataAniversario.get(Calendar.MONTH) == calendar.get(Calendar.MONTH))) {
                    String nome = c.getNome();
                    DefaultScheduleEvent event = new DefaultScheduleEvent(nome, calendar.getTime(), calendar.getTime(), "eventoCalendarioAniversario");
                    event.setAllDay(true);
                    eventModel.addEvent(event);
                }

            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        service.definirEventoColaborador(usuario.getColaborador().getId()).forEach(e -> {
            eventModel.addEvent(e);
        });



    }

    @Override
    protected Class<Colaborador> getClazz() {
        return Colaborador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "AGENDA_ANIVERSARIANTE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }
}
