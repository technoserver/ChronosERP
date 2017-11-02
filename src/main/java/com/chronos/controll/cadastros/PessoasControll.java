package com.chronos.controll.cadastros;

import com.chronos.controll.AbstractControll;
import com.chronos.controll.cadastros.datamodel.PessoaDataModel;
import com.chronos.modelo.entidades.*;
import com.chronos.modelo.entidades.view.ViewPessoa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 30/10/17.
 */
@Named
@ViewScoped
public class PessoasControll extends AbstractControll<Pessoa> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<ViewPessoa> viewPessoaRepository;
    @Inject
    private PessoaService service;
    @Inject
    private Repository<Municipio> municipios;
    @Inject
    private Repository<EstadoCivil> estadosCivis;

    private PessoaDataModel pessoaDataModel;
    private PessoaContato pessoaContatoSelecionado;
    private PessoaContato pessoaContato;
    private PessoaEndereco pessoaEnderecoSelecionado;
    private PessoaEndereco pessoaEndereco;
    private PessoaEndereco endereco;
    private PessoaTelefone pessoaTelefoneSelecionado;
    private PessoaTelefone pessoaTelefone;
    private ViewPessoa pessoaSelecionada;

    private Municipio cidade;
    private List<Municipio> cidades;


    public PessoaDataModel getPessoaDataModel() {

        if (pessoaDataModel == null) {
            pessoaDataModel = new PessoaDataModel();
            pessoaDataModel.setDao(viewPessoaRepository);
            pessoaDataModel.setClazz(ViewPessoa.class);
        }

        return pessoaDataModel;
    }

    @Override
    public void doCreate() {
        super.doCreate();


        getObjeto().setListaPessoaContato(new HashSet<>());
        getObjeto().setListaPessoaEndereco(new HashSet<>());
        getObjeto().setListaPessoaTelefone(new HashSet<>());

        endereco = new PessoaEndereco();
        endereco.setPrincipal("S");
        endereco.setCobranca("S");
        endereco.setCorrespondencia("S");
        endereco.setEntrega("S");

        endereco.setPessoa(getObjeto());
        getObjeto().getListaPessoaEndereco().add(endereco);

        PessoaFisica pf = new PessoaFisica();

        getObjeto().setTipo("F");
        getObjeto().setPessoaFisica(pf);
        pf.setPessoa(getObjeto());

    }

    @Override
    public void doEdit() {
        super.doEdit();
        Pessoa p = dataModel.getRowData(String.valueOf(pessoaSelecionada.getId()));
        setObjeto(p);

        endereco = p.getListaPessoaEndereco().stream().filter((e) -> e.getPrincipal().equals("S")).findFirst().orElse(new PessoaEndereco());
        cidade = new Municipio(0, endereco.getCidade(), endereco.getMunicipioIbge());
    }

    @Override
    public void salvar() {
        try {
            service.salvar(getObjeto(), empresa);
            Mensagem.addInfoMessage("Dados salvo com sucesso");
        } catch (Exception ex) {
            ex.printStackTrace();
            Mensagem.addErrorMessage("", ex);
        }
    }

    public void definirTipo() {
        Pessoa pessoa = getObjeto();
        if (pessoa.getTipo().equals("F")) {
            pessoa.setPessoaJuridica(null);
            PessoaFisica pf = new PessoaFisica();
            pessoa.setTipo("F");
            pessoa.setPessoaFisica(pf);
            pf.setPessoa(pessoa);
        } else {
            pessoa.setPessoaFisica(null);
            PessoaJuridica pj = new PessoaJuridica();
            pessoa.setTipo("J");
            pessoa.setPessoaJuridica(pj);
            pj.setPessoa(pessoa);
        }
    }

    public void incluirContato() {
        pessoaContato = new PessoaContato();
        pessoaContato.setPessoa(getObjeto());
    }

    public void alterarContato() {
        pessoaContato = pessoaContatoSelecionado;
    }

    public void salvarContato() {
        if (pessoaContato.getId() == null) {
            getObjeto().getListaPessoaContato().add(pessoaContato);
        }
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Contato salvo com sucesso!");

    }

    public void excluirContato() {
        getObjeto().getListaPessoaContato().remove(pessoaContatoSelecionado);
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Contato excluído com sucesso!");


    }

    public void incluirEndereco() {
        pessoaEndereco = new PessoaEndereco();
        pessoaEndereco.setPessoa(getObjeto());
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
            getObjeto().getListaPessoaEndereco().add(pessoaEndereco);
        }
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Endereço salvo com sucesso!");

    }

    public void excluirEndereco() {
        getObjeto().getListaPessoaEndereco().remove(pessoaEnderecoSelecionado);
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Endereço excluído com sucesso!");

    }

    public void incluirTelefone() {
        pessoaTelefone = new PessoaTelefone();
        pessoaTelefone.setPessoa(getObjeto());
    }

    public void alterarTelefone() {
        pessoaTelefone = pessoaTelefoneSelecionado;
    }

    public void salvarTelefone() {
        if (pessoaTelefone.getId() == null) {
            getObjeto().getListaPessoaTelefone().add(pessoaTelefone);
        }
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Telefone salvo com sucesso!");

    }

    public void excluirTelefone() {
        Pessoa pessoa = getObjeto();
        pessoa.getListaPessoaTelefone().remove(pessoaTelefoneSelecionado);
        dao.atualizar(getObjeto());
        Mensagem.addInfoMessage("Telefone excluído com sucesso!");

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

    public void atualizarCodIbge() {
        endereco.setMunicipioIbge(cidade.getCodigoIbge());
        endereco.setCidade(cidade.getNome());
    }

    public void instanciaCidade() {
        cidade = null;
    }

    public List<EstadoCivil> getListaEstadoCivil(String nome) {
        List<EstadoCivil> listaEstadoCivil = new ArrayList<>();
        try {
            listaEstadoCivil = estadosCivis.getEntitys(EstadoCivil.class, "nome", nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEstadoCivil;
    }


    @Override
    protected Class<Pessoa> getClazz() {
        return Pessoa.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PESSOA";
    }

    @Override
    protected boolean auditar() {
        return false;
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

    public ViewPessoa getPessoaSelecionada() {
        return pessoaSelecionada;
    }

    public void setPessoaSelecionada(ViewPessoa pessoaSelecionada) {
        this.pessoaSelecionada = pessoaSelecionada;
    }
}
