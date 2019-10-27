package com.chronos.bo.nfe;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.com.samuelweb.certificado.exception.CertificadoException;
import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema_4.enviNFe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsSitNFe.TRetConsSitNFe;
import br.inf.portalfiscal.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import com.chronos.dto.ConfEmissorDTO;
import com.chronos.dto.EventoDTO;
import com.chronos.dto.RetornoEventoDTO;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.service.ChronosException;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.Estados;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.transmissor.init.Configuracoes;
import com.chronos.transmissor.nfe.Nfe;
import com.chronos.transmissor.util.ConstantesNFe;
import com.chronos.transmissor.util.XmlUtil;
import com.chronos.util.Constants;
import com.chronos.util.FormatValor;
import com.chronos.util.jsf.FacesUtil;

import java.util.Date;


/**
 * Created by john on 26/09/17.
 */
public class NfeTransmissao {


    private static NfeTransmissao instance;
    private Configuracoes configuracoes;


    public static NfeTransmissao getInstance() {
        if (instance == null) {
            instance = new NfeTransmissao();
        }
        return instance;
    }


    public TEnviNFe geraNFeEnv(NfeCabecalho nfe) throws Exception {

        GeraXMLEnvio geraXmlNfe = new GeraXMLEnvio();
        iniciarConfiguracoes();
        TEnviNFe nfeEnv = geraXmlNfe.gerarXmlEnvio(nfe);

        return nfeEnv;
    }

    public String gerarXmlNfe(NfeCabecalho nfe) throws Exception {

        TEnviNFe enviNFe = geraNFeEnv(nfe);
        String xml = XmlUtil.objectToXml(enviNFe);
        return xml;
    }

    public br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe.InfInut inutilizarNFe(int serie, int numInicial, int numFinal, ModeloDocumento modelo, String cnpj, String justificativa) throws Exception {

        if (justificativa.trim().length() < 15) {
            throw new Exception("A justificativa deve ter no mínimo 15 caracteres.");
        }
        if (justificativa.trim().length() > 255) {
            throw new Exception("A justificativa deve ter no máximo 255 caracteres.");
        }
        iniciarConfiguracoes();
        String id = "ID"
                + configuracoes.getEstado().getCodigoIbge()
                + FormatValor.getInstance().formatarAno(new Date())
                + cnpj
                + modelo.getCodigo()
                + FormatValor.getInstance().formatarSerieToString(serie)
                + FormatValor.getInstance().formatarNumeroDocFiscalToString(Integer.valueOf(numInicial))
                + FormatValor.getInstance().formatarNumeroDocFiscalToString(Integer.valueOf(numFinal));


        br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe retorno;
        String tipo = modelo == ModeloDocumento.NFE ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        retorno = Nfe.inutilizacao(id, justificativa, tipo, false);
        br.inf.portalfiscal.nfe.schema_4.inutNFe.TRetInutNFe.InfInut infRetorno = retorno.getInfInut();

        return infRetorno;
    }

    public String statusServico() throws Exception {
        TRetConsStatServ retorno = Nfe.statusServico(ConstantesNFe.NFE);
        String status;
        status = "Status:" + retorno.getCStat() + "\n";
        status += "Motivo:" + retorno.getXMotivo() + "\n";
        status += "Data:" + retorno.getDhRecbto() + "\n";

        return status;
    }

    public TRetEnvEvento enviarCartaCorrecao(EventoDTO eventoDTO) throws Exception {
        GeraXMLEnvio gerar = new GeraXMLEnvio();
        iniciarConfiguracoes();

        eventoDTO.setAmbiente(configuracoes.getAmbiente());
        eventoDTO.setCodigoUF(configuracoes.getEstado().getCodigoIbge());
        br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento envEvento = gerar.cartaCorrecao(eventoDTO);

        TRetEnvEvento retorno = Nfe.cce(envEvento, false, ConstantesNFe.NFE);


        return retorno;
    }

    public TRetConsSitNFe consultarNfe(String chave) throws Exception {
        iniciarConfiguracoes();
        TRetConsSitNFe retorno = Nfe.consultaXml(chave, ConstantesNFe.NFE);
        System.out.println("Status:" + retorno.getCStat());
        System.out.println("Motivo:" + retorno.getXMotivo());
        System.out.println("Data:" + retorno.getProtNFe().getInfProt().getDhRecbto());

        return retorno;

    }


    public RetornoEventoDTO cancelarNFe(EventoDTO eventoDTO) throws EmissorException {

        if (eventoDTO.getMotivo() == null) {
            throw new EmissorException("É necessário informar uma justificativa para o cancelamento da NF-e.");
        }
        if (eventoDTO.getMotivo().trim().equals("")) {
            throw new EmissorException("É necessário informar uma justificativa para o cancelamento da NF-e.");
        }
        if (eventoDTO.getMotivo().trim().length() < 15) {
            throw new EmissorException("A justificativa deve ter no mínimo 15 caracteres.");
        }
        if (eventoDTO.getMotivo().trim().length() > 255) {
            throw new EmissorException("A justificativa deve ter no máximo 255 caracteres.");
        }

        GeraXMLEnvio gerar = new GeraXMLEnvio();
        iniciarConfiguracoes();
        eventoDTO.setAmbiente(configuracoes.getAmbiente());
        eventoDTO.setCodigoUF(configuracoes.getEstado().getCodigoIbge());
        TEnvEvento enviEvento = gerar.cancelarNfe(eventoDTO);
        String modelo = eventoDTO.getModeloDocumento() == ModeloDocumento.NFE ? ConstantesNFe.NFE : ConstantesNFe.NFCE;
        br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TRetEnvEvento retornoSefaz = Nfe.cancelarNfe(enviEvento, false, modelo);

        RetornoEventoDTO retorno = new RetornoEventoDTO(enviEvento, retornoSefaz);

        return retorno;
    }


    public Configuracoes iniciarConfiguracoes(ConfEmissorDTO conf) throws CertificadoException {



            Certificado certificado = CertificadoService.certificadoPfx(conf.getCaminhoCertificado(), conf.getSenhaCertificado());

            configuracoes = Configuracoes.iniciaConfiguracoes(Estados.getUFbyIbge(conf.getCodigoUf().toString()), conf.getWebserviceAmbiente().toString(),
                    certificado, conf.getCaminhoSchemas(), conf.getVersao());

        configuracoes.setLog(Constants.DESENVOLVIMENTO);
            FacesUtil.setConfEmissor(configuracoes);

        return configuracoes;
    }


    public void iniciarConfiguracoes() {
        try {

            Configuracoes conf = FacesUtil.getConfEmissor();

            if (conf != null) {
                Configuracoes.iniciaConfiguracoes(conf.getEstado(), conf.getAmbiente(), conf.getCertificado(), conf.getPastaSchemas(), conf.getVersao());
            }


        } catch (Exception ex) {

        }
    }


    private void validarConfEmissor(ConfEmissorDTO configuracao) throws ChronosException {

        if (configuracao == null) {
            throw new ChronosException("Configuração não definida");
        } else if (configuracao.getWebserviceAmbiente() == null) {
            throw new ChronosException("Ambiente de transmissão não definido");
        } else if (configuracao.getCodigoUf() == null) {
            throw new ChronosException("UF de transmissão nao definido");
        } else if (configuracao.getCaminhoCertificado() == null || configuracao.getSenhaCertificado() == null) {
            throw new ChronosException("Certificado não definido");
        } else if (configuracao.getCaminhoSchemas() == null) {
            throw new ChronosException("Schemas não definido");
        }

    }


    public Configuracoes getConfiguracoes() {
        return configuracoes;
    }
}
