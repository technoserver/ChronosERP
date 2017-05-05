/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;


import com.chronos.repository.Repository;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author usuario
 * @param <T>
 */
public class ERPLazyDataModel<T> extends LazyDataModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Class<T> clazz;
    protected Repository<T> dao;
    private Map<String, Object> filtro;
    private List<T> objs;
    
    public ERPLazyDataModel() {
        filtro = new HashMap<>();

    }

    @Override
    public T getRowData(String rowKey) {
        try {
            return dao.getEntityJoinFetch(Integer.valueOf(rowKey), getClazz());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getRowKey(T object) {
        try {
            Method metodo = object.getClass().getDeclaredMethod("getId");
            return metodo.invoke(object);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        try {
            objs = dao.getEntityPagination(getClazz(), first, pageSize, sortField, sortOrder, filtro);

            Long totalRegistros = dao.getTotalRegistros(getClazz(), filtro);
            this.setRowCount(totalRegistros.intValue());
            return objs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs = new LinkedList<T>();
    }
    
    public void addFiltro(String campo, Object valor){
        this.filtro.put(campo, valor);
       
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Repository<T> getDao() {
        return dao;
    }

    public void setDao(Repository<T> dao) {
        this.dao = dao;
    }

    

    public Map<String, Object> getFiltro() {
        return filtro;
    }

    public void setFiltro(Map<String, Object> filtro) {
        this.filtro = filtro;
    }

    public List<T> getObjs() {
        return objs;
    }

    public void setObjs(List<T> objs) {
        this.objs = objs;
    }
    
    

}
