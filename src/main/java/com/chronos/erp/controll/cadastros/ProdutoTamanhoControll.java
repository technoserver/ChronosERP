package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.EstoqueTamanho;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ProdutoTamanhoControll extends AbstractControll<EstoqueTamanho> implements Serializable {


    private static final long serialVersionUID = 1L;

    @Override
    public void salvar() {


        try {

            EstoqueTamanho tamanho = dao.get(EstoqueTamanho.class, "codigo", getObjeto().getCodigo());

            if (tamanho != null && !tamanho.getId().equals(getObjeto().getId())) {
                throw new ChronosException("Esté código já foi definido");
            }

            super.salvar();
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao salvar", ex);
            }
        }
    }

    @Override
    protected Class<EstoqueTamanho> getClazz() {
        return EstoqueTamanho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "ESTOQUE_COR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
