package com.chronos.service.configuracao;

import com.chronos.modelo.entidades.PdvConfiguracaoBalanca;
import com.chronos.modelo.enuns.ModeloBalanca;
import com.chronos.service.ChronosException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PdvConfiguracaoTest {

    private BalancaService service;
    private PdvConfiguracaoBalanca conf;

    @Before
    public void before() {
        service = new BalancaService();
        conf = new PdvConfiguracaoBalanca();
        conf.setTamanhoCodigoProduto(5);
        conf.setTamanhoIdentificador(5);
        conf.setIdentificador("T");

    }

    @Test
    public void devemos_garantir_que_gere_layut() throws ChronosException {

        conf.setModelo(ModeloBalanca.FILIZOLA);

        String layout = service.montaLayout(conf);

        assertFalse(StringUtils.isEmpty(layout));
    }

    @Test(expected = ChronosException.class)
    public void devemos_garantir_que_tamanho_das_configuracao_nao_passe_de_13() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(6);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");


        service.montaLayout(conf);
    }


    @Test
    public void devemos_garantir_que_gere_layut_com_codigo_produto() throws ChronosException {

        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.contains("C"));
    }

    @Test
    public void devemos_garantir_que_gere_layut_com_codigo_produto_de_acordo_com_tamanho() throws ChronosException {

        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);

        String layout = service.montaLayout(conf);


        Pattern pattern = Pattern.compile("[C]");
        Matcher matcher = pattern.matcher(layout);
        int countCharacter = 0;
        while (matcher.find()) {
            countCharacter++;
        }


        assertTrue(countCharacter == conf.getTamanhoCodigoProduto());
    }

    @Test
    public void devemos_garantir_que_gere_layut_com_tipo() throws ChronosException {

        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.contains("T"));
    }

    @Test
    public void devemos_garantir_que_gere_layut_com_tipo_de_acordo_com_tamanho() throws ChronosException {

        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);


        Pattern pattern = Pattern.compile("[T]");
        Matcher matcher = pattern.matcher(layout);
        int countCharacter = 0;
        while (matcher.find()) {
            countCharacter++;
        }


        assertTrue(countCharacter == conf.getTamanhoIdentificador());
    }

    @Test
    public void devemos_garantir_que_layut_tenha_um_tamanho_13() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.length() == 14);
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCC0TTTTTTDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCC0TTTTTTDV"));
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCC00PPPPPDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(5);
        conf.setIdentificador("P");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCC00PPPPPDV"));
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCC00QQQQQDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(4);
        conf.setTamanhoIdentificador(5);
        conf.setIdentificador("Q");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCC00QQQQQDV"));
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCCCTTTTTTDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(5);
        conf.setTamanhoIdentificador(6);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCCCTTTTTTDV"));
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCCC0PPPPPDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(5);
        conf.setTamanhoIdentificador(5);
        conf.setIdentificador("P");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCCC0PPPPPDV"));
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_nesse_padrao_1CCCCCCTTTTTDV() throws ChronosException {
        conf.setModelo(ModeloBalanca.FILIZOLA);
        conf.setTamanhoCodigoProduto(6);
        conf.setTamanhoIdentificador(5);
        conf.setIdentificador("T");

        String layout = service.montaLayout(conf);

        assertTrue(layout.equals("1CCCCCCTTTTTDV"));
    }

}
