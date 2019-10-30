package com.chronos.erp.controll.agenda;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ReuniaoSala;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 18/12/17.
 */
@Named
@ViewScoped
public class ReuniaoSalaControll extends AbstractControll<ReuniaoSala> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<ReuniaoSala> getClazz() {
        return ReuniaoSala.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "REUNIAO_SALA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
