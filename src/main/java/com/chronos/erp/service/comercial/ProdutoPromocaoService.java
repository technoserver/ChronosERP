package com.chronos.erp.service.comercial;

import com.chronos.erp.modelo.entidades.ProdutoPromocao;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jpa.Transactional;

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
