package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by john on 02/08/17.
 */
public class FornecedorService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<Pessoa> pessoas;
    @Inject
    private Repository<PessoaFisica> pessoasFisica;
    @Inject
    private Repository<PessoaJuridica> pessoasJuridica;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoas;
    @Inject
    private PessoaService pessoaService;
    private String cpfCnpj;

    @Transactional
    public Fornecedor cadastrarFornecedor(NfeEmitente emitente, Empresa empresa) throws Exception {

        Pessoa pessoa = new Pessoa();
        pessoa.setListaPessoaContato(new HashSet<>());
        pessoa.setListaPessoaEndereco(new HashSet<>());
        pessoa.setListaPessoaTelefone(new HashSet<>());


        PessoaEndereco end = new PessoaEndereco();
        Fornecedor fornecedor = new Fornecedor();


        end.setBairro(emitente.getBairro());
        end.setCep(emitente.getCep());
        end.setCidade(emitente.getNomeMunicipio());
        end.setComplemento(emitente.getComplemento());
        end.setLogradouro(emitente.getLogradouro());
        end.setMunicipioIbge(emitente.getCodigoMunicipio());
        end.setNumero(StringUtils.leftPad(emitente.getNumero(), 10, ' ').substring(0, 10).trim());

        end.setPrincipal("S");
        end.setUf(emitente.getUf());

        pessoa.setCliente("N");
        pessoa.setFornecedor("S");
        pessoa.setTransportadora("N");
        pessoa.setColaborador("N");
        pessoa.setNome(emitente.getNome());

        pessoa.setTipo(emitente.getCpfCnpj().length() > 11 ? "J" : "F");
        pessoa.getListaPessoaEndereco().add(end);
        cpfCnpj = emitente.getCpfCnpj();


        if (pessoa.getTipo().equals("F")) {
            pessoa = instanciaPessoaFisica(pessoa);
            pessoa.getPessoaFisica().setCpf(emitente.getCpfCnpj());
        } else {
            pessoa = instanciaPessoaJuridica(pessoa);
            pessoa.getPessoaJuridica().setCnpj(emitente.getCpfCnpj());
            pessoa.getPessoaJuridica().setCrt(emitente.getCrt());
            pessoa.getPessoaJuridica().setFantasia(emitente.getFantasia());
            pessoa.getPessoaJuridica().setInscricaoEstadual(emitente.getInscricaoEstadual());
            pessoa.getPessoaJuridica().setInscricaoMunicipal(emitente.getInscricaoMunicipal());
            pessoa.getPessoaJuridica().setSuframa(emitente.getSuframa() + "");

        }

        end.setPessoa(pessoa);

        pessoa = pessoaService.salvar(pessoa, empresa);


        fornecedor.setPessoa(pessoa);
        fornecedor.setDataCadastro(new Date());
        fornecedor.setSituacaoForCli(new SituacaoForCli(1, "NORMAL", "SEM RESTRICAO"));
        fornecedor.setAtividadeForCli(new AtividadeForCli(1));
        fornecedor.setObservacao("IMPORTACAO");
        fornecedor.setDataCadastro(new Date());
        fornecedor.setDesde(new Date());
        fornecedor = fornecedores.atualizar(fornecedor);


        return fornecedor;
    }


    private Pessoa instanciaPessoaFisica(Pessoa pessoa) {
        PessoaFisica pf = new PessoaFisica();
        pessoa.setTipo("F");
        pessoa.setPessoaFisica(pf);
        pessoa.setPessoaJuridica(null);
        pf.setPessoa(pessoa);
        pf.setCpf(cpfCnpj);
        EstadoCivil ec = new EstadoCivil();
        ec.setId(1);
        pf.setEstadoCivil(ec);

        return pessoa;
    }

    private Pessoa instanciaPessoaJuridica(Pessoa pessoa) {
        PessoaJuridica pj = new PessoaJuridica();
        pessoa.setTipo("J");
        pessoa.setPessoaJuridica(pj);
        pj.setPessoa(pessoa);
        pj.setCnpj(cpfCnpj);
        return pessoa;
    }


    public boolean existeFornecedorByCpfCnj(String cpfCnpj) {
        String atributo = cpfCnpj.length() > 11 ? "pessoa.pessoaJuridica.cnpj" : "pessoa.pessoaFisica.cpf";
        return fornecedores.existeRegisro(Fornecedor.class, atributo, cpfCnpj);
    }

    public Fornecedor getFornecedor(String cpfCnpj) {
        Fornecedor fornecedor = fornecedores.get(Fornecedor.class, "pessoa.pessoaJuridica.cnpj", cpfCnpj, new Object[]{"pessoa.nome"});
        return fornecedor;
    }

}
