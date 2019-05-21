package com.chronos.controll.vendas;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.AcaoLog;
import com.chronos.modelo.enuns.Modulo;
import com.chronos.modelo.enuns.SituacaoVenda;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.ItemVendaService;
import com.chronos.service.comercial.NfeService;
import com.chronos.service.comercial.VendaService;
import com.chronos.service.financeiro.FinLancamentoReceberService;
import com.chronos.service.gerencial.AuditoriaService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
    private Repository<NfeCabecalho> nfeRepository;
    @Inject
    private Repository<TributOperacaoFiscal> operacaoFiscalRepository;
    @Inject
    private NfeService nfeService;

    @Inject
    private FinLancamentoReceberService finLancamentoReceberService;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private VendaService vendaService;
    @Inject
    private ItemVendaService itemService;
    @Inject
    private ProdutoService produtoService;
    @Inject
    private AuditoriaService auditoriaService;
    @Inject
    private Repository<SituacaoForCli> situacaoForCliRepository;

    private ProdutoDTO produto;


    private VendaDetalhe vendaDetalhe;
    private VendaDetalhe vendaDetalheSelecionado;

    private PessoaCliente pessoaCliente;


    private Produto produto2;

    private String justificativa;

    private Date dataInicial, dataFinal;
    private String nome;
    private String situacao;
    private Map<String, String> status;

    private String tipoAutorizacao = "P";

    private TributOperacaoFiscal operacaoFiscal;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        status = new LinkedHashMap<>();
        status.put("Todos", "");
        status.put("Faturada", "F");
        status.put("Encerrada", "E");
        status.put("Cancelada", "C");

    }

    @Override
    public ERPLazyDataModel<VendaCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(VendaCabecalho.class);
        }

        dataModel.setAtributos(new Object[]{"dataVenda", "numeroFatura", "valorTotal", "situacao", "cliente.pessoa.nome"});
        dataModel.getFiltros().clear();
        pesquisar();
        return dataModel;
    }


    public void pesquisar() {

        if (dataInicial != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "dataVenda", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }
        if (dataFinal != null) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "dataVenda", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (!StringUtils.isEmpty(nome)) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "cliente.pessoa.nome", Filtro.LIKE, nome));
        }

        if (!StringUtils.isEmpty(situacao) && !situacao.equals("T")) {
            dataModel.getFiltros().add(new Filtro(Filtro.AND, "situacao", Filtro.IGUAL, situacao));
        }
        dataModel.getFiltros().add(new Filtro("empresa.id", empresa.getId()));

    }

    @Override
    public void doCreate() {
        super.doCreate();
        getObjeto().setEmpresa(empresa);
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

        try {

            VendaCabecalho venda = vendaService.salvar(getObjeto());
            setObjeto(venda);
            Mensagem.addInfoMessage("Pedido de venda salvo");

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro salvar o pedido venda", ex);
            }
        }
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


        try {
            vendaDetalhe.setProduto(new Produto(produto.getId(), produto.getNome()));

            itemService.verificarRestricao(vendaDetalhe);

            if (itemService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogVendaDetalhe').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                vendaService.addItem(getObjeto(), vendaDetalhe);
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao add o item da venda", ex);
            }
        }

    }

    public void excluirVendaDetalhe() {

        getObjeto().getListaVendaDetalhe().remove(vendaDetalheSelecionado);
        getObjeto().calcularValorTotal();


    }

    @Override
    public boolean autorizacaoSupervisor() {

        try {
            if (vendaService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                switch (tipoAutorizacao) {
                    case "P":
                        vendaService.addItem(getObjeto(), vendaDetalhe);
                        break;

                    default:
                        vendaService.salvar(getObjeto());
                }
            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao autoizar o procedimento", ex);
            }
        }
        return true;
    }

    public void buscarEncerrarVenda() {

        try {
            VendaCabecalho venda = dataModel.getRowData(getObjetoSelecionado().getId().toString());

            if (venda.getListaVendaDetalhe().isEmpty()) {
                throw new ChronosException("Não foram informado produtos para essa venda");
            }

            setObjeto(venda);
            encerrarVenda();
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao encerrar a venda", ex);
            }

        }


    }

    public void encerrarVenda() {
        try {
            vendaService.faturarVenda(getObjeto());
            Mensagem.addInfoMessage("Venda encerrada com sucesso");
            setTelaGrid(true);
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao encerrar a venda", ex);
            }


        }
    }


    public void faturarVenda() {
        boolean estoque = isTemAcesso("ESTOQUE");
        if (!getObjeto().getListaVendaDetalhe().isEmpty()) {
            vendaService.transmitirNFe(getObjeto(), ModeloDocumento.NFE, estoque);
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
            VendaCabecalho venda = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
            if (situacao == SituacaoVenda.Encerrado) {

                setObjeto(venda);
                getObjeto().setSituacao(SituacaoVenda.CANCELADA.getCodigo());
                finLancamentoReceberService.excluirFinanceiro(new DecimalFormat("VD0000000").format(getObjetoSelecionado().getId()), Modulo.VENDA);
                salvar();
                for (VendaDetalhe item : getObjeto().getListaVendaDetalhe()) {
                    estoqueRepositoy.atualizaEstoqueEmpresaControle(empresa.getId(), item.getProduto().getId(), item.getQuantidade());
                }
                auditoriaService.gerarLog(AcaoLog.CANCELAR_VENDA, "Venda cancelada", " VENDAS");
            } else if (situacao == SituacaoVenda.Faturado) {
                setObjeto(venda);
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

                    auditoriaService.gerarLog(AcaoLog.CANCELAR_VENDA, "Venda cancelada por " + justificativa, " VENDAS");

                }

            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
            } else {
                throw new RuntimeException("erro ao cancelar a venda", ex);
            }
        }
    }

    public void exibirDevolucao() {
        VendaCabecalho venda = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
        setObjeto(venda);

        getObjeto().getListaVendaDetalhe().forEach(i -> i.setQuantidadeDevolvida(i.getQuantidade()));
    }

    public void definirQuantidadeDevolvida() {

    }


    public void gerarDevolucao() {

        try {


            vendaService.gerarDevolucao(getObjeto(), operacaoFiscal);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Ocorreu um erro!", ex);
                FacesContext.getCurrentInstance().validationFailed();
            } else {
                throw new RuntimeException("erro ao gerar a devolucao", ex);
            }
        }

    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("descricao", Filtro.LIKE, descricao));
            listaTributOperacaoFiscal = operacaoFiscalRepository.getEntitys(TributOperacaoFiscal.class, filtros, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoInss", "estoque", "estoqueVerificado"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
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
        SituacaoForCli situacao = situacaoForCliRepository.get(pessoaCliente.getIdSituacaoForCli(), SituacaoForCli.class);
        Cliente cliente = new Cliente();
        cliente.setSituacaoForCli(situacao);
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

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Map<String, String> getStatus() {
        return status;
    }

    public TributOperacaoFiscal getOperacaoFiscal() {
        return operacaoFiscal;
    }

    public void setOperacaoFiscal(TributOperacaoFiscal operacaoFiscal) {
        this.operacaoFiscal = operacaoFiscal;
    }
}
