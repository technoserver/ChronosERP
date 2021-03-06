package com.chronos.erp.service.comercial;

import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEnviMDFe;
import br.inf.portalfiscal.mdfe.schema_300.evEncMDFe.TRetEvento;
import br.inf.portalfiscal.mdfe.schema_300.retConsReciMDFe.TRetConsReciMDFe;
import com.chronos.erp.bo.mdfe.MdfeTransmissao;
import com.chronos.erp.dto.ConfiguracaoMdfeDTO;
import com.chronos.erp.dto.DocFiscalDto;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.modelo.enuns.TipoArquivo;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.configuracao.NotaFiscalService;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.FormatValor;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.util.ConstantesMDFe;
import com.chronos.transmissor.util.ValidarMDFe;
import com.chronos.transmissor.util.XmlUtil;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 16/06/18.
 */
public class MdfeService implements Serializable {


    private static final long serialVersionUID = 1L;

    private Empresa empresa;

    @Inject
    private ExternalContext context;

    @Inject
    private Repository<MdfeCabecalho> repository;
    @Inject
    private Repository<MdfeConfiguracao> configuracaoRepository;
    @Inject
    private Repository<MdfeXml> xmlRepository;

    private ConfiguracaoMdfeDTO configuracao;

    @Inject
    private NotaFiscalService notaService;


    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();

    }

    public void definirDadosPadrao(MdfeCabecalho mdfe) throws Exception {
        EmpresaEndereco endereco = empresa.buscarEnderecoPrincipal();
        mdfe.setUf(empresa.getCodigoIbgeUf());
        mdfe.setEmpresa(empresa);
        mdfe.setMdfeRodoviario(new MdfeRodoviario());
        mdfe.getMdfeRodoviario().setMdfeCabecalho(mdfe);

        configuracao = getConfiguracao();

        if (configuracao != null) {
            mdfe.setInformacoesAddContribuinte(configuracao.getObservacaoPadrao());
            mdfe.getMdfeRodoviario().setRntrc(configuracao.getRntrc());
            mdfe.setTipoAmbiente(configuracao.getWebserviceAmbiente());
        }
        if (endereco != null) {
            mdfe.setUfInicio(endereco.getUf());
            MdfeMunicipioCarregamento carregamento = new MdfeMunicipioCarregamento();
            carregamento.setCodigoMunicipio(endereco.getMunicipioIbge().toString());
            carregamento.setNomeMunicipio(endereco.getCidade());
            carregamento.setMdfeCabecalho(mdfe);
            mdfe.setListaMdfeMunicipioCarregamento(new HashSet<>());
            mdfe.getListaMdfeMunicipioCarregamento().add(carregamento);
        }
    }


    public void definirMunicipio(MdfeCabecalho mdfe, Municipio mun, String id) {

        if (id.equals("municipioInicio")) {
            MdfeMunicipioCarregamento carregamento = new MdfeMunicipioCarregamento();
            carregamento.setCodigoMunicipio(mun.getCodigoIbge().toString());
            carregamento.setNomeMunicipio(mun.getNome());
            carregamento.setMdfeCabecalho(mdfe);
            mdfe.setListaMdfeMunicipioCarregamento(new HashSet<>());
            mdfe.getListaMdfeMunicipioCarregamento().add(carregamento);
        } else {
            MdfeMunicipioDescarregamento descarregamento = new MdfeMunicipioDescarregamento();
            descarregamento.setCodigoMunicipio(mun.getCodigoIbge().toString());
            descarregamento.setNomeMunicipio(mun.getNome());
            descarregamento.setMdfeCabecalho(mdfe);
            mdfe.setListaMdfeMunicipioDescarregamento(new HashSet<>());
            mdfe.getListaMdfeMunicipioDescarregamento().add(descarregamento);

            mdfe.getListaMdfeMunicipioDescarregamento().iterator().next()
                    .setListaMdfeInformacaoNfe(new HashSet<>());
            mdfe.getListaMdfeMunicipioDescarregamento().iterator().next()
                    .setListaMdfeInformacaoCte(new HashSet<>());
        }


    }

    public void addDocFiscal(MdfeCabecalho mdfe, DocFiscalDto docFiscal, List<MdfeInformacaoNfe> listaMdfeInformacaoNfe, List<MdfeInformacaoCte> listaMdfeInformacaoCte) {

        if (docFiscal.getModelo() == 57) {

            MdfeInformacaoCte mdfeInformacaoCte = new MdfeInformacaoCte(docFiscal.getChave(), docFiscal.getIndicadorReentrega());

            if (!listaMdfeInformacaoCte.contains(mdfeInformacaoCte)) {
                listaMdfeInformacaoCte.add(mdfeInformacaoCte);
                Mensagem.addInfoMessage("Doc Salvo com sucesso!");

            } else {
                Mensagem.addInfoMessage("Registro já foi adicionado!");
            }

            mdfe.setQuantidadeTotalCte(listaMdfeInformacaoCte.size());
            mdfe.setQuantidadeTotalNfe(0);
        } else {
            MdfeInformacaoNfe mdfeInformacaoNfe = new MdfeInformacaoNfe(docFiscal.getChave(), docFiscal.getIndicadorReentrega());

            if (!listaMdfeInformacaoNfe.contains(mdfeInformacaoNfe)) {
                listaMdfeInformacaoNfe.add(mdfeInformacaoNfe);
                Mensagem.addInfoMessage("Doc Salvo com sucesso!");

            } else {
                Mensagem.addInfoMessage("Registro já foi adicionado!");
            }

            mdfe.setQuantidadeTotalNfe(listaMdfeInformacaoNfe.size());
            mdfe.setQuantidadeTotalCte(0);
        }

    }

    public MdfeRodoviarioVeiculo definirMdfeVeiculo(Veiculo veiculo) {
        MdfeRodoviarioVeiculo mdfeRodoviarioVeiculo = new MdfeRodoviarioVeiculo();
        mdfeRodoviarioVeiculo.setCapacidadeKg(veiculo.getCapacidadeKg());
        mdfeRodoviarioVeiculo.setCapacidadeM3(veiculo.getCapacidadeM3());
        mdfeRodoviarioVeiculo.setCodigoInterno(veiculo.getCodigoInterno());
        mdfeRodoviarioVeiculo.setPlaca(veiculo.getPlaca());
        mdfeRodoviarioVeiculo.setRenavam(veiculo.getRenavam());
        mdfeRodoviarioVeiculo.setTara(veiculo.getTara());
        mdfeRodoviarioVeiculo.setTipoCarroceria(veiculo.getTipoCarroceria());

        mdfeRodoviarioVeiculo.setTipoRodado(veiculo.getTipoRodado());

        mdfeRodoviarioVeiculo.setUfLicenciamento(veiculo.getUf());

        if (veiculo.getTransportadora() != null) {
            Transportadora transportadora = veiculo.getTransportadora();
            MdfeRodoviarioProprietarioVeiculo propVeic = new MdfeRodoviarioProprietarioVeiculo();
            propVeic.setMdfeRodoviarioVeiculo(mdfeRodoviarioVeiculo);
            propVeic.setCpfCnpj(transportadora.getPessoa().getIdentificador());

            propVeic.setNome(transportadora.getPessoa().getNome());
            propVeic.setRntrc(transportadora.getRntrc());
            propVeic.setTipo(transportadora.getTipo());
            propVeic.setUf(transportadora.getPessoa().buscarEnderecoPrincipal().getUf());

            mdfeRodoviarioVeiculo.setMdfeRodoviarioProprietarioVeiculo(propVeic);
        }

        return mdfeRodoviarioVeiculo;
    }

    public MdfeCabecalho salvar(MdfeCabecalho mdfe, List<MdfeInformacaoCte> listaMdfeInformacaoCte, List<MdfeInformacaoNfe> listaMdfeInformacaoNfe) throws Exception {

        MdfeMunicipioDescarregamento descarregamento = mdfe.getListaMdfeMunicipioDescarregamento().stream().findFirst().get();

        if (mdfe.getTipoEmitente() == 1 && listaMdfeInformacaoCte.isEmpty()) {
            throw new Exception("Informe os Conhecimentos de transportes!");
        }

        if (mdfe.getTipoEmitente() == 2 && listaMdfeInformacaoNfe.isEmpty()) {
            throw new Exception("Informe as Notas Fiscais Eletrônicas!");

        }

        if (mdfe.getTipoEmitente() == 1) {
            for (MdfeInformacaoCte info : listaMdfeInformacaoCte) {
                info.setMdfeMunicipioDescarregamento(descarregamento);
            }
            descarregamento.setListaMdfeInformacaoCte(new HashSet<>(listaMdfeInformacaoCte));
            descarregamento.setListaMdfeInformacaoNfe(new HashSet<>());
        } else {

            for (MdfeInformacaoNfe info : listaMdfeInformacaoNfe) {
                info.setMdfeMunicipioDescarregamento(descarregamento);
            }
            descarregamento.setListaMdfeInformacaoNfe(new HashSet<>(listaMdfeInformacaoNfe));
            descarregamento.setListaMdfeInformacaoCte(new HashSet<>());
        }

        validar(mdfe);
        if (mdfe.getMdfeEmitente() == null) {
            definirEmitente(mdfe);
        }
        mdfe = repository.atualizar(mdfe);
        return mdfe;
    }

    public TRetEvento encerrar(String chave, String cnpj, String protocolo, String ibgeUF, String ibgeMunicipio) throws Exception {
        MdfeTransmissao transmissao = new MdfeTransmissao(empresa, getConfiguracao());
        TRetEvento retEvento = transmissao.encerrar(chave, cnpj, protocolo, ibgeUF, ibgeMunicipio);
        return retEvento;
    }

    @Transactional
    public StatusTransmissao enviarMdfe(MdfeCabecalho mdfe) throws Exception {
        StatusTransmissao status = StatusTransmissao.ENVIADA;

        verificarStatusNota(mdfe);

        MdfeTransmissao transmissao = new MdfeTransmissao(empresa, getConfiguracao());
        definirNumero(mdfe);
        TEnviMDFe enviMDFe = transmissao.gerarMdfeEnv(mdfe);

        TRetConsReciMDFe retornoMdfe = transmissao.transmitirMdfe(enviMDFe);

        if (retornoMdfe.getCStat().equals("104")) {

            switch (retornoMdfe.getProtMDFe().getInfProt().getCStat()) {
                case "100":
                    mdfe.setNumeroProtocolo(retornoMdfe.getProtMDFe().getInfProt().getNProt());
                    String xmlProc = XmlUtil.criaMdfeProc(enviMDFe, retornoMdfe.getProtMDFe());
                    status = StatusTransmissao.AUTORIZADA;
                    mdfe.setStatusMdfe(status.getCodigo());
                    mdfe.setDataHoraEmissao(FormatValor.getInstance().formatarDataNota(enviMDFe.getMDFe().getInfMDFe().getIde().getDhEmi()));
                    mdfe.setDataHoraProcessamento(FormatValor.getInstance().formatarDataNota(retornoMdfe.getProtMDFe().getInfProt().getDhRecbto()));
                    mdfe.setCodigoStatusTransmissao(Integer.valueOf(retornoMdfe.getCStat()));
                    mdfe.setDescricaoMotivoResposta(retornoMdfe.getXMotivo());
                    mdfe = repository.atualizar(mdfe);
                    salvaMdfeXml(xmlProc, mdfe);
                    salvarXmlProcessado(xmlProc, mdfe.getNomeXml());
                    Mensagem.addInfoMessage("MDF-e enviada com sucesso!");

                    break;
                case "204":
                case "539":
                    status = StatusTransmissao.DUPLICIDADE;
                    mdfe.setStatusMdfe(status.getCodigo());
                    repository.atualizar(mdfe);
                    Mensagem.addInfoMessage(retornoMdfe.getProtMDFe().getInfProt().getXMotivo());
                    break;
                default:
                    gerarXml(mdfe, enviMDFe, TipoArquivo.MDFePreProcessado);
                    Mensagem.addInfoMessage(retornoMdfe.getProtMDFe().getInfProt().getXMotivo());
                    break;
            }

        } else if (retornoMdfe.getCStat().equals("215") || retornoMdfe.getCStat().equals("225")) {
            if (org.springframework.util.StringUtils.isEmpty(configuracao.getCaminhoSchemas())) {
                Mensagem.addErrorMessage("Preenchimento do xml invalido para mais detalhes informes o cmainho dos schemas para validação");
            } else {
                String xml = gerarXml(mdfe, enviMDFe, TipoArquivo.MDFePreProcessado);

                String erroValidacao = ValidarMDFe.validaXml(xml, ConstantesMDFe.SERVICOS.ENVIO);
                Mensagem.addInfoMessage(StringUtils.isEmpty(erroValidacao) ? retornoMdfe.getXMotivo() : erroValidacao);
            }
            mdfe.setNumeroMdfe(null);
            mdfe.setSerie(null);
            status = StatusTransmissao.EDICAO;
            mdfe.setStatusMdfe(StatusTransmissao.EDICAO.getCodigo());
            repository.atualizar(mdfe);
        } else {
            Mensagem.addInfoMessage(retornoMdfe.getXMotivo());
        }


        return status;
    }

    public void salvaMdfeXml(String xml, MdfeCabecalho mdfe) {
        MdfeXml mdfeXml = new MdfeXml();
        if (StatusTransmissao.isAutorizado(mdfe.getStatusMdfe())) {
            mdfeXml.setMdfeCabecalho(mdfe);
            mdfeXml.setXml(xml.getBytes());
            xmlRepository.salvar(mdfeXml);
        } else {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro(Filtro.AND, "mdfeCabecalho.id", Filtro.IGUAL, mdfe.getId()));
            String atributos[] = null;
            mdfeXml = xmlRepository.get(MdfeXml.class, filtros, atributos);
            mdfeXml.setMdfeCabecalho(mdfe);
            mdfeXml.setXml(xml.getBytes());
            xmlRepository.atualizar(mdfeXml);
        }
    }

    private String gerarXml(MdfeCabecalho mdfe, TEnviMDFe enviMDFe, TipoArquivo tipo) throws Exception {
        String xml = XmlUtil.objectMdfeToXml(enviMDFe);
        String nomeXml = mdfe.getChaveAcessoCompleta() + ".xml";
        ArquivoUtil.getInstance().escrever(tipo, empresa.getCnpj(), xml.getBytes(), nomeXml);
        return xml;
    }

    public String gerarMdfePreProcessada(MdfeCabecalho mdfe) throws Exception {
        MdfeTransmissao transmissao = new MdfeTransmissao(empresa, getConfiguracao());
        TEnviMDFe enviMDFe = transmissao.gerarMdfeEnv(mdfe);
        String xml = XmlUtil.objectMdfeToXml(enviMDFe);
        return salvarXmlPreProcessado(xml, mdfe.getChaveAcessoCompleta() + ".xml");
    }

    public String salvarXmlPreProcessado(String xml, String nomeXml) throws IOException {
        return ArquivoUtil.getInstance().escrever(TipoArquivo.MDFePreProcessado, empresa.getCnpj(), xml.getBytes(), nomeXml);
    }

    public String salvarXmlProcessado(String xml, String nomeXml) throws IOException {
        return ArquivoUtil.getInstance().escrever(TipoArquivo.MDFe, empresa.getCnpj(), xml.getBytes(), nomeXml);
    }


    public String consultarNaoEncerrados(String cnpj) throws Exception {
        MdfeTransmissao transmissao = new MdfeTransmissao(empresa, getConfiguracao());
        return transmissao.consultarNaoEncerrados(cnpj);
    }


    private ConfiguracaoMdfeDTO getConfiguracao() throws Exception {
        if (configuracao == null) {
            configuracao = configuracaoRepository.getNamedQuery(ConfiguracaoMdfeDTO.class, "Mdfe.configuracao", empresa.getId());
        }
        if (configuracao == null) {
            throw new Exception("Configurações para o MDFe não definidas");
        }

        return configuracao;
    }

    private void verificarStatusNota(MdfeCabecalho mdfe) throws Exception {
        if (StatusTransmissao.isAutorizado(mdfe.getStatusMdfe())) {
            throw new Exception("Esta MDF-e já foi autorizada. Operação não permitida ");
        } else if (StatusTransmissao.isCancelada(mdfe.getStatusMdfe())) {
            throw new Exception("Esta MDF-e já foi autorizada. Operação não permitida ");
        }
    }

    private void definirNumero(MdfeCabecalho mdfe) throws Exception {
        notaService.definirChaveAcessoMdfe(mdfe);
    }

    private void definirEmitente(MdfeCabecalho mdfe) {

        EmpresaEndereco enderecoPrincipal = empresa.buscarEnderecoPrincipal();
        MdfeEmitente mdfeEmitente = new MdfeEmitente();
        mdfeEmitente.setMdfeCabecalho(mdfe);
        mdfeEmitente.setBairro(enderecoPrincipal.getBairro());
        mdfeEmitente.setCep(enderecoPrincipal.getCep());
        mdfeEmitente.setCodigoMunicipio(String.valueOf(enderecoPrincipal.getMunicipioIbge()));
        mdfeEmitente.setComplemento(enderecoPrincipal.getComplemento());
        mdfeEmitente.setLogradouro(enderecoPrincipal.getLogradouro());
        mdfeEmitente.setNomeMunicipio(enderecoPrincipal.getCidade());
        mdfeEmitente.setNumero(enderecoPrincipal.getNumero());
        mdfeEmitente.setTelefone(enderecoPrincipal.getFone());
        mdfeEmitente.setUf(enderecoPrincipal.getUf());
        mdfeEmitente.setCnpj(empresa.getCnpj());
        mdfeEmitente.setIe(empresa.getInscricaoEstadual());
        mdfeEmitente.setFantasia(empresa.getNomeFantasia());
        mdfeEmitente.setNome(empresa.getRazaoSocial());
        mdfe.setMdfeEmitente(mdfeEmitente);

    }

    private void validar(MdfeCabecalho mdfe) throws Exception {

        if (StatusTransmissao.isAutorizado(mdfe.getStatusMdfe())) {
            throw new Exception("MDF-e já autorizado !");
        }

        if (StatusTransmissao.isCancelada(mdfe.getStatusMdfe())) {
            throw new Exception("MDF-e já cancelado !");
        }

        if (mdfe.getListaMdfeMunicipioCarregamento().isEmpty()) {
            throw new Exception("Informe o Municípo de carregamento!");
        }
        if (mdfe.getListaMdfeMunicipioDescarregamento().isEmpty()) {

            throw new Exception("Informe o Municípo de descarregamento!");
        }

        if (mdfe.getTipoEmitente() == 1 && (mdfe.getListaMdfeInformacaoSeguro() == null || mdfe.getListaMdfeInformacaoSeguro().isEmpty())) {
            throw new Exception("Informe as informações do seguro da carga!");

        }

        if (mdfe.getMdfeRodoviario().getListaMdfeRodoviarioMotorista().isEmpty()) {
            throw new Exception("Informe os dados dos condutores!");

        }
        if (mdfe.getMdfeRodoviario().getListaMdfeRodoviarioVeiculo().isEmpty()) {
            throw new Exception("Informe os dados das veiculos!");

        }


        if (mdfe.getPesoBrutoCarga() == null || mdfe.getPesoBrutoCarga().equals(BigDecimal.ZERO)) {
            throw new Exception("Informe o peso Bruto da Carga!");

        }
        if (mdfe.getValorCarga() == null || mdfe.getValorCarga().equals(BigDecimal.ZERO)) {
            throw new Exception("Informe o valor Bruto da Carga!");

        }


    }


}
