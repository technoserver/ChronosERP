package com.chronos.service.comercial;

import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEnviMDFe;
import br.inf.portalfiscal.mdfe.schema_300.retConsReciMDFe.TRetConsReciMDFe;
import com.chronos.bo.mdfe.MdfeTransmissao;
import com.chronos.dto.DocFiscalDto;
import com.chronos.exception.EmissorException;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.enuns.TipoArquivo;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.*;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
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

    private MdfeConfiguracao configuracao;


    @PostConstruct
    private void init() {
        empresa = FacesUtil.getEmpresaUsuario();

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

        if (veiculo.getProprietarioVeiculo() != null) {
            ProprietarioVeiculo proprietario = veiculo.getProprietarioVeiculo();
            MdfeRodoviarioProprietarioVeiculo propVeic = new MdfeRodoviarioProprietarioVeiculo();
            propVeic.setMdfeRodoviarioVeiculo(mdfeRodoviarioVeiculo);
            propVeic.setCpfCnpj(proprietario.getCpfCnpj());
            propVeic.setIe(proprietario.getIe());
            propVeic.setNome(proprietario.getNome());
            propVeic.setRntrc(proprietario.getRntrc());
            propVeic.setTipo(proprietario.getTipo());
            propVeic.setUf(proprietario.getUf());

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

        mdfe = repository.atualizar(mdfe);
        return mdfe;
    }

    public StatusTransmissao enviarMdfe(MdfeCabecalho mdfe) throws EmissorException, Exception {
        StatusTransmissao status = StatusTransmissao.ENVIADA;

            verificarStatusNota(mdfe);

            MdfeTransmissao transmissao = new MdfeTransmissao(empresa, getConfiguracao());
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
                        status = StatusTransmissao.EDICAO;
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
                    String xml = gerarXml(mdfe, enviMDFe, TipoArquivo.CTePreProcessado);
                    ;
                    String erroValidacao = ValidarMDFe.validaXml(xml, ConstantesMDFe.SERVICOS.ENVIO);
                    Mensagem.addInfoMessage(StringUtils.isEmpty(erroValidacao) ? retornoMdfe.getXMotivo() : erroValidacao);
                }
                status = StatusTransmissao.EDICAO;
                mdfe.setStatusMdfe(status.EDICAO.getCodigo());
                repository.atualizar(mdfe);
            } else {
                Mensagem.addInfoMessage(retornoMdfe.getXMotivo());
            }



        return status;
    }

    public void salvaMdfeXml(String xml, MdfeCabecalho mdfe) throws Exception {
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


    private MdfeConfiguracao getConfiguracao() throws Exception {
        if (configuracao == null) {
            configuracao = configuracaoRepository.get(MdfeConfiguracao.class, "empresa.id", empresa.getId());
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

    private void definirNumero(MdfeCabecalho mdfe) {
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

    private NotaFiscalTipo getNotaFicalTipo(String modelo, Empresa empresa) throws Exception {

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


}
