package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by john on 02/08/17.
 */
public class ProdutoFornecedorService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Produto> produtos;
    @Inject
    private Repository<FornecedorProduto> repository;
    @Inject
    private Repository<EmpresaProduto> empresaProdutos;
    @Inject
    private EmpresaProdutoService empresaProdutoService;

    @Inject
    private Repository<UnidadeConversao> unidadeConversaoRepository;

    @Inject
    private ProdutoService produtoService;

    @Transactional
    public FornecedorProduto salvar(Produto produto, Fornecedor fornecedor, Empresa empresa, BigDecimal valorCompra, String codigoFornecedor) throws ChronosException {


        UnidadeConversao unidadeConversao = produto.getConversoes().isEmpty() ? null : produto.getConversoes().get(0);


        if (produto.getId() == null) {
            List<Empresa> empresas = new ArrayList<>();
            empresas.add(empresa);

            produto = produtoService.salvar(produto, empresas, null);
        }


        FornecedorProduto forProd = new FornecedorProduto();
        forProd.setFornecedor(fornecedor);
        forProd.setProduto(produto);
        forProd.setCodigoFornecedorProduto(codigoFornecedor);
        forProd.setDataUltimaCompra(new Date());
        forProd.setPrecoUltimaCompra(valorCompra);


        FornecedorProduto fornecedorProduto = repository.atualizar(forProd);

        if (unidadeConversao != null) {
            unidadeConversao.setProduto(produto);
            unidadeConversaoRepository.salvar(unidadeConversao);
            produto.setUnidadeConversao(unidadeConversao);
        }

        return fornecedorProduto;
    }

    public boolean existeProduto(String codigo) {
        return repository.existeRegisro(FornecedorProduto.class, "codigoFornecedorProduto", codigo);
    }


    public Produto getProduto(String codigo) {
        Produto produto = repository.get(FornecedorProduto.class, "codigoFornecedorProduto", codigo, new Object[]{"produto.id,produto.nome"}).getProduto();

        return produto;
    }

    public void removerProdutoFornecedor(Integer idproduto, Integer idforncedor) {

        List<Filtro> filtros = new ArrayList<>();

        filtros.add(new Filtro("produto.id", idproduto));
        filtros.add(new Filtro("fornecedor.id", idforncedor));

        repository.excluir(FornecedorProduto.class, filtros);
    }


}
