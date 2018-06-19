package com.chronos.controll.mdfe;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.MdfeConfiguracao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 19/06/18.
 */
@Named
@ViewScoped
public class MdfeConfiguracaoControll extends AbstractControll<MdfeConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);

    }

    @Override
    protected Class<MdfeConfiguracao> getClazz() {
        return MdfeConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "MDFE_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
