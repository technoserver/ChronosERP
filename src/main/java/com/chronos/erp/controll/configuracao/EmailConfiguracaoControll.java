package com.chronos.erp.controll.configuracao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.EmailConfiguracao;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 16/07/18.
 */
@Named
@ViewScoped
public class EmailConfiguracaoControll extends AbstractControll<EmailConfiguracao> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        EmailConfiguracao email = dao.get(EmailConfiguracao.class, "empresa.id", empresa.getId());
        setObjeto(email);
        setTelaGrid(false);
        if (email == null) {
            doCreate();
        }

    }

    @Override
    public void salvar() {
        try {

            if (getObjeto().getId() != null) {
                EmailConfiguracao email = dao.get(EmailConfiguracao.class, "usuario", getObjeto().getUsuario());

                if (email != null && !email.equals(getObjeto())) {
                    throw new ChronosException("Já foi defiido um usuario");
                }
            }

            super.salvar();
            setTelaGrid(false);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao cadastra email", ex);
            }
        }

    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
    }

    @Override
    protected Class<EmailConfiguracao> getClazz() {
        return EmailConfiguracao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "EMAIL_CONFIGURACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
