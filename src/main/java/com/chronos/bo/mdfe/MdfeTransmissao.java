package com.chronos.bo.mdfe;


import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.inf.portalfiscal.mdfe.schema_300.consMDFeNaoEnc.TConsMDFeNaoEnc;
import br.inf.portalfiscal.mdfe.schema_300.consReciMDFe.TConsReciMDFe;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEnviMDFe;
import br.inf.portalfiscal.mdfe.schema_300.retConsReciMDFe.TRetConsReciMDFe;
import br.inf.portalfiscal.mdfe.schema_300.retEnviMDFe.TRetEnviMDFe;
import com.chronos.dto.ConfiguracaoMdfeDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.MdfeCabecalho;
import com.chronos.service.ChronosException;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.Estados;
import com.chronos.transmissor.init.Configuracoes;
import com.chronos.transmissor.mdfe.Mdfe;

import java.io.File;


/**
 * Created by john on 19/06/18.
 */
public class MdfeTransmissao {

    private Empresa empresa;
    private Configuracoes confEnvio;
    private ConfiguracaoMdfeDTO configuracao;
    private Certificado certificado;

    public MdfeTransmissao(Empresa empresa, ConfiguracaoMdfeDTO configuracao) {
        this.empresa = empresa;
        this.configuracao = configuracao;
    }

    public TRetConsReciMDFe transmitirMdfe(TEnviMDFe enviMDFe) throws Exception {

        instanciarConfiguracoes(configuracao);
        TRetEnviMDFe retorno = Mdfe.enviar(enviMDFe);
        if (!retorno.getCStat().equals("103")) {

            throw new EmissorException(
                    "Status:" + retorno.getCStat() + " - Motivo:" + retorno.getXMotivo());
        }
        String recibo = retorno.getInfRec().getNRec();

        TConsReciMDFe consReciMDFe = new TConsReciMDFe();
        consReciMDFe.setVersao(confEnvio.getVersao());
        consReciMDFe.setTpAmb(confEnvio.getAmbiente());
        consReciMDFe.setNRec(recibo);
        TRetConsReciMDFe retornoMdfe;
        while (true) {
            retornoMdfe = Mdfe.consultarRecibo(consReciMDFe, false);
            if (retornoMdfe.getCStat().equals("105")) {
                System.out.println("Lote Em Processamento, vai tentar novamente apos 2 Segundo.");
                Thread.sleep(1000);
                continue;
            } else {
                break;
            }

        }
        return retornoMdfe;
    }

    public TEnviMDFe gerarMdfeEnv(MdfeCabecalho mdfe) throws Exception {
        instanciarConfiguracoes(configuracao);
        GerarXmlEnvio geraXml = new GerarXmlEnvio();
        TEnviMDFe enviMDFe = geraXml.gerarMdfe(mdfe);
        return enviMDFe;
    }

    public String consultarNaoEncerrados(String cnpj) throws Exception {
        instanciarConfiguracoes(configuracao);
        GerarXmlEnvio geraXml = new GerarXmlEnvio();
        TConsMDFeNaoEnc consMDFeNaoEnc = geraXml.consultarNaoEcenrrados(configuracao.getWebserviceAmbiente().toString(), cnpj);


        br.inf.portalfiscal.mdfe.schema_300.consMDFeNaoEnc.TRetConsMDFeNaoEnc retorno = Mdfe
                .consultarNaoEncerrado(consMDFeNaoEnc, false);

        String result = "";
        result += "Status:" + retorno.getCStat() + "\n";
        result += "Motivo:" + retorno.getXMotivo() + "\n";
        result += "UF:" + retorno.getCUF() + "\n";

        if (retorno.getInfMDFe() != null) {
            result = retorno.getInfMDFe().stream()
                    .map((inf) -> "Chave :" + inf.getChMDFe() + " Número Protocolo " + inf.getNProt() + "\n")
                    .reduce(result, String::concat);
        }

        return result;
    }


    private void instanciarConfiguracoes(ConfiguracaoMdfeDTO configuracao) throws Exception {
        if (confEnvio == null) {
            if (configuracao == null) {
                throw new ChronosException("Configurações de transmissão não definidas !");
            }
            certificado = getCertificaodServidor(configuracao.getCertificadoDigitalCaminho(), configuracao.getCertificadoDigitalSenha());
            iniciarConfiguracoes(certificado, configuracao.getWebserviceAmbiente(), configuracao.getCaminhoSchemas());
            if (certificado == null) {
                throw new ChronosException("É Necessário informar os dados do certificado antes do envio !");
            }
            if (confEnvio == null) {
                throw new ChronosException("É Nescessário iniciar as configurações de transmissao!");
            }
        }

    }

    public Configuracoes iniciarConfiguracoes(Certificado certificado, int ambiente, String caminhoSchema) {
        confEnvio = Configuracoes.iniciaConfiguracoes(Estados.getUFbyIbge(empresa.getCodigoIbgeUf().toString()), String.valueOf(ambiente), certificado, caminhoSchema, "3.00");
        confEnvio.setProtocol(true);
        return confEnvio;
    }

    private Certificado getCertificaodServidor(String caminho, String senha) throws Exception {
        File fileCertificado = new File(caminho);
        Certificado cert = fileCertificado.exists() ? CertificadoService.certificadoPfx(caminho, senha) : null;
        return cert;
    }

}
