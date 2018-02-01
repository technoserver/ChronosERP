package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.PdvVendaCabecalho;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaComissao;
import com.chronos.modelo.entidades.enuns.Modulo;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.repository.VendaComissaoRepository;
import com.chronos.repository.VendaRepository;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 06/09/17.
 */
public class VendaService implements Serializable {


    private static final long serialVersionUID = 1L;
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
    private Repository<PdvVendaCabecalho> pdvRepository;


    @Transactional
    public VendaCabecalho faturarVenda(VendaCabecalho venda) {
        try {

            venda.setSituacao(SituacaoVenda.Faturado.getCodigo());
            venda = repository.atualizar(venda);
            List<ProdutoVendaDTO> produtos = new ArrayList<>();
            venda.getListaVendaDetalhe().forEach(p->{
                produtos.add(new ProdutoVendaDTO(p.getId(),p.getQuantidade()));
            });
            estoqueRepositoy.atualizaEstoqueVerificado(venda.getEmpresa().getId(), produtos);
            finLancamentoReceberService.gerarLancamento(venda.getId(), venda.getValorTotal(), venda.getCliente(),
                    venda.getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());

            gerarComissao(venda);


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", ex);
        }

        return venda;
    }

    @Transactional
    public void transmitirNFe(PdvVendaCabecalho venda, boolean atualizarEstoque) throws Exception {

        SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getStatusVenda());
        if (situacao == SituacaoVenda.NotaFiscal) {
            throw new Exception("Essa venda já possue NFe");
        }
        ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(venda.getEmpresa(), ModeloDocumento.NFCE);

        VendaToNFe vendaNfe = new VendaToNFe(ModeloDocumento.NFCE, configuracao, venda);
        NfeCabecalho nfe = vendaNfe.gerarNfe();
        StatusTransmissao status = transmitirNFe(nfe, configuracao, atualizarEstoque);

        if (status == StatusTransmissao.AUTORIZADA) {
            venda.setStatusVenda(SituacaoVenda.NotaFiscal.getCodigo());
            venda.setId(nfe.getId());
            pdvRepository.atualizar(venda);
            Mensagem.addInfoMessage("NFCe transmitida com sucesso");
        }
    }

    @Transactional
    public void transmitirNFe(VendaCabecalho venda, ModeloDocumento modelo, boolean atualizarEstoque) {
        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getSituacao());
            if (situacao == SituacaoVenda.NotaFiscal) {
                throw new Exception("Essa venda já possue NFe");
            }


            ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(venda.getEmpresa(), modelo);
            NfeCabecalho nfe;
            VendaToNFe vendaNfe = new VendaToNFe(modelo, configuracao, venda);
            nfe = vendaNfe.gerarNfe();

            nfe.setVendaCabecalho(venda);

            StatusTransmissao status = nfeService.transmitirNFe(nfe, configuracao, atualizarEstoque);
            if (status == StatusTransmissao.AUTORIZADA) {
                venda.setSituacao(SituacaoVenda.NotaFiscal.getCodigo());
                venda.setNumeroFatura(nfe.getVendaCabecalho().getNumeroFatura());
                repository.atualizar(venda);
                String msg = modelo == ModeloDocumento.NFE ? "NFe transmitida com sucesso" : "NFCe transmitida com sucesso";
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
        StatusTransmissao status = nfeService.transmitirNFe(nfe, configuracao, atualizarEstoque);

        return status;
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
}
