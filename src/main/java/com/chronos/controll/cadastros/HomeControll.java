package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.AgendaCompromisso;
import com.chronos.modelo.entidades.Colaborador;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class HomeControll extends AbstractControll<Colaborador> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<AgendaCompromisso> compromissos;


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
}
