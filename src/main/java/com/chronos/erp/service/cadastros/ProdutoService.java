package com.chronos.erp.service.cadastros;

import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.ModeloBalanca;
import com.chronos.erp.modelo.enuns.PrecoPrioritario;
import com.chronos.erp.repository.EstoqueRepository;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 14/12/17.
 */

public class ProdutoService implements Serializable {

    private static final long serialVersionUID = 8349487869365747547L;


    private EstoqueRepository repository;
    private Repository<Produto> produtoRepository;
    private Repository<EmpresaProduto> empresaProdutoRepository;

    private Repository<EstoqueGrade> estoqueGradeRepository;

    @Inject
    public ProdutoService(EstoqueRepository repository, Repository<Produto> produtoRepository, Repository<EmpresaProduto> empresaProdutoRepository, Repository<EstoqueGrade> estoqueGradeRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.empresaProdutoRepository = empresaProdutoRepository;
        this.estoqueGradeRepository = estoqueGradeRepository;
    }

    @Transactional
    public Produto salvar(Produto produto, List<Empresa> empresas, List<EstoqueGrade> grades) throws ChronosException {

        if (produto.getValorVendaAtacado() != null && produto.getValorVendaAtacado().signum() > 0
                && (produto.getQuantidadeVendaAtacado() == null || produto.getQuantidadeVendaAtacado().signum() <= 0)) {
            throw new ChronosException("Para informar valor de venda no atacado  é preciso informar a quantidade para atacado");
        }

        if (StringUtils.isEmpty(produto.getTipo())) {
            throw new ChronosException("Tipo do produto não definido");
        }

        if (produto.getTipo().equals("V") && produto.getTributGrupoTributario() == null) {
            throw new ChronosException("É necesário informar o Grupo Tributário");
        } else {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "gtin", Filtro.IGUAL, produto.getGtin()));
            if (produto.getId() != null) {
                filtros.add(new Filtro(Filtro.AND, "id", Filtro.DIFERENTE, produto.getId()));
            }
            Produto p = StringUtils.isEmpty(produto.getGtin()) ? null : produtoRepository.get(Produto.class, filtros);
            if (p != null) {
                throw new ChronosException("Este GTIN já está sendo utilizado por outro produto.");
            } else {
                if (StringUtils.isEmpty(produto.getDescricaoPdv())) {
                    String nomePdv = produto.getNome().length() > 30 ? produto.getNome().substring(0, 30) : produto.getNome();
                    produto.setDescricaoPdv(nomePdv);
                }
                if (!StringUtils.isEmpty(produto.getImagem())) {
                    ArquivoUtil.getInstance().salvarFotoProduto(produto.getImagem());
                }

                if (produto.getProdutoGrade() != null) {
                    produto.setPossuiGrade(true);
                }

                if (produto.getId() == null) {
                    BigDecimal controle = produto.getControle();
                    produto = produtoRepository.atualizar(produto);

                    if (empresas == null) {
                        empresas = new ArrayList<>();
                        empresas.add(FacesUtil.getEmpresaUsuario());
                    }

                    if (empresas.isEmpty()) {
                        empresas.add(FacesUtil.getEmpresaUsuario());
                    }

                    produto.setControle(controle);
                    gerarEmpresaProduto(produto, empresas);


                } else {
                    produto.setDataAlteracao(new Date());
                    produto = produtoRepository.atualizar(produto);
                    //TODO verificar o fluxo de salva produt alterado.

                }

                if (grades != null && !grades.isEmpty()) {

                    for (EstoqueGrade g : grades) {

                        if (g.getId() == null) {
                            String codigo = produto.getId() + g.getEstoqueCor().getCodigo() + g.getEstoqueTamanho().getCodigo();
                            g.setCodigo(codigo);
                            g.setIdproduto(produto.getId());
                            estoqueGradeRepository.salvar(g);
                        }
                    }


                }


            }
        }

        return produto;
    }

    public List<Produto> getListProdutoVenda(String nome, Empresa empresa, boolean servico) {
        List<Produto> produtos = new ArrayList<>();
        List<ProdutoDTO> produtosDTO;
        try {
            String isServico = servico ? "S" : "N";
            produtosDTO = repository.getProdutoDTO(nome, empresa, isServico);
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

        descricao = descricao.trim();
        listaProduto = repository.getProdutoDTO(descricao, empresa, "S");


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

    public BigDecimal defnirPrecoVenda(ProdutoDTO produto) {

        BigDecimal valor = produto.getValorVenda();

        Map<PrecoPrioritario, BigDecimal> list = new HashMap<>();

        list.put(PrecoPrioritario.VALOR_VENDA, produto.getValorVenda());

        if (produto.getPrecoPromocao() != null && produto.getPrecoPromocao().signum() > 0) {
            list.put(PrecoPrioritario.PRODUTO_PROMOCIONAL, produto.getPrecoPromocao());
        }

        if (produto.getPrecoTabela() != null && produto.getPrecoTabela().signum() > 0) {
            list.put(PrecoPrioritario.TABELA_PRECO, produto.getPrecoTabela());
        }


        if (produto.getQuantidadeVenda() != null && produto.getQuantidadeVendaAtacado() != null
                && produto.getValorVendaAtacado() != null &&
                (produto.getQuantidadeVenda().compareTo(produto.getQuantidadeVendaAtacado()) >= 0)) {

            list.put(PrecoPrioritario.PRECO_ATACADO, produto.getValorVendaAtacado());

        }

        if (list.size() > 1 && produto.getPrecoPrioritario() == PrecoPrioritario.MENOR_PRECO) {


            valor = list
                    .entrySet()
                    .stream()
                    .min(Comparator.comparing(Map.Entry::getValue))
                    .map(Map.Entry::getValue).get();
        } else {
            BigDecimal precoPrioritario = list.get(produto.getPrecoPrioritario());
            valor = list.size() == 1 ? valor : precoPrioritario == null ? produto.getValorVenda() : precoPrioritario;
        }

        return valor;
    }

    public void gerarIntegracaoBalanca(PdvConfiguracaoBalanca configuracaoBalanca) throws Exception {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "codigoBalanca", Filtro.NAO_NULO, ""));


        List<Produto> produtos = repository.getProdutosBalanca();


        if (!produtos.isEmpty()) {

            if (configuracaoBalanca.getModelo() == ModeloBalanca.FILIZOLA) {
                gerarTxtFilizola(produtos, configuracaoBalanca);
            } else if (configuracaoBalanca.getModelo() == ModeloBalanca.TOLEDO) {
                gerarTxtToledo(produtos, configuracaoBalanca);
            } else {
                throw new ChronosException("No momento apenas foi feito a integração com a Filizola ou Toledo");
            }

        } else {
            Mensagem.addInfoMessage("Não foram encontrados produtos com codigo de balança.");
        }

    }

    private void gerarTxtFilizola(List<Produto> produtos, PdvConfiguracaoBalanca configuracaoBalanca) throws Exception {
        File file = File.createTempFile("CADTXT", ".txt");

        FileWriter writer = new FileWriter(file);

        int i = 0;
        for (Produto p : produtos) {
            p.setBalanca(configuracaoBalanca);
            String item = p.montarItemBalancaFilizola();
            if ((produtos.size() - 1) > i) {
                item += "\r\n";
            }
            writer.write(item);
            i++;
        }
        writer.close();


        FacesUtil.downloadArquivo(file, "CADTXT.txt", true);
    }


    private void gerarTxtToledo(List<Produto> produtos, PdvConfiguracaoBalanca configuracaoBalanca) throws Exception {

        List<String> arqvuivos = new ArrayList<>();

        File fileProduto = File.createTempFile("ITENSMGV", ".txt");
        File fileNutri = File.createTempFile("INFNUTRI", ".txt");
        FileWriter writer = new FileWriter(fileProduto);
        boolean exiteTabelaNutricional = false;
        int i = 0;
        for (Produto p : produtos) {
            p.setBalanca(configuracaoBalanca);
            String item = p.montarItemBalancaToledo();
            if ((produtos.size() - 1) > i) {
                item += "\r\n";
            }
            writer.write(item);
            i++;
            if (p.getTabelaNutricional() != null) {
                exiteTabelaNutricional = true;
            }
        }
        writer.close();

        if (!exiteTabelaNutricional) {
            FacesUtil.downloadArquivo(fileProduto, "ITENSMGV.txt", true);
        } else {


            //arqvuivos.add(fileProduto.getAbsolutePath());
            writer = new FileWriter(fileNutri);
            for (Produto p : produtos) {

                if (p.getTabelaNutricional() != null) {
                    String item = p.montarItemBalancaToledoNutricao();
                    if ((produtos.size() - 1) > i) {
                        item += "\r\n";
                    }
                    writer.write(item);
                    i++;
                }

            }

            writer.close();
            //  arqvuivos.add(fileNutri.getAbsolutePath());
            // File arquivoZip = ArquivoUtil.getInstance().compactarArquivos(arqvuivos, "toledo");
            //FacesUtil.downloadArquivo(arquivoZip, "toledo.zip", true);
            FacesUtil.downloadArquivo(fileProduto, "ITENSMGV.txt", true);
            FacesUtil.downloadArquivo(fileNutri, "INFNUTRI.txt", true);
        }


    }

    private void gerarEmpresaProduto(Produto produto, List<Empresa> empresas) {

        for (Empresa emp : empresas) {
            EmpresaProduto produtoEmpresa = new EmpresaProduto();
            produtoEmpresa.setEmpresa(emp);
            produtoEmpresa.setProduto(produto);
            produtoEmpresa.setEstoqueVerificado(produto.getControle());
            produtoEmpresa.setQuantidadeEstoque(BigDecimal.ZERO);
            empresaProdutoRepository.salvar(produtoEmpresa);

        }
    }

    @Transactional
    public void atualizarPrecoSugerido(Integer id, BigDecimal custoUnitario, BigDecimal encargosVenda, BigDecimal valorVenda) {
        custoUnitario = Biblioteca.soma(custoUnitario, encargosVenda);
        repository.atualizarPrecoSugerido(id, custoUnitario, valorVenda);
    }
}
