package com.chronos.repository;

import com.chronos.dto.UsuarioDTO;
import com.chronos.modelo.entidades.Papel;
import com.chronos.modelo.entidades.PapelFuncao;
import com.chronos.modelo.entidades.Usuario;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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


        Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login");
        q.setParameter("login", login);
        List<Usuario> user = q.getResultList();

        return user.get(0);
        
        
    }

    public UsuarioDTO getUsuarioDTO(String login) {
        String jpql = "SELECT new com.chronos.dto.UsuarioDTO(u.id,u.login,u.senha,u.administrador,p.nome,c.foto34,c.id,cg.nome ,e) " +
                "FROM Usuario u ,IN(u.colaborador.pessoa.listaEmpresa) e " +
                "INNER JOIN u.colaborador c " +
                "INNER JOIN c.cargo cg " +
                "INNER JOIN c.pessoa p " +
                "WHERE u.login = :login";
        Query q = em.createQuery(jpql);
        q.setParameter("login", login);
        return (UsuarioDTO) q.getSingleResult();
    }

}
