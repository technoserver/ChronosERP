/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.Transportadora;
import com.chronos.modelo.enuns.TelaPessoa;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class TransportadoraControll extends PessoaControll<Transportadora> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PessoaService service;

    private String completo;

    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("N", "N", "S", "N");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDataCadastro(new Date());
        completo = "S";

    }

    @Override
    public void salvar() {

        Transportadora transportadora = null;
        try {
            transportadora = service.salvarTransportadora(getObjeto(), empresa);
            setObjeto(transportadora);
            Mensagem.addInfoMessage("Cliente salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }

    }

    @Override
    protected Class<Transportadora> getClazz() {
        return Transportadora.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "TRANSPORTADORA";
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
    
}
