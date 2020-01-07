package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.OsConfiguracao;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class OsConfiguracaoControll extends AbstractControll<OsConfiguracao> implements Serializable {


    @PostConstruct
    @Override
    public void init() {
        super.init();
        setTelaGrid(false);

        OsConfiguracao os = dao.get(OsConfiguracao.class, "empresa.id", empresa.getId());
        if (os == null) {
            doCreate();
        } else {
            setObjeto(os);
        }
    }

    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setEmpresa(empresa);

    }

    @Override
    public void salvar() {
        super.salvar();
    }

    @Override
    protected Class<OsConfiguracao> getClazz() {
        return OsConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OS_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
