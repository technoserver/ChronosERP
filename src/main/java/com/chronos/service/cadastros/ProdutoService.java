package com.chronos.service.cadastros;

import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.Empresa;
import com.chronos.modelo.entidades.EstoqueTransferenciaCabecalho;
import com.chronos.modelo.entidades.EstoqueTransferenciaDetalhe;
import com.chronos.modelo.entidades.Produto;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by john on 14/12/17.
 */

public class ProdutoService implements Serializable {

    private static final long serialVersionUID = 8349487869365747547L;

    @Inject
    private EstoqueRepository repository;


    public List<Produto> getListProdutoVenda(String nome, Empresa empresa) {
        List<Produto> produtos = new ArrayList<>();
        List<ProdutoDTO> produtosDTO = new ArrayList<>();
        try {
            produtosDTO = repository.getProdutoDTO(nome, empresa);
            produtos = produtosDTO.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;

    }

    public List<ProdutoDTO> getListaProdutoDTO(Empresa empresa, String descricao, boolean moduloVenda) {
        List<ProdutoDTO> listaProduto;
        List<Filtro> filtros = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isNumeric(descricao)) {
            filtros.add(new Filtro(Filtro.AND, "id", Filtro.IGUAL, descricao));
            // listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        } else {
            filtros.add(new Filtro(Filtro.OR, "nome", Filtro.LIKE, descricao.trim()));
            filtros.add(new Filtro(Filtro.OR, "gtin", Filtro.LIKE, descricao.trim()));
            filtros.add(new Filtro(Filtro.OR, "codigoInterno", Filtro.LIKE, descricao.trim()));
            //  listaProduto = produtoDao.getEntitys(Produto.class, filtros);
        }
        if (moduloVenda) {
            filtros.add(new Filtro(Filtro.AND, "servico", "N"));
            filtros.add(new Filtro(Filtro.AND, "tipo", "V"));
        }
        listaProduto = repository.getProdutoDTO(descricao, empresa);
        return listaProduto;
    }

    public List<Produto> getProdutosTransferencia(Integer idempresaOrigem, int idempresaDestino, String filtro) {
        List<Produto> produtos;
        List<ProdutoDTO> list;
        String str = "0" + filtro.replaceAll("\\D", "");
        int codigo = str.length() > 9 ? 0 : Integer.valueOf(str);
        if (codigo > 0) {
            list = repository.getProdutosTransferencia(idempresaOrigem, codigo);

        } else {
            filtro = filtro.trim().toLowerCase();
            filtro = filtro.length() == 13 || filtro.length() == 14 ? filtro : "%" + filtro + "%";
            list = repository.getProdutosTransferencia(idempresaOrigem, filtro);
        }
        produtos = list.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        return produtos;
    }

    @Transactional
    public void transferenciaEstoque(EstoqueTransferenciaCabecalho transferencia, List<EstoqueTransferenciaDetalhe> itens) {

        for (EstoqueTransferenciaDetalhe item : itens) {
            if (transferencia.getTributOperacaoFiscal().getEstoqueVerificado() && transferencia.getTributOperacaoFiscal().getEstoque()) {
                repository.atualizaEstoqueEmpresaControleFiscal(transferencia.getEmpresaOrigem().getId(), item.getProduto().getId(), item.getQuantidade().negate());
            } else if (transferencia.getTributOperacaoFiscal().getEstoque()) {
                repository.atualizaEstoqueEmpresa(transferencia.getEmpresaOrigem().getId(), item.getProduto().getId(), item.getQuantidade().negate());
            } else if (transferencia.getTributOperacaoFiscal().getEstoqueVerificado()) {
                repository.atualizaEstoqueEmpresaControle(transferencia.getEmpresaOrigem().getId(), item.getProduto().getId(), item.getQuantidade().negate());
            }
        }

    }
}
