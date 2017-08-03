package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.*;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by john on 02/08/17.
 */
public class FornecedorService implements Serializable {

    @Inject
    private Repository<Fornecedor> fornecedores;
    @Inject
    private Repository<Pessoa> pessoas;

    private String cpfCnpj;

    private static final long serialVersionUID = 1L;


    @Transactional
    public Fornecedor cadastrarFornecedor(NfeEmitente emitente,Empresa empresa) throws Exception {

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
        pessoa.setNome(emitente.getNome());
        pessoa.setTransportadora("N");
        pessoa.setTipo(emitente.getCpfCnpj().length() > 11 ? "J" : "F");
        pessoa.getListaPessoaEndereco().add(end);
        cpfCnpj = emitente.getCpfCnpj();

        pessoa.setListaEmpresa(new HashSet<>());
        pessoa.getListaEmpresa().add(empresa);

        pessoa  = pessoa.getTipo().equals("F")?instanciaPessoaFisica(pessoa):instanciaPessoaJuridica(pessoa);

        end.setPessoa(pessoa);

        pessoa = pessoas.atualizar(pessoa);
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

}
