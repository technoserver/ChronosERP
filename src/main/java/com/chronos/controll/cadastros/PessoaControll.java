/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author john
 */
public abstract class PessoaControll<T> extends AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private PessoaContato pessoaContatoSelecionado;
    private PessoaContato pessoaContato;
    private PessoaEndereco pessoaEnderecoSelecionado;
    private PessoaEndereco pessoaEndereco;
    private PessoaTelefone pessoaTelefoneSelecionado;
    private PessoaTelefone pessoaTelefone;


    @Inject
    private Repository<Pessoa> pessoas;
    @Inject
    private Repository<EstadoCivil> estadosCivis;

    private String cpf;
    private String cnpj;


    @Override
    public void doCreate() {
        super.doCreate();

        setPessoa(new Pessoa());

        getPessoa().setListaPessoaContato(new HashSet<>());
        getPessoa().setListaPessoaEndereco(new HashSet<>());
        getPessoa().setListaPessoaTelefone(new HashSet<>());
    }

    @Override
    public void doEdit() {
        super.doEdit();
        Class<Pessoa> classToCast = Pessoa.class;
        Pessoa p = pessoas.getEntityJoinFetch(getPessoa().getId(), Pessoa.class);
        setPessoa(p);
    }

    public void definirTipo() {
        Pessoa pessoa = getPessoa();
        if (pessoa.getTipo().equals("F")) {
            instanciaPessoaFisica(pessoa);
        } else {
            instanciaPessoaJuridica(pessoa);
        }
    }

    public void incluirContato() {
        pessoaContato = new PessoaContato();
        pessoaContato.setPessoa(getPessoa());
    }

    public void alterarContato() {
        pessoaContato = pessoaContatoSelecionado;
    }

    public void salvarContato() {
        if (pessoaContato.getId() == null) {
            getPessoa().getListaPessoaContato().add(pessoaContato);
        }
        salvar("Contato salvo com sucesso!");
    }

    public void excluirContato() {

        getPessoa().getListaPessoaContato().remove(pessoaContatoSelecionado);
        salvar("Contato excluído com sucesso!");

    }

    public void incluirEndereco() {
        pessoaEndereco = new PessoaEndereco();
        pessoaEndereco.setPessoa(getPessoa());
    }

    public void alterarEndereco() {
        pessoaEndereco = pessoaEnderecoSelecionado;
    }

    public void salvarEndereco() {
        if (pessoaEndereco.getCep() != null) {
            pessoaEndereco.setCep(pessoaEndereco.getCep().replaceAll("\\D", ""));
        }
        if (pessoaEndereco.getId() == null) {
            getPessoa().getListaPessoaEndereco().add(pessoaEndereco);
        }
        salvar("Endereço salvo com sucesso!");
    }

    public void excluirEndereco() {
        getPessoa().getListaPessoaEndereco().remove(pessoaEnderecoSelecionado);
        salvar("Endereço excluído com sucesso!");
    }

    public void incluirTelefone() {
        pessoaTelefone = new PessoaTelefone();
        pessoaTelefone.setPessoa(getPessoa());
    }

    public void alterarTelefone() {
        pessoaTelefone = pessoaTelefoneSelecionado;
    }

    public void salvarTelefone() {
        if (pessoaTelefone.getId() == null) {
            getPessoa().getListaPessoaTelefone().add(pessoaTelefone);
        }
        salvar("Telefone salvo com sucesso!");
    }

    public void excluirTelefone() {
        getPessoa().getListaPessoaTelefone().remove(pessoaTelefoneSelecionado);
        salvar("Telefone excluído com sucesso!");
    }


    public List<EstadoCivil> getListaEstadoCivil(String nome) {
        List<EstadoCivil> listaEstadoCivil = new ArrayList<>();
        try {
            listaEstadoCivil = estadosCivis.getEntitys(EstadoCivil.class, "nome", nome, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaEstadoCivil;
    }

    protected Pessoa novaPessoa(String cliente, String colaborador, String transportadora, String fornecedor) {

        Pessoa pessoa = new Pessoa();
        pessoa.setListaPessoaContato(new HashSet<>());
        pessoa.setListaPessoaEndereco(new HashSet<>());
        pessoa.setListaPessoaTelefone(new HashSet<>());

        if (cliente.equals("S")) {
            pessoa.setCliente(cliente);
        }
        if (transportadora.equals("S")) {
            pessoa.setTransportadora(transportadora);
        }
        if (colaborador.equals("S")) {
            pessoa.setColaborador(colaborador);
        }
        if (fornecedor.equals("S")) {
            pessoa.setFornecedor(fornecedor);
        }
        pessoa = instanciaPessoaFisica(pessoa);
        return pessoa;
    }


    protected Pessoa instanciaPessoaFisica(Pessoa pessoa) {
        PessoaFisica pf = new PessoaFisica();
        pessoa.setNome(getPessoa().getNome());
        pessoa.setTipo("F");
        pessoa.setPessoaFisica(pf);
        pf.setPessoa(pessoa);
        pf.setCpf(cpf);
        EstadoCivil ec = new EstadoCivil();
        ec.setId(1);
        pf.setEstadoCivil(ec);

        return pessoa;
    }

    protected Pessoa instanciaPessoaJuridica(Pessoa pessoa) {
        PessoaJuridica pj = new PessoaJuridica();
        pessoa.setNome(getPessoa().getNome());
        pessoa.setTipo("J");
        pessoa.setPessoaJuridica(pj);
        pj.setPessoa(pessoa);
        pj.setCnpj(cnpj);

        return pessoa;
    }

    public abstract Pessoa getPessoa();

    public abstract void setPessoa(Pessoa pessoa);

    public abstract String getTela();

}
