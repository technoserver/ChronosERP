/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import com.chronos.modelo.entidades.PapelFuncao;
import com.chronos.modelo.entidades.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author john
 */
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager em;

    public Usuarios() {
    }

    
    
    public Usuario getUsuario(String nomeUsuario, String senhaUsuario) throws Exception {
        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha");
        q.setParameter("login", nomeUsuario);
        q.setParameter("senha", senhaUsuario);
        return (Usuario) q.getSingleResult();
    }

    public List<PapelFuncao> getPapelFuncao(Usuario usuario) throws Exception {
        Query q = em.createQuery("SELECT p FROM PapelFuncao p join fetch p.papel join fetch p.funcao WHERE p.papel = :papel");
        q.setParameter("papel", usuario.getPapel());
        return (List<PapelFuncao>) q.getResultList();
    }

}
