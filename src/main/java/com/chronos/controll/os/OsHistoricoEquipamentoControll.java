package com.chronos.controll.os;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.OsAberturaEquipamento;
import com.chronos.modelo.entidades.OsProdutoServico;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsHistoricoEquipamentoControll extends AbstractControll<OsAberturaEquipamento> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<OsProdutoServico> produtoServicoRepository;

    private List<OsProdutoServico> listaOsProdutoServico;


    @Override
    public void doEdit() {
        super.doEdit();

        try {
            List<OsAberturaEquipamento> listaAberturaEquipamento = dao.getEntitys(OsAberturaEquipamento.class, "numeroSerie", getObjeto().getNumeroSerie());

            listaOsProdutoServico = new ArrayList<>();
            for (OsAberturaEquipamento a : listaAberturaEquipamento) {
                listaOsProdutoServico.addAll(produtoServicoRepository.getEntitys(OsProdutoServico.class, "osAbertura", a.getOsAbertura()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }
    }

    @Override
    protected Class<OsAberturaEquipamento> getClazz() {
        return OsAberturaEquipamento.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OS_HISTORICO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<OsProdutoServico> getListaOsProdutoServico() {
        return listaOsProdutoServico;
    }

    public void setListaOsProdutoServico(List<OsProdutoServico> listaOsProdutoServico) {
        this.listaOsProdutoServico = listaOsProdutoServico;
    }
}
