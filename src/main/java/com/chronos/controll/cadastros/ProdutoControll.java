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
import com.chronos.util.ArquivoUtil;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.jsf.Mensagem;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.Visibility;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private ProdutoGrupo grupo;
    private ProdutoEmpresaDataModel produtoDataModel;
    private List<EmpresaProduto> listProdutoEmpresa;
    private ViewProdutoEmpresa produtoSelecionado;

    private String produto;
    private String strGrupo;
    private String strSubGrupo;
    private String inativo;
    private String nomeProdutoOld;
    private String nomeFoto;

    private ProdutoMarca marca;
    private Almoxarifado almoxarifado;
    private ProdutoSubGrupo subGrupo;

    private UnidadeConversao unidadeConversao;
    private UnidadeConversao unidadeConversaoSelecionada;
    private List<UnidadeConversao> conversoes;
    @NotNull(message = "Unidade de conversão obrigatória")
    private UnidadeProduto unidadeProduto;
    private String acao;
    @NotNull(message = "Fator de conversão obrigatório")
    @DecimalMin(value = "0.01", message = "valor mínimo de conversão 0.01")
    private BigDecimal fator;


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
        getObjeto().setDataCadastro(new Date());
        grupo = new ProdutoGrupo();
        conversoes = new ArrayList<>();

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
            if (getObjeto().getTributGrupoTributario() == null) {
                Mensagem.addWarnMessage("É necesário informar o Grupo Tributário OU o ICMS Customizado.");
            } else {
                List<Filtro> filtros = new ArrayList<>();
                filtros.add(new Filtro(Filtro.AND, "gtin", Filtro.IGUAL, getObjeto().getGtin()));
                if (getObjeto().getId() != null) {
                    filtros.add(new Filtro(Filtro.AND, "id", Filtro.DIFERENTE, getObjeto().getId()));
                }
                Produto p = StringUtils.isEmpty(getObjeto().getGtin()) ? null : dao.get(Produto.class, filtros);
                if (p != null) {
                    Mensagem.addWarnMessage("Este GTIN já está sendo utilizado por outro produto.");
                } else {
                    if (StringUtils.isEmpty(getObjeto().getDescricaoPdv())) {
                        String nomePdv = getObjeto().getNome().length() > 30 ? getObjeto().getNome().substring(0, 30) : getObjeto().getNome();
                        getObjeto().setDescricaoPdv(nomePdv);
                    }
                    if (!StringUtils.isEmpty(getObjeto().getImagem())) {
                        ArquivoUtil.getInstance().salvarFotoProduto(getObjeto().getImagem());
                    }
                    if (getObjeto().getId() == null) {
                        super.salvar(null);
                        EmpresaProduto produtoEmpresa = new EmpresaProduto();
                        produtoEmpresa.setEmpresa(empresa);
                        produtoEmpresa.setProduto(getObjeto());
                        produtoEmpresa.setQuantidadeEstoque(BigDecimal.ZERO);
                        produtoEmpresa.setEstoqueVerificado(BigDecimal.ZERO);
                        produtosEmpresa.salvar(produtoEmpresa);
                    } else {
                        getObjeto().setDataAlteracao(new Date());
                        super.salvar(null);
                        //TODO verificar o fluxo de salva produt alterado.
                        if (nomeProdutoOld != null && !nomeProdutoOld.equals(getObjeto().getNome())) {
                            ProdutoAlteracaoItem produtoAlteracao = new ProdutoAlteracaoItem();

                        }
                    }


                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("Ocorreu um erro ao salvar o registro!", ex);
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


    public void gerarTxtToledo() {


        try {
            List<Produto> produtos = buscarProdutosBalanca();
            if (!produtos.isEmpty()) {
                File file = File.createTempFile("ITENSMGV", ".txt");

                FileWriter writer = new FileWriter(file);

                int i = 0;
                for (Produto p : produtos) {

                    String item = p.montarItemBalancaToledo();
                    if ((produtos.size() - 1) > i) {
                        item += "\r\n";
                    }
                    writer.write(item);
                    i++;
                }
                writer.close();
                //FileUtils.writeLines(file, linhas);

                FacesUtil.downloadArquivo(file, "ITENSMGV.txt");
            } else {
                Mensagem.addInfoMessage("Não foram encontrados produtos com codigo de balança e que podem ser fracionado");
            }

        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            } else {
                throw new RuntimeException("erro ao gera dados para balança", ex);
            }

        }
    }

    public void gerarTxtFilizola() {


        try {
            List<Produto> produtos = buscarProdutosBalanca();
            if (!produtos.isEmpty()) {
                File file = File.createTempFile("CADTXT", ".txt");

                List<String> linhas = new ArrayList<>();
                for (Produto p : produtos) {
                    linhas.add(p.montarItemBalancaFilizola());
                }
                FileUtils.writeLines(file, linhas);
                FacesUtil.downloadArquivo(file, "CADTXT.txt");
            } else {
                Mensagem.addInfoMessage("Não foram encontrados produtos com codigo de balança e que podem ser fracionado");
            }

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
}
