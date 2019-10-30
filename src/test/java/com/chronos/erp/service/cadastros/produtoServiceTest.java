package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.EmpresaProduto;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.entidades.TributGrupoTributario;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class produtoServiceTest {


    private ProdutoService service;

    @Mock
    private Repository<Produto> produtoRepository;
    @Mock
    private Repository<EmpresaProduto> empresaProdutoRepository;

    private Produto produto;
    private List<Empresa> empresas;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ProdutoService(null, produtoRepository, empresaProdutoRepository, null);
        produto = new Produto();
        empresas = new ArrayList<>();
        Empresa empresa = new Empresa();
        empresas.add(empresa);
    }

    @Test(expected = ChronosException.class)
    public void devemos_garantir_que_seja_informado_o_tipo_no_produto() throws ChronosException {
        service.salvar(produto, empresas, null);
    }

    @Test(expected = ChronosException.class)
    public void devemos_garantir_para_produto_do_tipo_venda_seja_obrigatorio_informa_a_tributacao() throws ChronosException {
        produto.setTipo("V");
        service.salvar(produto, empresas, null);
    }

    @Test
    public void devemos_garantir_que_seja_possivel_salvar_um_produto() throws ChronosException {

        when(produtoRepository.atualizar(produto)).thenReturn(new Produto(1, "PRODUTO TEste"));

        produto.setTipo("V");
        produto.setNome("PRODUTO TEste");
        produto.setTributGrupoTributario(new TributGrupoTributario(1));
        service.salvar(produto, empresas, null);
    }
}
