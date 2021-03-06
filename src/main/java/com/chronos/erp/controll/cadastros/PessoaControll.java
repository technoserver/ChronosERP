/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author john
 */
public abstract class PessoaControll<T> extends AbstractControll<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;
    @Inject
    private Repository<Municipio> municipios;
    @Inject
    private Repository<Pessoa> pessoas;
    @Inject
    private Repository<EstadoCivil> estadosCivis;
    @Inject
    private Repository<Empresa> empresaRepository;
    protected Empresa emp;
    private PessoaContato pessoaContatoSelecionado;
    private PessoaContato pessoaContato;
    private PessoaEndereco pessoaEnderecoSelecionado;
    private PessoaEndereco pessoaEndereco;
    private PessoaEndereco endereco;
    private PessoaTelefone pessoaTelefoneSelecionado;
    private PessoaTelefone pessoaTelefone;
    private Municipio cidade;
    private List<Municipio> cidades;
    private List<EstadoCivil> listEstadoCivil;


    private String cpf;
    private String cnpj;


    @PostConstruct
    @Override
    public void init() {
        super.init();
        listEstadoCivil = new ArrayList<>();
        listEstadoCivil.add(new EstadoCivil(1, "SOLTEIRO"));
        listEstadoCivil.add(new EstadoCivil(2, "CASADO"));
        listEstadoCivil.add(new EstadoCivil(3, "VIUVO"));
        listEstadoCivil.add(new EstadoCivil(4, "SEPARADO JUDICIALMENTE"));
        listEstadoCivil.add(new EstadoCivil(5, "DIVORCIADO"));
    }

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
        if (getObjeto() == null) {
            super.doEdit();
        }

        Class<Pessoa> classToCast = Pessoa.class;
        Pessoa p = pessoas.getJoinFetch(getPessoa().getId(), Pessoa.class);
        setPessoa(p);

        endereco = getPessoa().getListaPessoaEndereco().stream().filter((e) -> e.getPrincipal().equals("S")).findFirst().orElse(new PessoaEndereco());
        cidade = new Municipio(0, endereco.getCidade(), endereco.getMunicipioIbge());


        List<Filtro> filtros = new ArrayList<>();
        filtros.add(new Filtro("empresaPrincipal", "S"));
        filtros.add(new Filtro("pessoa.id", getPessoa().getId()));
        EmpresaPessoa empresaPessoa = empresaPessoaRepository.get(EmpresaPessoa.class, filtros, new Object[]{"empresa.id", "empresa.razaoSocial"});

        if (empresaPessoa != null) {
            emp = empresaPessoa.getEmpresa();
        }
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
        pessoas.atualizar(getPessoa());
        Mensagem.addInfoMessage("Contato salvo com sucesso!");

    }

    public void excluirContato() {
        Pessoa pessoa = getPessoa();
        pessoa.getListaPessoaContato().remove(pessoaContatoSelecionado);
        pessoas.atualizar(pessoa);
        Mensagem.addInfoMessage("Contato excluído com sucesso!");


    }

    public void incluirEndereco() {
        pessoaEndereco = new PessoaEndereco();
        pessoaEndereco.setPessoa(getPessoa());
        pessoaEndereco.setPrincipal("N");
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
        pessoas.atualizar(getPessoa());
        Mensagem.addInfoMessage("Endereço salvo com sucesso!");

    }

    public void excluirEndereco() {
        getPessoa().getListaPessoaEndereco().remove(pessoaEnderecoSelecionado);
        pessoas.atualizar(getPessoa());
        Mensagem.addInfoMessage("Endereço excluído com sucesso!");

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
        pessoas.atualizar(getPessoa());
        Mensagem.addInfoMessage("Telefone salvo com sucesso!");

    }

    public void excluirTelefone() {
        Pessoa pessoa = getPessoa();
        pessoa.getListaPessoaTelefone().remove(pessoaTelefoneSelecionado);
        pessoas.atualizar(getPessoa());
        Mensagem.addInfoMessage("Telefone excluído com sucesso!");

    }

    public List<Empresa> getListaEmpresa(String nome) {
        List<Empresa> listaEmpresa = new ArrayList<>();
        try {
            listaEmpresa = empresaRepository.getEntitys(Empresa.class, "razaoSocial", nome, new Object[]{"razaoSocial"});
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaEmpresa;
    }

    protected Pessoa novaPessoa(String cliente, String colaborador, String transportadora, String fornecedor) {

        Pessoa pessoa = new Pessoa();

        pessoa.setCliente(cliente);
        pessoa.setFornecedor(fornecedor);
        pessoa.setColaborador(colaborador);
        pessoa.setTransportadora(transportadora);

        pessoa.setListaPessoaContato(new HashSet<>());
        pessoa.setListaPessoaEndereco(new HashSet<>());
        pessoa.setListaPessoaTelefone(new HashSet<>());

        endereco = new PessoaEndereco();
        endereco.setPrincipal("S");
        endereco.setCobranca("S");
        endereco.setCorrespondencia("S");
        endereco.setEntrega("S");

        endereco.setPessoa(pessoa);
        pessoa.getListaPessoaEndereco().add(endereco);

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
        setPessoa(pessoa);
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

    public void atualizarCodIbge() {
        endereco.setMunicipioIbge(cidade.getCodigoIbge());
        endereco.setCidade(cidade.getNome());
    }

    public void instanciaCidade() {
        cidade = null;
    }

    public List<Municipio> getMunicipios(String nome) {
        cidades = new LinkedList<>();
        try {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("uf.sigla", endereco.getUf()));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            atributos = new Object[]{"nome", "codigoIbge"};
            cidades = municipios.getEntitys(Municipio.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidades;
    }


    public abstract Pessoa getPessoa();

    public abstract void setPessoa(Pessoa pessoa);

    public abstract String getTela();

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public PessoaEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(PessoaEndereco endereco) {
        this.endereco = endereco;
    }

    public PessoaContato getPessoaContatoSelecionado() {
        return pessoaContatoSelecionado;
    }

    public void setPessoaContatoSelecionado(PessoaContato pessoaContatoSelecionado) {
        this.pessoaContatoSelecionado = pessoaContatoSelecionado;
    }

    public PessoaContato getPessoaContato() {
        return pessoaContato;
    }

    public void setPessoaContato(PessoaContato pessoaContato) {
        this.pessoaContato = pessoaContato;
    }

    public PessoaEndereco getPessoaEnderecoSelecionado() {
        return pessoaEnderecoSelecionado;
    }

    public void setPessoaEnderecoSelecionado(PessoaEndereco pessoaEnderecoSelecionado) {
        this.pessoaEnderecoSelecionado = pessoaEnderecoSelecionado;
    }

    public PessoaTelefone getPessoaTelefoneSelecionado() {
        return pessoaTelefoneSelecionado;
    }

    public void setPessoaTelefoneSelecionado(PessoaTelefone pessoaTelefoneSelecionado) {
        this.pessoaTelefoneSelecionado = pessoaTelefoneSelecionado;
    }

    public PessoaTelefone getPessoaTelefone() {
        return pessoaTelefone;
    }

    public void setPessoaTelefone(PessoaTelefone pessoaTelefone) {
        this.pessoaTelefone = pessoaTelefone;
    }

    public Municipio getCidade() {
        return cidade;
    }

    public void setCidade(Municipio cidade) {
        this.cidade = cidade;
    }

    public Empresa getEmp() {
        return emp;
    }

    public void setEmp(Empresa emp) {
        this.emp = emp;
    }

    public List<EstadoCivil> getListEstadoCivil() {
        return listEstadoCivil;
    }
}
