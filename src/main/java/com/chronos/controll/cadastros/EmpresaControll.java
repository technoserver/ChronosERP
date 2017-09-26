package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Empresa;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 24/09/17.
 */
@Named
@ViewScoped
public class EmpresaControll extends AbstractControll<Empresa> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    protected Class<Empresa> getClazz() {
        return Empresa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "EMPRESA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
