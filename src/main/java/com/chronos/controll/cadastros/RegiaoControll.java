package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Regiao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 15/06/18.
 */
@Named
@ViewScoped
public class RegiaoControll extends AbstractControll<Regiao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Regiao> getClazz() {
        return Regiao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "REGIAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
