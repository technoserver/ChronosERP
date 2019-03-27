/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.controll.cadastros.datamodel.ProdutoEmpresaDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.view.ViewProdutoEmpresa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.service.cadastros.ProdutoService;
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.Mensagem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

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
    private Repository<TabelaNutricionalCabecalho> tabelaNutricionalRepository;

    @Inject
    private Repository<PdvConfiguracaoBalanca> pdvConfiguracaoBalancaRepository;
    @Inject
    private Repository<ProdutoGrade> gradeRepository;

    @Inject
    private ProdutoService service;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;

    private ProdutoGrupo grupo;
    private ProdutoEmpresaDataModel produtoDataModel;
    private List<EmpresaProduto> listProdutoEmpresa;
    private List<Empresa> empresas;
    private ViewProdutoEmpresa produtoSelecionado;

    private Integer codigo;
    private String gtin;
    private String produto;
    private String strGrupo;
    private String strSubGrupo;
    private String inativo;
    private String nomeProdutoOld;
    private String nomeFoto;
    private String ncm;

    private ProdutoMarca marca;
    private Almoxarifado almoxarifado;
    private ProdutoSubGrupo subGrupo;

    private UnidadeConversao unidadeConversao;
    private UnidadeConversao unidadeConversaoSelecionada;
    private List<UnidadeConversao> conversoes;
    private List<Ncm> ncms;
    private Ncm ncmSelecionado;

    @NotNull(message = "Unidade de conversão obrigatória")
    private UnidadeProduto unidadeProduto;
    private String acao;
    @NotNull(message = "Fator de conversão obrigatório")
    @DecimalMin(value = "0.01", message = "valor mínimo de conversão 0.01")
    private BigDecimal fator;

    private int idempresa;
    private Map<String, Integer> listaEmpresas;

    private PdvConfiguracaoBalanca configuracaoBalanca;

    private List<PdvConfiguracaoBalanca> configuracoesBalanca;

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

        if (!StringUtils.isEmpty(inativo)) {
            produtoDataModel.addFiltro("inativo", inativo, Filtro.IGUAL);
        }

        if (!StringUtils.isEmpty(codigo)) {
            produtoDataModel.addFiltro("id", codigo, Filtro.IGUAL);
        }

        if (!StringUtils.isEmpty(gtin)) {
            produtoDataModel.addFiltro("gtin", gtin, Filtro.IGUAL);
        }

        produtoDataModel.addFiltro("excluido", "N", Filtro.IGUAL);
        produtoDataModel.addFiltro("idempresa", empresa.getId(), Filtro.IGUAL);
    }

    @Override
    public ERPLazyDataModel<Produto> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setClazz(getClazz());
            dataModel.setDao(dao);
            joinFetch = new Object[]{"produtoMarca"};
            Object[] atribs = new Object[]{"nome", "valorVenda", "quantidadeEstoque", "unidadeProduto"};
            dataModel.setAtributos(atribs);
            dataModel.setJoinFetch(joinFetch);

        }
        dataModel.addFiltro("excluido", "N", Filtro.IGUAL);
        if (dataModel.getFiltros().isEmpty()) {
            dataModel.addFiltro("inativo", "N", Filtro.IGUAL);
        }

        return dataModel;
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

    @Override
    public void doCreate() {
        super.doCreate(); //To change body of generated methods, choose Tools | Templates.
        getObjeto().setExcluido("N");
        getObjeto().setInativo("N");
        getObjeto().setPossuiGrade(false);
        getObjeto().setDataCadastro(new Date());
        grupo = new ProdutoGrupo();
        conversoes = new ArrayList<>();

        listaEmpresas = new LinkedHashMap<>();

        List<EmpresaPessoa> empresaPessoas = empresaPessoaRepository.getEntitys(EmpresaPessoa.class, "pessoa.id", usuario.getIdpessoa(), new Object[]{"empresa.id, empresa.razaoSocial"});

        if (!empresaPessoas.isEmpty() && empresaPessoas.size() > 1) {

            listaEmpresas.put("Todas", 0);
            for (EmpresaPessoa emp : empresaPessoas) {
                listaEmpresas.put(emp.getEmpresa().getRazaoSocial(), emp.getEmpresa().getId());
            }


        }
    }

    @Override
    public void doEdit() {
        super.doEdit();
        Produto produto = dao.getJoinFetch(produtoSelecionado.getId(), Produto.class);
        setObjeto(produto);
        grupo = getObjeto().getProdutoSubGrupo().getProdutoGrupo();
        nomeProdutoOld = getObjeto().getNome();
        nomeFoto = getObjeto().getImagem();
        conversoes = unidadeConversaoRepository.getEntitys(UnidadeConversao.class, "produto.id", getObjeto().getId(), new Object[]{"sigla", "fatorConversao", "acao"});
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
            empresas = new ArrayList<>();
            if (getObjeto().getId() == null) {
                if (idempresa == 0 && listaEmpresas.size() > 1) {
                    for (Integer id : listaEmpresas.values()) {
                        if (id > 0) {
                            empresas.add(new Empresa(id));
                        }

                    }
                } else {
                    empresas.add(empresa);
                }
            } else {
                empresas.add(empresa);
            }

            setObjeto(service.salvar(getObjeto(), empresas));
            Mensagem.addInfoMessage("Registro salvo com sucesso");
        } catch (Exception ex) {

            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("Ocorreu um erro ao salvar o registro!", ex);
            }

        }


    }

    public void copiar() {

        try {
            Produto produto = new Produto();
            doEdit();
            BeanUtils.copyProperties(getObjeto(), produto, "id", "gtin");
            listaEmpresas = new HashMap<>();
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
        grupo = grupos.atualizar(grupo);
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

    public void pesquisarNcm() {
        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro(Filtro.AND, "descricao", Filtro.LIKE, ncm));
        filtros.add(new Filtro(Filtro.OR, "codigo", Filtro.LIKE, ncm));

        ncms = ncmRepository.getEntitys(Ncm.class, filtros);

    }

    public void selecionarNcm() {
        getObjeto().setNcm(ncmSelecionado.getCodigo());
        ncm = "";
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

    public Map<String, Integer> getListaEmpresas() {
        return listaEmpresas;
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
}
