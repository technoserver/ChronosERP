package com.chronos.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by john on 02/01/18.
 */
@Configuration
@PropertySource("classpath:datasource.properties")
public class DataSourceProperty {

    private static final long serialVersionUID = 1L;

    @Value("${chronos.url}")
    private String url;
    @Value("${chronos.username}")
    private String username;
    @Value("${chronos.password}")
    private String password;
    @Value("${chronos.database}")
    private String database;
    @Value("${chronos.driver}")
    private String driver;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
