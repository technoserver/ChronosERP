/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.repository;

import com.chronos.erp.util.jpa.Transactional;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author john
 */
public class RepositoryImp<T> implements Serializable, Repository<T> {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RepositoryImp.class);


    @Inject
    protected EntityManager em;

    @Override
    public void clear() throws PersistenceException {
        em.clear();
    }

    @Transactional
    @Override
    public void salvar(T bean) throws PersistenceException {
        em.persist(bean);

    }

    @Override
    public <S extends T> S saveAndFlush(S bean) {
        S obj = em.merge(bean);
        em.flush();
        return obj;
    }


    @Transactional
    @Override
    public void salvar(List<T> beans) throws PersistenceException {
        int i = 0;
        for (T t : beans) {
            i++;
            em.persist(t);
            if (i % 50 == 0) {
                em.flush();
                em.clear();
            }

        }

    }

    @Transactional
    @Override
    public T atualizar(T bean) throws PersistenceException {
        return em.merge(bean);

    }


    @Transactional
    @Override
    public void atualizar(Class<T> clazz, List<Filtro> filtros, Map<String, Object> atributos) throws PersistenceException {
        StringBuilder jpql = new StringBuilder("UPDATE " + clazz.getName() + " o SET ");

        int i = 1;
        for (String atributo : atributos.keySet()) {
            Object valor = atributos.get(atributo);

            jpql.append("o.").append(atributo).append(" = ").append((valor.getClass() == String.class) ? "'" + valor + "'," : valor + ",");
        }

        jpql = new StringBuilder(jpql.substring(0, jpql.length() - 1));

        jpql.append(" where 1=1 ");
        for (Filtro f : filtros) {
            jpql.append(f.getOperadorLogico());
            jpql.append(" o.");
            jpql.append(f.getAtributo());
            jpql.append(" ");
            jpql.append(f.getOperadorRelacional());
            jpql.append((f.getValor().getClass() == String.class) ? "'" + f.getValor() + "'" : f.getValor());
            jpql.append(" ");
        }
        executeCommand(jpql.toString().trim());


    }

    @Transactional
    @Override
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


    @Transactional
    @Override
    public boolean updateNativo(Class<T> clazz, List<Filtro> filtros, Map<String, Object> atributos) {
        StringBuilder sql = new StringBuilder("update " + clazz.getAnnotation(Table.class).name() + " set ");

        for (String atributo : atributos.keySet()) {
            Object valor = atributos.get(atributo);

            sql.append(atributo).append(" = ").append((valor.getClass() == String.class) ? "'" + valor + "'," : valor + ",");
        }
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));

        sql.append(" where 1=1 ");
        for (Filtro f : filtros) {
            sql.append(f.getOperadorLogico()).append(" ").append(f.getAtributo()).append(" ").append(f.getOperadorRelacional()).append((f.getValor().getClass() == String.class) ? "'" + f.getValor() + "'" : f.getValor());
        }


        return executarQueryNativa(sql.toString());
    }


    @Override
    public void excluir(Class<T> clazz) throws PersistenceException {
        excluir(clazz, new ArrayList<>());
    }

    @Transactional
    @Override
    public void excluir(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        List<Filtro> filtros = new ArrayList<>();
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.IGUAL, valor));
        }

        excluir(clazz, filtros);
    }

    @Transactional
    @Override
    public void excluir(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {
        String jpql = "delete  FROM " + clazz.getName() + " o";
        jpql += " where 1=1 ";
        jpql = montaQuery(jpql, null, null, filtros);
        Query q = queryPrepared(jpql, filtros);

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
        String jpql = "select count(o.id) from " + clazz.getName() + " o where o." + atributo + "=:valor";
        Query query = em.createQuery(jpql);
        query.setParameter("valor", valor);
        return (Long) query.getSingleResult() > 0;

    }

    @Override
    public boolean existeRegisro(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {

        String jpql = "select count(o.id) from " + clazz.getName() + " o where 1=1 ";
        jpql = montaQuery(jpql, null, null, filtros);

        Query query = queryPrepared(jpql, filtros);

        return (Long) query.getSingleResult() > 0;
    }

    @Override
    public Object getObject(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException {
        StringBuilder jpql = new StringBuilder("SELECT o.id ");

        if (atributos != null && atributos.length > 0) {
            for (Object obj : atributos) {
                jpql.append(", o.").append(obj.toString());
            }
            jpql.append(" FROM ").append(clazz.getName()).append(" o ");
        }
        jpql.append(" WHERE o.").append(atributo).append(" = :valor");
        Query query = em.createQuery(jpql.toString());
        query.setParameter("valor", valor);
        Object obj = query.getResultList().stream().findFirst().orElse(null);
        return obj;
    }


    @Override
    public <T> T getNamedQuery(Class<T> clazz, String namedQuery, Object... atributos) throws PersistenceException {

        TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                Object obj = atributos[i];
                query.setParameter(i + 1, obj);

            }
        }

        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public T get(Integer id, Class<T> clazz) throws PersistenceException {
        return em.find(clazz, id);
    }

    @Override
    public T get(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        return get(clazz, atributo, valor, null);
    }


    @Override
    public Optional<T> getOptional(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        Query q = em.createQuery("SELECT o  FROM " + clazz.getName() + " o WHERE o." + atributo + " = :valor");
        q.setParameter("valor", valor);
        return q.getResultList().stream().findFirst();


    }

    @Override
    public T get(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException {
        return getEntitys(clazz, atributo, valor, atributos).stream().findFirst().orElse(null);
    }

    @Override
    public T get(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {
        List<T> objetos = getEntitys(clazz, filtros);
        return objetos.stream().findFirst().orElse(null);
    }

    @Override
    public T get(Class<T> clazz, List<Filtro> filtros, Object[] atributos) throws PersistenceException {
        List<T> objetos = getEntitys(clazz, filtros, atributos);
        return objetos.stream().findFirst().orElse(null);
    }


    @SuppressWarnings("unchecked")
    @Override
    public T getJoinFetch(Integer id, Class<T> clazz) throws PersistenceException {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT o FROM " + clazz.getName() + " o");
        Field fields[] = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == Set.class) {
                jpql.append(" LEFT JOIN FETCH o.").append(f.getName());
            }
        }

        jpql.append(" WHERE o.id = :id");

        Query query = em.createQuery(jpql.toString());
        query.setParameter("id", id);
        try {
            Object obj = query.getSingleResult();
            return (T) obj;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public T getJoinFetchList(Integer id, Class<T> clazz) throws PersistenceException {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT o FROM " + clazz.getName() + " o");
        Field fields[] = clazz.getDeclaredFields();
        for (Field f : fields) {
            if (f.getType() == List.class) {
                jpql.append(" LEFT JOIN FETCH o.").append(f.getName());
            }
        }

        jpql.append(" WHERE o.id = :id");

        Query query = em.createQuery(jpql.toString());
        query.setParameter("id", id);
        try {
            Object obj = query.getSingleResult();
            return (T) obj;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public List<T> getAll(Class<T> clazz) throws PersistenceException {

        Query query = createQuery("SELECT o FROM " + clazz.getName() + " o");
        return query.getResultList();
    }


    @Override
    public Long getTotalRegistros(Class<T> clazz, String atributo, Object valor) throws PersistenceException {
        List<Filtro> filtros = new ArrayList<>();
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.IGUAL, valor));
        }

        return getTotalRegistros(clazz, filtros);
    }

    @Override
    public Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws PersistenceException {
        String jpql = "SELECT COUNT(o.id) FROM " + clazz.getName() + " o WHERE 1 = 1";
        jpql = montaQuery(jpql, null, null, filters);
        Query query = queryPrepared(jpql, filters);
        return (Long) query.getSingleResult();
    }

    @Override
    public Object getMaxValor(Class<T> clazz, String atributo, List<Filtro> filters) {
        String jpql = "SELECT MAX(o." + atributo + ") FROM " + clazz.getName() + " o WHERE 1 = 1";

        int i = 0;
        for (Filtro f : filters) {
            i++;

            jpql += " " + f.getOperadorLogico()
                    + (f.getValor().getClass() == String.class ? " LOWER(o." + f.getAtributo() + ") " : " o." + f.getAtributo() + " ")
                    + f.getOperadorRelacional() + ":valor" + i;

        }
        Query query = queryPrepared(jpql, filters);
        return Optional.ofNullable(query.getSingleResult()).orElse(0);
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


        return getEntitys(clazz, atributo, valor, atributos, null);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object[] atributos, Object[] joins) throws PersistenceException {
        List<Filtro> filtros = new ArrayList<>();
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.IGUAL, valor));
        }

        return getEntitys(clazz, filtros, atributos, joins);
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
    public <T> List<T> getEntitysNamedQuery(Class<T> clazz, String namedQuery, Object... atributos) throws PersistenceException {

        TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
        if (atributos != null) {
            for (int i = 0; i < atributos.length; i++) {
                Object obj = atributos[i];
                query.setParameter(i + 1, obj);

            }
        }

        return query.getResultList();
    }

    @Override
    public <T> List<T> getEntitysNamedQueryParam(Class<T> clazz, String namedQuery, int[] values, String paramentro) throws PersistenceException {

        TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
        query.setParameter(paramentro, Arrays.asList(values));

        return query.getResultList();
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros) throws PersistenceException {
        Object atributos[] = null;
        return getEntitys(clazz, filtros, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros, Object[] atributos) throws PersistenceException {

        return getEntitys(clazz, filtros, 0, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filtros, Object[] atributos, Object[] joinFetch) throws PersistenceException {

        return getEntitys(clazz, filtros, 0, 0, null, null, joinFetch, atributos);
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

        return getEntitys(clazz, filters, 0, qtdRegistro, sortField, sortOrder, null);
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
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro, String sortField, SortOrder sortOrder, Object[] atributos) {
        return getEntitys(clazz, filters, 0, qtdRegistro, sortField, sortOrder, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException {

        String[] joinFecth = null;
        return getEntitys(clazz, filters, first, pageSize, sortField, sortOrder, joinFecth, atributos);
    }

    @Override
    public List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] joinFetch, Object[] atributos) throws PersistenceException {
        return getTs(clazz, clazz, filters, first, pageSize, sortField, sortOrder, joinFetch, atributos);
    }

    @Override
    public <T1> List<T1> executeQuery(Class<T1> classToCast, String query, int[] ids) {
        List<Integer> idss = Arrays.stream(ids)
                .boxed()
                .collect(Collectors.toList());
        Query qr = em.createQuery(query);
        qr.setParameter("ids", idss);
        return qr.getResultList();
    }

    /**
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
        return getTs(clazz, classToCast, filters, first, pageSize, sortField, sortOrder, joinFetch, atributos);
    }

    private List<T> getTs(Class<T> clazz, Class classToCast, List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder, Object[] joinFetch, Object[] atributos) {
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

    private int executeCommand(String query, Object... values) {
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
                jpql += " LEFT JOIN FETCH o." + jf;
            }
        }
        jpql += " WHERE 1 = 1";
        return jpql;
    }

    private String montaQuery(String jpql, String sortField, SortOrder sortOrder, List<Filtro> filters) {

        int i = 0;
        StringBuilder jpqlBuilder = new StringBuilder(jpql);
        for (Filtro f : filters) {
            i++;

            jpqlBuilder.append(" ");
            jpqlBuilder.append(f.getOperadorLogico());
            jpqlBuilder.append(f.isAbriExpresao() ? " (" : "");
            if (f.getOperadorRelacional().equals(Filtro.NAO_NULO)) {
                jpqlBuilder.append(" o." + f.getAtributo() + " ");
                jpqlBuilder.append(Filtro.NAO_NULO);
            } else if (f.getOperadorRelacional().equals(Filtro.BETWEEN)) {
                jpqlBuilder.append(" o." + f.getAtributo() + " ");
                jpqlBuilder.append(Filtro.BETWEEN);
                jpqlBuilder.append(" :valor" + i + "A");
                jpqlBuilder.append(" AND ");
                jpqlBuilder.append(" :valor" + i + "B");
            } else if (f.getOperadorRelacional().equals(Filtro.IN)) {
                jpqlBuilder.append(" o." + f.getAtributo() + " ");
                jpqlBuilder.append(Filtro.IN);
                jpqlBuilder.append(" (");

                for (int y = 0; y < f.getValores().length; y++) {
                    jpqlBuilder.append(":valor" + i + y);
                    if (y < f.getValores().length - 1) {
                        jpqlBuilder.append(",");
                    }
                }
                jpqlBuilder.append(" )");
            } else if (f.getOperadorRelacional().equals(Filtro.LIKE) && f.getValor().getClass() == String.class) {
                jpqlBuilder.append(" LOWER(o." + f.getAtributo() + ") ");
                jpqlBuilder.append(Filtro.LIKE);
                jpqlBuilder.append(" :valor").append(i);
            } else if (f.getValor() instanceof Date) {
                jpqlBuilder.append(" o." + f.getAtributo() + " ");
                jpqlBuilder.append(f.getOperadorRelacional());
                jpqlBuilder.append(" :valor").append(i);
            } else {
                jpqlBuilder.append(" o." + f.getAtributo() + " ");
                jpqlBuilder.append(f.getOperadorRelacional());
                jpqlBuilder.append(" :valor").append(i);
            }

            jpqlBuilder.append(f.isFecharExpresao() ? " )" : "");

        }
        jpql = jpqlBuilder.toString();
        QueryUtil queryUtil = new QueryUtil();
        jpql = queryUtil.definirOrdenacao(jpql, sortField, sortOrder);
        return jpql;
    }

    private String addCondicao(List<Filtro> filters, String andOr) {
        StringBuilder jpql = new StringBuilder();
        if (!filters.isEmpty()) {
            jpql = new StringBuilder(" AND (");
            boolean primeiraCondicao = true;
            for (Filtro f : filters) {
                String atributo = f.getAtributo();
                Object valor = f.getValor();
                if (valor != null) {
                    if (valor.getClass() == String.class) {
                        if (primeiraCondicao) {
                            jpql.append(" LOWER(o.").append(atributo).append(") like :").append(atributo.replaceAll("\\.", ""));
                            primeiraCondicao = false;
                        } else {
                            jpql.append(" ").append(andOr).append(" LOWER(o.").append(atributo).append(") like :").append(atributo.replaceAll("\\.", ""));
                        }
                    } else if (valor instanceof Date) {
                        jpql.append(" ").append(andOr).append(" TRUNC(o.").append(atributo).append(") = :").append(atributo.replaceAll("\\.", ""));
                    } else {
                        if (primeiraCondicao) {
                            jpql.append(" o.").append(atributo).append(" = :").append(atributo.replaceAll("\\.", ""));
                            primeiraCondicao = false;
                        } else {
                            jpql.append(" ").append(andOr).append(" o.").append(atributo).append(" = :").append(atributo.replaceAll("\\.", ""));
                        }
                    }
                }
            }
            jpql.append(")");
        }

        return jpql.toString();
    }

    private Query queryPrepared(String jpql, List<Filtro> filters) {
        Query query = em.createQuery(jpql);

        int i = 0;
        for (Filtro f : filters) {
            i++;

            if (f.getOperadorRelacional().equals(Filtro.NAO_NULO)) {

            } else if (f.getOperadorRelacional().equals(Filtro.IN)) {

                for (int y = 0; y < f.getValores().length; y++) {
                    query.setParameter("valor" + i + y, f.getValores()[y]);
                }

            } else if (f.getOperadorRelacional().equals(Filtro.BETWEEN)) {
                castParameter(query, "valor" + i + "A", f.getValores()[0]);
                castParameter(query, "valor" + i + "B", f.getValores()[1]);
            } else if (f.getValor().getClass() == String.class && f.getOperadorRelacional().equals(Filtro.LIKE)) {
                query.setParameter("valor" + i, "%" + String.valueOf(f.getValor()).trim().toLowerCase() + "%");
            } else if (f.getValor().getClass() == String.class) {
                query.setParameter("valor" + i, String.valueOf(f.getValor()).trim());
            } else {
                castParameter(query, "valor" + i, f.getValor());

            }
        }
        return query;
    }

    private void castParameter(Query query, String parametro, Object valor) {
        if (valor instanceof Date) {
            Date data = (Date) valor;
            query.setParameter(parametro, data, TemporalType.DATE);
        } else {
            query.setParameter(parametro, valor);
        }
    }

    @Transactional
    private boolean executarQueryNativa(String query) {
        boolean result = false;
        try {

            result = em.createNativeQuery(query).executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("Erro ao Executa sql " + ex.getMessage());

        }

        return result;
    }


}
