package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.repository.Filtro;
import com.chronos.repository.FinLancamentoReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
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


    @Transactional
    public void gerarContasReceber(PdvVendaCabecalho venda, List<FinParcelaReceber> parcelas) {


        String identificador = "E" + venda.getEmpresa().getId()
                + "M" + Modulo.PDV.getCodigo()
                + "V" + venda.getId()
                + "C" + venda.getCliente().getId()
                + "Q" + parcelas.size();


        FinLancamentoReceber lancamentoReceber = new FinLancamentoReceber();
        lancamentoReceber.setCliente(venda.getCliente());
        lancamentoReceber.setFinDocumentoOrigem(new FinDocumentoOrigem(101));

        lancamentoReceber.setValorTotal(venda.getValorTotal());
        lancamentoReceber.setValorAReceber(venda.getValorTotal());
        lancamentoReceber.setDataLancamento(venda.getDataHoraVenda());
        lancamentoReceber.setNumeroDocumento(identificador);
        lancamentoReceber.setCodigoModuloLcto(Modulo.PDV.getCodigo());
        lancamentoReceber.setEmpresa(venda.getEmpresa());


        Date primeiroVencimento = parcelas.stream().map(p -> p.getDataVencimento()).min(Date::compareTo).get();

        // pega o primeiro vencimento
        lancamentoReceber.setPrimeiroVencimento(primeiroVencimento);
        lancamentoReceber.setIntervaloEntreParcelas(null);
        lancamentoReceber.setListaFinParcelaReceber(new ArrayList<>());
        lancamentoReceber.setListaFinLctoReceberNtFinanceira(new HashSet<>());

        parcelas.forEach(p -> p.setFinLancamentoReceber(lancamentoReceber));
        lancamentoReceber.setQuantidadeParcela(parcelas.size());
        lancamentoReceber.setListaFinParcelaReceber(parcelas);

        geraNaturezaFinanceira(lancamentoReceber, Constantes.FIN.NATUREZA_VENDA);
        lancamentos.salvar(lancamentoReceber);
    }

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


        String identificador = "E" + lancamento.getEmrpesa().getId()
                + "M" + lancamento.getCodigoModulo()
                + "V" + lancamento.getId()
                + "C" + lancamento.getCliente().getId()
                + "Q";

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


        if (condicao.getVistaPrazo().equals("1")) {

            List<FinParcelaReceber> finParcelaRecebers = gerarParcelas(lancamentoReceber.getValorTotal(), lancamento.getDataLancamento(), condicao);
            finParcelaRecebers.forEach(p -> p.setFinLancamentoReceber(lancamentoReceber));
            lancamentoReceber.setQuantidadeParcela(finParcelaRecebers.size());
            lancamentoReceber.setListaFinParcelaReceber(finParcelaRecebers);

        } else {

            lancamentoReceber.setQuantidadeParcela(1);
            parcelaReceber = new FinParcelaReceber();
            parcelaReceber.setFinLancamentoReceber(lancamentoReceber);
            parcelaReceber.setNumeroParcela(1);
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


    public List<FinParcelaReceber> gerarParcelas(BigDecimal valor, Date dataEmissao, VendaCondicoesPagamento condicao) throws ChronosException {

        List<VendaCondicoesParcelas> parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicao.getId());


        int number = 1;
        List<FinParcelaReceber> parcelasReceber = new ArrayList<>();
        for (VendaCondicoesParcelas parcela : parcelas) {
            FinParcelaReceber parcelaReceber = new FinParcelaReceber();

            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setFinStatusParcela(new FinStatusParcela(1));
            parcelaReceber.setContaCaixa(condicao.getTipoRecebimento().getContaCaixa());
            parcelaReceber.setDataEmissao(dataEmissao);
            parcelaReceber.setDataVencimento(Biblioteca.addDay(dataEmissao, parcela.getDias()));
            parcelaReceber.setValor(Biblioteca.calcularValorPercentual(valor, parcela.getTaxa()));

            parcelasReceber.add(parcelaReceber);
        }

        return parcelasReceber;
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
        filtros.add(new Filtro("finParcelaReceber.finLancamentoReceber.numeroDocumento", Filtro.LIKE, numeroDocumeto));
        filtros.add(new Filtro("finParcelaReceber.finLancamentoReceber.codigoModuloLcto", modulo.getCodigo()));
        FinParcelaRecebimento finLan = recebimentoRepository.get(FinParcelaRecebimento.class, filtros, new Object[]{"dataRecebimento",});

        if (finLan != null) {
            throw new Exception(modulo + " já possue recebimentos");
        } else {
            filtros.clear();
            filtros.add(new Filtro("numeroDocumento", Filtro.LIKE, numeroDocumeto));
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
