/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;

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

    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
     //  super.insertFilters(servletContext,new FilterUserInadiplente());
    }
    
    
    
}
