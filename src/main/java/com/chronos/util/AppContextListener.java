/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util;

import com.chronos.util.flyway.FlyWay;
import com.chronos.util.tenant.ConstantsTenant;
import com.chronos.util.validation.NotBlankClientValidationConstraint;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.validator.constraints.NotBlank;
import org.primefaces.validate.bean.BeanValidationMetadataMapper;

import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

/**
 * @author john
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.print("Destruindo o contexto");
        ConstantsTenant.FACTORIES.forEach((tenantName, entityManagerFactory) -> entityManagerFactory.close());
        ConstantsTenant.FACTORIES.clear();
        ConstantsTenant.TENANTS.clear();
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        FlyWay flyWay = new FlyWay();
        try {
            flyWay.migration();
            System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");

            BeanValidationMetadataMapper.registerConstraintMapping(NotBlank.class,
                    new NotBlankClientValidationConstraint());
        } catch (FlywayException | NamingException | SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erro ao executa procedimentos de migracao\n" + ex.getCause(), ex);
        }



    }
}
