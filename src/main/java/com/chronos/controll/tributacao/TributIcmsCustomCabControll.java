package com.chronos.controll.tributacao;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.TributIcmsCustomCab;
import com.chronos.modelo.entidades.TributIcmsCustomDet;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class TributIcmsCustomCabControll extends AbstractControll<TributIcmsCustomCab> implements Serializable {

    private static final long serialVersionUID = 1L;

    private TributIcmsCustomDet tributIcmsCustomDet;
    private TributIcmsCustomDet tributIcmsCustomDetSelecionado;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaTributIcmsCustomDet(new HashSet<>());
    }

    public void incluirTributIcmsCustomDet() {
        tributIcmsCustomDet = new TributIcmsCustomDet();
        tributIcmsCustomDet.setTributIcmsCustomCab(getObjeto());
    }

    public void alterarTributIcmsCustomDet() {
        tributIcmsCustomDet = tributIcmsCustomDetSelecionado;
    }

    public void salvarTributIcmsCustomDet() {
        if (tributIcmsCustomDet.getId() == null) {
            getObjeto().getListaTributIcmsCustomDet().add(tributIcmsCustomDet);
        }
        salvar("Registro salvo com sucesso!");
    }

    public void excluirTributIcmsCustomDet() {

        getObjeto().getListaTributIcmsCustomDet().remove(tributIcmsCustomDetSelecionado);
        salvar("Registro excluído com sucesso!");

    }

    @Override
    protected Class<TributIcmsCustomCab> getClazz() {
        return TributIcmsCustomCab.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRIBUT_ICMS_CUSTOM_CAB";
    }

    public TributIcmsCustomDet getTributIcmsCustomDet() {
        return tributIcmsCustomDet;
    }

    public void setTributIcmsCustomDet(TributIcmsCustomDet tributIcmsCustomDet) {
        this.tributIcmsCustomDet = tributIcmsCustomDet;
    }

    public TributIcmsCustomDet getTributIcmsCustomDetSelecionado() {
        return tributIcmsCustomDetSelecionado;
    }

    public void setTributIcmsCustomDetSelecionado(TributIcmsCustomDet tributIcmsCustomDetSelecionado) {
        this.tributIcmsCustomDetSelecionado = tributIcmsCustomDetSelecionado;
    }

}