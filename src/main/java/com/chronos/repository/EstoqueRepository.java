package com.chronos.repository;

import com.chronos.dto.EstoqueIdealDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewProdutoEmpresa;
import com.chronos.util.jpa.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, List<VendaDetalhe> itens) throws Exception {
        for (VendaDetalhe item : itens) {
            atualizaEstoqueEmpresaControle(idEmpresa, item.getProduto().getId(), item.getQuantidade().negate());
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

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.controle = p.controle + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";
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

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<ViewProdutoEmpresa> root = criteria.from(ViewProdutoEmpresa.class);


        String jpql = "select new com.chronos.dto.ProdutoDTO(p.id,p.nome,p.valorVenda,ep.quantidadeEstoque,ep.controle,p.ncm,p.imagem,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,un.sigla) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null)";


        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
        return null;


    }

    public List<ProdutoDTO> getProdutoDTO(String nome, Empresa empresa) throws Exception {

//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
//        Root<ViewProdutoEmpresa> root = criteria.from(ViewProdutoEmpresa.class);
//
//        criteria.select(builder.construct(Produto.class
//                ,root.get("id"), root.get("nome")
//                ,root.get("valorVenda"), root.get("quantidade"),
//                root.get("controle"), root.get("ncm")
//                ,root.get("imagem"), root.get("tributGrupoTributario")),
//                root.get("tributIcmsCustomCab"), root.get("unidadeProduto"));


//        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();;
//        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
//        Root<Produto> root = criteriaQuery.from(Produto.class);
//        Join<Pessoa, EmpresaProduto> join = root.join("ep", JoinType.INNER);
//
//
//        Predicate or = criteriaBuilder.or(
//                criteriaBuilder.isNotNull(root.get("tributIcmsCustomCab")),criteriaBuilder.isNotNull(root.get("tributGrupoTributario")));
//
//        criteriaQuery.select(root).where(criteriaBuilder.equal(join.get("tipo"), "TIPO")).where(or);
//
//        TypedQuery typedQuery = em.createQuery(criteriaQuery);
//        typedQuery.getResultList();

        String jpql = "select new com.chronos.dto.ProdutoDTO(p.id,p.nome,p.valorVenda,ep.quantidadeEstoque,ep.controle,p.ncm,p.imagem,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,un.sigla,un.podeFracionar) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null)";


        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
        return produtos;


    }

    public List<EstoqueIdealDTO> getItensCompraSugerida(Empresa empresa) throws Exception {
        String jpql = "select new com.chronos.dto.EstoqueIdealDTO(p.id,p.nome,p.valorCompra,ep.quantidadeEstoque,ep.controle,p.estoqueIdeal) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "WHERE p.controle < p.estoqueMinimo and ep.empresa.id = ?1";

        List<EstoqueIdealDTO> produtos = getEntity(EstoqueIdealDTO.class, jpql, empresa.getId());
        return produtos;
    }


}
