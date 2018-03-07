/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.flyway;

import com.chronos.modelo.entidades.tenant.Tenant;
import org.flywaydb.core.Flyway;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.util.List;

/**
 * @author john
 */
public class FlyWay {

    private DataSource ds;

    public void migration() throws Exception {

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");

        ds = (DataSource) envCtx.lookup("jdbc/chronosLightDB");




        // Inicialição do FlyWay
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setTable("version");
        flyway.setDataSource(ds);

        flyway.setValidateOnMigrate(true);

        // executa Migração;
//        for(Tenant t : tenants){
//         // flyway.setDataSource(prop.getProperty("chronos.url")+"?currentSchema="+t.getNome(), prop.getProperty("chronos.username"), prop.getProperty("chronos.password"));
//            flyway.setSchemas(t.getNome());
//            flyway.migrate();
//        }

    }


    public List<Tenant> getTenants() {
        EntityManager em;
        List tenants;
        try{

            EntityManagerFactory factory = Persistence.createEntityManagerFactory("ChronosAdminUP");
            em = factory.createEntityManager();
            em.getTransaction().begin();
            Query q = em.createQuery("SELECT t  FROM Tenant t WHERE t.ativo = :ativo");
            q.setParameter("ativo", true);
            tenants  =  q.getResultList();
            if (factory != null && factory.isOpen()) {
                factory.close();
            }

        }catch (Exception ex){
            throw ex;

        }
        return tenants;
    }


}
