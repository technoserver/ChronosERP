/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.modelo.entidades.AtividadeForCli;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.SituacaoForCli;
import com.chronos.modelo.entidades.enuns.TelaPessoa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
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
public class FornecedorControll extends PessoaControll<Fornecedor> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<AtividadeForCli> atividades;
    @Inject
    private Repository<SituacaoForCli> situacoes;
    @Inject
    private Repository<Pessoa> pessoas;
    @Inject
    private PessoaService service;

    private String completo;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        completo = "N";
    }

    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("N", "N", "N", "S");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDesde(new Date());

        completo = "N";
    }


    @Override
    public void salvar() {
        Fornecedor fornecedor = null;
        try {
            if (completo.equals("S")) {
                fornecedor = service.salvarFornecedor(getObjeto(), empresa);
                setObjeto(fornecedor);
            } else {
                fornecedor = getObjeto();
                Pessoa pessoa = pessoas.getJoinFetch(fornecedor.getPessoa().getId(), Pessoa.class);
                fornecedor.setPessoa(pessoa);
                fornecedor.getPessoa().setFornecedor("S");
                fornecedor = dao.atualizar(fornecedor);

            }
            Mensagem.addInfoMessage("Fornecedor salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("id", Filtro.DIFERENTE, 1));
            filtros.add(new Filtro("id", Filtro.DIFERENTE, 2));
            listaPessoa = pessoas.getEntitys(Pessoa.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPessoa;
    }

    public List<AtividadeForCli> getListaAtividadeForCli(String nome) {
        List<AtividadeForCli> listaAtividadeForCli = new ArrayList<>();
        try {
            listaAtividadeForCli = atividades.getEntitys(AtividadeForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAtividadeForCli;
    }

    public List<SituacaoForCli> getListaSituacaoForCli(String nome) {
        List<SituacaoForCli> listaSituacaoForCli = new ArrayList<>();
        try {
            listaSituacaoForCli = situacoes.getEntitys(SituacaoForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoForCli;
    }


    @Override
    protected Class<Fornecedor> getClazz() {
        return Fornecedor.class;
    }

    @Override
    protected String getFuncaoBase() {

        return "FORNECEDOR";
    }

    @Override
    protected boolean auditar() {
        return false;
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

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }
}
