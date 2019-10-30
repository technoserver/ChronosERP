package com.chronos.erp.cadastros;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.service.ChronosException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProdutoTest {

    private Produto produto;
    private PdvConfiguracaoBalanca balanca;

    private TabelaNutricionalCabecalho tabelaNutricional;

    @Before
    public void setUp() {
        produto = new Produto();
        produto.setNome("AR LI 1/2 ZB");
        produto.setValorVenda(BigDecimal.valueOf(17.82));
        produto.setCodigoBalanca(12764);

        balanca = new PdvConfiguracaoBalanca();
        balanca.setTamanhoIdentificador(5);
        balanca.setTamanhoCodigoProduto(5);
        balanca.setIdentificador("P");

        tabelaNutricional = new TabelaNutricionalCabecalho();
        tabelaNutricional.setNome("30g ou 3 fatias*******************F");
        tabelaNutricional.setPorcao(new BigDecimal("100"));
        tabelaNutricional.setUnidade("gr");

        tabelaNutricional.setNutrientes(new ArrayList<>());

        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(1, "Valor Calorico"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(2, "Carboidratos"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(3, "Proteinas"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(4, "Gorduras Totais"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(5, "Gorduras Saturadas"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(6, "Gorduras Trans"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(7, "Fibra alimentar"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(8, "Sodio"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(9, "Calcio"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(14, "Ferro"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(19, "Colesterol"), BigDecimal.TEN, BigDecimal.valueOf(1234)));


        produto.setBalanca(balanca);


    }

    @Test
    public void devemos_garantir_que_codigo_produto_tenha_6_casas() throws ChronosException {

        String itemBalanca = produto.montarItemBalancaFilizola();
        String codigoProduto = itemBalanca.substring(0, 6);
        Assert.assertEquals("012764", codigoProduto);
    }

    @Test
    public void devemos_garantir_que_seja_definido_identificador() throws ChronosException {

        String itemBalanca = produto.montarItemBalancaFilizola();
        String identificador = itemBalanca.substring(6, 7);
        Assert.assertEquals("P", identificador);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_descricao_produto_com_22_casas() throws ChronosException {
        String itemBalanca = produto.montarItemBalancaFilizola();
        String descricao = itemBalanca.substring(7, 29);
        Assert.assertTrue(descricao.length() == 22);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_valor_com_7_casas() throws ChronosException {

        String itemBalanca = produto.montarItemBalancaFilizola();
        String valor = itemBalanca.substring(29, 36);
        Assert.assertTrue(valor.length() == 7);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_valor_gerado_esteja_correto() throws ChronosException {
        produto.setValorVenda(new BigDecimal("29.99"));

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(29, 36);
        BigDecimal valor = new BigDecimal(str);
        BigDecimal valorVenda = valor.divide(BigDecimal.valueOf(100));

        Assert.assertEquals(produto.getValorVenda(), valorVenda);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_validade_com_3_casas() throws ChronosException {

        produto.setDiasValidade(187);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(36, 39);
        Assert.assertTrue(str.length() == 3);
    }

    @Test
    public void devemos_garantir_que_quando_tive_valor_nutricional_informe_separador_arroba() throws ChronosException {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(39, 40);

        Assert.assertEquals("@", str);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_descricao_nutricional_com_35_casas() throws ChronosException {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(40, 75);
        Assert.assertTrue(str.length() == 35);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_valor_calorico() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(75, 80);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_calorico() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(80, 84);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_valor_proteina() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(84, 89);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_proteina() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(89, 93);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_gordura_totais() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(93, 98);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_gordura_totais() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(98, 102);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_gordura_saturada() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(102, 107);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_gordura_saturada() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(107, 111);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }


    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_gordura_trans() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(111, 116);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_gordura_trans() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(116, 120);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_fibra_alimentar() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(120, 125);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_fibra_alimentar() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(125, 129);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_calcio() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(129, 134);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_calcio() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(134, 138);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }


    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_ferro() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(138, 143);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_ferro() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(143, 147);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_quantidade_sodio() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(147, 152);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 10);
    }

    @Test
    public void devemos_garantir_que_seja_gerado_porcetagem_sodio() throws ChronosException {
        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaFilizola();
        String str = itemBalanca.substring(152, 156);
        int valor = Integer.parseInt(str);
        Assert.assertTrue(valor == 1234);
    }


    @Test
    public void devemos_garatir_que_seja_gerado_layout_basico() throws ChronosException {


        String itemBalanca = produto.montarItemBalancaFilizola();
        Assert.assertEquals("012764PAR LI 1/2 ZB          0001782000", itemBalanca);
    }

    @Test
    public void devemos_garatir_que_seja_gerado_layout_com_validade() throws ChronosException {
        produto.setDiasValidade(90);

        String itemBalanca = produto.montarItemBalancaFilizola();
        Assert.assertEquals("012764PAR LI 1/2 ZB          0001782090", itemBalanca);
    }


}
