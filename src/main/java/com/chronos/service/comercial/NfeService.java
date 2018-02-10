package com.chronos.service.comercial;

import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TProcEvento;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema.envinfe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema.envinfe.TRetEnviNFe;
import com.chronos.bo.nfe.NfeTransmissao;
import com.chronos.bo.nfe.NfeUtil;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.infra.enuns.LocalDestino;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.modelo.entidades.enuns.TipoArquivo;
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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();
        context = FacesContext.getCurrentInstance().getExternalContext();
    }

    public void dadosPadroes(NfeCabecalho nfe, ModeloDocumento modelo, Empresa empresa, ConfiguracaoEmissorDTO configuacao) throws Exception {
        NfeUtil nfeUtil = new NfeUtil();

        nfe = nfeUtil.dadosPadroes(nfe, modelo, empresa, configuacao);
        //   setarConfiguracoesNFe(nfe, modelo);
    }

    public ConfiguracaoEmissorDTO getConfEmisor(Empresa empresa, ModeloDocumento modelo) throws Exception {
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

    public void baixaXml(List<NfeCabecalho> nfes, Empresa empresa) throws Exception {
        if (nfes.size() > 0) {
            final int BUFFER = 2048;
            BufferedInputStream origin = null;
            File arquivoZip = File.createTempFile(empresa.getCnpj(), ".zip");
            arquivoZip.deleteOnExit();
            FileOutputStream dest = new FileOutputStream(arquivoZip);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];

            for (NfeCabecalho n : nfes) {
                String pastaXml = ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj());
                File arquivoXml = new File(pastaXml + System.getProperty("file.separator") + n.getNomeXml());
                if (!arquivoXml.exists()) {
                    List<Filtro> filtros = new LinkedList<>();
                    filtros.add(new Filtro(Filtro.AND, "nfeCabecalho.id", Filtro.IGUAL, n.getId()));
                    String atributos[] = new String[]{"xml"};
                    NfeXml nfeXml = nfeXmlRepository.get(NfeXml.class, filtros, atributos);
                    if (nfeXml != null) {
                        arquivoXml = new File(ArquivoUtil.getInstance().escrever(TipoArquivo.NFe, empresa.getCnpj(), nfeXml.getXml(), n.getNomeXml()));
                    }

                }
                if (arquivoXml.exists()) {
                    FileInputStream fi = new FileInputStream(arquivoXml);

                    origin = new BufferedInputStream(fi, 20);
                    ZipEntry entry = new ZipEntry(arquivoXml.getName());
                    out.putNextEntry(entry);

                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }

                    origin.close();
                }

                // adiciona ao arquivo compactado
            }
            out.close();
            FacesUtil.downloadArquivo(arquivoZip, empresa.getCnpj() + ".zip");
        }
    }

    public String inutilizarNFe(ConfiguracaoEmissorDTO configuracao, String modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
        NotaFiscalTipo notaFiscalTipo = getNotaFicalTipo(modelo, empresa);
        if (notaFiscalTipo == null) {
            throw new Exception("Não foi informando numeração para o modelo " + modelo);
        }
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        configuracao.setCaminhoSchemas(schemas);
        br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut infRetorno = transmissao.inutilizarNFe(configuracao, modelo, serie, numInicial, numFinal, justificativa);
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
        if (nfe.getNumero() == null || nfe.getSerie() == null) {
            notaFiscalTipo = getNotaFicalTipo(nfe.getCodigoModelo(), empresa);
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
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, descricao.trim()));
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
        BigDecimal residuo = BigDecimal.ZERO;
        BigDecimal somaParcelas = BigDecimal.ZERO;
        BigDecimal valorParcela = BigDecimal.ZERO;
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

    public String gerarNfePreProcessada(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao) throws Exception {
        TEnviNFe nfeEnv = gerarNfeEnv(nfe, configuracao);
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        configuracao.setCaminhoSchemas(schemas);
        String xml = XmlUtil.objectToXml(nfeEnv);
        return salvarXml(xml, TipoArquivo.NFePreProcessada, nfe.getChaveAcesso() + nfe.getDigitoChaveAcesso() + ".xml");
    }

    private String salvarXml(String xml, TipoArquivo tipoArquivo, String nomeArquivo) throws IOException {
        return ArquivoUtil.getInstance().escrever(tipoArquivo, empresa.getCnpj(), xml.getBytes(), nomeArquivo);
    }

    public TEnviNFe gerarNfeEnv(NfeCabecalho nfe, ConfiguracaoEmissorDTO confiEmissor) throws Exception {
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        String schemas = org.springframework.util.StringUtils.isEmpty(confiEmissor.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : confiEmissor.getCaminhoSchemas();
        confiEmissor.setCaminhoSchemas(schemas);
        TEnviNFe nfeEnv = transmissao.geraNFeEnv(nfe, confiEmissor);

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
    public StatusTransmissao transmitirNFe(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao, boolean atualizarEstoque) throws Exception {
        validacaoNfe(nfe);
        VendaCabecalho venda = nfe.getVendaCabecalho();
        OsAbertura os = nfe.getOs();
        PdvVendaCabecalho pdv = nfe.getPdv();
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        StatusTransmissao status = StatusTransmissao.ENVIADA;
        String schemas = context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE);
        configuracao.setCaminhoSchemas(schemas);
        String tipo = modelo == ModeloDocumento.NFE ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        NotaFiscalTipo notaFiscalTipo = gerarNumeracao(nfe);
        TEnviNFe nfeEnv = gerarNfeEnv(nfe, configuracao);
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

    public void danfe(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao) throws Exception {
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
            gerarDanfe(nfe, configuracao);
            FacesUtil.downloadArquivo(filePdf, filePdf.getName());
        }
    }

    public void gerarDanfe(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao) throws Exception {
        StatusTransmissao statusTransmissao = StatusTransmissao.valueOfCodigo(nfe.getStatusNota());
        ModeloDocumento modelo = ModeloDocumento.getByCodigo(Integer.valueOf(nfe.getCodigoModelo()));
        String caminho = statusTransmissao == StatusTransmissao.AUTORIZADA
                ? ArquivoUtil.getInstance().getPastaXmlNfeProcessada(empresa.getCnpj()) : ArquivoUtil.getInstance().getPastaXmlNfePreProcessada(empresa.getCnpj());

        caminho += System.getProperty("file.separator") + (statusTransmissao == StatusTransmissao.AUTORIZADA ? nfe.getNomeXml() : nfe.getChaveAcessoCompleta() + ".xml");
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        configuracao.setCaminhoSchemas(schemas);
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        transmissao.instanciarConfiguracoes(configuracao);
        HashMap parametrosRelatorio = new HashMap<>();
        String camLogo = ArquivoUtil.getInstance().getImagemTransmissao(empresa.getCnpj());
        camLogo = new File(camLogo).exists() ? camLogo : context.getRealPath("resources/images/logo_nfe_peq.png");
        Image logo = new ImageIcon(camLogo).getImage();
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

    public String cartaCorrecao(NfeCabecalho nfe, String justificativa, ConfiguracaoEmissorDTO configuracao) throws Exception {
        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new Exception("NF-e náo autorizada. Cancelamento náo permitido!");
        }
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        configuracao.setCaminhoSchemas(schemas);
        br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento retorno = transmissao.enviarCartaCorrecao(configuracao, nfe.getChaveAcessoCompleta(), justificativa);
        if (retorno.getCStat().equals("128")) {
            if (retorno.getRetEvento().get(0).getInfEvento().getCStat().equals("135")) {
                Mensagem.addInfoMessage("Carta de correção enviada  com sucesso");
            } else {
                Mensagem.addInfoMessage(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            }
        } else {
            Mensagem.addInfoMessage(retorno.getXMotivo());
        }

        return retorno.getXMotivo();

    }

    @Transactional
    public boolean cancelarNFe(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao, boolean estoque) throws Exception {

        boolean cancelado = false;
        if (StatusTransmissao.isCancelada(nfe.getStatusNota())) {
            throw new Exception("NF-e já cancelada. Cancelamento náo permitido!");
        }
        if (!StatusTransmissao.isAutorizado(nfe.getStatusNota())) {
            throw new Exception("NF-e náo autorizada. Cancelamento náo permitido!");
        }
        String tipo = nfe.getCodigoModelo().equals("55") ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        String schemas = org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas()) ? context.getRealPath(Constantes.DIRETORIO_SCHEMA_NFE) : configuracao.getCaminhoSchemas();
        configuracao.setCaminhoSchemas(schemas);
        NfeTransmissao transmissao = new NfeTransmissao(empresa);
        TEnvEvento evento = transmissao.cancelarNFe(configuracao, nfe.getNumeroProtocolo(), nfe.getUfEmitente().toString(), String.valueOf(nfe.getAmbiente()), nfe.getChaveAcessoCompleta(), nfe.getJustificativaCancelamento());
        TRetEnvEvento retorno = Nfe.cancelarNfe(evento, false, tipo);
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro("id", nfe.getId()));
        Map<String, Object> atributos = new HashMap<>();
        atributos.put("status_nota", StatusTransmissao.CANCELADA.getCodigo());
        if (retorno.getCStat().equals("128")) {
            if (retorno.getRetEvento().get(0).getInfEvento().getCStat().equals("135")) {
                nfe.setNumeroProtocolo(retorno.getRetEvento().get(0).getInfEvento().getNProt());
                nfe.setVersaoAplicativo(retorno.getRetEvento().get(0).getInfEvento().getVerAplic());
                nfe.setDataHoraProcessamento(FormatValor.getInstance().formatarDataNota(retorno.getRetEvento().get(0).getInfEvento().getDhRegEvento()));
                String xml = xmlCancelado(retorno, evento);
                nfe.setStatusNota(StatusTransmissao.CANCELADA.getCodigo());
                nfe = nfeRepository.procedimentoNfeCancelada(nfe, estoque);
                cancelado = true;
            } else if (retorno.getRetEvento().get(0).getInfEvento().getCStat().equals("573")) {
                cancelado = repository.updateNativo(NfeCabecalho.class, filtros, atributos);
                Mensagem.addInfoMessage(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            } else {
                Mensagem.addInfoMessage(retorno.getRetEvento().get(0).getInfEvento().getXMotivo());
            }
        } else {
            Mensagem.addInfoMessage(retorno.getXMotivo());
        }
        return cancelado;
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

    public NotaFiscalTipo getNotaFicalTipo(String modelo, Empresa empresa) throws Exception {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        filtros.add(new Filtro(Filtro.AND, "notaFiscalModelo.codigo", Filtro.IGUAL, modelo));
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


}
