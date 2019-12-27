package com.chronos.erp.repository;

import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.modelo.entidades.NfeDetalhe;
import com.chronos.erp.modelo.enuns.Modulo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by john on 14/10/17.
 */
public class NfeRepository extends AbstractRepository implements Serializable {

    private NfeCabecalho nfe;

    public NfeCabecalho procedimentoNfeAutorizada(NfeCabecalho nfe, boolean atualizarEstoque, Modulo modulo) throws Exception {
        this.nfe = nfe;
        nfe = atualizar(nfe);

        if (atualizarEstoque) {

            if (nfe.getTipoOperacao().equals(1)) {
                if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                    atualizaEstoqueEmpresaEstoqueVerificado(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe(), modulo);
                } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                    atualizaEstoqueVerificado(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe(), modulo);
                } else {
                    atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe(), modulo);
                }
            } else {
                procedimentoNfeCancelada(nfe, true);
            }


        }


        return nfe;
    }

    public NfeCabecalho procedimentoNfeCancelada(NfeCabecalho nfe, boolean estoque) throws Exception {
        this.nfe = atualizar(nfe);
        if (estoque) {
            for (NfeDetalhe nfeDetalhe : nfe.getListaNfeDetalhe()) {

                if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                    atualizaEstoqueEmpresaEstoqueVerificado(nfe.getEmpresa().getId(), nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial());
                } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                    atualizaEstoqueVerificado(nfe.getEmpresa().getId(), nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial());
                } else {
                    atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial());
                }


            }
        }

        return this.nfe;
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe, Modulo modulo) throws Exception {

        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if (nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")) {

                atualizarEstoqueMovimento(nfeDetalhe.getProduto().getId(), idEmpresa, nfeDetalhe.getQuantidadeComercial(), modulo.getCodigo(), nfeDetalhe.getNfeCabecalho().getNumero(), "V", "S");
                atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());

            }

        }
    }

    public void atualizaEstoqueVerificado(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe, Modulo modulo) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if (nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")) {
                atualizarEstoqueMovimento(nfeDetalhe.getProduto().getId(), idEmpresa, nfeDetalhe.getQuantidadeComercial(), modulo.getCodigo(), nfeDetalhe.getNfeCabecalho().getNumero(), "V", "S");
                atualizaEstoqueVerificado(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());

            }

        }
    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe, Modulo modulo) throws Exception {

        String numero = listaNfeDetalhe.get(0).getNfeCabecalho().getNumero();
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if (nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")) {
                atualizarEstoqueMovimento(nfeDetalhe.getProduto().getId(), idEmpresa, nfeDetalhe.getQuantidadeComercial(), modulo.getCodigo(), numero, "V", "S");
                atualizarEstoqueMovimento(nfeDetalhe.getProduto().getId(), idEmpresa, nfeDetalhe.getQuantidadeComercial(), modulo.getCodigo(), numero, "F", "S");
                atualizaEstoqueEmpresaEstoqueVerificado(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());

            }

        }
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizaEstoqueVerificado(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado= p.estoqueVerificado + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {
        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1,p.estoqueVerificado= p.estoqueVerificado + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizarEstoqueMovimento(int idproduto, int idempresa, BigDecimal quantidade, String codigo_modulo, String documento, String tipo, String entradaSaida) {

        String sql = "SELECT CAST(movimento_produto(" + idempresa + "," + idproduto + "," + quantidade + ",'" + codigo_modulo + "" +
                "','" + documento + "','" + tipo + "','" + entradaSaida + "') AS text)";
        em.createNativeQuery(sql).getSingleResult();
    }

}
