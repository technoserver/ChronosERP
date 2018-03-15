package com.chronos.service.financeiro;

import com.chronos.dto.LancamentoReceber;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.repository.Filtro;
import com.chronos.repository.FinLancamentoReceberRepository;
import com.chronos.repository.Repository;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;
import org.springframework.util.StringUtils;

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
    private Repository<FinLancamentoReceberCartao> lancamentoReceberCartaoRepository;

    @Inject
    private Repository<FinParcelaRecebimento> recebimentoRepository;
    @Inject
    private Repository<VendaCondicoesParcelas> condicoes;
    @Inject
    private Repository<FinParcelaReceber> parcelasRepository;
    @Inject
    private Repository<FinLctoReceberNtFinanceira> parcelaNaturezaRepository;


    public void gerarLancamento(int id , BigDecimal valor, Cliente cliente, VendaCondicoesPagamento condicoesPagamento, String codModulo, NaturezaFinanceira naturezaFinanceira, Empresa empresa) throws Exception {
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
    public void gerarContasReceber(LancamentoReceber lancamento, NaturezaFinanceira naturezaFinanceira) throws Exception {
        VendaCondicoesPagamento condicoesParcelas = lancamento.getCondicoesPagamento();
        if (condicoesParcelas.getVistaPrazo().equals("1")) {
            condicoesParcelas.setParcelas(condicoes.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", condicoesParcelas.getId()));
        }
        String identificador = "E" + lancamento.getEmrpesa().getId()
                + "M" + lancamento.getCodigoModulo()
                + "V" + lancamento.getId()
                + "C" + lancamento.getCliente().getId()
                + "Q" + (condicoesParcelas.getParcelas()!=null? condicoesParcelas.getParcelas().size():1);

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
                parcelaReceber.setValor(Biblioteca.calcTaxa(lancamento.getValorTotal(), parcelas.getTaxa()));

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

    public void gerarLancamentoCartao(int id, BigDecimal valor, OperadoraCartao operadoraCartao, int qtdParcelas, String codModulo, Empresa empresa, String identificador) {

        String numDoc = "E" + empresa.getId()
                + "M" + codModulo
                + "V" + id
                + "O" + operadoraCartao.getId()
                + "Q" + qtdParcelas;

        if (!StringUtils.isEmpty(identificador)) {
            numDoc += "NSU" + identificador;
        }

        FinLancamentoReceberCartao lancamento = new FinLancamentoReceberCartao();

        lancamento.setValorBruto(valor);
        lancamento.setTaxaAplicada(operadoraCartao.getTaxaAdm());
        BigDecimal valorEcargos = valor.multiply(operadoraCartao.getTaxaAdm()).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_DOWN);
        lancamento.setValorEncargos(valorEcargos);
        lancamento.setValorLiquido(valor.subtract(valorEcargos));

        lancamento.setDataLancamento(lancamento.getDataLancamento());
        lancamento.setNumeroDocumento(numDoc);
        lancamento.setCodigoModuloLcto(codModulo);
        lancamento.setEmpresa(empresa);
        lancamento.setOperadoraCartao(operadoraCartao);

        // pega o primeiro vencimento
        lancamento.setPrimeiroVencimento(new Date());
        lancamento.setDataLancamento(new Date());
        lancamento.setIntervaloEntreParcelas(30);
        lancamento.setQuantidadeParcela(qtdParcelas);
        lancamento.setListaFinParcelaReceberCartao(new ArrayList<>());

        FinParcelaReceberCartao parcelaReceber;
        int number = 1;
        BigDecimal valorParcela = valor.divide(BigDecimal.valueOf(qtdParcelas), BigDecimal.ROUND_DOWN);
        for (int i = 0; i < qtdParcelas; i++) {
            parcelaReceber = new FinParcelaReceberCartao();
            parcelaReceber.setFinLancamentoReceberCartao(lancamento);
            parcelaReceber.setNumeroParcela(number++);
            parcelaReceber.setPago(false);
            parcelaReceber.setContaCaixa(operadoraCartao.getContaCaixa());
            parcelaReceber.setDataEmissao(lancamento.getDataLancamento());
            parcelaReceber.setDataVencimento(Biblioteca.addDay(lancamento.getDataLancamento(), 30));

            parcelaReceber.setValorBruto(valorParcela);
            parcelaReceber.setTaxaAplicada(operadoraCartao.getTaxaAdm());
            BigDecimal valorEcargosParcela = Biblioteca.calcTaxa(valorParcela, operadoraCartao.getTaxaAdm());
            parcelaReceber.setValorEncargos(valorEcargosParcela);
            parcelaReceber.setValorLiquido(valorParcela.subtract(valorEcargos));

            lancamento.getListaFinParcelaReceberCartao().add(parcelaReceber);
        }

        lancamentoReceberCartaoRepository.salvar(lancamento);

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
