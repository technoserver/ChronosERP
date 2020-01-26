package com.chronos.erp.service.comercial;

import com.chronos.erp.bo.nfe.VendaToNFe;
import com.chronos.erp.dto.ConfiguracaoEmissorDTO;
import com.chronos.erp.dto.ProdutoVendaDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.modelo.enuns.TipoLancamento;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.AbstractService;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.ContaPessoaService;
import com.chronos.erp.service.financeiro.FinLancamentoReceberService;
import com.chronos.erp.service.financeiro.MovimentoService;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.Constants;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by john on 13/12/17.
 */
public class OsService extends AbstractService<OsAbertura> {

    @Inject
    private Repository<OsAbertura> repository;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private EstoqueRepository estoqueRepositoy;

    private Set<OsProdutoServico> itens;
    @Inject
    private NfeService nfeService;
    @Inject
    private MovimentoService movimentoService;
    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;

    @Inject
    private Repository<CondicoesParcelas> parcelasRepository;

    @Inject
    private ComissaoService comissaoService;

    @Inject
    private AuditoriaService auditoriaService;

    @Inject
    private Repository<ContaPessoa> contaPessoaRepository;
    @Inject
    private ContaPessoaService contaPessoaService;


    public OsAbertura salvar(OsAbertura os) throws ChronosException {


        Optional<OsFormaPagamento> formaPagamento = os.getListaFormaPagamento().stream().filter(f -> f.equals("14")).findFirst();

        if (formaPagamento.isPresent() && os.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
            throw new ChronosException("Cliente com restrinções de bloqueio");
        }


        if (os.isNovo()) {
            repository.salvar(os);
            os.setNumero("OS" + new DecimalFormat("0000000").format(os.getId()));
            os = repository.atualizar(os);
        } else {
            os = repository.atualizar(os);
        }
        return os;
    }


    public OsAbertura salvarItem(OsAbertura os, OsProdutoServico item, String tipoDesconto, BigDecimal desconto) throws ChronosException {
        itens = os.getListaOsProdutoServico();

        if (item.getProduto() == null || item.getProduto().getServico() == null) {
            throw new ChronosException("Tipo do produto não definido");
        }

        item.setTipo(item.getProduto().getServico() != null && item.getProduto().getServico().equals("S") ? 1 : 0);
        Optional<OsProdutoServico> itemOptional = buscarItem(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();


        if (desconto.signum() > 0) {
            if (tipoDesconto.equals("%")) {
                BigDecimal valorDesconto = Biblioteca.calcularValorPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(desconto);
                item.setValorDesconto(valorDesconto);
            } else {
                item.setValorDesconto(desconto);
                BigDecimal taxDesc = Biblioteca.descDinheiroToPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(taxDesc);
            }
        }

        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            item.setQuantidade(quantidade);
        } else {
            item.setQuantidade(quantidade);
            os.getListaOsProdutoServico().add(item);
        }

        os.calcularValores();

        return os;
    }

    public void aplicarDesconto(OsAbertura os, int tipoDesconto, BigDecimal desconto) throws ChronosException {
        BigDecimal valorDesconto;


        if (os.getValorTotal() == null) {
            throw new ChronosException("Valor total não informando");
        }

        if (os.getListaOsProdutoServico() == null || os.getListaOsProdutoServico().isEmpty()) {
            throw new ChronosException("Não foram informado item(s) para está venda");
        }

        if (tipoDesconto == 1) {
            valorDesconto = desconto;
        } else {
            valorDesconto = Biblioteca.calcularValorPercentual(os.getValorTotal(), desconto);

        }

        BigDecimal fator = valorDesconto.divide(os.getValorTotal(), MathContext.DECIMAL64);
        BigDecimal descAntecipado = os.getListaOsProdutoServico()
                .stream()
                .map(OsProdutoServico::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        for (OsProdutoServico i : os.getListaOsProdutoServico()) {
            BigDecimal descItem = Biblioteca.multiplica(fator, i.getValorSubtotal());
            BigDecimal vlrDesc = Biblioteca.soma(Optional.ofNullable(i.getValorDesconto()).orElse(BigDecimal.ZERO), descItem);
            BigDecimal vlrTotal = Biblioteca.subtrai(i.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(i.getValorSubtotal(), vlrTotal);
            i.setValorDesconto(vlrDesc);
            i.setValorTotal(vlrTotal);
            i.setTaxaDesconto(txDesc);

        }

        BigDecimal descItens = os.getListaOsProdutoServico()
                .stream()
                .map(OsProdutoServico::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal sobra = Biblioteca.soma(valorDesconto, descAntecipado);
        sobra = Biblioteca.subtrai(sobra, descItens);

        if (sobra.signum() > 0 || sobra.signum() < 0) {
            OsProdutoServico item = os.getListaOsProdutoServico().stream().findFirst().get();
            BigDecimal vlrDesc = Biblioteca.soma(item.getValorDesconto(), sobra);
            BigDecimal vlrTotal = Biblioteca.subtrai(item.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(item.getValorSubtotal(), vlrTotal);
            item.setValorDesconto(vlrDesc);
            item.setValorTotal(vlrTotal);
        }

        os.calcularValores();
    }


    public void removerDesconto(OsAbertura os) {

        os.setValorTotalDesconto(BigDecimal.ZERO);

        os.getListaOsProdutoServico().forEach(i -> {
            i.setTaxaDesconto(BigDecimal.ZERO);
            i.setValorDesconto(BigDecimal.ZERO);
            i.setValorTotal(i.getValorSubtotal());
        });
        os.calcularValorProduto();
        os.calcularValorServico();
        os.calcularValorTotal();
    }


    @Transactional
    public void transmitirNFe(OsAbertura os, ModeloDocumento modelo, boolean atualizarEstoque) throws Exception {

        if (os.getListaOsProdutoServico() == null || os.getListaOsProdutoServico().isEmpty()) {
            throw new ChronosException("Produtos ou serviço não informado !!!");
        }

        if (!os.getStatus().equals(12)) {
            encerrar(os);
        }

        NfeCabecalho nfe;

        VendaToNFe vendaNfe = new VendaToNFe(modelo, os);
        nfe = vendaNfe.gerarNfe();
        nfe.setOs(os);
        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
        nfe.setSerie(configuracaoEmissorDTO.getSerie());
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
        nfe.setCsc(configuracaoEmissorDTO.getCsc());

        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

        if (status == StatusTransmissao.AUTORIZADA) {
            Mensagem.addInfoMessage("OS Faturada com sucesso");
            os.setStatus(13);
            repository.atualizar(os);
        }


    }

    @Transactional
    public void encerrar(OsAbertura os) throws ChronosException {

        if (os.getTecnico() == null) {
            throw new ChronosException("Técnico não definido");
        }


        if (!os.getListaOsProdutoServico().isEmpty()) {


            List<ProdutoVendaDTO> produtos = new ArrayList<>();
            os.getListaOsProdutoServico()
                    .stream()
                    .filter(p -> p.getProduto().getServico().equalsIgnoreCase("N"))
                    .forEach(p -> {
                        produtos.add(new ProdutoVendaDTO(p.getProduto().getId(), p.getQuantidade()));
                    });

            estoqueRepositoy.atualizaEstoqueVerificado(os.getEmpresa().getId(), produtos);
        }

        if (os.getValorTotalProduto().signum() > 0 || os.getValorTotalServico().signum() > 0) {
            BigDecimal recebidoAteAgora = BigDecimal.ZERO;

            if (os.getListaFormaPagamento() == null | os.getListaFormaPagamento().isEmpty()) {
                throw new ChronosException("Forma de pagamento não definidas");
            }

            for (OsFormaPagamento p : os.getListaFormaPagamento()) {
                recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getFormaPagamento().getValor());
            }

            if (os.getValorTotal().compareTo(recebidoAteAgora) != 0) {
                throw new ChronosException("Valores informado nos pagamento não estão consolidado !!!");
            }

            if (os.getListaOsProdutoServico() == null || os.getListaOsProdutoServico().isEmpty()) {
                throw new ChronosException("Produtos ou serviço não informado !!!");
            }

            Optional<OsFormaPagamento> formaPagamento = os.getListaFormaPagamento().stream().filter(f -> f.equals("14")).findFirst();

            if (formaPagamento.isPresent()) {
                formaPagamento.get().getFormaPagamento().getCondicao();
                finLancamentoReceberService.gerarLancamento(os.getId(), os.getValorTotal(), os.getCliente(),
                        formaPagamento.get().getFormaPagamento().getCondicao(), Modulo.VENDA.getCodigo(), Constants.FIN.NATUREZA_VENDA, os.getEmpresa());
            }

            formaPagamento = os.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals("05")).findFirst();

            if (formaPagamento.isPresent()) {
                ContaPessoa conta = contaPessoaRepository.get(ContaPessoa.class, "pessoa.id", os.getCliente().getPessoa().getId());

                if (conta == null || conta.getSaldo().compareTo(formaPagamento.get().getFormaPagamento().getValor()) < 0) {
                    throw new ChronosException("Saldo insuficiente para debita na conta do cliente");
                } else {
                    contaPessoaService.lancaMovimento(conta, formaPagamento.get().getFormaPagamento().getValor(), TipoLancamento.DEBITO, Modulo.PDV.getCodigo(), os.getId().toString());
                }
            }

            BigDecimal comissao = gerarComissoes(os);

            os.setValorComissao(comissao);

            if (os.getMovimento() != null) {
                movimentoService.lancaVenda(os.getValorTotal(), os.getValorTotalDesconto(), BigDecimal.ZERO);
            }
        }

        os.setDataFim(new Date());
        os = repository.saveAndFlush(os);

        auditoriaService.gerarLog(AcaoLog.ENCERRAR_OS, "Encerrado OS " + os.getNumero(), "OS");

        os.setStatus(12);

        repository.atualizar(os);

    }

    @Transactional
    public void cancelarOs(OsAbertura os, boolean estoque, String justificativa) throws Exception {
        boolean cancelado = true;

        if (os.getStatus().equals(13)) {
            NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);
            nfe.setJustificativaCancelamento(justificativa);
            cancelado = nfeService.cancelarNFe(nfe, estoque);
        }

        if (cancelado && (os.getValorTotalProduto().signum() > 0 || os.getValorTotalServico().signum() > 0)) {
            finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
        }

        if (estoque && cancelado && os.getValorTotalProduto().signum() > 0) {
            for (OsProdutoServico item : os.getListaOsProdutoServico()) {
                if (item.getProduto().getServico().equals("N")) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(os.getEmpresa().getId(), item.getProduto().getId(), item.getQuantidade());
                }
            }
        }
        os.setStatus(11);
        salvar(os);
        Mensagem.addInfoMessage("OS cancelada com sucesso");
    }

    public void gerarDanfe(OsAbertura os) throws Exception {
        NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);

        nfeService.danfe(nfe);
    }

    public void gerarOSDoOrcamento(OrcamentoCabecalho orcamento, OsAbertura os) {


        for (OrcamentoDetalhe d : orcamento.getListaOrcamentoDetalhe()) {
            OsProdutoServico item = new OsProdutoServico();
            item.setOsAbertura(os);
            item.setProduto(d.getProduto());
            item.setQuantidade(d.getQuantidade());
            item.setTaxaDesconto(d.getTaxaDesconto());
            item.setValorDesconto(d.getValorDesconto());
            item.setValorSubtotal(d.getValorSubtotal());
            item.setValorTotal(d.getValorTotal());
            item.setValorUnitario(d.getValorUnitario());
            int tipo = d.getProduto().getServico().equals("S") ? 1 : 0;
            item.setTipo(tipo);
            os.getListaOsProdutoServico().add(item);
        }

        for (OrcamentoFormaPagamento p : orcamento.getListaFormaPagamento()) {
            OsFormaPagamento pag = new OsFormaPagamento();
            pag.getFormaPagamento().setEstorno(p.getFormaPagamento().getEstorno());
            pag.getFormaPagamento().setForma(p.getFormaPagamento().getForma());
            pag.setOsAbertura(os);
            pag.getFormaPagamento().setTipoPagamento(p.getFormaPagamento().getTipoPagamento());
            pag.getFormaPagamento().setTroco(p.getFormaPagamento().getTroco());
            pag.getFormaPagamento().setValor(p.getFormaPagamento().getValor());
            pag.getFormaPagamento().setBandeira(p.getFormaPagamento().getBandeira());
            pag.getFormaPagamento().setCartaoTipoIntegracao(p.getFormaPagamento().getCartaoTipoIntegracao());
            pag.getFormaPagamento().setCnpjOperadoraCartao(p.getFormaPagamento().getCnpjOperadoraCartao());
            pag.getFormaPagamento().setNumeroAutorizacao(p.getFormaPagamento().getNumeroAutorizacao());

            os.getListaFormaPagamento().add(pag);
        }


        os.setCliente(orcamento.getCliente());
        os.setVendedor(orcamento.getVendedor());
        os.setValorComissao(orcamento.getValorComissao());
        os.setValorTotal(orcamento.getValorTotal());
        os.setEmpresa(orcamento.getEmpresa());
        os.setOrcamentoCabecalho(orcamento);
        os.getVendedor().setNome(os.getVendedor().getColaborador().getPessoa().getNome());
        os.setTipoAtendimento("OC");

        os.calcularValores();


    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    private BigDecimal gerarComissoes(OsAbertura os) throws ChronosException {


        BigDecimal comissaoTecnico = BigDecimal.ZERO;

        BigDecimal comissaoVendedor = BigDecimal.ZERO;

        if (os.getTecnico().getComissao() != null && os.getTecnico().getComissao().signum() > 0
                && os.getValorTotalServico().signum() > 0) {
            comissaoTecnico = Biblioteca.calcularValorPercentual(os.getValorTotalServico(), os.getTecnico().getComissao());
            comissaoService.gerarComissao("A", "C", comissaoTecnico, os.getValorTotalServico(),
                    os.getId().toString(), os.getTecnico().getColaborador(), Modulo.OS);
        }

        if (os.getVendedor() != null && os.getVendedor().getComissao() != null && os.getVendedor().getComissao().signum() > 0
                && os.getValorTotalProduto().signum() > 0) {
            comissaoVendedor = Biblioteca.calcularValorPercentual(os.getValorTotalProduto(), os.getVendedor().getComissao());

            comissaoService.gerarComissao("A", "C", comissaoVendedor, os.getValorTotalProduto(),
                    os.getId().toString(), os.getVendedor().getColaborador(), Modulo.OS);
        }

        return Biblioteca.soma(comissaoTecnico, comissaoVendedor);
    }


    @Override
    protected Class<OsAbertura> getClazz() {
        return OsAbertura.class;
    }
}
