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


    public NfeCabecalho procedimentoNfeAutorizada(NfeCabecalho nfe, boolean atualizarEstoque) throws Exception {
        nfe = atualizar(nfe);
        if (atualizarEstoque) {
            if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                atualizaEstoqueEmpresaEstoqueVerificado(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
            } else {
                atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
            }
        }

        return nfe;
    }

    public NfeCabecalho procedimentoNfeCancelada(NfeCabecalho nfe, boolean estoque) throws Exception {
        nfe = atualizar(nfe);
        if (estoque) {
            for (NfeDetalhe nfeDetalhe : nfe.getListaNfeDetalhe()) {
                atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial());
            }
        }

        return nfe;
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresaEstoqueVerificado(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, Integer idProduto, BigDecimal quantidade, BigDecimal quantidadeVerificada) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1,p.estoqueVerificado= p.estoqueVerificado + ?2 where p.produto.id = ?3 and p.empresa.id= ?4";
        execute(jpql, quantidade, quantidadeVerificada, idProduto, idEmpresa);

    }

}
