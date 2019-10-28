package com.chronos.erp.repository;

import com.chronos.erp.modelo.entidades.Colaborador;

import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

public class ColaboradorRepository extends AbstractRepository implements Serializable {


    private static final long serialVersionUID = 3521212569157231099L;


    public List<Colaborador> getColaboradoresEmpresaByNome(String nome) {
        String jpql = "select new com.chronos.modelo.entidades.Colaborador(c.id,c.pessoa.id,c.pessoa.nome)  from Colaborador c " +
                "inner join c.pessoa p " +
                "inner join p.listaEmpresa e " +
                "where  lower(p.nome) like :nome and p.colaborador = 'S' and p.id <> 1";
        TypedQuery<Colaborador> query = em.createQuery(jpql, Colaborador.class);
        query.setParameter("nome", "%" + nome.trim() + "%");
        return query.getResultList();
    }
}
