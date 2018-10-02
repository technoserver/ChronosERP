package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.repository.Filtro;
import com.chronos.repository.FinLancamentoReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 09/09/17.
 */
public class FinLancamentoReceberService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FinLancamentoReceberRepository lancamentos;


    @Inject
    private Repository<FinParcelaRecebimento> recebimentoRepository;
    @Inject
    private Repository<VendaCondicoesParcelas> condicoes;
    @Inject
    private Repository<FinParcelaReceber> parcelasRepository;
    @Inject
    private Repository<FinLctoReceberNtFinanceira> parcelaNaturezaRepository;


    public void gerarLancamento(int id, BigDecimal valor, Cliente cliente, VendaCondicoesPagamento condicoesPagamento, String codModulo, NaturezaFinanceira naturezaFinanceira, Empresa empresa) {
        LancamentoReceber lancamento = new LancamentoReceber();
        lancamento.setCliente(cliente);
        lancamento.setCondicoesPagamento(condicoesPagamento);
        lancamento.setDataLancamento(new Date());
        lancamento.setValorTotal(valor);
        lancamento.setId(id);
        lancamento.setCodigoModulo(codModulo);
        lancamento.setEmrpesa(empresa);
        gerarContasReceber(lancamento, naturezaFinanceira);
    }


    @Transactional
    public void gerarContasReceber(LancamentoReceber lancamento, NaturezaFinanceira naturezaFinanceira) {
        VendaCondicoesPagamento condicoesParcelas = lancamento.getCondicoesPagamento();
        int qtdParcelas = 1;
        if (condicoesParcelas.getVistaPrazo().equals("1")) {
            condicoesParcelas.setParcelas(condicoes.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicoesParcelas.getId()));
            qtdParcelas = condicoesParcelas.getParcelas().size();
        }

        String identificador = "E" + lancamento.getEmrpesa().getId()
                + "M" + lancamento.getCodigoModulo()
                + "V" + lancamento.getId()
                + "C" + lancamento.getCliente().getId()
                + "Q" + qtdParcelas;

        FinLancamentoReceber lancamentoReceber = new FinLancamentoReceber();
        lancamentoReceber.setCliente(lancamento.getCliente());
        lancamentoReceber.setFinDocumentoOrigem(new FinDocumentoOrigem(101));

        lancamentoReceber.setValorTotal(lancamento.getValorTotal());
        lancamentoReceber.setValorAReceber(lancamento.getValorTotal());
        lancamentoReceber.setDataLancamento(lancamento.getDataLancamento());
        lancamentoReceber.setNumeroDocumento(identificador);
        lancamentoReceber.setCodigoModuloLcto(lancamento.getCodigoModulo());
        lancamentoReceber.setEmpresa(lancamento.getEmrpesa());

        // pega o primeiro vencimento
        lancamentoReceber.setPrimeiroVencimento(lancamento.getDataLancamento());
        lancamentoReceber.setIntervaloEntreParcelas(null);
        lancamentoReceber.setListaFinParcelaReceber(new ArrayList<>());
        lancamentoReceber.setListaFinLctoReceberNtFinanceira(new HashSet<>());

        FinParcelaReceber parcelaReceber;
        int number = 1;

        if (condicoesParcelas.getVistaPrazo().equals("1")) {
            lancamentoReceber.setQuantidadeParcela(condicoesParcelas.getParcelas().size());
            for (VendaCondicoesParcelas parcelas : condicoesParcelas.getParcelas()) {
                parcelaReceber = new FinParcelaReceber();
                parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
                parcelaReceber.setNumeroParcela(number++);
                parcelaReceber.setFinStatusParcela(new FinStatusParcela(1));
                parcelaReceber.setContaCaixa(condicoesParcelas.getTipoRecebimento().getContaCaixa());
                parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
                parcelaReceber.setDataVencimento(Biblioteca.addDay(lancamento.getDataLancamento(), parcelas.getDias()));
                parcelaReceber.setValor(Biblioteca.calcularValorPercentual(lancamento.getValorTotal(), parcelas.getTaxa()));

                lancamentoReceber.getListaFinParcelaReceber().add(parcelaReceber);
            }
        } else {

            lancamentoReceber.setQuantidadeParcela(1);
            parcelaReceber = new FinParcelaReceber();
            parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setFinStatusParcela(new FinStatusParcela(2));
            parcelaReceber.setContaCaixa(condicoesParcelas.getTipoRecebimento().getContaCaixa());
            parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
            parcelaReceber.setDataVencimento(new Date());
            parcelaReceber.setValor(lancamento.getValorTotal());

            FinParcelaRecebimento recebimento = new FinParcelaRecebimento();

            recebimento.setFinParcelaReceber(parcelaReceber);
            recebimento.setContaCaixa(condicoesParcelas.getTipoRecebimento().getContaCaixa());
            recebimento.setDataRecebimento(new Date());
            recebimento.setFinTipoRecebimento(condicoesParcelas.getTipoRecebimento());
            recebimento.setTaxaDesconto(BigDecimal.ZERO);
            recebimento.setValorDesconto(BigDecimal.ZERO);
            recebimento.setTaxaJuro(BigDecimal.ZERO);
            recebimento.setValorJuro(BigDecimal.ZERO);
            recebimento.setTaxaMulta(BigDecimal.ZERO);
            recebimento.setValorMulta(BigDecimal.ZERO);
            recebimento.setValorRecebido(lancamento.getValorTotal());

            parcelaReceber.setListaFinParcelaRecebimento(new HashSet<>());
            parcelaReceber.getListaFinParcelaRecebimento().add(recebimento);

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
