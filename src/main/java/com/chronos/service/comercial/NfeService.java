package com.chronos.service.comercial;

import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.bo.nfe.NfeUtil;
import com.chronos.dto.*;
import com.chronos.infra.enuns.FormatoImpressaoDanfe;
import com.chronos.infra.enuns.LocalDestino;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.nfe.Nfe;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.NfeRepository;
import com.chronos.repository.Repository;
import com.chronos.util.*;
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
    private EstoqueRepository estoqueRepositoy;

    @Inject
    private EstoqueRepository produtos;

    @Inject
    private NfeRepository nfeRepository;

    @Inject
    private ExternalContext context;

    private boolean salvarXml;

    private ConfiguracaoEmissorDTO configuracao;

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
        context = FacesContext.getCurrentInstance().getExternalContext();
        NfeTransmissao.getInstance().iniciarConfiguracoes();
    }

    public NfeCabecalho dadosPadroes(ModeloDocumento modelo) throws Exception {
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setFormatoImpressaoDanfe(modelo == ModeloDocumento.NFE ? FormatoImpressaoDanfe.DANFE_RETRATO.getCodigo() : FormatoImpressaoDanfe.DANFE_NFCE.getCodigo());
        nfe.setUfEmitente(empresa.getCodigoIbgeUf());
        nfe.setCodigoMunicipio(empresa.getCodigoIbgeCidade());
        nfe.setCodigoModelo(String.valueOf(modelo));
        nfe.setEmpresa(empresa);

        nfe.getEmitente().setNfeCabecalho(nfe);

        nfe.getDestinatario().setNfeCabecalho(nfe);

        nfe.getLocalEntrega().setNfeCabecalho(nfe);

        nfe.getLocalRetirada().setNfeCabecalho(nfe);

        nfe.getTransporte().setNfeCabecalho(nfe);

        nfe.getFatura().setNfeCabecalho(nfe);


        if (configuracao == null && configuracao.getModelo() != modelo.getCodigo()) {
            configuracao = getConfEmisor(modelo);
        }

        if (configuracao != null) {
            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            if (StringUtils.isEmpty(nfe.getInformacoesAddContribuinte())) {
                nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());
            }
            nfe.setCsc(configuracao.getCsc());

        }

        return nfe;
    }

    public ConfiguracaoEmissorDTO getConfEmisor(ModeloDocumento modelo) throws Exception {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("empresa.id", empresa.getId()));

        ConfiguracaoEmissorDTO configuracaoDTO;

        if (modelo == ModeloDocumento.NFE) {
            NfeConfiguracao configuracao = configuracoesNfe.get(NfeConfiguracao.class, filtros);
            if (configuracao == null) {
                throw new Exception("Configurações da NF-e  não definidas");
            }
            configuracaoDTO = new ConfiguracaoEmissorDTO(configuracao);
        } else {
            PdvConfiguracao configuracao = configuracoesNfce.get(PdvConfiguracao.class, filtros);
            if (configuracao == null) {
                throw new Exception("Configurações da NFC-e  não definidas");
            }
            configuracaoDTO = new ConfiguracaoEmissorDTO(configuracao);
        }

        return configuracaoDTO;
    }

    public void setarConfiguracoesNFe(NfeCabecalho nfe, ModeloDocumento modelo) throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));


        if (modelo == ModeloDocumento.NFE) {
            NfeConfiguracao configuracao = configuracoesNfe.get(NfeConfiguracao.class, filtros);

            if (configuracao == null) {
                throw new Exception("Configurações da NF-e  não definidas");
            }
            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setProcessoEmissao(configuracao.getProcessoEmissao());
            nfe.setVersaoProcessoEmissao(configuracao.getVersaoProcessoEmissao());

            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());


        } else {
            PdvConfiguracao configuracao = configuracoesNfce.get(PdvConfiguracao.class, filtros);
            if (configuracao == null) {
                throw new Exception("Configurações da NFC-e  não definidas");
            }

            nfe.setAmbiente(configuracao.getWebserviceAmbiente());
            nfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());


        }

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
                String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());
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

    public String inutilizarNFe(ModeloDocumento modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
        NotaFiscalTipo notaFiscalTipo = getNotaFicalTipo(modelo);
        if (notaFiscalTipo == null) {
            throw new Exception("Não foi informando numeração para o modelo " + modelo);
        }
        iniciarConfEmissor(modelo);
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
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        if (nfe.getNumero() == null || nfe.getSerie() == null) {
            notaFiscalTipo = getNotaFicalTipo(modelo);
            numero = notaFiscalTipo.getUltimoNumero();
            serie = notaFiscalTipo.getSerie();

        } else {
            numero = Integer.valueOf(nfe.getNumero());
            serie = nfe.getSerie();
        }

        nfe.setNumero(FormatValor.getInstance().formatarNumeroDocFiscalToString(numero));
        nfe.setCodigoNumerico(FormatValor.getInstance().formatarCodigoNumeroDocFiscalToString(numero));
        nfe.setSerie(serie);
        nfe.setChaveAcesso("" + nfe.getEmpresa().getCodigoIbgeUf()
                + FormatValor.getInstance().formatarAno(nfe.getDataHoraEmissao())
                + FormatValor.getInstance().formatarMes(nfe.getDataHoraEmissao())
                + nfe.getEmpresa().getCnpj()
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
        item = nfeUtil.defineTributacao(item, empresa, operacaoFiscal, destinatario);
        return item;
    }

    public NfeCabecalho atualizarTotais(NfeCabecalho nfe) throws Exception {
        NfeUtil nFeUtil = new NfeUtil();
        return nFeUtil.calcularTotalNFe(nfe);
    }

    public List<Produto> getListaProduto(String descricao) throws Exception {
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

    public List<ProdutoDTO> getListaProdutoDTO(String descricao,boolean moduloVenda) throws Exception {
        List<ProdutoDTO> listaProduto;
        List<Filtro> filtros = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNumeric(descricao)) {
            filtros.add(new Filtro(Filtro.AND, "id", Filtro.IGUAL, descricao));
            // listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        } else {
            filtros.add(new Filtro(Filtro.OR, "nome", Filtro.LIKE, descricao.trim()));
            filtros.add(new Filtro(Filtro.OR, "gtin", Filtro.LIKE, descricao.trim()));
            filtros.add(new Filtro(Filtro.OR, "codigoInterno", Filtro.LIKE, descricao.trim()));
            //  listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        }
        if(moduloVenda){
            filtros.add(new Filtro(Filtro.AND, "servico","N" ));
            filtros.add(new Filtro(Filtro.AND, "tipo","V" ));
        }
        listaProduto = produtos.getProdutoDTO(descricao, empresa);
        return listaProduto;
    }

    public void gerarDuplicatas(NfeCabecalho nfe, VendaCondicoesPagamento condicoesPagamento, Date primeiroVencimento, int intervaloParcelas, int qtdParcelas) throws Exception {

        if (nfe.getValorTotal() == null || nfe.getValorTotal().signum() == 0) {
            throw new Exception("O valor total da NFe deve ser maior que 0");
        }
        if (condicoesPagamento == null && (primeiroVencimento == null || intervaloParcelas == 0 || qtdParcelas == 0)) {
            throw new Exception("Se a condição de pagament não for informada é preciso informa o primeiro vencimento,intervalo de parcelas e a quantidade.");
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
                valorParcela = Biblioteca.calcTaxa(nfe.getValorTotal(), parcelas.getTaxa());
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
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        iniciarConfEmissor(modelo);

        TEnviNFe nfeEnv = gerarNfeEnv(nfe);

        String xml = XmlUtil.objectToXml(nfeEnv);
        return salvarXml(xml, TipoArquivo.NFePreProcessada, nfe.getChaveAcesso() + nfe.getDigitoChaveAcesso() + ".xml");
    }

    private String salvarXml(String xml, TipoArquivo tipoArquivo, String nomeArquivo) throws IOException {
        return ArquivoUtil.getInstance().escrever(tipoArquivo, empresa.getCnpj(), xml.getBytes(), nomeArquivo);
    }

    public TEnviNFe gerarNfeEnv(NfeCabecalho nfe) throws Exception {
        TEnviNFe nfeEnv = NfeTransmissao.getInstance().geraNFeEnv(nfe);
        return nfeEnv;
    }

    public void visualizarXml(String caminho) throws Exception {
        File file = new File(caminho);
        if (!file.exists()) {
            throw new Exception("Arquivo não encontrado");
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
            throw new Exception("Esta NF-e já foi autorizada. Operação não permitida ");
        } else if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new Exception("Esta NF-e já foi autorizada. Operação não permitida ");
        }
    }

    public String consultarStatusNfe(ModeloDocumento modelo) throws Exception {
        iniciarConfEmissor(modelo);
        return NfeTransmissao.getInstance().statusServico();
    }

    private NfeXml salvaNfeXml(String xml, NfeCabecalho nfe) throws Exception {
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

    @Transactional
    public StatusTransmissao transmitirNFe(NfeCabecalho nfe, boolean atualizarEstoque) throws Exception {
        validacaoNfe(nfe);
        VendaCabecalho venda = nfe.getVendaCabecalho();
        OsAbertura os = nfe.getOs();
        PdvVendaCabecalho pdv = nfe.getPdv();
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        StatusTransmissao status = StatusTransmissao.ENVIADA;

        String tipo = modelo == ModeloDocumento.NFE ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        NotaFiscalTipo notaFiscalTipo = gerarNumeracao(nfe);
        TEnviNFe nfeEnv = gerarNfeEnv(nfe);
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
                if(pdv!=null){
                    pdv.setIdnfe(nfe.getId());
                    nfe.setPdv(pdv);
                }
                status = StatusTransmissao.AUTORIZADA;

            } else if (retorno.getProtNFe().getInfProt().getCStat().equals("204")
                    || retorno.getProtNFe().getInfProt().getCStat().equals("539")) {
                status = StatusTransmissao.DUPLICIDADE;
                Mensagem.addErrorMessage(retorno.getProtNFe().getInfProt().getXMotivo());
            } else {
                salvarXml = true;
                Mensagem.addErrorMessage(retorno.getProtNFe().getInfProt().getXMotivo());
            }
        } else if (retorno.getCStat().equals("215") || retorno.getCStat().equals("225")) {
            status = StatusTransmissao.SCHEMA_INVALIDO;
            if (org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas())) {
                Mensagem.addErrorMessage("Preenchimento do xml invalido para mais detalhes informes o camminho dos schemas para validação");
            } else {
                String xml = XmlUtil.objectToXml(nfeEnv);
                String erroValidacao = ValidarNFe.validaXml(xml, ValidarNFe.ENVIO);
                Mensagem.addErrorMessage(erroValidacao);
            }

        }

        if (salvarXml) {
            String xml = XmlUtil.objectToXml(nfeEnv);
            salvarXml(xml, TipoArquivo.NFePreProcessada, nfe.getChaveAcessoCompleta() + ".xml");

        }

        return status;
    }

    public void danfe(NfeCabecalho nfe) throws Exception {
        String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());
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


        StatusTransmissao statusTransmissao = StatusTransmissao.valueOfCodigo(nfe.getStatusNota());
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        String caminho = statusTransmissao == StatusTransmissao.AUTORIZADA
                ? ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj()) : ArquivoUtil.getInstance().getPastaXmlNfePreProcessada(empresa.getCnpj());

        caminho += System.getProperty("file.separator") + (statusTransmissao == StatusTransmissao.AUTORIZADA ? nfe.getNomeXml() : nfe.getChaveAcessoCompleta() + ".xml");
        iniciarConfEmissor(modelo);
        HashMap parametrosRelatorio = new HashMap<>();
        String camLogo = ArquivoUtil.getInstance().getImagemTransmissao(empresa.getCnpj());
        camLogo = new File(camLogo).exists() ? camLogo : context.getRealPath("resources/images/logo_nfe_peq.png");
        Image logo = new ImageIcon(camLogo).getImage();
        parametrosRelatorio.put("Logo", logo);
        parametrosRelatorio.put("danfe_logo", logo);
        String expressaoDataSource = "";
        String nomeRelatorioJasper = "";
        if (statusTransmissao == StatusTransmissao.AUTORIZADA) {
            expressaoDataSource = "/nfeProc/NFe/infNFe/det";
            if (!new File(caminho).exists()) {
                List<Filtro> filtros = new LinkedList<>();
                filtros.add(new Filtro(Filtro.AND, "nfeCabecalho.id", Filtro.IGUAL, nfe.getId()));
                String atributos[] = new String[]{"xml"};
                NfeXml nfeXml = nfeXmlRepository.get(NfeXml.class, filtros, atributos);
                if (nfeXml == null) {
                    throw new RuntimeException("Xml inexistente!");
                }
                String xml = new String(nfeXml.getXml(), "UTF-8");
                salvarXml(xml, TipoArquivo.NFe, nfe.getNomeXml());
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
                BufferedImage image = MatrixToImageWriter
                        .toBufferedImage(new QRCodeWriter().encode(nfe.getQrcode(), BarcodeFormat.QR_CODE, 300, 300));
                parametrosRelatorio.put("QR_CODE", image);
                parametrosRelatorio.put("ENDERECO_SEFAZ", url);
                parametrosRelatorio.put("operador", FacesUtil.getUsuarioSessao().getLogin());

                JRXmlDataSource faturaDataSource = new JRXmlDataSource(caminho, "//pag");
                parametrosRelatorio.put("Fatura_Datasource", faturaDataSource);
            }


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

    public String cartaCorrecao(NfeCabecalho nfe, String justificativa) throws Exception {
        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new Exception("NF-e náo autorizada. Cancelamento náo permitido!");
        }

        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));

        iniciarConfEmissor(modelo);
        EventoDTO evento = new EventoDTO();
        evento.setProtocolo(nfe.getNumeroProtocolo());
        evento.setMotivo(nfe.getJustificativaCancelamento());
        evento.setChave(nfe.getChaveAcessoCompleta());
        evento.setCnpj(empresa.getCnpj());


        String enviarCartaCorrecao = NfeTransmissao.getInstance().enviarCartaCorrecao(evento);

        Mensagem.addInfoMessage(enviarCartaCorrecao);

        return enviarCartaCorrecao;

    }

    @Transactional
    public boolean cancelarNFe(NfeCabecalho nfe, boolean estoque) throws Exception {

        boolean cancelado = false;
        if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new Exception("NF-e já cancelada. Cancelamento náo permitido!");
        }
        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new Exception("NF-e náo autorizada. Cancelamento náo permitido!");
        }


        EventoDTO evento = new EventoDTO();
        evento.setProtocolo(nfe.getNumeroProtocolo());
        evento.setMotivo(nfe.getJustificativaCancelamento());
        evento.setChave(nfe.getChaveAcessoCompleta());
        evento.setCnpj(empresa.getCnpj());
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
                String xml = XmlUtil.objectToXml(retorno.getProcEvento());
                nfe.setStatusNota(StatusTransmissao.CANCELADA.getCodigo());
                nfe = nfeRepository.procedimentoNfeCancelada(nfe, estoque);
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
            TEnviNFe nfeEnv = gerarNfeEnv(nfe);
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

        iniciarConfEmissor(modelo);
        TRetConsSitNFe result = NfeTransmissao.getInstance().consultarNfe(chave);

        return result;
    }

    public void validacaoNfe(NfeCabecalho nfe) throws Exception {
        if (StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new Exception("Esta NF-e já foi autorizada. Operação não permitida ");
        } else if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new Exception("Esta NF-e já foi autorizada. Operação não permitida ");
        }
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        if (modelo == ModeloDocumento.NFCE && StringUtils.isEmpty(nfe.getCsc())) {
            throw new Exception("É Necessário informar o CSC!");
        }
        LocalDestino destino = LocalDestino.getByCodigo(nfe.getLocalDestino());
        int cfop = 0;
        int cfopAux = 0;

        if (nfe.getListaNfeDetalhe() == null || nfe.getListaNfeDetalhe().isEmpty()) {
            throw new Exception("Não foram definido itens para emissão da NFe");
        }
        for (NfeDetalhe item : nfe.getListaNfeDetalhe()) {
            cfopAux = item.getCfop();
            if (cfop == 0) {
                cfop = item.getCfop();
            }
        }
        if (destino == LocalDestino.INTERESTADUAL && cfop < 6000) {
            throw new Exception("CFOP :" + cfop + " inválido para operações Interestadual");
        }

        LocalDestino local = LocalDestino.getByCodigo(nfe.getLocalDestino());
        if (local == LocalDestino.INTERESTADUAL && modelo == ModeloDocumento.NFCE) {
            throw new Exception("Emissão de NFCe somente para operações locais");
        }

        BigDecimal valorDucplicata = BigDecimal.ZERO;

        if (nfe.getListaDuplicata() != null && !nfe.getListaDuplicata().isEmpty()) {
            valorDucplicata = nfe.getListaDuplicata().stream()
                    .map(NfeDuplicata::getValor)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
        }


        if (valorDucplicata.signum() == 1 && valorDucplicata.compareTo(nfe.getValorTotal()) != 0) {
            throw new Exception("Soma dos valores da duplicata Invalida");
        }

    }

    public NotaFiscalTipo getNotaFicalTipo(ModeloDocumento modelo) throws Exception {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo.getCodigo().toString()));
        Object[] atributos = new String[]{"serie", "ultimoNumero", "notaFiscalModelo.id"};
        NotaFiscalTipo notaFiscalTipo = tiposNotaFiscal.get(NotaFiscalTipo.class, filtros, atributos);
        if (notaFiscalTipo == null) {
            throw new Exception("Configuração de numero fiscal para o modelo :" + modelo + " não definida");
        }

        notaFiscalTipo.setUltimoNumero(notaFiscalTipo.proximoNumero());

        return notaFiscalTipo;
    }

    private void atualizarNumeroNfe(NotaFiscalTipo notaFiscalTipo, int numero) {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("id", notaFiscalTipo.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("ultimo_numero", numero);
        tiposNotaFiscal.updateNativo(NotaFiscalTipo.class, filtros, atributos);
    }

    private String xmlCancelado(TRetEnvEvento retorno, TEnvEvento evento) throws Exception {
        TProcEvento procEvento = new TProcEvento();
        procEvento.setVersao("1.00");
        procEvento.setEvento(evento.getEvento().get(0));
        procEvento.setRetEvento(retorno.getRetEvento().get(0));
        String xml = XmlUtil.objectToXml(procEvento);
        return xml;
    }


    private void iniciarConfEmissor(ModeloDocumento modelo) throws Exception {

        if (configuracao == null) {
            configuracao = getConfEmisor(modelo);
        }
        validarConfEmissor(configuracao);

        if (NfeTransmissao.getInstance().getConfiguracoes() == null) {
            ConfEmissorDTO conf = new ConfEmissorDTO(Integer.valueOf(configuracao.getWebserviceUf()), configuracao.getCaminhoSchemas(),
                    configuracao.getCertificadoDigitalCaminho(), configuracao.getCertificadoDigitalSenha(), configuracao.getWebserviceAmbiente(), "4.00");
            NfeTransmissao.getInstance().iniciarConfigurações(conf);
        }

    }

    private void validarConfEmissor(ConfiguracaoEmissorDTO configuracao) throws Exception {

        if (configuracao == null) {
            throw new Exception("Configuração não definida");
        } else if (configuracao.getWebserviceAmbiente() == null) {
            throw new Exception("Ambiente de transmissão não definido");
        } else if (configuracao.getWebserviceUf() == null) {
            throw new Exception("UF de transmissão nao definido");
        } else if (configuracao.getCertificadoDigitalCaminho() == null || configuracao.getCertificadoDigitalSenha() == null) {
            throw new Exception("Certificado não definido");
        } else if (configuracao.getCaminhoSchemas() == null) {
            throw new Exception("Schemas não definido");
        }

    }


}
