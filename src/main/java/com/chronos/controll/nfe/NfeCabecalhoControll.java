package com.chronos.controll.nfe;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.infra.enuns.LocalDestino;
import com.chronos.infra.enuns.ModeloDocumento;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.PessoaCliente;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.comercial.NfeService;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    private NfeService nfeService;

    private PessoaCliente pessoaCliente;
    private NfeDetalhe nfeDetalhe;
    private NfeDetalhe nfeDetalheSelecionado;
    private NfeReferenciada nfeReferenciada;
    private NfeReferenciada nfeReferenciadaSelecionado;
    private String observacao;
    private boolean podeIncluirProduto;
    private boolean duplicidade;
    private boolean dadosSalvos;

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
        dataModel.setAtributos(new Object[]{"cliente", "serie", "numero", "dataHoraEmissao", "chaveAcesso", "digitoChaveAcesso", "valorTotal", "statusNota", "codigoModelo"});
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
            nfeService.dadosPadroes(getObjeto(), ModeloDocumento.NFE, empresa);
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
            NfeCabecalho n = getDataModel().getRowData(getObjetoSelecionado().getId().toString());
            setObjeto(n);
            dadosSalvos = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void salvar() {


        try {
            nfeService.validar(getObjeto());
            if (getObjeto().getId() == null) {
                gerarNumeracao(getObjeto());
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
            if (!getObjeto().getListaNfeDetalhe().contains(nfeDetalhe)) {
                getObjeto().getListaNfeDetalhe().add(nfeDetalhe);
            }
            setObjeto(nfeService.atualizarTotais(getObjeto()));
            Mensagem.addInfoMessage("Registro incluído!");
            dadosSalvos = false;
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o produto\n", e);
        }

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
        nfeDetalhe.calcularTotal();
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

        nfeDetalhe.setListaArmamento(new HashSet<>());
        nfeDetalhe.setListaMedicamento(new HashSet<>());
        nfeDetalhe.setListaDeclaracaoImportacao(new HashSet<>());
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


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Transportadora">


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Crud Cliente">


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos NFe">


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Procedimentos Diversos">


    public void gerarNumeracao() {
        try {
            getObjeto().setNumero(null);
            gerarNumeracao(getObjeto());
        } catch (Exception ex) {
            Mensagem.addErrorMessage("", ex);
            throw new RuntimeException("Erro ao gera um novo número da NFe", ex);

        }
    }

    public void gerarNumeracao(NfeCabecalho nfe) throws Exception {
        nfeService.gerarNumeracao(nfe);
        duplicidade = false;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pesquisas">

    public List<TributOperacaoFiscal> getListaTributOperacaoFiscal(String descricao) {
        List<TributOperacaoFiscal> listaTributOperacaoFiscal = new ArrayList<>();

        try {
            listaTributOperacaoFiscal = operacoes.getEntitys(TributOperacaoFiscal.class, "descricao", descricao, new Object[]{"descricao"});
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

            listaProduto = nfeService.getListaProduto(descricao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProduto;
    }

    public void selecionaValorProduto(SelectEvent event) {
        Produto produto = (Produto) event.getObject();
        nfeDetalhe.setValorUnitarioComercial(Optional.ofNullable(produto.getValorVenda()).orElse(BigDecimal.ZERO));
    }

    // </editor-fold>

    @Override
    protected Class<NfeCabecalho> getClazz() {
        return NfeCabecalho.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "NFE_CABECALHO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    // <editor-fold defaultstate="collapsed" desc="GETS SETS">


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

// </editor-fold>


}
