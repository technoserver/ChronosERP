package com.chronos.repository;

import org.primefaces.model.SortOrder;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    public <T> void salvar(T bean) {
        em.persist(bean);
    }

    public <T> T salvarFlush(T bean) {
       T obj =   em.merge(bean);
       em.flush();
       return obj;
    }

    public <T> T atualizar(T bean) throws PersistenceException {
        return em.merge(bean);

    }

    public <T> void excluir(T bean) {
        Object updateObj = em.merge(bean);
        em.remove(updateObj);
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

    public <T> T get(Class<T> classToCast, List<Filtro> filtros, Object[] atributos, Object[] joinFetch) {
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


    protected int execute(String query, Object... values) {
        int result = 0;


        Query qr = createQuery(query, values);
        result = qr.executeUpdate();

        return result;
    }

    protected <T> int execute(String jpql, Class<T> resultClass, Object... values) {
        int result = 0;


        TypedQuery<T> query = createQuery(jpql, resultClass, values);
        result = query.executeUpdate();

        return result;
    }

    public void atualizarNamedQuery(String namedQuery, Object... values) {
        Query updateQuery = em.createNamedQuery(namedQuery);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                updateQuery.setParameter(i + 1, obj);

            }
        }
        updateQuery.executeUpdate();
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


}
