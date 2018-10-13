package com.chronos.service.comercial;

import com.chronos.modelo.entidades.ProdutoPromocao;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;

public class ProdutoPromocaoService implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoPromocao> repository;


    @Transactional
    public ProdutoPromocao salvar(ProdutoPromocao produtoPromocao) {
        return repository.atualizar(produtoPromocao);
    }
}
