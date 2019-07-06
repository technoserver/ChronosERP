/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.TelaPessoa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author john
 */

@Named
@ViewScoped
public class ColaboradorControll extends PessoaControll<Colaborador> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<TipoAdmissao> tipoAdmissoes;
    @Inject
    private Repository<SituacaoColaborador> situacoesColaborador;
    @Inject
    private Repository<TipoColaborador> tipoColaboradores;
    @Inject
    private Repository<NivelFormacao> nivesFormacao;
    @Inject
    private Repository<Cargo> cargos;
    @Inject
    private Repository<Setor> setores;
    @Inject
    private Repository<Pessoa> pessoas;


    private Cargo cargo;
    private Setor setor;

    private Map<String, Integer> listaTipoAdmissao;
    private Map<String, Integer> listaSituacao;
    private Map<String, Integer> listaTipo;

    private Map<String, Integer> listaNivelFormacao;
    private Integer idnivelFormacao;
    private Integer idtipoAdimissao;
    private Integer idsituacao;
    private Integer idtipo;

    private String completo;

    @Inject
    private PessoaService service;

    private String nome, situacao, nomeCargo, nomeSetor;


    @Override
    public ERPLazyDataModel<Colaborador> getDataModel() {
        if (dataModel == null) {
            dataModel = new ERPLazyDataModel<>();
            dataModel.setDao(dao);
            dataModel.setClazz(Colaborador.class);

        }
        dataModel.getFiltros().clear();
        dataModel.addFiltro("pessoa.id", 1, Filtro.DIFERENTE);
        dataModel.addFiltro("pessoa.id", 2, Filtro.DIFERENTE);
        pesquisar();
        dataModel.setAtributos(new Object[]{"pessoa.id", "pessoa.nome", "matricula", "situacaoColaborador.nome", "cargo.nome", "setor.nome"});
        return dataModel;
    }

    public void pesquisar() {

        if (!StringUtils.isEmpty(nome)) {
            dataModel.getFiltros().add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
        }

        if (!StringUtils.isEmpty(nomeCargo)) {
            dataModel.getFiltros().add(new Filtro("cargo.nome", Filtro.LIKE, nomeCargo));
        }

        if (!StringUtils.isEmpty(nomeSetor)) {
            dataModel.getFiltros().add(new Filtro("setor.nome", Filtro.LIKE, nomeSetor));
        }


    }

    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("N", "S", "N", "N");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDataAdmissao(new Date());
        getObjeto().setDataCadastro(new Date());
        completo = "S";
        iniciarObjetos();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        Colaborador colaborador = dao.get(getObjeto().getId(), Colaborador.class);

        setObjeto(colaborador);

        idnivelFormacao = colaborador.getNivelFormacao().getId();
        idtipoAdimissao = colaborador.getTipoAdmissao() != null ? colaborador.getTipoAdmissao().getId() : 1;
        idsituacao = colaborador.getSituacaoColaborador().getId();
        idtipo = colaborador.getTipoColaborador().getId();
        iniciarObjetos();
    }

    @Override
    public void salvar() {

        Colaborador colaborador = null;
        try {

            if (completo.equals("S")) {
                Empresa ep = getObjeto().getId() != null ? empresa : emp;

                getObjeto().setNivelFormacao(new NivelFormacao(idnivelFormacao));
                getObjeto().setTipoAdmissao(new TipoAdmissao(idtipoAdimissao));
                getObjeto().setSituacaoColaborador(new SituacaoColaborador(idsituacao));
                getObjeto().setTipoColaborador(new TipoColaborador(idtipo));
                colaborador = service.salvarColaborador(getObjeto(), ep);
                setObjeto(colaborador);

            } else {
                Pessoa pessoa = pessoas.getJoinFetch(getObjeto().getPessoa().getId(), Pessoa.class);
                pessoa.setColaborador("S");

                getObjeto().setPessoa(pessoa);

                getObjeto().setDataCadastro(new Date());
                getObjeto().setDataAdmissao(new Date());
                getObjeto().setSituacaoColaborador(new SituacaoColaborador(1));
                getObjeto().setTipoAdmissao(new TipoAdmissao(1));
                getObjeto().setNivelFormacao(new NivelFormacao(1));
                getObjeto().setTipoColaborador(new TipoColaborador(1));

                dao.atualizar(getObjeto());
            }

            Mensagem.addInfoMessage("Colaborador salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }

    }

    public void addCargo() {
        cargo = new Cargo();
        cargo.setEmpresa(empresa);

    }

    public void salvarCargo() {
        cargo = cargos.atualizar(cargo);
        getObjeto().setCargo(cargo);
        setActiveTabIndex(1);
        Mensagem.addInfoMessage("Cargo adicionado com sucesso");
    }

    public void addSetor() {
        setor = new Setor();
        setor.setEmpresa(empresa);
    }

    public void salvarSetor() {
        setor = setores.atualizar(setor);
        getObjeto().setSetor(setor);
        setActiveTabIndex(1);
        Mensagem.addInfoMessage("Setor adicionado com sucesso");
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {

            listaPessoa = pessoas.getEntitys(Pessoa.class, "nome", nome, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPessoa;
    }

    public List<TipoAdmissao> getListaTipoAdmissao(String nome) {
        List<TipoAdmissao> listaTipoAdmissao = new ArrayList<>();
        try {
            listaTipoAdmissao = tipoAdmissoes.getEntitys(TipoAdmissao.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoAdmissao;
    }

    public List<SituacaoColaborador> getListaSituacaoColaborador(String nome) {
        List<SituacaoColaborador> listaSituacaoColaborador = new ArrayList<>();
        try {
            listaSituacaoColaborador = situacoesColaborador.getEntitys(SituacaoColaborador.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoColaborador;
    }

    public List<TipoColaborador> getListaTipoColaborador(String nome) {
        List<TipoColaborador> listaTipoColaborador = new ArrayList<>();
        try {
            listaTipoColaborador = tipoColaboradores.getEntitys(TipoColaborador.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTipoColaborador;
    }

    public List<NivelFormacao> getListaNivelFormacao(String nome) {
        List<NivelFormacao> listaNivelFormacao = new ArrayList<>();
        try {
            listaNivelFormacao = nivesFormacao.getEntitys(NivelFormacao.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaNivelFormacao;
    }

    public List<Cargo> getListaCargo(String nome) {
        List<Cargo> listaCargo = new ArrayList<>();
        try {
            listaCargo = cargos.getEntitys(Cargo.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaCargo;
    }

    public List<Setor> getListaSetor(String nome) {
        List<Setor> listaSetor = new ArrayList<>();
        try {
            listaSetor = setores.getEntitys(Setor.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSetor;
    }


    private void iniciarObjetos() {

        listaSituacao = new LinkedHashMap<>();
        listaTipoAdmissao = new LinkedHashMap<>();
        listaNivelFormacao = new LinkedHashMap<>();
        listaTipo = new LinkedHashMap<>();

        listaNivelFormacao.put("Analfabeto", 1);
        listaNivelFormacao.put("Até 4ª série incompleta do 1º grau (ensino fundamental)", 2);
        listaNivelFormacao.put("4ª série completa do 1º grau (ensino fundamental)", 3);
        listaNivelFormacao.put("5ª a 8ª série incompleta do 1º grau (ensino fundamental)", 4);
        listaNivelFormacao.put("1º grau completo (ensino fundamental)", 5);
        listaNivelFormacao.put("2º grau incompleto (ensino médio)", 6);
        listaNivelFormacao.put("2º grau completo (ensino médio)", 7);
        listaNivelFormacao.put("Superior Incompleto", 8);
        listaNivelFormacao.put("Superior Completo", 9);
        listaNivelFormacao.put("Pós-Graduação/Especialização", 10);
        listaNivelFormacao.put("Mestrado", 11);
        listaNivelFormacao.put("Doutorado", 12);
        listaNivelFormacao.put("Pós-Doutorado", 13);


        listaTipoAdmissao.put("PRIMEIRO EMPREGO", 1);
        listaTipoAdmissao.put("REEMPREGO", 2);
        listaTipoAdmissao.put("CONTRATO POR PRAZO DETERMINADO", 3);
        listaTipoAdmissao.put("REINTEGRACAO", 4);
        listaTipoAdmissao.put("TRANSFERENCIA", 5);


        listaSituacao.put("ATIVO", 1);
        listaSituacao.put("INATIVO", 2);

        listaTipo.put("EMPREGADO", 1);
        listaTipo.put("REPRESENTANTE", 2);
    }


    @Override
    public Pessoa getPessoa() {
        return getObjeto().getPessoa();
    }

    @Override
    public void setPessoa(Pessoa pessoa) {
        getObjeto().setPessoa(pessoa);
    }

    @Override
    public String getTela() {
        return TelaPessoa.COLABORADOR.getCodigo();
    }

    @Override
    protected Class<Colaborador> getClazz() {
        return Colaborador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "COLABORADOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }


    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Integer getIdsituacao() {
        return idsituacao;
    }

    public void setIdsituacao(Integer idsituacao) {
        this.idsituacao = idsituacao;
    }

    public Integer getIdnivelFormacao() {
        return idnivelFormacao;
    }

    public void setIdnivelFormacao(Integer idnivelFormacao) {
        this.idnivelFormacao = idnivelFormacao;
    }

    public Integer getIdtipoAdimissao() {
        return idtipoAdimissao;
    }

    public void setIdtipoAdimissao(Integer idtipoAdimissao) {
        this.idtipoAdimissao = idtipoAdimissao;
    }

    public Integer getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(Integer idtipo) {
        this.idtipo = idtipo;
    }

    public Map<String, Integer> getListaTipo() {
        return listaTipo;
    }

    public Map<String, Integer> getListaNivelFormacao() {
        return listaNivelFormacao;
    }

    public Map<String, Integer> getListaTipoAdmissao() {
        return listaTipoAdmissao;
    }

    public Map<String, Integer> getListaSituacao() {
        return listaSituacao;
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

    public String getNomeCargo() {
        return nomeCargo;
    }

    public void setNomeCargo(String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    public String getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(String nomeSetor) {
        this.nomeSetor = nomeSetor;
    }
}
