package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.repository.VendaRepository;
import com.chronos.service.AbstractService;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.springframework.beans.BeanUtils;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 06/09/17.
 */
public class VendaService extends AbstractService<VendaCabecalho> {


    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;
    @Inject
    private EstoqueRepository estoqueRepositoy;

    @Inject
    private VendaRepository repository;
    @Inject
    private NfeService nfeService;
    @Inject
    private SyncPendentesService syncPendentesService;
    @Inject
    private Repository<VendaCondicoesParcelas> parcelasRepository;

    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private VendaComissaoService vendaComissaoService;
    @Inject
    private Repository<NfeCabecalho> nfeCabecalhoRepository;


    @Transactional
    public VendaCabecalho salvar(VendaCabecalho venda) throws ChronosException {

        if (venda.getCondicoesPagamento() != null) {
            venda.setFormaPagamento(venda.getCondicoesPagamento().getVistaPrazo().equals("V")
                    ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo());
        }

        if (venda.getSituacao().equals(SituacaoVenda.Digitacao.getCodigo()) && venda.getFormaPagamento().equals("1") && venda.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
            throw new ChronosException("Cliente com restrinções de bloqueio");
        }

        if (venda.getListaVendaDetalhe() == null || venda.getListaVendaDetalhe().isEmpty()) {
            throw new ChronosException("Não foram definido itens para o pedido de venda");
        }

        venda = repository.atualizar(venda);

        return venda;

    }

    public boolean verificarRestricao(VendaCabecalho venda) throws Exception {
        this.objeto = venda;
        necessarioAutorizacaoSupervisor = false;
        return verificarRestricao();
    }


    @Transactional
    public VendaCabecalho faturarVenda(VendaCabecalho venda) {


        venda.setSituacao(SituacaoVenda.Encerrado.getCodigo());
        Integer idempresa = venda.getEmpresa().getId();
        AdmParametro parametro = FacesUtil.getParamentos();
        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        venda.getListaVendaDetalhe().forEach(p -> {
            produtos.add(new ProdutoVendaDTO(p.getProduto().getId(), p.getQuantidade()));
            if (parametro != null && parametro.getFrenteCaixa()) {
                syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, p.getProduto().getId());
            }
        });
        estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
        finLancamentoReceberService.gerarLancamento(venda.getId(), venda.getValorTotal(), venda.getCliente(),
                venda.getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());

        String doc = "M" + Modulo.VENDA.getCodigo() + "V" + venda.getId();
        venda = repository.salvarFlush(venda);

        vendaComissaoService.gerarComissao("A", "C", venda.getValorComissao(), venda.getValorTotal(), doc, venda.getVendedor());

        auditoriaService.gerarLog(AcaoLog.ENCERRAR_VENDA, "Encerramento do pedido de venda " + venda.getId(), "VENDA");


        return venda;
    }


    @Transactional
    public void transmitirNFe(VendaCabecalho venda, ModeloDocumento modelo, boolean atualizarEstoque) {
        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getSituacao());
            if (situacao == SituacaoVenda.Faturado) {
                throw new ChronosException("Essa venda já possue NFe");
            }


            NfeCabecalho nfe;
            List<VendaCondicoesParcelas> parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", venda.getCondicoesPagamento().getId());
            venda.getCondicoesPagamento().setParcelas(parcelas);
            VendaToNFe vendaNfe = new VendaToNFe(modelo, venda);
            nfe = vendaNfe.gerarNfe();

            nfe.setVendaCabecalho(venda);
            ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
            nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
            nfe.setCsc(configuracaoEmissorDTO.getCsc());
            nfe.setSerie(configuracaoEmissorDTO.getSerie());
            String infAdd = nfe.getInformacoesAddContribuinte();
            infAdd += " " + venda.getObservacao();
            nfe.setInformacoesAddContribuinte(infAdd);
            StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

            if (status == StatusTransmissao.AUTORIZADA) {
                venda.getCondicoesPagamento().setParcelas(null);
                venda.setSituacao(SituacaoVenda.Faturado.getCodigo());
                venda.setNumeroFatura(nfe.getVendaCabecalho().getNumeroFatura());
                repository.atualizar(venda);
                String msg = modelo == ModeloDocumento.NFE ? "NFe transmitida com sucesso" : "NFCe transmitida com sucesso";
                auditoriaService.gerarLog(AcaoLog.FATURAR_VENDA, "Faturamento do pedido de venda " + venda.getId() + " numero da NF-e " + nfe.getNumero(), "VENDA");
                Mensagem.addInfoMessage(msg);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    @Transactional
    public StatusTransmissao transmitirNFe(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao, boolean atualizarEstoque) throws Exception {

        nfe.setCsc(configuracao.getCsc());
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

        return status;
    }

    @Transactional
    public void gerarDevolucao(VendaCabecalho venda, TributOperacaoFiscal operacaoFiscal) throws Exception {

        if (operacaoFiscal == null && venda.getSituacao().equals(SituacaoVenda.Faturado.getCodigo())) {
            throw new ChronosException("è preciso informa uma operação fiscal para gerar a NFe de devolução");
        }

        for (VendaDetalhe item : venda.getListaVendaDetalhe()) {
            if (item.getQuantidadeDevolvida().compareTo(item.getQuantidade()) > 0) {
                throw new ChronosException("Quantidade devolvida maior que a quantidade vendida");
            }
        }

        if (venda.getSituacao().equals(SituacaoVenda.Faturado.getCodigo())) {

            NfeCabecalho nfeSalva = nfeCabecalhoRepository.get(NfeCabecalho.class, "venda.id", venda.getId());

            if (nfeSalva == null) {
                throw new ChronosException("NFe não locaizada");
            }

            NfeCabecalho nfe = new NfeCabecalho();


            BeanUtils.copyProperties(nfe, nfeSalva, "id");


            ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);

            nfe.getListaNfeDetalhe().forEach(item -> {

                Optional<VendaDetalhe> first = venda.getListaVendaDetalhe().stream().filter(i -> i.getProduto().getId() == item.getId()).findFirst();

                item.setId(null);


                if (first.isPresent()) {

                    VendaDetalhe vendaDetalhe = first.get();

                    alterarQuantidade(item, vendaDetalhe.getQuantidade());


                } else {
                    nfe.getListaNfeDetalhe().remove(item);
                }
            });


            atualizaTotais(nfe);


        } else {

        }


    }


    public VendaCabecalho gerarVenaDoOrcamento(VendaOrcamentoCabecalho orcamento) {
        VendaCabecalho venda = new VendaCabecalho();
        VendaDetalhe itemVenda;
        venda.setListaVendaDetalhe(new ArrayList<>());
        venda.setVendaOrcamentoCabecalho(orcamento);

        for (VendaOrcamentoDetalhe d : orcamento.getListaVendaOrcamentoDetalhe()) {
            itemVenda = new VendaDetalhe();
            itemVenda.setVendaCabecalho(venda);
            itemVenda.setProduto(d.getProduto());
            itemVenda.setQuantidade(d.getQuantidade());
            itemVenda.setTaxaDesconto(d.getTaxaDesconto());
            itemVenda.setValorDesconto(d.getValorDesconto());
            itemVenda.setValorSubtotal(d.getValorSubtotal());
            itemVenda.setValorTotal(d.getValorTotal());
            itemVenda.setValorUnitario(d.getValorUnitario());

            venda.getListaVendaDetalhe().add(itemVenda);
        }


        venda.setCliente(orcamento.getCliente());
        venda.setCondicoesPagamento(orcamento.getCondicoesPagamento());
        venda.setTransportadora(orcamento.getTransportadora());
        venda.setVendedor(orcamento.getVendedor());
        venda.setTipoFrete(orcamento.getTipoFrete());
        venda.setValorSubtotal(orcamento.getValorSubtotal());
        venda.setValorFrete(orcamento.getValorFrete());
        venda.setTaxaComissao(orcamento.getTaxaComissao());
        venda.setValorComissao(orcamento.getValorComissao());
        venda.setTaxaDesconto(orcamento.getValorDesconto());
        venda.setValorTotal(orcamento.getValorTotal());
        venda.setObservacao(orcamento.getObservacao());
        venda.setEmpresa(orcamento.getEmpresa());

        String forma = venda.getCondicoesPagamento().getVistaPrazo().equals("V")
                ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo();
        venda.setFormaPagamento(forma);

        venda.calcularValorTotal();

        return venda;
    }

    public VendaCabecalho addItem(VendaCabecalho venda, VendaDetalhe vendaDetalhe) {

        Optional<VendaDetalhe> itemVenda = getItemVenda(venda, vendaDetalhe.getProduto());
        BigDecimal quantidade = vendaDetalhe.getQuantidade();
        BigDecimal valor = vendaDetalhe.getValorUnitario();

        if (itemVenda.isPresent()) {
            quantidade = itemVenda.get().getQuantidade().add(quantidade);
            itemVenda.get().setQuantidade(quantidade);
            itemVenda.get().setValorUnitario(valor);
            itemVenda.get().setTaxaDesconto(vendaDetalhe.getTaxaDesconto());
        } else {
            venda.getListaVendaDetalhe().add(vendaDetalhe);
        }

        venda.calcularValorTotal();
        return venda;
    }


    private Optional<VendaDetalhe> getItemVenda(VendaCabecalho venda, Produto produto) {
        return venda.getListaVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

    }


    public void alterarQuantidade(NfeDetalhe item, BigDecimal qtdAtual) {

        BigDecimal qtdOld = item.getQuantidadeComercial();
        BigDecimal vlrAux;


        item.setQuantidadeComercial(qtdAtual);
        item.setQuantidadeTributavel(qtdAtual);

        BigDecimal descontoUnitario = item.getValorDesconto() != null ? item.getValorDesconto().divide(qtdOld) : BigDecimal.ZERO;

        item
                .setValorBrutoProduto(qtdAtual.multiply(item.getValorUnitarioComercial()));
        item.setValorDesconto(descontoUnitario.multiply(qtdAtual));
        item.setValorSubtotal(item.getValorBrutoProduto());
        item.setValorTotal(item.getValorBrutoProduto()
                .subtract(item.getValorDesconto()));

        // icms
        if (item.getNfeDetalheImpostoIcms() != null) {
            item.getNfeDetalheImpostoIcms().setId(null);
            if (item.getNfeDetalheImpostoIcms().getBaseCalculoIcms() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
                item.getNfeDetalheImpostoIcms().setBaseCalculoIcms(vlrAux);
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIcms().getValorIcms());

                item.getNfeDetalheImpostoIcms().setValorIcms(vlrAux);
            }

            if (item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
                item.getNfeDetalheImpostoIcms().setValorBaseCalculoIcmsSt(vlrAux);

                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIcms().getValorIcmsSt());
                item.getNfeDetalheImpostoIcms().setValorIcmsSt(vlrAux);
            }
        }
        // IPI

        if (item.getNfeDetalheImpostoIpi() != null) {
            item.getNfeDetalheImpostoIpi().setId(null);
            if (item.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi());
                item.getNfeDetalheImpostoIpi().setValorBaseCalculoIpi(vlrAux);
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        item.getNfeDetalheImpostoIpi().getValorIpi());
                item.getNfeDetalheImpostoIpi().setValorIpi(vlrAux);
            }
        }

        // PIS
        if (item.getNfeDetalheImpostoPis() != null) {
            item.getNfeDetalheImpostoPis().setId(null);
            vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                    item.getNfeDetalheImpostoPis().getValorPis());
            item.getNfeDetalheImpostoPis().setValorPis(vlrAux);
        }

        // COFINS
        if (item.getNfeDetalheImpostoCofins() != null) {
            item.getNfeDetalheImpostoCofins().setId(null);
            vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                    item.getNfeDetalheImpostoCofins().getValorCofins());
            item.getNfeDetalheImpostoCofins().setValorCofins(vlrAux);
        }


    }

    private void atualizaTotais(NfeCabecalho nfe) {
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal valorFrete = BigDecimal.ZERO;
        BigDecimal valorSeguro = BigDecimal.ZERO;
        BigDecimal valorOutrasDespesas = BigDecimal.ZERO;
        BigDecimal desconto = BigDecimal.ZERO;
        BigDecimal baseCalculoIcms = BigDecimal.ZERO;
        BigDecimal valorIcms = BigDecimal.ZERO;
        BigDecimal baseCalculoIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIcmsSt = BigDecimal.ZERO;
        BigDecimal valorIpi = BigDecimal.ZERO;
        BigDecimal valorPis = BigDecimal.ZERO;
        BigDecimal valorCofins = BigDecimal.ZERO;
        BigDecimal valorNotaFiscal;
        BigDecimal valorIcmsDesonerado = BigDecimal.ZERO;
        BigDecimal totalServicos = BigDecimal.ZERO;
        BigDecimal baseCalculoIssqn = BigDecimal.ZERO;
        BigDecimal valorIssqn = BigDecimal.ZERO;
        BigDecimal valorPisIssqn = BigDecimal.ZERO;
        BigDecimal valorCofinsIssqn = BigDecimal.ZERO;

        for (NfeDetalhe itensNfe : nfe.getListaNfeDetalhe()) {
            totalProdutos = totalProdutos.add(itensNfe.getValorBrutoProduto());
            valorFrete = valorFrete.add(itensNfe.getValorFrete() != null ? itensNfe.getValorFrete() : BigDecimal.ZERO);
            valorSeguro = valorSeguro.add(itensNfe.getValorSeguro() != null ? itensNfe.getValorSeguro() : BigDecimal.ZERO);
            valorOutrasDespesas = valorOutrasDespesas.add(itensNfe.getValorOutrasDespesas() != null ? itensNfe.getValorOutrasDespesas() : BigDecimal.ZERO);
            desconto = desconto.add(itensNfe.getValorDesconto() != null ? itensNfe.getValorDesconto() : BigDecimal.ZERO);

            if (itensNfe.getNfeDetalheImpostoIcms().getBaseCalculoIcms() != null) {
                baseCalculoIcms = baseCalculoIcms.add(itensNfe.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcms() != null) {
                valorIcms = valorIcms.add(itensNfe.getNfeDetalheImpostoIcms().getValorIcms());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcmsDesonerado() != null) {
                valorIcmsDesonerado = valorIcmsDesonerado
                        .add(itensNfe.getNfeDetalheImpostoIcms().getValorIcmsDesonerado());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt() != null) {
                baseCalculoIcmsSt = baseCalculoIcmsSt
                        .add(itensNfe.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
            }
            if (itensNfe.getNfeDetalheImpostoIcms().getValorIcmsSt() != null) {
                valorIcmsSt = valorIcmsSt.add(itensNfe.getNfeDetalheImpostoIcms().getValorIcmsSt());
            }
            if (itensNfe.getNfeDetalheImpostoIpi() != null
                    && itensNfe.getNfeDetalheImpostoIpi().getValorIpi() != null) {
                valorIpi = valorIpi.add(itensNfe.getNfeDetalheImpostoIpi().getValorIpi());
            }
            if (itensNfe.getNfeDetalheImpostoPis() != null
                    && itensNfe.getNfeDetalheImpostoPis().getValorPis() != null) {
                valorPis = valorPis.add(itensNfe.getNfeDetalheImpostoPis().getValorPis());
            }
            if (itensNfe.getNfeDetalheImpostoCofins() != null
                    && itensNfe.getNfeDetalheImpostoCofins().getValorCofins() != null) {
                valorCofins = valorCofins.add(itensNfe.getNfeDetalheImpostoCofins().getValorCofins());
            }
        }

        valorNotaFiscal = totalProdutos.add(valorIcmsSt).add(valorFrete).add(valorSeguro).add(valorOutrasDespesas)
                .add(valorIpi).subtract(desconto);


        nfe.setValorFrete(valorFrete);
        nfe.setValorDespesasAcessorias(valorOutrasDespesas);
        nfe.setValorSeguro(valorSeguro);
        nfe.setValorDesconto(desconto);

        nfe.setValorServicos(totalServicos);
        nfe.setBaseCalculoIssqn(baseCalculoIssqn);
        nfe.setValorIssqn(valorIssqn);
        nfe.setValorPisIssqn(valorPisIssqn);
        nfe.setValorCofinsIssqn(valorCofinsIssqn);
        nfe.setValorIcmsDesonerado(valorIcmsDesonerado);

        nfe.setValorTotalProdutos(totalProdutos);
        nfe.setBaseCalculoIcms(baseCalculoIcms);
        nfe.setValorIcms(valorIcms);
        nfe.setBaseCalculoIcmsSt(baseCalculoIcmsSt);
        nfe.setValorIcmsSt(valorIcmsSt);
        nfe.setValorIpi(valorIpi);
        nfe.setValorPis(valorPis);
        nfe.setValorCofins(valorCofins);

        nfe.setValorTotal(valorNotaFiscal);
    }





    @Override
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }


}
