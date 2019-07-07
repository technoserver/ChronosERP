package com.chronos.repository;

import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.NfeDetalhe;
import com.chronos.modelo.enuns.Modulo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by john on 14/10/17.
 */
public class NfeRepository extends AbstractRepository implements Serializable {

    private NfeCabecalho nfe;

    public NfeCabecalho procedimentoNfeAutorizada(NfeCabecalho nfe, boolean atualizarEstoque) throws Exception {
        this. nfe = nfe;
        nfe = atualizar(nfe);

        if (atualizarEstoque) {

            if (nfe.getTipoOperacao().equals(1)) {
                if (nfe.getTributOperacaoFiscal().getEstoqueVerificado() && nfe.getTributOperacaoFiscal().getEstoque()) {
                    atualizaEstoqueEmpresaEstoqueVerificado(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
                } else if (nfe.getTributOperacaoFiscal().getEstoqueVerificado()) {
                    atualizaEstoqueVerificado(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
                } else {
                    atualizaEstoqueEmpresa(nfe.getEmpresa().getId(), nfe.getListaNfeDetalhe());
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


    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {

        listaNfeDetalhe.stream()
                .filter(item->item.getProduto().getServico().equalsIgnoreCase("N"))
                .forEach(nfeDetalhe->{

                });
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if(nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")){
                atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
            }

        }
    }

    public void atualizaEstoqueVerificado(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if(nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")){
                atualizaEstoqueVerificado(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
            }

        }
    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        String codigo = nfe.getCodigoModelo().equals("55") ? Modulo.NFe.getCodigo() : Modulo.NFCe.getCodigo();
        String numero = listaNfeDetalhe.get(0).getNfeCabecalho().getNumero();
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            if(nfeDetalhe.getProduto().getServico().equalsIgnoreCase("N")){
                atualizaEstoqueEmpresaEstoqueVerificado(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
                atualizarEstoqueMovimento(nfeDetalhe.getProduto().getId(), idEmpresa, nfeDetalhe.getQuantidadeComercial().negate(), codigo, numero, "S");
            }

        }
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);
        String codigo = nfe.getCodigoModelo().equals("55") ? Modulo.NFe.getCodigo() : Modulo.NFCe.getCodigo();
        atualizarEstoqueMovimento(idProduto, idEmpresa, quantidade, codigo, nfe.getNumero(), "S");

    }

    public void atualizaEstoqueVerificado(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado= p.estoqueVerificado + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizaEstoqueEmpresaEstoqueVerificado(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {
        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + ?1,p.estoqueVerificado= p.estoqueVerificado + ?1 where p.produto.id = ?2 and p.empresa.id= ?3";
        execute(jpql, quantidade, idProduto, idEmpresa);

    }

    public void atualizarEstoqueMovimento(int idproduto, int idempresa, BigDecimal quantidade, String codigo_modulo, String documento, String tipo) {

        String sql = "SELECT CAST(movimento_produto(" + idproduto + "," + idempresa + "," + quantidade + ",'" + codigo_modulo + "','" + documento + "','" + tipo + "') AS text)";
        em.createNativeQuery(sql).getSingleResult();
    }

}
