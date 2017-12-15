package com.chronos.service.cadastros;

import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.Produto;
import com.chronos.repository.EstoqueRepository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by john on 14/12/17.
 */

public class ProdutoService implements Serializable {

    private static final long serialVersionUID = 8349487869365747547L;

    @Inject
    private EstoqueRepository repository;


    public List<Produto> getListProdutoVenda(String nome, Empresa empresa) {
        List<Produto> produtos = new ArrayList<>();
        List<ProdutoDTO> produtosDTO = new ArrayList<>();
        try {
            produtosDTO = repository.getProdutoDTO(nome, empresa);
            produtos = produtosDTO.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;

    }
}
