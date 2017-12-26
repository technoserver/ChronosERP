package com.chronos.controll.gerencial;

import com.chronos.controll.AbstractControll;
import com.chronos.modelo.entidades.Funcao;
import com.chronos.modelo.entidades.Papel;
import com.chronos.modelo.entidades.PapelFuncao;
import com.chronos.repository.Repository;
import com.chronos.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 04/12/17.
 */
@Named
@ViewScoped
public class PapelControll extends AbstractControll<Papel> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private Repository<PapelFuncao> repositoryPapelFuncaoRepository;
    @Inject
    private Repository<Funcao> funcaoRepository;

    private List<PapelFuncao> listaPapelFuncao;
    private PapelFuncao papelFuncao;
    private PapelFuncao papelFuncaoSelecionado;


    @Override
    public void doCreate() {
        super.doCreate();
        listaPapelFuncao = new ArrayList<>();
    }

    @Override
    public void doEdit() {
        super.doEdit();

        if (getObjeto().getAcessoCompleto().equals("N")) {
            listaPapelFuncao = repositoryPapelFuncaoRepository.getEntitys(PapelFuncao.class, "papel.id", getObjeto().getId());
        } else {
            listaPapelFuncao = new ArrayList<>();
        }
    }

    @Override
    public void salvar() {
        super.salvar();
        if (getObjeto().getAcessoCompleto().equals("S")) {
            listaPapelFuncao.stream().forEach(pf -> {
                repositoryPapelFuncaoRepository.excluir(pf);
            });
            listaPapelFuncao = new ArrayList<>();
        } else {
            listaPapelFuncao.stream().forEach(pf -> {
                repositoryPapelFuncaoRepository.atualizar(pf);
            });
        }
        setTelaGrid(false);
    }


    public void incluirPermissao() {
        papelFuncao = new PapelFuncao();
        papelFuncao.setPapel(getObjeto());
    }

    public void alterarPermissao() {
        papelFuncao = papelFuncaoSelecionado;
    }

    public void excluirPermissao() {
        if (listaPapelFuncao.contains(papelFuncaoSelecionado)) {
            repositoryPapelFuncaoRepository.excluir(papelFuncaoSelecionado);
            if (papelFuncaoSelecionado.getId() != null) {
                listaPapelFuncao.remove(papelFuncaoSelecionado);
            }
            Mensagem.addInfoMessage("Exclusão realizada com sucesso");
        }
    }

    public void salvarPermissao() {
        if (!listaPapelFuncao.contains(papelFuncao)) {
            listaPapelFuncao.add(papelFuncao);
        }
        Mensagem.addInfoMessage("Permissão salva com sucesso");
    }


    public List<Funcao> getListFuncao(String nome) {
        List<Funcao> funcoes = new ArrayList<>();
        try {
            funcoes = funcaoRepository.getEntitys(Funcao.class, "nome", nome, new Object[]{"nome"});
        } catch (Exception ex) {

        }
        return funcoes;
    }

    @Override
    protected Class<Papel> getClazz() {
        return Papel.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PAPEL";
    }

    @Override
    protected boolean auditar() {
        return false;
    }

    public List<PapelFuncao> getListaPapelFuncao() {
        return listaPapelFuncao;
    }

    public void setListaPapelFuncao(List<PapelFuncao> listaPapelFuncao) {
        this.listaPapelFuncao = listaPapelFuncao;
    }

    public PapelFuncao getPapelFuncao() {
        return papelFuncao;
    }

    public void setPapelFuncao(PapelFuncao papelFuncao) {
        this.papelFuncao = papelFuncao;
    }

    public PapelFuncao getPapelFuncaoSelecionado() {
        return papelFuncaoSelecionado;
    }

    public void setPapelFuncaoSelecionado(PapelFuncao papelFuncaoSelecionado) {
        this.papelFuncaoSelecionado = papelFuncaoSelecionado;
    }
}