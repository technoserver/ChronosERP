/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jpa;

import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.util.jsf.FacesUtil;
import com.chronos.util.tenant.EntityManageProduceInject;
import com.chronos.util.tenant.TenantRegistry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author john
 */
@ApplicationScoped
public class EntityManagerProducer {


    private static final Logger logger = LoggerFactory.getLogger(EntityManagerProducer.class);

    @Inject
    private Tenant tenant;

    @Inject
    private TenantRegistry tenantRegistry;

    private SessionFactory sessionFactory;

    private EntityManagerFactory factory;


    public EntityManagerProducer() {
        factory = Persistence.createEntityManagerFactory("ChronosUP");
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }


    @Produces
    @RequestScoped
    @Default
    public Session createEntityManager() {

        tenant = FacesUtil.getTenantId();
        logger.debug("executando conexao para o tenant " + tenant.getNome());
        final EntityManager entityManager = tenantRegistry.getEntityManagerFactory(tenant.getNome()).createEntityManager();
        return (Session) entityManager;
    }

    @Produces
    @RequestScoped
    @EntityManageProduceInject
    public Session createEntityManagerTenant() {
        logger.debug("executando conexao para base administrativa ");
        return (Session) factory.createEntityManager();
    }


    public void closeEntityManager(@Disposes EntityManager manager) {
        manager.close();

    }


}
