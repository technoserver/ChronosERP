package com.chronos.erp.controll.gerencial;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.SituacaoForCli;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class SituacaoClienteControll extends AbstractControll<SituacaoForCli> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public void salvar() {

        try {
            if (getObjeto().getId() == 1 || getObjeto().getId() == 2) {
                throw new ChronosException("Alteração para esse regitro não permitida");
            }

            super.salvar();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao tenta excluir o registro");
            }
        }
    }

    @Override
    public void remover() {
        try {

            if (getObjetoSelecionado().getId() == 1 || getObjetoSelecionado().getId() == 2) {
                throw new ChronosException("Exclusão para esse regitro não permitida");
            }

            super.remover();
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao tenta excluir o registro");
            }
        }
    }

    @Override
    protected Class<SituacaoForCli> getClazz() {
        return SituacaoForCli.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "SITUACAO_CLIENTE";
    }

    @Override
    protected boolean auditar() {
        return true;
    }
}
