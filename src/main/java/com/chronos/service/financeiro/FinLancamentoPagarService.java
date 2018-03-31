package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoPagar;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by john on 30/03/18.
 */

public class FinLancamentoPagarService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<FinLancamentoPagar> repository;

    @Inject
    private Repository<VendaCondicoesParcelas> condicoesParcelasRepository;


    public void gerarLancamento(NfeCabecalho nfe, ContaCaixa contaCaixa, NaturezaFinanceira naturezaFinanceira) throws Exception {

        LancamentoPagar lancamento = new LancamentoPagar();

        nfe.getListaDuplicata().forEach(d -> {
            d.setContaCaixa(contaCaixa);
        });

        lancamento.setFornecedor(nfe.getFornecedor());
        lancamento.setDataLancamento(new Date());
        lancamento.setValorTotal(nfe.getValorTotal());
        lancamento.setId(nfe.getId());
        lancamento.setCodigoModulo(Constantes.MODULO.NFE);
        lancamento.setEmpresa(nfe.getEmpresa());
        lancamento.setDuplicatas(nfe.getListaDuplicata());

        gerarContasReceber(lancamento, naturezaFinanceira);

    }

    public void gerarLancamento(int id, BigDecimal valor, Fornecedor fornecedor, VendaCondicoesPagamento condicoesPagamento, String codModulo, NaturezaFinanceira naturezaFinanceira, Empresa empresa) throws Exception {
        LancamentoPagar lancamento = new LancamentoPagar();
        lancamento.setFornecedor(fornecedor);
        lancamento.setCondicoesPagamento(condicoesPagamento);
        lancamento.setDataLancamento(new Date());
        lancamento.setValorTotal(valor);
        lancamento.setId(id);
        lancamento.setCodigoModulo(codModulo);
        lancamento.setEmpresa(empresa);
        gerarContasReceber(lancamento, naturezaFinanceira);
    }


    @Transactional
    public void gerarContasReceber(LancamentoPagar lancamento, NaturezaFinanceira naturezaFinanceira) throws Exception {


        FinLancamentoPagar lancamentoPagar = new FinLancamentoPagar();
        lancamentoPagar.setFornecedor(lancamento.getFornecedor());
        lancamentoPagar.setFinDocumentoOrigem(new FinDocumentoOrigem(101));

        lancamentoPagar.setValorTotal(lancamento.getValorTotal());
        lancamentoPagar.setValorAPagar(lancamento.getValorTotal());
        lancamentoPagar.setDataLancamento(lancamento.getDataLancamento());
        lancamentoPagar.setCodigoModuloLcto(lancamento.getCodigoModulo());
        lancamentoPagar.setEmpresa(lancamento.getEmpresa());


        lancamentoPagar.setIntervaloEntreParcelas(null);
        lancamentoPagar.setListaFinParcelaPagar(new ArrayList<>());
        lancamentoPagar.setListaFinLctoPagarNtFinanceira(new HashSet<>());

        if (lancamento.getCondicoesPagamento() != null) {
            lancamentoPagar.setListaFinParcelaPagar(gerarParcelas(lancamentoPagar, lancamento.getCondicoesPagamento()));
        } else {
            lancamentoPagar.setListaFinParcelaPagar(gerarParcelas(lancamentoPagar, lancamento.getDuplicatas()));
        }

        String identificador = "E" + lancamento.getEmpresa().getId()
                + "M" + lancamento.getCodigoModulo()
                + "C" + lancamento.getId()
                + "F" + lancamento.getFornecedor().getId()
                + "Q" + lancamentoPagar.getListaFinParcelaPagar().size();


        repository.excluir(FinLancamentoPagar.class, "numeroDocumento", identificador);

        // pega o primeiro vencimento
        FinParcelaPagar parcela = lancamentoPagar
                .getListaFinParcelaPagar()
                .stream()
                .filter(p -> p.getNumeroParcela() == 1)
                .findFirst().get();
        lancamentoPagar.setPrimeiroVencimento(parcela.getDataVencimento());
        lancamentoPagar.setNumeroDocumento(identificador);
        geraNaturezaFinanceira(lancamentoPagar, naturezaFinanceira);
        repository.salvar(lancamentoPagar);

    }

    private List<FinParcelaPagar> gerarParcelas(FinLancamentoPagar lancamentoPagar, Set<NfeDuplicata> duplicatas) {
        List<FinParcelaPagar> parcelas = new ArrayList<>();

        int number = 1;
        for (NfeDuplicata d : duplicatas) {
            FinParcelaPagar parcelaPagar = new FinParcelaPagar();
            parcelaPagar.setFinLancamentoPagar(lancamentoPagar);
            parcelaPagar.setNumeroParcela(number++);
            parcelaPagar.setFinStatusParcela(new FinStatusParcela(1));
            parcelaPagar.setContaCaixa(d.getContaCaixa());
            parcelaPagar.setDataEmissao(d.getNfeCabecalho().getDataHoraEmissao());
            parcelaPagar.setDataVencimento(d.getDataVencimento());
            parcelaPagar.setValor(d.getValor());

            parcelas.add(parcelaPagar);
        }

        return parcelas;
    }

    private List<FinParcelaPagar> gerarParcelas(FinLancamentoPagar lancamentoPagar, VendaCondicoesPagamento condicoesPagamento) {
        List<FinParcelaPagar> parcelas = new ArrayList<>();

        if (condicoesPagamento.getVistaPrazo().equals("1")) {
            condicoesPagamento.setParcelas(condicoesParcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicoesPagamento.getId()));
        }

        FinParcelaPagar parcelaPagar;
        int number = 1;

        for (VendaCondicoesParcelas p : condicoesPagamento.getParcelas()) {
            parcelaPagar = new FinParcelaPagar();
            parcelaPagar.setFinLancamentoPagar(lancamentoPagar);
            parcelaPagar.setNumeroParcela(number++);
            parcelaPagar.setFinStatusParcela(new FinStatusParcela(1));
            parcelaPagar.setContaCaixa(condicoesPagamento.getTipoRecebimento().getContaCaixa());
            parcelaPagar.setDataEmissao(lancamentoPagar.getDataLancamento());
            parcelaPagar.setDataVencimento(Biblioteca.addDay(lancamentoPagar.getDataLancamento(), p.getDias()));
            parcelaPagar.setValor(Biblioteca.calcTaxa(lancamentoPagar.getValorTotal(), p.getTaxa()));

            parcelas.add(parcelaPagar);
        }

        return parcelas;
    }

    private void geraNaturezaFinanceira(FinLancamentoPagar finLancamentoPagar, NaturezaFinanceira naturezaFinanceira) {
        FinLctoPagarNtFinanceira finLctoPagarNtFinanceira = new FinLctoPagarNtFinanceira();
        finLctoPagarNtFinanceira.setFinLancamentoPagar(finLancamentoPagar);
        finLctoPagarNtFinanceira.setNaturezaFinanceira(naturezaFinanceira);
        finLctoPagarNtFinanceira.setDataInclusao(new Date());
        finLctoPagarNtFinanceira.setValor(finLancamentoPagar.getValorAPagar());

        finLancamentoPagar.getListaFinLctoPagarNtFinanceira().add(finLctoPagarNtFinanceira);
    }
}
