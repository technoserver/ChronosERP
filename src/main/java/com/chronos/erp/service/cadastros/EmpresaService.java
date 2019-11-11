package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.EmpresaEndereco;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 29/09/17.
 */
public class EmpresaService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;
    private EmpresaEndereco endereco;

    @Inject
    private Repository<Empresa> repository;


    public List<Empresa> getFiliais(int idmatriz, int idempresa, String tipo) {

        List<Empresa> empresas;
        List<Filtro> filtros = new ArrayList<>();

        if (tipo.equals("M")) {
            filtros.add(new Filtro("idempresa", idmatriz));
            filtros.add(new Filtro("id", Filtro.DIFERENTE, idmatriz));
        } else {
            filtros.add(new Filtro("idempresa", idmatriz));
            filtros.add(new Filtro("id", Filtro.DIFERENTE, idempresa));
        }


        empresas = repository.getEntitys(Empresa.class, filtros, new Object[]{"razaoSocial"});

        return empresas;
    }


}
