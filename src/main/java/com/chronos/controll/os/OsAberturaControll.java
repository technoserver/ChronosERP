package com.chronos.controll.os;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.service.comercial.OsProdutoServicoService;
import com.chronos.service.comercial.OsService;
import com.chronos.transmissor.infra.enuns.ModeloDocumento;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by john on 28/09/17.
 */
@Named
@ViewScoped
public class OsAberturaControll extends AbstractControll<OsAbertura> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<OsStatus> statusRepository;
    @Inject
    private Repository<Colaborador> colaboradorRepository;
    @Inject
    private Repository<Cliente> clienteRepository;
    @Inject
    private Repository<OsEquipamento> equipamentoRepository;
    @Inject
    private OsService osService;
    @Inject
    private OsProdutoServicoService produtoServicoService;
    @Inject
    private ProdutoService produtoService;
    @Inject
    private Repository<VendaCondicoesPagamento> pagamentoRepository;


    private OsAberturaEquipamento osAberturaEquipamento;
    private OsAberturaEquipamento osAberturaEquipamentoSelecionado;

    private OsProdutoServico osProdutoServico;
    private OsProdutoServico osProdutoServicoSelecionado;

    private OsEvolucao osEvolucao;
    private OsEvolucao osEvolucaoSelecionado;


    private boolean temProduto;
    private boolean emailValido;

    private String numero;
    private String cliente;
    private Date dataInicial;
    private Date dataFinal;
    private String justificativa;

    private Map<String, Integer> status;

    private boolean podeAlterarPreco = true;


    @PostConstruct
    @Override
    public void init() {
        super.init();

        status = getOsStatus().entrySet().stream()
                .filter(x -> !x.getValue().equals(11) || !x.getValue().equals(12) || !x.getValue().equals(13))
                .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));

        this.podeAlterarPreco = FacesUtil.getUsuarioSessao().getAdministrador().equals("S")
                || FacesUtil.getRestricao().getAlteraPrecoNaVenda().equals("S");
    }

    @Override
    public ERPLazyDataModel<OsAbertura> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
        }
        dataModel.setAtributos(new Object[]{"numero", "dataInicio", "dataPrevisao", "dataFim", "valorTotal", "cliente.id", "cliente.pessoa.nome", "status", "idnfeCabecalho"});

        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);
        }

        return dataModel;
    }


    public void pesquisar() {

        dataModel.getFiltros().clear();
        dataModel.addFiltro("empresa.id", empresa.getId(), Filtro.IGUAL);


        if (!StringUtils.isEmpty(cliente)) {
            dataModel.addFiltro("cliente.pessoa.nome", cliente, Filtro.LIKE);
        }

        if (!StringUtils.isEmpty(numero)) {
            dataModel.addFiltro("numero", numero, Filtro.LIKE);
        }


        if (dataInicial != null) {
            dataModel.addFiltro("dataInicio", dataInicial, Filtro.MAIOR_OU_IGUAL);
        }

        if (dataFinal != null) {
            dataModel.addFiltro("dataInicio", dataFinal, Filtro.MENOR_OU_IGUAL);
        }


    }


    @Override
    public void doCreate() {
        super.doCreate();

        getObjeto().setDataInicio(new Date());
        getObjeto().setHoraInicio(new SimpleDateFormat("HH:mm:ss").format(new Date()));

        getObjeto().setColaborador(usuario.getColaborador());

        getObjeto().setListaOsAberturaEquipamento(new HashSet<>());
        getObjeto().setListaOsProdutoServico(new HashSet<>());
        getObjeto().setListaOsEvolucao(new HashSet<>());
        getObjeto().setStatus(1);
        getObjeto().setEmpresa(empresa);

        temProduto = false;
    }

    @Override
    public void doEdit() {
        super.doEdit();
        OsAbertura os = getDataModel().getRowData(getObjeto().getId().toString());
        setObjeto(os);

        temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
    }

    @Override
    public void salvar() {
        try {
            osService.salvar(getObjeto());
            setTelaGrid(false);
            Mensagem.addInfoMessage("OS salvar com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Erro ao salvar o servico", e);
        }
    }


    public void encerrar() {
        try {
            OsAbertura os = isTelaGrid() ? dataModel.getRowData(getObjetoSelecionado().getId().toString()) : getObjeto();

            osService.encerrar(os);
            setTelaGrid(true);
            Mensagem.addInfoMessage("OS Encerrada com sucesso");


        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao faturar o servico", ex);
        }
    }

    public void faturar(String codigoModelo) {

        try {

            OsAbertura os = isTelaGrid() ? dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class) : getObjeto();

            ModeloDocumento modelo = codigoModelo.equals("65") ? ModeloDocumento.NFCE : ModeloDocumento.NFE;

            gerarNFe(os, modelo);

            setTelaGrid(true);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gerar faturamento", ex);
            }
        }
    }

    public void gerarNFe(OsAbertura os, ModeloDocumento modelo) throws Exception {

        boolean estoque = isTemAcesso("ESTOQUE");
        osService.transmitirNFe(os, modelo, estoque);
    }

    public void danfe() {
        try {
            osService.gerarDanfe(getObjetoSelecionado());
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao gera o danfe ", ex);
        }
    }

    public void cancelar() {
        try {

            boolean estoque = isTemAcesso("ESTOQUE");
            OsAbertura os = dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class);


            if (os.getStatus().equals(13)) {
                justificativa = "";
                PrimeFaces.current().executeScript("PF('dialogOutrasTelas4').show();");
            } else {
                osService.cancelarOs(os, estoque, "");
            }



        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Erro ao cancelar servico", ex);
        }
    }

    public void cancelarNFe() {
        try {

            boolean estoque = FacesUtil.isUserInRole("ESTOQUE");
            OsAbertura os = dao.getJoinFetch(getObjetoSelecionado().getId(), OsAbertura.class);
            osService.cancelarOs(os, estoque, justificativa);

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("Erro ao cancelar Cupom \n", ex);
            } else {
                throw new RuntimeException("Erro ao cancelar Cupom", ex);
            }
        }
    }


    public void incluirOsProdutoServico() {
        osProdutoServico = new OsProdutoServico();
        osProdutoServico.setOsAbertura(getObjeto());
        osProdutoServico.setQuantidade(BigDecimal.ONE);

    }

    public void alterarOsProdutoServico() {
        osProdutoServico = osProdutoServicoSelecionado;
    }

    public void salvarOsProdutoServico() {
        try {

            produtoServicoService.verificarRestricao(osProdutoServico);

            if (produtoServicoService.isNecessarioAutorizacaoSupervisor()) {
                PrimeFaces.current().executeScript("PF('dialogOsProdutoServico').hide();");
                PrimeFaces.current().executeScript("PF('dialogSupervisor').show();");
            } else {
                setObjeto(osService.salvarItem(getObjeto(), osProdutoServico));
                Mensagem.addInfoMessage("Produto " + osProdutoServico.getProduto().getNome() + " add com sucesso !");

                temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
                setActiveTabIndex(1);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto !", e);

        }
    }

    @Override
    public boolean autorizacaoSupervisor() {

        try {
            if (osService.liberarRestricao(usuarioSupervisor, senhaSupervisor)) {
                setObjeto(osService.salvarItem(getObjeto(), osProdutoServico));
                Mensagem.addInfoMessage("Produto " + osProdutoServico.getProduto().getNome() + " add com sucesso !");

                temProduto = getObjeto().getListaOsProdutoServico().size() > 0;
                setActiveTabIndex(1);
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

    public void excluirOsProdutoServico() {
        getObjeto().getListaOsProdutoServico().remove(osProdutoServicoSelecionado);
        getObjeto().calcularValores();
        setObjeto(dao.atualizar(getObjeto()));
        Mensagem.addInfoMessage("Servi/Produto excluído com sucesso!");
        temProduto = getObjeto().getListaOsProdutoServico().size() > 0;

    }


    public void incluirOsAberturaEquipamento() {
        osAberturaEquipamento = new OsAberturaEquipamento();
        osAberturaEquipamento.setOsAbertura(getObjeto());
    }

    public void alterarOsAberturaEquipamento() {
        osAberturaEquipamento = osAberturaEquipamentoSelecionado;
    }

    public void salvarOsAberturaEquipamento() {
        if (osAberturaEquipamento.getId() == null) {
            getObjeto().getListaOsAberturaEquipamento().add(osAberturaEquipamento);
        }
        salvar("Registro salvo com sucesso!");
        setTelaGrid(false);
        setActiveTabIndex(3);
    }

    public void excluirOsAberturaEquipamento() {
        getObjeto().getListaOsAberturaEquipamento().remove(osAberturaEquipamentoSelecionado);
        setObjeto(dao.atualizar(getObjeto()));
        Mensagem.addInfoMessage("Equipamento excluído com sucesso!");
        setTelaGrid(false);
    }

    public void incluirOsEvolucao() {
        osEvolucao = new OsEvolucao();
        osEvolucao.setOsAbertura(getObjeto());
    }

    public void alterarOsEvolucao() {
        osEvolucao = osEvolucaoSelecionado;
    }

    public void salvarOsEvolucao() {
        if (osEvolucao.getId() == null) {
            getObjeto().getListaOsEvolucao().add(osEvolucao);
        }
        setActiveTabIndex(2);
        salvar("Registro salvo com sucesso!");
        setTelaGrid(false);
        if (emailValido && osEvolucao.getEnviarEmail().equals("S")) {
            enviarEmail(getObjeto().getCliente().getPessoa().getEmail(), osEvolucao.getObservacao());
        }

    }


    public void excluirOsEvolucao() {

        getObjeto().getListaOsEvolucao().remove(osEvolucaoSelecionado);
        setObjeto(dao.atualizar(getObjeto()));
        Mensagem.addInfoMessage("Evolução excluído com sucesso!");

    }


    public List<OsStatus> getListaOsStatus(String nome) {
        List<OsStatus> listaOsStatus = new ArrayList<>();
        try {
            listaOsStatus = statusRepository.getEntitys(OsStatus.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOsStatus;
    }

    public List<Colaborador> getListaColaborador(String nome) {
        List<Colaborador> listaColaborador = new ArrayList<>();
        try {
            listaColaborador = colaboradorRepository.getEntitys(Colaborador.class, "pessoa.nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaColaborador;
    }

    public List<Cliente> getListaCliente(String nome) {
        List<Cliente> listaCliente = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("pessoa.cliente", "S"));
            atributos = new Object[]{"pessoa.nome", "situacaoForCli.nome", "situacaoForCli.bloquear"};
            listaCliente = clienteRepository.getEntitys(Cliente.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCliente;
    }

    public List<OsEquipamento> getListaOsEquipamento(String nome) {
        List<OsEquipamento> listaOsEquipamento = new ArrayList<>();
        try {
            listaOsEquipamento = equipamentoRepository.getEntitys(OsEquipamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOsEquipamento;
    }

    public List<Produto> getListaProduto(String nome) {
        List<Produto> listaProduto = new ArrayList<>();
        try {
            listaProduto = produtoService.getListProdutoVenda(nome, empresa, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public List<VendaCondicoesPagamento> getListaVendaCondicoesPagamento(String nome) {
        List<VendaCondicoesPagamento> listaVendaCondicoesPagamento = new ArrayList<>();
        try {
            listaVendaCondicoesPagamento = pagamentoRepository.getEntitys(VendaCondicoesPagamento.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();

        }
        return listaVendaCondicoesPagamento;
    }

    public void selecionaValorProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        osProdutoServico.setValorUnitario(produto.getValorVenda());
    }


    private void enviarEmail(String emailEnvio, String msg) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<OsAbertura> getClazz() {
        return OsAbertura.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "OS_ABERTURA";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public OsEvolucao getOsEvolucao() {
        return osEvolucao;
    }

    public void setOsEvolucao(OsEvolucao osEvolucao) {
        this.osEvolucao = osEvolucao;
    }

    public OsEvolucao getOsEvolucaoSelecionado() {
        return osEvolucaoSelecionado;
    }

    public void setOsEvolucaoSelecionado(OsEvolucao osEvolucaoSelecionado) {
        this.osEvolucaoSelecionado = osEvolucaoSelecionado;
    }

    public OsAberturaEquipamento getOsAberturaEquipamento() {
        return osAberturaEquipamento;
    }

    public void setOsAberturaEquipamento(OsAberturaEquipamento osAberturaEquipamento) {
        this.osAberturaEquipamento = osAberturaEquipamento;
    }

    public OsAberturaEquipamento getOsAberturaEquipamentoSelecionado() {
        return osAberturaEquipamentoSelecionado;
    }

    public void setOsAberturaEquipamentoSelecionado(OsAberturaEquipamento osAberturaEquipamentoSelecionado) {
        this.osAberturaEquipamentoSelecionado = osAberturaEquipamentoSelecionado;
    }

    public OsProdutoServico getOsProdutoServico() {
        return osProdutoServico;
    }

    public void setOsProdutoServico(OsProdutoServico osProdutoServico) {
        this.osProdutoServico = osProdutoServico;
    }

    public OsProdutoServico getOsProdutoServicoSelecionado() {
        return osProdutoServicoSelecionado;
    }

    public void setOsProdutoServicoSelecionado(OsProdutoServico osProdutoServicoSelecionado) {
        this.osProdutoServicoSelecionado = osProdutoServicoSelecionado;
    }

    public boolean isTemProduto() {
        return temProduto;
    }

    public void setTemProduto(boolean temProduto) {
        this.temProduto = temProduto;
    }

    public boolean isEmailValido() {
        emailValido = false;
        emailValido = !StringUtils.isEmpty(empresa.getEmail())
                && Biblioteca.validarEmail(empresa.getEmail())
                && getObjeto() != null
                && getObjeto().getCliente() != null
                && !StringUtils.isEmpty(getObjeto().getCliente().getPessoa().getEmail());
        if (!emailValido) {
            return emailValido;
        }
        String email = getObjeto().getCliente().getPessoa().getEmail();
        emailValido = Biblioteca.validarEmail(email);
        return emailValido;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public Map<String, Integer> getStatus() {
        return status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public boolean isPodeAlterarPreco() {
        return podeAlterarPreco;
    }
}
