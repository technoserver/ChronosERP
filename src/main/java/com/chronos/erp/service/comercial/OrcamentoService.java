package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.OrcamentoCabecalho;
import com.chronos.erp.modelo.entidades.OrcamentoDetalhe;
import com.chronos.erp.modelo.entidades.OrcamentoFormaPagamento;
import com.chronos.erp.modelo.entidades.VendaCabecalho;
import com.chronos.erp.modelo.enuns.SituacaoOrcamentoPedido;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

public class OrcamentoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<OrcamentoCabecalho> repository;
    @Inject
    private Repository<VendaCabecalho> vendaRepository;

    @Inject
    private VendaService vendaService;


    @Transactional
    public OrcamentoCabecalho salvar(OrcamentoCabecalho orcamento) throws ChronosException {
        String situacao = orcamento.getSituacao();

        if (orcamento.getListaOrcamentoDetalhe().isEmpty()) {
            throw new ChronosException("Itens não informado");
        }

        BigDecimal recebidoAteAgora = BigDecimal.ZERO;

        if (orcamento.getListaFormaPagamento() == null || orcamento.getListaFormaPagamento().isEmpty()) {
            throw new ChronosException("Forma de pagamento não definidas");
        }

        for (OrcamentoFormaPagamento p : orcamento.getListaFormaPagamento()) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getValor());
        }

        if (orcamento.getValorTotal().compareTo(recebidoAteAgora) != 0) {
            throw new ChronosException("Valores informado nos pagamento não estão consolidado !!!");
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

    public void salvarItem(OrcamentoCabecalho orcamento, OrcamentoDetalhe item, BigDecimal desconto, int tipoDesconto) throws ChronosException {


        if (item.getQuantidade() == null || item.getQuantidade().signum() <= 0) {
            throw new ChronosException("Quantidade não informada");
        }

        if (item.getValorUnitario() == null || item.getValorUnitario().signum() <= 0) {
            throw new ChronosException("Quantidade não informada");
        }

        item.calcularSubTotal();

        if (desconto != null && desconto.signum() > 0) {
            if (tipoDesconto == 0) {
                BigDecimal valorDesconto = Biblioteca.calcularValorPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(desconto);
                item.setValorDesconto(valorDesconto);
            } else {
                item.setValorDesconto(desconto);
                BigDecimal taxDesc = Biblioteca.descDinheiroToPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(taxDesc);
            }
        }

        item.calcularValorTotal();


        orcamento.getListaOrcamentoDetalhe().add(item);


        orcamento.calcularValorTotal();

    }

    @Transactional
    public OrcamentoCabecalho conveterEmVenda(OrcamentoCabecalho orcamento) throws ChronosException {

        VendaCabecalho venda = vendaService.gerarVenaDoOrcamento(orcamento);
        venda = vendaRepository.atualizar(venda);
        venda = vendaService.faturarVenda(venda);
        orcamento = venda.getOrcamentoCabecalho();
        orcamento.setSituacao(SituacaoOrcamentoPedido.FATURADO.getCodigo());
        orcamento = repository.saveAndFlush(orcamento);

        return orcamento;
    }


    public void aplicarDesconto(OrcamentoCabecalho orcamento, int tipoDesconto, BigDecimal desconto) throws ChronosException {
        BigDecimal valorDesconto;


        if (orcamento.getValorTotal() == null) {
            throw new ChronosException("Valor total não informando");
        }

        if (orcamento.getListaOrcamentoDetalhe() == null || orcamento.getListaOrcamentoDetalhe().isEmpty()) {
            throw new ChronosException("Não foram informado item(s)");
        }

        if (tipoDesconto == 1) {
            valorDesconto = desconto;
        } else {
            valorDesconto = Biblioteca.calcularValorPercentual(orcamento.getValorTotal(), desconto);

        }

        BigDecimal fator = Biblioteca.divide(valorDesconto, orcamento.getValorSubtotal());
        BigDecimal descAntecipado = orcamento.getListaOrcamentoDetalhe()
                .stream()
                .map(OrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        for (OrcamentoDetalhe i : orcamento.getListaOrcamentoDetalhe()) {
            BigDecimal descItem = Biblioteca.multiplica(fator, i.getValorSubtotal());
            BigDecimal vlrDesc = Biblioteca.soma(Optional.ofNullable(i.getValorDesconto()).orElse(BigDecimal.ZERO), descItem);
            i.setValorDesconto(vlrDesc);
            i.calcularValorTotal();
            BigDecimal vlrTotal = i.getValorTotal();
            BigDecimal txDesc = Biblioteca.calcularFator(i.getValorSubtotal(), vlrTotal);
            i.setTaxaDesconto(txDesc);


        }

        BigDecimal descItens = orcamento.getListaOrcamentoDetalhe()
                .stream()
                .map(OrcamentoDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal sobra = Biblioteca.soma(valorDesconto, descAntecipado);
        sobra = Biblioteca.subtrai(sobra, descItens);

        if (sobra.signum() > 0) {
            OrcamentoDetalhe item = orcamento.getListaOrcamentoDetalhe().get(0);
            BigDecimal vlrDesc = Biblioteca.soma(item.getValorDesconto(), sobra);
            BigDecimal vlrTotal = Biblioteca.subtrai(item.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(item.getValorSubtotal(), vlrTotal);
            item.setValorDesconto(vlrDesc);
            item.setValorTotal(vlrTotal);
        }

        orcamento.calcularValorTotal();
    }

    public void removerDesconto(OrcamentoCabecalho orcamento) {

        orcamento.setValorDesconto(BigDecimal.ZERO);
        orcamento.setTaxaDesconto(BigDecimal.ZERO);

        orcamento.getListaOrcamentoDetalhe().forEach(i -> {
            i.setTaxaDesconto(BigDecimal.ZERO);
            i.setValorDesconto(BigDecimal.ZERO);
            i.setValorTotal(i.getValorSubtotal());
        });

        orcamento.calcularValorTotal();
    }

    @Transactional
    public void excluir(OrcamentoCabecalho orcamento) {

    }
}
