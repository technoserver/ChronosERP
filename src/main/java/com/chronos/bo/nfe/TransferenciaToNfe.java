package com.chronos.bo.nfe;

import com.chronos.modelo.entidades.*;
import com.chronos.service.ChronosException;
import com.chronos.transmissor.infra.enuns.ConsumidorOperacao;
import com.chronos.transmissor.infra.enuns.IndicadorIe;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaToNfe {

    private NfeCabecalho nfe;
    private EstoqueTransferenciaCabecalho transferencia;
    private Empresa empresaDestino;
    private Empresa empresaOrigem;
    private NfeUtil nfeUtil;

    public TransferenciaToNfe(EstoqueTransferenciaCabecalho transferencia) {
        this.transferencia = transferencia;
        this.empresaDestino = transferencia.getEmpresaDestino();
        this.empresaOrigem = transferencia.getEmpresaOrigem();
        nfeUtil = new NfeUtil();
    }

    public NfeCabecalho gerarNFe() throws Exception {
        nfe = nfeUtil.definirDadosPadrao(ModeloDocumento.NFE, empresaOrigem);
        definirEmitente();
        definirDestinatario();
        definirOperacaoTributaria();
        gerarItens();
        definirFormaPagamento();
        return nfe;
    }

    private void definirEmitente() {
        NfeEmitente emitente = nfeUtil.getEmitente(empresaOrigem);
        emitente.setNfeCabecalho(nfe);
        nfe.setEmitente(emitente);
    }

    private void definirOperacaoTributaria() {
        nfe.setTributOperacaoFiscal(transferencia.getTributOperacaoFiscal());
        nfe.setNaturezaOperacao(StringUtils.isEmpty(transferencia.getTributOperacaoFiscal().getDescricaoNaNf()) ? transferencia.getTributOperacaoFiscal().getDescricao() : transferencia.getTributOperacaoFiscal().getDescricaoNaNf());
    }

    private void definirDestinatario() {


        nfe.getDestinatario().setCpfCnpj(empresaDestino.getCnpj());
        nfe.getDestinatario().setNome(empresaDestino.getRazaoSocial());
        EmpresaEndereco endereco = empresaDestino.buscarEnderecoPrincipal();

        nfe.getDestinatario().setLogradouro(endereco.getLogradouro());
        nfe.getDestinatario().setComplemento(endereco.getComplemento());
        nfe.getDestinatario().setNumero(endereco.getNumero());
        nfe.getDestinatario().setBairro(endereco.getBairro());
        nfe.getDestinatario().setNomeMunicipio(endereco.getCidade());
        nfe.getDestinatario().setCodigoMunicipio(endereco.getMunicipioIbge());
        nfe.getDestinatario().setUf(endereco.getUf());
        nfe.getDestinatario().setCep(endereco.getCep());
        nfe.getDestinatario().setTelefone(endereco.getFone());
        nfe.getDestinatario().setInscricaoEstadual(empresaDestino.getInscricaoEstadual());
        nfe.getDestinatario().setCodigoPais(1058);
        nfe.getDestinatario().setNomePais("Brazil");


        String ufDestino = endereco.getUf();
        LocalDestino localDestino = getLocalDestino(nfe.getEmitente().getUf(), ufDestino);


        IndicadorIe indicador;


        if (nfe.getDestinatario().getInscricaoEstadual() == null || nfe.getDestinatario().getInscricaoEstadual().equals("")) {
            indicador = IndicadorIe.NAO_CONTRIBUINTE;

        } else if (nfe.getDestinatario().getInscricaoEstadual().equalsIgnoreCase("ISENTO")) {
            indicador = IndicadorIe.CONTRIBUINTE_ISENTO;
        } else {
            indicador = IndicadorIe.CONTRIBUINTE_ICMS;
        }


        ConsumidorOperacao consumidorOperacao = indicador == IndicadorIe.NAO_CONTRIBUINTE ? ConsumidorOperacao.FINAL : ConsumidorOperacao.NORMAL;
        nfe.getDestinatario().setIndicadorIe(indicador.getCodigo());
        nfe.setLocalDestino(localDestino.getCodigo());
        nfe.setConsumidorOperacao(consumidorOperacao.getCodigo());
    }


    private void gerarItens() throws Exception {
        int numeroItem = 0;

        List<NfeDetalhe> itensNfe = new ArrayList<>();
        NfeUtil nfeUtil = new NfeUtil();
        for (EstoqueTransferenciaDetalhe item : transferencia.getListEstoqueTransferenciaDetalhe()) {
            if (item.getProduto().getNcm() == null) {
                throw new ChronosException("Não existe NCM para o produto :" + item.getProduto().getDescricaoPdv() + " informado. Operação não realizada.");
            }
            NfeDetalhe itemNfe = new NfeDetalhe();
            itemNfe.setNfeCabecalho(nfe);
            itemNfe.setQuantidadeComercial(item.getQuantidade());
            item.getProduto().setTributGrupoTributario(transferencia.getTributGrupoTributario());
            itemNfe.setProduto(item.getProduto());

            itemNfe.pegarInfoProduto();

            BigDecimal valorVenda = item.getValorCusto();
            valorVenda = valorVenda == null ? itemNfe.getProduto().getValorVenda() : valorVenda;
            itemNfe.setValorUnitarioComercial(valorVenda);
            itemNfe.setQuantidadeComercial(item.getQuantidade());
            itemNfe.setValorDesconto(BigDecimal.ZERO);
            itemNfe.setNumeroItem(++numeroItem);
            itemNfe.setEntraTotal(1);

            itemNfe = nfeUtil.defineTributacao(itemNfe, transferencia.getTributOperacaoFiscal(), nfe.getDestinatario());
            itensNfe.add(itemNfe);
        }

        nfe.getListaNfeDetalhe().addAll(itensNfe);


        nfeUtil.calcularTotalNFe(nfe);
    }

    private void definirFormaPagamento() {


        PdvTipoPagamento tipoPagamento = new PdvTipoPagamento();
        tipoPagamento = tipoPagamento.buscarPorCodigo("99");
        NfeFormaPagamento nfeFormaPagamento = new NfeFormaPagamento();
        nfeFormaPagamento.setPdvTipoPagamento(tipoPagamento);
        nfeFormaPagamento.setNfeCabecalho(nfe);
        nfeFormaPagamento.setForma("99");
        nfeFormaPagamento.setValor(BigDecimal.ZERO);
        nfe.getListaNfeFormaPagamento().add(nfeFormaPagamento);

    }

    private LocalDestino getLocalDestino(String uf, String ufDestino) {
        return uf.equals(ufDestino) ? LocalDestino.INTERNA : LocalDestino.INTERESTADUAL;
    }

}
