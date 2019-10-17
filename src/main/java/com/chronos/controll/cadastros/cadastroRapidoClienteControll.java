package com.chronos.controll.cadastros;

import com.chronos.dto.ClienteResumDTO;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.enuns.Estados;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.jsf.FacesUtil;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class cadastroRapidoClienteControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipioRepository;
    @Inject
    private Repository<Cliente> repository;
    @Inject
    private Repository<Pessoa> pessoaRepository;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;

    private ClienteResumDTO cliente;
    private Map<String, String> tipoPessoa;

    private Municipio cidade;
    private List<Municipio> cidades;


    @PostConstruct
    private void init() {
        cliente = new ClienteResumDTO();
        cliente.setUf("AC");
        tipoPessoa = new LinkedHashMap<>();
        tipoPessoa.put("Física", "F");
        tipoPessoa.put("Jurídica", "J");
    }

    public void abrirDialog() {
        Map<String, Object> opcoes = new HashMap<>();
        opcoes.put("modal", true);
        opcoes.put("resizable", false);
        //  opcoes.put("contentHeight", 470);


        PrimeFaces.current().dialog().openDynamic("/modulo/cadastros/comercial/cadastroRapidoCliente", opcoes, null);
    }

    public void closerDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    @Transactional
    public void salvar() {
        Pessoa pessoa = new Pessoa();
        pessoa.setTipo(cliente.getTipo());
        pessoa.setNome(cliente.getNome());
        pessoa.setCliente("S");
        pessoa.setColaborador("N");
        pessoa.setTransportadora("N");
        pessoa.setFornecedor("N");


        if (cliente.getTipo().equals("F")) {
            pessoa.setPessoaFisica(new PessoaFisica());
            pessoa.getPessoaFisica().setPessoa(pessoa);
            pessoa.getPessoaFisica().setEstadoCivil(new EstadoCivil(1));
        } else {
            pessoa.setPessoaFisica(new PessoaFisica());
            pessoa.getPessoaJuridica().setPessoa(pessoa);
            pessoa.getPessoaJuridica().setFantasia(cliente.getNome());
        }

        pessoa.setListaPessoaContato(new HashSet<>());
        pessoa.setListaPessoaEndereco(new HashSet<>());
        pessoa.setListaPessoaTelefone(new HashSet<>());
        PessoaEndereco end = new PessoaEndereco();
        end.setPessoa(pessoa);
        end.setPrincipal("S");
        end.setCep(cliente.getCep());
        end.setCidade(cliente.getCidade());
        end.setMunicipioIbge(cliente.getMunicipioIbge());
        end.setUf(cliente.getUf());
        end.setBairro(cliente.getBairro());
        end.setCobranca("S");
        end.setComplemento(cliente.getComplemento());
        end.setCorrespondencia("S");
        end.setEntrega("S");
        end.setLogradouro(cliente.getLogradouro());
        end.setNumero(cliente.getNumero());
        end.setFone(cliente.getFone());

        pessoa.getListaPessoaEndereco().add(end);

        pessoa = pessoaRepository.atualizar(pessoa);

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setPessoa(pessoa);
        clienteSalvo.setDataCadastro(new Date());
        clienteSalvo.setSituacaoForCli(new SituacaoForCli(1));
        clienteSalvo.setAtividadeForCli(new AtividadeForCli(1));
        clienteSalvo.setBloqueado("N");
        clienteSalvo.setDesde(new Date());
        clienteSalvo.setDataAlteracao(new Date());

        clienteSalvo = repository.atualizar(clienteSalvo);

        EmpresaPessoa empresaPessoa = new EmpresaPessoa();
        empresaPessoa.setPessoa(pessoa);
        empresaPessoa.setEmpresa(FacesUtil.getEmpresaUsuario());
        empresaPessoa.setResponsavelLegal("N");
        empresaPessoa.setEmpresaPrincipal("S");


        empresaPessoaRepository.salvar(empresaPessoa);

        PrimeFaces.current().dialog().closeDynamic(clienteSalvo);
    }

    public List<Estados> getEstado() {
        List<Estados> estados = new ArrayList<>();
        if (estados.isEmpty()) {
            estados = new LinkedList<>();
            estados.addAll(Arrays.asList(Estados.values()));
        }
        return estados;
    }

    public void instanciaCidade() {
        cidade = null;
    }

    public List<Municipio> getMunicipios(String nome) {
        cidades = new LinkedList<>();
        try {
            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("uf.sigla", cliente.getUf()));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));

            cidades = municipioRepository.getEntitys(Municipio.class, filtros, new Object[]{"nome", "codigoIbge"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidades;
    }

    public void atualizarCodIbge() {
        cliente.setMunicipioIbge(cidade.getCodigoIbge());
        cliente.setCidade(cidade.getNome());
    }


    public ClienteResumDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteResumDTO cliente) {
        this.cliente = cliente;
    }

    public Map<String, String> getTipoPessoa() {
        return tipoPessoa;
    }

    public Municipio getCidade() {
        return cidade;
    }

    public void setCidade(Municipio cidade) {
        this.cidade = cidade;
    }

    public List<Municipio> getCidades() {
        return cidades;
    }

    public void setCidades(List<Municipio> cidades) {
        this.cidades = cidades;
    }
}
