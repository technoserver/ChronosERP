package com.chronos.service.estoque;

import com.chronos.controll.cadastros.PessoaControll;
import com.chronos.modelo.entidades.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by john on 02/08/17.
 */
public class EntradaNotaFiscalService implements Serializable {


    private static final long serialVersionUID = 1L;



    public NfeCabecalho iniciar(){
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setEmitente(new NfeEmitente());
        nfe.getEmitente().setNfeCabecalho(nfe);
        nfe.setLocalEntrega(new NfeLocalEntrega());
        nfe.getLocalEntrega().setNfeCabecalho(nfe);
        nfe.setLocalRetirada(new NfeLocalRetirada());
        nfe.getLocalRetirada().setNfeCabecalho(nfe);
        nfe.setTransporte(new NfeTransporte());
        nfe.getTransporte().setNfeCabecalho(nfe);
        nfe.setFatura(new NfeFatura());
        nfe.getFatura().setNfeCabecalho(nfe);

        nfe.setListaNfeReferenciada(new HashSet<>());
        nfe.setListaNfReferenciada(new HashSet<>());
        nfe.setListaCteReferenciado(new HashSet<>());
        nfe.setListaProdRuralReferenciada(new HashSet<>());
        nfe.setListaCupomFiscalReferenciado(new HashSet<>());
        nfe.getTransporte().setListaTransporteReboque(new HashSet<>());
        nfe.getTransporte().setListaTransporteVolume(new HashSet<>());
        nfe.setListaDuplicata(new HashSet<>());
        nfe.setListaNfeDetalhe(new ArrayList<>());
        valoresPadrao(nfe);

        return nfe;
    }


    private void valoresPadrao(NfeCabecalho nfe) {
        nfe.setTipoOperacao(0);
        nfe.setStatusNota(0);
        nfe.setFormatoImpressaoDanfe(1);
        nfe.setConsumidorOperacao(1);
        nfe.setConsumidorPresenca(1);
        nfe.setTipoEmissao(1);
        nfe.setFinalidadeEmissao(1);
        nfe.setLocalDestino(1);
        nfe.getTransporte().setModalidadeFrete(0);

        Date dataAtual = new Date();
        nfe.setUfEmitente(nfe.getEmpresa().getCodigoIbgeUf());
        nfe.setDataHoraEmissao(dataAtual);
        nfe.setDataHoraEntradaSaida(dataAtual);

        nfe.setBaseCalculoIcms(BigDecimal.ZERO);
        nfe.setValorIcms(BigDecimal.ZERO);
        nfe.setValorTotalProdutos(BigDecimal.ZERO);
        nfe.setBaseCalculoIcmsSt(BigDecimal.ZERO);
        nfe.setValorIcmsSt(BigDecimal.ZERO);
        nfe.setValorIpi(BigDecimal.ZERO);
        nfe.setValorPis(BigDecimal.ZERO);
        nfe.setValorCofins(BigDecimal.ZERO);
        nfe.setValorFrete(BigDecimal.ZERO);
        nfe.setValorSeguro(BigDecimal.ZERO);
        nfe.setValorDespesasAcessorias(BigDecimal.ZERO);
        nfe.setValorDesconto(BigDecimal.ZERO);
        nfe.setValorTotal(BigDecimal.ZERO);
        nfe.setValorImpostoImportacao(BigDecimal.ZERO);
        nfe.setBaseCalculoIssqn(BigDecimal.ZERO);
        nfe.setValorIssqn(BigDecimal.ZERO);
        nfe.setValorPisIssqn(BigDecimal.ZERO);
        nfe.setValorCofinsIssqn(BigDecimal.ZERO);
        nfe.setValorServicos(BigDecimal.ZERO);
        nfe.setValorRetidoPis(BigDecimal.ZERO);
        nfe.setValorRetidoCofins(BigDecimal.ZERO);
        nfe.setValorRetidoCsll(BigDecimal.ZERO);
        nfe.setBaseCalculoIrrf(BigDecimal.ZERO);
        nfe.setValorRetidoIrrf(BigDecimal.ZERO);
        nfe.setBaseCalculoPrevidencia(BigDecimal.ZERO);
        nfe.setValorRetidoPrevidencia(BigDecimal.ZERO);
        nfe.setValorIcmsDesonerado(BigDecimal.ZERO);
    }
}
