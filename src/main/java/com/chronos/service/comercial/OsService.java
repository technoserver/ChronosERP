package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.AbstractService;
import com.chronos.service.ChronosException;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private FinLancamentoReceberService finLancamentoReceberService;

    @Inject
    private Repository<VendaCondicoesParcelas> parcelasRepository;


    public OsAbertura salvar(OsAbertura os) throws ChronosException {


        if (os.getCondicoesPagamento().getVistaPrazo().equals("1") && os.getCliente().getSituacaoForCli().getBloquear().equals("S")) {
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


    public OsAbertura salvarItem(OsAbertura os, OsProdutoServico item) throws ChronosException {
        itens = os.getListaOsProdutoServico();
        Optional<OsProdutoServico> itemOptional = buscarItem(item.getProduto());
        BigDecimal quantidade = item.getQuantidade();

        if (itemOptional.isPresent()) {
            item = itemOptional.get();
            item.setQuantidade(item.getQuantidade().add(quantidade));
        } else {
            item.setQuantidade(quantidade);
            itens.add(item);
        }

        item.setTipo(item.getProduto().getServico() != null && item.getProduto().getServico().equals("S") ? 1 : 0);
        os.calcularValores();
        return salvar(os);
    }

    @Transactional
    public void transmitirNFe(OsAbertura os, ModeloDocumento modelo, boolean atualizarEstoque) throws Exception {


        NfeCabecalho nfe;
        List<VendaCondicoesParcelas> parcelas = parcelasRepository.getEntitys(VendaCondicoesParcelas.class, "vendaCondicoesPagamento.id", os.getCondicoesPagamento().getId());
        os.getCondicoesPagamento().setParcelas(parcelas);
        VendaToNFe vendaNfe = new VendaToNFe(modelo, os);
        nfe = vendaNfe.gerarNfe();
        nfe.setOs(os);
        ConfiguracaoEmissorDTO configuracaoEmissorDTO = nfeService.instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento(), true);
        nfe.setSerie(configuracaoEmissorDTO.getSerie());
        nfe.setAmbiente(configuracaoEmissorDTO.getWebserviceAmbiente());
        nfe.setCsc(configuracaoEmissorDTO.getCsc());
        StatusTransmissao status = nfeService.transmitirNFe(nfe, atualizarEstoque);

        if (status == StatusTransmissao.AUTORIZADA) {
            String msg = modelo == ModeloDocumento.NFE ? "NFe transmitida com sucesso" : "NFCe transmitida com sucesso";
            Mensagem.addInfoMessage(msg);
        }


    }

    @Transactional
    public void faturar(OsAbertura os) throws ChronosException {
        os.setOsStatus(Constantes.OS.STATUS_FATURADO);

        List<ProdutoVendaDTO> produtos = new ArrayList<>();
        os.getListaOsProdutoServico()
                .stream()
                .filter(p -> p.getProduto().getServico().equalsIgnoreCase("N"))
                .forEach(p -> {
                    produtos.add(new ProdutoVendaDTO(p.getId(), p.getQuantidade()));
                });

        estoqueRepositoy.atualizaEstoqueVerificado(os.getEmpresa().getId(), produtos);
        finLancamentoReceberService.gerarLancamento(os.getId(), os.getValorTotal(), os.getCliente(),
                os.getCondicoesPagamento(), Modulo.VENDA.getCodigo(), Constantes.FIN.NATUREZA_VENDA, os.getEmpresa());
        os = repository.saveAndFlush(os);
    }

    @Transactional
    public void cancelarOs(OsAbertura os, boolean estoque) throws Exception {
        boolean cancelado = true;
        if (os.getOsStatus().getId() == 6) {
            NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);
            nfe.setJustificativaCancelamento("Cancelamento de por informação de valores invalido");


            cancelado = nfeService.cancelarNFe(nfe, estoque);
            if (cancelado) {
                finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
            }
        } else {
            finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
        }

        if (estoque && cancelado) {
            for (OsProdutoServico item : os.getListaOsProdutoServico()) {
                if (item.getProduto().getServico().equals("N")) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(os.getEmpresa().getId(), item.getProduto().getId(), item.getQuantidade());
                }

            }
        }
        os.setOsStatus(Constantes.OS.STATUS_CANCELADO);
        salvar(os);
        Mensagem.addInfoMessage("OS cancelada com sucesso");
    }

    public void gerarDanfe(OsAbertura os) throws Exception {
        NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);

        nfeService.danfe(nfe);
    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }


    @Override
    protected Class<OsAbertura> getClazz() {
        return OsAbertura.class;
    }
}
