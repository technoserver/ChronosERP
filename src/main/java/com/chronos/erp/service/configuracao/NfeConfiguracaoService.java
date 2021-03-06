package com.chronos.erp.service.configuracao;

import com.chronos.erp.dto.ConfiguracaoEmissorDTO;
import com.chronos.erp.dto.ConfiguracaoNfeDTO;
import com.chronos.erp.dto.ConfiguracaoPdvDTO;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.NfeConfiguracao;
import com.chronos.erp.modelo.entidades.PdvConfiguracao;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class NfeConfiguracaoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<NfeConfiguracao> repository;
    @Inject
    private Repository<PdvConfiguracao> pdvConfiguracaoRepository;


    public ConfiguracaoEmissorDTO instanciarConfNfe(Empresa empresa, ModeloDocumento modelo, String serie) throws ChronosException {

        ConfiguracaoEmissorDTO configuracao;

        if (modelo == ModeloDocumento.NFE) {
            ConfiguracaoNfeDTO configuracaoNfeDTO = repository.getNamedQuery(ConfiguracaoNfeDTO.class, "Nfe.configuracao", empresa.getId());

            if (configuracaoNfeDTO == null) {
                throw new ChronosException("É preciso definir as configuracoes para NF-e");
            }
            configuracao = new ConfiguracaoEmissorDTO(configuracaoNfeDTO);

         } else {

            if (StringUtils.isEmpty(serie)) {
                throw new ChronosException("É preciso definir a serie para localizar as configurações da NFC-e");
            }

            ConfiguracaoPdvDTO configuracaoPdvDTO = repository.getNamedQuery(ConfiguracaoPdvDTO.class, "Pdv.configuracao", empresa.getId(), serie);

            if (configuracaoPdvDTO == null) {
                throw new ChronosException("É preciso definir as configuracoes para NF-e");
            }
            configuracao = new ConfiguracaoEmissorDTO(configuracaoPdvDTO);
        }


        return configuracao;


    }

    public ConfiguracaoEmissorDTO geConfiguracoesNFe(Integer idempresa, ModeloDocumento modelo) throws Exception {

        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, idempresa));

        ConfiguracaoEmissorDTO configuracaoEmissorDTO;

        if (modelo == ModeloDocumento.NFE) {
            NfeConfiguracao configuracao = repository.get(NfeConfiguracao.class, filtros);

            if (configuracao == null) {
                throw new ChronosException("Configurações da NF-e  não definidas");
            }

            configuracaoEmissorDTO = new ConfiguracaoEmissorDTO(configuracao);

        } else {

            PdvConfiguracao configuracao = pdvConfiguracaoRepository.get(PdvConfiguracao.class, filtros);

            if (configuracao == null) {
                throw new ChronosException("Configurações da NFC-e  não definidas");
            }


            configuracaoEmissorDTO = new ConfiguracaoEmissorDTO(configuracao);
        }

        return configuracaoEmissorDTO;

    }

    public void validarConfEmissor(ConfiguracaoEmissorDTO configuracao) throws ChronosException {

        if (configuracao == null) {
            throw new ChronosException("Configuração não definida");
        } else if (configuracao.getWebserviceAmbiente() == null) {
            throw new ChronosException("Ambiente de transmissão não definido");
        } else if (configuracao.getWebserviceUf() == null) {
            throw new ChronosException("UF de transmissão nao definido");
        } else if (configuracao.getCertificadoDigitalCaminho() == null || configuracao.getCertificadoDigitalSenha() == null) {
            throw new ChronosException("Certificado não definido");
        } else if (configuracao.getCaminhoSchemas() == null) {
            throw new ChronosException("Schemas não definido");
        }

    }

}
