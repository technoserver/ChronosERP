package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.util.Biblioteca;
import com.chronos.erp.util.jpa.Transactional;
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
        boolean salvarEmpresaPessoa = cliente.getId() == null;
        cliente = clientes.atualizar(cliente);

        if (salvarEmpresaPessoa) {
            salvarEmpresaPessoa(empresa, cliente.getPessoa());
        }

        return cliente;
    }

    @Transactional
    public Transportadora salvarTransportadora(Transportadora transportadora, Empresa empresa) throws Exception {
        validarPessoa(transportadora.getPessoa());

        boolean salvarEmpresaPessoa = transportadora.getId() == null;

        transportadora = transportadoras.atualizar(transportadora);

        if (salvarEmpresaPessoa) {
            salvarEmpresaPessoa(empresa, transportadora.getPessoa());
        }

        return transportadora;
    }

    @Transactional
    public Colaborador salvarColaborador(Colaborador colaborador, Empresa empresa) throws Exception {


        validarPessoa(colaborador.getPessoa());

        boolean salvarEmpresaPessoa = colaborador.getPessoa().getId() == null;

        colaborador = colaboradores.atualizar(colaborador);
        if (salvarEmpresaPessoa) {
            salvarEmpresaPessoa(empresa, colaborador.getPessoa());
        }


        return colaborador;
    }


    public Pessoa salvar(Pessoa pessoa, Empresa empresa) throws Exception {
        validarPessoa(pessoa);
        boolean salvarEmpresaPessoa = pessoa.getId() == null;
        pessoa = pessoas.atualizar(pessoa);
        if (salvarEmpresaPessoa) {
            salvarEmpresaPessoa(empresa, pessoa);
        }
        return pessoa;
    }

    public void validarColaborador(Colaborador colaborador) throws ChronosException {

        boolean existeRegisro = colaboradores.existeRegisro(Colaborador.class, "pessoa.id", colaborador.getPessoa().getId());

        if (existeRegisro) {
            throw new ChronosException("Está pessoa já foi definida como colaborador");
        }
    }

    public void validarCliente(Cliente cliente) throws ChronosException {

        boolean existeRegisro = clientes.existeRegisro(Cliente.class, "pessoa.id", cliente.getPessoa().getId());

        if (existeRegisro) {
            throw new ChronosException("Está pessoa já foi definida como cliente");
        }
    }

    private void validarPessoa(Pessoa pessoa) throws Exception {

        if (pessoa.getTipo().equals("F")) {

            if (!StringUtils.isEmpty(pessoa.getPessoaFisica().getCpf())) {
                Object[] atributos = new Object[]{"pessoa.nome"};
                PessoaFisica pf = pessoasFisica.get(PessoaFisica.class, "cpf", pessoa.getPessoaFisica().getCpf().replaceAll("\\D", ""), atributos);

                if (pf != null && !pf.equals(pessoa.getPessoaFisica())) {
                    throw new ChronosException("CPF ja informado em :" + pf.getPessoa().getNome());
                }
                if (!Biblioteca.cpfValido(pessoa.getIdentificador())) {
                    throw new ChronosException("CPF invalido");
                }
            }

            pessoa.setPessoaJuridica(null);
        } else {
            if (!StringUtils.isEmpty(pessoa.getPessoaJuridica().getCnpj())) {
                Object[] atributos = new Object[]{"pessoa.nome"};
                PessoaJuridica pj = pessoasJuridica.get(PessoaJuridica.class, "cnpj", pessoa.getPessoaJuridica().getCnpj().replaceAll("\\D", ""), atributos);

                if (pj != null && !pj.equals(pessoa.getPessoaJuridica())) {
                    throw new ChronosException("CNPJ ja informado em :" + pj.getPessoa().getNome());
                }
                if (!Biblioteca.cnpjValido(pessoa.getIdentificador())) {
                    throw new ChronosException("CNPJ invalido");
                }
                pessoa.setPessoaFisica(null);
                String fantasia = StringUtils.isEmpty(pessoa.getPessoaJuridica().getFantasia()) ? pessoa.getNome() : pessoa.getPessoaJuridica().getFantasia();
                pessoa.getPessoaJuridica().setFantasia(fantasia);
            }
        }

        if (pessoa.getListaPessoaEndereco() == null || pessoa.getListaPessoaEndereco().isEmpty()) {
            throw new ChronosException("Endereço principal não informado");
        }

    }

    public EmpresaPessoa salvarEmpresaPessoa(Empresa empresa, Pessoa pessoa) {
        EmpresaPessoa empresaPessoa = new EmpresaPessoa();
        empresaPessoa.setPessoa(pessoa);
        empresaPessoa.setEmpresa(empresa);
        empresaPessoa.setResponsavelLegal("N");
        empresaPessoa.setEmpresaPrincipal("S");
        empresaPessoas.salvar(empresaPessoa);
        return empresaPessoa;
    }


}
