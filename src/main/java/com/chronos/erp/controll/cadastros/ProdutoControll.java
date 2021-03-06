/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.controll.cadastros.datamodel.ProdutoEmpresaDataModel;
import com.chronos.erp.dto.MapNomeIdDTO;
import com.chronos.erp.dto.ProdutoResumDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.view.ViewProdutoEmpresa;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.ProdutoRepository;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.ProdutoService;
import com.chronos.erp.util.ArquivoUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author john
 */
@Named
@ViewScoped
public class ProdutoControll extends AbstractControll<Produto> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoSubGrupo> subGrupos;
    @Inject
    private Repository<ProdutoGrupo> grupos;
    @Inject
    private Repository<TributIcmsCustomCab> icmsCustomizados;
    @Inject
    private Repository<UnidadeProduto> unidades;
    @Inject
    private Repository<Almoxarifado> almoxarifados;
    @Inject
    private Repository<TributGrupoTributario> gruposTributarios;
    @Inject
    private Repository<EstoqueCor> estoqueCorRepository;
    @Inject
    private Repository<EstoqueTamanho> estoqueTamanhoRepository;
    @Inject
    private Repository<EstoqueGrade> estoqueGradeRepository;
    @Inject
    private Repository<ViewProdutoEmpresa> produtos;
    @Inject
    private Repository<EmpresaProduto> produtosEmpresa;
    @Inject
    private Repository<ProdutoMarca> marcas;
    @Inject
    private Repository<ProdutoAlteracaoItem> produtosAlterado;
    @Inject
    private Repository<UnidadeConversao> unidadeConversaoRepository;
    @Inject
    private Repository<Ncm> ncmRepository;
    @Inject
    private Repository<Cest> cestRepository;
    @Inject
    private Repository<TabelaNutricionalCabecalho> tabelaNutricionalRepository;

    @Inject
    private Repository<PdvConfiguracaoBalanca> pdvConfiguracaoBalancaRepository;
    @Inject
    private Repository<ProdutoGrade> gradeRepository;
    @Inject
    private ProdutoRepository repository;

    @Inject
    private ProdutoService service;


    private ProdutoGrupo grupo;
    private ProdutoEmpresaDataModel produtoDataModel;
    private List<EmpresaProduto> listProdutoEmpresa;
    private List<Empresa> empresas;
    private List<EstoqueGrade> grades;
    private ViewProdutoEmpresa produtoSelecionado;


    private Integer codigo;
    private Integer idmepresaFiltro;
    private String gtin;
    private String produto;
    private String strGrupo;
    private String strSubGrupo;
    private String inativo = "N";
    private String nomeProdutoOld;
    private String nomeFoto;
    private String ncm;
    private String cest;

    private ProdutoMarca marca;
    private Almoxarifado almoxarifado;
    private ProdutoSubGrupo subGrupo;
    private EstoqueCor cor;
    private EstoqueTamanho tamanho;

    private UnidadeConversao unidadeConversao;
    private UnidadeConversao unidadeConversaoSelecionada;
    private List<UnidadeConversao> conversoes;
    private List<Ncm> ncms;
    private Ncm ncmSelecionado;

    private List<Cest> cests;
    private Cest cestSelecionado;

    @NotNull(message = "Unidade de conversão obrigatória")
    private UnidadeProduto unidadeProduto;
    private String acao;
    @NotNull(message = "Fator de conversão obrigatório")
    @DecimalMin(value = "0.01", message = "valor mínimo de conversão 0.01")
    private BigDecimal fator;

    private int idempresa;


    private PdvConfiguracaoBalanca configuracaoBalanca;

    private List<PdvConfiguracaoBalanca> configuracoesBalanca;

    private FichaTecnica fichaTecnica;
    private FichaTecnica fichaTecnicaSelecionado;
    private MapNomeIdDTO produtoFichaTecnica;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        idmepresaFiltro = empresa.getId();
        pesquisarEmpresas();
    }

    public ProdutoEmpresaDataModel getProdutoDataModel() {

        if (produtoDataModel == null) {
            produtoDataModel = new ProdutoEmpresaDataModel();
            produtoDataModel.setClazz(ViewProdutoEmpresa.class);
            produtoDataModel.setDao(produtos);
        }

        pesquisar();


        return produtoDataModel;
    }

    public void pesquisar() {
        produtoDataModel.getFiltros().clear();
        if (!StringUtils.isEmpty(produto)) {
            produtoDataModel.addFiltro("nome", produto);
        }
        if (!StringUtils.isEmpty(strSubGrupo)) {
            produtoDataModel.addFiltro("subgrupo", strSubGrupo);
        }
        if (!StringUtils.isEmpty(strGrupo)) {
            produtoDataModel.addFiltro("grupo", strGrupo);
        }

        if (!StringUtils.isEmpty(codigo)) {
            produtoDataModel.addFiltro("id", codigo, Filtro.IGUAL);
        }

        if (!StringUtils.isEmpty(gtin)) {
            produtoDataModel.addFiltro("gtin", gtin, Filtro.IGUAL);
        }

        idmepresaFiltro = idmepresaFiltro == null || idmepresaFiltro == 0 ? empresa.getId() : idmepresaFiltro;
        inativo = StringUtils.isEmpty(inativo) ? "N" : inativo;

        produtoDataModel.addFiltro("inativo", inativo, Filtro.IGUAL);
        produtoDataModel.addFiltro("excluido", "N", Filtro.IGUAL);
        produtoDataModel.addFiltro("idempresa", idmepresaFiltro, Filtro.IGUAL);
    }


    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.
        getObjeto().setExcluido("N");
        getObjeto().setInativo("N");
        getObjeto().setPossuiGrade(false);
        getObjeto().setDataCadastro(new Date());
        getObjeto().setControle(BigDecimal.ZERO);
        getObjeto().setListaFichaTecnica(new HashSet<>());
        grupo = new ProdutoGrupo();
        conversoes = new ArrayList<>();
        grades = new ArrayList<>();
        incluirFichaTecnica();


    }

    @Override
    public void doEdit() {
        super.doEdit();
        fichaTecnica = new FichaTecnica();
        fichaTecnica.setProduto(getObjeto());
        listaEmpresas = new ArrayList<>();
        Produto produto = dao.getJoinFetch(produtoSelecionado.getId(), Produto.class);
        setObjeto(produto);
        grupo = getObjeto().getProdutoSubGrupo().getProdutoGrupo();
        nomeProdutoOld = getObjeto().getNome();
        nomeFoto = getObjeto().getImagem();
        conversoes = unidadeConversaoRepository.getEntitys(UnidadeConversao.class, "produto.id", getObjeto().getId(), new Object[]{"sigla", "fatorConversao", "acao"});

        verificarGrade();
        incluirFichaTecnica();

    }

    private void verificarGrade() {
        if (getObjeto().getPossuiGrade() != null && getObjeto().getPossuiGrade()) {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("idproduto", getObjeto().getId()));
            filtros.add(new Filtro("idempresa", empresa.getId()));

            grades = estoqueGradeRepository.getEntitys(EstoqueGrade.class, filtros);
        } else {
            grades = new ArrayList<>();
        }
    }

    @Override
    public void remover() {

        try {
            getObjeto().setExcluido("S");
            dao.atualizar(getObjeto());
            if (!StringUtils.isEmpty(getObjeto().getImagem())) {
                ArquivoUtil.getInstance().removerFoto(getObjeto().getImagem());
            }
            Mensagem.addInfoMessage("Exclusao realizado com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }

    }


    @Override
    public void salvar() {
        try {
            getObjeto().setImagem(nomeFoto);
            if (getObjeto().getId() != null) {
                empresasSelecionada = new ArrayList<>();
            }

            setObjeto(service.salvar(getObjeto(), empresasSelecionada, grades));
            Mensagem.addInfoMessage("Registro salvo com sucesso");
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o registro!", ex);
            }

        }


    }

    public void salvarCadastroRapido(SelectEvent event) {
        ProdutoResumDTO prod = (ProdutoResumDTO) event.getObject();

        Produto produto = prod.gerarProduto();

        setObjeto(produto);

        salvar();
    }

    public void copiar() {

        try {
            Produto produto = new Produto();
            doEdit();
            BeanUtils.copyProperties(getObjeto(), produto, "id", "gtin");
            listaEmpresas = new ArrayList<>();
            setObjeto(produto);
            nomeFoto = produto.getImagem();
            Mensagem.addInfoMessage("Produto copiado com sucesso");
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o registro!", ex);
            }
        }


    }

    public void alteraTributacao() {
        getObjeto().setTributIcmsCustomCab(null);
        getObjeto().setTributGrupoTributario(null);
    }

    public void uploadImagem(FileUploadEvent event) {
        try {
//            if (getObjeto().getId() == null) {
//                throw new Exception("Necessário salvar o produto antes de realizar o upload");
//            }
            if (!StringUtils.isEmpty(nomeFoto)) {
                ArquivoUtil.getInstance().removerFoto(nomeFoto);
            }
            UploadedFile arquivo = event.getFile();
            nomeFoto = ArquivoUtil.getInstance().salvarFotoProdutoTemporariamente(arquivo);


        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public void incluirFichaTecnica() {
        fichaTecnica = new FichaTecnica();
        fichaTecnica.setProduto(getObjeto());
    }

    public void removerFichaTecnica() {

        getObjeto().getListaFichaTecnica().remove(fichaTecnicaSelecionado);


    }

    public void salvarFichaTecnica() {
        if (produtoFichaTecnica == null) {
            Mensagem.addErrorMessage("Composição obrigatoria");
            return;
        }

        if (fichaTecnica.getQuantidade() == null || fichaTecnica.getQuantidade().signum() <= 0) {
            Mensagem.addErrorMessage("Quanidade obrigatoria");
            return;
        }

        if (fichaTecnica.getSequenciaProducao() == null || fichaTecnica.getSequenciaProducao().equals(0)) {
            Mensagem.addErrorMessage("Sequência obrigatoria");
            return;
        }

        Optional<FichaTecnica> optionalFichaTecnica = getObjeto().getListaFichaTecnica()
                .stream()
                .filter(f -> f.getSequenciaProducao().equals(fichaTecnica.getSequenciaProducao()))
                .findFirst();

        if (optionalFichaTecnica.isPresent()) {
            Mensagem.addErrorMessage("Sequência já definida");
            return;
        }

        optionalFichaTecnica = getObjeto().getListaFichaTecnica()
                .stream()
                .filter(f -> f.getIdProdutoFilho().equals(produtoFichaTecnica.getId()))
                .findFirst();

        if (optionalFichaTecnica.isPresent()) {
            Mensagem.addErrorMessage("Composição já definida");
            return;
        }

        fichaTecnica.setDescricao(produtoFichaTecnica.getNome());
        fichaTecnica.setIdProdutoFilho(produtoFichaTecnica.getId());


        getObjeto().getListaFichaTecnica().add(fichaTecnica);
        fichaTecnica = new FichaTecnica();
        fichaTecnica.setProduto(getObjeto());
    }

    public List<MapNomeIdDTO> getListaProdutoComposicao(String nome) {
        List<MapNomeIdDTO> listaProduto = new ArrayList<>();
        try {
            listaProduto = repository.getProdutoComposicao(nome, empresa.getId());
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaProduto;
    }


    public List<ProdutoSubGrupo> getListaSubgrupo(String nome) {
        List<ProdutoSubGrupo> listaSubgrupo = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            if (grupo.getId() != null) {
                filtros.add(new Filtro("produtoGrupo.id", grupo.getId()));
            }
            listaSubgrupo = subGrupos.getEntitys(ProdutoSubGrupo.class, filtros);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSubgrupo;
    }

    public List<ProdutoGrupo> getListaGrupo(String nome) {
        List<ProdutoGrupo> listaGrupo = new ArrayList<>();
        try {

            listaGrupo = grupos.getEntitys(ProdutoGrupo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupo;
    }

    public List<TributIcmsCustomCab> getListaTributIcmsCustomCab(String nome) {
        List<TributIcmsCustomCab> listaTributIcmsCustomCab = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaTributIcmsCustomCab = icmsCustomizados.getEntitys(TributIcmsCustomCab.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTributIcmsCustomCab;
    }

    public List<UnidadeProduto> getListaUnidadeProduto(String nome) {
        List<UnidadeProduto> listaUnidadeProduto = new ArrayList<>();
        try {

            listaUnidadeProduto = unidades.getEntitys(UnidadeProduto.class, "descricao", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaUnidadeProduto;
    }

    public List<Almoxarifado> getListaAlmoxarifado(String nome) {
        List<Almoxarifado> listaAlmoxarifado = new ArrayList<>();
        try {

            listaAlmoxarifado = almoxarifados.getEntitys(Almoxarifado.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAlmoxarifado;
    }

    public List<TributGrupoTributario> getListaGrupoTributario(String nome) {
        List<TributGrupoTributario> listaGrupoTributario = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaGrupoTributario = gruposTributarios.getEntitys(TributGrupoTributario.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaGrupoTributario;
    }

    public List<EstoqueCor> getListaCor(String nome) {
        List<EstoqueCor> cores = new ArrayList<>();
        try {

            cores = estoqueCorRepository.getEntitys(EstoqueCor.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return cores;
    }

    public List<EstoqueTamanho> getListaTamanho(String nome) {
        List<EstoqueTamanho> tamanhos = new ArrayList<>();
        try {

            tamanhos = estoqueTamanhoRepository.getEntitys(EstoqueTamanho.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return tamanhos;
    }


    public List<ProdutoMarca> getListaMarcaProduto(String nome) {
        List<ProdutoMarca> listaMarcaProduto = new ArrayList<>();
        try {
            listaMarcaProduto = marcas.getEntitys(ProdutoMarca.class, "nome", nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMarcaProduto;
    }

    public List<TabelaNutricionalCabecalho> getListaTabelaNutricional(String nome) {
        List<TabelaNutricionalCabecalho> tabelas = new ArrayList<>();
        try {
            tabelas = tabelaNutricionalRepository.getEntitys(TabelaNutricionalCabecalho.class, "nome", nome, new Object[]{"nome"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tabelas;
    }

    public List<ProdutoGrade> getListaProdutoGrade(String nome) {
        List<ProdutoGrade> grades = new ArrayList<>();
        try {
            grades = gradeRepository.getEntitys(ProdutoGrade.class, "nome", nome, new Object[]{"nome"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    public void buscarProdutoEmpresas(ToggleEvent event) {


        try {
            if (event.getVisibility() == Visibility.VISIBLE) {
                ViewProdutoEmpresa view = (ViewProdutoEmpresa) event.getData();
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro("produto.id", Filtro.IGUAL, view.getId()));
                atributos = new Object[]{"empresa.id", "empresa.razaoSocial", "quantidadeEstoque", "estoqueVerificado"};

                listProdutoEmpresa = produtosEmpresa.getEntitys(EmpresaProduto.class, filtros, atributos);

                Produto produto = dao.get(view.getId(), Produto.class);
                setObjeto(produto);
                verificarGrade();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Não foi possivel buscar as informações das empresas!", ex);
        }

    }


    public void buscarConfiguracoesBalanca() {
        configuracoesBalanca = pdvConfiguracaoBalancaRepository.getEntitys(PdvConfiguracaoBalanca.class);
    }

    public void gerarIntegracaoBalanca() {
        try {

            service.gerarIntegracaoBalanca(configuracaoBalanca);
        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gera dados para balança", ex);
            }
        }

    }

    public void addGrade() {

        if (cor == null) {
            Mensagem.addErrorMessage("Cor obrigatoria");
            return;
        }

        if (tamanho == null) {
            Mensagem.addErrorMessage("Tamanho obrigatoria");
            return;
        }

        boolean present = grades
                .stream()
                .filter(g -> g.getEstoqueCor().getId().equals(cor.getId())
                        && g.getEstoqueTamanho().getId().equals(tamanho.getId()))
                .findFirst().isPresent();


        if (!present) {
            EstoqueGrade grade = new EstoqueGrade();
            grade.setEstoqueCor(cor);
            grade.setEstoqueTamanho(tamanho);
            grade.setIdempresa(empresa.getId());
            grades.add(grade);
        } else {
            Mensagem.addErrorMessage("Grade já definida");
        }


    }

    public void removerGrade(EstoqueGrade grade) {

        if (grade.getId() != null) {
            estoqueGradeRepository.excluir(grade);
        }

        grades.remove(grade);
    }

    public void addMarca() {
        marca = new ProdutoMarca();
    }

    public void salvarMarca() {
        marca = marcas.atualizar(marca);
        getObjeto().setProdutoMarca(marca);
    }

    public void addAlmoxarifado() {
        almoxarifado = new Almoxarifado();
    }

    public void salvarAlmoxarifado() {
        almoxarifado = almoxarifados.atualizar(almoxarifado);
        getObjeto().setAlmoxarifado(almoxarifado);
    }

    public void addGrupo() {
        grupo = new ProdutoGrupo();

    }

    public void salvarGrupo() {


        boolean existe = grupos.existeRegisro(ProdutoGrupo.class, "nome", grupo.getNome());

        if (existe) {
            Mensagem.addErrorMessage("Já existe grupo com esse nome");
        } else {
            grupo = grupos.atualizar(grupo);
        }


    }

    public void addSubGrupo() {
        subGrupo = new ProdutoSubGrupo();
        subGrupo.setProdutoGrupo(grupo);
    }

    public void addConversao() {


        try {

            if (conversoes.stream().filter(u -> u.getSigla().equals(unidadeProduto.getSigla())).findAny().isPresent()) {
                Mensagem.addErrorMessage("unidade de conversão já adcionada");
            } else {
                unidadeConversao = new UnidadeConversao();
                unidadeConversao.setProduto(getObjeto());
                unidadeConversao.setUnidadeProduto(unidadeProduto);
                unidadeConversao.setSigla(unidadeProduto.getSigla());
                unidadeConversao.setAcao(acao);
                unidadeConversao.setDescricao("Converter " + getObjeto().getUnidadeProduto().getSigla() + " para " + unidadeProduto.getSigla());
                unidadeConversao.setFatorConversao(fator);
                unidadeConversaoRepository.salvar(unidadeConversao);
                conversoes.add(unidadeConversao);
                fator = BigDecimal.ZERO;


            }


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Erro ao savar a unidade de conversão", ex);
            }
        }


    }

    public void removerUndiadeConversao() {
        try {
            unidadeConversaoRepository.excluir(UnidadeConversao.class, "id", unidadeConversaoSelecionada.getId());
            conversoes.remove(unidadeConversaoSelecionada);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao remover a undiade de conversão", ex);
        }
    }


    public void salvarSubgrupo() {
        subGrupo.setProdutoGrupo(grupo);
        subGrupo = subGrupos.atualizar(subGrupo);
        getObjeto().setProdutoSubGrupo(subGrupo);
    }

    public void exibirPesquisaNcm() {
        ncms = new ArrayList<>();
        ncm = "";
    }

    public void exibirPesquisaCest() {
        cests = new ArrayList<>();
        cest = StringUtils.isEmpty(getObjeto().getNcm()) ? "" : getObjeto().getNcm();
        if (cest.length() > 0) {
            pesquisarCest();
        }

    }

    public void pesquisarNcm() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "descricao", Filtro.LIKE, ncm));
        filtros.add(new Filtro(Filtro.OR, "codigo", Filtro.LIKE, ncm));

        ncms = ncmRepository.getEntitys(Ncm.class, filtros);

    }

    public void pesquisarCest() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "descricaoCest", Filtro.LIKE, cest));
        filtros.add(new Filtro(Filtro.OR, "cest", Filtro.LIKE, cest));
        filtros.add(new Filtro(Filtro.OR, "ncmSh", Filtro.LIKE, cest));

        cests = cestRepository.getEntitys(Cest.class, filtros);

    }

    public void selecionarNcm() {
        getObjeto().setNcm(ncmSelecionado.getCodigo());
        ncm = "";
    }

    public void selecionarCest() {
        getObjeto().setCest(cestSelecionado.getCest());
        cest = "";
    }

    private List<Produto> buscarProdutosBalanca() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "codigoBalanca", Filtro.NAO_NULO, ""));
        filtros.add(new Filtro("unidadeProduto.podeFracionar", "S"));
        return dao.getEntitys(Produto.class, filtros, new Object[]{"nome", "valorVenda", "codigoBalanca"});
    }


    @Override
    protected Class<Produto> getClazz() {
        return Produto.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public ProdutoGrupo getGrupo() {
        return grupo;
    }

    public void setGrupo(ProdutoGrupo grupo) {
        this.grupo = grupo;
    }


    public List<EmpresaProduto> getListProdutoEmpresa() {
        return listProdutoEmpresa;
    }

    public void setListProdutoEmpresa(List<EmpresaProduto> listProdutoEmpresa) {
        this.listProdutoEmpresa = listProdutoEmpresa;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getStrGrupo() {
        return strGrupo;
    }

    public void setStrGrupo(String strGrupo) {
        this.strGrupo = strGrupo;
    }

    public String getStrSubGrupo() {
        return strSubGrupo;
    }

    public void setStrSubGrupo(String strSubGrupo) {
        this.strSubGrupo = strSubGrupo;
    }

    public String getInativo() {
        return inativo;
    }

    public void setInativo(String inativo) {
        this.inativo = inativo;
    }

    public ViewProdutoEmpresa getProdutoSelecionado() {
        return produtoSelecionado;
    }

    public void setProdutoSelecionado(ViewProdutoEmpresa produtoSelecionado) {
        this.produtoSelecionado = produtoSelecionado;
    }

    public String getNomeFoto() {
        return nomeFoto;
    }

    public void setNomeFoto(String nomeFoto) {
        this.nomeFoto = nomeFoto;
    }

    public ProdutoMarca getMarca() {
        return marca;
    }

    public void setMarca(ProdutoMarca marca) {
        this.marca = marca;
    }

    public Almoxarifado getAlmoxarifado() {
        return almoxarifado;
    }

    public void setAlmoxarifado(Almoxarifado almoxarifado) {
        this.almoxarifado = almoxarifado;
    }

    public ProdutoSubGrupo getSubGrupo() {
        return subGrupo;
    }

    public void setSubGrupo(ProdutoSubGrupo subGrupo) {
        this.subGrupo = subGrupo;
    }


    public List<UnidadeConversao> getConversoes() {
        return conversoes;
    }

    public void setConversoes(List<UnidadeConversao> conversoes) {
        this.conversoes = conversoes;
    }

    public UnidadeProduto getUnidadeProduto() {
        return unidadeProduto;
    }

    public void setUnidadeProduto(UnidadeProduto unidadeProduto) {
        this.unidadeProduto = unidadeProduto;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public BigDecimal getFator() {
        return fator;
    }

    public void setFator(BigDecimal fator) {
        this.fator = fator;
    }

    public UnidadeConversao getUnidadeConversaoSelecionada() {
        return unidadeConversaoSelecionada;
    }

    public void setUnidadeConversaoSelecionada(UnidadeConversao unidadeConversaoSelecionada) {
        this.unidadeConversaoSelecionada = unidadeConversaoSelecionada;
    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
    }

    public List<Ncm> getNcms() {
        return ncms;
    }

    public void setNcms(List<Ncm> ncms) {
        this.ncms = ncms;
    }

    public Ncm getNcmSelecionado() {
        return ncmSelecionado;
    }

    public void setNcmSelecionado(Ncm ncmSelecionado) {
        this.ncmSelecionado = ncmSelecionado;
    }

    public String getNcm() {
        return ncm;
    }

    public void setNcm(String ncm) {
        this.ncm = ncm;
    }

    public PdvConfiguracaoBalanca getConfiguracaoBalanca() {
        return configuracaoBalanca;
    }

    public void setConfiguracaoBalanca(PdvConfiguracaoBalanca configuracaoBalanca) {
        this.configuracaoBalanca = configuracaoBalanca;
    }

    public List<PdvConfiguracaoBalanca> getConfiguracoesBalanca() {
        return configuracoesBalanca;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public Integer getIdmepresaFiltro() {
        return idmepresaFiltro;
    }

    public void setIdmepresaFiltro(Integer idmepresaFiltro) {
        this.idmepresaFiltro = idmepresaFiltro;
    }

    public List<Empresa> getEmpresasSelecionada() {
        return empresasSelecionada;
    }

    public void setEmpresasSelecionada(List<Empresa> empresasSelecionada) {
        this.empresasSelecionada = empresasSelecionada;
    }

    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }

    public boolean isListaEmpresaEmpty() {
        return listaEmpresas.isEmpty();
    }

    public EstoqueCor getCor() {
        return cor;
    }

    public void setCor(EstoqueCor cor) {
        this.cor = cor;
    }

    public EstoqueTamanho getTamanho() {
        return tamanho;
    }

    public void setTamanho(EstoqueTamanho tamanho) {
        this.tamanho = tamanho;
    }

    public List<EstoqueGrade> getGrades() {
        return grades;
    }

    public FichaTecnica getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(FichaTecnica fichaTecnica) {
        this.fichaTecnica = fichaTecnica;
    }

    public FichaTecnica getFichaTecnicaSelecionado() {
        return fichaTecnicaSelecionado;
    }

    public void setFichaTecnicaSelecionado(FichaTecnica fichaTecnicaSelecionado) {
        this.fichaTecnicaSelecionado = fichaTecnicaSelecionado;
    }

    public MapNomeIdDTO getProdutoFichaTecnica() {
        return produtoFichaTecnica;
    }

    public void setProdutoFichaTecnica(MapNomeIdDTO produtoFichaTecnica) {
        this.produtoFichaTecnica = produtoFichaTecnica;
    }

    public Cest getCestSelecionado() {
        return cestSelecionado;
    }

    public void setCestSelecionado(Cest cestSelecionado) {
        this.cestSelecionado = cestSelecionado;
    }

    public String getCest() {
        return cest;
    }

    public void setCest(String cest) {
        this.cest = cest;
    }

    public List<Cest> getCests() {
        return cests;
    }
}
