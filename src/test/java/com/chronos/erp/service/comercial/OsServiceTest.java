package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.OsAbertura;
import com.chronos.erp.modelo.entidades.OsProdutoServico;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.service.ChronosException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class OsServiceTest {

    private OsService service;
    private OsAbertura os;

    @Before
    public void setUp() {
        service = new OsService();

        os = new OsAbertura();
        os.setListaOsProdutoServico(new HashSet<>());
    }

    @Test
    public void devemos_garantir_que_seja_possivel_aplicar_desconto_em_dinheiro_por_item() throws ChronosException {

        OsProdutoServico item = new OsProdutoServico();

        Produto prod = new Produto();

        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.valueOf(30));
        item.setTipo(0);
        item.setProduto(prod);
        os.getListaOsProdutoServico().add(item);
        service.salvarItem(os, item, "RS", BigDecimal.valueOf(10));
        OsProdutoServico item2 = new OsProdutoServico();

        item2.setQuantidade(BigDecimal.ONE);
        item2.setValorUnitario(BigDecimal.valueOf(30));
        prod.setServico("S");
        item2.setProduto(prod);
        os.getListaOsProdutoServico().add(item2);
        service.salvarItem(os, item2, "RS", BigDecimal.valueOf(10));

        assertThat(os.getValorTotalDesconto(), equalTo(BigDecimal.valueOf(20)));
        assertThat(os.getValorTotal(), equalTo(BigDecimal.valueOf(40)));
        assertThat(os.getValorTotalServico(), equalTo(BigDecimal.valueOf(20)));
        assertThat(os.getValorTotalProduto(), equalTo(BigDecimal.valueOf(20)));
    }

    @Test
    public void devemos_garantir_que_seja_possivel_aplicar_desconto_em_percentual_por_item() throws ChronosException {

        OsProdutoServico item = new OsProdutoServico();

        Produto prod = new Produto();

        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.valueOf(30));
        item.setTipo(0);
        item.setProduto(prod);
        os.getListaOsProdutoServico().add(item);
        service.salvarItem(os, item, "%", BigDecimal.valueOf(33.33));

        assertThat(os.getValorTotal(), equalTo(BigDecimal.valueOf(20).setScale(2)));
        assertThat(os.getValorTotalDesconto(), equalTo(BigDecimal.valueOf(10).setScale(2)));
        assertThat(os.getValorTotalServico(), equalTo(BigDecimal.valueOf(0)));
        assertThat(os.getValorTotalProduto(), equalTo(BigDecimal.valueOf(20).setScale(2)));
    }

    @Test
    public void devemos_garantir_que_ao_remover_o_desconto_os_valores_fiquem_corretos() throws ChronosException {
        OsProdutoServico item = new OsProdutoServico();

        Produto prod = new Produto();

        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.valueOf(30));
        item.setTipo(0);
        item.setProduto(prod);
        os.getListaOsProdutoServico().add(item);
        service.salvarItem(os, item, "%", BigDecimal.valueOf(33.33));

        service.removerDesconto(os);

        assertThat(os.getValorTotal(), equalTo(BigDecimal.valueOf(30)));
        assertThat(os.getValorTotalDesconto(), equalTo(BigDecimal.ZERO));
        assertThat(os.getValorTotalServico(), equalTo(BigDecimal.valueOf(0)));
        assertThat(os.getValorTotalProduto(), equalTo(BigDecimal.valueOf(30)));
    }

    @Test
    public void devemos_garantir_que_seja_possivel_aplicar_um_desconto_dinheiro_no_total_da_venda() throws ChronosException {
        OsProdutoServico item = new OsProdutoServico();

        Produto prod = new Produto();

        item.setQuantidade(BigDecimal.ONE);
        item.setValorUnitario(BigDecimal.valueOf(30));
        item.setTipo(0);
        item.setProduto(prod);
        os.getListaOsProdutoServico().add(item);
        service.salvarItem(os, item, "%", BigDecimal.ZERO);

        OsProdutoServico item2 = new OsProdutoServico();

        item2.setQuantidade(BigDecimal.ONE);
        item2.setValorUnitario(BigDecimal.valueOf(30));
        prod.setServico("S");
        item2.setProduto(prod);
        os.getListaOsProdutoServico().add(item2);
        service.salvarItem(os, item2, "RS", BigDecimal.ZERO);


        service.aplicarDesconto(os, 1, BigDecimal.TEN);

        assertThat(os.getValorTotal(), equalTo(BigDecimal.valueOf(50).setScale(2)));
        assertThat(os.getValorTotalDesconto(), equalTo(BigDecimal.TEN.setScale(2)));
        assertThat(os.getValorTotalServico(), equalTo(BigDecimal.valueOf(25).setScale(2)));
        assertThat(os.getValorTotalProduto(), equalTo(BigDecimal.valueOf(25).setScale(2)));

    }
}
