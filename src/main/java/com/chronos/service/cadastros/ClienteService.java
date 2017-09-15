package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by john on 25/07/17.
 */
public class ClienteService implements Serializable{


    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<Pessoa> pessoas;
    @Inject
    private Repository<PessoaFisica> pessoasFisica;
    @Inject
    private Repository<PessoaJuridica> pessoasJuridica;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoas;



}
