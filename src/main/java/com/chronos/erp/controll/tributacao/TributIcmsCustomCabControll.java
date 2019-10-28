package com.chronos.erp.controll.tributacao;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.TributIcmsCustomCab;
import com.chronos.erp.modelo.entidades.TributIcmsCustomDet;

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

    @Override
    public void doEdit() {
        super.doEdit();
        TributIcmsCustomCab trib = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(trib);

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
        return "ICMS_CUSTOMIZADO";
    }

    @Override
    protected boolean auditar() {
        return false;
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

    public boolean isSimplesNascional() {
        return empresa.getCrt() != null && empresa.getCrt().equals("1");
    }

}
