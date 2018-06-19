package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ProprietarioVeiculo;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 16/06/18.
 */
@Named
@ViewScoped
public class ProprietarioVeiculoControll extends AbstractControll<ProprietarioVeiculo> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Override
    protected Class<ProprietarioVeiculo> getClazz() {
        return ProprietarioVeiculo.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PROPRIETARIO_VEICULO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
