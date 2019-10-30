package com.chronos.erp.repository;

import com.chronos.erp.modelo.entidades.OperadoraCartao;

import javax.persistence.TypedQuery;
import java.util.List;

public class OperadoraCartaoRepository extends AbstractRepository {


    public List<OperadoraCartao> getOperadoraResumidaComintervalo() {
        String jpql = "SELECT DISTINCT o FROM OperadoraCartao o JOIN o.contaCaixa LEFT JOIN FETCH o.listaOperadoraCartaoTaxas";
        TypedQuery<OperadoraCartao> query = em.createQuery(jpql, OperadoraCartao.class);
        List<OperadoraCartao> operadoras = query.getResultList();
        return operadoras;
    }

    public List<OperadoraCartao> getOperadoraResumidaComintervalo(String nome) {
        String jpql = "SELECT DISTINCT o FROM OperadoraCartao o JOIN o.contaCaixa LEFT JOIN FETCH o.listaOperadoraCartaoTaxas where lower(o.nome) like ?1";
        List<OperadoraCartao> operadoras = getEntity(OperadoraCartao.class, jpql, "%" + nome + "%");
        return operadoras;
    }
}
