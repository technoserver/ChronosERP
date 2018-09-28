package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

    @Transactional
    public FornecedorProduto salvar(Produto produto, Fornecedor fornecedor, Empresa empresa, BigDecimal valorCompra, String codigoFornecedor) {
        boolean salvarEmpresaProduto = produto.getId() == null;

        UnidadeConversao unidadeConversao = produto.getConversoes().isEmpty() ? null : produto.getConversoes().get(0);

        produto = produto.getId() != null ? produto : produtos.atualizar(produto);


        FornecedorProduto forProd = new FornecedorProduto();
        forProd.setFornecedor(fornecedor);
        forProd.setProduto(produto);
        forProd.setCodigoFornecedorProduto(codigoFornecedor);
        forProd.setDataUltimaCompra(new Date());
        forProd.setPrecoUltimaCompra(valorCompra);


        if (salvarEmpresaProduto) {
            empresaProdutoService.novoProduto(empresa, produto);
        }

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


}
