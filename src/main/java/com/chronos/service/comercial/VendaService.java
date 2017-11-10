package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaComissao;
import com.chronos.modelo.entidades.VendaCondicoesPagamento;
import com.chronos.modelo.entidades.enuns.FormaPagamento;
import com.chronos.modelo.entidades.enuns.Modulo;
import com.chronos.modelo.entidades.enuns.SituacaoVenda;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.VendaComissaoRepository;
import com.chronos.repository.VendaRepository;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

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


    @Transactional
    public VendaCabecalho faturarVenda(VendaCabecalho venda) {
        try {
            if (venda.getCondicoesPagamento() != null) {
                venda.setFormaPagamento(venda.getCondicoesPagamento().getVistaPrazo().equals("V")
                        ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo());
            }
            VendaCondicoesPagamento condicao = venda.getCondicoesPagamento();
            venda.setSituacao(SituacaoVenda.Faturado.getCodigo());
            venda = repository.atualizar(venda);
            estoqueRepositoy.atualizaEstoqueEmpresaControle(venda.getEmpresa().getId(), venda.getListaVendaDetalhe());
            finLancamentoReceberService.gerarLancamento(venda.getValorTotal(), venda.getCliente(),
                    new DecimalFormat("VD0000000").format(venda.getId()), condicao, Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, venda.getEmpresa());

            gerarComissao(venda);


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", ex);
        }

        return venda;
    }

    @Transactional
    public void transmitirNFe(VendaCabecalho venda, ModeloDocumento modelo) {
        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(venda.getSituacao());
            if (situacao != SituacaoVenda.Faturado) {
                throw new Exception("Essa venda não se encontra faturada");
            }
            if (situacao == SituacaoVenda.NotaFiscal) {
                throw new Exception("Essa venda já possue NFe");
            }


            ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(venda.getEmpresa(), modelo);
            NfeCabecalho nfe;
            VendaToNFe vendaNfe = new VendaToNFe(modelo, configuracao, venda);
            nfe = vendaNfe.gerarNfe();
            nfe.setCsc(configuracao.getCsc());
            nfe.setVendaCabecalho(venda);
            StatusTransmissao status = nfeService.transmitirNFe(nfe, configuracao);
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
