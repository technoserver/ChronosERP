package com.chronos.erp.service.cadastros;

import com.chronos.erp.modelo.entidades.Municipio;
import com.chronos.erp.repository.Filtro;
import com.chronos.erp.repository.Repository;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by john on 26/09/17.
 */
public class MunicipioService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Repository<Municipio> municipios;


    public List<Municipio> getMunicipios(String nome, String uf) {
        List<Municipio> cidades = new LinkedList<>();
        try {

            List<Filtro> filtros = new LinkedList<>();
            filtros.add(new Filtro("uf.sigla", uf));
            filtros.add(new Filtro("nome", Filtro.LIKE, nome));

            cidades = municipios.getEntitys(Municipio.class, filtros, new Object[]{"nome", "codigoIbge"});
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cidades;
    }

}
