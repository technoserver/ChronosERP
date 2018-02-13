package com.chronos.repository;

import com.chronos.dto.EstoqueIdealDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.dto.ProdutoVendaDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewProdutoEmpresa;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by john on 25/09/17.
 */
public class EstoqueRepository extends AbstractRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    public void atualizarPrecoProduto(int idproduto, BigDecimal valor) throws Exception {
        String jpql = "UPDATE Produto p SET p.valorVenda = ?1 where p.id =?2";
        execute(jpql, valor, idproduto);

    }

    public void ajustarEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = ?1 where p.produto.id = ?2 and p.empresa.id= ?3";

        execute(jpql, quantidade, idProduto, idEmpresa);
    }


    public void atualizaEstoqueVerificado(Integer idEmpresa, List<ProdutoVendaDTO> itens) throws Exception {
        for (ProdutoVendaDTO item : itens) {
            atualizaEstoqueEmpresaControle(idEmpresa, item.getId(), item.getQuantidade().negate());
        }
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresaControle(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresaControleFiscal(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) throws Exception {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresaControleFiscal(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }


    public void teste() {

    }

    public void teste(int id) {

    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado = p.estoqueVerificado + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }

    public void atualizaEstoqueEmpresaControleFiscal(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) throws Exception {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado = p.estoqueVerificado + :quantidade,p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }


    public List<NfeDetalhe> getItens(NfeCabecalho nfeCabecalho) throws Exception {
        String jpql = "SELECT NEW NfeDetalhe (o.id, o.produto, o.quantidadeComercial) FROM NfeDetalhe o WHERE o.nfeCabecalho.id = ?1";
        List<NfeDetalhe> itens = getEntity(NfeDetalhe.class, jpql, nfeCabecalho.getId());
        return itens;
    }

    public List<Produto> getProdutoEmpresa(String nome, Empresa empresa) throws Exception {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<ViewProdutoEmpresa> root = criteria.from(ViewProdutoEmpresa.class);


        String jpql = "select new com.chronos.dto.ProdutoDTO(p.id,p.nome,p.valorVenda,ep.quantidadeEstoque,ep.estoqueVerificado,p.ncm,p.imagem,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,un.sigla) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null)";


        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
        return null;


    }


    public List<ProdutoDTO> getProdutoDTO(String nome, Empresa empresa) throws Exception {

//        CriteriaBuilder builder = em.getCriteriaBuilder();
//        CriteriaQuery<ProdutoDTO> criteria = builder.createQuery(ProdutoDTO.class);
//        Root<Produto> root = criteria.from(Produto.class);
//        Join<Produto, EmpresaProduto> empresaProduto = root.join("produtosEmpresa", JoinType.INNER);
//        Join<Produto, UnidadeProduto> unidade = root.join("unidadeProduto", JoinType.INNER);
//        Join<Produto, TributGrupoTributario> grupo = root.join("tributGrupoTributario", JoinType.LEFT);
//        Join<Produto, TributGrupoTributario> icmsCustm = root.join("tributIcmsCustomCab", JoinType.LEFT);
//
//        criteria.select(builder.construct(ProdutoDTO.class
//                , root.get("id")
//                , root.get("nome")
//                , root.get("servico")
//                , root.get("codigoLst")
//                , root.get("valorVenda")
//                , empresaProduto.get("quantidadeEstoque")
//                , empresaProduto.get("estoqueVerificado")
//                , root.get("ncm")
//                , root.get("imagem")
//                , grupo.get("id")
//                , icmsCustm.get("id")
//                , unidade.get("sigla")
//                , unidade.get("podeFracionar")
//
//        ));
//        criteria.where(builder.equal(root.get("servico"),"N"),builder.equal(root.get("tipo"),"V"));
//
//
//        TypedQuery typedQuery = em.createQuery(criteria);
//        List<ProdutoDTO> produtos = typedQuery.getResultList();

        String jpql = "select new com.chronos.dto.ProdutoDTO(p.id,p.nome,p.descricaoPdv,p.servico,p.codigoLst,p.valorVenda,ep.quantidadeEstoque,ep.estoqueVerificado,p.ncm,p.imagem,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,un.sigla,un.podeFracionar) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null and p.tipo = 'V' )";


        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
        return produtos;
    }

    public List<EstoqueIdealDTO> getItensCompraSugerida(Empresa empresa) throws Exception {
        String jpql = "select new com.chronos.dto.EstoqueIdealDTO(p.id,p.nome,p.valorCompra,ep.quantidadeEstoque,ep.estoqueVerificado,p.estoqueIdeal) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "WHERE p.controle < p.estoqueMinimo and ep.empresa.id = ?1";

        List<EstoqueIdealDTO> produtos = getEntity(EstoqueIdealDTO.class, jpql, empresa.getId());
        return produtos;
    }

    private void executarQueryEstoque(Integer idEmpresa, Integer idProduto, BigDecimal quantidade, String jpql) {
        TypedQuery query = (TypedQuery) em.createQuery(jpql);
        query.setParameter("quantidade", quantidade);
        query.setParameter("idproduto", idProduto);
        query.setParameter("idempresa", idEmpresa);
        query.executeUpdate();
    }


}
