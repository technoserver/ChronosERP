package com.chronos.erp.controll.cadastros;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.ProdutoAtributo;
import com.chronos.erp.modelo.entidades.ProdutoGrade;
import com.chronos.erp.modelo.entidades.ProdutoGradeDetalhe;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Named
@ViewScoped
public class ProdutoGradeControll extends AbstractControll<ProdutoGrade> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ProdutoAtributo> atributoRepository;

    private ProdutoGradeDetalhe gradeSelecionada;
    private ProdutoGradeDetalhe grade;
    private ProdutoAtributo atributo;


    @Override
    public void doEdit() {
        super.doEdit();
        ProdutoGrade grade = dao.getJoinFetchList(getObjetoSelecionado().getId(), ProdutoGrade.class);
        setObjeto(grade);
    }

    @Override
    public void salvar() {

        if (getObjeto().getListaProdutoGradeDetalhe().isEmpty()) {
            Mensagem.addErrorMessage("Atributos não definido");
        } else {
            super.salvar();
        }

    }

    public void salvarAtributo() {

        if (atributo == null) {
            Mensagem.addErrorMessage("Atributo não definido");
        } else if (getObjeto().getListaProdutoGradeDetalhe().stream().filter(g -> g.getProdutoAtributo().equals(atributo)).count() > 0) {
            Mensagem.addErrorMessage("Atributo já definido");
        } else {
            grade = new ProdutoGradeDetalhe();
            grade.setProdutoAtributo(atributo);
            grade.setProdutoGrade(getObjeto());
            getObjeto().getListaProdutoGradeDetalhe().add(grade);
            atributo = null;
        }


    }

    public void removerAtributo() {
        int idx = IntStream.range(0, getObjeto().getListaProdutoGradeDetalhe().size())
                .filter(i -> getObjeto().getListaProdutoGradeDetalhe().get(i).getProdutoAtributo().equals(gradeSelecionada.getProdutoAtributo()))
                .findAny().getAsInt();
        getObjeto().getListaProdutoGradeDetalhe().remove(idx);

    }

    public List<ProdutoAtributo> getListaAtributos(String nome) {
        List<ProdutoAtributo> atributos = new ArrayList<>();
        atributos = atributoRepository.getEntitys(ProdutoAtributo.class, "nome", nome);

        return atributos;
    }

    @Override
    protected Class<ProdutoGrade> getClazz() {
        return ProdutoGrade.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PRODUTO_GRADE";
    }

    @Override
    protected boolean auditar() {
        return false;
    }


    public ProdutoGradeDetalhe getGradeSelecionada() {
        return gradeSelecionada;
    }

    public void setGradeSelecionada(ProdutoGradeDetalhe gradeSelecionada) {
        this.gradeSelecionada = gradeSelecionada;
    }

    public ProdutoAtributo getAtributo() {
        return atributo;
    }

    public void setAtributo(ProdutoAtributo atributo) {
        this.atributo = atributo;
    }
}
