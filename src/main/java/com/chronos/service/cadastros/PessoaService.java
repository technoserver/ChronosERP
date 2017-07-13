package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.Cliente;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.PessoaFisica;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by john on 11/07/17.
 */
public class PessoaService implements Serializable {


    @Inject
    private Repository<Cliente> clientes;
    @Inject
    private Repository<PessoaFisica> pessoasFisica;

    private String cpf;
    private String cnpj;

    private Pessoa novo(){
        Pessoa pessoa = null;


        return pessoa;
    }

}
