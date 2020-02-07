package com.chronos.erp.controll.nfe;

import com.chronos.erp.controll.ERPLazyDataModel;
import com.chronos.erp.dto.ProdutoDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.StatusTransmissao;
import com.chronos.erp.modelo.view.PessoaCliente;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.ProdutoService;
import com.chronos.erp.service.comercial.NfeDetalheService;
import com.chronos.erp.service.comercial.NfeService;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.outjected.email.api.SendFailedException;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.AuthenticationFailedException;
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
public class NfeCabecalhoControll extends NfeBaseControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<PessoaCliente> pessoas;
    @Inject
    private Repository<CondicoesPagamento> condicoes;
    @Inject
    private Repository<ViewPessoaTransportadora> transportadoraRepository;
    @Inject
    private Repository<Veiculo> veiculoRepository;
    @Inject
    private Repository<NfeEvento> eventoRepository;
    @Inject
    private Repository<EmailConfiguracao> emailConfiguracaoRepository;
    @Inject
    private Repository<NotaFiscalTipo> notaFiscalTipoRepository;

    @Inject
    private NfeService nfeService;
    @Inject
    private NfeDetalheService nfeDetalheService;

    @Inject
    private ProdutoService produtoService;

    private PessoaCliente pessoaCliente;
    private NfeDetalhe nfeDetalhe;
    private NfeDetalhe nfeDetalheSelecionado;
    private NfeReferenciada nfeReferenciada;
    private NfeReferenciada nfeReferenciadaSelecionado;
    private NfeTransporteVolume volume;
    private TipoPagamento tipoPagamento;
    private CondicoesPagamento condicoesPagamento;
    private NfeDestinatario destinatario;
    private int qtdParcelas;
    private int intervaloParcelas;
    private Date primeiroVencimento;
    private String observacao;
    private boolean podeIncluirProduto = false;
    private boolean duplicidade;
    private boolean dadosSalvos;
    private String justificativa;
    private int numeroNfe;
    private Date dataInicial;
    private Date dataFinal;
    private int status;
    private String cliente;
    private String codigoModelo;

    private List<Veiculo> veiculos;
    private List<NfeEvento> cartas;
    private NfeEvento nfeEventoSelecionado;
    private Veiculo veiculo;
    private ViewPessoaTransportadora transportadora;

    private String email;
    private String assunto;
    private String texto;

    private boolean podeAlterarPreco = true;

    @PostConstruct
    @Override
    public void init() {
        super.init();

        this.podeAlterarPreco = FacesUtil.getUsuarioSessao().getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");
        codigoModelo = "55";
        status = -1;
    }

    @Override
    public ERPLazyDataModel<NfeCabecalho> getDataModel() {

        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(NfeCabecalho.class);
        }

        dataModel.setAtributos(new Object[]{"destinatario.nome", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo"});



        dataModel.setSortOrder(SortOrder.DESCENDING);
        dataModel.setOrdernarPor("numero");
        pesquisar();
        return dataModel;
    }


    public void pesquisar() {

        dataModel.getFiltros().clear();
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", codigoModelo, Filtro.IGUAL);
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);

        if (numeroNfe > 0) {
            dataModel.getFiltros().add(new Filtro("numero", Filtro.LIKE, numeroNfe + ""));
        }


        if (dataInicial != null) {
            dataInicial = DateUtils.truncate(dataInicial, Calendar.DATE);
            dataModel.getFiltros().add(new Filtro("dataHoraEmissao", Filtro.MAIOR_OU_IGUAL, dataInicial));
        }

        if (dataFinal != null) {
            dataFinal = DateUtils.truncate(dataFinal, Calendar.DATE);
            dataFinal = DateUtils.addSeconds(DateUtils.addMinutes(DateUtils.addHours(dataFinal, 23), 59), 59);
            dataModel.getFiltros().add(new Filtro("dataHoraEmissao", Filtro.MENOR_OU_IGUAL, dataFinal));
        }

        if (status > -1) {
            dataModel.getFiltros().add(new Filtro("statusNota", Filtro.MENOR_OU_IGUAL, status));
        }

        if (!StringUtils.isEmpty(cliente)) {
            dataModel.getFiltros().add(new Filtro("destinatario.nome", Filtro.LIKE, cliente));
        }

    }


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud NFe">

    @Override
    public void doCreate() {
        try {
            super.doCreate();
            pessoaCliente = null;
            duplicidade = false;
            NfeCabecalho nfeCabecalho = nfeService.dadosPadroes(empresa, ModeloDocumento.NFE);
            setObjeto(nfeCabecalho);
            destinatario = new NfeDestinatario();
            destinatario.setNfeCabecalho(getObjeto());
            dadosSalvos = false;
            observacao = getObjeto().getInformacoesAddContribuinte();
            tipoPagamento = nfeService.instanciarFormaPagamento(getObjeto());
            veiculo = null;
            transportadora = null;
            volume = new NfeTransporteVolume();
            volume.setNfeTransporte(getObjeto().getTransporte());
            cartas = new ArrayList<>();
            this.setActiveTabIndex(0);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addFatalMessage("Erro ao criar a NFe. ", ex);
            } else {
                throw new RuntimeException("Erro ao criar a NFe. ", ex);
            }
        }

    }

    @Override
    public void doEdit() {
        try {
            super.doEdit();
            NfeCabecalho nfe = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
            tipoPagamento = nfe.getListaNfeFormaPagamento().stream().findFirst().orElse(new NfeFormaPagamento()).getTipoPagamento();
            setObjeto(nfe);
            nfeService.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            tipoPagamento = nfeService.instanciarFormaPagamento(getObjeto());

            destinatario = nfe.getDestinatario();
            if (destinatario == null) {
                destinatario = new NfeDestinatario();
                destinatario.setNfeCabecalho(nfe);
            }

            if (getObjeto().getTransporte() != null && getObjeto().getTransporte().getTransportadora() != null) {
                transportadora = new ViewPessoaTransportadora();
                transportadora.setId(getObjeto().getTransporte().getTransportadora().getId());
                transportadora.setNome(getObjeto().getTransporte().getTransportadora().getPessoa().getNome());
                veiculo = new Veiculo();
                veiculo.setPlaca(getObjeto().getTransporte().getPlacaVeiculo());
                veiculos = new ArrayList<>();
                veiculos.add(veiculo);
                getObjeto().getTransporte().setPlacaVeiculo(veiculo.getUf());
                getObjeto().getTransporte().setUfVeiculo(veiculo.getUf());

                if (getObjeto().getTransporte().getListaTransporteVolume() != null && getObjeto().getTransporte().getListaTransporteVolume().size() > 0) {
                    volume = getObjeto().getTransporte().getListaTransporteVolume().iterator().next();
                } else {
                    volume = new NfeTransporteVolume();
                    volume.setNfeTransporte(getObjeto().getTransporte());
                    getObjeto().getTransporte().setListaTransporteVolume(new HashSet<>());

                }

            }

            cartas = eventoRepository.getEntitys(NfeEvento.class, "idnfecabecalho", getObjeto().getId());
            dadosSalvos = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            this.setTelaGrid(true);
            Mensagem.addFatalMessage("Erro ao editar NFE. ", ex);
        }
    }

    @Override
    public void salvar() {
        try {
            String str = getObjeto().getInformacoesAddContribuinte() + " " + observacao;
            getObjeto().setInformacoesAddContribuinte(str);
            getObjeto().setNaturezaOperacao(getObjeto().getTributOperacaoFiscal().getDescricao());


            if (getObjeto().getTransporte() != null && getObjeto().getTransporte().getTransportadora() != null && veiculo != null) {
                getObjeto().getTransporte().setPlacaVeiculo(veiculo.getPlaca());
                getObjeto().getTransporte().setUfVeiculo(veiculo.getUf());

                if (getObjeto().getTransporte().getListaTransporteVolume() != null) {
                    getObjeto().getTransporte().getListaTransporteVolume().clear();
                    getObjeto().getTransporte().getListaTransporteVolume().add(volume);
                }
            }

            if (!StringUtils.isEmpty(destinatario.getNome())) {
                getObjeto().setDestinatario(destinatario);
            }

            setObjeto(nfeService.salvar(getObjeto(), tipoPagamento));

            Mensagem.addInfoMessage("NFe salva com sucesso");
            setTelaGrid(false);
            dadosSalvos = true;
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao salvar a nfe", ex);
            }


        }
    }

    public void duplicar() {
        try {
            NfeCabecalho nfe = getDataModel().getRowData(getObjetoSelecionado().getId().toString());

            nfeService.duplicarNfe(nfe);
            Mensagem.addInfoMessage("NFe duplicada com sucesso");

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao duplicar a nfe", ex);
            }
        }
    }

    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Produto">

    public void inlcuirProduto() {
        if (getObjeto().getTributOperacaoFiscal() == null) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("Antes de incluir produtos selecione a Operação Fiscal.");

        } else if (getObjeto().getCodigoModelo().equals("55") && StringUtils.isEmpty(destinatario.getNome())) {
            podeIncluirProduto = false;
            Mensagem.addInfoMessage("Antes de incluir produtos selecione o destinatário.");

        } else {
            podeIncluirProduto = true;
            nfeDetalhe = new NfeDetalhe();
            nfeDetalhe.setNfeCabecalho(getObjeto());
            nfeDetalhe.setQuantidadeComercial(BigDecimal.ONE);
            nfeDetalhe.setClassificacaoContabilConta(getObjeto().getTributOperacaoFiscal().getClassificacaoContabilConta());
            if (getObjeto().getFinalidadeEmissao() > 1) {
                instanciarImpostos(nfeDetalhe);
            }
        }
    }

    public void salvaProduto() {
        try {
            nfeDetalhe.calcularValorTotalProduto();
            nfeDetalheService.verificarRestricao(nfeDetalhe);

            if (nfeDetalheService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogNfeDetalhe').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                nfeDetalhe = nfeDetalheService.realizaCalculosItem(nfeDetalhe, getObjeto().getTributOperacaoFiscal(), destinatario);
                NfeCabecalho nfe = nfeDetalheService.addProduto(getObjeto(), nfeDetalhe);
                setObjeto(nfe);
                setObjeto(nfeService.atualizarTotais(getObjeto()));
                dadosSalvos = false;
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {

                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o produto\n", ex);
            }

        }

    }

    @Override
    public boolean autorizacaoSupervisor() {

        try {
            if (nfeDetalheService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                NfeCabecalho nfe = nfeDetalheService.addProduto(getObjeto(), nfeDetalhe);
                setObjeto(nfe);
                setObjeto(nfeService.atualizarTotais(getObjeto()));
                dadosSalvos = false;
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
            condicoesPagamento = condicoesPagamento != null
                    ? condicoes.getJoinFetch(condicoesPagamento.getId(), CondicoesPagamento.class)
                    : null;
            nfeService.gerarDuplicatas(getObjeto(), condicoesPagamento, primeiroVencimento, intervaloParcelas, qtdParcelas);
            Mensagem.addInfoMessage("Duplicatas geradas com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }


    }

    public void apagarDuplicatas() {
        getObjeto().setListaDuplicata(new HashSet<>());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Transportadora">


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Cliente">


    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Procedimentos NFe">

    public void imprimirCartaCorrecao() {

        try {
            nfeService.imprimirCartaCorrecao(nfeEventoSelecionado, getObjeto());
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gerar carta de correção", ex);
            }

        }
    }

    public void danfe() {
        try {

            nfeService.danfe(getObjeto());

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }



    public void transmitirNfe() {
        try {
            if (dadosSalvos) {

                boolean estoque = isTemAcesso("ESTOQUE");

                nfeService.instanciarDadosConfiguracoes(getObjeto());

                StatusTransmissao status = nfeService.transmitirNFe(getObjeto(), estoque);
                if (status == StatusTransmissao.AUTORIZADA) {

                    Mensagem.addInfoMessage("NFe transmitida com sucesso");
                } else {
                    duplicidade = status == StatusTransmissao.DUPLICIDADE;

                }


            } else {
                Mensagem.addInfoMessage("Antes de enviar a NF-e é necessário salvar as informações!");
            }
        } catch (EmissorException ex) {
            if (ex.getMessage().contains("Read timed out") || ex.getMessage().contains("connect timed out")) {
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

            boolean estoque = isTemAcesso("ESTOQUE");
            boolean cancelado = nfeService.cancelarNFe(getObjeto(), estoque);
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
            justificativa = Biblioteca.removerAcentos(justificativa);
            NfeEvento nfeEvento = nfeService.cartaCorrecao(getObjeto(), justificativa);

            if (cartas == null) {
                cartas = new ArrayList<>();
                cartas.add(nfeEvento);
            } else {
                cartas.add(nfeEvento);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao enviar a carta de correção!", e);
        }
    }

    public void verificarSituacao() {
        try {

            nfeService.verificarSituacao(getObjeto());

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao consultar a nfe!", ex);
        }
    }

    public void exibirEnvioEmail() {


        NfeCabecalho nfe = isTelaGrid() ? dataModel.getRowData(getObjetoSelecionado().getId().toString()) : getObjeto();
        setObjeto(nfe);

        EmailConfiguracao emailConfiguracao = emailConfiguracaoRepository.get(EmailConfiguracao.class, "empresa.id", empresa.getId(), new Object[]{"assunto", "texto"});

        if (emailConfiguracao != null) {
            assunto = emailConfiguracao.getAssunto();
            texto = emailConfiguracao.getTexto();
        }
        email = nfe.getDestinatario().getEmail();

    }

    public void enviarEmail() {

        try {

            nfeService.enviarEmail(getObjeto(), email, assunto, texto);
            Mensagem.addInfoMessage("Email enviado com sucesso");
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else if (ex instanceof AuthenticationFailedException || ex instanceof SendFailedException) {
                Mensagem.addErrorMessage("Erro ao enviar email", ex);
            } else {
                throw new RuntimeException("Erro ao enviar email", ex);
            }


        }
    }

    public void limparJustificativa() {
        justificativa = "";
    }

    public void visualizarXml() {
        try {
            if (dadosSalvos) {

                String caminho = nfeService.gerarNfePreProcessada(getObjeto());

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
    public void atulizarNumeracao() {
        try {
            int ultimoNumero = Integer.valueOf(getObjeto().getNumero());
            String serie = getObjeto().getSerie();
            int idempresa = getObjeto().getEmpresa().getId();
            String modelo = getObjeto().getCodigoModelo();
            notaFiscalTipoRepository.atualizarNamedQuery("NotaFiscalTipo.UpdateNumeroModelo", ultimoNumero, modelo, serie, idempresa);
            getObjeto().setNumero("");
            getObjeto().setChaveAcesso("");
            getObjeto().setDigitoChaveAcesso(null);
            getObjeto().setCodigoNumerico("");
            Mensagem.addInfoMessage("Atualizado numeração da NFe para " + ultimoNumero);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao atualizar o número da NFe");
            } else {
                throw new RuntimeException("Erro ao atualizar o número da NFe", ex);
            }
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pesquisas">


    public List<ViewPessoaTransportadora> getListaTransportadora(String nome) {
        List<ViewPessoaTransportadora> listaTransportadora = new ArrayList<>();
        try {
            listaTransportadora = transportadoraRepository.getEntitys(ViewPessoaTransportadora.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTransportadora;
    }

    public void selecionarTransportadora(SelectEvent event) {
        transportadora = (ViewPessoaTransportadora) event.getObject();

        NfeTransporte nfeTransporte = getObjeto().getTransporte();

        nfeTransporte.setTransportadora(new Transportadora(transportadora.getId(), transportadora.getNome()));

        nfeTransporte.setCpfCnpj(transportadora.getCpfCnpj());
        nfeTransporte.setEmpresaEndereco(transportadora.getCidade());
        nfeTransporte.setNome(transportadora.getNome());
        nfeTransporte.setMunicipio(transportadora.getMunicipioIbge());
        nfeTransporte.setNomeMunicipio(transportadora.getCidade());

        nfeTransporte.setRntcVeiculo(transportadora.getRntrc());
        nfeTransporte.setUf(transportadora.getUf());

        veiculos = veiculoRepository.getEntitys(Veiculo.class, "transportadora.id", transportadora.getId());
        veiculo = null;

    }

    public void selecionarVeiculo() {
        NfeTransporte nfeTransporte = getObjeto().getTransporte();
        nfeTransporte.setPlacaVeiculo(veiculo.getPlaca());
        nfeTransporte.setUfVeiculo(veiculo.getUf());
    }


    public List<CondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<CondicoesPagamento> listaCondicoesPagamento = new ArrayList<>();
        try {
            listaCondicoesPagamento = condicoes.getEntitys(CondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCondicoesPagamento;
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


        try {
            pessoaCliente = (PessoaCliente) event.getObject();
            Cliente cliente = new Cliente();
            cliente.setId(pessoaCliente.getId());
            getObjeto().setCliente(cliente);

            destinatario.setCpfCnpj(pessoaCliente.getCpfCnpj());
            destinatario.setNome(pessoaCliente.getNome());
            destinatario.setLogradouro(pessoaCliente.getLogradouro());
            destinatario.setComplemento(pessoaCliente.getComplemento());
            destinatario.setNumero(pessoaCliente.getNumero());
            destinatario.setBairro(pessoaCliente.getBairro());
            destinatario.setNomeMunicipio(pessoaCliente.getCidade());
            destinatario.setCodigoMunicipio(pessoaCliente.getMunicipioIbge());
            destinatario.setUf(pessoaCliente.getUf());
            destinatario.setCep(pessoaCliente.getCep());
            destinatario.setTelefone(pessoaCliente.getFone());
            destinatario.setInscricaoEstadual(pessoaCliente.getRgIe());
            destinatario.setEmail(pessoaCliente.getEmail());
            destinatario.setCodigoPais(1058);
            destinatario.setNomePais("Brazil");


            getObjeto().setLocalDestino(LocalDestino.getByUf(empresa.buscarEnderecoPrincipal().getUf(), pessoaCliente.getUf()));
            nfeService.definirIndicadorIe(destinatario, getObjeto().getModeloDocumento());
            dadosSalvos = false;
        } catch (ChronosException e) {
            if (e instanceof ChronosException) {
                Mensagem.addErrorMessage("", e);
            } else {
                throw new RuntimeException("Erro definir destinatário", e);
            }
        }


    }

    public List<Produto> getListaProduto(String descricao) {
        List<Produto> listaProduto = new ArrayList<>();

        try {
            List<ProdutoDTO> list = produtoService.getListaProdutoDTO(empresa, descricao, false);
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

    public List<TipoPagamento> getListaNfceTipoPagamento(String nome) {
        List<TipoPagamento> listaTipoPagamento = new ArrayList<>();
        try {

            listaTipoPagamento = StringUtils.isEmpty(nome) ? nfeService.getTipoPagamentos() : nfeService.getTipoPagamentos()
                    .stream().filter(p -> p.getDescricao().toLowerCase().contains(nome)).collect(Collectors.toList());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoPagamento;
    }

//    private PdvConfiguracao getConfiguraNfce() throws Exception {
//        List<Filtro> filtros = new LinkedList<>();
//        filtros.add(new Filtro(Filtro.AND, "empresa.id", Filtro.IGUAL, empresa.getId()));
//        configuracao = configuracao == null ? configuracoes.get(PdvConfiguracao.class, filtros) : configuracao;
//        if (configuracao == null) {
//            throw new Exception("NFC-e não configurada !!!");
//        }
//        return configuracao;
//    }

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


    public NfeDestinatario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(NfeDestinatario destinatario) {
        this.destinatario = destinatario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public NfeTransporteVolume getVolume() {
        return volume;
    }

    public void setVolume(NfeTransporteVolume volume) {
        this.volume = volume;
    }

    public List<NfeEvento> getCartas() {
        return cartas;
    }

    public NfeEvento getNfeEventoSelecionado() {
        return nfeEventoSelecionado;
    }

    public void setNfeEventoSelecionado(NfeEvento nfeEventoSelecionado) {
        this.nfeEventoSelecionado = nfeEventoSelecionado;
    }

    public ViewPessoaTransportadora getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(ViewPessoaTransportadora transportadora) {
        this.transportadora = transportadora;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public CondicoesPagamento getCondicoesPagamento() {
        return condicoesPagamento;
    }

    public void setCondicoesPagamento(CondicoesPagamento condicoesPagamento) {
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

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public boolean isPodeAlterarPreco() {
        return podeAlterarPreco;
    }

    public String getCodigoModelo() {
        return codigoModelo;
    }

    public void setCodigoModelo(String codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    // </editor-fold>


}
