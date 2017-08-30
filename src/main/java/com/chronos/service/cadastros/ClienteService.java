package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.Cliente;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.PessoaFisica;
import com.chronos.modelo.entidades.PessoaJuridica;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.Biblioteca;
import com.chronos.util.jsf.Mensagem;

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


    public Cliente salvar(Cliente cliente)  {
        try {
            validarPessoa(cliente.getPessoa());
            Pessoa pessoa = cliente.getPessoa().getId()==null? pessoas.atualizar(cliente.getPessoa()):cliente.getPessoa();
            cliente.setPessoa(pessoa);
            cliente = clientes.atualizar(cliente);
            clientes.getEntityJoinFetch(1,Cliente.class);

        } catch (Exception ex) {
            Mensagem.addErrorMessage("",ex);
        }

        return cliente;
    }


    private void validarPessoa(Pessoa pessoa) throws Exception {
        Long total;
        if (pessoa.getTipo().equals("F")) {
            total = pessoasFisica.getTotalRegistros(PessoaFisica.class, "cpf", pessoa.getIdentificador());
            if (total > 0) {
                throw new ChronosException("CPF ja informado em :" + pessoa.getNome());
            }
            if(!Biblioteca.cpfValido(pessoa.getIdentificador())){
                throw new ChronosException("CPF invalido");
            }
        } else {
            total = pessoasJuridica.getTotalRegistros(PessoaJuridica.class, "cnpj", pessoa.getIdentificador());
            if (total > 0) {
                throw new ChronosException("CNPJ ja informado em :" + pessoa.getNome());
            }
            if(Biblioteca.cnpjValido(pessoa.getIdentificador())){
                throw new ChronosException("CNPJ invalido");
            }
        }

        if(pessoa.getListaPessoaEndereco()==null || pessoa.getListaPessoaEndereco().isEmpty()){
            throw new ChronosException("Endereço principal não informado");
        }


    }
}
