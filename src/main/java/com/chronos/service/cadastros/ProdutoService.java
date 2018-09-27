package com.chronos.service.cadastros;

import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by john on 14/12/17.
 */

public class ProdutoService implements Serializable {

    private static final long serialVersionUID = 8349487869365747547L;

    @Inject
    private EstoqueRepository repository;
    @Inject
    private Repository<Produto> produtoRepository;

    @Inject
    private Repository<EmpresaProduto> empresaProdutoRepository;


    @Transactional
    public Produto salvar(Produto produto, List<Empresa> empresas) throws ChronosException {

        if (produto.getValorVendaAtacado() != null && produto.getValorVendaAtacado().signum() > 0
                && (produto.getQuantidadeVendaAtacado() == null || produto.getQuantidadeVendaAtacado().signum() <= 0)) {
            throw new ChronosException("Para informar valor de venda no atacado  é preciso informar a quantidade para atacado");
        }

        if (produto.getTributGrupoTributario() == null) {
            Mensagem.addWarnMessage("É necesário informar o Grupo Tributário OU o ICMS Customizado.");
        } else {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "gtin", Filtro.IGUAL, produto.getGtin()));
            if (produto.getId() != null) {
                filtros.add(new Filtro(Filtro.AND, "id", Filtro.DIFERENTE, produto.getId()));
            }
            Produto p = StringUtils.isEmpty(produto.getGtin()) ? null : produtoRepository.get(Produto.class, filtros);
            if (p != null) {
                Mensagem.addWarnMessage("Este GTIN já está sendo utilizado por outro produto.");
            } else {
                if (StringUtils.isEmpty(produto.getDescricaoPdv())) {
                    String nomePdv = produto.getNome().length() > 30 ? produto.getNome().substring(0, 30) : produto.getNome();
                    produto.setDescricaoPdv(nomePdv);
                }
                if (!StringUtils.isEmpty(produto.getImagem())) {
                    ArquivoUtil.getInstance().salvarFotoProduto(produto.getImagem());
                }
                if (produto.getId() == null) {

                    produto = produtoRepository.atualizar(produto);
                    gerarEmpresaProduto(produto, empresas);

                } else {
                    produto.setDataAlteracao(new Date());
                    produto = produtoRepository.atualizar(produto);
                    //TODO verificar o fluxo de salva produt alterado.

                }


            }
        }

        return produto;
    }

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

    public Produto addConversaoUnidade(Produto produto, UnidadeProduto un, BigDecimal fator, String acao) throws ChronosException {

        if (produto.getUnidadeProduto() == null) {
            throw new ChronosException("Unidade do produto não definida");
        }

        if (fator == null || fator.signum() <= 0) {
            throw new ChronosException("Fator de conversão não definido");
        }

        if (produto.getConversoes() == null || produto.getConversoes().isEmpty()) {
            produto.setConversoes(new ArrayList<>());
        } else {
            if (produto.getConversoes().stream().filter(u -> u.getSigla().equals(un.getSigla())).findAny().isPresent()) {
                throw new ChronosException("unidade de conversão já adcionada");
            }
        }

        UnidadeConversao unidadeConversao = new UnidadeConversao();
        unidadeConversao.setProduto(produto);
        unidadeConversao.setUnidadeProduto(un);
        unidadeConversao.setSigla(un.getSigla());
        unidadeConversao.setAcao(acao);
        unidadeConversao.setDescricao("Converter " + produto.getUnidadeProduto().getSigla() + " para " + un.getSigla());
        unidadeConversao.setFatorConversao(fator);

        if (produto.getConversoes() == null) {
            produto.setConversoes(new ArrayList<>());
        }

        produto.getConversoes().add(unidadeConversao);

        return produto;
    }

    private void gerarEmpresaProduto(Produto produto, List<Empresa> empresas) {

        for (Empresa emp : empresas) {
            EmpresaProduto produtoEmpresa = new EmpresaProduto();
            produtoEmpresa.setEmpresa(emp);
            produtoEmpresa.setProduto(produto);

            empresaProdutoRepository.salvar(produtoEmpresa);

        }
    }
}
