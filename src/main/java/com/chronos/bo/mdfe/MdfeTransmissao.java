package com.chronos.bo.mdfe;


import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import br.inf.portalfiscal.mdfe.schema_300.consReciMDFe.TConsReciMDFe;
import br.inf.portalfiscal.mdfe.schema_300.enviMDFe.TEnviMDFe;
import br.inf.portalfiscal.mdfe.schema_300.retConsReciMDFe.TRetConsReciMDFe;
import br.inf.portalfiscal.mdfe.schema_300.retEnviMDFe.TRetEnviMDFe;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.MdfeCabecalho;
import com.chronos.modelo.entidades.MdfeConfiguracao;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.Estados;
import com.chronos.transmissor.init.Configuracoes;
import com.chronos.transmissor.mdfe.Mdfe;
import com.chronos.util.ArquivoUtil;

import java.io.File;


/**
 * Created by john on 19/06/18.
 */
public class MdfeTransmissao {

    private Empresa empresa;
    private Configuracoes confEnvio;
    private MdfeConfiguracao configuracao;
    private Certificado certificado;

    public MdfeTransmissao(Empresa empresa, MdfeConfiguracao configuracao) {
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


    private void instanciarConfiguracoes(MdfeConfiguracao configuracao) throws Exception {
        if (confEnvio == null) {
            if (configuracao == null) {
                throw new Exception("Configurações de transmissão não definidas !");
            }
            certificado = getCertificaodServidor(configuracao.getCertificadoDigitalSenha());
            iniciarConfiguracoes(certificado, configuracao.getWebserviceAmbiente(), configuracao.getCaminhoSchemas());
            if (certificado == null) {
                throw new Exception("É Necessário informar os dados do certificado antes do envio !");
            }
            if (confEnvio == null) {
                throw new Exception("É Nescessário iniciar as configurações de transmissao!");
            }
        }

    }

    public Configuracoes iniciarConfiguracoes(Certificado certificado, int ambiente, String caminhoSchema) throws Exception {
        confEnvio = Configuracoes.iniciaConfiguracoes(Estados.getUFbyIbge(empresa.getCodigoIbgeUf().toString()), String.valueOf(ambiente), certificado, caminhoSchema, "3.00");
        confEnvio.setProtocol(true);
        return confEnvio;
    }

    private Certificado getCertificaodServidor(String senha) throws Exception {
        String caminhoArquivo = ArquivoUtil.getInstance().getCertificado(empresa.getCnpj());
        File fileCertificado = new File(caminhoArquivo);
        Certificado cert = fileCertificado.exists() ? CertificadoService.certificadoPfx(caminhoArquivo, senha) : null;
        return cert;
    }

}
