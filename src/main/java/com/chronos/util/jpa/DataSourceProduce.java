package com.chronos.util.jpa;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import java.beans.PropertyVetoException;

/**
 * Created by john on 02/01/18.
 */
@ApplicationScoped
public class DataSourceProduce {

    @Produces
    @RequestScoped
    @Default
    public ComboPooledDataSource dataSource() {

        ComboPooledDataSource dataSource = new ComboPooledDataSource("tenant");
        try {
            dataSource.setDriverClass("org.postgresql.Driver");
        } catch (PropertyVetoException pve) {
            System.out.println("Cannot load datasource driver (postgresql) : " + pve.getMessage());
            return null;
        }

        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/chronosLight");
        dataSource.setUser("aplicacao");
        dataSource.setPassword("aplicacao");
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxIdleTime(300);


        return dataSource;

    }
}
