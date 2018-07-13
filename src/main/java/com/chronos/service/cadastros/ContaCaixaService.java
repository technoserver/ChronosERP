package com.chronos.service.cadastros;

import com.chronos.modelo.entidades.ContaCaixa;
import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContaCaixaService implements Serializable {

    @Inject
    private Repository<ContaCaixa> repository;


    public List<ContaCaixa> getListaContaCaixaComAgencia(String nome) {
        List<ContaCaixa> contas = new LinkedList<>();
        try {
            Object[] atributos = new Object[]{"nome"};
            List<Filtro> filtros = new ArrayList<>();
            filtros.add(new Filtro(Filtro.AND, "nome", Filtro.LIKE, nome));
            filtros.add(new Filtro(Filtro.AND, "agenciaBanco", Filtro.NAO_NULO, ""));
            contas = repository.getEntitys(ContaCaixa.class, filtros, atributos);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return contas;
    }
}
