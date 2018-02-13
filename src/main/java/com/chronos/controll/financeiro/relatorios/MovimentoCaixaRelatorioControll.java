package com.chronos.controll.financeiro.relatorios;

import com.chronos.controll.AbstractRelatorioControll;
import com.chronos.modelo.entidades.PdvMovimento;
import com.chronos.modelo.entidades.view.ViewMovimentoCaixa;
import com.chronos.repository.Repository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class MovimentoCaixaRelatorioControll extends AbstractRelatorioControll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<ViewMovimentoCaixa> repository;
    private List<ViewMovimentoCaixa> movimentos;
    private PdvMovimento movimento;

    private int idmovimento;


    public void buscarMovimentos(){
        movimentos = repository.getEntitys(ViewMovimentoCaixa.class,"idmovimento",idmovimento);

    }


    public int getIdmovimento() {
        return idmovimento;
    }

    public void setIdmovimento(int idmovimento) {
        this.idmovimento = idmovimento;
    }

    public PdvMovimento getMovimento() {
        return movimento;
    }
}
