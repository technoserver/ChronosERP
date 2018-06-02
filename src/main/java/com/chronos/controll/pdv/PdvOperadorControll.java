package com.chronos.controll.pdv;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvOperador;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 01/06/18.
 */
@Named
@ViewScoped
public class PdvOperadorControll extends AbstractControll<PdvOperador> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public void salvar() {

        PdvOperador operador = dao.get(PdvOperador.class, "login", getObjeto().getLogin());

        if (operador != null && !operador.equals(getObjeto())) {
            Mensagem.addErrorMessage("Operador já cadastrado");
        } else {
            super.salvar();
        }


    }

    @Override
    protected Class<PdvOperador> getClazz() {
        return PdvOperador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PDV_OPERADOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
