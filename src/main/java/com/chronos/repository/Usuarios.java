/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.repository;

import com.chronos.modelo.entidades.PapelFuncao;
import com.chronos.modelo.entidades.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

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

    public Optional<Usuario> getUser(String nomeUsuario) {
        Query q = em.createQuery("SELECT new Usuario(u.id)  FROM Usuario u WHERE u.login = :login");
        q.setParameter("login", nomeUsuario);       
        return q.getResultList().stream().findFirst();
    }

    public List<PapelFuncao> getPapelFuncao(Usuario usuario) throws Exception {
        Query q = em.createQuery("SELECT p FROM PapelFuncao p join fetch p.papel join fetch p.funcao WHERE p.papel = :papel");
        q.setParameter("papel", usuario.getPapel());
        return (List<PapelFuncao>) q.getResultList();
    }

    public List<PapelFuncao> getPapelFuncao(int idusuario) throws Exception {
        String jpql = "select pf from PapelFuncao pf , Usuario u "
                + "INNER JOIN FETCH pf.Funcao f "
                + "INNER JOIN FETCH pf.Papel p "
                + "where u.id = 1 and u.papel.id = p.id";
        Query q = em.createQuery(jpql);
        // q.setParameter("papel", usuario.getPapel());
        return (List<PapelFuncao>) q.getResultList();
    }

    public Usuario getUsuario(String login) throws Exception {
//        CriteriaBuilder build  = em.getCriteriaBuilder();
//        CriteriaQuery<Usuario> criteria = build.createQuery(Usuario.class);
//        Root<Usuario> usuario = criteria.from(Usuario.class);
//        Join<Usuario,Papel> papel = ((Join)usuario.fetch("papel"));
//        Join<Papel,PapelFuncao> papelFuncao = (Join)papel.fetch("listaPapelFuncao", JoinType.LEFT);
//
//        criteria.where(build.equal(usuario.get("login"), login));
//        TypedQuery<Usuario> query = em.createQuery(criteria);
//        List<Usuario> user =  query.getResultList();


        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login");
        q.setParameter("login", login);
        List<Usuario> user = q.getResultList();

        return user.get(0);
        
        
    }

}
