package com.chronos.bo.nfe;

import br.inf.portalfiscal.nfe.schema.inutnfe.TInutNFe;
import br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe;
import br.inf.portalfiscal.nfe.schema.retinutnfe.TRetInutNFe.InfInut;
import com.chronos.AmbienteEmissao;
import com.chronos.infra.enuns.Estados;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.NfeConfiguracao;
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


    public InfInut inutilizarNFe(NfeConfiguracao configuracao, String modelo, Integer serie, Integer numInicial, Integer numFinal, String justificativa) throws Exception {
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

    public String statusServico(NfeConfiguracao configuracao) throws Exception {
        String status;
        instanciarConfiguracoes(configuracao);
        GeraXMLEnvio xmlEnvio = new GeraXMLEnvio();
        String codigoIBGE = Estados.getUFbySigla(endereco.getUf()).getCodigoIbge();
        status = xmlEnvio.consultarStatus(configuracao.getWebserviceAmbiente().toString(), codigoIBGE, "3.10");

        return status;
    }

    public void instanciarConfiguracoes(NfeConfiguracao configuracao) throws Exception {
        ConfigurarAmbienteEmissor conf = new ConfigurarAmbienteEmissor(empresa.getCnpj(), "3.10", AmbienteEmissao.getByCodigo(configuracao.getWebserviceAmbiente()), endereco.getUf(), configuracao.getCaminhoSchemas());
        conf.instanciarConfiguracoes(configuracao.getCertificadoDigitalSenha());
    }

    private EmpresaEndereco getEnderecoPrincipal(Empresa empresa) {
        return empresa.getListaEndereco().stream()
                .filter(end -> end.getPrincipal().equals("S"))
                .findFirst().orElse(new EmpresaEndereco());
    }
}
