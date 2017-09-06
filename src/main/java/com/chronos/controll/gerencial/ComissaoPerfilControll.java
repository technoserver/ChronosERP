package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.ComissaoPerfil;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 23/08/17.
 */
@Named
@ViewScoped
public class ComissaoPerfilControll extends AbstractControll<ComissaoPerfil> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<ComissaoPerfil> getClazz() {
        return ComissaoPerfil.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "Comissao_Perfil";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
