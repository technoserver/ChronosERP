package com.chronos.repository;

import com.chronos.util.jpa.ChronosEntityManagerFactory;
import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 25/09/17.
 */
public abstract class AbstractRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean autoCommit = true;
    private boolean criador = false;

    @Inject
    protected EntityManager em;

    protected <T> void salvar(T bean) {
        em.persist(bean);
    }

    protected <T> T atualizar(T bean) throws PersistenceException {
        return em.merge(bean);

    }

    public <T> T get(Class<T> classToCast, String query, Object... values) {
        Query qr = createQuery(query, values);
        return (T) qr.getResultList().stream().findFirst().orElse(null);
    }

    public <T> T get(Class<T> classToCast, List<Filtro> filtros) throws Exception {
        return get(classToCast, filtros, null);
    }

    public <T> T get(Class<T> classToCast, List<Filtro> filtros, Object[] atributos) throws Exception {
        return get(classToCast, filtros, atributos, null);
    }

    public <T> T get(Class<T> classToCast, List<Filtro> filtros, Object[] atributos, Object[] joinFetch) throws Exception {
        String jpql = montaQuery(classToCast, classToCast, atributos, joinFetch);

        jpql = montaQuery(jpql, null, null, filtros);
        Query query = queryPrepared(jpql, filtros);
        query.setFirstResult(0);

        return (T) query.getResultList().stream().findFirst().orElse(null);
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

    protected <T> int execute(String jpql, Class<T> resultClass, Object... values) throws Exception {
        int result = 0;


        TypedQuery<T> query = createQuery(jpql, resultClass, values);
        result = query.executeUpdate();

        return result;
    }

    private <T> TypedQuery<T> createQuery(String jpql, Class<T> resultClass, Object[] values) {
        TypedQuery<T> query = em.createQuery(jpql, resultClass);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object object = values[i];
                query.setParameter(i + 1, object);
            }
        }
        return query;
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

    private String montaQuery(Class clazz, Class classToCast, Object[] atributos, Object[] joinFetch) {
        String jpql = (atributos != null && atributos.length > 0) ? "SELECT NEW " + classToCast.getName() + "(o.id " : "SELECT o FROM " + clazz.getName() + " o ";
        if (atributos != null && atributos.length > 0) {
            for (Object atributo : atributos) {
                jpql += ", o." + atributo.toString();
            }
            jpql += ")  FROM " + clazz.getName() + " o ";
        }
        if (joinFetch != null) {
            for (Object jf : joinFetch) {
                jpql += " JOIN o." + jf;
            }
        }
        jpql += " WHERE 1 = 1";
        return jpql;
    }

    private String montaQuery(String jpql, String sortField, SortOrder sortOrder, List<Filtro> filters) {

        int i = 0;
        for (Filtro f : filters) {
            i++;

            jpql += " " + f.getOperadorLogico()
                    + (f.getValor().getClass() == String.class ? " LOWER(o." + f.getAtributo() + ") " : " o." + f.getAtributo() + " ")
                    + f.getOperadorRelacional() + ":valor" + i;

        }

        QueryUtil queryUtil = new QueryUtil();
        jpql = queryUtil.definirOrdenacao(jpql, sortField, sortOrder);
        return jpql;
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
        EntityTransaction trx = em.getTransaction();
        if (!trx.isActive()) {
            trx.begin();
            trx.rollback();
            trx.begin();
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
