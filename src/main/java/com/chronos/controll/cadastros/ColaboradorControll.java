/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.TelaPessoa;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String completo;

    @Inject
    private PessoaService service;


    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("N", "S", "N", "N");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDataAdmissao(new Date());
        getObjeto().setDataCadastro(new Date());
        completo = "S";
    }

    @Override
    public void salvar() {

        Colaborador colaborador = null;
        try {
            colaborador = service.salvarColaborador(getObjeto(), empresa);
            setObjeto(colaborador);
            Mensagem.addInfoMessage("Colaborador salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }

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
        return TelaPessoa.CLIENTE.getCodigo();
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
}
