package com.chronos.repository;

import com.chronos.modelo.entidades.OperadoraCartao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

public class OperadoraCartaoRepository implements Serializable {

    @Inject
    protected EntityManager em;


    public List<OperadoraCartao> getOperadoraResumidaComintervalo() {
        String jpql = "SELECT DISTINCT o FROM OperadoraCartao o JOIN o.contaCaixa LEFT JOIN FETCH o.listaOperadoraCartaoTaxas";
        TypedQuery<OperadoraCartao> query = em.createQuery(jpql, OperadoraCartao.class);
        List<OperadoraCartao> operadoras = query.getResultList();
        return operadoras;
    }
}
