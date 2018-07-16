/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import org.primefaces.model.SortOrder;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author john
 * @param <T>
 */
public interface Repository<T> {

    void clear() throws PersistenceException;

    void salvar(T bean) throws PersistenceException;

    void salvar(List<T> bean) throws PersistenceException;

    T atualizar(T bean) throws PersistenceException;


    <S extends T> S saveAndFlush(S var1);

    void atualizar(Class<T> clazz, List<Filtro> filtros, Map<String, Object> atributos) throws PersistenceException;

    void atualizarNamedQuery(String namedQuery, Object... values);

    boolean updateNativo(Class<T> clazz,List<Filtro> filtros,Map<String,Object> atributos);

    void excluir(Class<T> clazz) throws PersistenceException;

    void excluir(Class<T> clazz, String atributo, Object valor) throws PersistenceException;

    void excluir(Class<T> clazz, List<Filtro> filtros) throws PersistenceException;

    void excluir(T bean) throws PersistenceException;

    void excluir(T bean, Integer id) throws PersistenceException;

    boolean existeRegisro(Class<T> clazz, String atributo, Object valor) throws PersistenceException;

    Object getObject(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException;

    T get(Integer id, Class<T> clazz) throws PersistenceException;

    T get(Class<T> clazz, String atributo, Object valor) throws PersistenceException;

    <T> T getNamedQuery(Class<T> clazz, String namedQuery, Object... atributos) throws PersistenceException;

    Optional<T> getOptional(Class<T> clazz, String atributo, Object valor) throws PersistenceException;

    T get(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException;

    T get(Class<T> clazz, List<Filtro> filtros) throws PersistenceException;

    T get(Class<T> clazz, List<Filtro> filtros, Object[] atributos) throws PersistenceException;

    Long getTotalRegistros(Class<T> clazz, String atributo, Object valor) throws PersistenceException;

    Long getTotalRegistros(Class<T> clazz, List<Filtro> filters) throws PersistenceException;

    T getEntityJoinFetch(Integer id, Class<T> clazz) throws PersistenceException;


    T getJoinFetch(Integer id, Class<T> clazz) throws PersistenceException;

    List<T> getAll(Class<T> clazz) throws PersistenceException;

    <T> List<T> getEntitysToQuery(Class<T> clazz, String query, Object... values) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, String atributo, Object valor) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object[] atributos) throws PersistenceException;

    List<T> getEntitys(Class<T> clazz, String atributo, Object valor, Object[] atributos, Object[] joins) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz) throws PersistenceException;    
    
    List<T> getEntitys(Class<T> clazz,Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, Object[] atributos) throws PersistenceException;

    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, Object[] atributos,Object[] joinFetch) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters,int qtdRegistro) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters,int qtdRegistro,Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException;
    
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro,String sortField, SortOrder sortOrder) throws PersistenceException;
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int qtdRegistro,String sortField, SortOrder sortOrder, Object[] atributos) throws Exception;
    
  
    
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro,String sortField, SortOrder sortOrder) throws PersistenceException;

    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro,String sortField, SortOrder sortOrder, Object[] atributos) throws PersistenceException;
     
    List<T> getEntitys(Class<T> clazz, List<Filtro> filters, int first, int qtdRegistro, Object[] atributos) throws PersistenceException;

    List<T> getEntitys(Class<T> clazz,Class classToCast,List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder,Object[] joinfetch,Object[] atributos) throws Exception;
    
    List<T> getEntitys(Class<T> clazz,List<Filtro> filters, int first, int pageSize, String sortField, SortOrder sortOrder,Object[] joinfetch,Object[] atributos) throws Exception;


}
