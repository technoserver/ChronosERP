/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.flyway;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGPoolingDataSource;

/**
 *
 * @author john
 */
public class FlyWay {

    public void migration() {
        // Criação do DataSource
        PGPoolingDataSource dataSource = new PGPoolingDataSource();
        dataSource.setUser("postgres");
        dataSource.setPassword("p@ssw0rd");
        dataSource.setDatabaseName("chronosLight");
        dataSource.setInitialConnections(10);
        dataSource.setPortNumber(5432);       
        dataSource.setServerName("localhost");
       

        // Inicialição do FlyWay
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setBaselineOnMigrate(true);
        flyway.setTable("version");
      //  flyway.setDataSource("jdbc:postgresql://localhost:5432/chronosEmissor", "postgres", "p@ssw0rd");
        flyway.setValidateOnMigrate(true);

        // executa Migração;
        flyway.migrate();
    }
}
