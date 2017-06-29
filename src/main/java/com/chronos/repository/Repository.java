/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import java.util.List;
import java.util.Map;
import javax.persistence.PersistenceException;
import org.primefaces.model.SortOrder;

/**
 *
 * @author john
 * @param <T>
 */
public interface Repository<T> {

    void salvar(T bean) throws PersistenceException;

    T atualizar(T bean) throws PersistenceException;

    void excluir(Class<T> clazz) throws PersistenceException;

    void excluir(T bean) throws PersistenceException;

    void excluir(T bean, Integer id) throws PersistenceException;

    T get(Integer id, Class<T> clazz) throws PersistenceException;
    
    T get(Integer id, Class<T> clazz,List<Filtro> filters) throws PersistenceException;
    
    T get(Integer id, Class<T> clazz,List<Filtro> filters, Object[] atributos) throws PersistenceException;
    
    T getJoinFetch(Integer id, Class<T> clazz) throws PersistenceException;

    List<T> getAll(Class<T> clazz) throws Exception;

    Long getTotalRegistros(Class<T> clazz, Map<String, Object> filters) throws PersistenceException;

    Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws PersistenceException;

    List<T> getEntitysPagination(Class<T> clazz, int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws PersistenceException;
    
    <T> List<T> getEntitysToQuery(Class<T> clazz, String query, Object... values) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, String atributo, Object valor) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz) throws PersistenceException;    
    
    List<T> getEntitys(Class<T> clazz,Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, Object[] atributos) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters,int qtdRegistro) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters,int qtdRegistro,Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro,String sortField, SortOrder sortOrder) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro,String sortField, SortOrder sortOrder, Object[] atributos) throws Exception;
    
  
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro,String sortField, SortOrder sortOrder) throws PersistenceException;

    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro,String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException;
     
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro, Object[] atributos) throws PersistenceException;
    
    
    
    List<T> getEntitys(Class<T> clazz,List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder,Object[] joinfetch,Object[] atributos) throws Exception;
    
}
