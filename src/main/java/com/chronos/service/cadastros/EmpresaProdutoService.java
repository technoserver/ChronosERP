package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EmpresaProduto;
import com.chronos.modelo.entidades.Produto;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmpresaProdutoService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<EmpresaProduto> repository;

    @Transactional
    public void novoProduto(Empresa empresa, Produto produto) {

        EmpresaProduto empProduto = new EmpresaProduto();
        produto.setProdutosEmpresa(new ArrayList<>());
        empProduto.setEmpresa(empresa);
        empProduto.setProduto(produto);
        empProduto.setQuantidadeEstoque(BigDecimal.ZERO);
        empProduto.setEstoqueVerificado(BigDecimal.ZERO);
        repository.salvar(empProduto);

    }

    @Transactional
    public void novoProdutoQuandoNaoExiste(Empresa empresa, Produto produto) {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("empresa.id", empresa.getId()));
        filtros.add(new Filtro("produto.id", produto.getId()));
        boolean existeRegisro = repository.existeRegisro(EmpresaProduto.class, filtros);
        if (!existeRegisro) {
            novoProduto(empresa, produto);
        }
    }


}
