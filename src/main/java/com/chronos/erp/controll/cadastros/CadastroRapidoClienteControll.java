package com.chronos.erp.controll.cadastros;

import com.chronos.erp.dto.ClienteResumDTO;
import com.chronos.erp.modelo.entidades.*;
import com.chronos.erp.modelo.enuns.Estados;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.service.ChronosException;
import com.chronos.erp.service.cadastros.PessoaService;
import com.chronos.erp.util.jpa.Transactional;
import com.chronos.erp.util.jsf.FacesUtil;
import com.chronos.erp.util.jsf.Mensagem;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class CadastroRapidoClienteControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipioRepository;
    @Inject
    private Repository<Cliente> repository;
    @Inject
    private Repository<Pessoa> pessoaRepository;
    @Inject
    private Repository<EmpresaPessoa> empresaPessoaRepository;

    @Inject
    private PessoaService service;


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


        try {


            Pessoa pessoa = new Pessoa();
            pessoa.setTipo(cliente.getTipo());
            pessoa.setNome(cliente.getNome());
            pessoa.setCliente("S");
            pessoa.setColaborador("N");
            pessoa.setTransportadora("N");
            pessoa.setFornecedor("N");
            pessoa.setEmail(cliente.getEmail());


            if (cliente.getTipo().equals("F")) {
                pessoa.setPessoaFisica(new PessoaFisica());
                pessoa.getPessoaFisica().setPessoa(pessoa);
                pessoa.getPessoaFisica().setEstadoCivil(new EstadoCivil(1));
                pessoa.getPessoaFisica().setCpf(cliente.getCpfCnpj());
                pessoa.getPessoaFisica().setDataNascimento(cliente.getDataNascimento());
            } else {
                pessoa.setPessoaFisica(new PessoaFisica());
                pessoa.getPessoaJuridica().setPessoa(pessoa);
                pessoa.getPessoaJuridica().setFantasia(cliente.getNome());
                pessoa.getPessoaJuridica().setCnpj(cliente.getCpfCnpj());
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

            Cliente cliente = new Cliente();
            cliente.setPessoa(pessoa);
            cliente.setDataCadastro(new Date());
            cliente.setSituacaoForCli(new SituacaoForCli(1));
            cliente.setAtividadeForCli(new AtividadeForCli(1));
            cliente.setBloqueado("N");
            cliente.setDesde(new Date());
            cliente.setDataAlteracao(new Date());

            Cliente clienteSalvo = service.salvarCliente(cliente, FacesUtil.getEmpresaUsuario());


            PrimeFaces.current().dialog().closeDynamic(clienteSalvo);


        } catch (Exception ex) {
            if (ex instanceof ChronosException) {
                Mensagem.addErrorMessage("", ex);
            }
        }
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
