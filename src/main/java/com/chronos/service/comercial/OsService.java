package com.chronos.service.comercial;

import com.chronos.bo.nfe.VendaToNFe;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Repository;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.util.Constantes;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by john on 13/12/17.
 */
public class OsService implements Serializable {

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

    private Empresa empresa;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();

    }

    public OsAbertura salvar(OsAbertura os) throws Exception {
        if (os.isNovo()) {
            repository.salvar(os);
            os.setNumero("OS" + new DecimalFormat("0000000").format(os.getId()));
            os = repository.atualizar(os);
        } else {
            os = repository.atualizar(os);
        }
        return os;
    }


    public OsAbertura salvarItem(OsAbertura os, OsProdutoServico item) throws Exception {
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

        ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(os.getEmpresa(), modelo);
        NfeCabecalho nfe;
        VendaToNFe vendaNfe = new VendaToNFe(modelo, configuracao, os);
        nfe = vendaNfe.gerarNfe();
        nfe.setCsc(configuracao.getCsc());
        nfe.setOs(os);

        StatusTransmissao status = nfeService.transmitirNFe(nfe, configuracao, atualizarEstoque);
        if (status == StatusTransmissao.AUTORIZADA) {
            String msg = modelo == ModeloDocumento.NFE ? "NFe transmitida com sucesso" : "NFCe transmitida com sucesso";
            Mensagem.addInfoMessage(msg);
        }


    }

    @Transactional
    public void faturar(OsAbertura os) throws Exception {
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
            ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
            ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(empresa, modelo);

            cancelado = nfeService.cancelarNFe(nfe, configuracao, estoque);
            if (cancelado) {
                finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
            }
        } else {
            finLancamentoReceberService.excluirFinanceiro(os.getNumero(), Modulo.OS);
        }

        if (estoque && cancelado) {
            for (OsProdutoServico item : os.getListaOsProdutoServico()) {
                if (item.getProduto().getServico().equals("N")) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                }

            }
        }
        os.setOsStatus(Constantes.OS.STATUS_CANCELADO);
        salvar(os);
        Mensagem.addInfoMessage("OS cancelada com sucesso");
    }

    public void gerarDanfe(OsAbertura os) throws Exception {
        NfeCabecalho nfe = nfeRepository.get(os.getIdnfeCabecalho(), NfeCabecalho.class);
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        ConfiguracaoEmissorDTO configuracao = nfeService.getConfEmisor(empresa, modelo);
        nfeService.danfe(nfe, configuracao);
    }

    private Optional<OsProdutoServico> buscarItem(Produto produto) {
        return itens.stream().filter(i -> i.getProduto().equals(produto))
                .findAny();
    }



}
