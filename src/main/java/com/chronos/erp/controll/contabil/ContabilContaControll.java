package com.chronos.erp.controll.contabil;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ContabilConta;
import com.chronos.erp.modelo.entidades.PlanoConta;
import com.chronos.erp.repository.Repository;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ContabilContaControll extends AbstractControll<ContabilConta> {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PlanoConta> planoContaRepository;


    private List<ContabilConta> contas;
    private boolean classificacao;
    private String mascara;

    @Override
    public void doCreate() {
        super.doCreate();
        classificacao = true;
        contas = null;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        definirContas(getObjeto().getPlanoConta().getNiveis());
    }

    public List<PlanoConta> getListaPlanoConta(String nome) {
        List<PlanoConta> lista = new ArrayList<>();
        try {
            lista = planoContaRepository.getEntitys(PlanoConta.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return lista;
    }

    public void selecionarPlano(SelectEvent event) {

        try {
            PlanoConta plano = (PlanoConta) event.getObject();
            int nivel = plano.getNiveis() >= 1 ? plano.getNiveis() - 1 : 1;
            contas = nivel == 0 ? null : dao.getEntitys(ContabilConta.class, "planoConta.niveis", nivel);
            definirContas(nivel);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void definirContas(int nivel) {
        contas = nivel == 0 ? null : dao.getEntitys(ContabilConta.class, "planoConta.niveis", nivel);
        definirMascara();
    }

    public void definirMascara() {
        classificacao = false;
        mascara = getObjeto().getPlanoConta().getMascara().replace("#", "9");
    }


    @Override
    protected Class<ContabilConta> getClazz() {
        return ContabilConta.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CONTA_CONTABIL";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<ContabilConta> getContas() {
        return contas;
    }

    public boolean isClassificacao() {
        return classificacao;
    }

    public String getMascara() {
        return mascara;
    }
}
