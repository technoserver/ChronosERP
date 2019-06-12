package com.chronos.service.comercial;

import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaEndereco;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.service.ChronosException;
import com.chronos.service.configuracao.NfeConfiguracaoService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class NfeServiceTest {

    private NfeService service;
    private Empresa empresa;

    @Mock
    private NfeConfiguracaoService configuracaoService;
    private ConfiguracaoEmissorDTO configuracao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new NfeService(null, configuracaoService, null, null,
                null, null, null, null, null, null);
        empresa = new Empresa();
        empresa.setListaEndereco(new HashSet<>());

        EmpresaEndereco end = new EmpresaEndereco();
        end.setPrincipal("S");

        empresa.getListaEndereco().add(end);

        configuracao = new ConfiguracaoEmissorDTO();
        configuracao.setWebserviceAmbiente(2);
        configuracao.setWebserviceUf("28");
        configuracao.setCertificadoDigitalSenha("12345678");
        configuracao.setCertificadoDigitalCaminho(this.getClass().getResource("/arquivos/certificado.pfx").getPath());
        configuracao.setCaminhoSchemas("");

    }

    @Test(expected = ChronosException.class)
    public void devemos_garantir_que_para_gerar_daados_emitente_os_dados_da_empresa_estejam_completo() throws ChronosException {
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setEmpresa(empresa);
        service.definirEmitente(nfe);
    }

    @Test
    public void devemos_garantir_ser_possivel_gerar_emitente_a_partir_da_empresa() throws ChronosException {
        NfeCabecalho nfe = new NfeCabecalho();
        nfe.setEmpresa(empresa);
        service.definirEmitente(nfe);
    }

    @Test
    public void devemos_garantir_gerar_dados_padroes_para_nfe() throws Exception {


        when(configuracaoService.instanciarConfNfe(empresa, ModeloDocumento.NFE)).thenReturn(configuracao);

        NfeCabecalho nfe = service.dadosPadroes(empresa, ModeloDocumento.NFE);
        assertNotNull(nfe);

        assertNotNull(nfe.getTransporte());
        assertNotNull(nfe.getTransporte().getModalidadeFrete());

    }


}
