package com.chronos.service.comercial;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.SituacaoVenda;
import com.chronos.repository.Repository;
import com.chronos.service.AbstractService;
import com.chronos.service.ChronosException;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VendaConsignadaService extends AbstractService<VendaConsignadaCabecalho> {

    @Inject
    private Repository<VendaConsignadaCabecalho> repository;
    @Inject
    private VendaService vendaService;

    public VendaConsignadaCabecalho addItem(VendaConsignadaCabecalho venda, VendaConsignadaDetalhe item, String tipoDesconto, BigDecimal desconto) throws ChronosException {

        item.calcularValorSubtotal();

        if (desconto.signum() > 0) {
            if (tipoDesconto.equals("%")) {
                BigDecimal valorDesconto = Biblioteca.calcularValorPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(desconto);
                item.setValorDesconto(valorDesconto);
                item.calcularValorTotal();
            } else {
                item.setValorDesconto(desconto);
                item.calcularValorTotal();
                BigDecimal taxDesc = Biblioteca.calcularPercentual(item.getValorSubtotal(), item.getValorTotal());
                item.setTaxaDesconto(taxDesc);
            }
        }


        item.calcularValorTotal();

        venda.getListaVendaConsignadaDetalhe().add(item);


        venda.calcularValorTotal();
        return venda;
    }

    @Override
    protected Class<VendaConsignadaCabecalho> getClazz() {
        return VendaConsignadaCabecalho.class;
    }

    @Transactional
    public VendaConsignadaCabecalho salvar(VendaConsignadaCabecalho venda) throws ChronosException {
        validar(venda);
        venda = repository.atualizar(venda);
        return venda;
    }

    @Transactional
    public void gerarVenda(VendaConsignadaCabecalho venda) {

    }

    @Transactional
    public void encerrar(VendaConsignadaCabecalho venda) throws ChronosException {


        for (VendaConsignadaDetalhe item : venda.getListaVendaConsignadaDetalhe()) {

            if (item.getQuantidadeVendida() == null) {
                item.setQuantidadeVendida(BigDecimal.ZERO);
            }

            if (item.getQuantidadeDevolvida() == null) {
                item.setQuantidadeDevolvida(BigDecimal.ZERO);
            }

            if (item.getQuantidade().compareTo(item.getQuantidadeVendida()) < 0) {
                throw new ChronosException("Quantidade vendida não pode ser maior do que a quantidade");
            }

            if (item.getQuantidade().compareTo(item.getQuantidadeDevolvida()) < 0) {
                throw new ChronosException("Quantidade devolvida não pode ser maior do que a quantidade");
            }

            BigDecimal soma = item.getQuantidadeVendida().add(item.getQuantidadeDevolvida());

            if (item.getQuantidade().compareTo(soma) < 0) {
                throw new ChronosException("A soma da quantidade devolvida +  quantidade vendida não pode ser maior que a quantidade");
            }
        }

        List<VendaConsignadaDetalhe> itensVendido = venda.getListaVendaConsignadaDetalhe().stream().filter(i -> i.getQuantidadeVendida().signum() > 0).collect(Collectors.toList());

        if (itensVendido.isEmpty()) {
            venda.getListaVendaConsignadaDetalhe().forEach(i -> {
                i.setStatus(StatusConsignacao.RECOLHIDO);
                i.setQuantidadeDevolvida(i.getQuantidade());
            });
            venda.setStatus(StatusConsignacao.RECOLHIDO);
        } else {

            VendaCabecalho vendaCabecalho = criarVenda(venda, itensVendido);

            vendaCabecalho = vendaService.faturarVenda(vendaCabecalho);

            venda.setIdvendaCebecalho(vendaCabecalho.getId());
            venda.setStatus(StatusConsignacao.ENCERRADO);

        }

        repository.atualizar(venda);
    }

    private VendaCabecalho criarVenda(VendaConsignadaCabecalho vendaConsignada, List<VendaConsignadaDetalhe> itens) {

        List<VendaDetalhe> itensVendenda = new ArrayList<>();

        VendaCabecalho vendaCabecalho = new VendaCabecalho();
        vendaCabecalho.setSituacao(SituacaoVenda.Encerrado.getCodigo());
        vendaCabecalho.setCliente(vendaConsignada.getCliente());
        vendaCabecalho.setVendedor(vendaConsignada.getVendedor());
        vendaCabecalho.setEmpresa(vendaConsignada.getEmpresa());
        vendaCabecalho.setTaxaComissao(vendaConsignada.getTaxaComissao());
        vendaCabecalho.setDataSaida(new Date());
        vendaCabecalho.setLocalEntrega(vendaConsignada.getLocalEntrega());
        vendaCabecalho.setCondicoesPagamento(vendaConsignada.getCondicoesPagamento());
        vendaCabecalho.setListaVendaDetalhe(itensVendenda);

        for (VendaConsignadaDetalhe item : itens) {

            if (item.getQuantidade().compareTo(item.getQuantidadeVendida()) == 0) {
                item.setStatus(StatusConsignacao.VENDIDO);
            } else {
                item.setStatus(StatusConsignacao.RECOLHIDO_PARCIALMENTE);
            }

            VendaDetalhe itemVenda = new VendaDetalhe();
            itemVenda.setVendaCabecalho(vendaCabecalho);
            itemVenda.setValorUnitario(item.getValorUnitario());
            itemVenda.setQuantidade(item.getQuantidadeVendida());
            itemVenda.setTaxaDesconto(item.getTaxaDesconto());
            itemVenda.calcularDesconto();
            itemVenda.calcularValorTotal();
            itemVenda.setProduto(item.getProduto());

            vendaCabecalho.getListaVendaDetalhe().add(itemVenda);
            vendaCabecalho.calcularValorTotal();


        }

        return vendaCabecalho;
    }

    private void validar(VendaConsignadaCabecalho venda) throws ChronosException {
        if (venda.getListaVendaConsignadaDetalhe() == null || venda.getListaVendaConsignadaDetalhe().isEmpty()) {
            throw new ChronosException("Não foran informado itens !!!");
        }
    }
}
