package com.chronos.controll.pdv;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by john on 01/06/18.
 */
@Named
@ViewScoped
public class PdvCaixaControll extends AbstractControll<PdvCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public void salvar() {
        if (validar()) {
            super.salvar();
        }
    }

    private boolean validar() {
        PdvCaixa caixa = dao.get(PdvCaixa.class, "codigo", getObjeto().getCodigo());

        if (caixa != null && !caixa.equals(getObjeto())) {
            Mensagem.addErrorMessage("Já foi informado um caixa com esse codigo");
            return false;
        } else {
            caixa = dao.get(PdvCaixa.class, "nome", getObjeto().getNome());

            if (caixa != null && !caixa.equals(getObjeto())) {
                Mensagem.addErrorMessage("Já foi informado um caixa com esse nome");
                return false;
            }
        }

        return true;
    }

    @Override
    protected Class<PdvCaixa> getClazz() {
        return PdvCaixa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PDV_CAIXA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
