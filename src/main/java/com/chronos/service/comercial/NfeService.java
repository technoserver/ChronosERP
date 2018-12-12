package com.chronos.service.comercial;

import br.com.samuelweb.certificado.exception.CertificadoException;
import br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TNfeProc;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.bo.nfe.NfeUtil;
import com.chronos.dto.*;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.EventoNfe;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.NfeRepository;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.FormatoImpressaoDanfe;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.transmissor.infra.enuns.StatusEnum;
import com.chronos.transmissor.init.Configuracoes;
import com.chronos.transmissor.nfe.Nfe;
import com.chronos.transmissor.util.ConstantesNFe;
import com.chronos.transmissor.util.ValidarNFe;
import com.chronos.transmissor.util.WebServiceUtil;
import com.chronos.transmissor.util.XmlUtil;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.Biblioteca;
import com.chronos.util.Constantes;
import com.chronos.util.FormatValor;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import com.chronos.util.report.JasperReportUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static java.nio.file.FileSystems.getDefault;

/**
 * Created by john on 26/09/17.
 */
public class NfeService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;

    @Inject
    private Repository<NotaFiscalTipo> tiposNotaFiscal;
    @Inject
    private Repository<NfeNumeroInutilizado> numeros;
    @Inject
    private Repository<NotaFiscalModelo> modelos;
    @Inject
    private Repository<NfeConfiguracao> configuracoesNfe;
    @Inject
    private Repository<PdvConfiguracao> configuracoesNfce;
    @Inject
    private Repository<NfeCabecalho> repository;
    @Inject
    private Repository<NfeXml> nfeXmlRepository;
    @Inject
    private Repository<OsAbertura> osRepository;
    @Inject
    private Repository<NfeEvento> eventoRepository;

    @Inject
    private EstoqueRepository produtos;

    @Inject
    private NfeRepository nfeRepository;

    @Inject
    private ExternalContext context;

    private boolean salvarXml;

    private ConfiguracaoEmissorDTO configuracao;
    private Configuracoes configuracoes;

    @PostConstruct
    private void init() {
        configuracoes = FacesUtil.getConfEmissor();
    }

    public ConfiguracaoEmissorDTO instanciarConfNfe(Empresa empresa, ModeloDocumento modelo, boolean buscar) throws ChronosException, CertificadoException {

        if (buscar) {
            configuracoes = null;
        }
        return instanciarConfNfe(empresa, modelo);
    }

    public ConfiguracaoEmissorDTO instanciarConfNfe(Empresa empresa, ModeloDocumento modelo) throws ChronosException, CertificadoException {
        this.empresa = empresa;
        if (configuracao == null) {
            if (modelo == ModeloDocumento.NFE) {
                ConfiguracaoNfeDTO configuracaoNfeDTO = configuracoesNfe.getNamedQuery(ConfiguracaoNfeDTO.class, "Nfe.configuracao", empresa.getId());

                if (configuracaoNfeDTO == null) {
                    throw new ChronosException("É preciso definir as configuracoes para NF-e");
                }
                configuracao = new ConfiguracaoEmissorDTO(configuracaoNfeDTO);

            } else {
                ConfiguracaoPdvDTO configuracaoPdvDTO = configuracoesNfe.getNamedQuery(ConfiguracaoPdvDTO.class, "Pdv.configuracao", empresa.getId());

                if (configuracaoPdvDTO == null) {
                    throw new ChronosException("É preciso definir as configuracoes para NF-e");
                }
                configuracao = new ConfiguracaoEmissorDTO(configuracaoPdvDTO);
            }
        }

        if (configuracoes == null) {
            validarConfEmissor(configuracao);
            configuracoes = NfeTransmissao.getInstance().iniciarConfiguracoes(new ConfEmissorDTO(Integer.valueOf(configuracao.getWebserviceUf()), configuracao.getCaminhoSchemas(),
                    configuracao.getCertificadoDigitalCaminho(), configuracao.getCertificadoDigitalSenha(), configuracao.getWebserviceAmbiente(), "4.00"));
        }


        return configuracao;


    }

    public NfeCabecalho dadosPadroes(ModeloDocumento modelo) throws ChronosException, CertificadoException {
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setFormatoImpressaoDanfe(modelo == ModeloDocumento.NFE ? FormatoImpressaoDanfe.DANFE_RETRATO.getCodigo() : FormatoImpressaoDanfe.DANFE_NFCE.getCodigo());
        nfe.setUfEmitente(empresa.getCodigoIbgeUf());
        nfe.setCodigoMunicipio(empresa.getCodigoIbgeCidade());
        nfe.setCodigoModelo(String.valueOf(modelo.getCodigo()));
        nfe.setEmpresa(empresa);


        nfe.getDestinatario().setNfeCabecalho(nfe);

        if (modelo == ModeloDocumento.NFE) {

            nfe.getFatura().setNfeCabecalho(nfe);

        }


        definirEmitente(nfe);

        definirFormaPagamento(nfe, new PdvTipoPagamento().buscarPorCodigo("01"));


        instanciarDadosConfiguracoes(nfe);

        return nfe;
    }

    public void instanciarDadosConfiguracoes(NfeCabecalho nfe) throws ChronosException, CertificadoException {
        if (configuracao == null) {
            instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento());
        }
        if (configuracao != null) {
            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            if (StringUtils.isEmpty(nfe.getInformacoesAddContribuinte())) {
                nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());
            }
            nfe.setCsc(configuracao.getCsc());

        }
    }

    @Transactional
    public NfeCabecalho salvar(NfeCabecalho nfe, PdvTipoPagamento tipoPagamento) throws ChronosException {


        validacaoNfe(nfe);


        definirFormaPagamento(nfe, tipoPagamento);

        int i = 0;
        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {
            item.setNumeroItem(++i);
        }

        nfe = repository.atualizar(nfe);
        int count = nfe.getListaNfeFormaPagamento().size();
        return nfe;
    }


    public ConfiguracaoEmissorDTO setarConfiguracoesNFe(NfeCabecalho nfe, ModeloDocumento modelo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));

        ConfiguracaoEmissorDTO configuracaoEmissorDTO = null;
        if (modelo == ModeloDocumento.NFE) {
            NfeConfiguracao configuracao = configuracoesNfe.get(NfeConfiguracao.class, filtros);

            if (configuracao == null) {
                throw new ChronosException("Configurações da NF-e  não definidas");
            }
            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setProcessoEmissao(configuracao.getProcessoEmissao());
            nfe.setVersaoProcessoEmissao(configuracao.getVersaoProcessoEmissao());

            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());
            configuracaoEmissorDTO = new ConfiguracaoEmissorDTO(configuracao);

        } else {
            PdvConfiguracao configuracao = configuracoesNfce.get(PdvConfiguracao.class, filtros);
            if (configuracao == null) {
                throw new ChronosException("Configurações da NFC-e  não definidas");
            }

            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());

            configuracaoEmissorDTO = new ConfiguracaoEmissorDTO(configuracao);
        }

        return configuracaoEmissorDTO;

    }

    public void baixaNfe(NfeCabecalho nfe) throws Exception {

        if (StatusTransmissao.isAutorizado(nfe.getStatusNota())) {

            String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());
            String arquivoPdf = pastaXml + System.getProperty("file.separator") + nfe.getNomePdf();
            String caminhoXml = pastaXml + System.getProperty("file.separator") + nfe.getNomeXml();
            File fileXml = new File(caminhoXml);
            File filePdf = new File(arquivoPdf);

            if (!fileXml.exists() || !filePdf.exists()) {
                gerarDanfe(nfe);
            }
            List<String> files = Arrays.asList(caminhoXml, arquivoPdf);
            File arquivoZip = ArquivoUtil.compactarArquivos(files, nfe.getChaveAcessoCompleta());
            FacesUtil.downloadArquivo(arquivoZip, nfe.getChaveAcessoCompleta() + ".zip");
        }

    }

    public void baixaXml(List<NfeCabecalho> nfes, String nomeArquivo) throws Exception {
        if (!nfes.isEmpty()) {
            List<String> arqvuivos = new ArrayList<>();
            for (NfeCabecalho nfe : nfes) {

                String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(nfe.getEmpresa().getCnpj());
                String arquivoPdf = pastaXml + System.getProperty("file.separator") + nfe.getNomePdf();
                String caminhoXml = pastaXml + System.getProperty("file.separator") + nfe.getNomeXml();
                File fileXml = new File(caminhoXml);
                File filePdf = new File(arquivoPdf);

                if (!fileXml.exists() || !filePdf.exists()) {
                    gerarDanfe(nfe);

                }
                arqvuivos.add(caminhoXml);
                arqvuivos.add(arquivoPdf);
            }
            File arquivoZip = ArquivoUtil.getInstance().compactarArquivos(arqvuivos, nomeArquivo);
            FacesUtil.downloadArquivo(arquivoZip, nomeArquivo + ".zip");
        }
    }

    public String inutilizarNFe(Empresa empresa, ModeloDocumento modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
        this.empresa = empresa;
        NotaFiscalTipo notaFiscalTipo = getNotaFicalTipo(modelo, org.apache.commons.lang3.StringUtils.leftPad(serie.toString(), 3, '0'));
        if (notaFiscalTipo == null) {
            throw new ChronosException("Não foi informando numeração para o modelo " + modelo);
        }
        instanciarConfNfe(empresa, modelo);
        TRetInutNFe.InfInut infRetorno = NfeTransmissao.getInstance().inutilizarNFe(serie, numInicial, numFinal, modelo, empresa.getCnpj(), justificativa);

        String resultado = "";
        switch (infRetorno.getCStat()) {
            case "102":
                for (int i = numInicial; i <= numFinal; i++) {
                    NfeNumeroInutilizado numInutilizado = new NfeNumeroInutilizado();
                    numInutilizado.setDataInutilizacao(new Date());
                    numInutilizado.setEmpresa(empresa);
                    numInutilizado.setNumero(i);
                    numInutilizado.setSerie(String.valueOf(serie));
                    numeros.salvar(numInutilizado);
                }

                if (numFinal > notaFiscalTipo.getUltimoNumero()) {


                    atualizarNumeroNfe(notaFiscalTipo, numFinal);
                }
                resultado = infRetorno.getXMotivo();
                break;
            case "241":
                resultado = infRetorno.getXMotivo();
                break;
            case "215":
                String xml = XmlUtil.objectToXml(infRetorno);
                String erroValidacao = ValidarNFe.validaXml(xml, "inutilizacao");
                Mensagem.addErrorMessage(erroValidacao);
                break;
            default:
                resultado = infRetorno.getXMotivo();
                break;
        }

        return resultado;
    }


    public NotaFiscalTipo gerarNumeracao(NfeCabecalho nfe) throws Exception {

        Integer numero;
        String serie;
        NotaFiscalTipo notaFiscalTipo = null;
        ModeloDocumento modelo = nfe.getModeloDocumento();
        if (StringUtils.isEmpty(nfe.getNumero())) {
            notaFiscalTipo = modelo == ModeloDocumento.NFE ? getNotaFicalTipo(modelo) : getNotaFicalTipo(modelo, nfe.getSerie());
            numero = notaFiscalTipo.proximoNumero();
            serie = notaFiscalTipo.getSerie();
        } else {
            numero = Integer.valueOf(nfe.getNumero());
            serie = nfe.getSerie();
        }

        nfe.setNumero(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        nfe.setCodigoNumerico(FormatValor.getInstance().formatarCodigoNumeroDocFiscalToString(numero));
        nfe.setSerie(serie);
        nfe.setChaveAcesso("" + empresa.getCodigoIbgeUf()
                + FormatValor.getInstance().formatarAno(new Date())
                + FormatValor.getInstance().formatarMes(new Date())
                + empresa.getCnpj()
                + nfe.getCodigoModelo()
                + nfe.getSerie()
                + nfe.getNumero()
                + "1"
                + nfe.getCodigoNumerico());
        nfe.setDigitoChaveAcesso(Biblioteca.modulo11(nfe.getChaveAcesso()).toString());
        return notaFiscalTipo;
    }


    public NfeDetalhe definirTributacao(NfeDetalhe item, TributOperacaoFiscal operacaoFiscal, NfeDestinatario destinatario) throws Exception {
        NfeUtil nfeUtil = new NfeUtil();
        item = nfeUtil.defineTributacao(item, operacaoFiscal, destinatario);
        return item;
    }

    public NfeCabecalho atualizarTotais(NfeCabecalho nfe) throws Exception {
        NfeUtil nFeUtil = new NfeUtil();
        return nFeUtil.calcularTotalNFe(nfe);
    }

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto;
        List<Filtro> filtros = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNumeric(descricao)) {
            filtros.add(new Filtro(Filtro.AND, "id", Filtro.IGUAL, descricao));
            // listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        } else {
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, descricao.trim()));
            //  listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        }

        listaProduto = produtos.getProdutoEmpresa(descricao, empresa);
        return listaProduto;
    }



    public void gerarDuplicatas(NfeCabecalho nfe, VendaCondicoesPagamento condicoesPagamento, Date primeiroVencimento, int intervaloParcelas, int qtdParcelas) throws Exception {

        if (nfe.getValorTotal() == null || nfe.getValorTotal().signum() == 0) {
            throw new ChronosException("O valor total da NFe deve ser maior que 0");
        }
        if (condicoesPagamento == null && (primeiroVencimento == null || intervaloParcelas == 0 || qtdParcelas == 0)) {
            throw new ChronosException("Se a condição de pagament não for informada é preciso informa o primeiro vencimento,intervalo de parcelas e a quantidade.");
        }
        if (nfe.getListaDuplicata() == null) {
            nfe.setListaDuplicata(new HashSet<>());
        }
        nfe.getListaDuplicata().clear();
        BigDecimal residuo;
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal valorParcela;
        if (condicoesPagamento != null) {
            int number = 0;
            for (VendaCondicoesParcelas parcelas : condicoesPagamento.getParcelas()) {
                NfeDuplicata duplicata = new NfeDuplicata();
                duplicata.setNfeCabecalho(nfe);
                valorParcela = Biblioteca.calcularValorPercentual(nfe.getValorTotal(), parcelas.getTaxa());
                duplicata.setDataVencimento(Biblioteca.addDay(new Date(), parcelas.getDias()));
                duplicata.setValor(valorParcela);
                somaParcelas = somaParcelas.add(valorParcela);
                if (number == (condicoesPagamento.getParcelas().size() - 1)) {
                    residuo = nfe.getValorTotal().subtract(somaParcelas);
                    valorParcela = valorParcela.add(residuo);
                    duplicata.setValor(valorParcela);
                }
                duplicata.setNumero(String.format("%3s", String.valueOf(number++ + 1)));
                nfe.getListaDuplicata().add(duplicata);
            }
        } else {
            Calendar firstVencimento = Calendar.getInstance();
            firstVencimento.setTime(primeiroVencimento);
            valorParcela = nfe.getValorTotal().divide(BigDecimal.valueOf(qtdParcelas), RoundingMode.HALF_DOWN);

            for (int i = 0; i < qtdParcelas; i++) {
                NfeDuplicata duplicata = new NfeDuplicata();
                duplicata.setNfeCabecalho(nfe);
                duplicata.setNumero(String.format("%3s", String.valueOf(i + 1)));
                if (i > 0) {
                    firstVencimento.add(Calendar.DAY_OF_MONTH, intervaloParcelas);
                }
                duplicata.setDataVencimento(firstVencimento.getTime());
                duplicata.setValor(valorParcela);

                somaParcelas = somaParcelas.add(valorParcela);
                if (i == (qtdParcelas - 1)) {
                    residuo = nfe.getValorTotal().subtract(somaParcelas);
                    valorParcela = valorParcela.add(residuo);
                    duplicata.setValor(valorParcela);
                }
                nfe.getListaDuplicata().add(duplicata);
            }
        }


    }

    public String gerarNfePreProcessada(NfeCabecalho nfe) throws Exception {
        String xml = NfeTransmissao.getInstance().gerarXmlNfe(nfe);
        return salvarXml(xml, TipoArquivo.NFePreProcessada, nfe.getChaveAcesso() + nfe.getDigitoChaveAcesso() + ".xml");
    }




    public void visualizarXml(String caminho) throws Exception {
        File file = new File(caminho);
        if (!file.exists()) {
            throw new ChronosException("Arquivo não encontrado");
        }
        FacesContext fc = FacesContext.getCurrentInstance();

        byte[] conteudo = Biblioteca.getBytesFromFile(new File(caminho));
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        response.reset();
        response.setContentType("application/xml");
        response.setContentLength(conteudo.length);
        response.setHeader("Content-disposition", "inline; filename=nfe.xml");
        response.getOutputStream().write(conteudo);
        response.getOutputStream().flush();
        response.getOutputStream().flush();
        fc.responseComplete();
    }

    public void verificarStatusNota(NfeCabecalho nfe) throws Exception {
        if (StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new ChronosException("Esta NF-e já foi autorizada. Operação não permitida ");
        } else if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new ChronosException("Esta NF-e já foi autorizada. Operação não permitida ");
        }
    }

    public String consultarStatusNfe(ModeloDocumento modelo) throws Exception {


        return NfeTransmissao.getInstance().statusServico();
    }



    @Transactional
    public StatusTransmissao transmitirNFe(NfeCabecalho nfe, boolean atualizarEstoque) throws Exception {
        validacaoNfe(nfe);
        nfe.setDataHoraEmissao(new Date());

        if (StatusTransmissao.DUPLICIDADE == nfe.getStatusTransmissao()) {
            nfe.setNumero("");
            nfe.setChaveAcesso("");
        }

        if (nfe.getTransporte() == null || nfe.getTransporte().getModalidadeFrete() == null || nfe.getTransporte().getModalidadeFrete().equals("0")) {
            nfe.setTransporte(null);
        }

        VendaCabecalho venda = nfe.getVendaCabecalho();
        OsAbertura os = nfe.getOs();
        EstoqueTransferenciaCabecalho transferencia = nfe.getTransferencia();
        PdvVendaCabecalho pdv = nfe.getPdv();
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        StatusTransmissao status = StatusTransmissao.ENVIADA;

        String tipo = modelo == ModeloDocumento.NFE ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        NotaFiscalTipo notaFiscalTipo = gerarNumeracao(nfe);
        TEnviNFe nfeEnv = NfeTransmissao.getInstance().geraNFeEnv(nfe);
        nfe.setQrcode(modelo == ModeloDocumento.NFCE ? nfeEnv.getNFe().get(0).getInfNFeSupl().getQrCode() : "");
        TRetEnviNFe retorno = Nfe.enviarNfe(nfeEnv, tipo);

        if (retorno.getCStat().equals("104")) {
            if (retorno.getProtNFe().getInfProt().getCStat().equals("100")) {

                nfe.setNumeroProtocolo(retorno.getProtNFe().getInfProt().getNProt());
                nfe.setVersaoAplicativo(retorno.getProtNFe().getInfProt().getVerAplic());
                nfe.setDataHoraProcessamento(FormatValor.getInstance().formatarDataNota(retorno.getProtNFe().getInfProt().getDhRecbto()));
                String xmlProc = XmlUtil.criaNfeProc(nfeEnv, retorno.getProtNFe());
                nfe.setStatusNota(StatusTransmissao.AUTORIZADA.getCodigo());
                nfe = nfeRepository.procedimentoNfeAutorizada(nfe, atualizarEstoque);
                atualizarNumeroNfe(notaFiscalTipo, Integer.valueOf(nfe.getNumero()));
                salvaNfeXml(xmlProc, nfe);
                salvarXml(xmlProc, TipoArquivo.NFe, nfe.getNomeXml());

                if (venda != null) {
                    venda.setNumeroFatura(nfe.getId());
                    nfe.setVendaCabecalho(venda);
                }
                if (os != null) {
                    os.setOsStatus(Constantes.OS.STATUS_EMITIDO);
                    os.setIdnfeCabecalho(nfe.getId());
                    osRepository.atualizar(os);
                }
                if (pdv != null) {
                    pdv.setIdnfe(nfe.getId());
                    nfe.setPdv(pdv);
                }

                if (transferencia != null) {
                    transferencia.setIdnfecabeclaho(nfe.getId());
                }

                status = StatusTransmissao.AUTORIZADA;

            } else if (retorno.getProtNFe().getInfProt().getCStat().equals("204")
                    || retorno.getProtNFe().getInfProt().getCStat().equals("539")) {
                status = StatusTransmissao.DUPLICIDADE;
                nfe.setStatusNota(StatusTransmissao.DUPLICIDADE.getCodigo());
                nfe = repository.atualizar(nfe);
                Mensagem.addErrorMessage(retorno.getProtNFe().getInfProt().getXMotivo());
            } else {
                salvarXml = true;
                Mensagem.addErrorMessage(retorno.getProtNFe().getInfProt().getXMotivo());
            }
        } else if (retorno.getCStat().equals("215") || retorno.getCStat().equals("225")) {
            status = StatusTransmissao.SCHEMA_INVALIDO;
            if (configuracoes == null || org.springframework.util.StringUtils.isEmpty(configuracoes.getPastaSchemas())) {
                Mensagem.addErrorMessage("Preenchimento do xml invalido para mais detalhes informes o camminho dos schemas para validação");
            } else {
                String xml = XmlUtil.objectToXml(nfeEnv);
                String erroValidacao = ValidarNFe.validaXml(xml, ValidarNFe.ENVIO);
                Mensagem.addErrorMessage(erroValidacao);
            }

        } else {
            Mensagem.addErrorMessage(retorno.getXMotivo());
        }

        if (salvarXml) {
            String xml = XmlUtil.objectToXml(nfeEnv);
            salvarXml(xml, TipoArquivo.NFePreProcessada, nfe.getChaveAcessoCompleta() + ".xml");

        }

        return status;
    }

    public void imprimirCartaCorrecao(NfeEvento evento, NfeCabecalho nfe) throws Exception {


        String nomeRelatorioJasper = "CartaCorrecao";
        HashMap parametrosRelatorio = new LinkedHashMap();


        parametrosRelatorio.put("chaveAcesso", nfe.getChaveAcessoCompleta());

        parametrosRelatorio.put("protocolo", evento.getProtocolo());
        parametrosRelatorio.put("justificativa", evento.getJustificativa());

        parametrosRelatorio.put("orgao", empresa.buscarEnderecoPrincipal().getUf());
        parametrosRelatorio.put("seqEvento", evento.getSequencia().toString());
        parametrosRelatorio.put("cnpj", empresa.getCnpj());
        parametrosRelatorio.put("dataHoraEvento", FormatValor.getInstance().formatarDataNota(evento.getDataHora()));


        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        JasperReportUtil report = new JasperReportUtil(response);

        report.gerarRelatorioBaixa(parametrosRelatorio, Constantes.CAMINHODANFE, nomeRelatorioJasper, "CartaCorrecao.pdf");


    }

    public void danfe(NfeCabecalho nfe) throws Exception {
        empresa = nfe.getEmpresa();
        String cnpj = empresa.getCnpj();
        String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(cnpj);
        String arquivoPdf = pastaXml + System.getProperty("file.separator") + nfe.getNomePdf();
        String caminhoXml = pastaXml + System.getProperty("file.separator") + nfe.getNomeXml();
        File fileXml = new File(caminhoXml);
        File filePdf = new File(arquivoPdf);

        if (!filePdf.exists() && !fileXml.exists()) {
            System.out.println("Na existe nem xml e nem pdf");
        }

        if (filePdf.exists()) {
            FacesUtil.downloadArquivo(filePdf, filePdf.getName());
        } else {
            gerarDanfe(nfe);
            FacesUtil.downloadArquivo(filePdf, filePdf.getName());
        }
    }

    public void gerarDanfe(NfeCabecalho nfe) throws Exception {

        empresa = nfe.getEmpresa();
        StatusTransmissao statusTransmissao = StatusTransmissao.valueOfCodigo(nfe.getStatusNota());
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        String caminho = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());

        caminho += System.getProperty("file.separator") + nfe.getNomeXml();

        HashMap parametrosRelatorio = new HashMap<>();
        String camLogo = ArquivoUtil.getInstance().getImagemTransmissao(empresa.getCnpj());
        camLogo = new File(camLogo).exists() ? camLogo : context.getRealPath("resources/images/logo_nfe_peq.png");
        Image logo = new ImageIcon(camLogo).getImage();
        // logo.flush();
        parametrosRelatorio.put("Logo", logo);
        parametrosRelatorio.put("danfe_logo", logo);
        String expressaoDataSource = "";
        String nomeRelatorioJasper = "";

        expressaoDataSource = "/nfeProc/NFe/infNFe/det";
        if (statusTransmissao == StatusTransmissao.AUTORIZADA) {

            if (!new File(caminho).exists()) {
                gerarXml(nfe.getId(), nfe.getNomeXml());
            }

            if (modelo == ModeloDocumento.NFE) {
                nomeRelatorioJasper = Constantes.JASPERNFE;
                JRXmlDataSource faturaDataSource = new JRXmlDataSource(caminho, "//dup");
                InputStream inFt = this.getClass().getResourceAsStream("com/chronos/erplight/relatorios/comercial/nfe/DanfeRetratoFatura.jasper");
                parametrosRelatorio.put("Fatura_Datasource", faturaDataSource);
                parametrosRelatorio.put("danfeRetratoFatura", inFt);
            } else {

                nomeRelatorioJasper = Constantes.JASPERNFCE;

                String url = WebServiceUtil.getUrl(ConstantesNFe.NFCE, ConstantesNFe.SERVICOS.URL_CONSULTANFCE);

                if (nfe.getQrcode() == null) {
                    throw new ChronosException("Para NFC-e com chave " + nfe.getChaveAcessoCompleta() + " está faltando QRCode");
                }

                BufferedImage image = MatrixToImageWriter
                        .toBufferedImage(new QRCodeWriter().encode(nfe.getQrcode(), BarcodeFormat.QR_CODE, 300, 300));
                parametrosRelatorio.put("QR_CODE", image);
                parametrosRelatorio.put("ENDERECO_SEFAZ", url);
                parametrosRelatorio.put("operador", FacesUtil.getUsuarioSessao().getLogin());

                JRXmlDataSource faturaDataSource = new JRXmlDataSource(caminho, "//pag");
                parametrosRelatorio.put("Fatura_Datasource", faturaDataSource);
            }


        } else {

            if (!new File(caminho).exists()) {
                gerarXml(nfe.getId(), nfe.getNomeXml());
            }


            br.inf.portalfiscal.nfe.schema_4.enviNFe.TNfeProc nfeProc = XmlUtil.xmlToObject(XmlUtil.leXml(caminho), TNfeProc.class);

            TNFe.InfNFe infNFe = nfeProc.getNFe().getInfNFe();
            String data = nfeProc.getProtNFe().getInfProt().getDhRecbto();
            String[] split = data.split("-");
            expressaoDataSource = "/";
            nomeRelatorioJasper = "EventoCancelamento";
            parametrosRelatorio.put("numNota", infNFe.getIde().getNNF());
            parametrosRelatorio.put("serie", infNFe.getIde().getSerie());
            parametrosRelatorio.put("mesAno", split[0] + "/" + split[1]);

            parametrosRelatorio.put("chaveAcesso", infNFe.getId().substring(3));
            parametrosRelatorio.put("modelo", infNFe.getIde().getMod());
            parametrosRelatorio.put("protocolo", nfeProc.getProtNFe().getInfProt().getNProt());
            parametrosRelatorio.put("justificativa", nfe.getJustificativaCancelamento());
            parametrosRelatorio.put("status", "101 - pedido de cancelamento");
            parametrosRelatorio.put("ambiente", infNFe.getIde().getTpAmb());
            parametrosRelatorio.put("orgao", infNFe.getIde().getCUF());
            parametrosRelatorio.put("tipoEvento", "110111");
            parametrosRelatorio.put("seqEvento", "1");
            parametrosRelatorio.put("versaoEvento", "1.00");

            parametrosRelatorio.put("dataHoraEvento", data);
            parametrosRelatorio.put("dataHoraRegistro", data);
            parametrosRelatorio.put("descEvento", "Cancelamento");
        }

        JasperReportUtil report = new JasperReportUtil();

        byte[] pdfFile = report.gerarRelatorio(new JRXmlDataSource(caminho, expressaoDataSource), parametrosRelatorio, Constantes.CAMINHODANFE, nomeRelatorioJasper);
        String caminhoPdf = null;
        if (modelo == ModeloDocumento.NFE) {
            caminhoPdf = ArquivoUtil.getInstance().escrever(TipoArquivo.NFe, empresa.getCnpj(), pdfFile, nfe.getNomePdf());
        } else {
            File fileTemp = new File(context.getRealPath("/") + System.getProperty("file.separator") + "temp");
            if (!fileTemp.exists()) {
                fileTemp.mkdir();
            }
            Path localPdf = getDefault().getPath(fileTemp.getPath(), "cupom" + nfe.getNumero() + ".pdf");
            ArquivoUtil.getInstance().escreverComCopia(TipoArquivo.NFCe, empresa.getCnpj(), pdfFile, nfe.getNomePdf(), localPdf);
        }


    }

    public NfeEvento cartaCorrecao(NfeCabecalho nfe, String justificativa) throws Exception {

        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new ChronosException("NF-e náo autorizada. Cancelamento náo permitido!");
        }

        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("idnfecabecalho", nfe.getId()));
        Integer sequencia = (Integer) eventoRepository.getMaxValor(NfeEvento.class, "sequencia", filtros);
        sequencia++;

        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));

        EventoDTO evento = new EventoDTO();
        evento.setProtocolo(nfe.getNumeroProtocolo());
        evento.setMotivo(justificativa);
        evento.setChave(nfe.getChaveAcessoCompleta());
        evento.setCnpj(empresa.getCnpj());
        evento.setSequencia(sequencia);


        TRetEnvEvento retorno = NfeTransmissao.getInstance().enviarCartaCorrecao(evento);


        if (!StatusEnum.LOTE_EVENTO_PROCESSADO.getCodigo().equals(retorno.getCStat())) {
            throw new EmissorException("Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
        }

        if (!StatusEnum.EVENTO_VINCULADO.getCodigo().equals(retorno.getRetEvento().get(0).getInfEvento().getCStat())) {
            throw new EmissorException("Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat() + " - Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
        }


        NfeEvento nfeEvento = new NfeEvento();

        nfeEvento.setDataHora(new Date());
        nfeEvento.setIdnfecabecalho(nfe.getId());
        nfeEvento.setJustificativa(justificativa);
        nfeEvento.setTipo(EventoNfe.CARTA_CORRECAO);
        nfeEvento.setSequencia(sequencia);
        nfeEvento.setProtocolo(retorno.getRetEvento().get(0).getInfEvento().getNProt());

        eventoRepository.salvar(nfeEvento);

        String result = "";
        result += "Status:" + retorno.getRetEvento().get(0).getInfEvento().getCStat() + " \n";
        result += "Motivo:" + retorno.getRetEvento().get(0).getInfEvento().getXMotivo() + " \n";
        result += "Data:" + retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento();


        Mensagem.addInfoMessage(result);

        return nfeEvento;

    }

    @Transactional
    public boolean cancelarNFe(NfeCabecalho nfe, boolean estoque) throws Exception {

        boolean cancelado = false;
        if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new ChronosException("NF-e já cancelada. Cancelamento náo permitido!");
        }
        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new ChronosException("NF-e náo autorizada. Cancelamento náo permitido!");
        }

        instanciarConfNfe(nfe.getEmpresa(), nfe.getModeloDocumento());
        EventoDTO evento = new EventoDTO();
        evento.setProtocolo(nfe.getNumeroProtocolo());
        evento.setMotivo(nfe.getJustificativaCancelamento());
        evento.setChave(nfe.getChaveAcessoCompleta());
        evento.setCnpj(empresa.getCnpj());
        evento.setModeloDocumento(nfe.getModeloDocumento());
        RetornoEventoDTO retorno = NfeTransmissao.getInstance().cancelarNFe(evento);

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("id", nfe.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("status_nota", StatusTransmissao.CANCELADA.getCodigo());
        if (retorno.getRetorno().getCStat().equals("128")) {
            if (retorno.getRetorno().getRetEvento().get(0).getInfEvento().getCStat().equals("135")) {
                nfe.setNumeroProtocolo(retorno.getRetorno().getRetEvento().get(0).getInfEvento().getNProt());
                nfe.setVersaoAplicativo(retorno.getRetorno().getRetEvento().get(0).getInfEvento().getVerAplic());
                nfe.setDataHoraProcessamento(FormatValor.getInstance().formatarDataNota(retorno.getRetorno().getRetEvento().get(0).getInfEvento().getDhRegEvento()));
                nfe.setStatusNota(StatusTransmissao.CANCELADA.getCodigo());
                nfe = nfeRepository.procedimentoNfeCancelada(nfe, estoque);
                salvarXmlCancelado(nfe);
                cancelado = true;
            } else if (retorno.getRetorno().getRetEvento().get(0).getInfEvento().getCStat().equals("573")) {
                cancelado = repository.updateNativo(NfeCabecalho.class, filtros, atributos);
                Mensagem.addInfoMessage(retorno.getRetorno().getRetEvento().get(0).getInfEvento().getXMotivo());
            } else {
                Mensagem.addInfoMessage(retorno.getRetorno().getRetEvento().get(0).getInfEvento().getXMotivo());
            }
        } else {
            Mensagem.addInfoMessage(retorno.getRetorno().getXMotivo());
        }
        return cancelado;
    }

    @Transactional
    public void verificarSituacao(NfeCabecalho nfe) throws Exception {

        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        TRetConsSitNFe result = consultarNfe(nfe.getChaveAcessoCompleta(), modelo);

        if (result.getCStat().equals("100")) {
            NotaFiscalTipo notaFiscalTipo = getNotaFicalTipo(modelo);
            TEnviNFe nfeEnv = NfeTransmissao.getInstance().geraNFeEnv(nfe);
            nfe.setNumeroProtocolo(result.getProtNFe().getInfProt().getNProt());
            nfe.setVersaoAplicativo(result.getProtNFe().getInfProt().getVerAplic());
            String xmlProc = XmlUtil.criaNfeProc(nfeEnv, result.getProtNFe());
            nfe.setStatusNota(StatusTransmissao.AUTORIZADA.getCodigo());
            nfe = nfeRepository.procedimentoNfeAutorizada(nfe, false);
            if (Integer.valueOf(nfe.getNumero()) == notaFiscalTipo.getUltimoNumero()) {
                atualizarNumeroNfe(notaFiscalTipo, Integer.valueOf(nfe.getNumero()));
            }
            salvaNfeXml(xmlProc, nfe);
            salvarXml(xmlProc, TipoArquivo.NFe, nfe.getNomeXml());
        } else {
            nfe.setStatusNota(StatusTransmissao.EDICAO.getCodigo());
            nfe = nfeRepository.procedimentoNfeAutorizada(nfe, false);
        }

    }

    public TRetConsSitNFe consultarNfe(String chave, ModeloDocumento modelo) throws Exception {



        TRetConsSitNFe result = NfeTransmissao.getInstance().consultarNfe(chave);

        return result;
    }

    public void validacaoNfe(NfeCabecalho nfe) throws ChronosException {
        if (StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new ChronosException("Esta NF-e já foi autorizada. Operação não permitida ");
        } else if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new ChronosException("Esta NF-e já foi autorizada. Operação não permitida ");
        }
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        if (modelo == ModeloDocumento.NFCE && StringUtils.isEmpty(nfe.getCsc())) {
            throw new ChronosException("É Necessário informar o CSC!");
        }
        LocalDestino destino = LocalDestino.getByCodigo(nfe.getLocalDestino());
        int cfop = 0;
        int cfopAux = 0;

        if (nfe.getListaNfeDetalhe() == null || nfe.getListaNfeDetalhe().isEmpty()) {
            throw new ChronosException("Não foram definido itens para emissão da NFe");
        }
        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {
            cfopAux = item.getCfop();
            if (cfop == 0) {
                cfop = item.getCfop();
            }
        }
        if (destino == LocalDestino.INTERESTADUAL && cfop < 6000) {
            throw new ChronosException("CFOP :" + cfop + " inválido para operações Interestadual");
        }

        LocalDestino local = LocalDestino.getByCodigo(nfe.getLocalDestino());
        if (local == LocalDestino.INTERESTADUAL && modelo == ModeloDocumento.NFCE) {
            throw new ChronosException("Emissão de NFCe somente para operações locais");
        }

        BigDecimal valorDucplicata = BigDecimal.ZERO;

        if (nfe.getListaDuplicata() != null && !nfe.getListaDuplicata().isEmpty()) {
            valorDucplicata = nfe.getListaDuplicata().stream()
                    .map(NfeDuplicata::getValor)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }


        if (valorDucplicata.signum() == 1 && valorDucplicata.compareTo(nfe.getValorTotal()) != 0) {
            throw new ChronosException("Soma dos valores da duplicata Invalida");
        }

    }


    public NotaFiscalTipo getNotaFicalTipo(ModeloDocumento modelo) throws ChronosException {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo.getCodigo().toString()));

        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = tiposNotaFiscal.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new ChronosException("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }


        return notaFiscalTipo;
    }

    public NotaFiscalTipo getNotaFicalTipo(ModeloDocumento modelo, String serie) throws ChronosException {

        if (modelo == null || StringUtils.isEmpty(serie)) {
            String msg = modelo == null ? "Modelo não definido" : "Serie não definida";
            throw new ChronosException("Configuração de " + msg);
        }

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo.getCodigo().toString()));
        filtros.add(new Filtro(Filtro.AND, "serie", Filtro.IGUAL, serie));
        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = tiposNotaFiscal.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new ChronosException("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }

        notaFiscalTipo.setUltimoNumero(notaFiscalTipo.proximoNumero());

        return notaFiscalTipo;
    }

    public List<PdvTipoPagamento> getTipoPagamentos() {
        List<PdvTipoPagamento> tipos = new ArrayList<>();
        tipos.add(new PdvTipoPagamento(1, "01", "Dinheiro", "S", "N"));
        tipos.add(new PdvTipoPagamento(2, "02", "Cheque", "N", "S"));
        tipos.add(new PdvTipoPagamento(3, "03", "Cartão de Credito", "N", "S"));
        tipos.add(new PdvTipoPagamento(4, "04", "Cartão de Debito", "N", "N"));
        tipos.add(new PdvTipoPagamento(5, "05", "Credito Loja", "N", "N"));
        tipos.add(new PdvTipoPagamento(6, "14", "Duplicata Mercantil", "N", "S"));
        tipos.add(new PdvTipoPagamento(7, "10", "Vale Alimentacao", "N", "N"));
        tipos.add(new PdvTipoPagamento(8, "11", "Vale Refeicao", "N", "N"));
        tipos.add(new PdvTipoPagamento(9, "12", "Vale Presente", "N", "N"));
        tipos.add(new PdvTipoPagamento(10, "13", "Vale Combustivel", "N", "N"));
        tipos.add(new PdvTipoPagamento(11, "15", "Boleto Bancario", "N", "N"));
        tipos.add(new PdvTipoPagamento(12, "90", "Sem pagamento", "N", "S"));
        tipos.add(new PdvTipoPagamento(13, "99", "Outros", "N", "N"));

        return tipos;
    }

    public PdvTipoPagamento instanciarFormaPagamento(NfeCabecalho nfe) {
        Optional<NfeFormaPagamento> formaPagOpt = nfe.getListaNfeFormaPagamento().stream().findFirst();
        PdvTipoPagamento tipoPagamento = null;
        if (formaPagOpt.isPresent()) {
            NfeFormaPagamento forma = formaPagOpt.get();
            Optional<PdvTipoPagamento> tipoPagOpt = getTipoPagamentos()
                    .stream()
                    .filter(p -> p.getCodigo().equals(forma.getForma()))
                    .findFirst();

            if (tipoPagOpt.isPresent()) {
                tipoPagamento = tipoPagOpt.get();
            }

        }

        return tipoPagamento;
    }

    public void cleanConf() {
        FacesUtil.setConfEmissor(null);
    }

    public void definirIndicadorIe(NfeDestinatario destinatario, ModeloDocumento modelo) {
        if (modelo == ModeloDocumento.NFE) {

            if (destinatario.getCpfCnpj().length() == 14) {
                if (destinatario.getInscricaoEstadual() == null || destinatario.getInscricaoEstadual().equals("")) {
                    destinatario.setIndicadorIe(9);

                } else if (destinatario.getInscricaoEstadual().equalsIgnoreCase("ISENTO")) {
                    if (destinatario.getUf().equals("AM") || destinatario.getUf().equals("BA")
                            || destinatario.getUf().equals("CE") || destinatario.getUf().equals("GO")
                            || destinatario.getUf().equals("MG") || destinatario.getUf().equals("MS")
                            || destinatario.getUf().equals("MT") || destinatario.getUf().equals("PE")
                            || destinatario.getUf().equals("RN") || destinatario.getUf().equals("SE")
                            || destinatario.getUf().equals("SP")) {
                        destinatario.setIndicadorIe(9);
                    } else {
                        destinatario.setIndicadorIe(2);
                    }
                } else {
                    destinatario.setIndicadorIe(1);

                }

            } else {
                destinatario.setIndicadorIe(9);
            }

        } else {
            if (destinatario != null) {
                destinatario.setIndicadorIe(9);
            }

        }
    }


    public void definirEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    private void atualizarNumeroNfe(NotaFiscalTipo notaFiscalTipo, int numero) {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("id", notaFiscalTipo.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("ultimo_numero", numero);
        tiposNotaFiscal.updateNativo(NotaFiscalTipo.class, filtros, atributos);
    }


    private void definirEmitente(NfeCabecalho nfe) {
        NfeEmitente emitente = new NfeEmitente();

        EmpresaEndereco endereco = empresa.buscarEnderecoPrincipal();

        emitente.setCpfCnpj(empresa.getCnpj());
        emitente.setInscricaoEstadual(empresa.getInscricaoEstadual());
        emitente.setNome(empresa.getRazaoSocial());
        emitente.setFantasia(empresa.getNomeFantasia());
        emitente.setLogradouro(endereco.getLogradouro());
        emitente.setNumero(endereco.getNumero());
        emitente.setComplemento(endereco.getComplemento());
        emitente.setBairro(endereco.getBairro());
        emitente.setCodigoMunicipio(endereco.getMunicipioIbge());
        emitente.setNomeMunicipio(endereco.getCidade());
        emitente.setUf(endereco.getUf());
        emitente.setCep(endereco.getCep());
        emitente.setCrt(empresa.getCrt());
        emitente.setCodigoPais(1058);
        emitente.setNomePais("BRASIL");
        emitente.setTelefone(endereco.getFone());
        emitente.setInscricaoEstadualSt(empresa.getInscricaoEstadualSt());
        emitente.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
        emitente.setCnae(empresa.getCodigoCnaePrincipal());
        emitente.setNfeCabecalho(nfe);
        nfe.setEmitente(emitente);
    }

    private void definirFormaPagamento(NfeCabecalho nfe, PdvTipoPagamento tipoPagamento) {
        NfeFormaPagamento nfeFormaPagamento = new NfeFormaPagamento();
        nfeFormaPagamento.setPdvTipoPagamento(tipoPagamento);
        nfeFormaPagamento.setNfeCabecalho(nfe);
        nfeFormaPagamento.setForma(tipoPagamento.getCodigo());
        nfeFormaPagamento.setValor(nfe.getValorTotal());
        nfe.getListaNfeFormaPagamento().clear();
        nfe.getListaNfeFormaPagamento().add(nfeFormaPagamento);
    }


    private void iniciarConfEmissor(ModeloDocumento modelo) throws Exception {


        validarConfEmissor(configuracao);

        if (NfeTransmissao.getInstance().getConfiguracoes() == null) {
            ConfEmissorDTO conf = new ConfEmissorDTO(Integer.valueOf(configuracao.getWebserviceUf()), configuracao.getCaminhoSchemas(),
                    configuracao.getCertificadoDigitalCaminho(), configuracao.getCertificadoDigitalSenha(), configuracao.getWebserviceAmbiente(), "4.00");
            NfeTransmissao.getInstance().iniciarConfiguracoes(conf);
        }

    }

    private void validarConfEmissor(ConfiguracaoEmissorDTO configuracao) throws ChronosException {

        if (configuracao == null) {
            throw new ChronosException("Configuração não definida");
        } else if (configuracao.getWebserviceAmbiente() == null) {
            throw new ChronosException("Ambiente de transmissão não definido");
        } else if (configuracao.getWebserviceUf() == null) {
            throw new ChronosException("UF de transmissão nao definido");
        } else if (configuracao.getCertificadoDigitalCaminho() == null || configuracao.getCertificadoDigitalSenha() == null) {
            throw new ChronosException("Certificado não definido");
        } else if (configuracao.getCaminhoSchemas() == null) {
            throw new ChronosException("Schemas não definido");
        }

    }

    private String salvarXml(String xml, TipoArquivo tipoArquivo, String nomeArquivo) throws IOException {
        return ArquivoUtil.getInstance().escrever(tipoArquivo, empresa.getCnpj(), xml.getBytes(), nomeArquivo);
    }

    private String salvarXmlCancelado(NfeCabecalho nfe) throws Exception {
        String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());
        String caminhoXml = pastaXml + System.getProperty("file.separator") + nfe.getNomeXml();
        if (!new File(caminhoXml).exists()) {
            gerarXml(nfe.getId(), nfe.getNomeXml());
        }

        br.inf.portalfiscal.nfe.schema_4.enviNFe.TNfeProc nfeProc = XmlUtil.xmlToObject(XmlUtil.leXml(caminhoXml), TNfeProc.class);
        nfeProc.getProtNFe().getInfProt().setNProt(nfe.getNumeroProtocolo());
        nfeProc.getProtNFe().getInfProt().setCStat("101"); // 101
        nfeProc.getProtNFe().getInfProt().setDhRecbto(FormatValor.getInstance().formatarDataNota(nfe.getDataHoraProcessamento()));
        nfeProc.getProtNFe().getInfProt().setXMotivo("NF-e Cancelada"); // NF-e Cancelada

        String xml = XmlUtil.objectToXml(nfeProc);

        salvaNfeXml(xml, nfe);

        return salvarXml(xml, TipoArquivo.NFe, nfe.getNomeXml());
    }

    private String gerarXml(int idnfe, String nome) throws IOException {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "nfeCabecalho.id", Filtro.IGUAL, idnfe));
        String atributos[] = new String[]{"xml"};
        NfeXml nfeXml = nfeXmlRepository.get(NfeXml.class, filtros, atributos);
        if (nfeXml == null) {
            throw new RuntimeException("Xml inexistente!");
        }
        String xml = new String(nfeXml.getXml(), StandardCharsets.UTF_8);
        return salvarXml(xml, TipoArquivo.NFe, nome);
    }

    private NfeXml salvaNfeXml(String xml, NfeCabecalho nfe) {
        NfeXml nfeXml = new NfeXml();
        if (StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            nfeXml.setNfeCabecalho(nfe);
            nfeXml.setXml(xml.getBytes());
            nfeXmlRepository.atualizar(nfeXml);
        } else {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro(Filtro.AND, "nfeCabecalho.id", Filtro.IGUAL, nfe.getId()));
            String atributos[] = null;
            nfeXml = nfeXmlRepository.get(NfeXml.class, filtros, atributos);
            nfeXml.setNfeCabecalho(nfe);
            nfeXml.setXml(xml.getBytes());
            nfeXmlRepository.atualizar(nfeXml);
        }

        return nfeXml;
    }


}
