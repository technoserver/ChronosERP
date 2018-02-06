/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.cadastros.datamodel.ClienteDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.enuns.TelaPessoa;
import com.chronos.modelo.entidades.view.PessoaCliente;
import com.chronos.repository.Filtro;
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
 * @author john
 */
@Named
@ViewScoped
public class ClienteControll extends PessoaControll<Cliente> implements Serializable {

    private static final long serialVersionUID = 1L;

    private ClienteDataModel clienteDataModel;
    @Inject
    private Repository<PessoaCliente> clientes;
    @Inject
    private Repository<Regiao> regioes;
    @Inject
    private Repository<TabelaPreco> tabelasPreco;
    @Inject
    private Repository<Convenio> convenios;
    @Inject
    private Repository<TributOperacaoFiscal> operacoesFiscais;
    @Inject
    private Repository<AtividadeForCli> atividadesClientes;
    @Inject
    private Repository<SituacaoForCli> situacoesCliente;
    @Inject
    private Repository<Pessoa> pessoas;

    @Inject
    private PessoaService service;



    private PessoaCliente clienteSelecionado;

    private String completo;

    public ClienteDataModel getClienteDataModel() {

        if (clienteDataModel == null) {
            clienteDataModel = new ClienteDataModel();
            clienteDataModel.setDao(clientes);
            clienteDataModel.setClazz(PessoaCliente.class);
        }

        return clienteDataModel;
    }


    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("S","N","N","N");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDesde(new Date());
        getObjeto().setBloqueado("N");
        completo = "N";

    }

    @Override
    public void doEdit() {

        Cliente cliente = dao.getJoinFetch(clienteSelecionado.getId(), Cliente.class);
        setObjeto(cliente);
        super.doEdit();
        setTelaGrid(false);
        completo = "N";
    }

    @Override
    public void salvar() {

        Cliente cliente = null;
        try {
            if (completo.equals("S")) {
                cliente = service.salvarCliente(getObjeto(), empresa);
                setObjeto(cliente);

            } else {

                Pessoa pessoa = pessoas.getJoinFetch(getObjeto().getPessoa().getId(), Pessoa.class);
                pessoa.setCliente("S");

                getObjeto().setPessoa(pessoa);
                dao.atualizar(getObjeto());
            }
            setTelaGrid(true);
            Mensagem.addInfoMessage("Cliente salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }

    }

    @Override
    public void remover() {
        Cliente cliente = dataModel.getRowData(String.valueOf(clienteSelecionado.getId()));
        setObjetoSelecionado(cliente);
        super.remover();
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            listaPessoa = pessoas.getEntitys(Pessoa.class, filtros, atributos);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return listaPessoa;
    }


    public List<Regiao> getListaRegiao(String nome) {
        List<Regiao> listaRegiao = new ArrayList<>();
        try {
            listaRegiao = regioes.getEntitys(Regiao.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaRegiao;
    }

    public List<TabelaPreco> getListaTabelaPreco(String nome) {
        List<TabelaPreco> listaTabelaPreco = new ArrayList<>();
        try {
            listaTabelaPreco = tabelasPreco.getEntitys(TabelaPreco.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTabelaPreco;
    }

    public List<Convenio> getListaConvenio(String nome) {
        List<Convenio> listaConvenio = new ArrayList<>();
        try {
            listaConvenio = convenios.getEntitys(Convenio.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaConvenio;
    }

    public List<TributOperacaoFiscal> getListaOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaOperacaoFiscal = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaOperacaoFiscal = operacoesFiscais.getEntitys(TributOperacaoFiscal.class, "descricao", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOperacaoFiscal;
    }

    public List<AtividadeForCli> getListaAtividadeForCli(String nome) {
        List<AtividadeForCli> listaAtividadeForCli = new ArrayList<>();
        try {
            listaAtividadeForCli = atividadesClientes.getEntitys(AtividadeForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAtividadeForCli;
    }

    public List<SituacaoForCli> getListaSituacaoForCli(String nome) {
        List<SituacaoForCli> listaSituacaoForCli = new ArrayList<>();
        try {
            listaSituacaoForCli = situacoesCliente.getEntitys(SituacaoForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoForCli;
    }





    public void instanciaPessoaFisica(){
        Pessoa pessoa = instanciaPessoaFisica(getObjeto().getPessoa());
        getObjeto().setPessoa(pessoa);
    }

    public void instanciaPessoaJuridica(){
        Pessoa pessoa = instanciaPessoaJuridica(getObjeto().getPessoa());
        getObjeto().setPessoa(pessoa);
    }

    @Override
    protected Class<Cliente> getClazz() {
        return Cliente.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "CLIENTE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public PessoaCliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(PessoaCliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
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
}
