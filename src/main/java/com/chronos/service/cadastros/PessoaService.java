package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.service.ChronosException;
import com.chronos.util.Biblioteca;
import com.chronos.util.jpa.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by john on 11/07/17.
 */
public class PessoaService implements Serializable {


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
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<Transportadora> transportadoras;
    @Inject
    private Repository<Colaborador> colaboradores;


    @Transactional
    public Fornecedor salvarFornecedor(Fornecedor fornecedor, Empresa empresa) throws Exception {
        validarPessoa(fornecedor.getPessoa());
        fornecedor = fornecedores.atualizar(fornecedor);
        salvarEmpresaPessoa(empresa, fornecedor.getPessoa());

        return fornecedor;
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente, Empresa empresa) throws Exception {
        validarPessoa(cliente.getPessoa());
        cliente = clientes.atualizar(cliente);
        salvarEmpresaPessoa(empresa, cliente.getPessoa());
        return cliente;
    }

    @Transactional
    public Transportadora salvarTransportadora(Transportadora transportadora, Empresa empresa) throws Exception {
        validarPessoa(transportadora.getPessoa());
        transportadora = transportadoras.atualizar(transportadora);
        salvarEmpresaPessoa(empresa, transportadora.getPessoa());
        return transportadora;
    }

    @Transactional
    public Colaborador salvarColaborador(Colaborador colaborador, Empresa empresa) throws Exception {
        validarPessoa(colaborador.getPessoa());
        colaborador = colaboradores.atualizar(colaborador);
        salvarEmpresaPessoa(empresa, colaborador.getPessoa());

        return colaborador;
    }


    private void salvarEmpresaPessoa(Empresa empresa, Pessoa pessoa) {
        EmpresaPessoa empresaPessoa = new EmpresaPessoa();
        empresaPessoa.setPessoa(pessoa);
        empresaPessoa.setEmpresa(empresa);
        empresaPessoa.setResponsavelLegal("N");
        empresaPessoas.salvar(empresaPessoa);
    }


    private void validarPessoa(Pessoa pessoa) throws Exception {

        if (pessoa.getTipo().equals("F")) {
            Object[] atributos = new Object[]{"cpf"};
            PessoaFisica pf = pessoasFisica.get(PessoaFisica.class, "cpf", pessoa.getPessoaFisica().getCpf().replaceAll("\\D", ""), atributos);

            if (pf != null && !pf.equals(pessoa.getPessoaFisica())) {
                throw new ChronosException("CPF ja informado em :" + pessoa.getNome());
            }
            if (!Biblioteca.cpfValido(pessoa.getIdentificador())) {
                throw new ChronosException("CPF invalido");
            }
            pessoa.setPessoaJuridica(null);
        } else {
            Object[] atributos = new Object[]{"cnpj"};
            PessoaJuridica pj = pessoasJuridica.get(PessoaJuridica.class, "cnpj", pessoa.getPessoaJuridica().getCnpj().replaceAll("\\D", ""), atributos);

            if (pj != null && !pj.equals(pessoa.getPessoaJuridica())) {
                throw new ChronosException("CNPJ ja informado em :" + pessoa.getNome());
            }
            if (!Biblioteca.cnpjValido(pessoa.getIdentificador())) {
                throw new ChronosException("CNPJ invalido");
            }
            pessoa.setPessoaFisica(null);
            String fantasia = StringUtils.isEmpty(pessoa.getPessoaJuridica().getFantasia()) ? pessoa.getNome() : pessoa.getPessoaJuridica().getFantasia();
            pessoa.getPessoaJuridica().setFantasia(fantasia);
        }

        if (pessoa.getListaPessoaEndereco() == null || pessoa.getListaPessoaEndereco().isEmpty()) {
            throw new ChronosException("Endereço principal não informado");
        }

    }



}
