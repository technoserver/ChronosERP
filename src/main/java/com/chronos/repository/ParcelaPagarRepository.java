package com.chronos.repository;

import com.chronos.modelo.entidades.FinParcelaPagamento;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;

/**
 * Created by john on 12/10/17.
 */
public class ParcelaPagarRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    @Transactional
    public void baixarParcela(FinParcelaPagamento parcela) {
        em.persist(parcela);

    }

    @Transactional
    public void atualizarStatusParcela(int idparcela, int idstatus) {
        String jpql = "UPDATE  FinParcelaPagar p set p.finStatusParcela.id = ?2 where p.id = ?1";
        executeCommand(jpql, idparcela, idstatus);
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
