/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author john
 * @param <T>
 */
public interface Repository<T> {

    void salvar(T bean) throws Exception;

    T atualizar(T bean) throws Exception;

    void excluir(Class<T> clazz) throws Exception;

    void excluir(T bean) throws Exception;

    void excluir(T bean, Integer id) throws Exception;

    T get(Integer id, Class<T> clazz) throws Exception;
    
    T getJoinFetch(Integer id, Class<T> clazz) throws Exception;

    List<T> getAll(Class<T> clazz) throws Exception;

    Long getTotalRegistros(Class<T> clazz, Map<String, Object> filters) throws Exception;

    Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws Exception;

    List<T> getEntitysPagination(Class<T> clazz, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws Exception;
    
    <T> List<T> getEntitysToQuery(Class<T> clazz, String query, Object... values) throws Exception;
    
    List<T> getEntitys(Class<T> clazz, String atributo, Object valor,Object... atributos) throws Exception;
    
    List<T> getEntitys(Class<T> clazz,List<Filtro> filters,Object... atributos) throws Exception;
    
    List<T> getEntitys(Class<T> clazz,List<Filtro> filters,int qtdRegistro,String sortField, SortOrder sortOrder, Object... atributos) throws Exception;
    
}
