package com.chronos.bo.nfe;

import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.infra.enuns.ConsumidorOperacao;
import com.chronos.infra.enuns.IndicadorIe;
import com.chronos.infra.enuns.LocalDestino;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.TipoVenda;
import com.chronos.repository.Repository;
import com.chronos.util.cdi.ManualCDILookup;
import org.springframework.util.StringUtils;

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
    private PdvVendaCabecalho pdvVenda;
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

    public VendaToNFe(ModeloDocumento modelo, ConfiguracaoEmissorDTO configuracao, OsAbertura os) {
        this.modelo = modelo;
        this.os = os;
        cliente = os.getCliente();
        empresa = os.getEmpresa();
        tipoVenda = TipoVenda.OS;
        this.configuracao = configuracao;
        nfeUtil = new NfeUtil();

    }

    public VendaToNFe(ModeloDocumento modelo, ConfiguracaoEmissorDTO configuracao, PdvVendaCabecalho pdvVenda) {
        this.modelo = modelo;
        this.pdvVenda = pdvVenda;
        cliente = pdvVenda.getCliente();
        empresa = pdvVenda.getEmpresa();
        tipoVenda = TipoVenda.PDV;
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
        definirFormaPagamento();
        return nfe;
    }

    private void valoresPadrao() {

        nfe = nfeUtil.dadosPadroes(nfe, modelo, empresa, configuracao);
    }

    private void definirOperacaoTributaria() throws java.lang.Exception {
        if (cliente != null && cliente.getTributOperacaoFiscal() == null) {
            throw new Exception("Operação tributaria do Cliente " + cliente.getPessoa().getNome() + " não definida");
        }
        operacaoFiscal = cliente == null ? getOperacaoFiscalPadrao() : cliente.getTributOperacaoFiscal();
        nfe.setTributOperacaoFiscal(operacaoFiscal);
        nfe.setNaturezaOperacao(StringUtils.isEmpty(operacaoFiscal.getDescricaoNaNf()) ? operacaoFiscal.getDescricao() : operacaoFiscal.getDescricaoNaNf());
    }

    private void definirDestinatario() {
        if (cliente != null ) {
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
        String ufDestino = nfe.getDestinatario() != null && !StringUtils.isEmpty(nfe.getDestinatario().getUf()) ? nfe.getDestinatario().getUf() : nfe.getEmitente().getUf();
        LocalDestino localDestino = getLocalDestino(nfe.getEmitente().getUf(), ufDestino);


        // NFC-e não pode ser emitida para contribuinte do ICMS
        IndicadorIe indicador;
        if (modelo == ModeloDocumento.NFCE) {
            indicador = IndicadorIe.NAO_CONTRIBUINTE;
        } else if (nfe.getDestinatario().getCpfCnpj().length() == 14) {
            if (nfe.getDestinatario().getInscricaoEstadual() == null || nfe.getDestinatario().getInscricaoEstadual().equals("")) {
                indicador = IndicadorIe.NAO_CONTRIBUINTE;

            } else if (nfe.getDestinatario().getInscricaoEstadual().equalsIgnoreCase("ISENTO")) {
                indicador = IndicadorIe.CONTRIBUINTE_ISENTO;
            } else {
                indicador = IndicadorIe.CONTRIBUINTE_ICMS;
            }
        } else if (nfe.getDestinatario().getUf().equals("AM") || ufDestino.equals("BA")
                || ufDestino.equals("CE") || ufDestino.equals("GO")
                || ufDestino.equals("MG") || ufDestino.equals("MS")
                || ufDestino.equals("MT") || ufDestino.equals("PE")
                || ufDestino.equals("RN") || ufDestino.equals("SE")
                || ufDestino.equals("SP")) {
            indicador = IndicadorIe.NAO_CONTRIBUINTE;
        } else {
            indicador = IndicadorIe.CONTRIBUINTE_ISENTO;
        }
        ConsumidorOperacao consumidorOperacao = indicador == IndicadorIe.NAO_CONTRIBUINTE ? ConsumidorOperacao.FINAL : ConsumidorOperacao.NORMAL;
        nfe.getDestinatario().setIndicadorIe(indicador.getCodigo());
        nfe.setLocalDestino(localDestino.getCodigo());
        nfe.setConsumidorOperacao(consumidorOperacao.getCodigo());
    }

    private void gerarItensVenda() {
        itens = new ArrayList<>();
        if (tipoVenda == TipoVenda.VENDA) {
            venda.getListaVendaDetalhe().stream().forEach((itemVenda) -> {
                ItemVendaDTO item = new ItemVendaDTO(itemVenda);
                itens.add(item);
            });
        } else if (tipoVenda == TipoVenda.OS) {
            os.getListaOsProdutoServico().stream().forEach((itemOs) -> {
                ItemVendaDTO item = new ItemVendaDTO(itemOs);
                itens.add(item);
            });
        } else {
            pdvVenda.getListaPdvVendaDetalhe().stream().forEach((itemPdv) -> {
                ItemVendaDTO item = new ItemVendaDTO(itemPdv);
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


        if (tipoVenda == TipoVenda.PDV) {
            pdvVenda.getListaFormaPagamento().stream().forEach(f -> {
                NfeFormaPagamento pagamento = new NfeFormaPagamento();
                pagamento.setTroco(f.getTroco());
                pagamento.setBandeira(f.getBandeira());
                pagamento.setCartaoTipoIntegracao(f.getCartaoTipoIntegracao());
                pagamento.setCnpjOperadoraCartao(f.getCnpjOperadoraCartao());
                pagamento.setEstorno(f.getEstorno());
                pagamento.setForma(f.getForma());
                pagamento.setNumeroAutorizacao(f.getNumeroAutorizacao());
                pagamento.setPdvTipoPagamento(f.getPdvTipoPagamento());
                pagamento.setNfeCabecalho(nfe);
                pagamento.setValor(f.getValor());
                nfe.getListaNfeFormaPagamento().add(pagamento);
            });
        } else {
            FinTipoRecebimento tipoRecebimento = tipoVenda == TipoVenda.VENDA ? venda.getCondicoesPagamento().getTipoRecebimento() : os.getCondicoesPagamento().getTipoRecebimento();
            PdvTipoPagamento tipoPagamento = new PdvTipoPagamento();
            tipoPagamento = tipoPagamento.buscarPorCodigo(tipoRecebimento.getTipo());
            NfeFormaPagamento nfeFormaPagamento = new NfeFormaPagamento();
            nfeFormaPagamento.setPdvTipoPagamento(tipoPagamento);
            nfeFormaPagamento.setNfeCabecalho(nfe);
            nfeFormaPagamento.setForma(tipoRecebimento.getTipo());
            nfeFormaPagamento.setValor(tipoVenda == TipoVenda.VENDA ? venda.getValorTotal() : os.getValorTotal());
            nfe.getListaNfeFormaPagamento().add(nfeFormaPagamento);
        }
    }

    private LocalDestino getLocalDestino(String uf, String ufDestino) {
        return uf.equals(ufDestino) ? LocalDestino.INTERNA : LocalDestino.INTERESTADUAL;
    }

    private TributOperacaoFiscal getOperacaoFiscalPadrao() {
        Repository<TributOperacaoFiscal> operacaoRepository = getFacadeWithJNDI(Repository.class);

        return operacaoRepository.get(1, TributOperacaoFiscal.class);
    }


}
