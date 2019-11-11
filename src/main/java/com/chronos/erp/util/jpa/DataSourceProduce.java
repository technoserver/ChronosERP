package com.chronos.erp.util.jpa;


import javax.enterprise.context.ApplicationScoped;

/**
 * Created by john on 02/01/18.
 */
@ApplicationScoped
public class DataSourceProduce {

//    @Produces
//    @RequestScoped
//    @Default
//    public ComboPooledDataSource dataSource() {
//
//        ComboPooledDataSource dataSource = new ComboPooledDataSource("tenant");
//        try {
//            dataSource.setDriverClass("org.postgresql.Driver");
//        } catch (PropertyVetoException pve) {
//            System.out.println("Cannot load datasource driver (postgresql) : " + pve.getMessage());
//            return null;
//        }
//
//        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/chronosLight");
//        dataSource.setUser("aplicacao");
//        dataSource.setPassword("aplicacao");
//        dataSource.setMinPoolSize(1);
//        dataSource.setMaxPoolSize(20);
//        dataSource.setMaxIdleTime(300);
//
//
//        return dataSource;
//
//    }
}
