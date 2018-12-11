package com.chronos.controll.pdv;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvConfiguracaoBalanca;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PdvBalancaControll extends AbstractControll<PdvConfiguracaoBalanca> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    protected Class<PdvConfiguracaoBalanca> getClazz() {
        return PdvConfiguracaoBalanca.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONFIGURACAO_BALANCA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
