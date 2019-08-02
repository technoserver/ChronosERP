package com.chronos.service.comercial;

import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaOrcamentoCabecalho;
import com.chronos.modelo.entidades.VendaOrcamentoDetalhe;
import com.chronos.modelo.enuns.SituacaoOrcamentoPedido;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

public class VendaOrcamentoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<VendaOrcamentoCabecalho> repository;
    @Inject
    private Repository<VendaCabecalho> vendaRepository;

    @Inject
    private VendaService vendaService;


    @Transactional
    public VendaOrcamentoCabecalho salvar(VendaOrcamentoCabecalho orcamento) throws ChronosException {
        String situacao = orcamento.getSituacao();

        if (orcamento.getListaVendaOrcamentoDetalhe().isEmpty()) {
            throw new ChronosException("Itens não informado");
        }

        if (!situacao.equals(SituacaoOrcamentoPedido.PENDENTE.getCodigo())) {
            String mensagem = "Este orçamento não pode ser alterado.\n";
            if (situacao.equals(SituacaoOrcamentoPedido.CANCELADO.getCodigo())) {
                mensagem += "Situação: CANCELADO";
            }
            if (situacao.equals(SituacaoOrcamentoPedido.APROVADO.getCodigo())) {
                mensagem += "Situação: APROVADO";
            }
            throw new ChronosException(mensagem);
        }
        Integer id = orcamento.getId();
        orcamento = repository.atualizar(orcamento);

        if (id == null) {
            String codigo = orcamento.getTipo().equals("P") ? "#PE" : "#OE";
            codigo += StringUtils.leftPad(orcamento.getId().toString(), 3, "0");
            orcamento.setCodigo(codigo);
            orcamento = repository.atualizar(orcamento);
        }


        return orcamento;
    }

    @Transactional
    public VendaOrcamentoCabecalho conveterEmVenda(VendaOrcamentoCabecalho orcamento) throws ChronosException {

        VendaCabecalho venda = vendaService.gerarVenaDoOrcamento(orcamento);
        venda = vendaRepository.atualizar(venda);
        venda = vendaService.faturarVenda(venda);
        orcamento = venda.getVendaOrcamentoCabecalho();
        orcamento.setSituacao(SituacaoOrcamentoPedido.FATURADO.getCodigo());
        orcamento = repository.saveAndFlush(orcamento);

        return orcamento;
    }


    public void aplicarDesconto(VendaOrcamentoCabecalho orcamento, int tipoDesconto, BigDecimal desconto) throws ChronosException {
        BigDecimal valorDesconto;


        if (orcamento.getValorTotal() == null) {
            throw new ChronosException("Valor total não informando");
        }

        if (orcamento.getListaVendaOrcamentoDetalhe() == null || orcamento.getListaVendaOrcamentoDetalhe().isEmpty()) {
            throw new ChronosException("Não foram informado item(s)");
        }

        if (tipoDesconto == 1) {
            valorDesconto = desconto;
        } else {
            valorDesconto = Biblioteca.calcularValorPercentual(orcamento.getValorTotal(), desconto);

        }

        BigDecimal fator = Biblioteca.divide(valorDesconto, orcamento.getValorSubtotal());
        BigDecimal descAntecipado = orcamento.getListaVendaOrcamentoDetalhe()
                .stream()
                .map(VendaOrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        for (VendaOrcamentoDetalhe i : orcamento.getListaVendaOrcamentoDetalhe()) {
            BigDecimal descItem = Biblioteca.multiplica(fator, i.getValorSubtotal());
            BigDecimal vlrDesc = Biblioteca.soma(Optional.ofNullable(i.getValorDesconto()).orElse(BigDecimal.ZERO), descItem);
            BigDecimal vlrTotal = Biblioteca.subtrai(i.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(i.getValorSubtotal(), vlrTotal);
            i.setValorDesconto(vlrDesc);
            i.setValorTotal(vlrTotal);
            i.setTaxaDesconto(txDesc);

        }

        BigDecimal descItens = orcamento.getListaVendaOrcamentoDetalhe()
                .stream()
                .map(VendaOrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal sobra = Biblioteca.soma(valorDesconto, descAntecipado);
        sobra = Biblioteca.subtrai(sobra, descItens);

        if (sobra.signum() > 0) {
            VendaOrcamentoDetalhe item = orcamento.getListaVendaOrcamentoDetalhe().get(0);
            BigDecimal vlrDesc = Biblioteca.soma(item.getValorDesconto(), sobra);
            BigDecimal vlrTotal = Biblioteca.subtrai(item.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(item.getValorSubtotal(), vlrTotal);
            item.setValorDesconto(vlrDesc);
            item.setValorTotal(vlrTotal);
        }

        orcamento.calcularValorTotal();
    }

    public void removerDesconto(VendaOrcamentoCabecalho orcamento) {

        orcamento.setValorDesconto(BigDecimal.ZERO);
        orcamento.setTaxaDesconto(BigDecimal.ZERO);

        orcamento.getListaVendaOrcamentoDetalhe().forEach(i -> {
            i.setTaxaDesconto(BigDecimal.ZERO);
            i.setValorDesconto(BigDecimal.ZERO);
            i.setValorTotal(i.getValorSubtotal());
        });

        orcamento.calcularValorTotal();
    }

    @Transactional
    public void excluir(VendaOrcamentoCabecalho orcamento) {

    }
}
