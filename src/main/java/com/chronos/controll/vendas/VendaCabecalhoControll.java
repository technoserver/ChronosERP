package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.FormaPagamento;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.SituacaoVenda;
import com.chronos.modelo.enuns.TipoFrete;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.comercial.VendaService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by john on 16/08/17.
 */
@Named
@ViewScoped
public class VendaCabecalhoControll extends AbstractControll<VendaCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<VendaOrcamentoCabecalho> orcamentos;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<Transportadora> transportadora;
    @Inject
    private Repository<NotaFiscalTipo> tipos;
    @Inject
    private Repository<PessoaCliente> clientes;
    @Inject
    private Repository<Vendedor> vendedores;
    @Inject
    private Repository<Produto> produtos;
    @Inject
    private Repository<ComissaoObjetivo> objetivos;
    @Inject
    private Repository<VendaComissao> comissoes;
    @Inject
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private NfeService nfeService;
    @Inject
    private AuditoriaService audService;
    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private VendaService vendaService;
    @Inject
    private ProdutoService produtoService;
    private ProdutoDTO produto;


    private VendaDetalhe vendaDetalhe;
    private VendaDetalhe vendaDetalheSelecionado;

    private PessoaCliente pessoaCliente;



    private Produto produto2;

    private String justificativa;


    @Override
    public ERPLazyDataModel<VendaCabecalho> getDataModel() {
        if(dataModel == null){
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(VendaCabecalho.class);
        }

        dataModel.setAtributos(new Object[]{"dataVenda","numeroFatura","valorTotal","situacao","cliente.pessoa.nome"});
        dataModel.getFiltros().clear();
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));
        return dataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
        getObjeto().setListaVendaDetalhe(new ArrayList<>());
        getObjeto().setDataVenda(new Date());
        getObjeto().setSituacao(SituacaoVenda.Digitacao.getCodigo());
        getObjeto().setTipoFrete(TipoFrete.CIF.getCodigo());
        pessoaCliente = null;

    }

    @Override
    public void doEdit() {
        super.doEdit();
        VendaCabecalho venda = dataModel.getRowData(getObjeto().getId().toString());
        setObjeto(venda);
        pessoaCliente = new PessoaCliente();
        pessoaCliente.setNome(getObjeto().getCliente().getPessoa().getNome());
    }

    @Override
    public void salvar() {
        if (getObjeto().getCondicoesPagamento() != null) {
            getObjeto().setFormaPagamento(getObjeto().getCondicoesPagamento().getVistaPrazo().equals("V")
                    ? FormaPagamento.AVISTA.getCodigo() : FormaPagamento.APRAZO.getCodigo());
        }
        super.salvar();
        setTelaGrid(false);
    }

    public void incluirVendaDetalhe() {
        vendaDetalhe = new VendaDetalhe();
        vendaDetalhe.setVendaCabecalho(getObjeto());
        vendaDetalhe.setQuantidade(BigDecimal.ONE);
        produto = new ProdutoDTO();
    }

    public void definirValorProduto(SelectEvent event) {
        produto = (ProdutoDTO) event.getObject();
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        vendaDetalhe.setValorUnitario(precoVenda);
    }

    public void definirValorAtacado() {
        produto.setQuantidadeVenda(vendaDetalhe.getQuantidade());
        BigDecimal precoVenda = produtoService.defnirPrecoVenda(produto);
        vendaDetalhe.setValorUnitario(precoVenda);
    }

    public void alterarVendaDetalhe() {
        vendaDetalhe = vendaDetalheSelecionado;
    }

    public void salvarVendaDetalhe() {
        vendaDetalhe.setProduto(new Produto(produto.getId(), produto.getNome()));
        if (vendaDetalhe.getId() == null) {
            getObjeto().getListaVendaDetalhe().stream()
                    .filter(p -> p.getProduto().getId() == vendaDetalhe.getProduto().getId())
                    .forEach(item -> {
                        item.setQuantidade(vendaDetalhe.getQuantidade());
                        item.setValorUnitario(vendaDetalhe.getValorUnitario());
                        item.setTaxaDesconto(vendaDetalhe.getTaxaDesconto());
                    });
            boolean encontrou = getObjeto().getListaVendaDetalhe().stream()
                    .filter(p -> p.getProduto().getId() == vendaDetalhe.getProduto().getId())
                    .findFirst().isPresent();
            if (!encontrou) {
                vendaDetalhe.calcularValorTotal();
                getObjeto().getListaVendaDetalhe().add(vendaDetalhe);
            }


        }
        try {
            getObjeto().calcularValorTotal();
            salvar("Registro salvo com sucesso!");
            setTelaGrid(false);
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public void excluirVendaDetalhe() {
        try {
            getObjeto().getListaVendaDetalhe().remove(vendaDetalheSelecionado);
            getObjeto().calcularValorTotal();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);
        }

    }


    public void encerrarVenda() {
        try {

            vendaService.faturarVenda(getObjeto());
            Mensagem.addInfoMessage("Venda faturada com sucesso");
            setTelaGrid(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", ex);
        }
    }


    public void gerarNFe() {
        ModeloDocumento modelo = ModeloDocumento.NFE;
        VendaCabecalho venda = dao.getJoinFetch(getObjetoSelecionado().getId(), VendaCabecalho.class);
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!venda.getListaVendaDetalhe().isEmpty()) {
            vendaService.transmitirNFe(venda, modelo, estoque);
        }

    }


    public void gerarNfce() {
        ModeloDocumento modelo = ModeloDocumento.NFCE;
        VendaCabecalho venda = dao.getJoinFetch(getObjetoSelecionado().getId(), VendaCabecalho.class);
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!venda.getListaVendaDetalhe().isEmpty()) {
            vendaService.transmitirNFe(venda, modelo, estoque);
        }
    }

    public void danfe() {
        try {
            int idnfe = getObjetoSelecionado().getNumeroFatura();
            NfeCabecalho nfe = nfeRepository.get(idnfe, NfeCabecalho.class);


            nfeService.danfe(nfe);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    @Transactional
    public void cancelarVenda() {


        try {
            SituacaoVenda situacao = SituacaoVenda.valueOfCodigo(getObjetoSelecionado().getSituacao());
            if (situacao == SituacaoVenda.Faturado) {

                setObjeto(getObjetoSelecionado());
                getObjeto().setSituacao(SituacaoVenda.CANCELADA.getCodigo());
                finLancamentoReceberService.excluirFinanceiro(new DecimalFormat("VD0000000").format(getObjetoSelecionado().getId()), Modulo.VENDA);
                salvar();
                for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                }
            } else if (situacao == SituacaoVenda.Faturado) {
                setObjeto(getObjetoSelecionado());
                NfeCabecalho nfe = nfeRepository.get(getObjeto().getNumeroFatura(), NfeCabecalho.class);
                nfe.setJustificativaCancelamento(justificativa);

                boolean estoque = isTemAcesso("ESTOQUE");
                boolean cancelada = nfeService.cancelarNFe(nfe, estoque);
                if (cancelada) {
                    finLancamentoReceberService.excluirFinanceiro(new DecimalFormat("VD0000000").format(getObjetoSelecionado().getId()), Modulo.VENDA);
                    getObjeto().setSituacao(SituacaoVenda.CANCELADA.getCodigo());
                    Map<String, Object> atributos = new HashMap<>();
                    atributos.put("situacao", SituacaoVenda.CANCELADA.getCodigo());
                    List<Filtro> filtros = new LinkedList<>();
                    filtros.add(new Filtro("id", getObjeto().getId()));
                    dao.updateNativo(VendaCabecalho.class, filtros, atributos);
                    if (estoque) {
                        for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                            estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                        }
                    }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    public void carregaItensOrcamento(SelectEvent event) {
        try {
            VendaOrcamentoCabecalho orcamento = (VendaOrcamentoCabecalho) event.getObject();
            VendaDetalhe itemVenda;
            getObjeto().setListaVendaDetalhe(new ArrayList<>());
            getObjeto().setVendaOrcamentoCabecalho(orcamentos.getEntityJoinFetch(orcamento.getId(), VendaOrcamentoCabecalho.class));
            for (VendaOrcamentoDetalhe d : getObjeto().getVendaOrcamentoCabecalho().getListaVendaOrcamentoDetalhe()) {
                itemVenda = new VendaDetalhe();
                itemVenda.setVendaCabecalho(getObjeto());
                itemVenda.setProduto(d.getProduto());
                itemVenda.setQuantidade(d.getQuantidade());
                itemVenda.setTaxaDesconto(d.getTaxaDesconto());
                itemVenda.setValorDesconto(d.getValorDesconto());
                itemVenda.setValorSubtotal(d.getValorSubtotal());
                itemVenda.setValorTotal(d.getValorTotal());
                itemVenda.setValorUnitario(d.getValorUnitario());

                getObjeto().getListaVendaDetalhe().add(itemVenda);
            }

            getObjeto().setCliente(orcamento.getCliente());
            getObjeto().setCondicoesPagamento(orcamento.getCondicoesPagamento());
            getObjeto().setTransportadora(orcamento.getTransportadora());
            getObjeto().setVendedor(orcamento.getVendedor());
            getObjeto().setTipoFrete(orcamento.getTipoFrete());
            getObjeto().setValorSubtotal(orcamento.getValorSubtotal());
            getObjeto().setValorFrete(orcamento.getValorFrete());
            getObjeto().setTaxaComissao(orcamento.getTaxaComissao());
            getObjeto().setValorComissao(orcamento.getValorComissao());
            getObjeto().setTaxaDesconto(orcamento.getValorDesconto());
            getObjeto().setValorTotal(orcamento.getValorTotal());
            getObjeto().setObservacao(orcamento.getObservacao());

            getObjeto().calcularValorTotal();
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro!", e);

        }
    }

    public List<VendaOrcamentoCabecalho> getListaVendaOrcamentoCabecalho(String nome) {
        List<VendaOrcamentoCabecalho> listaVendaOrcamentoCabecalho = new ArrayList<>();
        try {
            listaVendaOrcamentoCabecalho = orcamentos.getEntitys(VendaOrcamentoCabecalho.class, "codigo", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaOrcamentoCabecalho;
    }

    public List<VendaCondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            Object[] join = new Object[]{""};
            listaVendaCondicoesPagamento = condicoes.getEntitys(VendaCondicoesPagamento.class, "nome", nome, new Object[]{"nome", "vistaPrazo"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaCondicoesPagamento;
    }

    public List<Transportadora> getListaTransportadora(String nome) {
        List<Transportadora> listaTransportadora = new ArrayList<>();
        try {
            listaTransportadora = transportadora.getEntitys(Transportadora.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTransportadora;
    }

    public List<NotaFiscalTipo> getListaTipoNotaFiscal(String nome) {
        List<NotaFiscalTipo> listaTipoNotaFiscal = new ArrayList<>();
        try {
            listaTipoNotaFiscal = tipos.getEntitys(NotaFiscalTipo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoNotaFiscal;
    }

    public List<PessoaCliente> getListaCliente(String nome) {
        List<PessoaCliente> listaCliente = new ArrayList<>();
        try {
            listaCliente = clientes.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCliente;
    }

    public List<Vendedor> getListaVendedor(String nome) {
        List<Vendedor> listaVendedor = new ArrayList<>();
        try {
            listaVendedor = vendedores.getEntitys(Vendedor.class, "colaborador.pessoa.nome", nome, new Object[]{"colaborador.pessoa.nome", "comissao"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVendedor;
    }

    public List<ProdutoDTO> getListaProduto(String nome) {
        List<ProdutoDTO> listaProduto = new ArrayList<>();

        try {

            listaProduto = produtoService.getListaProdutoDTO(empresa, nome, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;

    }


    public void definirEnderecoEntrega(SelectEvent event) {
        PessoaCliente pessoaCliente = (PessoaCliente) event.getObject();
        Cliente cliente = new Cliente();
        cliente.setId(pessoaCliente.getId());
        getObjeto().setCliente(cliente);
        String endereco = "End: " + pessoaCliente.getLogradouro() + "," + pessoaCliente.getNumero() + " ";
        endereco += "Bairro :" + pessoaCliente.getBairro() + " Cep: " + pessoaCliente.getCep() + " - ";
        endereco += pessoaCliente.getCidade() + "/" + pessoaCliente.getUf();
        getObjeto().setLocalEntrega(endereco);
        getObjeto().setLocalCobranca(endereco);
    }


    public void definirTaxaComissao(SelectEvent event) {
        Vendedor vendedor = (Vendedor) event.getObject();
        getObjeto().setTaxaComissao(vendedor.getComissao());
    }



    @Override
    protected Class<VendaCabecalho> getClazz() {
        return VendaCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "VENDA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public VendaDetalhe getVendaDetalhe() {
        return vendaDetalhe;
    }

    public void setVendaDetalhe(VendaDetalhe vendaDetalhe) {
        this.vendaDetalhe = vendaDetalhe;
    }

    public VendaDetalhe getVendaDetalheSelecionado() {
        return vendaDetalheSelecionado;
    }

    public void setVendaDetalheSelecionado(VendaDetalhe vendaDetalheSelecionado) {
        this.vendaDetalheSelecionado = vendaDetalheSelecionado;
    }

    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
    }

    public Produto getProduto2() {
        return produto2;
    }

    public void setProduto2(Produto produto2) {
        this.produto2 = produto2;
    }
}
