package com.chronos.erp.dto;

import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;

/**
 * Created by john on 15/06/18.
 */
public class RetornoEventoDTO {

    private final TEnvEvento enviEvento;
    private final TRetEnvEvento retorno;
    private final TProcEvento procEvento;


    public RetornoEventoDTO(TEnvEvento enviEvento, TRetEnvEvento retorno) {
        this.enviEvento = enviEvento;
        this.retorno = retorno;
        this.procEvento = new TProcEvento();
    }


    public TProcEvento getProcEvento() {
        procEvento.setVersao("1.00");
        procEvento.setEvento(enviEvento.getEvento().get(0));
        procEvento.setRetEvento(retorno.getRetEvento().get(0));
        return procEvento;
    }

    public TEnvEvento getEnviEvento() {
        return enviEvento;
    }

    public TRetEnvEvento getRetorno() {
        return retorno;
    }
}
