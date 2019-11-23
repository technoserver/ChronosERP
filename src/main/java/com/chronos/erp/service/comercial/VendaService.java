package com.chronos.erp.service.comercial;

import com.chronos.erp.bo.nfe.VendaToNFe;
import com.chronos.erp.dto.ConfiguracaoEmissorDTO;
import com.chronos.erp.dto.ProdutoVendaDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.*;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.repository.VendaRepository;
import com.chronos.erp.service.AbstractService;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.financeiro.FinLancamentoReceberService;
import com.chronos.erp.service.gerencial.AuditoriaService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.Constants;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

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
    private Repository<CondicoesParcelas> parcelasRepository;

    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private ComissaoService comissaoService;
    @Inject
    private Repository<NfeCabecalho> nfeCabecalhoRepository;
    @Inject
    private Repository<VendaDevolucao> vendaDevolucaoRepository;


    @Transactional
    public VendaCabecalho salvar(VendaCabecalho venda) throws ChronosException {


        if (venda.getListaVendaDetalhe() == null || venda.getListaVendaDetalhe().isEmpty()) {
            throw new ChronosException("Não foram definido itens para o pedido de venda");
        }

        BigDecimal recebidoAteAgora = BigDecimal.ZERO;

        if (venda.getListaFormaPagamento() == null || venda.getListaFormaPagamento().isEmpty()) {
            throw new ChronosException("Forma de pagamento não definidas");
        }

        for (VendaFormaPagamento p : venda.getListaFormaPagamento()) {
            recebidoAteAgora = Biblioteca.soma(recebidoAteAgora, p.getFormaPagamento().getValor());
        }

        if (venda.getValorTotal().compareTo(recebidoAteAgora) != 0) {
            throw new ChronosException("Valores informado nos pagamento não estão consolidado !!!");
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
    public VendaCabecalho faturarVenda(VendaCabecalho venda) throws ChronosException {


        Optional<VendaFormaPagamento> first = venda.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals(14)).findFirst();

        if (venda.getSituacao().equals(SituacaoVenda.Digitacao.getCodigo()) && first.isPresent() && venda.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
            throw new ChronosException("Cliente com restrinções de bloqueio");
        }


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

        if (venda.getId() == null) {
            venda = salvar(venda);
        }

        Optional<VendaFormaPagamento> pagamentoOptional = venda.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals("14")).findFirst();

        if (pagamentoOptional.isPresent()) {
            finLancamentoReceberService.gerarLancamento(venda.getId(), venda.getValorTotal(), venda.getCliente(),
                    pagamentoOptional.get().getFormaPagamento().getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constants.FIN.NATUREZA_VENDA, venda.getEmpresa());
        }


        venda = repository.salvarFlush(venda);

        comissaoService.gerarComissao("A", "C", venda.getValorComissao(), venda.getValorTotal(),
                venda.getId().toString(), venda.getVendedor().getColaborador(), Modulo.VENDA);

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

            Optional<VendaFormaPagamento> pagamentoOptional = venda.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals("14")).findFirst();

            if (pagamentoOptional.isPresent() && pagamentoOptional.get().getFormaPagamento().getCondicoesPagamento() != null) {
                List<CondicoesParcelas> parcelas = parcelasRepository.getEntitys(CondicoesParcelas.class, "condicoesPagamento.id", pagamentoOptional.get().getFormaPagamento().getCondicoesPagamento().getId());
                pagamentoOptional.get().getFormaPagamento().getCondicoesPagamento().setParcelas(parcelas);
            }


            VendaToNFe vendaNfe = new VendaToNFe(modelo, venda);
            nfe = vendaNfe.gerarNfe();

            nfe.setVendaCabecalho(venda);
            ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
            nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
            nfe.setCsc(configuracaoEmissorDTO.getCsc());
            nfe.setTokenCsc(configuracaoEmissorDTO.getTokenCsc());
            nfe.setSerie(configuracaoEmissorDTO.getSerie());
            String infAdd = nfe.getInformacoesAddContribuinte();


            if (!StringUtils.isEmpty(infAdd)) {
                infAdd += "\n" + configuracaoEmissorDTO.getObservacaoPadrao();
            }

            infAdd += "\n" + venda.getObservacao();

            nfe.setInformacoesAddContribuinte(infAdd);
            StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

            if (status == StatusTransmissao.AUTORIZADA) {
                String codigo = venda.getSituacao();


                venda.setSituacao(SituacaoVenda.Faturado.getCodigo());
                venda.setNumeroFatura(nfe.getVendaCabecalho().getNumeroFatura());
                repository.atualizar(venda);

                if (!codigo.equals(SituacaoVenda.Encerrado.getCodigo())) {
                    comissaoService.gerarComissao("A", "C", venda.getValorComissao(), venda.getValorTotal(),
                            venda.getId().toString(), venda.getVendedor().getColaborador(), Modulo.VENDA);
                }

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

        VendaDevolucao devolucao = new VendaDevolucao();
        List<VendaDevolucaoItem> itensDevolucao = new ArrayList<>();


        String totalParcial = venda.isExcludoItem() || venda.calcularValorDevolucao().compareTo(venda.getValorTotal()) != 0 ? "P" : "T";

        devolucao.setTotalParcial(totalParcial);
        devolucao.setValorCredito(venda.getValorTotal());
        devolucao.setVendaCabecalho(venda);

        if (venda.getSituacao().equals(SituacaoVenda.Faturado.getCodigo())) {

            NfeCabecalho nfeSalva = nfeCabecalhoRepository.get(NfeCabecalho.class, "vendaCabecalho.id", venda.getId());

            if (nfeSalva == null) {
                throw new ChronosException("NFe não locaizada");
            }

            NfeCabecalho nfe = new NfeCabecalho();


            BeanUtils.copyProperties(nfeSalva, nfe, "id", "chaveAcesso", "numero", "codigoNumerico", "serie", "listaNfeDetalhe", "listaNfeReferenciada",
                    "digitoChaveAcesso", "listaNfeFormaPagamento", "listaDuplicata", "fatura", "tributOperacaoFiscal",
                    "qrcode", "urlChave", "statusNota", "valorTotalTributos",
                    "valorTotalTributosFederais", "valorTotalTributosEstaduais", "valorTotalTributosMunicipais");

            nfe.setCodigoModelo(ModeloDocumento.NFE.getCodigo().toString());

            ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);

            nfe.setNaturezaOperacao(operacaoFiscal.getDescricao());
            nfe.setTributOperacaoFiscal(operacaoFiscal);
            nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
            nfe.setSerie(configuracaoEmissorDTO.getSerie());
            nfe.setTipoOperacao(0);
            nfe.setFinalidadeEmissao(FinalidadeEmissao.DEVOLUCAO.getCodigo());
            nfe.setDataHoraEntradaSaida(new Date());

            List<NfeDetalhe> itens = new ArrayList<>();


            nfeSalva.getListaNfeDetalhe().forEach(item -> {

                NfeDetalhe newItem;

                Optional<VendaDetalhe> first = venda.getListaVendaDetalhe().stream()
                        .filter(i -> i.getProduto().getId().equals(item.getProduto().getId()) && i.getQuantidadeDevolvida().signum() > 0)
                        .findFirst();


                if (first.isPresent()) {


                    VendaDetalhe vendaDetalhe = first.get();

                    newItem = alterarQuantidade(item, vendaDetalhe.getQuantidadeDevolvida());
                    newItem.setCfop(operacaoFiscal.getCfop());
                    newItem.setNfeCabecalho(nfe);

                    itens.add(newItem);

                    VendaDevolucaoItem itemDevolucao = new VendaDevolucaoItem();

                    itemDevolucao.setVendaDevolucao(devolucao);
                    itemDevolucao.setQuantidade(vendaDetalhe.getQuantidadeDevolvida());
                    itemDevolucao.setProduto(vendaDetalhe.getProduto());

                    BigDecimal valorPorItem = Biblioteca.valorPorItem(vendaDetalhe.getQuantidade(), vendaDetalhe.getQuantidadeDevolvida(), vendaDetalhe.getValorTotal());

                    itemDevolucao.setValor(valorPorItem);

                    itensDevolucao.add(itemDevolucao);

                }
            });


            nfe.setListaNfeDetalhe(itens);


            atualizaTotais(nfe);

            NfeFormaPagamento forma = new NfeFormaPagamento();
            forma.setNfeCabecalho(nfe);
            forma.setTipoPagamento(new TipoPagamento().buscarPorCodigo("90"));
            forma.setForma("90");

            nfe.getListaNfeFormaPagamento().add(forma);


            NfeReferenciada referenciada = new NfeReferenciada();
            referenciada.setNfeCabecalho(nfe);
            referenciada.setChaveAcesso(nfeSalva.getChaveAcessoCompleta());
            nfe.getListaNfeReferenciada().add(referenciada);


            StatusTransmissao status = nfeService.transmitirNFe(nfe, true);


            if (status == StatusTransmissao.AUTORIZADA) {

                devolucao.setListaVendaDevolucaoItem(itensDevolucao);

                vendaDevolucaoRepository.salvar(devolucao);

                String situacao = totalParcial.equals("T") ? SituacaoVenda.Devolucao.getCodigo() : SituacaoVenda.Devolucao_PARCIAL.getCodigo();


                repository.atualizarNamedQuery("VendaCabecalho.UpdateSituacao", situacao, venda.getId());

                Mensagem.addInfoMessage("Devolução gerada com sucesso");
                auditoriaService.gerarLog(AcaoLog.DEVOLUCAO, "Devolução de venda " + venda.getId() + " numero da NF-e " + nfe.getNumero(), "VENDA");

            } else {
                FacesContext.getCurrentInstance().validationFailed();
            }


        } else {

            venda.getListaVendaDetalhe().forEach(item -> {

                if (item.getQuantidadeDevolvida().signum() > 0) {

                    VendaDevolucaoItem itemDevolucao = new VendaDevolucaoItem();

                    itemDevolucao.setVendaDevolucao(devolucao);
                    itemDevolucao.setQuantidade(item.getQuantidadeDevolvida());
                    itemDevolucao.setProduto(item.getProduto());

                    BigDecimal valorPorItem = Biblioteca.valorPorItem(item.getQuantidade(), item.getQuantidadeDevolvida(), item.getValorTotal());

                    itemDevolucao.setValor(valorPorItem);

                    itensDevolucao.add(itemDevolucao);

                    estoqueRepositoy.atualizaEstoqueEmpresaControle(venda.getEmpresa().getId(), item.getProduto().getId(), item.getQuantidadeDevolvida());

                }

            });

            devolucao.setListaVendaDevolucaoItem(itensDevolucao);
            vendaDevolucaoRepository.salvar(devolucao);


            String situacao = totalParcial.equals("T") ? SituacaoVenda.Devolucao.getCodigo() : SituacaoVenda.Devolucao_PARCIAL.getCodigo();

            repository.atualizarNamedQuery("VendaCabecalho.UpdateSituacao", situacao, venda.getId());

            auditoriaService.gerarLog(AcaoLog.DEVOLUCAO, "Devolução de venda " + venda.getId(), "VENDA");

            Mensagem.addInfoMessage("Devolução gerada com sucesso");

        }


    }


    public VendaCabecalho gerarVenaDoOrcamento(OrcamentoCabecalho orcamento) {
        VendaCabecalho venda = new VendaCabecalho();
        VendaDetalhe itemVenda;
        venda.setListaVendaDetalhe(new ArrayList<>());
        venda.setOrcamentoCabecalho(orcamento);

        for (OrcamentoDetalhe d : orcamento.getListaOrcamentoDetalhe()) {
            itemVenda = new VendaDetalhe();
            itemVenda.setVendaCabecalho(venda);
            itemVenda.setIdgrade(d.getIdgrade());
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
        venda.setDataSaida(orcamento.getDataEntrega());
        venda.setListaFormaPagamento(new HashSet<>());

        orcamento.getListaFormaPagamento().forEach(p -> {
            VendaFormaPagamento pag = new VendaFormaPagamento();
            pag.setVendaCabecalho(venda);
            pag.setFormaPagamento(p.getFormaPagamento());
            venda.getListaFormaPagamento().add(pag);
        });

        venda.calcularValorTotal();

        return venda;
    }

    public VendaCabecalho addItem(VendaCabecalho venda, VendaDetalhe item, BigDecimal desconto, int tipoDesconto) throws ChronosException {


        item.calcularValorTotal();

        if (desconto != null && desconto.signum() > 0) {
            if (tipoDesconto == 0) {
                BigDecimal valorDesconto = Biblioteca.calcularValorPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(desconto);
                item.setValorDesconto(valorDesconto);
            } else {
                item.setValorDesconto(desconto);
                BigDecimal taxDesc = Biblioteca.descDinheiroToPercentual(item.getValorSubtotal(), desconto);
                item.setTaxaDesconto(taxDesc);
            }
        }

        venda.getListaVendaDetalhe().add(item);

        venda.calcularValorTotal();
        return venda;
    }


    private Optional<VendaDetalhe> getItemVenda(VendaCabecalho venda, Produto produto) {
        return venda.getListaVendaDetalhe().stream().filter(i -> i.getProduto().equals(produto)).findAny();

    }


    public NfeDetalhe alterarQuantidade(NfeDetalhe item, BigDecimal qtdAtual) {


        NfeDetalhe newItem = new NfeDetalhe();

        BeanUtils.copyProperties(item, newItem, "id", "nfeDetalheImpostoCofins", "nfeDetalheImpostoPis",
                "nfeDetalheImpostoIcms", "nfeDetalheImpostoIpi", "nfeCabecalho", "valorTotalTributos",
                "valorTotalTributosFederais", "valorTotalTributosEstaduais", "valorTotalTributosMunicipais");

        BigDecimal qtdOld = item.getQuantidadeComercial();
        BigDecimal vlrAux;


        item.setQuantidadeComercial(qtdAtual);
        item.setQuantidadeTributavel(qtdAtual);

        BigDecimal descontoUnitario = item.getValorDesconto() != null ? item.getValorDesconto().divide(qtdOld) : BigDecimal.ZERO;

        item.setValorBrutoProduto(qtdAtual.multiply(item.getValorUnitarioComercial()));
        item.setValorDesconto(descontoUnitario.multiply(qtdAtual));
        item.setValorSubtotal(item.getValorBrutoProduto());
        item.setValorTotal(item.getValorBrutoProduto()
                .subtract(item.getValorDesconto()));

        // icms
        if (item.getNfeDetalheImpostoIcms() != null) {

            newItem.setNfeDetalheImpostoIcms(new NfeDetalheImpostoIcms());
            newItem.getNfeDetalheImpostoIcms().setNfeDetalhe(newItem);

            BeanUtils.copyProperties(item.getNfeDetalheImpostoIcms(), newItem.getNfeDetalheImpostoIcms(), "id", "nfeDetalhe");

            if (newItem.getNfeDetalheImpostoIcms().getBaseCalculoIcms() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIcms().getBaseCalculoIcms());
                newItem.getNfeDetalheImpostoIcms().setBaseCalculoIcms(vlrAux);
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIcms().getValorIcms());

                newItem.getNfeDetalheImpostoIcms().setValorIcms(vlrAux);
            }

            if (newItem.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIcms().getValorBaseCalculoIcmsSt());
                newItem.getNfeDetalheImpostoIcms().setValorBaseCalculoIcmsSt(vlrAux);

                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIcms().getValorIcmsSt());
                newItem.getNfeDetalheImpostoIcms().setValorIcmsSt(vlrAux);
            }
        }
        // IPI

        if (item.getNfeDetalheImpostoIpi() != null) {

            newItem.setNfeDetalheImpostoIpi(new NfeDetalheImpostoIpi());
            newItem.getNfeDetalheImpostoIpi().setNfeDetalhe(newItem);

            BeanUtils.copyProperties(item.getNfeDetalheImpostoIpi(), newItem.getNfeDetalheImpostoIpi(), "id", "nfeDetalhe");

            if (item.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi() != null) {
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIpi().getValorBaseCalculoIpi());
                newItem.getNfeDetalheImpostoIpi().setValorBaseCalculoIpi(vlrAux);
                vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                        newItem.getNfeDetalheImpostoIpi().getValorIpi());
                newItem.getNfeDetalheImpostoIpi().setValorIpi(vlrAux);
            }
        }

        // PIS
        if (item.getNfeDetalheImpostoPis() != null) {

            newItem.setNfeDetalheImpostoPis(new NfeDetalheImpostoPis());
            newItem.getNfeDetalheImpostoPis().setNfeDetalhe(newItem);

            BeanUtils.copyProperties(item.getNfeDetalheImpostoPis(), newItem.getNfeDetalheImpostoPis(), "id", "nfeDetalhe");

            newItem.getNfeDetalheImpostoPis().setId(null);
            vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                    newItem.getNfeDetalheImpostoPis().getValorPis());
            newItem.getNfeDetalheImpostoPis().setValorPis(vlrAux);
        }

        // COFINS
        if (item.getNfeDetalheImpostoCofins() != null) {

            newItem.setNfeDetalheImpostoCofins(new NfeDetalheImpostoCofins());
            newItem.getNfeDetalheImpostoCofins().setNfeDetalhe(newItem);

            BeanUtils.copyProperties(item.getNfeDetalheImpostoCofins(), newItem.getNfeDetalheImpostoCofins(), "id", "nfeDetalhe");

            newItem.getNfeDetalheImpostoCofins().setId(null);
            vlrAux = Biblioteca.valorPorItem(qtdOld, qtdAtual,
                    newItem.getNfeDetalheImpostoCofins().getValorCofins());
            newItem.getNfeDetalheImpostoCofins().setValorCofins(vlrAux);
        }

        return newItem;
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


    public void aplicarDesconto(VendaCabecalho venda, int tipoDesconto, BigDecimal desconto) throws ChronosException {
        BigDecimal valorDesconto;


        if (venda.getValorTotal() == null) {
            throw new ChronosException("Valor total não informando");
        }

        if (venda.getListaVendaDetalhe() == null || venda.getListaVendaDetalhe().isEmpty()) {
            throw new ChronosException("Não foram informado item(s) para está venda");
        }

        if (tipoDesconto == 1) {
            valorDesconto = desconto;
        } else {
            valorDesconto = Biblioteca.calcularValorPercentual(venda.getValorTotal(), desconto);

        }

        BigDecimal fator = Biblioteca.divide(valorDesconto, venda.getValorSubtotal());
        BigDecimal descAntecipado = venda.getListaVendaDetalhe()
                .stream()
                .map(VendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        for (VendaDetalhe i : venda.getListaVendaDetalhe()) {
            BigDecimal descItem = Biblioteca.multiplica(fator, i.getValorSubtotal());
            BigDecimal vlrDesc = Biblioteca.soma(Optional.ofNullable(i.getValorDesconto()).orElse(BigDecimal.ZERO), descItem);
            BigDecimal vlrTotal = Biblioteca.subtrai(i.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(i.getValorSubtotal(), vlrTotal);
            i.setValorDesconto(vlrDesc);
            i.setValorTotal(vlrTotal);
            i.setTaxaDesconto(txDesc);

        }

        BigDecimal descItens = venda.getListaVendaDetalhe()
                .stream()
                .map(VendaDetalhe::getValorDesconto)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal sobra = Biblioteca.soma(valorDesconto, descAntecipado);
        sobra = Biblioteca.subtrai(sobra, descItens);

        if (sobra.signum() > 0 || sobra.signum() < 0) {
            VendaDetalhe item = venda.getListaVendaDetalhe().get(0);
            BigDecimal vlrDesc = Biblioteca.soma(item.getValorDesconto(), sobra);
            BigDecimal vlrTotal = Biblioteca.subtrai(item.getValorSubtotal(), vlrDesc);
            BigDecimal txDesc = Biblioteca.calcularFator(item.getValorSubtotal(), vlrTotal);
            item.setValorDesconto(vlrDesc);
            item.setValorTotal(vlrTotal);
        }

        venda.calcularValorTotal();
    }

    public void removerDesconto(VendaCabecalho venda) {

        venda.setValorDesconto(BigDecimal.ZERO);
        venda.setTaxaDesconto(BigDecimal.ZERO);

        venda.getListaVendaDetalhe().forEach(i -> {
            i.setTaxaDesconto(BigDecimal.ZERO);
            i.setValorDesconto(BigDecimal.ZERO);
            i.setValorTotal(i.getValorSubtotal());
        });

        venda.calcularValorTotal();
    }


    @Override
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }


}
