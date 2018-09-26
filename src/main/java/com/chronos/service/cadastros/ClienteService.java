package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.modelo.view.PessoaCliente;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by john on 25/07/17.
 */
public class ClienteService implements Serializable {


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


    public Cliente cadastrarCliente(PessoaCliente pessoaCliente, Empresa empresa) {
        Cliente cliente = instanciarCliente(empresa);

        PessoaEndereco endereco = cliente.getPessoa().getListaPessoaEndereco().stream().findFirst().orElse(new PessoaEndereco());

        cliente.getPessoa().setNome(pessoaCliente.getNome());
        cliente.getPessoa().setEmail(pessoaCliente.getEmail());
        if (pessoaCliente.getCpfCnpj().length() > 11) {
            cliente.getPessoa().setTipo("J");
            cliente.getPessoa().getPessoaJuridica().setCnpj(pessoaCliente.getCpfCnpj());
            cliente.getPessoa().getPessoaJuridica().setFantasia(pessoaCliente.getNome());
            cliente.getPessoa().getPessoaJuridica().setInscricaoEstadual(pessoaCliente.getRgIe());

        } else {
            cliente.getPessoa().setTipo("F");
            cliente.getPessoa().getPessoaFisica().setCpf(pessoaCliente.getCpfCnpj());

        }
        if (cliente.getPessoa().getTipo().equals("F")) {
            cliente.getPessoa().setPessoaJuridica(null);

        } else {
            cliente.getPessoa().setPessoaFisica(null);
        }
        endereco.setBairro(pessoaCliente.getBairro());
        endereco.setCep(pessoaCliente.getCep());
        endereco.setCidade(pessoaCliente.getCidade());
        endereco.setComplemento(pessoaCliente.getComplemento());
        endereco.setFone(pessoaCliente.getFone());
        endereco.setLogradouro(pessoaCliente.getLogradouro());
        endereco.setMunicipioIbge(pessoaCliente.getMunicipioIbge());
        endereco.setNumero(pessoaCliente.getNumero());
        endereco.setUf(pessoaCliente.getUf());

        cliente = clientes.atualizar(cliente);


        return cliente;
    }

    public Cliente cadastrarCliente(NfeDestinatario destinatario, Empresa empresa) {
        Cliente cliente = instanciarCliente(empresa);

        PessoaEndereco endereco = cliente.getPessoa().getListaPessoaEndereco().stream().findFirst().orElse(new PessoaEndereco());

        cliente.getPessoa().setNome(destinatario.getNome());
        cliente.getPessoa().setEmail(destinatario.getEmail());
        if (destinatario.getCpfCnpj().length() > 11) {
            cliente.getPessoa().setTipo("J");
            cliente.getPessoa().getPessoaJuridica().setCnpj(destinatario.getCpfCnpj());
            cliente.getPessoa().getPessoaJuridica().setFantasia(destinatario.getNome());
            cliente.getPessoa().getPessoaJuridica().setInscricaoEstadual(destinatario.getInscricaoEstadual());

        } else {
            cliente.getPessoa().setTipo("F");
            cliente.getPessoa().getPessoaFisica().setCpf(destinatario.getCpfCnpj());

        }
        if (cliente.getPessoa().getTipo().equals("F")) {
            cliente.getPessoa().setPessoaJuridica(null);

        } else {
            cliente.getPessoa().setPessoaFisica(null);
        }
        endereco.setBairro(destinatario.getBairro());
        endereco.setCep(destinatario.getCep());
        endereco.setCidade(destinatario.getNomeMunicipio());
        endereco.setComplemento(destinatario.getComplemento());
        endereco.setFone(destinatario.getTelefone());
        endereco.setLogradouro(destinatario.getLogradouro());
        endereco.setMunicipioIbge(destinatario.getCodigoMunicipio());
        endereco.setNumero(destinatario.getNumero());
        endereco.setUf(destinatario.getUf());

        cliente = clientes.atualizar(cliente);


        return cliente;
    }

    private Cliente instanciarCliente(Empresa empresa) {
        Cliente cliente = new Cliente();

        Pessoa pessoa = new Pessoa();
        PessoaFisica pessoaFisica = new PessoaFisica();
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        PessoaEndereco endereco = new PessoaEndereco();
        pessoaFisica.setPessoa(pessoa);
        pessoaJuridica.setPessoa(pessoa);

        endereco.setPrincipal("S");
        endereco.setCobranca("S");
        endereco.setEntrega("S");
        endereco.setCorrespondencia("S");

        pessoa.setTipo("F");
        pessoa.setCliente("S");
        pessoa.setFornecedor("N");
        pessoa.setTransportadora("N");
        pessoa.setColaborador("N");
        pessoa.setPessoaFisica(pessoaFisica);
        pessoa.setPessoaJuridica(pessoaJuridica);
        pessoa.setListaPessoaContato(new HashSet<>());
        pessoa.setListaPessoaEndereco(new HashSet<>());
        pessoa.getListaPessoaEndereco().add(endereco);


        endereco.setPessoa(pessoa);

        cliente.setDataCadastro(new Date());
        cliente.setPessoa(pessoa);
        cliente.setSituacaoForCli(new SituacaoForCli(1, "NORMAL", "SEM RESTRICAO"));
        cliente.setAtividadeForCli(new AtividadeForCli(1));

        return cliente;
    }

}
