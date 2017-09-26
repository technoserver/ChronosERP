package com.chronos.repository;

import com.chronos.util.jpa.ChronosEntityManagerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 25/09/17.
 */
public abstract class AbstractRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean autoCommit = true;
    private boolean criador = false;

    // @Inject
    protected EntityManager em;


    public <T> T get(Class<T> classToCast, String query, Object... values) {
        Query qr = createQuery(query, values);
        return (T) qr.getResultList().stream().findFirst().orElse(null);
    }

    public <T> T get(Class<T> classToCast, Serializable primaryKey) {
        return em.find(classToCast, primaryKey);
    }

    public <T> List<T> getEntity(Class<T> classToCast, String query, Object... values) {

        Query qr = createQuery(query, values);
        return qr.getResultList();
    }


    protected int execute(String query, Object... values) throws Exception {
        int result = 0;


        Query qr = createQuery(query, values);
        result = qr.executeUpdate();

        return result;
    }


    private Query createQuery(String query, Object[] values) {
        Query qr = em.createQuery(query);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object object = values[i];
                qr.setParameter(i + 1, object);
            }
        }
        return qr;
    }

    private Query queryPrepared(String jpql, List<Filtro> filters) {
        Query query = em.createQuery(jpql);

        int i = 0;
        for (Filtro f : filters) {
            i++;
            if (f.getValor().getClass() == String.class && f.getOperadorRelacional().equals(Filtro.LIKE)) {
                query.setParameter("valor" + i, "%" + String.valueOf(f.getValor()).trim().toLowerCase() + "%");
            } else if (f.getValor().getClass() == String.class) {
                query.setParameter("valor" + i, String.valueOf(f.getValor()).trim().toLowerCase());

            } else {
                query.setParameter("valor" + i, f.getValor());
            }
        }
        return query;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }


    protected EntityManager abrirConexao() throws Exception {
        if (em == null || !em.isOpen()) {
            em = ChronosEntityManagerFactory.createEntityManager();
            em.getTransaction().begin();
        }
        return em;
    }

    public void fecharConexao() throws Exception {
        if (em != null && em.isOpen()) {
            try {
                if (em.getTransaction() != null && em.getTransaction().isActive()) {
                    if (em.getTransaction().getRollbackOnly()) {
                        em.getTransaction().rollback();
                    } else {
                        em.getTransaction().commit();
                    }
                }
            } catch (Exception e) {
                if (em.getTransaction() != null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e;
            } finally {
                em.close();
            }
        }
    }

}
