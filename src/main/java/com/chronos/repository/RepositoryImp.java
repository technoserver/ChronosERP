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
    public void salvar(T bean) throws Exception {

    }

    @Transactional
    @Override
    public T atualizar(T bean) throws Exception {
        Object updateObj = em.merge(bean);
        return (T) updateObj;
    }

    @Transactional
    @Override
    public void excluir(Class<T> clazz) throws Exception {
        Object updateObj = em.merge(clazz);
        em.remove(updateObj);
    }

    @Transactional
    @Override
    public void excluir(T bean) throws Exception {
        Object updateObj = em.merge(bean);
        em.remove(updateObj);
    }

    @Transactional
    @Override
    public void excluir(T bean, Integer id) throws Exception {
        em.remove(em.getReference(bean.getClass(), id));
    }

    @Override
    public T get(Integer id, Class<T> clazz) throws Exception {
        return em.find(clazz, id);
    }

    @Override
    public T getJoinFetch(Integer id, Class<T> clazz) throws Exception {
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
    public List<T> getAll(Class<T> clazz) throws Exception {

        Query query = createQuery("SELECT o FROM " + clazz.getName() + " o");
        List<T> beans = query.getResultList();
        return beans;
    }

    @Override
    public Long getTotalRegistros(Class<T> clazz, Map<String, Object> filters) throws Exception {
        String jpql = "SELECT COUNT(o.id) FROM " + clazz.getName() + " o WHERE 1 = 1";
        jpql = montaQuery(jpql, null, null, filters);
        Query query = queryPrepared(jpql, filters);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws Exception {
        String jpql = "SELECT COUNT(o.id) FROM " + clazz.getName() + " o WHERE 1 = 1";
        jpql = montaQuery(jpql, null, null, filters);
        Query query = queryPrepared(jpql, filters);
        return (Long) query.getSingleResult();
    }

    @Override
    public <T> List<T> getEntitysToQuery(Class<T> clazz, String query, Object... values) throws Exception {
        Query qr = createQuery(query, values);
        return qr.getResultList();
    }
    
    @Override
    public List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object... atributos) throws Exception {
        List<Filtro> filtros = new ArrayList<>();
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.IGUAL, valor));
        }

        return getEntitys(clazz, filtros, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros, Object... atributos) throws Exception {

        return getEntitys(clazz, filtros, 20, null, null, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro, String sortField, SortOrder sortOrder, Object... atributos) throws Exception {
        String jpql = atributos != null ? "SELECT NEW " + clazz.getName() + "(o.id " : "SELECT o FROM " + clazz.getName() + " o WHERE 1 = 1";

        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                jpql += ", o." + atributos[i].toString();
            }
            jpql += ")  FROM " + clazz.getName() + " o WHERE 1 = 1 ";
        }

        jpql = montaQuery(jpql, sortField, sortOrder, filters);
        Query query = queryPrepared(jpql, filters);
        query.setFirstResult(0);
        query.setMaxResults(qtdRegistro);
        List<T> beans = query.getResultList();

        return beans;
    }

    @Override
    public List<T> getEntitysPagination(Class<T> clazz, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws Exception {
        String jpql = "SELECT o FROM " + clazz.getName() + " o WHERE 1 = 1";
        jpql = montaQuery(jpql, sortField, sortOrder, filters);
        Query query = queryPrepared(jpql, filters);
        query.setFirstResult(first);
        query.setMaxResults(pageSize);
        List<T> beans = query.getResultList();
        return beans;
    }

    private String montaQuery(String jpql, String sortField, SortOrder sortOrder, List<Filtro> filters) {

        int i = 0;
        for (Filtro f : filters) {
            i++;
            String operadorRelacional ="" ;
            switch(f.getOperadorRelacional()){
                case Filtro.LIKE  :
                    operadorRelacional = " LOWER(o." + f.getAtributo() + ")"+ f.getOperadorRelacional() +" " + ":valor" + i;
                    break;
                case Filtro.NAO_NULO :
                    operadorRelacional = f.getAtributo()+" "+f.getOperadorRelacional();
                    break;
                default:
                    operadorRelacional = f.getAtributo()+" "+f.getOperadorRelacional()+ ":valor" + i;
                    break;
            }
            
            jpql += " " + f.getOperadorLogico() + " "+operadorRelacional;

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

    private String montaQuery(String jpql, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            String atributo = it.next();
            Object valor = filters.get(atributo);
            if (valor != null) {
                if (valor.getClass() == String.class) {
                    jpql += " AND LOWER(o." + atributo + ") like :" + atributo;
                } else {
                    jpql += " AND o." + atributo + " = :" + atributo;
                }
            }
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

    private Query queryPrepared(String jpql, Map<String, Object> filters) {
        Query query = em.createQuery(jpql);
        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
            String atributo = it.next();
            Object valor = filters.get(atributo);
            if (valor != null) {
                if (valor.getClass() == String.class) {
                    query.setParameter(atributo, "%" + String.valueOf(valor).toLowerCase().trim() + "%");
                } else {
                    query.setParameter(atributo, valor);
                }
            }
        }
        return query;
    }

    private Query queryPrepared(String jpql, List<Filtro> filters) {
        Query query = em.createQuery(jpql);

        int i = 0;
        for (Filtro f : filters) {
            i++;
            if (f.getValor().getClass() == String.class && f.getOperadorRelacional().equals(Filtro.LIKE)) {
                query.setParameter("valor" + i, "%" + String.valueOf(f.getValor()).trim().toLowerCase() + "%");
            } else {
                if(!f.getOperadorRelacional().equals(Filtro.NAO_NULO)){
                    query.setParameter("valor" + i, f.getValor());
                }
                
            }
        }
        return query;
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

   

}
