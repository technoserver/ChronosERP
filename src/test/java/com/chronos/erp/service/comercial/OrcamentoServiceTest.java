package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.OrcamentoCabecalho;
import com.chronos.erp.modelo.entidades.OrcamentoDetalhe;
import com.chronos.erp.service.ChronosException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class OrcamentoServiceTest {

    private OrcamentoCabecalho orcamento;
    private OrcamentoService service;

    @Before
    public void setUp() {
        orcamento = new OrcamentoCabecalho();
        service = new OrcamentoService();

    }

    @Test
    public void devemos_garantir_que_ser_possivel_add_um_novo_item_no_orcamento() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.TEN);
        service.salvarItem(orcamento, item, null, 1);
        assertEquals(orcamento.getListaOrcamentoDetalhe().size(), 1);
    }

    @Test(expected = ChronosException.class)
    public void devemos_garantir_que_nao_seja_possivel_add_item_com_valores_zerados() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();

        service.salvarItem(orcamento, item, null, 1);
    }

    @Test
    public void devemos_garantir_que_seja_possivel_calcular_os_valores_totais_do_item_ao_add() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.TEN);

        service.salvarItem(orcamento, item, null, 1);

        assertEquals(item.getValorSubtotal(), BigDecimal.TEN.setScale(2));
        assertEquals(item.getValorTotal(), BigDecimal.TEN.setScale(2));
    }

    @Test
    public void devemos_garantir_que_seja_possivel_calcular_os_valores_totais_do_item_ao_add_com_desconto_em_dinheiro() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.TEN);

        service.salvarItem(orcamento, item, BigDecimal.valueOf(5), 1);

        assertEquals(item.getValorSubtotal(), BigDecimal.TEN.setScale(2));
        assertEquals(item.getValorTotal(), BigDecimal.valueOf(5).setScale(2));
        assertEquals(item.getValorDesconto(), BigDecimal.valueOf(5));
    }

    @Test
    public void devemos_garantir_que_seja_possivel_calcular_os_valores_totais_do_item_ao_add_com_desconto_em_percentual() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.TEN);

        service.salvarItem(orcamento, item, BigDecimal.valueOf(50), 0);

        assertEquals(item.getValorSubtotal(), BigDecimal.TEN.setScale(2));
        assertEquals(item.getValorTotal(), BigDecimal.valueOf(5).setScale(2));
        assertEquals(item.getValorDesconto(), BigDecimal.valueOf(5).setScale(2));
    }

    @Test
    public void devemos_garantir_que_seja_possveil_calcular_os_totais_com_desconto() throws ChronosException {

        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.valueOf(0.5));
        item.setValorUnitario(BigDecimal.valueOf(4.99));

        service.salvarItem(orcamento, item, BigDecimal.valueOf(0.25), 1);

        OrcamentoDetalhe item2 = new OrcamentoDetalhe();
        item2.setQuantidade(BigDecimal.valueOf(5));
        item2.setValorUnitario(BigDecimal.valueOf(2.99));

        service.salvarItem(orcamento, item2, BigDecimal.valueOf(1.5), 1);


        OrcamentoDetalhe item3 = new OrcamentoDetalhe();
        item3.setQuantidade(BigDecimal.valueOf(4));
        item3.setValorUnitario(BigDecimal.valueOf(1.99));

        service.salvarItem(orcamento, item3, BigDecimal.valueOf(0.8), 1);


        OrcamentoDetalhe item4 = new OrcamentoDetalhe();
        item4.setQuantidade(BigDecimal.valueOf(6));
        item4.setValorUnitario(BigDecimal.valueOf(3.99));

        service.salvarItem(orcamento, item4, BigDecimal.valueOf(2.39), 1);

        assertEquals(orcamento.getValorSubtotal(), BigDecimal.valueOf(49.35));
        assertEquals(orcamento.getValorTotal(), BigDecimal.valueOf(44.41));
        assertEquals(orcamento.getValorDesconto(), BigDecimal.valueOf(4.94));

    }

    @Test
    public void devemos_garantir_que_seja_possivel_aplicar_desconto_em_todo_os_itens() throws ChronosException {
        OrcamentoDetalhe item = new OrcamentoDetalhe();
        item.setQuantidade(BigDecimal.valueOf(2));
        item.setValorUnitario(BigDecimal.valueOf(36.47));

        service.salvarItem(orcamento, item, null, 1);

        service.aplicarDesconto(orcamento, 1, BigDecimal.valueOf(0.94));

        assertEquals(item.getValorSubtotal(), BigDecimal.valueOf(72.94));
        assertEquals(item.getValorTotal(), BigDecimal.valueOf(72).setScale(2));
    }


}
