/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.controll;


import com.chronos.repository.Filtro;
import com.chronos.repository.Repository;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
    private List<Filtro> filtros;
    private List<T> objs;

    
    public ERPLazyDataModel() {
        filtros = new ArrayList<>();

    }

    @Override
    public T getRowData(String rowKey) {
        try {
            return dao.getJoinFetch(Integer.valueOf(rowKey), getClazz());

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
            objs = dao.getEntitys(getClazz(),filtros, first, pageSize, sortField, sortOrder );

            Long totalRegistros = dao.getTotalRegistros(getClazz(), filtros);
            this.setRowCount(totalRegistros.intValue());
            filtros.clear();
            return objs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs = new LinkedList<T>();
    }

    public void addFiltro(String atributo, Object valor) {
        if(valor!=null){
            addFiltro(atributo, valor, Filtro.LIKE);
        }
    }

    public void addFiltro(String atributo, Object valor,String operadorRelacional) {
        if (filtros == null) {
            filtros = new ArrayList<>();
        }
        if (valor.getClass() == String.class) {
            filtros.add(new Filtro(Filtro.AND, atributo, Filtro.LIKE, valor));
        } else {
            filtros.add(new Filtro(Filtro.AND, atributo, operadorRelacional, valor));
        }
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



    public List<T> getObjs() {
        return objs;
    }

    public void setObjs(List<T> objs) {
        this.objs = objs;
    }
    
    

}
