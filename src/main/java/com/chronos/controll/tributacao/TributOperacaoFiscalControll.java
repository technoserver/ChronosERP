package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Cfop;
import com.chronos.modelo.entidades.TributOperacaoFiscal;
import com.chronos.repository.Repository;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributOperacaoFiscalControll extends AbstractControll<TributOperacaoFiscal> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cfop> cfops;

    private Cfop cfop;



    public void selecionaCFOP(SelectEvent event) {
        Cfop cfopSelecionado = (Cfop) event.getObject();
        getObjeto().setCfop(cfopSelecionado.getCfop());

        this.cfop = null;
    }

    public List<Cfop> getListaCfop(String nome) {
        List<Cfop> listaCfop = new ArrayList<>();
        try {
            listaCfop = cfops.getEntitys(Cfop.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCfop;
    }

    @Override
    protected Class<TributOperacaoFiscal> getClazz() {
        return TributOperacaoFiscal.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_OPERACAO_FISCAL";
    }

    public Cfop getCfop() {
        return cfop;
    }

    public void setCfop(Cfop cfop) {
        this.cfop = cfop;
    }
}
