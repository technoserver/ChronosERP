package com.chronos.erp.service.comercial;

import com.chronos.erp.bo.nfe.VendaToNFe;
import com.chronos.erp.dto.ConfiguracaoEmissorDTO;
import com.chronos.erp.dto.ProdutoVendaDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.AcaoLog;
import com.chronos.erp.modelo.enuns.Modulo;
import com.chronos.erp.modelo.enuns.SituacaoVenda;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.*;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 19/01/18.
 */
public class VendaPdvService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;
    @Inject
    private FinLancamentoReceberCartaoService finLancamentoReceberCartaoService;
    @Inject
    private OperadoraCartaoService operadoraCartaoService;

    @Inject
    private Repository<PdvVendaCabecalho> repository;

    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;

    @Inject
    private Repository<PdvVendaDetalhe> vendaDetalheRepository;

    @Inject
    private Repository<FinLancamentoReceberCartao> finLancamentoReceberCartaoRepository;

    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private MovimentoService movimentoService;
    @Inject
    private ContaPessoaService contaPessoaService;

    @Inject
    private Repository<ContaPessoa> contaPessoaRepository;

    @Inject
    private SyncPendentesService syncPendentesService;

    @Inject
    private NfeService nfeService;

    @Inject
    private AuditoriaService auditoriaService;

    @Inject
    private ComissaoService comissaoService;

    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private VendaDevolucaoService vendaDevolucaoService;


    @Transactional
    public void transmitirNFe(PdvVendaCabecalho venda, boolean atualizarEstoque, ModeloDocumento modelo) throws Exception {

        SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getStatusVenda());

        if (situacao == SituacaoVenda.Faturado) {
            throw new ChronosException("Essa venda já possue NFe");
        }


        VendaToNFe vendaNfe = new VendaToNFe(modelo, venda);
        NfeCabecalho nfe = vendaNfe.gerarNfe();
        nfe.setPdv(venda);

        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), nfe.getSerie(), true);
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
        String msg = nfe.getInformacoesAddContribuinte();
        if (!StringUtils.isEmpty(msg)) {
            msg += "\n" + configuracaoEmissorDTO.getObservacaoPadrao();
        }
        nfe.setInformacoesAddContribuinte(msg);
        if (StringUtils.isEmpty(configuracaoEmissorDTO.getSerie())) {
            throw new ChronosException("Serie da NFCe não definida");
        }

        nfe.setSerie(configuracaoEmissorDTO.getSerie());


        nfe.setCsc(configuracaoEmissorDTO.getCsc());
        nfe.setTokenCsc(configuracaoEmissorDTO.getTokenCsc());
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);


        if (status == StatusTransmissao.AUTORIZADA) {
            venda.setStatusVenda(SituacaoVenda.Faturado.getCodigo());
            repository.atualizar(venda);
            Mensagem.addInfoMessage("NFCe transmitida com sucesso");
        }
        auditoriaService.gerarLog(AcaoLog.FATURAR_VENDA, "Faturamento do pedido de venda " + venda.getId() + " numero da NFC-e " + nfe.getNumero(), "PDV");
    }

    @Transactional
    public void cancelar(Integer idvenda, boolean estoque, String justificativa) throws Exception {
        boolean cancelado = true;

        PdvVendaCabecalho venda = repository.get(idvenda, PdvVendaCabecalho.class);


        String numDoc = "E" + venda.getEmpresa().getId()
                + "M" + Modulo.PDV.getCodigo()
                + "V" + venda.getId();


        if (venda.getStatusVenda().equals("F")) {
            NfeCabecalho nfe = nfeRepository.get(venda.getIdnfe(), NfeCabecalho.class);
            nfe.setJustificativaCancelamento(justificativa);


            cancelado = nfeService.cancelarNFe(nfe, estoque);
            if (cancelado) {
                finLancamentoReceberService.excluirFinanceiro(numDoc, Modulo.PDV);
            }
        } else {
            finLancamentoReceberService.excluirFinanceiro(numDoc, Modulo.PDV);
        }


        if (estoque && cancelado) {
            List<PdvVendaDetalhe> itens = vendaDetalheRepository.getEntitys(PdvVendaDetalhe.class, "pdvVendaCabecalho.id", idvenda);
            for (PdvVendaDetalhe item : itens) {
                if (item.getProduto().getServico().equals("N")) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(venda.getEmpresa().getId(), item.getProduto().getId(), item.getQuantidade());
                    if (item.getIdgrade() != null) {
                        EstoqueGrade grade = estoqueGradeRepository.get(item.getIdgrade(), EstoqueGrade.class);
                        estoqueRepositoy.atualizarGradeVerificado(grade.getIdempresa(), grade.getIdproduto(), grade.getEstoqueCor().getId(), grade.getEstoqueTamanho().getId(), item.getQuantidade());
                    }
                }

            }
        }

        auditoriaService.gerarLog(AcaoLog.CANCELAR_VENDA, "Venda cancelada", "PDV");

        venda.setStatusVenda("C");
        repository.atualizar(venda);
        Mensagem.addInfoMessage("Venda cancelada com sucesso");
    }

    public void aplicarDesconto(PdvVendaCabecalho venda, String tipoDesconto, BigDecimal desconto) throws ChronosException {
        BigDecimal valorDesconto;


        if (venda.getValorTotal() == null) {
            throw new ChronosException("Valor total não informando");
        }

        if (venda.getListaPdvVendaDetalhe() == null || venda.getListaPdvVendaDetalhe().isEmpty()) {
            throw new ChronosException("Não foram informado item(s) para está venda");
        }

        if (!tipoDesconto.equals("P")) {
            valorDesconto = desconto;
        } else {
            valorDesconto = Biblioteca.calcularValorPercentual(venda.getValorTotal(), desconto);

        }

        BigDecimal fator = Biblioteca.divide(valorDesconto, venda.getValorSubtotal());
        BigDecimal descAntecipado = venda.getListaPdvVendaDetalhe()
                .stream()
                .map(PdvVendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        for (PdvVendaDetalhe i : venda.getListaPdvVendaDetalhe()) {
            BigDecimal descItem = Biblioteca.multiplica(fator, i.getValorSubtotal());
            BigDecimal vlrDesc = Biblioteca.soma(Optional.ofNullable(i.getValorDesconto()).orElse(BigDecimal.ZERO), descItem);
            BigDecimal vlrTotal = Biblioteca.subtrai(i.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(i.getValorSubtotal(), vlrTotal);
            i.setValorDesconto(vlrDesc);
            i.setValorTotal(vlrTotal);
            i.setTaxaDesconto(txDesc);

        }

        BigDecimal descItens = venda.getListaPdvVendaDetalhe()
                .stream()
                .map(PdvVendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal sobra = Biblioteca.soma(valorDesconto, descAntecipado);
        sobra = Biblioteca.subtrai(sobra, descItens);

        if (sobra.signum() > 0 || sobra.signum() < 0) {
            PdvVendaDetalhe item = venda.getListaPdvVendaDetalhe().get(0);
            BigDecimal vlrDesc = Biblioteca.soma(item.getValorDesconto(), sobra);
            BigDecimal vlrTotal = Biblioteca.subtrai(item.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(item.getValorSubtotal(), vlrTotal);
            item.setValorDesconto(vlrDesc);
            item.setValorTotal(vlrTotal);
        }

        venda.calcularValorTotal();
    }

    public VendaDevolucao gerarDevolucao(PdvVendaCabecalho venda) {
        VendaDevolucao devolucao = new VendaDevolucao();


        devolucao.setIdVenda(venda.getId());
        devolucao.setValorCredito(venda.getValorTotal());
        devolucao.setDataDevolucao(new Date());
        devolucao.setGeradoCredito("N");
        devolucao.setCreditoUtilizado("N");
        devolucao.setListaVendaDevolucaoItem(new ArrayList<>());
        devolucao.setValorVenda(venda.getValorTotal());
        devolucao.setCodigoModulo(Modulo.PDV.getCodigo());

        venda.getListaPdvVendaDetalhe().forEach(i -> {
            VendaDevolucaoItem item = new VendaDevolucaoItem();
            item.setProduto(i.getProduto());
            item.setQuantidade(i.getQuantidade());
            item.setValor(i.getValorTotal());
            item.setVendaDevolucao(devolucao);
            item.setQuantidadeVenda(i.getQuantidade());
            devolucao.getListaVendaDevolucaoItem().add(item);
        });


        return devolucao;
    }

    @Transactional
    public void confirmarDevolucao(VendaDevolucao devolucao, PdvVendaCabecalho venda) throws ChronosException {

        devolucao = vendaDevolucaoService.gerarDevolucao(devolucao);

        venda.setStatusVenda(devolucao.getTotalParcial().equals("P") ? "DP" : "D");
        venda.setNomeCliente(venda.getCliente().getPessoa().getNome());
        // todo anexar CPF/CNPJ
//        String cpfCnpj = venda.getCliente().getPessoa().getTipo().equals("F")
//                ? venda.getCliente().getPessoa().getPessoaFisica().getCpf()
//                : venda.getCliente().getPessoa().getPessoaJuridica().getCnpj();
        repository.atualizar(venda);

        if (venda.getValorComissao() != null && venda.getValorComissao().signum() > 0) {
            comissaoService.gerarComissao("A", "D", venda.getValorComissao(), venda.getValorTotal(),
                    venda.getId().toString(), venda.getVendedor().getColaborador(), Modulo.PDV);
        }

        List<ProdutoVendaDTO> produtos = new ArrayList<>();

        venda.getListaPdvVendaDetalhe().forEach(p -> {
            produtos.add(new ProdutoVendaDTO(p.getProduto().getId(), p.getQuantidade().negate()));
        });

        estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);

        if (devolucao.getGeradoCredito().equals("S") && venda.getCliente() != null) {
            contaPessoaService.lancarMovimentoDevolucao(venda.getCliente(), devolucao);
        }

        String conteudo = String.format("Devolução de Venda Nº %d modulo %s", devolucao.getIdVenda(), devolucao.getCodigoModulo());
        auditoriaService.gerarLog(AcaoLog.DEVOLUCAO, conteudo, "PDV");
    }

    public void removerDesconto(PdvVendaCabecalho venda) {
        venda.setValorDesconto(BigDecimal.ZERO);
        venda.setTaxaDesconto(BigDecimal.ZERO);

        venda.getListaPdvVendaDetalhe().forEach(i -> {
            i.setTaxaDesconto(BigDecimal.ZERO);
            i.setValorDesconto(BigDecimal.ZERO);
            i.setValorTotal(i.getValorSubtotal());
        });

        venda.calcularValorTotal();
    }
}
