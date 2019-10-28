package com.chronos.erp.controll.pdv;

import com.chronos.erp.controll.AbstractControll;
import com.chronos.erp.modelo.entidades.Colaborador;
import com.chronos.erp.modelo.entidades.PdvOperador;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;
import com.chronos.erp.util.jsf.Mensagem;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 01/06/18.
 */
@Named
@ViewScoped
public class PdvOperadorControll extends AbstractControll<PdvOperador> implements Serializable {

    private static final long serialVersionUID = 1L;


    @Inject
    private Repository<Colaborador> colaboradores;

    @Override
    public void salvar() {

        PdvOperador operador = dao.get(PdvOperador.class, "login", getObjeto().getLogin());

        if (operador != null && !operador.equals(getObjeto())) {
            Mensagem.addErrorMessage("Operador já cadastrado");
        } else {
            super.salvar();
        }


    }

    public List<Colaborador> getListColaborador(String nome) {
        List<Colaborador> list = new ArrayList<>();
        try {
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro("pessoa.nome", Filtro.LIKE, nome));
            filtros.add(new Filtro("pessoa.id", Filtro.DIFERENTE, 1));
            filtros.add(new Filtro("pessoa.colaborador", "S"));
            list = colaboradores.getEntitys(Colaborador.class, filtros, new Object[]{"pessoa.id", "pessoa.nome"});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    protected Class<PdvOperador> getClazz() {
        return PdvOperador.class;
    }

    @Override
    protected String getFuncaoBase() {
        return "PDV_OPERADOR";
    }

    @Override
    protected boolean auditar() {
        return false;
    }
}
