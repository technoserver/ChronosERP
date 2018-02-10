package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ConfEmail;
import com.chronos.dto.ConfiguracaoEmissorDTO;
import com.chronos.dto.ProdutoDTO;
import com.chronos.exception.EmissorException;
import com.chronos.infra.enuns.LocalDestino;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.StatusTransmissao;
import com.chronos.modelo.entidades.view.PessoaCliente;
import com.chronos.repository.EstoqueRepository;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.jsf.Mensagem;
import com.chronos.util.mail.EnvioEmail;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by john on 26/09/17.
 */
@Named
@ViewScoped
public class NfeCabecalhoControll extends AbstractControll<NfeCabecalho> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<TributOperacaoFiscal> operacoes;
    @Inject
    private Repository<PessoaCliente> pessoas;
    @Inject
    private Repository<VendaCondicoesPagamento> condicoes;
    @Inject
    private Repository<NfeConfiguracao> configuracoes;
    @Inject
    private EstoqueRepository estoqueRepositoy;
    @Inject
    private Repository<PdvTipoPagamento> tipoPagamentoRepository;


    @Inject
    private NfeService nfeService;

    private PessoaCliente pessoaCliente;
    private NfeDetalhe nfeDetalhe;
    private NfeDetalhe nfeDetalheSelecionado;
    private NfeReferenciada nfeReferenciada;
    private NfeReferenciada nfeReferenciadaSelecionado;
    private PdvTipoPagamento tipoPagamento;
    private VendaCondicoesPagamento condicoesPagamento;
    private NfeConfiguracao configuracao;
    private int qtdParcelas;
    private int intervaloParcelas;
    private Date primeiroVencimento;
    private String observacao;
    private boolean podeIncluirProduto;
    private boolean duplicidade;
    private boolean dadosSalvos;
    private String justificativa;
    private int numeroNfe;
    private Date dataInicial;
    private Date dataFinal;

    @PostConstruct
    @Override
    public void init() {
        super.init();

    }

    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(NfeCabecalho.class);
        }
        dataModel.setAtributos(new Object[]{"cliente.pessoa.nome", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "55", Filtro.IGUAL);
        return dataModel;
    }
    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud NFe">

    @Override
    public void doCreate() {
        try {
            super.doCreate();
            getObjeto().setDestinatario(new NfeDestinatario());
            getObjeto().getDestinatario().setNfeCabecalho(getObjeto());
            configuracao = configuraNfe();
            nfeService.dadosPadroes(getObjeto(), ModeloDocumento.NFE, empresa, new ConfiguracaoEmissorDTO(configuracao));
            dadosSalvos = false;
            observacao = getObjeto().getInformacoesAddContribuinte();
            this.setActiveTabIndex(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setTelaGrid(true);
            Mensagem.addFatalMessage("Erro ao criar a NFe. ", ex);
        }

    }

    @Override
    public void doEdit() {
        try {
            super.doEdit();
            NfeCabecalho nfe = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
            tipoPagamento = nfe.getListaNfeFormaPagamento().stream().findFirst().orElse(new NfeFormaPagamento()).getPdvTipoPagamento();
            setObjeto(nfe);
            dadosSalvos = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void salvar() {


        try {
            nfeService.validacaoNfe(getObjeto());
            if (getObjeto().getId() == null) {
                NfeFormaPagamento nfeFormaPagamento = new NfeFormaPagamento();
                nfeFormaPagamento.setPdvTipoPagamento(tipoPagamento);
                nfeFormaPagamento.setNfeCabecalho(getObjeto());
                nfeFormaPagamento.setForma(tipoPagamento.getCodigo());
                nfeFormaPagamento.setValor(getObjeto().getValorTotal());
                getObjeto().getListaNfeFormaPagamento().add(nfeFormaPagamento);
            }
            String str = getObjeto().getInformacoesAddContribuinte() + " " + observacao;
            getObjeto().setInformacoesAddContribuinte(str);
            super.salvar();
            setTelaGrid(false);
            dadosSalvos = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Produto">

    public void inlcuirProduto() {
        if (getObjeto().getTributOperacaoFiscal() == null) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("Antes de incluir produtos selecione a Operação Fiscal.");

        } else if (getObjeto().getDestinatario().getNome() == null || getObjeto().getDestinatario().getNome().isEmpty()) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("Antes de incluir produtos selecione o destinatário.");

        } else {
            podeIncluirProduto = true;
            nfeDetalhe = new NfeDetalhe();
            nfeDetalhe.setNfeCabecalho(getObjeto());
            nfeDetalhe.setQuantidadeComercial(BigDecimal.ONE);
            if (getObjeto().getFinalidadeEmissao() > 1) {
                instanciaImpostos();
            }
        }
    }

    public void salvaProduto() {
        try {
            realizaCalculosItem();
            Optional<NfeDetalhe> itemNfeOptional = buscarItemPorProduto(nfeDetalhe.getProduto());
            NfeDetalhe item = null;
            if (itemNfeOptional.isPresent()) {
                item = itemNfeOptional.get();
                item = nfeDetalhe;
            } else {
                getObjeto().getListaNfeDetalhe().add(0, nfeDetalhe);
            }
            setObjeto(nfeService.atualizarTotais(getObjeto()));
            Mensagem.addInfoMessage("Registro incluído!");
            dadosSalvos = false;
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto\n", e);
        }

    }

    private Optional<NfeDetalhe> buscarItemPorProduto(Produto produto) {
        return getObjeto().getListaNfeDetalhe().stream()
                .filter(i -> i.getProduto().equals(produto))
                .findAny();
    }

    public void excluirItem(Produto produto) {
        int indice = IntStream.range(0, getObjeto().getListaNfeDetalhe().size())
                .filter(i -> getObjeto().getListaNfeDetalhe().get(i).getProduto().equals(produto))
                .findAny().getAsInt();
        getObjeto().getListaNfeDetalhe().remove(indice);
    }


    public void excluirProduto() {
        try {
            if (nfeDetalheSelecionado == null) {
                Mensagem.addInfoMessage("Nenhum registro selecionado!");
            } else {
                getObjeto().getListaNfeDetalhe().remove(nfeDetalheSelecionado);
                setObjeto(nfeService.atualizarTotais(getObjeto()));
                dadosSalvos = false;
                Mensagem.addInfoMessage("Registro excluido!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    public void detalheImposto() {

    }

    /**
     * setar os valores do item que está sendo salvo
     *
     * @return
     * @throws Exception
     */
    private NfeDetalhe realizaCalculosItem() throws Exception {

        if (nfeDetalhe.getProduto().getNcm() == null) {
            throw new Exception("Não existe NCM para o produto informado. Operação não realizada.");
        }

        BigDecimal valorVenda = nfeDetalhe.getValorUnitarioComercial();
        valorVenda = valorVenda == null ? nfeDetalhe.getProduto().getValorVenda() : valorVenda;
        nfeDetalhe.setValorUnitarioComercial(valorVenda);
        nfeDetalhe.setEntraTotal(1);
        nfeDetalhe.pegarInfoProduto();
        nfeDetalhe.calcularValorTotalProduto();
        nfeDetalhe = nfeService.definirTributacao(nfeDetalhe, getObjeto().getTributOperacaoFiscal(), getObjeto().getDestinatario());

        return nfeDetalhe;
    }

    private void instanciaImpostos() {
        nfeDetalhe.setNfeDetalheImpostoIssqn(new NfeDetalheImpostoIssqn());
        nfeDetalhe.getNfeDetalheImpostoIssqn().setNfeDetalhe(nfeDetalhe);
        nfeDetalhe.setNfeDetalheImpostoPis(new NfeDetalheImpostoPis());
        nfeDetalhe.getNfeDetalheImpostoPis().setNfeDetalhe(nfeDetalhe);
        nfeDetalhe.setNfeDetalheImpostoCofins(new NfeDetalheImpostoCofins());
        nfeDetalhe.getNfeDetalheImpostoCofins().setNfeDetalhe(nfeDetalhe);
        nfeDetalhe.setNfeDetalheImpostoIcms(new NfeDetalheImpostoIcms());
        nfeDetalhe.getNfeDetalheImpostoIcms().setNfeDetalhe(nfeDetalhe);
        nfeDetalhe.setNfeDetalheImpostoIpi(new NfeDetalheImpostoIpi());
        nfeDetalhe.getNfeDetalheImpostoIpi().setNfeDetalhe(nfeDetalhe);
        nfeDetalhe.setNfeDetalheImpostoIi(new NfeDetalheImpostoIi());
        nfeDetalhe.getNfeDetalheImpostoIi().setNfeDetalhe(nfeDetalhe);
//
//        nfeDetalhe.setListaArmamento(new HashSet<>());
//        nfeDetalhe.setListaMedicamento(new HashSet<>());
//        nfeDetalhe.setListaDeclaracaoImportacao(new HashSet<>());
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud NFe referenciadas">


    public void incluiNfeReferenciada() {
        dadosSalvos = false;
        nfeReferenciada = new NfeReferenciada();
        nfeReferenciada.setNfeCabecalho(getObjeto());
    }

    public void excluirNfeReferenciada() {
        getObjeto().getListaNfeReferenciada().remove(nfeReferenciadaSelecionado);
        dadosSalvos = false;
        Mensagem.addInfoMessage("Registro excluido!");
    }

    public void salvarNferefenciada() {
        try {
            if (getObjeto().getListaNfeReferenciada() == null) {
                getObjeto().setListaNfeReferenciada(new HashSet<>());
            }
            if (!getObjeto().getListaNfeReferenciada().contains(nfeReferenciada)) {
                getObjeto().getListaNfeReferenciada().add(nfeReferenciada);

                Mensagem.addInfoMessage("Registro incluído!");
            }
            dadosSalvos = false;
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addFatalMessage("Ocorreu um erro ao salvar a Nfe referenciada \n", e);
        }
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Duplicatas">

    public void gerarDulicatas() {

        try {
            condicoesPagamento = condicoes.getJoinFetch(condicoesPagamento.getId(), VendaCondicoesPagamento.class);
            nfeService.gerarDuplicatas(getObjeto(), condicoesPagamento, primeiroVencimento, intervaloParcelas, qtdParcelas);
            Mensagem.addInfoMessage("Duplicatas geradas com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }


    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Transportadora">


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Cliente">


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos NFe">
    public NfeConfiguracao configuraNfe() throws Exception {
        List<Filtro> filtros = new LinkedList<>();
        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
        configuracao = configuracoes.get(NfeConfiguracao.class, filtros);
        return configuracao;
    }

    public void danfe() {


        try {
            configuracao = configuraNfe();
            nfeService.danfe(getObjeto(), new ConfiguracaoEmissorDTO(configuracao));

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }


    public void transmitirNfe() {
        try {
            if (dadosSalvos) {

                configuracao = configuracao != null ? configuracao : configuraNfe();
                boolean estoque = isTemAcesso("ESTOQUE");
                StatusTransmissao status = nfeService.transmitirNFe(getObjeto(), new ConfiguracaoEmissorDTO(configuracao), estoque);
                if (status == StatusTransmissao.AUTORIZADA) {

                    Mensagem.addInfoMessage("NFe transmitida com sucesso");
                } else {
                    duplicidade = status == StatusTransmissao.DUPLICIDADE;
                }

            } else {
                Mensagem.addInfoMessage("Antes de enviar a NF-e é necessário salvar as informações!");
            }
        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out")) {
                try {
                    getObjeto().setStatusNota(StatusTransmissao.ENVIADA.getCodigo());
                    dao.atualizar(getObjeto());
                } catch (Exception ex1) {

                }
            }
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao transmitir\n", ex);
        }
    }
    public void cancelaNfe() {

        try {
            getObjeto().setJustificativaCancelamento(justificativa);
            configuracao = configuracao != null ? configuracao : configuraNfe();
            boolean estoque = isTemAcesso("ESTOQUE");
            boolean cancelado = nfeService.cancelarNFe(getObjeto(), new ConfiguracaoEmissorDTO(configuracao), estoque);
            if (cancelado) {
                Mensagem.addInfoMessage("NFe cancelada com sucesso");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao cancelar a NF-e!\n", e);
        }

    }

    public void cartaCorrecao() {
        try {
            configuracao = configuracao != null ? configuracao : configuraNfe();
            nfeService.cartaCorrecao(getObjeto(), justificativa, new ConfiguracaoEmissorDTO(configuracao));
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao enviar a carta de correção!", e);
        }
    }

    public void enviarEmail() {

        try {
            configuracao = configuracao != null ? configuracao : configuraNfe();
            ConfEmail confEmail = new ConfEmail();
            confEmail.setHost(configuracao.getEmailServidorSmtp());
            confEmail.setPorta(configuracao.getEmailPorta());
            confEmail.setSsl(configuracao.getEmailAutenticaSsl().equals("S"));
            confEmail.setUsuario(configuracao.getEmailUsuario());
            confEmail.setSenha(configuracao.getEmailSenha());
            confEmail.setTsl(!configuracao.getEmailAutenticaSsl().equals("S"));
            EnvioEmail envio = new EnvioEmail();
            envio.enviarNfeEmail(confEmail, getObjeto());
            System.out.print("teste");
            Mensagem.addInfoMessage("Email enviado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao enviar email!", ex);
        }
    }

    public void limparJustificativa() {
        justificativa = "";
    }

    public void visualizarXml() {
        try {
            if (dadosSalvos) {
                configuracao = configuracao != null ? configuracao : configuraNfe();
                String caminho = nfeService.gerarNfePreProcessada(getObjeto(), new ConfiguracaoEmissorDTO(configuracao));

                nfeService.visualizarXml(caminho);
            } else {
                Mensagem.addInfoMessage("É preciso salvar as informações");
                return;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Diversos">



    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pesquisas">

    public List<VendaCondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            listaVendaCondicoesPagamento = condicoes.getEntitys(VendaCondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaVendaCondicoesPagamento;
    }

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao", "cfop", "obrigacaoFiscal", "destacaIpi", "destacaPisCofins", "calculoIssqn"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributOperacaoFiscal;
    }

    public List<PessoaCliente> getListaPessoaCliente(String nome) {
        List<PessoaCliente> listaPessoaCliente = new ArrayList<>();
        try {
            listaPessoaCliente = pessoas.getEntitys(PessoaCliente.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPessoaCliente;
    }

    public void selecionaDestinatario(SelectEvent event) {
        pessoaCliente = (PessoaCliente) event.getObject();
        Cliente cliente = new Cliente();
        cliente.setId(pessoaCliente.getId());
        getObjeto().setCliente(cliente);

        getObjeto().getDestinatario().setCpfCnpj(pessoaCliente.getCpfCnpj());
        getObjeto().getDestinatario().setNome(pessoaCliente.getNome());
        getObjeto().getDestinatario().setLogradouro(pessoaCliente.getLogradouro());
        getObjeto().getDestinatario().setComplemento(pessoaCliente.getComplemento());
        getObjeto().getDestinatario().setNumero(pessoaCliente.getNumero());
        getObjeto().getDestinatario().setBairro(pessoaCliente.getBairro());
        getObjeto().getDestinatario().setNomeMunicipio(pessoaCliente.getCidade());
        getObjeto().getDestinatario().setCodigoMunicipio(pessoaCliente.getMunicipioIbge());
        getObjeto().getDestinatario().setUf(pessoaCliente.getUf());
        getObjeto().getDestinatario().setCep(pessoaCliente.getCep());
        getObjeto().getDestinatario().setTelefone(pessoaCliente.getFone());
        getObjeto().getDestinatario().setInscricaoEstadual(pessoaCliente.getRgIe());
        getObjeto().getDestinatario().setEmail(pessoaCliente.getEmail());
        getObjeto().getDestinatario().setCodigoPais(1058);
        getObjeto().getDestinatario().setNomePais("Brazil");


        getObjeto().setLocalDestino(LocalDestino.getByUf(empresa.buscarEnderecoPrincipal().getUf(), pessoaCliente.getUf()));


        dadosSalvos = false;

    }

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto = new ArrayList<>();

        try {
            List<ProdutoDTO> list = nfeService.getListaProdutoDTO(descricao,false);
            listaProduto = list.stream().map(ProdutoDTO::getProduto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public void selecionaValorProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        nfeDetalhe.setValorUnitarioComercial(Optional.ofNullable(produto.getValorVenda()).orElse(BigDecimal.ZERO));
    }

    public List<PdvTipoPagamento> getListaNfceTipoPagamento(String nome) {
        List<PdvTipoPagamento> listaPdvTipoPagamento = new ArrayList<>();
        try {
            listaPdvTipoPagamento = tipoPagamentoRepository.getEntitys(PdvTipoPagamento.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPdvTipoPagamento;
    }

    // </editor-fold>

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    // <editor-fold defaultstate="collapsed" desc="GETS SETS">


    public VendaCondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(VendaCondicoesPagamento condicoesPagamento) {
        this.condicoesPagamento = condicoesPagamento;
    }

    public PessoaCliente getPessoaCliente() {
        return pessoaCliente;
    }

    public void setPessoaCliente(PessoaCliente pessoaCliente) {
        this.pessoaCliente = pessoaCliente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public boolean isDuplicidade() {
        return duplicidade;
    }

    public void setDuplicidade(boolean duplicidade) {
        this.duplicidade = duplicidade;
    }

    public NfeDetalhe getNfeDetalhe() {
        return nfeDetalhe;
    }

    public void setNfeDetalhe(NfeDetalhe nfeDetalhe) {
        this.nfeDetalhe = nfeDetalhe;
    }

    public NfeDetalhe getNfeDetalheSelecionado() {
        return nfeDetalheSelecionado;
    }

    public void setNfeDetalheSelecionado(NfeDetalhe nfeDetalheSelecionado) {
        this.nfeDetalheSelecionado = nfeDetalheSelecionado;
    }

    public NfeReferenciada getNfeReferenciada() {
        return nfeReferenciada;
    }

    public void setNfeReferenciada(NfeReferenciada nfeReferenciada) {
        this.nfeReferenciada = nfeReferenciada;
    }

    public NfeReferenciada getNfeReferenciadaSelecionado() {
        return nfeReferenciadaSelecionado;
    }

    public void setNfeReferenciadaSelecionado(NfeReferenciada nfeReferenciadaSelecionado) {
        this.nfeReferenciadaSelecionado = nfeReferenciadaSelecionado;
    }

    public boolean isPodeIncluirProduto() {
        return podeIncluirProduto;
    }

    public void setPodeIncluirProduto(boolean podeIncluirProduto) {
        this.podeIncluirProduto = podeIncluirProduto;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public Integer getIntervaloParcelas() {
        return intervaloParcelas;
    }

    public void setIntervaloParcelas(Integer intervaloParcelas) {
        this.intervaloParcelas = intervaloParcelas;
    }

    public Date getPrimeiroVencimento() {
        return primeiroVencimento;
    }

    public void setPrimeiroVencimento(Date primeiroVencimento) {
        this.primeiroVencimento = primeiroVencimento;
    }

    public boolean isDadosSalvos() {
        return dadosSalvos;
    }

    public void setDadosSalvos(boolean dadosSalvos) {
        this.dadosSalvos = dadosSalvos;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public PdvTipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(PdvTipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public int getNumeroNfe() {
        return numeroNfe;
    }

    public void setNumeroNfe(int numeroNfe) {
        this.numeroNfe = numeroNfe;
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

// </editor-fold>


}
