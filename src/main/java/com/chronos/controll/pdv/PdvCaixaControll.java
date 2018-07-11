package com.chronos.controll.pdv;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PdvCaixa;
import com.chronos.modelo.entidades.PdvConfiguracao;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 01/06/18.
 */
@Named
@ViewScoped
public class PdvCaixaControll extends AbstractControll<PdvCaixa> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PdvConfiguracao> pdvConfiguracaoRepository;

    @Override
    public void salvar() {
        if (validar()) {
            super.salvar();
        }
    }

    private boolean validar() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "codigo", Filtro.IGUAL, getObjeto().getCodigo()));
        filtros.add(new Filtro(Filtro.OR, "nome", Filtro.IGUAL, getObjeto().getNome()));
        PdvCaixa caixa = dao.get(PdvCaixa.class, filtros);

        if (caixa != null && !caixa.equals(getObjeto())) {

            if (caixa.getCodigo().equals(getObjeto().getCodigo())) {
                Mensagem.addErrorMessage("Já foi informado um caixa com esse codigo");
                return false;
            } else if (caixa.getNome().equals(getObjeto().getNome())) {
                Mensagem.addErrorMessage("Já foi informado um caixa com esse nome");
                return false;
            }


        } else {
            filtros.clear();
            filtros.add(new Filtro("web", Filtro.IGUAL, "S"));
            caixa = dao.get(PdvCaixa.class, filtros);
            if (caixa != null && !caixa.equals(getObjeto()) && getObjeto().getWeb().equals("S")) {
                Mensagem.addErrorMessage("Já foi informado um caixa web");
                return false;
            }

            PdvConfiguracao conf = pdvConfiguracaoRepository.get(PdvConfiguracao.class, "pdvCaixa", getObjeto(), new Object[]{"pdvCaixa"});
            if (conf != null && getObjeto().getWeb().equals("N")) {
                Mensagem.addErrorMessage("já foram definidas configurações com esse caixa WEB. Alteração de tipo não realizada");
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
