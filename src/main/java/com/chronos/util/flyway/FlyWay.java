/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.flyway;

import com.chronos.modelo.entidades.tenant.Tenant;
import com.chronos.repository.TenantRepository;
import com.chronos.security.DataSourceProperty;
import com.chronos.util.cdi.CDIServiceLocator;
import com.chronos.util.jpa.ChronosEntityManagerFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author john
 */
public class FlyWay {

    private DataSource ds;

    public void migration() throws Exception {

//        Context initCtx = new InitialContext();
//        Context envCtx = (Context) initCtx.lookup("java:comp/env");
//
//        ds = (DataSource) envCtx.lookup("jdbc/chronosLightDB");


        List<Tenant> tenants = getTenants();
        Properties prop = new Properties();
        InputStream in = getClass().getResourceAsStream("/datasource.properties");
        prop.load(in);
        in.close();

        // Inicialição do FlyWay
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setTable("version");
      //  flyway.setDataSource(ds);
        flyway.setDataSource(prop.getProperty("chronos.url"), prop.getProperty("chronos.username"), prop.getProperty("chronos.password"));
        flyway.setValidateOnMigrate(true);

        // executa Migração;
        for(Tenant t : tenants){
         // flyway.setDataSource(prop.getProperty("chronos.url")+"?currentSchema="+t.getNome(), prop.getProperty("chronos.username"), prop.getProperty("chronos.password"));
            flyway.setSchemas(t.getNome());
            flyway.migrate();
        }

    }



    public List<Tenant> getTenants() throws Exception {
        EntityManager em = null;
        List<Tenant>  tenants ;
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

        }finally {

        }
        return tenants;
    }


}
