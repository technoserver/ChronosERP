package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.dto.ProdutoDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.StatusTransmissao;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.NfeDetalheService;
import com.chronos.service.comercial.NfeService;
import com.chronos.transmissor.exception.EmissorException;
import com.chronos.transmissor.infra.enuns.LocalDestino;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

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
    private Repository<ViewPessoaTransportadora> transportadoraRepository;
    @Inject
    private Repository<Veiculo> veiculoRepository;
    @Inject
    private Repository<NfeEvento> eventoRepository;


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
    private PdvTipoPagamento tipoPagamento;
    private VendaCondicoesPagamento condicoesPagamento;
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

    private List<Veiculo> veiculos;
    private List<NfeEvento> cartas;
    private NfeEvento nfeEventoSelecionado;
    private Veiculo veiculo;
    private ViewPessoaTransportadora transportadora;

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
        dataModel.setAtributos(new Object[]{"destinatario.nome", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo"});
        dataModel.getFiltros().clear();
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        dataModel.addFiltro("codigoModelo", "55", Filtro.IGUAL);
        dataModel.addFiltro("tipoOperacao", 1, Filtro.IGUAL);

        dataModel.setSortOrder(SortOrder.DESCENDING);
        dataModel.setOrdernarPor("numero");

        return dataModel;
    }
    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud NFe">

    @Override
    public void doCreate() {
        try {
            super.doCreate();

            nfeService.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            getObjeto().setDestinatario(new NfeDestinatario());
            getObjeto().getDestinatario().setNfeCabecalho(getObjeto());

            NfeCabecalho nfeCabecalho = nfeService.dadosPadroes(ModeloDocumento.NFE);
            setObjeto(nfeCabecalho);
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
            tipoPagamento = nfe.getListaNfeFormaPagamento().stream().findFirst().orElse(new NfeFormaPagamento()).getPdvTipoPagamento();
            setObjeto(nfe);
            nfeService.instanciarConfNfe(empresa, ModeloDocumento.NFE);
            tipoPagamento = nfeService.instanciarFormaPagamento(getObjeto());

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
            nfeDetalhe.calcularValorTotalProduto();
            nfeDetalheService.verificarRestricao(nfeDetalhe);

            if (nfeDetalheService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogNfeDetalhe').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
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

    public void enviarEmail() {

        try {
//            configuracao = configuracao != null ? configuracao : configuraNfe();
//            ConfEmail confEmail = new ConfEmail();
//            confEmail.setHost(configuracao.getEmailServidorSmtp());
//            confEmail.setPorta(configuracao.getEmailPorta());
//            confEmail.setSsl(configuracao.getEmailAutenticaSsl().equals("S"));
//            confEmail.setUsuario(configuracao.getEmailUsuario());
//            confEmail.setSenha(configuracao.getEmailSenha());
//            confEmail.setTsl(!configuracao.getEmailAutenticaSsl().equals("S"));
//            EnvioEmail envio = new EnvioEmail();
//            envio.enviarNfeEmail(confEmail, getObjeto());
//
//            Mensagem.addInfoMessage("Email enviado com sucesso");
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

        nfeService.definirIndicadorIe(getObjeto().getDestinatario(), getObjeto().getModeloDocumento());

        dadosSalvos = false;

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

    public List<PdvTipoPagamento> getListaNfceTipoPagamento(String nome) {
        List<PdvTipoPagamento> listaPdvTipoPagamento = new ArrayList<>();
        try {

            listaPdvTipoPagamento = StringUtils.isEmpty(nome) ? nfeService.getTipoPagamentos() : nfeService.getTipoPagamentos()
                    .stream().filter(p -> p.getDescricao().toLowerCase().contains(nome)).collect(Collectors.toList());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaPdvTipoPagamento;
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
