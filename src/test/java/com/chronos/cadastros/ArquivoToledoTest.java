package com.chronos.cadastros;

import com.chronos.modelo.entidades.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ArquivoToledoTest {

    private Produto produto;
    private PdvConfiguracaoBalanca balanca;

    private TabelaNutricionalCabecalho tabelaNutricional;


    @Before
    public void setUp() {
        produto = new Produto();
        produto.setNome("COCA COLA 350ML");
        produto.setValorVenda(BigDecimal.valueOf(17.82));
        produto.setCodigoBalanca(12764);

        balanca = new PdvConfiguracaoBalanca();
        balanca.setTamanhoIdentificador(5);
        balanca.setTamanhoCodigoProduto(5);
        balanca.setIdentificador("P");

        tabelaNutricional = new TabelaNutricionalCabecalho();
        tabelaNutricional.setId(12764);

        tabelaNutricional.setNome("30g ou 3 fatias*******************F");
        tabelaNutricional.setPorcao(new BigDecimal("300"));
        tabelaNutricional.setUnidade("GR");
        tabelaNutricional.setParteInteiraMedidaCaseria(1);
        tabelaNutricional.setMedidaCaseiraUtilizada("16");
        tabelaNutricional.setParteDecimalMedidaCaseria(0);

        tabelaNutricional.setNutrientes(new ArrayList<>());

        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(1, "Valor Calorico"), BigDecimal.valueOf(1622), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(2, "Carboidratos"), BigDecimal.valueOf(0.6), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(3, "Proteinas"), BigDecimal.valueOf(9.5), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(4, "Gorduras Totais"), BigDecimal.valueOf(12.3), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(5, "Gorduras Saturadas"), BigDecimal.valueOf(8.5), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(6, "Gorduras Trans"), BigDecimal.valueOf(4), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(7, "Fibra alimentar"), BigDecimal.valueOf(23), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(8, "Sodio"), BigDecimal.valueOf(304), BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(9, "Calcio"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(14, "Ferro"), BigDecimal.TEN, BigDecimal.valueOf(1234)));
        tabelaNutricional.getNutrientes().add(new TabelaNutricionalDetalhe(new Nutriente(19, "Colesterol"), BigDecimal.TEN, BigDecimal.valueOf(1234)));


        produto.setBalanca(balanca);


    }

    @Test
    public void devemos_garantir_que_o_inicio_da_string_da_info_nutricional_comerce_com_a_letra_N() {
        produto.setTabelaNutricional(tabelaNutricional);
        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String codigo = itemBalanca.substring(0, 1);
        Assert.assertEquals("N", codigo);
    }

    @Test
    public void devemos_garantir_a_geracao_do_codigo_nutricional() {
        produto.setTabelaNutricional(tabelaNutricional);
        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String codigo = itemBalanca.substring(1, 7);
        Assert.assertEquals("012764", codigo);
    }

    @Test
    public void devemos_garantir_a_geracao_do_reservado_da_info_nutricional() {
        produto.setTabelaNutricional(tabelaNutricional);
        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String codigo = itemBalanca.substring(7, 8);
        Assert.assertEquals("0", codigo);
    }

    @Test
    public void devemos_garantir_a_geracao_da_quantidade_da_info_nutricional() {
        produto.setTabelaNutricional(tabelaNutricional);
        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String codigo = itemBalanca.substring(8, 11);
        Assert.assertEquals("300", codigo);
    }


    @Test
    public void devemos_garantir_a_geracao_da_undidade_da_info_nutricional() {
        produto.setTabelaNutricional(tabelaNutricional);
        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String codigo = itemBalanca.substring(11, 12);
        Assert.assertEquals("0", codigo);
    }

    @Test
    public void devemos_garantir_a_geracao_da_parte_interia_da_medida_caseria_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(12, 14);
        Assert.assertEquals("01", str);
    }


    @Test
    public void devemos_garantir_a_geracao_da_parte_decimal_da_medida_caseria_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(14, 15);
        Assert.assertEquals(tabelaNutricional.getParteDecimalMedidaCaseria().toString(), str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_medida_caseria_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(15, 17);
        Assert.assertEquals(tabelaNutricional.getMedidaCaseiraUtilizada(), str);
    }

    @Test
    public void devemos_garantir_a_geracao_do_valor_calorico_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(17, 21);
        Assert.assertEquals("1622", str);
    }

    @Test
    public void devemos_garantir_a_geracao_do_carboidratos_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(21, 25);
        Assert.assertEquals("0006", str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_proteinas_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(25, 28);
        Assert.assertEquals("095", str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_gorduras_totais_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(28, 31);
        Assert.assertEquals("123", str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_gorduras_saturadas_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(31, 34);
        Assert.assertEquals("085", str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_gorduras_trans_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(34, 37);
        Assert.assertEquals("040", str);
    }

    @Test
    public void devemos_garantir_a_geracao_da_fibra_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(37, 40);
        Assert.assertEquals("230", str);
    }

    @Test
    public void devemos_garantir_a_geracao_do_sodio_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();
        String str = itemBalanca.substring(40, 45);
        Assert.assertEquals("03040", str);
    }

    @Test
    public void devemos_garantir_a_geracao_de_da_info_nutricional() {

        produto.setTabelaNutricional(tabelaNutricional);

        String itemBalanca = produto.montarItemBalancaToledoNutricao();

        Assert.assertEquals("N01276403000010161622000609512308504023003040", itemBalanca);
    }

//    @Test
//    public void devemos_garantir_a_geracao_do_produto() throws ChronosException {
//        produto.setTabelaNutricional(tabelaNutricional);
//        String itemBalanca = produto.montarItemBalancaToledo();
//        String str = "011000004000200000COCA COLA 350ML                                   0000040000000004110000000000000000000000000000000000000000000000000000000000000000";
//
//        Assert.assertEquals(itemBalanca, str);
//    }


}
