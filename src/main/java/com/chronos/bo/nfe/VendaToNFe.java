package com.chronos.bo.nfe;

import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.TipoVenda;
import com.chronos.util.cdi.ManualCDILookup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 18/09/17.
 */
public class VendaToNFe extends ManualCDILookup {

    private NfeCabecalho nfe;
    private Cliente cliente;
    private ModeloDocumento modelo;
    private Empresa empresa;
    private List<ItemVendaDTO> itens;
    private VendaCabecalho venda;
    private OsAbertura os;
    private TipoVenda tipoVenda;
    private TributOperacaoFiscal operacaoFiscal;
    private NfeUtil nfeUtil;
    private ConfiguracaoEmissorDTO configuracao;

    public VendaToNFe(ModeloDocumento modelo, ConfiguracaoEmissorDTO configuracao, VendaCabecalho venda) {
        this.modelo = modelo;
        this.venda = venda;
        cliente = venda.getCliente();
        empresa = venda.getEmpresa();
        tipoVenda = TipoVenda.VENDA;
        this.configuracao = configuracao;
        nfeUtil = new NfeUtil();

    }

    public NfeCabecalho gerarNfe() throws Exception {
        nfe = new NfeCabecalho();
        valoresPadrao();
        definirDestinatario();
        definirOperacaoTributaria();
        gerarItensVenda();
        addItens();
        nfeUtil.gerarNumeracao(nfe, empresa);
        definirFormaPagamento();
        return nfe;
    }

    private void valoresPadrao() {

        nfe = nfeUtil.dadosPadroes(nfe, modelo, empresa, configuracao);
    }

    private void definirOperacaoTributaria() throws java.lang.Exception {
        if (cliente.getTributOperacaoFiscal() == null) {
            throw new Exception("Operação tributaria do Cliente " + cliente.getPessoa().getNome() + " não definida");
        }
        operacaoFiscal = cliente.getTributOperacaoFiscal();
        nfe.setTributOperacaoFiscal(operacaoFiscal);
        nfe.setNaturezaOperacao(cliente.getTributOperacaoFiscal().getDescricaoNaNf());
    }

    private void definirDestinatario() {
        nfe.setCliente(cliente);
        PessoaEndereco endereco = cliente.getPessoa().buscarEnderecoPrincipal();
        nfe.getDestinatario().setCpfCnpj(cliente.getPessoa().getIdentificador());
        nfe.getDestinatario().setNome(cliente.getPessoa().getNome());
        nfe.getDestinatario().setLogradouro(endereco.getLogradouro());
        nfe.getDestinatario().setComplemento(endereco.getComplemento());
        nfe.getDestinatario().setNumero(endereco.getNumero());
        nfe.getDestinatario().setBairro(endereco.getBairro());
        nfe.getDestinatario().setNomeMunicipio(endereco.getCidade());
        nfe.getDestinatario().setCodigoMunicipio(endereco.getMunicipioIbge());
        nfe.getDestinatario().setUf(endereco.getUf());
        nfe.getDestinatario().setCep(endereco.getCep());
        nfe.getDestinatario().setTelefone(endereco.getFone());
        nfe.getDestinatario().setInscricaoEstadual(cliente.getPessoa().getRgOrIe());
        nfe.getDestinatario().setEmail(cliente.getPessoa().getEmail());
        nfe.getDestinatario().setCodigoPais(1058);
        nfe.getDestinatario().setNomePais("Brazil");
    }

    private void gerarItensVenda() {
        itens = new ArrayList<>();
        if (tipoVenda == TipoVenda.VENDA) {
            venda.getListaVendaDetalhe().stream().forEach((itemVenda) -> {
                ItemVendaDTO item = new ItemVendaDTO();
                item.setDesconto(itemVenda.getValorDesconto());
                item.setProduto(itemVenda.getProduto());
                item.setQuantidade(itemVenda.getQuantidade());
                item.setValor(itemVenda.getValorUnitario());
                item.setSubtotal(itemVenda.getValorSubtotal());
                item.setTotal(itemVenda.getValorTotal());
                itens.add(item);
            });
        } else {
            os.getListaOsProdutoServico().stream().forEach((itemOs) -> {
                ItemVendaDTO item = new ItemVendaDTO();
                item.setDesconto(itemOs.getValorDesconto());
                item.setProduto(itemOs.getProduto());
                item.setQuantidade(itemOs.getQuantidade());
                item.setValor(itemOs.getValorUnitario());
                item.setSubtotal(itemOs.getValorSubtotal());
                item.setTotal(itemOs.getValorTotal());

                itens.add(item);
            });
        }
    }

    private void addItens() throws Exception {
        int numeroItem = 0;

        List<NfeDetalhe> itensNfe = new ArrayList<>();
        NfeUtil nfeUtil = new NfeUtil();
        for (ItemVendaDTO item : itens) {
            if (item.getProduto().getNcm() == null) {
                throw new Exception("Não existe NCM para o produto :" + item.getProduto().getDescricaoPdv() + " informado. Operação não realizada.");
            }
            NfeDetalhe itemNfe = new NfeDetalhe();
            itemNfe.setNfeCabecalho(nfe);
            itemNfe.setQuantidadeComercial(item.getQuantidade());
            itemNfe.setProduto(item.getProduto());
            itemNfe.pegarInfoProduto();

            BigDecimal valorVenda = item.getValor();
            valorVenda = valorVenda == null ? itemNfe.getProduto().getValorVenda() : valorVenda;
            itemNfe.setValorUnitarioComercial(valorVenda);
            itemNfe.setQuantidadeComercial(item.getQuantidade());
            itemNfe.setValorDesconto(item.getDesconto());
            itemNfe.setNumeroItem(++numeroItem);
            itemNfe.setEntraTotal(1);

            itemNfe = nfeUtil.defineTributacao(itemNfe, empresa, operacaoFiscal, nfe.getDestinatario());
            itensNfe.add(itemNfe);
        }

        nfe.getListaNfeDetalhe().addAll(itensNfe);


        nfeUtil.calcularTotalNFe(nfe);
    }

    public void definirFormaPagamento() {
        FinTipoRecebimento tipoRecebimento = venda.getCondicoesPagamento().getTipoRecebimento();
        NfceTipoPagamento tipoPagamento = new NfceTipoPagamento();
        tipoPagamento = tipoPagamento.buscarPorCodigo(tipoRecebimento.getTipo());
        NfeFormaPagamento nfeFormaPagamento = new NfeFormaPagamento();
        nfeFormaPagamento.setNfceTipoPagamento(tipoPagamento);
        nfeFormaPagamento.setNfeCabecalho(nfe);
        nfeFormaPagamento.setForma(tipoRecebimento.getTipo());
        nfeFormaPagamento.setValor(venda.getValorTotal());
        nfe.getListaNfeFormaPagamento().add(nfeFormaPagamento);
    }


}
