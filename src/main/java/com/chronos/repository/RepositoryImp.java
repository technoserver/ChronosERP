/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import com.chronos.util.jpa.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.primefaces.model.SortOrder;

/**
 *
 * @author john
 */
public class RepositoryImp<T> implements Serializable, Repository<T> {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    @Override
    public void clear() throws PersistenceException {
        em.clear();
    }

    @Transactional
    @Override
    public void salvar(T bean) throws PersistenceException {
        em.persist(bean);

    }

    @Transactional
    @Override
    public T atualizar(T bean) throws PersistenceException {
        Object updateObj = em.merge(bean);
        return (T) updateObj;
    }

    @Transactional
    @Override
    public void excluir(Class<T> clazz) throws PersistenceException {
        String jpql = "delete  FROM " + clazz.getName();
        Query q = em.createQuery(jpql);

        q.executeUpdate();
    }

    @Transactional
    @Override
    public void excluir(T bean) throws PersistenceException {
        Object updateObj = em.merge(bean);
        em.remove(updateObj);
    }

    @Transactional
    @Override
    public void excluir(T bean, Integer id) throws PersistenceException {
        em.remove(em.getReference(bean.getClass(), id));
    }

    @Override
    public boolean existeRegisro(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        return false;
    }

    @Override
    public T get(Integer id, Class<T> clazz) throws PersistenceException {
        return em.find(clazz, id);
    }

    @Override
    public T get(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        return getEntitys(clazz,atributo,valor).get(0);
    }

    @Override
    public T get(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {
        return getEntitys(clazz,filtros).get(0);
    }


    @Override
    public T getJoinFetch(Integer id, Class<T> clazz) throws PersistenceException {
        String jpql = "SELECT DISTINCT o FROM " + clazz.getName() + " o";
        Field fields[] = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == Set.class) {
                jpql += " LEFT JOIN FETCH o." + f.getName();
            }
        }

        jpql += " WHERE o.id = :id";

        Query query = em.createQuery(jpql);
        query.setParameter("id", id);
        try {
            return (T) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<T> getAll(Class<T> clazz) throws PersistenceException {

        Query query = createQuery("SELECT o FROM " + clazz.getName() + " o");
        List<T> beans = query.getResultList();
        return beans;
    }



    @Override
    public Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws PersistenceException {
        String jpql = "SELECT COUNT(o.id) FROM " + clazz.getName() + " o WHERE 1 = 1";
        jpql = montaQuery(jpql, null, null, filters);
        Query query = queryPrepared(jpql, filters);
        return (Long) query.getSingleResult();
    }

    @Override
    public T getEntityJoinFetch(Integer id, Class<T> clazz) throws PersistenceException {
        return null;
    }

    @Override
    public <T> List<T> getEntitysToQuery(Class<T> clazz, String query, Object... values) throws PersistenceException {
        Query qr = createQuery(query, values);
        return qr.getResultList();
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, atributo, valor, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException {
        List<Filtro> filtros = new ArrayList<>();
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.IGUAL, valor));
        }

        return getEntitys(clazz, filtros, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz) throws PersistenceException {

        Object atributos[] = null;
        return getEntitys(clazz, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, Object[] atributos) throws PersistenceException {
        String jpql = (atributos != null && atributos.length > 0) ? "SELECT NEW " + clazz.getName() + "(o.id " : "SELECT o FROM " + clazz.getName() + " o ";

        if (atributos != null && atributos.length > 0) {
            for (Object atributo : atributos) {
                jpql += ", o." + atributo.toString();
            }
            jpql += ")  FROM " + clazz.getName() + " o ";
        }
        jpql += " WHERE 1 = 1";
        Query query = em.createQuery(jpql);
        List<T> beans = query.getResultList();

        return beans;
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filtros, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros, Object[] atributos) throws PersistenceException {

        return getEntitys(clazz, filtros, 20, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filters, qtdRegistro, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro, Object[] atributos) throws PersistenceException {
        return getEntitys(clazz, filters, 0, qtdRegistro, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filters, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException {

        return getEntitys(clazz, filters, 0, 0, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro, String sortField, SortOrder sortOrder) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filters, 0, qtdRegistro, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro, String sortField, SortOrder sortOrder) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filters, first, qtdRegistro, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro, Object[] atributos) throws PersistenceException {
        return getEntitys(clazz, filters, first, qtdRegistro, null, null, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro, String sortField, SortOrder sortOrder, Object[] atributos) throws Exception {
        return getEntitys(clazz, filters, 0, qtdRegistro, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException {

        String[] joinFecth = null;
        return getEntitys(clazz, filters, first, pageSize, sortField, sortOrder, joinFecth, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] joinFetch, Object[] atributos) throws PersistenceException {
        String jpql = montaQuery(clazz, clazz, atributos, joinFetch);

        jpql = montaQuery(jpql, sortField, sortOrder, filters);
        Query query = queryPrepared(jpql, filters);
        query.setFirstResult(first);
        if (pageSize > 0) {
            query.setMaxResults(pageSize);
        }
        List<T> beans = query.getResultList();

        return beans;
    }

    /**
     *
     * @param clazz
     * @param classToCast
     * @param filters
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param joinFetch
     * @param atributos
     * @return
     * @throws Exception
     */
    @Override
    public List<T> getEntitys(Class<T> clazz, Class classToCast, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] joinFetch, Object[] atributos) throws PersistenceException {
        String jpql = montaQuery(clazz, classToCast, atributos, joinFetch);

        jpql = montaQuery(jpql, sortField, sortOrder, filters);
        Query query = queryPrepared(jpql, filters);
        query.setFirstResult(first);
        if (pageSize > 0) {
            query.setMaxResults(pageSize);
        }
        List<T> beans = query.getResultList();

        return beans;
    }

    protected int executeCommand(String query, Object... values) {
        Query qr = createQuery(query, values);
        return qr.executeUpdate();
    }



    protected Query createQuery(String query, Object... values) {
        Query qr = em.createQuery(query);

        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                qr.setParameter(i + 1, obj);

            }
        }
        return qr;
    }

    private String montaQuery(Class<T> clazz, Class classToCast, Object[] atributos, Object[] joinFetch) {
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

        if (sortField != null && sortOrder != null) {
            if (sortOrder.equals(SortOrder.ASCENDING)) {
                jpql += " ORDER BY o." + sortField + " ASC";
            } else if (sortOrder.equals(SortOrder.DESCENDING)) {
                jpql += " ORDER BY o." + sortField + " DESC";
            }
        }
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
