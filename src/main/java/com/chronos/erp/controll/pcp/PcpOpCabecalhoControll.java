package com.chronos.erp.controll.pcp;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.PcpOpCabecalho;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashSet;

@Named
@ViewScoped
public class PcpOpCabecalhoControll extends AbstractControll<PcpOpCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaPcpOpDetalhe(new HashSet<>());
    }

    @Override
    protected Class<PcpOpCabecalho> getClazz() {
        return PcpOpCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PCP_OPERACAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
