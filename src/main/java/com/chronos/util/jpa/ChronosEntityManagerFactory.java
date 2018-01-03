package com.chronos.util.jpa;

import com.chronos.util.tenant.TenantRegistry;
import org.hibernate.Session;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by john on 26/09/17.
 */
@WebListener
public class ChronosEntityManagerFactory implements ServletContextListener {

//    private static EntityManagerFactory factory;

    @Inject
    private TenantRegistry tenantRegistry;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
       // factory = Persistence.createEntityManagerFactory("ChronosUP");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        if (factory != null && factory.isOpen()) {
//            factory.close();
//        }
        tenantRegistry.shutdownTenants();
    }

//    public static Session createEntityManager() throws Exception {
//        if (factory == null || !factory.isOpen()) {
//            throw new Exception("Erro ao criar o Entity Manager.");
//        }
//        return (Session) factory.createEntityManager();
//    }

//    public static EntityManager createEntityManager() throws Exception {
//        if (factory == null || !factory.isOpen()) {
//            throw new Exception("Erro ao criar o Entity Manager.");
//        }
//        return factory.createEntityManager();
//    }
}
