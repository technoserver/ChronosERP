/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import com.chronos.util.flyway.FlyWay;
import javax.servlet.ServletContext;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

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
        FlyWay flyWay= new FlyWay();
        flyWay.migration();
    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
       
    }
    
    
    
}
