package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.service.comercial.NfeService;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.io.Serializable;

public class NfeBaseControll extends AbstractControll<NfeCabecalho> implements Serializable {


    @Inject
    private NfeService service;

    private boolean duplicidade;

    public void transmitirNfe() {
        try {


            boolean estoque = isTemAcesso("ESTOQUE");
            StatusTransmissao status = service.transmitirNFe(getObjeto(), estoque);
            if (status == StatusTransmissao.AUTORIZADA) {

                Mensagem.addInfoMessage("NFe transmitida com sucesso");
            } else {
                duplicidade = status == StatusTransmissao.DUPLICIDADE;
            }


        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out")) {
                try {
                    getObjeto().setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    dao.atualizar(getObjeto());
                } catch (Exception ex1) {

                }
            }
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        }
    }


    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
