package com.chronos.repository;

import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.NfeDetalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by john on 14/10/17.
 */
public class NfeRepository extends AbstractRepository implements Serializable {


    public NfeCabecalho procedimentoNfeAutorizada(NfeCabecalho nfe) throws Exception {
        nfe = atualizar(nfe);
        atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
        return nfe;
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

}
