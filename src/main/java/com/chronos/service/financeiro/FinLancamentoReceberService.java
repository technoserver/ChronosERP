package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.repository.Filtro;
import com.chronos.repository.FinLancamentoReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
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
    private Repository<VendaCondicoesParcelas> parcelasRepository;
    @Inject
    private Repository<FinLctoReceberNtFinanceira> parcelaNaturezaRepository;
    @Inject
    private Repository<FinParcelaReceber> parcelaReceberRepository;


    public void gerarLancamento(int id, BigDecimal valor, Cliente cliente, VendaCondicoesPagamento condicoesPagamento, String codModulo, NaturezaFinanceira naturezaFinanceira, Empresa empresa) throws ChronosException {
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
    public void gerarContasReceber(LancamentoReceber lancamento, NaturezaFinanceira naturezaFinanceira) throws ChronosException {
        VendaCondicoesPagamento condicao = lancamento.getCondicoesPagamento();
        ContaCaixa conta = condicao.getTipoRecebimento().getContaCaixa();

        List<VendaCondicoesParcelas> parcelas = new ArrayList<>();
        int qtdParcelas = 1;
        if (condicao.getVistaPrazo().equals("1")) {
            parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicao.getId());
            qtdParcelas = parcelas.size();
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

        if (condicao.getVistaPrazo().equals("1")) {
            lancamentoReceber.setQuantidadeParcela(qtdParcelas);
            for (VendaCondicoesParcelas parcela : parcelas) {
                parcelaReceber = new FinParcelaReceber();
                parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
                parcelaReceber.setNumeroParcela(number++);
                parcelaReceber.setFinStatusParcela(new FinStatusParcela(1));
                parcelaReceber.setContaCaixa(condicao.getTipoRecebimento().getContaCaixa());
                parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
                parcelaReceber.setDataVencimento(Biblioteca.addDay(lancamento.getDataLancamento(), parcela.getDias()));
                parcelaReceber.setValor(Biblioteca.calcularValorPercentual(lancamento.getValorTotal(), parcela.getTaxa()));

                lancamentoReceber.getListaFinParcelaReceber().add(parcelaReceber);
            }
        } else {

            lancamentoReceber.setQuantidadeParcela(1);
            parcelaReceber = new FinParcelaReceber();
            parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setFinStatusParcela(new FinStatusParcela(2));
            parcelaReceber.setContaCaixa(conta);
            parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
            parcelaReceber.setDataVencimento(new Date());
            parcelaReceber.setValor(lancamento.getValorTotal());

            FinParcelaRecebimento recebimento = new FinParcelaRecebimento();

            recebimento.setFinParcelaReceber(parcelaReceber);
            recebimento.setContaCaixa(conta);
            recebimento.setDataRecebimento(new Date());
            recebimento.setFinTipoRecebimento(condicao.getTipoRecebimento());
            recebimento.setTaxaDesconto(BigDecimal.ZERO);
            recebimento.setValorDesconto(BigDecimal.ZERO);
            recebimento.setTaxaJuro(conta.getTaxaJuro());
            recebimento.setValorJuro(BigDecimal.ZERO);
            recebimento.setTaxaMulta(conta.getTaxaMulta());
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
                parcelaReceberRepository.excluir(FinParcelaReceber.class, "finLancamentoReceber.id", lancamento.getId());
                parcelaNaturezaRepository.excluir(FinLctoReceberNtFinanceira.class, "finLancamentoReceber.id", lancamento.getId());
                lancamentos.excluir(lancamento);
            }

        }
    }
}
