package com.chronos.erp.repository;

import com.chronos.erp.dto.UsuarioDTO;
import com.chronos.erp.modelo.entidades.Empresa;
import com.chronos.erp.modelo.entidades.Papel;
import com.chronos.erp.modelo.entidades.PapelFuncao;
import com.chronos.erp.modelo.entidades.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author john
 */
public class UsuarioRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public UsuarioRepository() {
    }

    public Usuario getUsuario(String nomeUsuario, String senhaUsuario) {
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

    public List<PapelFuncao> getPapelFuncao(Usuario usuario) {
        Query q = em.createQuery("SELECT p FROM PapelFuncao p join fetch p.papel join fetch p.funcao WHERE p.papel = :papel");
        q.setParameter("papel", usuario.getPapel());
        return (List<PapelFuncao>) q.getResultList();
    }

    public Papel getPapelFuncao(int idusuario) {
        String jpql = "select u.papel from Usuario u "
                + "INNER JOIN u.papel p "
                + "LEFT JOIN FETCH p.listaPapelFuncao "
                + "where u.id =:id";
        TypedQuery<Papel> q = em.createQuery(jpql, Papel.class);
        q.setParameter("id", idusuario);
        return q.getResultList().stream().findFirst().orElse(null);
    }

    public Usuario getUsuario(String login) {

        TypedQuery<Usuario> q = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login", Usuario.class);
        q.setParameter("login", login);

        return q.getResultList().stream().findFirst().orElse(null);


    }

    public UsuarioDTO getUsuarioDTO(String login) {
        TypedQuery<UsuarioDTO> q = em.createNamedQuery("Usuario.login", UsuarioDTO.class);
        q.setParameter("login", login);

        UsuarioDTO user = q.getSingleResult();
        definirEmpresaUsuario(user);
        return user;
    }

    private void definirEmpresaUsuario(UsuarioDTO user) {
        String jpql = "SELECT e from Empresa e LEFT JOIN FETCH e.listaEndereco where e.id = :id";
        TypedQuery<Empresa> q = em.createQuery(jpql, Empresa.class);
        q.setParameter("id", user.getIdempresa());
        Empresa emp = q.getSingleResult();
        user.setEmpresa(emp);
    }

}
