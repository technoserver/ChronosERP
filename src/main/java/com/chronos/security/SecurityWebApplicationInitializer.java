/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.util.flyway.FlyWay;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.io.IOException;

/**
 *
 * @author john
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        FlyWay flyWay = new FlyWay();
        try {
            flyWay.migration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
     //  super.insertFilters(servletContext,new FilterUserInadiplente());
    }
    
    
    
}
