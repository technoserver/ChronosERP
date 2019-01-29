package com.chronos.repository;


import com.chronos.modelo.tenant.Tenant;
import com.chronos.modelo.tenant.UsuarioTenant;
import com.chronos.modelo.view.ViewUsuarioTenant;
import com.chronos.util.jpa.Transactional;
import com.chronos.util.tenant.EntityManageProduceInject;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 31/12/17.
 */
public class TenantRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    @EntityManageProduceInject
    private EntityManager em;

    public Optional<ViewUsuarioTenant> getUser(String nomeUsuario) {

        Query q = em.createQuery("SELECT u  FROM ViewUsuarioTenant u WHERE u.login = :login");
        q.setMaxResults(1);
        q.setParameter("login", nomeUsuario);
        return q.getResultList().stream().findFirst();
    }

    public Optional<UsuarioTenant> getUserTenant(String nomeUsuario) {

        Query q = em.createQuery("SELECT u  FROM UsuarioTenant u WHERE u.login = :login");
        q.setMaxResults(1);
        q.setParameter("login", nomeUsuario);
        return q.getResultList().stream().findFirst();
    }

    public List<Tenant> getTenant() {
        Query q = em.createQuery("SELECT t  FROM Tenant t WHERE t.ativo = :ativo");
        q.setParameter("ativo", "S");
        return q.getResultList();
    }


    public boolean existeUsuario(String login) {
        String jpql = "SELECT COUNT(u.id) FROM UsuarioTenant u WHERE u.login = :login";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("login", login);
        return query.getSingleResult() > 0;
    }

    @Transactional
    public void salvar(UsuarioTenant usuario){
        EntityTransaction trx = em.getTransaction();
        em.persist(usuario);
        boolean criador = false;
        try{
            if (!trx.isActive()) {
                // truque para fazer rollback no que já passou
                // (senão, um futuro commit, confirmaria até mesmo operações sem transação)
                trx.begin();
                trx.rollback();

                // agora sim inicia a transação
                trx.begin();

                criador = true;
                if (usuario.getId() == null) {
                    em.persist(usuario);
                } else {
                    em.merge(usuario);
                }

            }
        }catch (Exception ex){

            if (trx != null && criador) {
                trx.rollback();
            }

            throw ex;
        } finally {
            if (trx != null && trx.isActive() && criador) {
                trx.commit();
            }
        }
    }

}
