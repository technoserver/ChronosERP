package com.chronos.erp.repository;

import com.chronos.erp.dto.MapNomeIdDTO;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.view.ViewProdutoEmpresa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

public class ProdutoRepository extends AbstractRepository implements Serializable {

    public List<MapNomeIdDTO> getProdutoComposicao(String nome, Integer idempresa) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<ViewProdutoEmpresa> root = criteria.from(ViewProdutoEmpresa.class);


        String jpql = "select new com.chronos.erp.dto.MapNomeIdDTO(p.id,p.nome,'') From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and p.tipo='C' and p.inativo = 'N'";


        List<MapNomeIdDTO> produtos = getEntity(MapNomeIdDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", idempresa);
        return produtos;


    }
}
