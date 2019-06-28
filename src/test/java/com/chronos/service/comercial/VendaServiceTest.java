package com.chronos.service.comercial;

import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.VendaCabecalho;
import com.chronos.modelo.entidades.VendaDetalhe;
import com.chronos.service.ChronosException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VendaServiceTest {


    private VendaCabecalho venda;
    private List<VendaDetalhe> itens;

    private VendaService service;

    @Before
    public void setUp() {

        service = new VendaService();

        venda = new VendaCabecalho();
        itens = new ArrayList<>();
        venda.setListaVendaDetalhe(itens);

        Produto prod = new Produto();

        VendaDetalhe item1 = new VendaDetalhe();
        item1.setQuantidade(BigDecimal.valueOf(2));
        item1.setValorUnitario(BigDecimal.valueOf(25));

        VendaDetalhe item2 = new VendaDetalhe();
        item2.setQuantidade(BigDecimal.valueOf(2));
        item2.setValorUnitario(BigDecimal.valueOf(25));

        itens.add(item1);
        itens.add(item2);

        venda.calcularValorTotal();
    }

    @Test
    public void devemos_garantir_que_os_valores_da_venda_sejam_calculado() {

        assertEquals(venda.getValorTotal(), new BigDecimal("100"));
        assertEquals(venda.getValorSubtotal(), new BigDecimal("100"));
    }

    @Test
    public void devemos_garantir_quer_o_valor_de_desconto_em_dinheiro_seja_aplicado_nos_itens() throws ChronosException {

        service.aplicarDesconto(venda, 1, BigDecimal.TEN);


        assertEquals(venda.getValorTotal(), BigDecimal.valueOf(90).setScale(2));

    }

    @Test
    public void devemos_garantir_quer_o_valor_de_desconto_em_percentual_seja_aplicado_nos_itens() throws ChronosException {

        service.aplicarDesconto(venda, 2, BigDecimal.TEN);
        assertEquals(venda.getValorTotal(), BigDecimal.valueOf(90).setScale(2));


    }

    @Test
    public void devemos_garantir_quer_o_valor_de_desconto_em_percentual_seja_aplicado_nos_itens_com_15_porcento() throws ChronosException {

        service.aplicarDesconto(venda, 2, BigDecimal.valueOf(15));
        assertEquals(venda.getValorTotal(), BigDecimal.valueOf(85).setScale(2));
    }


//    @Test()
//    public void devemos_garantir_que_os_valores_de_desconto_sejam_calculado_quando_alterar_valor_unitario(){
//
//        VendaDetalhe item =new VendaDetalhe();
//
//        item.setQuantidade(BigDecimal.ONE);
//        item.setValorUnitario(BigDecimal.TEN);
//        item.setValorVenda(BigDecimal.valueOf(5));
//        item.calcularValorTotal();
//
//        assertEquals(item.getValorDesconto(),new BigDecimal("5.00"));
//        assertEquals(item.getValorTotal(),new BigDecimal("5.00"));
//        assertEquals(item.getValorSubtotal(),new BigDecimal("10.00"));
//        assertEquals(item.getTaxaDesconto(),new BigDecimal("50.00"));
//    }
//
//    @Test()
//    public void devemos_garantir_que_os_valores_de_desconto_sejam_calculado_quando_informado_percentual(){
//
//        VendaDetalhe item =new VendaDetalhe();
//
//        item.setQuantidade(BigDecimal.ONE);
//        item.setValorUnitario(BigDecimal.TEN);
//        item.setValorVenda(BigDecimal.TEN);
//        item.setTaxaDesconto(new BigDecimal("50.00"));
//        item.calcularValorTotal();
//
//        assertEquals(item.getValorDesconto(),new BigDecimal("5.00"));
//        assertEquals(item.getValorTotal(),new BigDecimal("5.00"));
//        assertEquals(item.getValorSubtotal(),new BigDecimal("10.00"));
//        assertEquals(item.getTaxaDesconto(),new BigDecimal("50.00"));
//    }
//
//    @Test()
//    public void devemos_garantir_que_os_valores_de_desconto_sejam_calculado_com_preferencia_valor_unitario_alterado(){
//
//        VendaDetalhe item =new VendaDetalhe();
//
//        item.setQuantidade(BigDecimal.ONE);
//        item.setValorUnitario(BigDecimal.TEN);
//        item.setValorVenda(BigDecimal.valueOf(5));
//        item.setTaxaDesconto(BigDecimal.TEN);
//        item.calcularValorTotal();
//
//        assertEquals(item.getValorDesconto(),new BigDecimal("5.00"));
//        assertEquals(item.getValorTotal(),new BigDecimal("5.00"));
//        assertEquals(item.getValorSubtotal(),new BigDecimal("10.00"));
//        assertEquals(item.getTaxaDesconto(),new BigDecimal("50.00"));
//    }
//}
}