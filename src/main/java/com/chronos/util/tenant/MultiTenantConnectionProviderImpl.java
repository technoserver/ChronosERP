package com.chronos.util.tenant;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by john on 22/01/18.
 */
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

    private static final String DEFAULT_TENANT_ID = "public";
    private Logger log = Logger.getLogger(MultiTenantConnectionProviderImpl.class.getName());


    //private DriverManagerConnectionProviderImpl provider = new DriverManagerConnectionProviderImpl();

    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();

    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantid) throws SQLException {

        final Connection connection = getAnyConnection();
        StringBuilder sql = new StringBuilder();
        sql.append("SET search_path TO ");
        sql.append(tenantid != null ? tenantid : DEFAULT_TENANT_ID);
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql.toString());
        } catch (SQLException e) {
            throw new HibernateException("Problem setting schema to " + tenantid, e);
        } finally {

        }
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        //  return provider.isUnwrappableAs(aClass);
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        // return dataSource.unwrap(aClass);
        return null;
    }


    @Override
    public void injectServices(ServiceRegistryImplementor registry) {
//        Map settings = registry.getService(ConfigurationService.class).getSettings();
//        provider.configure(settings);
//        provider.injectServices(registry);


        try {
            final Context init = new InitialContext();

            Context envCtx = (Context) init.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/chronosLightDB");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
