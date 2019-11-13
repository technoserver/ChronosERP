package com.chronos.erp.repository;

import com.chronos.erp.dto.EstoqueIdealDTO;
import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.dto.ProdutoVendaDTO;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.NfeCabecalho;
import com.chronos.erp.modelo.entidades.NfeDetalhe;
import com.chronos.erp.modelo.entidades.Produto;
import com.chronos.erp.modelo.view.ViewProdutoEmpresa;
import org.apache.commons.lang.StringUtils;

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

    public void atualizarPrecoProduto(int idproduto, BigDecimal valor) {
        String jpql = "UPDATE Produto p SET p.valorVenda = ?1 where p.id =?2";
        execute(jpql, valor, idproduto);

    }

    public void ajustarEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = ?1 where p.produto.id = ?2 and p.empresa.id= ?3";

        execute(jpql, quantidade, idProduto, idEmpresa);
    }

    public void ajustarEstoqueAndVerificadoEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = ?1, p.estoqueVerificado = ?1 where p.produto.id = ?2 and p.empresa.id= ?3";

        execute(jpql, quantidade, idProduto, idEmpresa);
    }


    public void atualizaEstoqueVerificado(Integer idEmpresa, List<ProdutoVendaDTO> itens) {
        for (ProdutoVendaDTO item : itens) {
            atualizaEstoqueEmpresaControle(idEmpresa, item.getId(), item.getQuantidade().negate());
        }
    }

    public void atualizaEstoqueEmpresa(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresa(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresaControle(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizaEstoqueEmpresaControleFiscal(Integer idEmpresa, List<NfeDetalhe> listaNfeDetalhe) {
        for (NfeDetalhe nfeDetalhe : listaNfeDetalhe) {
            atualizaEstoqueEmpresaControleFiscal(idEmpresa, nfeDetalhe.getProduto().getId(), nfeDetalhe.getQuantidadeComercial().negate());
        }
    }

    public void atualizarGradeQuantidaAndVerificado(Integer idEmpresa, Integer idprduto, Integer idcor, Integer idtamanho, BigDecimal quantidade) {
        execute("UPDATE EstoqueGrade g set g.quantidade  = g.quantidade +  ?1, g.verificado = g.verificado + ?1 " +
                "where g.idempresa = ?2 and g.idproduto = ?3 and g.estoqueCor.id = ?4 and g.estoqueTamanho.id =?5", quantidade, idEmpresa, idprduto, idcor, idtamanho);
    }

    public void atualizarGradeQuantidade(Integer idEmpresa, Integer idprduto, Integer idcor, Integer idtamanho, BigDecimal quantidade) {
        execute("UPDATE EstoqueGrade g set g.quantidade  = g.quantidade +  ?1 " +
                "where g.idempresa = ?2 and g.idproduto = ?3 and g.estoqueCor.id = ?4 and g.estoqueTamanho.id =?5", quantidade, idEmpresa, idprduto, idcor, idtamanho);
    }

    public void atualizarGradeVerificado(Integer idEmpresa, Integer idprduto, Integer idcor, Integer idtamanho, BigDecimal quantidade) {
        execute("UPDATE EstoqueGrade g set g.verificado  = g.verificado +  ?1 " +
                "where g.idempresa = ?2 and g.idproduto = ?3 and g.estoqueCor.id = ?4 and g.estoqueTamanho.id =?5", quantidade, idEmpresa, idprduto, idcor, idtamanho);
    }


    public void atualizaEstoqueEmpresa(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }

    public void atualizaEstoqueEmpresaControle(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado = p.estoqueVerificado + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }

    public void atualizaEstoqueEmpresaControleFiscal(Integer idEmpresa, Integer idProduto, BigDecimal quantidade) {

        String jpql = "UPDATE EmpresaProduto p set p.estoqueVerificado = p.estoqueVerificado + :quantidade,p.quantidadeEstoque = p.quantidadeEstoque + :quantidade where p.produto.id = :idproduto and p.empresa.id= :idempresa";

        executarQueryEstoque(idEmpresa, idProduto, quantidade, jpql);

    }


    public List<NfeDetalhe> getItens(NfeCabecalho nfeCabecalho) {
        String jpql = "SELECT NEW NfeDetalhe (o.id, o.produto, o.quantidadeComercial) FROM NfeDetalhe o WHERE o.nfeCabecalho.id = ?1";
        List<NfeDetalhe> itens = getEntity(NfeDetalhe.class, jpql, nfeCabecalho.getId());
        return itens;
    }

    public List<Produto> getProdutoEmpresa(String nome, Empresa empresa) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Produto> criteria = builder.createQuery(Produto.class);
        Root<ViewProdutoEmpresa> root = criteria.from(ViewProdutoEmpresa.class);


        String jpql = "select new com.chronos.erp.dto.ProdutoDTO(p.id,p.nome,p.valorVenda,ep.quantidadeEstoque,ep.estoqueVerificado,p.ncm,p.imagem,p.tributGrupoTributario.id,p.tributIcmsCustomCab.id ,un.sigla) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where LOWER(p.nome)  like ?1 and ep.empresa.id = ?2 and (p.tributIcmsCustomCab is not null or p.tributGrupoTributario is not null)";


        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, "%" + nome.toLowerCase().trim() + "%", empresa.getId());
        return null;


    }


    public List<ProdutoDTO> getProdutoDTO(String nome, Empresa empresa, String servico) {

        String filtro = servico.equals("S") ? "" : "and p.servico = '" + servico + "'";
        int id = StringUtils.isEmpty(nome) && nome.length() <= 9 && org.apache.commons.lang3.StringUtils.isNumeric(nome) ? Integer.parseInt(nome) : 0;
        String jpql = "select DISTINCT new com.chronos.erp.dto.ProdutoDTO(p.id,p.produtoGrade.id,p.nome,p.descricaoPdv,p.servico,p.codigoLst,p.valorVenda," +
                "ep.quantidadeEstoque,ep.estoqueVerificado,p.ncm,p.imagem,p.tributGrupoTributario.id,un.sigla," +
                "un.podeFracionar,pp.valor,p.precoPrioritario,p.quantidadeVendaAtacado,p.valorVendaAtacado) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "LEFT JOIN ProdutoPromocao pp on pp.produto.id = p.id " +
                "where (LOWER(p.nome)  like ?1 or p.gtin = ?1 or p.codigoInterno = ?1 or p.id = ?3) and ep.empresa.id = ?2 and  " +
                "p.tributGrupoTributario is not null and p.tipo = 'V' and p.inativo = 'N' " +
                filtro +
                "order by p.nome";


        nome = !org.apache.commons.lang3.StringUtils.isNumeric(nome) ? "%" + nome.toLowerCase().trim() + "%" : nome;
        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, nome, empresa.getId(), id);
        return produtos;
    }


    public List<ProdutoDTO> getProdutosTransferencia(int idempresaOrigem, Object filtro) {
        String jpql = "select new com.chronos.erp.dto.ProdutoDTO(p.id,p.nome,p.custoUnitario,ep.quantidadeEstoque,ep.estoqueVerificado,p.ncm,un.sigla) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "INNER JOIN UnidadeProduto un ON p.unidadeProduto.id  = un.id " +
                "where ep.empresa.id = ?1 and p.tipo = 'V' and p.custoUnitario > 0 ";

        if (filtro instanceof Integer) {
            jpql += "and p.id = ?2";
        } else {
            jpql += "and (LOWER(p.nome)  like ?2 or p.gtin = ?2 or p.codigoInterno = ?2 )";
        }

        List<ProdutoDTO> produtos = getEntity(ProdutoDTO.class, jpql, idempresaOrigem, filtro);
        return produtos;
    }

    public List<EstoqueIdealDTO> getItensCompraSugerida(Empresa empresa) {
        String jpql = "select new com.chronos.erp.dto.EstoqueIdealDTO(p.id,p.nome,p.valorCompra,ep.quantidadeEstoque,ep.estoqueVerificado,p.estoqueIdeal) From Produto p " +
                "INNER JOIN EmpresaProduto ep ON ep.produto.id  = p.id " +
                "WHERE p.controle < p.estoqueMinimo and ep.empresa.id = ?1";

        List<EstoqueIdealDTO> produtos = getEntity(EstoqueIdealDTO.class, jpql, empresa.getId());
        return produtos;
    }

    public List<Produto> getProdutosBalanca() {
        String jpql = "select new Produto(p.id,p.nome,p.valorVenda,p.codigoBalanca,p.diasValidade,p.tabelaNutricional) from Produto p  left join p.tabelaNutricional where p.codigoBalanca is not null";
        List<Produto> produtos = getEntity(Produto.class, jpql);
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
