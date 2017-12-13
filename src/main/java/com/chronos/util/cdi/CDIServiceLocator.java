/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Set;



/**
 *
 * @author john
 */
public class CDIServiceLocator {

    private static BeanManager getBeanManager() {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
        } catch (NamingException e) {
            throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");
        }
    }


    public static <T> T getBean(Class<T> clazz) {
        BeanManager bm = getBeanManager();
        Set<Bean<?>> beans = bm.getBeans(clazz);

        if (beans == null || beans.isEmpty()) {
            return null;
        }

        Bean<T> bean = (Bean<T>) beans.iterator().next();

        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        T o = (T) bm.getReference(bean, clazz, ctx);

        return o;
    }
}
