package com.chronos.service.estoque;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.FinLancamentoPagarService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Created by john on 02/08/17.
 */
public class EntradaNotaFiscalService implements Serializable {


    private static final long serialVersionUID = 1L;
    private Empresa empresa;


    @Inject
    private Repository<NfeCabecalho> repository;
    @Inject
    private EstoqueRepository estoqueRepository;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private FinLancamentoPagarService lancamentoPagarService;


    @Transactional
    public void salvar(NfeCabecalho nfe, ContaCaixa contaCaixa, NaturezaFinanceira naturezaFinanceira) throws Exception {


        boolean inclusao = false;
        if (nfe.getId() == null) {
            inclusao = true;
            for (NfeDetalhe detalhe : nfe.getListaNfeDetalhe()) {
                atualizarEstoque(nfe.getTributOperacaoFiscal(), detalhe);

            }


        } else {
            List<NfeDetalhe> listaNfeDetOld = estoqueRepository.getItens(nfe);
            for (NfeDetalhe detalhe : listaNfeDetOld) {
                atualizarEstoque(nfe.getTributOperacaoFiscal(), detalhe);
            }
            if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                estoqueRepository.atualizaEstoqueEmpresaControleFiscal(empresa.getId(), nfe.getListaNfeDetalhe());
            } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                estoqueRepository.atualizaEstoqueEmpresaControle(empresa.getId(), nfe.getListaNfeDetalhe());
            } else {
                estoqueRepository.atualizaEstoqueEmpresa(empresa.getId(), nfe.getListaNfeDetalhe());
            }
        }
        String descricao = "Entrada da NFe :" + nfe.getNumero() + " Fornecedor :" + nfe.getEmitente().getNome();
        nfe = repository.atualizar(nfe);
        if (!nfe.getListaDuplicata().isEmpty()) {
            lancamentoPagarService.gerarLancamento(nfe, contaCaixa, naturezaFinanceira);
        }
        if (inclusao) {
            auditoriaService.gerarLog(AcaoLog.INSERT, descricao, "Entrada de NF");
        }
    }

    public NfeCabecalho iniciar(Empresa empresa){
        this.empresa = empresa;
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setEmpresa(empresa);
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
        nfe.setTipoEmissao(1);
        nfe.setFinalidadeEmissao(1);
        nfe.setLocalDestino(1);
        nfe.getTransporte().setModalidadeFrete(0);

        Date dataAtual = new Date();
        nfe.setUfEmitente(empresa.getCodigoIbgeUf());
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

    private void atualizarEstoque(TributOperacaoFiscal operacaoFiscal, NfeDetalhe detalhe) throws Exception {
        if (operacaoFiscal.getEstoqueVerificado() && operacaoFiscal.getEstoque()) {
            estoqueRepository.atualizaEstoqueEmpresaControleFiscal(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        } else if (operacaoFiscal.getEstoqueVerificado()) {
            estoqueRepository.atualizaEstoqueEmpresaControle(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        } else {
            estoqueRepository.atualizaEstoqueEmpresa(empresa.getId(), detalhe.getProduto().getId(), detalhe.getQuantidadeComercial());
        }
    }
}
