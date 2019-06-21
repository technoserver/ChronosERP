/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll.cadastros;

import com.chronos.controll.ERPLazyDataModel;
import com.chronos.modelo.entidades.AtividadeForCli;
import com.chronos.modelo.entidades.Fornecedor;
import com.chronos.modelo.entidades.Pessoa;
import com.chronos.modelo.entidades.SituacaoForCli;
import com.chronos.modelo.enuns.TelaPessoa;
import com.chronos.modelo.view.ViewPessoaFornecedor;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import com.chronos.service.cadastros.PessoaService;
import com.chronos.util.jsf.Mensagem;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author john
 */
@Named
@ViewScoped
public class FornecedorControll extends PessoaControll<Fornecedor> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<AtividadeForCli> atividades;
    @Inject
    private Repository<SituacaoForCli> situacoes;
    @Inject
    private Repository<Pessoa> pessoas;

    @Inject
    private Repository<ViewPessoaFornecedor> viewPessoaFornecedorRepository;

    @Inject
    private PessoaService service;

    private ERPLazyDataModel<ViewPessoaFornecedor> fornecedorDataModel;

    private ViewPessoaFornecedor fornecedorSelecionado;

    private String completo;
    private String nome;
    private Integer codigo;
    private String cpfCnpj;

    @PostConstruct
    @Override
    public void init() {
        super.init();
        completo = "N";
    }


    public ERPLazyDataModel<ViewPessoaFornecedor> getFornecedorDataModel() {

        if (fornecedorDataModel == null) {
            fornecedorDataModel = new ERPLazyDataModel<>();
            fornecedorDataModel.setClazz(ViewPessoaFornecedor.class);
            fornecedorDataModel.setDao(viewPessoaFornecedorRepository);
        }
        return fornecedorDataModel;
    }


    public void pesquisar() {
        fornecedorDataModel.getFiltros().clear();
        if (!StringUtils.isEmpty(nome)) {
            fornecedorDataModel.getFiltros().add(new Filtro("nome", Filtro.LIKE, nome));
        }
        if (!StringUtils.isEmpty(cpfCnpj)) {
            fornecedorDataModel.getFiltros().add(new Filtro("cpfCnpj", Filtro.LIKE, cpfCnpj));
        }

        if (codigo != null) {
            fornecedorDataModel.getFiltros().add(new Filtro("id", Filtro.IGUAL, codigo));
        }
    }

    @Override
    public void doCreate() {
        super.doCreate();
        Pessoa pessoa = novaPessoa("N", "N", "N", "S");
        getObjeto().setPessoa(pessoa);
        getObjeto().setDesde(new Date());

        completo = "N";
    }

    @Override
    public void doEdit() {
        Fornecedor fornecedor = dao.getJoinFetch(fornecedorSelecionado.getId(), Fornecedor.class);
        setObjeto(fornecedor);
        super.doEdit();
        setTelaGrid(false);
        completo = "N";
    }

    @Override
    public void salvar() {
        Fornecedor fornecedor = null;
        try {
            if (completo.equals("S")) {
                fornecedor = service.salvarFornecedor(getObjeto(), empresa);
                setObjeto(fornecedor);
            } else {
                fornecedor = getObjeto();
                Pessoa pessoa = pessoas.getJoinFetch(fornecedor.getPessoa().getId(), Pessoa.class);
                fornecedor.setPessoa(pessoa);
                fornecedor.getPessoa().setFornecedor("S");
                fornecedor = dao.atualizar(fornecedor);

            }
            Mensagem.addInfoMessage("Fornecedor salvo com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            Mensagem.addErrorMessage("", e);
        }
    }

    public List<Pessoa> getListaPessoa(String nome) {
        List<Pessoa> listaPessoa = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));
            listaPessoa = pessoas.getEntitys(Pessoa.class, filtros, atributos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPessoa;
    }

    public List<AtividadeForCli> getListaAtividadeForCli(String nome) {
        List<AtividadeForCli> listaAtividadeForCli = new ArrayList<>();
        try {
            listaAtividadeForCli = atividades.getEntitys(AtividadeForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaAtividadeForCli;
    }

    public List<SituacaoForCli> getListaSituacaoForCli(String nome) {
        List<SituacaoForCli> listaSituacaoForCli = new ArrayList<>();
        try {
            listaSituacaoForCli = situacoes.getEntitys(SituacaoForCli.class, "nome", nome);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return listaSituacaoForCli;
    }


    @Override
    protected Class<Fornecedor> getClazz() {
        return Fornecedor.class;
    }

    @Override
    protected String getFuncaoBase() {

        return "FORNECEDOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    @Override
    public Pessoa getPessoa() {
        return getObjeto().getPessoa();
    }

    @Override
    public void setPessoa(Pessoa pessoa) {
        getObjeto().setPessoa(pessoa);
    }

    @Override
    public String getTela() {
        return TelaPessoa.CLIENTE.getCodigo();
    }

    public String getCompleto() {
        return completo;
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }

    public ViewPessoaFornecedor getFornecedorSelecionado() {
        return fornecedorSelecionado;
    }

    public void setFornecedorSelecionado(ViewPessoaFornecedor fornecedorSelecionado) {
        this.fornecedorSelecionado = fornecedorSelecionado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }
}
