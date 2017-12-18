package com.chronos.controll.agenda;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgendaCategoriaCompromisso;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class AgendaCategoriaCompromissoControll extends AbstractControll<AgendaCategoriaCompromisso> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<AgendaCategoriaCompromisso> getClazz() {
        return AgendaCategoriaCompromisso.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "AGENDA_CATEGORIA_COMPROMISSO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
