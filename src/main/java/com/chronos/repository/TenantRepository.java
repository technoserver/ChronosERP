package com.chronos.repository;


import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.modelo.entidades.tenant.UsuarioTenant;
import com.chronos.util.tenant.EntityManageProduceInject;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Created by john on 31/12/17.
 */
public class TenantRepository {


    @Inject
    @EntityManageProduceInject
    private EntityManager em;

    public Optional<UsuarioTenant> getUser(String nomeUsuario) {
        Query q = em.createQuery("SELECT u  FROM UsuarioTenant u  JOIN FETCH u.tenant WHERE u.login = :login and u.tenant.ativo = true ");
        q.setParameter("login", nomeUsuario);
        return q.getResultList().stream().findFirst();
    }

    public List<Tenant> getTenant() {
        Query q = em.createQuery("SELECT t  FROM Tenant t WHERE t.ativo = :ativo");
        q.setParameter("ativo", "S");
        return q.getResultList();
    }
}
