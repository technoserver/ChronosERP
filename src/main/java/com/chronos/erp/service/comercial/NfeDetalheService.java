package com.chronos.erp.service.comercial;

import com.chronos.erp.bo.nfe.NfeUtil;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.service.AbstractService;
import com.chronos.erp.service.ChronosException;

import java.math.BigDecimal;
import java.util.Optional;

public class NfeDetalheService extends AbstractService<NfeDetalhe> {


    public NfeCabecalho addProduto(NfeCabecalho nfe, NfeDetalhe item) throws Exception {

        realizaCalculosItem(item);
        Optional<NfeDetalhe> itemNfeOptional = buscarItemPorProduto(nfe, item.getProduto());
        if (itemNfeOptional.isPresent()) {
            NfeDetalhe nfeDetalhe = itemNfeOptional.get();
            nfeDetalhe.setQuantidadeComercial(nfeDetalhe.getQuantidadeComercial().add(item.getQuantidadeComercial()));
            nfeDetalhe.setQuantidadeTributavel(nfeDetalhe.getQuantidadeTributavel().add(item.getQuantidadeTributavel()));


        } else {
            nfe.getListaNfeDetalhe().add(0, item);

        }
        return nfe;
    }

    public boolean verificarRestricao(NfeDetalhe item) throws Exception {
        this.objeto = item;
        return verificarRestricao();
    }

    private NfeDetalhe realizaCalculosItem(NfeDetalhe item) throws Exception {

        if (item.getProduto().getNcm() == null) {
            throw new ChronosException("Não existe NCM para o produto informado. Operação não realizada.");
        }

        BigDecimal valorVenda = item.getValorUnitarioComercial();
        valorVenda = valorVenda == null ? item.getProduto().getValorVenda() : valorVenda;
        item.setValorUnitarioComercial(valorVenda);
        item.setQuantidadeTributavel(item.getQuantidadeComercial());
        item.setValorUnitarioTributavel(valorVenda);
        item.setEntraTotal(1);
        item.pegarInfoProduto();
        item = definirTributacao(item, item.getNfeCabecalho().getTributOperacaoFiscal(), item.getNfeCabecalho().getDestinatario());

        return item;
    }

    public NfeDetalhe definirTributacao(NfeDetalhe item, TributOperacaoFiscal operacaoFiscal, NfeDestinatario destinatario) throws Exception {
        NfeUtil nfeUtil = new NfeUtil();
        item = nfeUtil.defineTributacao(item, operacaoFiscal, destinatario);
        return item;
    }

    private Optional<NfeDetalhe> buscarItemPorProduto(NfeCabecalho nfe, Produto produto) {
        return nfe.getListaNfeDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }


    @Override
    protected Class<NfeDetalhe> getClazz() {
        return NfeDetalhe.class;
    }


}
