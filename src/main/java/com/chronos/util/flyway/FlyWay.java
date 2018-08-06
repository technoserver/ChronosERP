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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author john
 */
public class FlyWay {

    private DataSource ds;

    private Context envCtx;

    public void migration() throws NamingException, SQLException {

        initContext();

        ds = (DataSource) envCtx.lookup("jdbc/chronosLightDB");
        // Inicialição do FlyWay
        Flyway flyway = new Flyway();

        flyway.setBaselineOnMigrate(true);
        flyway.setTable("version");
        flyway.setDataSource(ds);

        flyway.setValidateOnMigrate(true);
        getTenants().forEach(t -> {
            flyway.setSchemas(t);
            flyway.migrate();
        });

    }


    public List<String> getTenants() throws SQLException, NamingException {

        List<String> tenants = new ArrayList<>();
        final Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {


            try (ResultSet result = statement.executeQuery("SELECT t.nome FROM tenant t WHERE t.ativo = TRUE ")) {
                while (result.next()) {
                    String str = result.getString("nome");
                    tenants.add(str);
                }
            }


        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            connection.close();
        }

        return tenants;
    }

    private Connection getConnection() throws SQLException, NamingException {
        DataSource ds = (DataSource) envCtx.lookup("jdbc/chronosAdminDB");
        return ds.getConnection();

    }

    private void initContext() throws NamingException {
        Context initCtx = new InitialContext();
        envCtx = (Context) initCtx.lookup("java:comp/env");
    }


}
