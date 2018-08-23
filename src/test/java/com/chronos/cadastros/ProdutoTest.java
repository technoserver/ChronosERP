package com.chronos.cadastros;

import com.chronos.modelo.entidades.Produto;
import com.chronos.service.ChronosException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class ProdutoTest {

    private Produto produto;


    @Before
    public void setUp() {
        produto = new Produto();

    }

    @Test
    public void gerarItemBalancaFilizola() throws ChronosException {
        produto.setNome("AR LI 1/2 ZB");
        produto.setValorVenda(BigDecimal.valueOf(17.82));
        produto.setCodigoBalanca(12764);

        String itemBalanca = produto.montarItemBalancaFilizola();
        Assert.assertEquals("012764PAR LI 1/2 ZB          0001782000", itemBalanca);
    }

    @Test
    public void gerarItemBalancaToledo() throws ChronosException {
        produto.setNome("APRESUNTADO FRIMESA KG");
        produto.setValorVenda(BigDecimal.valueOf(10.25));
        produto.setCodigoBalanca(28583);

        String itemBalanca = produto.montarItemBalancaToledo();
        Assert.assertEquals("010028583001025000APRESUNTADO FRIMESA KG                            0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000", itemBalanca);
    }
}
