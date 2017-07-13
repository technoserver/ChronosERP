/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.PessoaContato;
import com.chronos.modelo.entidades.PessoaEndereco;
import com.chronos.modelo.entidades.PessoaTelefone;

import java.io.Serializable;

/**
 *
 * @author john
 */
public abstract class PessoaControll<T> extends AbstractControll<Pessoa> implements Serializable {

    private static final long serialVersionUID = 1L;

    private PessoaContato pessoaContatoSelecionado;
    private PessoaContato pessoaContato;
    private PessoaEndereco pessoaEnderecoSelecionado;
    private PessoaEndereco pessoaEndereco;
    private PessoaTelefone pessoaTelefoneSelecionado;
    private PessoaTelefone pessoaTelefone;

    private String cpf;
    private String cnpj;

    public abstract Pessoa getPessoa();

    public abstract void setPessoa(Pessoa pessoa);


    
}
