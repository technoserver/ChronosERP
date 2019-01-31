package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.*;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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


    @Transactional
    public PdvVendaCabecalho finalizarVenda(PdvVendaCabecalho venda) throws Exception {

        venda.setStatusVenda(SituacaoVenda.Encerrado.getCodigo());
        Integer idempresa = venda.getEmpresa().getId();
        AdmParametro parametro = FacesUtil.getParamentos();
        List<PdvFormaPagamento> pagamentos = venda.getListaFormaPagamento();
        venda = repository.atualizar(venda);
        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        venda.getListaPdvVendaDetalhe().forEach(p -> {
            produtos.add(new ProdutoVendaDTO(p.getProduto().getId(), p.getQuantidade()));
            if (parametro != null && parametro.getFrenteCaixa()) {
                syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, p.getProduto().getId());
            }
        });
        estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
        for (PdvFormaPagamento p : pagamentos) {
            if (p.getPdvTipoPagamento().getGeraParcelas().equals("S") && p.getPdvTipoPagamento().getCodigo().equals("14")) {
                finLancamentoReceberService.gerarLancamento(venda.getId(), p.getValor(), venda.getCliente(), p.getCondicao(), Modulo.PDV.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());
            }

            if (p.getPdvTipoPagamento().getCodigo().equals("05")) {
                ContaPessoa conta = contaPessoaRepository.get(ContaPessoa.class, "pessoa.id", venda.getCliente().getPessoa().getId());

                if (conta == null || conta.getSaldo().compareTo(p.getValor()) < 0) {
                    throw new ChronosException("Saldo insuficiente para debita na conta do cliente");
                } else {
                    contaPessoaService.lancaMovimento(conta, p.getValor(), TipoLancamento.DEBITO, Modulo.PDV.getCodigo(), venda.getId().toString());
                }
            }
            if (p.getPdvTipoPagamento().getCodigo().equals("03")) {

                OperadoraCartaoTaxa operadoraCartaoTaxa = operadoraCartaoService.getOperadoraCartaoTaxa(new ArrayList<>(p.getOperadoraCartao().getListaOperadoraCartaoTaxas()), p.getQtdParcelas());
                FinLancamentoReceberCartao finLancamentoReceberCartao = finLancamentoReceberCartaoService.gerarLancamento(venda.getId(), p.getValor(), p.getOperadoraCartao(), operadoraCartaoTaxa, p.getQtdParcelas(), Modulo.VENDA.getCodigo(), venda.getEmpresa(), p.getPdvTipoPagamento().getIdentificador());
                finLancamentoReceberCartaoRepository.salvar(finLancamentoReceberCartao);
            }

        }
        movimentoService.lancaVenda(venda.getValorTotal(), venda.getValorDesconto(), venda.getTroco());


        auditoriaService.gerarLog(AcaoLog.ENCERRAR_VENDA, "Encerramento do pedido de venda " + venda.getId(), "PDV");

        return venda;


    }


    @Transactional
    public void transmitirNFe(PdvVendaCabecalho venda, boolean atualizarEstoque) throws Exception {

        SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getStatusVenda());

        if (situacao == SituacaoVenda.Faturado) {
            throw new ChronosException("Essa venda já possue NFe");
        }


        VendaToNFe vendaNfe = new VendaToNFe(ModeloDocumento.NFCE, venda);
        NfeCabecalho nfe = vendaNfe.gerarNfe();
        nfe.setPdv(venda);

        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());

        if (StringUtils.isEmpty(configuracaoEmissorDTO.getSerie())) {
            throw new ChronosException("Serie da NFCe não definida");
        }

        nfe.setSerie(configuracaoEmissorDTO.getSerie());


        nfe.setCsc(configuracaoEmissorDTO.getCsc());
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);


        if (status == StatusTransmissao.AUTORIZADA) {
            venda.setStatusVenda(SituacaoVenda.Faturado.getCodigo());
            repository.atualizar(venda);
            Mensagem.addInfoMessage("NFCe transmitida com sucesso");
        }
        auditoriaService.gerarLog(AcaoLog.FATURAR_VENDA, "Faturamento do pedido de venda " + venda.getId() + " numero da NFC-e " + nfe.getNumero(), "PDV");
    }
}
