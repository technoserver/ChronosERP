package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.AgendaCompromisso;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgendaService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<AgendaCompromisso> repository;

    public List<ScheduleEvent> definirEventoColaborador(Integer idcolaborador) {

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "colaborador.id", Filtro.IGUAL, idcolaborador));
        List<AgendaCompromisso> compromissos = repository.getEntitys(AgendaCompromisso.class, filtros);
        List<ScheduleEvent> scheduleEventList = new ArrayList<>();
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
            scheduleEventList.add(event);


        }
        return scheduleEventList;
    }
}
