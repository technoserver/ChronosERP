package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.repository.VendaComissaoRepository;
import com.chronos.repository.VendaRepository;
import com.chronos.service.AbstractService;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    private VendaComissaoRepository comissoes;
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


    @Transactional
    public VendaCabecalho salvar(VendaCabecalho venda) throws ChronosException {

        if (venda.getCondicoesPagamento() != null) {
            venda.setFormaPagamento(venda.getCondicoesPagamento().getVistaPrazo().equals("V")
                    ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo());
        }

        if (venda.getFormaPagamento().equals("1") && venda.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
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
            venda.getListaVendaDetalhe().forEach(p->{
                produtos.add(new ProdutoVendaDTO(p.getProduto().getId(), p.getQuantidade()));
                if (parametro != null && parametro.getFrenteCaixa()) {
                    syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, p.getId());
                }
            });
            estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
            finLancamentoReceberService.gerarLancamento(venda.getId(), venda.getValorTotal(), venda.getCliente(),
                    venda.getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());

            gerarComissao(venda);
            venda = repository.salvarFlush(venda);
        auditoriaService.gerarLog(AcaoLog.ENCERRAR_VENDA, "Encerramento do pedido de venda " + venda.getId(), "VENDA");


        return venda;
    }



    @Transactional
    public void transmitirNFe(VendaCabecalho venda, ModeloDocumento modelo, boolean atualizarEstoque) {
        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getSituacao());
            if (situacao == SituacaoVenda.Faturado) {
                throw new Exception("Essa venda já possue NFe");
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


    private void gerarComissao(VendaCabecalho venda) {
        VendaComissao comissao = new VendaComissao();
        comissao.setDataLancamento(new Date());
        comissao.setSituacao("A");
        comissao.setTipoContabil("C");
        comissao.setValorComissao(venda.getValorComissao());
        comissao.setValorVenda(venda.getValorTotal());
        comissao.setVendaCabecalho(venda);
        comissao.setVendedor(venda.getVendedor());
        comissoes.salvar(comissao);
    }

    private void verificarR() {

    }


    @Override
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }


}
