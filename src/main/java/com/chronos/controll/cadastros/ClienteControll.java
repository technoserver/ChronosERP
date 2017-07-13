/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.ERPLazyDataModel;
import com.chronos.controll.cadastros.datamodel.ClienteDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.PessoaCliente;
import com.chronos.repository.Repository;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class ClienteControll extends AbstractControll<Cliente> implements Serializable {

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

    private PessoaCliente clienteSelecionado;

    public ClienteDataModel getClienteDataModel() {

        if(clienteDataModel==null){
            clienteDataModel = new ClienteDataModel();
            clienteDataModel.setDao(clientes);
            clienteDataModel.setClazz(PessoaCliente.class);
        }

        return clienteDataModel;
    }

    @Override
    public void doEdit() {
        Cliente cliente = dao.getJoinFetch(clienteSelecionado.getId(),Cliente.class);
        setObjeto(cliente);
        setTelaGrid(false);
    }



    public List<Regiao> getListaRegiao(String nome) {
        List<Regiao> listaRegiao = new ArrayList<>();
        try {
            listaRegiao = regioes.getEntitys(Regiao.class, "nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaRegiao;
    }

    public List<TabelaPreco> getListaTabelaPreco(String nome) {
        List<TabelaPreco> listaTabelaPreco = new ArrayList<>();
        try {
            listaTabelaPreco = tabelasPreco.getEntitys(TabelaPreco.class, "nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaTabelaPreco;
    }

    public List<Convenio> getListaConvenio(String nome) {
        List<Convenio> listaConvenio = new ArrayList<>();
        try {
            listaConvenio = convenios.getEntitys(Convenio.class, "nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaConvenio;
    }

    public List<TributOperacaoFiscal> getListaOperacaoFiscal(String nome) {
        List<TributOperacaoFiscal> listaOperacaoFiscal = new ArrayList<>();
        try {
            atributos = new Object[]{"descricao"};
            listaOperacaoFiscal = operacoesFiscais.getEntitys(TributOperacaoFiscal.class, "descricao", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaOperacaoFiscal;
    }

    public List<AtividadeForCli> getListaAtividadeForCli(String nome) {
        List<AtividadeForCli> listaAtividadeForCli = new ArrayList<>();
        try {
            listaAtividadeForCli = atividadesClientes.getEntitys(AtividadeForCli.class, "nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAtividadeForCli;
    }

    public List<SituacaoForCli> getListaSituacaoForCli(String nome) {
        List<SituacaoForCli> listaSituacaoForCli = new ArrayList<>();
        try {
            listaSituacaoForCli = situacoesCliente.getEntitys(SituacaoForCli.class, "nome", nome,atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoForCli;
    }


    public PessoaCliente getClienteSelecionado() {
        return clienteSelecionado;
    }

    public void setClienteSelecionado(PessoaCliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
    }

    @Override
    protected Class<Cliente> getClazz() {
        return Cliente.class;
    }

    @Override
    protected String getFuncaoBase() {
        return  "Cliente";
    }


}
