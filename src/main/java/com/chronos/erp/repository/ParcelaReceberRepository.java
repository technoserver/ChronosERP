package com.chronos.erp.repository;

import com.chronos.erp.modelo.entidades.FinParcelaRecebimento;
import com.chronos.erp.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by john on 12/10/17.
 */
public class ParcelaReceberRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    @Transactional
    public void baixarParcela(FinParcelaRecebimento parcela) {
        em.persist(parcela);

    }

    @Transactional
    public void atualizarStatusParcela(int idparcela, int idstatus) {
        String jpql = "UPDATE  FinParcelaReceber p set p.finStatusParcela.id = ?2 where p.id = ?1";
        executeCommand(jpql, idparcela, idstatus);
    }

    @Transactional
    public void atualizarJuros(Integer idparcela, BigDecimal valorJuro) {
        String jpql = "UPDATE  FinParcelaReceber p set p.valorJuro = valorJuro + ?2 where p.id = ?1";
        executeCommand(jpql, idparcela, valorJuro);
    }

    private int executeCommand(String query, Object... values) {
        Query qr = createQuery(query, values);
        return qr.executeUpdate();
    }


    private Query createQuery(String query, Object... values) {
        Query qr = em.createQuery(query);

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                qr.setParameter(i + 1, obj);

            }
        }
        return qr;
    }

}
