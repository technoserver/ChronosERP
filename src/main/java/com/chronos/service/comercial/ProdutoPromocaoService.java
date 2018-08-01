package com.chronos.service.comercial;

import com.chronos.modelo.entidades.Produto;
import com.chronos.modelo.entidades.ProdutoPromocao;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;

public class ProdutoPromocaoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoPromocao> repository;
    @Inject
    private Repository<Produto> produtoRepository;


    @Transactional
    public ProdutoPromocao salvar(ProdutoPromocao produtoPromocao) {
        Produto produto = produtoRepository.get(produtoPromocao.getProduto().getId(), Produto.class);
        produto.setDataAlteracao(new Date());
        produtoRepository.atualizar(produto);
        return repository.atualizar(produtoPromocao);
    }
}
