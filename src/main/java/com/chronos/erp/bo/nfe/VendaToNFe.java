package com.chronos.erp.bo.nfe;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.TipoVenda;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.cdi.ManualCDILookup;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.transmissor.infra.enuns.ConsumidorOperacao;
import com.chronos.transmissor.infra.enuns.IndicadorIe;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

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


    public VendaToNFe(ModeloDocumento modelo, VendaCabecalho venda) {
        this.modelo = modelo;
        this.venda = venda;
        cliente = venda.getCliente();
        empresa = venda.getEmpresa();
        tipoVenda = TipoVenda.VENDA;
        nfeUtil = new NfeUtil();

    }

    public VendaToNFe(ModeloDocumento modelo, OsAbertura os) {
        this.modelo = modelo;
        this.os = os;
        cliente = os.getCliente();
        empresa = os.getEmpresa();
        tipoVenda = TipoVenda.OS;
        nfeUtil = new NfeUtil();

    }

    public VendaToNFe(ModeloDocumento modelo, PdvVendaCabecalho pdvVenda) throws Exception {
        this.modelo = modelo;
        this.pdvVenda = pdvVenda;
        cliente = instanciaCliente(pdvVenda);
        empresa = pdvVenda.getEmpresa();
        tipoVenda = TipoVenda.PDV;
        nfeUtil = new NfeUtil();

    }


    public NfeCabecalho gerarNfe() throws Exception {

        nfe = nfeUtil.definirDadosPadrao(modelo, empresa);

        definirEmitente();

        definirDestinatario();


        definirOperacaoTributaria();
        gerarItensVenda();
        addItens();
        definirFormaPagamento();
        gerarDuplicatas();
        if (tipoVenda == TipoVenda.VENDA && venda.getTransportadora() != null) {
            nfe.setTransporte(new NfeTransporte());
            nfe.getTransporte().setNfeCabecalho(nfe);
            String cpfCnpj = venda.getTransportadora().getPessoa().getTipo().equals("F")
                    ? venda.getTransportadora().getPessoa().getPessoaFisica().getCpf()
                    : venda.getTransportadora().getPessoa().getPessoaJuridica().getCnpj();

            nfe.getTransporte().setTransportadora(venda.getTransportadora());
            nfe.getTransporte().setCpfCnpj(cpfCnpj);
            nfe.getTransporte().setEmpresaEndereco(venda.getTransportadora().getPessoa().buscarEnderecoPrincipal().getCidade());
            nfe.getTransporte().setNome(venda.getTransportadora().getPessoa().getNome());
            nfe.getTransporte().setMunicipio(venda.getTransportadora().getPessoa().buscarEnderecoPrincipal().getMunicipioIbge());
            nfe.getTransporte().setNomeMunicipio(venda.getTransportadora().getPessoa().buscarEnderecoPrincipal().getCidade());

            nfe.getTransporte().setUf(venda.getTransportadora().getPessoa().buscarEnderecoPrincipal().getUf());

            int modalidadeFrete = 0;
            if (!StringUtils.isEmpty(venda.getTipoFrete())) {
                modalidadeFrete = venda.getTipoFrete().equals("C") ? 0 : 1;
            }
            nfe.getTransporte().setModalidadeFrete(modalidadeFrete);
        }


        return nfe;
    }

    private Cliente instanciaCliente(PdvVendaCabecalho pdvVenda) throws Exception {
        Cliente cliente = pdvVenda.getCliente();

        if (cliente == null && !StringUtils.isEmpty(pdvVenda.getCpfCnpjCliente()) && !StringUtils.isEmpty(pdvVenda.getNomeCliente())) {

            cliente = new Cliente();
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(pdvVenda.getNomeCliente());
            if (pdvVenda.getCpfCnpjCliente().length() > 11) {
                PessoaJuridica pj = new PessoaJuridica();
                pj.setCnpj(pdvVenda.getCpfCnpjCliente());
                pessoa.setPessoaJuridica(pj);
                pessoa.setTipo("J");
            } else {
                PessoaFisica pf = new PessoaFisica();
                pf.setCpf(pdvVenda.getCpfCnpjCliente());
                pessoa.setPessoaFisica(pf);
                pessoa.setTipo("F");
            }
            cliente.setPessoa(pessoa);

            TributOperacaoFiscal operacaoFiscalPadrao = getOperacaoFiscalPadrao();

            cliente.setTributOperacaoFiscal(operacaoFiscalPadrao);

        }

        return cliente;
    }


    private void definirOperacaoTributaria() throws java.lang.Exception {
        if (cliente != null && cliente.getTributOperacaoFiscal() == null) {
            throw new Exception("Operação tributaria do Cliente " + cliente.getPessoa().getNome() + " não definida");
        }
        operacaoFiscal = cliente == null ? getOperacaoFiscalPadrao() : cliente.getTributOperacaoFiscal();
        nfe.setTributOperacaoFiscal(operacaoFiscal);
        nfe.setNaturezaOperacao(StringUtils.isEmpty(operacaoFiscal.getDescricaoNaNf()) ? operacaoFiscal.getDescricao() : operacaoFiscal.getDescricaoNaNf());
    }

    private void definirEmitente() {
        NfeEmitente emitente = nfeUtil.getEmitente(empresa);
        emitente.setNfeCabecalho(nfe);
        nfe.setEmitente(emitente);
    }

    private void definirDestinatario() throws ChronosException {

        if (modelo == ModeloDocumento.NFE && cliente == null) {
            throw new ChronosException("Para emissão de NF-e é preciso informar um cliente");
        }

        nfe.setDestinatario(new NfeDestinatario());
        nfe.getDestinatario().setNfeCabecalho(nfe);
        if (cliente != null) {
            if (cliente.getId() != null) {
                nfe.setCliente(cliente);
            }

            PessoaEndereco endereco = cliente.getPessoa().buscarEnderecoPrincipal();
            nfe.getDestinatario().setCpfCnpj(cliente.getPessoa().getIdentificador());
            nfe.getDestinatario().setNome(cliente.getPessoa().getNome());
            if (endereco != null) {
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

        }
        String ufDestino = nfe.getDestinatario() != null && !StringUtils.isEmpty(nfe.getDestinatario().getUf()) ? nfe.getDestinatario().getUf() : nfe.getEmitente().getUf();
        LocalDestino localDestino = getLocalDestino(nfe.getEmitente().getUf(), ufDestino);


        // NFC-e não pode ser emitida para contribuinte do ICMS
        IndicadorIe indicador;
        if (modelo == ModeloDocumento.NFE) {

            if (nfe.getDestinatario().getCpfCnpj().length() == 14) {
                if (nfe.getDestinatario().getInscricaoEstadual() == null || nfe.getDestinatario().getInscricaoEstadual().equals("")) {
                    indicador = IndicadorIe.NAO_CONTRIBUINTE;

                } else if (nfe.getDestinatario().getInscricaoEstadual().equalsIgnoreCase("ISENTO")) {
                    if (nfe.getDestinatario().getUf().equals("AM") || nfe.getDestinatario().getUf().equals("BA")
                            || nfe.getDestinatario().getUf().equals("CE") || nfe.getDestinatario().getUf().equals("GO")
                            || nfe.getDestinatario().getUf().equals("MG") || nfe.getDestinatario().getUf().equals("MS")
                            || nfe.getDestinatario().getUf().equals("MT") || nfe.getDestinatario().getUf().equals("PE")
                            || nfe.getDestinatario().getUf().equals("RN") || nfe.getDestinatario().getUf().equals("SE")
                            || nfe.getDestinatario().getUf().equals("SP")) {

                        indicador = IndicadorIe.NAO_CONTRIBUINTE;
                    } else {

                        indicador = IndicadorIe.CONTRIBUINTE_ISENTO;
                    }
                } else {
                    indicador = IndicadorIe.CONTRIBUINTE_ICMS;

                }

            } else {
                indicador = IndicadorIe.NAO_CONTRIBUINTE;
            }

        } else {

            indicador = IndicadorIe.NAO_CONTRIBUINTE;


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
                throw new ChronosException("Não existe NCM para o produto :" + item.getProduto().getDescricaoPdv() + " informado. Operação não realizada.");
            }

            if (empresa.buscarEnderecoPrincipal().getUf().equals("DF") || !item.getProduto().getServico().equals("S")) {

                NfeDetalhe itemNfe = new NfeDetalhe();
                itemNfe.setNfeCabecalho(nfe);
                itemNfe.setQuantidadeComercial(item.getQuantidade());
                itemNfe.setQuantidadeTributavel(item.getQuantidade());
                itemNfe.setProduto(item.getProduto());
                itemNfe.pegarInfoProduto();
                itemNfe.setClassificacaoContabilConta(operacaoFiscal.getClassificacaoContabilConta());
                BigDecimal valorVenda = item.getValor();
                valorVenda = valorVenda == null ? itemNfe.getProduto().getValorVenda() : valorVenda;
                itemNfe.setValorUnitarioComercial(valorVenda);
                itemNfe.setValorUnitarioTributavel(valorVenda);
                itemNfe.setValorDesconto(item.getDesconto());
                itemNfe.setNumeroItem(++numeroItem);
                itemNfe.setEntraTotal(1);

                itemNfe = nfeUtil.defineTributacao(itemNfe, operacaoFiscal, nfe.getDestinatario());
                itensNfe.add(itemNfe);
            }


        }

        nfe.getListaNfeDetalhe().addAll(itensNfe);


        nfeUtil.calcularTotalNFe(nfe);
        nfeUtil.definirTotaisTributos(nfe);
    }

    public void definirFormaPagamento() {


        if (tipoVenda == TipoVenda.PDV) {
            nfe.setTroco(pdvVenda.getTroco());
            pdvVenda.getListaFormaPagamento().stream().forEach(f -> {
                NfeFormaPagamento pagamento = preecherFormarPagamento(f.getFormaPagamento());
                nfe.getListaNfeFormaPagamento().add(pagamento);
            });
        } else if (tipoVenda == TipoVenda.OS) {
            os.getListaFormaPagamento().stream().forEach(f -> {
                NfeFormaPagamento pagamento = preecherFormarPagamento(f.getFormaPagamento());
                nfe.getListaNfeFormaPagamento().add(pagamento);
            });
        } else {

            venda.getListaFormaPagamento().stream().forEach(f -> {
                NfeFormaPagamento pagamento = preecherFormarPagamento(f.getFormaPagamento());
                nfe.getListaNfeFormaPagamento().add(pagamento);
            });


        }
    }

    public NfeFormaPagamento preecherFormarPagamento(FormaPagamento forma) {
        NfeFormaPagamento pagamento = new NfeFormaPagamento();
        pagamento.setTroco(forma.getTroco());
        pagamento.setBandeira(forma.getBandeira());
        pagamento.setCartaoTipoIntegracao(forma.getCartaoTipoIntegracao());
        pagamento.setCnpjOperadoraCartao(forma.getCnpjOperadoraCartao());
        pagamento.setEstorno(forma.getEstorno());
        pagamento.setForma(forma.getForma());
        pagamento.setNumeroAutorizacao(forma.getNumeroAutorizacao());
        pagamento.setTipoPagamento(forma.getTipoPagamento());
        pagamento.setNfeCabecalho(nfe);
        pagamento.setValor(forma.getValor());

        return pagamento;
    }

    public void gerarDuplicatas() throws ChronosException {


        for (NfeFormaPagamento f : nfe.getListaNfeFormaPagamento()) {

            if (f.getForma().equals("14")) {


                if (nfe.getListaDuplicata() == null) {
                    nfe.setListaDuplicata(new HashSet<>());
                }

                BigDecimal residuo;
                BigDecimal somaParcelas = BigDecimal.ZERO;
                BigDecimal valorParcela;
                int number = 0;

                FormaPagamento formaPagamento = tipoVenda == TipoVenda.OS
                        ? os.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals("14")).findFirst().get().getFormaPagamento()
                        : venda.getListaFormaPagamento().stream().filter(p -> p.getFormaPagamento().getForma().equals("14")).findFirst().get().getFormaPagamento();
                if (formaPagamento.getCondicoesPagamento() == null) {
                    throw new ChronosException("Para geração de Duplicatas na NFe é preciso informar a condição de pagamento");
                }

                List<CondicoesParcelas> parcelas = formaPagamento.getCondicoesPagamento().getParcelas();

                for (CondicoesParcelas parcela : parcelas) {
                    NfeDuplicata duplicata = new NfeDuplicata();
                    duplicata.setNfeCabecalho(nfe);
                    valorParcela = Biblioteca.calcularValorPercentual(nfe.getValorTotal(), parcela.getTaxa());
                    duplicata.setDataVencimento(Biblioteca.addDay(new Date(), parcela.getDias()));
                    duplicata.setValor(valorParcela);
                    somaParcelas = somaParcelas.add(valorParcela);
                    if (number == (parcelas.size() - 1)) {
                        residuo = nfe.getValorTotal().subtract(somaParcelas);
                        valorParcela = valorParcela.add(residuo);
                        duplicata.setValor(valorParcela);
                    }
                    duplicata.setNumero(String.format("%3s", String.valueOf(number++ + 1)));
                    nfe.getListaDuplicata().add(duplicata);
                }

                NfeFatura fatura = new NfeFatura();
                fatura.setNfeCabecalho(nfe);
                nfe.setFatura(fatura);

                String numFatura = String.valueOf(nfe.getListaDuplicata().size());
                BigDecimal valorOriginal = tipoVenda == TipoVenda.OS
                        ? Biblioteca.soma(nfe.getValorServicos(), Optional.ofNullable(nfe.getValorTotalProdutos()).orElse(BigDecimal.ZERO))
                        : nfe.getValorTotalProdutos();
                numFatura = org.apache.commons.lang3.StringUtils.leftPad(numFatura, 3, "0");
                fatura.setNumero(numFatura);
                fatura.setValorLiquido(nfe.getValorTotal());
                fatura.setValorOriginal(valorOriginal);
                fatura.setValorDesconto(nfe.getValorDesconto());

            }

        }

    }

    private LocalDestino getLocalDestino(String uf, String ufDestino) {
        return uf.equals(ufDestino) ? LocalDestino.INTERNA : LocalDestino.INTERESTADUAL;
    }

    private TributOperacaoFiscal getOperacaoFiscalPadrao() throws Exception {

        AdmParametro parametro = FacesUtil.getParamentos();

        if (parametro == null || parametro.getTributOperacaoFiscalPadrao() == null) {
            throw new Exception("Operação tributaria padrão não definida");
        }

        Repository<TributOperacaoFiscal> operacaoRepository = getFacadeWithJNDI(Repository.class);

        return operacaoRepository.get(parametro.getTributOperacaoFiscalPadrao(), TributOperacaoFiscal.class);
    }


}
