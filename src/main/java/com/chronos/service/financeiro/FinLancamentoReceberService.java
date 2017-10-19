package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.Modulo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 09/09/17.
 */
public class FinLancamentoReceberService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<FinLancamentoReceber> lancamentos;
    @Inject
    private Repository<FinParcelaRecebimento> recebimentoRepository;
    @Inject
    private Repository<VendaCondicoesParcelas> condicoes;
    @Inject
    private Repository<FinParcelaReceber> parcelasRepository;
    @Inject
    private Repository<FinLctoReceberNtFinanceira> parcelaNaturezaRepository;


    public void gerarLancamento(BigDecimal valor, Cliente cliente, String numDocumento, VendaCondicoesPagamento condicoesPagamento, String codModulo, NaturezaFinanceira naturezaFinanceira, Empresa empresa) throws Exception {
        LancamentoReceber lancamento = new LancamentoReceber();
        lancamento.setCliente(cliente);
        lancamento.setCondicoesPagamento(condicoesPagamento);
        lancamento.setDataLancamento(new Date());
        lancamento.setValorTotal(valor);
        lancamento.setNumDocumento(numDocumento);
        lancamento.setCodigoModulo(codModulo);
        lancamento.setEmrpesa(empresa);
        gerarContasReceber(lancamento, naturezaFinanceira);
    }

    public void gerarContasReceber(LancamentoReceber lancamento, NaturezaFinanceira naturezaFinanceira) throws Exception {
        VendaCondicoesPagamento condicoesParcelas = lancamento.getCondicoesPagamento();
        condicoesParcelas.setParcelas(condicoes.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicoesParcelas.getId()));

        FinLancamentoReceber lancamentoReceber = new FinLancamentoReceber();
        lancamentoReceber.setCliente(lancamento.getCliente());
        lancamentoReceber.setFinDocumentoOrigem(new FinDocumentoOrigem(101));
        lancamentoReceber.setQuantidadeParcela(condicoesParcelas.getParcelas().size());
        lancamentoReceber.setValorTotal(lancamento.getValorTotal());
        lancamentoReceber.setValorAReceber(lancamento.getValorTotal());
        lancamentoReceber.setDataLancamento(lancamento.getDataLancamento());
        lancamentoReceber.setNumeroDocumento(lancamento.getNumDocumento());
        lancamentoReceber.setCodigoModuloLcto(lancamento.getCodigoModulo());
        lancamentoReceber.setEmpresa(lancamento.getEmrpesa());

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

    @Transactional
    public void excluirFinanceiro(String numeroDocumeto, Modulo modulo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("finParcelaReceber.finLancamentoReceber.numeroDocumento", numeroDocumeto));
        filtros.add(new Filtro("finParcelaReceber.finLancamentoReceber.codigoModuloLcto", modulo.getCodigo()));
        FinParcelaRecebimento finLan = recebimentoRepository.get(FinParcelaRecebimento.class, filtros, new Object[]{"dataRecebimento",});

        if (finLan != null) {
            throw new Exception(modulo + " já possue recebimentos");
        } else {
            filtros.clear();
            filtros.add(new Filtro("numeroDocumento", numeroDocumeto));
            filtros.add(new Filtro("codigoModuloLcto", modulo.getCodigo()));
            FinLancamentoReceber lancamento = lancamentos.get(FinLancamentoReceber.class, filtros, new Object[]{"numeroDocumento",});
            if (lancamento != null) {
                parcelasRepository.excluir(FinParcelaReceber.class, "finLancamentoReceber.id", lancamento.getId());
                parcelaNaturezaRepository.excluir(FinLctoReceberNtFinanceira.class, "finLancamentoReceber.id", lancamento.getId());
                lancamentos.excluir(lancamento);
            }

        }
    }
}
