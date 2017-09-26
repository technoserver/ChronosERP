package com.chronos.bo.nfe;

import com.chronos.AmbienteEmissao;
import com.chronos.infra.enuns.Estados;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.NfeConfiguracao;

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
