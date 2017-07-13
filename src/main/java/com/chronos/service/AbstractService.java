package com.chronos.service;

import com.chronos.repository.Repository;
import com.chronos.util.jpa.Transactional;

import javax.inject.Inject;

/**
 * Created by john on 11/07/17.
 */
public  class AbstractService<T> {

    @Inject
    private Repository<T> repositorys;



    @Transactional
    public void salvar(T obj){
       repositorys.salvar(obj);
    }

    @Transactional
    public T atualizar(T obj){
        return repositorys.atualizar(obj);
    }
    @Transactional
    public void excluir(T obj){
        repositorys.excluir(obj);
    }
    @Transactional
    public void excluir(T obj,int id){
        repositorys.excluir(obj,id);
    }

}
