package com.chronos.controll.pcp;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.PcpInstrucao;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PcpInstrucaoControll extends AbstractControll<PcpInstrucao> implements Serializable {


    @Override
    protected Class<PcpInstrucao> getClazz() {
        return PcpInstrucao.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PCP_INSTRUCAO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
