package com.chronos.bo.nfe;

import br.inf.portalfiscal.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.inf.portalfiscal.nfe.schema.envcce.TRetEnvEvento;
import br.inf.portalfiscal.nfe.schema.envinfe.TEnviNFe;
import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe;
import br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe;
import br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.infra.enuns.AmbienteEmissao;
import com.chronos.infra.enuns.Estados;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.nfe.Nfe;
import com.chronos.util.ConstantesNFe;


/**
 * Created by john on 26/09/17.
 */
public class NfeTransmissao {


    private final Empresa empresa;
    private final EmpresaEndereco endereco;

    public NfeTransmissao(Empresa empresa) {
        this.empresa = empresa;
        this.endereco = getEnderecoPrincipal(empresa);
    }


    public TEnviNFe geraNFeEnv(NfeCabecalho nfe, ConfiguracaoEmissorDTO configuracao) throws Exception {
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio geraXmlNfe = new GeraXMLEnvio();
        TEnviNFe nfeEnv = geraXmlNfe.gerarXmlEnvio(empresa, nfe);

        return nfeEnv;
    }

    public InfInut inutilizarNFe(ConfiguracaoEmissorDTO configuracao, String modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
        if (justificativa.trim().length() < 15) {
            throw new Exception("A justificativa deve ter no mínimo 15 caracteres.");
        }
        if (justificativa.trim().length() > 255) {
            throw new Exception("A justificativa deve ter no máximo 255 caracteres.");
        }
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio gerar = new GeraXMLEnvio();
        String codigoIBGE = Estados.getUFbySigla(endereco.getUf()).getCodigoIbge();
        TInutNFe inutNFe = gerar.inutilizarNfe(configuracao.getWebserviceAmbiente().toString(), codigoIBGE, modelo, serie, numInicial, numFinal, empresa.getCnpj(), justificativa);
        TRetInutNFe retorno = Nfe.inutilizacao(inutNFe, true, modelo.equals("55") ? ConstantesNFe.NFE : ConstantesNFe.NFCE);
        br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut infRetorno = retorno.getInfInut();
        return infRetorno;
    }

    public String statusServico(ConfiguracaoEmissorDTO configuracao) throws Exception {
        String status;
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio xmlEnvio = new GeraXMLEnvio();
        String codigoIBGE = Estados.getUFbySigla(endereco.getUf()).getCodigoIbge();
        status = xmlEnvio.consultarStatus(configuracao.getWebserviceAmbiente().toString(), codigoIBGE, "3.10");

        return status;
    }

    public TRetEnvEvento enviarCartaCorrecao(ConfiguracaoEmissorDTO configuracao, String chave, String correcao) throws Exception {
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio gerar = new GeraXMLEnvio();
        String codigoIBGE = Estados.getUFbySigla(endereco.getUf()).getCodigoIbge();
        br.inf.portalfiscal.nfe.schema.envcce.TEnvEvento evento = gerar.cartaCorrecao(chave, configuracao.getWebserviceAmbiente().toString(), codigoIBGE, empresa.getCnpj(), correcao);
        TRetEnvEvento retorno = Nfe.cce(evento, false, ConstantesNFe.NFE);

        return retorno;
    }

    public void instanciarConfiguracoes(ConfiguracaoEmissorDTO configuracao) throws Exception {
        ConfigurarAmbienteEmissor conf = new ConfigurarAmbienteEmissor(empresa.getCnpj(), "3.10", AmbienteEmissao.getByCodigo(configuracao.getWebserviceAmbiente()), endereco.getUf(), configuracao.getCaminhoSchemas());
        conf.instanciarConfiguracoes(configuracao.getCertificadoDigitalSenha());
    }

    private EmpresaEndereco getEnderecoPrincipal(Empresa empresa) {
        return empresa.getListaEndereco().stream()
                .filter(end -> end.getPrincipal().equals("S"))
                .findFirst().orElse(new EmpresaEndereco());
    }

    public TEnvEvento cancelarNFe(ConfiguracaoEmissorDTO configuracao, String protocolo, String uf, String ambiente, String chave, String justificativa) throws Exception {
        if (justificativa == null) {
            throw new Exception("É necessário informar uma justificativa para o cancelamento da NF-e.");
        }
        if (justificativa.trim().equals("")) {
            throw new Exception("É necessário informar uma justificativa para o cancelamento da NF-e.");
        }
        if (justificativa.trim().length() < 15) {
            throw new Exception("A justificativa deve ter no mínimo 15 caracteres.");
        }
        if (justificativa.trim().length() > 255) {
            throw new Exception("A justificativa deve ter no máximo 255 caracteres.");
        }
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio geraXml = new GeraXMLEnvio();
        String cnpj = empresa.getCnpj();
        TEnvEvento evento = geraXml.cancelarNfe(chave, protocolo, ambiente, uf, cnpj, justificativa);

        return evento;
    }
}
