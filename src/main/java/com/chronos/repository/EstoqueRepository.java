package com.chronos.repository;

import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.NfeDetalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by john on 25/09/17.
 */
public class EstoqueRepository extends AbstractRepository implements Serializable {

    private static final long serialVersionUID = 1L;


    public void atualizaEstoque(List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoque(nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }


    public void atualizaEstoque(Integer idProduto, BigDecimal quantidade) throws Exception {
        //atualiza tabela PRODUTO
        String jpql = "UPDATE Produto p set p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.id = :id";
        // update(jpql, idProduto, quantidade);
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {
        try {
            abrirConexao();
            String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1 where p.produto.id = ?2 and p.empresa.id=?3";
            execute(jpql, quantidade, idProduto, idEmpresa);
        } catch (Exception e) {
            throw e;
        } finally {
            if (isAutoCommit()) {
                fecharConexao();
            }
        }

    }

    public List<NfeDetalhe> getItens(NfeCabecalho nfeCabecalho) throws Exception {
        abrirConexao();
        String jpql = "SELECT NEW NfeDetalhe (o.id, o.produto, o.quantidadeComercial) FROM NfeDetalhe o WHERE o.nfeCabecalho.id = ?1";
        List<NfeDetalhe> itens = getEntity(NfeDetalhe.class, jpql, nfeCabecalho.getId());
        return itens;

    }
}
