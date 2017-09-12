package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by john on 09/09/17.
 */
public class FinLancamentoReceberService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<FinLancamentoReceber> lancamentos;
    @Inject
    private Repository<VendaCondicoesParcelas> parcelas;

    public void gerarContasReceber(LancamentoReceber lancamento, NaturezaFinanceira naturezaFinanceira) throws Exception {
        VendaCondicoesPagamento condicoesParcelas = lancamento.getCondicoesPagamento();
        condicoesParcelas.setParcelas(parcelas.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicoesParcelas.getId()));

        FinLancamentoReceber lancamentoReceber = new FinLancamentoReceber();
        lancamentoReceber.setCliente(lancamento.getCliente());
        lancamentoReceber.setFinDocumentoOrigem(new FinDocumentoOrigem(101));
        lancamentoReceber.setQuantidadeParcela(condicoesParcelas.getParcelas().size());
        lancamentoReceber.setValorTotal(lancamento.getValorTotal());
        lancamentoReceber.setValorAReceber(lancamento.getValorTotal());
        lancamentoReceber.setDataLancamento(lancamento.getDataLancamento());
        lancamentoReceber.setNumeroDocumento(lancamento.getNumDocumento());
        lancamentoReceber.setCodigoModuloLcto(lancamento.getCodigoModulo());

        // pega o primeiro vencimento
        lancamentoReceber.setPrimeiroVencimento(lancamento.getDataLancamento());
        lancamentoReceber.setIntervaloEntreParcelas(null);
        lancamentoReceber.setListaFinParcelaReceber(new HashSet<>());
        lancamentoReceber.setListaFinLctoReceberNtFinanceira(new HashSet<>());

        FinParcelaReceber parcelaReceber;
        int number = 1;
        for (VendaCondicoesParcelas parcelas : condicoesParcelas.getParcelas()) {
            parcelaReceber = new FinParcelaReceber();
            parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setFinStatusParcela(new FinStatusParcela(1));
            parcelaReceber.setContaCaixa(condicoesParcelas.getTipoRecebimento().getContaCaixa());
            parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
            parcelaReceber.setDataVencimento(Biblioteca.addDay(lancamento.getDataLancamento(), parcelas.getDias()));
            parcelaReceber.setValor(Biblioteca.calcTaxa(lancamento.getValorTotal(), parcelas.getTaxa()));

            lancamentoReceber.getListaFinParcelaReceber().add(parcelaReceber);
        }
        geraNaturezaFinanceira(lancamentoReceber, naturezaFinanceira);
        lancamentos.salvar(lancamentoReceber);

    }

    private void geraNaturezaFinanceira(FinLancamentoReceber lancamentoReceber, NaturezaFinanceira naturezaFinanceira) {
        FinLctoReceberNtFinanceira finLctoReceberNaturezaFinancaeira = new FinLctoReceberNtFinanceira();
        finLctoReceberNaturezaFinancaeira.setFinLancamentoReceber(lancamentoReceber);
        finLctoReceberNaturezaFinancaeira.setNaturezaFinanceira(naturezaFinanceira);
        finLctoReceberNaturezaFinancaeira.setDataInclusao(new Date());
        finLctoReceberNaturezaFinancaeira.setValor(lancamentoReceber.getValorAReceber());

        lancamentoReceber.getListaFinLctoReceberNtFinanceira().add(finLctoReceberNaturezaFinancaeira);
    }
}
