package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.VendaCabecalho;
import com.chronos.erp.modelo.entidades.VendaDetalhe;
import com.chronos.erp.service.ChronosException;
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
        item1.calcularValorTotal();
        VendaDetalhe item2 = new VendaDetalhe();
        item2.setQuantidade(BigDecimal.valueOf(2));
        item2.setValorUnitario(BigDecimal.valueOf(25));
        item2.calcularValorTotal();
        itens.add(item1);
        itens.add(item2);

        venda.calcularValorTotal();
    }

    @Test
    public void devemos_garantir_que_os_valores_da_venda_sejam_calculado() {

        assertEquals(venda.getValorTotal(), new BigDecimal("100").setScale(2));
        assertEquals(venda.getValorSubtotal(), new BigDecimal("100").setScale(2));


    }


    @Test
    public void devemos_garantir_que_os_valores_da_venda_sejam_calculado_quand_add_item() throws ChronosException {


        VendaDetalhe item = new VendaDetalhe();
        item.setQuantidade(BigDecimal.valueOf(2));
        item.setValorUnitario(BigDecimal.valueOf(25));
        item.setProduto(new Produto(1, ""));

        service.addItem(venda, item, null, 1);

        assertEquals(venda.getValorTotal(), new BigDecimal("150").setScale(2));
        assertEquals(venda.getValorSubtotal(), new BigDecimal("150").setScale(2));
    }

    @Test
    public void devemos_garantir_que_os_valores_da_venda_sejam_calculado_quand_add_item_com_desconto() throws ChronosException {


        VendaDetalhe item = new VendaDetalhe();
        item.setQuantidade(BigDecimal.valueOf(2));
        item.setValorUnitario(BigDecimal.valueOf(25));
        item.setProduto(new Produto(1, ""));

        service.addItem(venda, item, BigDecimal.TEN, 1);

        assertEquals(venda.getValorTotal(), new BigDecimal("140").setScale(2));
        assertEquals(venda.getValorSubtotal(), new BigDecimal("150").setScale(2));
    }


    @Test
    public void devemos_garantir_quer_o_valor_de_desconto_em_dinheiro_seja_aplicado_nos_itens() throws ChronosException {

        service.aplicarDesconto(venda, 1, BigDecimal.TEN);


        assertEquals(venda.getValorTotal(), BigDecimal.valueOf(90).setScale(2));

    }

    @Test
    public void devemos_garantir_que_seja_possveil_calcular_os_totais_com_desconto() throws ChronosException {

        venda.getListaVendaDetalhe().clear();

        VendaDetalhe item = new VendaDetalhe();
        item.setQuantidade(BigDecimal.valueOf(0.5));
        item.setValorUnitario(BigDecimal.valueOf(4.99));

        service.addItem(venda, item, BigDecimal.valueOf(0.25), 1);

        VendaDetalhe item2 = new VendaDetalhe();
        item2.setQuantidade(BigDecimal.valueOf(5));
        item2.setValorUnitario(BigDecimal.valueOf(2.99));

        service.addItem(venda, item2, BigDecimal.valueOf(1.5), 1);


        VendaDetalhe item3 = new VendaDetalhe();
        item3.setQuantidade(BigDecimal.valueOf(4));
        item3.setValorUnitario(BigDecimal.valueOf(1.99));

        service.addItem(venda, item3, BigDecimal.valueOf(0.8), 1);


        VendaDetalhe item4 = new VendaDetalhe();
        item4.setQuantidade(BigDecimal.valueOf(6));
        item4.setValorUnitario(BigDecimal.valueOf(3.99));

        service.addItem(venda, item4, BigDecimal.valueOf(2.39), 1);

        assertEquals(venda.getValorSubtotal(), BigDecimal.valueOf(49.35));
        assertEquals(venda.getValorTotal(), BigDecimal.valueOf(44.41));
        assertEquals(venda.getValorDesconto(), BigDecimal.valueOf(4.94));

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