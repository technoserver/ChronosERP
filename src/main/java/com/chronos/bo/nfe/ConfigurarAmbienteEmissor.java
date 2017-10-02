package com.chronos.bo.nfe;

import br.com.samuelweb.certificado.Certificado;
import br.com.samuelweb.certificado.CertificadoService;
import com.chronos.exception.EmissorException;
import com.chronos.infra.enuns.AmbienteEmissao;
import com.chronos.infra.enuns.Estados;
import com.chronos.init.Configuracoes;
import com.chronos.util.ArquivoUtil;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Created by john on 26/09/17.
 */
public class ConfigurarAmbienteEmissor {


    private Certificado certificado;
    private Configuracoes configuracoes;

    private String cnpj;
    private String versao;
    private AmbienteEmissao ambiente;
    private String uf;
    private String caminhoSchema;


    private ConfigurarAmbienteEmissor() {
    }


    public ConfigurarAmbienteEmissor(String cnpj, String versao, AmbienteEmissao ambiente, String uf, String caminhoSchema) {
        this.cnpj = cnpj;
        this.versao = versao;
        this.ambiente = ambiente;
        this.uf = uf;
        this.caminhoSchema = caminhoSchema;
    }

    public Certificado getCertificaodServidor(String senha) throws Exception {
        String caminhoArquivo = ArquivoUtil.getInstance().getCertificado(cnpj);
        File fileCertificado = new File(caminhoArquivo);
        Certificado cert = fileCertificado.exists() ? CertificadoService.certificadoPfx(caminhoArquivo, senha) : null;
        return cert;
    }

    public Configuracoes iniciarConfiguracoes(Certificado certificado) throws EmissorException, Exception {
        configuracoes = Configuracoes.iniciaConfiguracoes(Estados.getUFbySigla(uf), String.valueOf(ambiente.getCodigo()), certificado, caminhoSchema, versao);
        configuracoes.setProtocol(true);
        return configuracoes;
    }

    public void instanciarConfiguracoes(String senhaCertificado) throws Exception {
        validarConfiguracoes();
        if (StringUtils.isEmpty(senhaCertificado)) {
            throw new Exception("Configurações de transmissão não definidas 'SENHA CERTIFICADO'!");
        }
        certificado = getCertificaodServidor(senhaCertificado);
        iniciarConfiguracoes(certificado);
        if (certificado == null) {
            throw new Exception("É Necessário informar os dados do certificado antes do envio !");
        }
        if (!certificado.isValido()) {
            throw new Exception("Certificado não é valido data de validade " + certificado.getVencimento());
        }
        if (configuracoes == null) {
            throw new Exception("É Nescessário iniciar as configurações de transmissao!");
        }

    }

    public void validarConfiguracoes() throws Exception {


        if (StringUtils.isEmpty(cnpj)) {
            throw new Exception("Configurações de transmissão não definidas 'CNPJ'!");
        } else if (StringUtils.isEmpty(versao)) {
            throw new Exception("Configurações de transmissão não definidas 'VERSAO'!");
        } else if (StringUtils.isEmpty(ambiente)) {
            throw new Exception("Configurações de transmissão não definidas 'AMBIENTE'!");
        } else if (StringUtils.isEmpty(uf)) {
            throw new Exception("Configurações de transmissão não definidas 'UF'!");
        } else if (StringUtils.isEmpty(caminhoSchema)) {
            throw new Exception("Configurações de transmissão não definidas 'CAMINHO SCHEMAS'!");
        }
    }
}
