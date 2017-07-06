/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import com.chronos.util.validation.NotBlankClientValidationConstraint;
import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.validate.bean.BeanValidationMetadataMapper;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author john
 */
@WebListener
public class AppContextListener implements ServletContextListener {
    
    	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
		
		BeanValidationMetadataMapper.registerConstraintMapping(NotBlank.class, 
				new NotBlankClientValidationConstraint());
	}
}
