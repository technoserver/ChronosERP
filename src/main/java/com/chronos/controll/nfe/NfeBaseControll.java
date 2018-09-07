package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.service.ChronosException;
import com.chronos.service.comercial.NfeService;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.io.Serializable;

public class NfeBaseControll extends AbstractControll<NfeCabecalho> implements Serializable {


    @Inject
    private NfeService service;

    private boolean duplicidade;
    private String justificativa;

    public void transmitirNfe(boolean iniciarConfiguracao) {
        try {

            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }

            boolean estoque = isTemAcesso("ESTOQUE");
            StatusTransmissao status = service.transmitirNFe(getObjeto(), estoque);
            if (status == StatusTransmissao.AUTORIZADA) {

                Mensagem.addInfoMessage("NFe transmitida com sucesso");
            } else {
                duplicidade = status == StatusTransmissao.DUPLICIDADE;
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                if (ex.getMessage().contains("Read timed out")) {
                    getObjeto().setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    dao.atualizar(getObjeto());
                } else {
                    Mensagem.addErrorMessage("", ex);
                }
            } else {
                throw new RuntimeException("", ex);
            }

        }
    }

    public void cancelaNfe(boolean iniciarConfiguracao) {
        try {
            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }
            getObjeto().setJustificativaCancelamento(justificativa);

            boolean estoque = isTemAcesso("ESTOQUE");
            boolean cancelado = service.cancelarNFe(getObjeto(), estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFe cancelada com sucesso");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar NFe ", ex);
            }

        }

    }

    public void cartaCorrecao(boolean iniciarConfiguracao) {
        try {
            if (iniciarConfiguracao) {
                service.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            }
            service.cartaCorrecao(getObjeto(), justificativa);
        } catch (Exception ex) {
            if (ex instanceof ChronosException || ex instanceof EmissorException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar a carta de correção", ex);
            }

        }
    }


    public void danfe() {


        try {

            service.danfe(getObjeto());

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao gerar o danfe", ex);
            }
        }
    }


    public void limparJustificativa() {
        justificativa = "";
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

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}
