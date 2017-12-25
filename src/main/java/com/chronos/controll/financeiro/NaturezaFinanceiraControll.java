package com.chronos.controll.financeiro;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NaturezaFinanceira;
import com.chronos.modelo.entidades.PlanoNaturezaFinanceira;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 15/08/17.
 */

@Named
@ViewScoped
public class NaturezaFinanceiraControll extends AbstractControll<NaturezaFinanceira> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PlanoNaturezaFinanceira> planos;
    private List<NaturezaFinanceira> naturezas;
    private String mascara;
    private boolean classificacao;

    @Override
    public void doCreate() {
        super.doCreate();
        classificacao = true;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        definirMascara();
    }

    @Override
    public void remover() {
        int id = getObjetoSelecionado().getId();
        if (id <= 66) {
            Mensagem.addErrorMessage("Não é possivel remover a natureza " + getObjetoSelecionado().getDescricao());
        } else {
            super.remover();
        }

    }

    @Override
    public void salvar() {
        boolean existe = dao.existeRegisro(NaturezaFinanceira.class, "classificacao", getObjeto().getClassificacao());
        if (existe) {
            Mensagem.addErrorMessage("Já existe uma natureza com essa classificação");
        } else {
            if (getObjeto().getNaturezaFinanceira() != null) {
                String str = getObjeto().getNaturezaFinanceira().getClassificacao();
                if (!getObjeto().getClassificacao().contains(str)) {
                    Mensagem.addErrorMessage("Classificação invalida, deve comerça com : " + str);
                    return;
                }
            }

            super.salvar();
        }


    }

    public void validar() {
    }

    public List<PlanoNaturezaFinanceira> getListaPlanoNaturezaFinanceira(String nome) {
        List<PlanoNaturezaFinanceira> listaPlanoNaturezaFinanceira = new ArrayList<>();
        try {
            listaPlanoNaturezaFinanceira = planos.getEntitys(PlanoNaturezaFinanceira.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPlanoNaturezaFinanceira;
    }

    public void selecionarPlano(SelectEvent event) {
        PlanoNaturezaFinanceira plano = (PlanoNaturezaFinanceira) event.getObject();
        List<Filtro> filtros = new ArrayList<>();
        int nivel = plano.getNiveis() > 1 ? plano.getNiveis() - 1 : 1;
        naturezas = (nivel - 1) == 0 ? null : dao.getEntitys(NaturezaFinanceira.class, "planoNaturezaFinanceira.niveis", nivel);
        definirMascara();
    }

    public void buscarNaturezas() {
        naturezas = dao.getEntitys(NaturezaFinanceira.class, new Object[]{"classificacao", "descricao", "tipo", "planoNaturezaFinanceira.niveis"});
        String descricao = "";
        int nivel;
        for (NaturezaFinanceira n : naturezas) {
            nivel = n.getPlanoNaturezaFinanceira().getNiveis();
            descricao = StringUtils.leftPad("", nivel);
            descricao += n.getDescricao();
            n.setDescricao(descricao);
        }

    }

    public void definirMascara() {
        classificacao = false;
        mascara = getObjeto().getPlanoNaturezaFinanceira().getMascara().replace("#", "9");
    }

    @Override
    protected Class<NaturezaFinanceira> getClazz() {
        return NaturezaFinanceira.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NATUREZA_FINANCEIRA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public List<NaturezaFinanceira> getNaturezas() {
        return naturezas;
    }

    public void setNaturezas(List<NaturezaFinanceira> naturezas) {
        this.naturezas = naturezas;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public boolean isClassificacao() {
        return classificacao;
    }

    public void setClassificacao(boolean classificacao) {
        this.classificacao = classificacao;
    }
}
