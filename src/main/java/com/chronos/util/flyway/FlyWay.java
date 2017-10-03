/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.flyway;

import org.flywaydb.core.Flyway;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @author john
 */
public class FlyWay {

    public void migration() throws NamingException {

        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");

        DataSource ds = (DataSource) envCtx.lookup("jdbc/chronosLightDB");

        // Criação do DataSource
//        PGPoolingDataSource dataSource = new PGPoolingDataSource();
//        dataSource.setUser("postgres");
//        dataSource.setPassword("p@ssw0rd");
//        dataSource.setDatabaseName("chronosLight");
//        dataSource.setInitialConnections(10);
//        dataSource.setPortNumber(5432);
//        dataSource.setServerName("localhost");


        // Inicialição do FlyWay
        Flyway flyway = new Flyway();
        flyway.setDataSource(ds);
        flyway.setBaselineOnMigrate(true);
        flyway.setTable("version");
        //  flyway.setDataSource("jdbc:postgresql://localhost:5432/chronosEmissor", "postgres", "p@ssw0rd");
        flyway.setValidateOnMigrate(true);

        // executa Migração;
        flyway.migrate();
    }


}
