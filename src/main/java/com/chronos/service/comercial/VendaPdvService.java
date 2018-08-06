package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.SituacaoVenda;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoLancamento;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.ContaPessoaService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.financeiro.MovimentoService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

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
    private Repository<PdvVendaCabecalho> repository;
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



    @Transactional
    public PdvVendaCabecalho finalizarVenda(PdvVendaCabecalho venda){
        try {
            venda.setStatusVenda("F");
            Integer idempresa = venda.getEmpresa().getId();
            AdmParametro parametro = FacesUtil.getParamentos();
            List<PdvFormaPagamento> pagamentos = venda.getListaFormaPagamento();
            venda = repository.atualizar(venda);
            List<ProdutoVendaDTO> produtos = new ArrayList<>();
            venda.getListaPdvVendaDetalhe().forEach(p->{
                produtos.add(new ProdutoVendaDTO(p.getProduto().getId(),p.getQuantidade()));
                if (parametro != null && parametro.getFrenteCaixa()) {
                    syncPendentesService.gerarSyncPendetensEstoque(0, idempresa, p.getProduto().getId());
                }
            });
            estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
            for (PdvFormaPagamento p:pagamentos) {
                if(p.getPdvTipoPagamento().getGeraParcelas().equals("S")){
                    finLancamentoReceberService.gerarLancamento(venda.getId(), p.getValor(),venda.getCliente(),p.getCondicao(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());
                }

                if (p.getPdvTipoPagamento().getCodigo().equals("05")) {
                    ContaPessoa conta = contaPessoaRepository.get(ContaPessoa.class, "pessoa.id", venda.getCliente().getPessoa().getId());

                    if (conta == null || conta.getSaldo().compareTo(p.getValor()) <= 0) {
                        throw new Exception("Saldo insuficiente para debita na conta do cliente");
                    } else {
                        contaPessoaService.lancaMovimento(conta, p.getValor(), TipoLancamento.DEBITO, Modulo.VENDA.getCodigo(), venda.getId().toString());
                    }
                }
                if (p.getPdvTipoPagamento().getCodigo().equals("03")) {
                    finLancamentoReceberService.gerarLancamentoCartao(venda.getId(), p.getValor(), p.getOperadoraCartao(), p.getQtdParcelas(), Modulo.VENDA.getCodigo(), venda.getEmpresa(), p.getPdvTipoPagamento().getIdentificador());
                }

            }
            movimentoService.lancaVenda(venda.getValorTotal(),venda.getValorDesconto(),venda.getTroco());
            return venda;
        }catch (Exception ex){
            ex.printStackTrace();
            throw new RuntimeException("Erro ao finalizar a venda",ex);
        }


    }


    @Transactional
    public void transmitirNFe(PdvVendaCabecalho venda, boolean atualizarEstoque) throws Exception {

        SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getStatusVenda());
        if (situacao == SituacaoVenda.NotaFiscal) {
            throw new Exception("Essa venda já possue NFe");
        }


        PdvCaixa caixa = FacesUtil.getMovimento().getPdvCaixa();

        VendaToNFe vendaNfe = new VendaToNFe(ModeloDocumento.NFCE, venda);
        NfeCabecalho nfe = vendaNfe.gerarNfe();
        nfe.setPdv(venda);
        nfe.setSerie(caixa.getCodigo());
        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getModeloDocumento(), true);
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
        nfe.setCsc(configuracaoEmissorDTO.getCsc());
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);


        if (status == StatusTransmissao.AUTORIZADA) {
            venda.setStatusVenda(SituacaoVenda.NotaFiscal.getCodigo());
            repository.atualizar(venda);
            Mensagem.addInfoMessage("NFCe transmitida com sucesso");
        }
    }
}
