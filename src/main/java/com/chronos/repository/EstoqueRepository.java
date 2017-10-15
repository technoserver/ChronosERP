package com.chronos.repository;

import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.NfeCabecalho;
import com.chronos.modelo.entidades.NfeDetalhe;
import com.chronos.modelo.entidades.Produto;
import com.chronos.util.jpa.Transactional;

import javax.persistence.TypedQuery;
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

    @Transactional
    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";
        //  execute(jpql, quantidade, idProduto, idEmpresa);

        // String jpql = "UPDATE Produto p set p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.id = :id";

        TypedQuery query = (TypedQuery) em.createQuery(jpql);
        query.setParameter("quantidade", quantidade);
        query.setParameter("idproduto", idProduto);
        query.setParameter("idempresa", idEmpresa);
        query.executeUpdate();

    }

    public List<NfeDetalhe> getItens(NfeCabecalho nfeCabecalho) throws Exception {

        //  abrirConexao();
        String jpql = "SELECT NEW NfeDetalhe (o.id, o.produto, o.quantidadeComercial) FROM NfeDetalhe o WHERE o.nfeCabecalho.id = ?1";
        List<NfeDetalhe> itens = getEntity(NfeDetalhe.class, jpql, nfeCabecalho.getId());
        return itens;
    }

    public List<Produto> getProdutoEmpresa(String nome, Empresa empresa) throws Exception {

            String jpql = "select new Produto(p.id,p.nome,p.valorVenda,ep.quantidadeEstoque,p.ncm,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,p.unidadeProduto) From Produto p " +
                    "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                    "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null)";


        List<Produto> produtos = getEntity(Produto.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
            return produtos;


    }


}
