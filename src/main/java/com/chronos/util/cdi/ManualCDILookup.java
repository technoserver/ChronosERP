package com.chronos.util.cdi;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author john
 */
public abstract class ManualCDILookup {

    public <T> T getFacadeWithJNDI(Class<T> classToFind) {
        BeanManager bm = getBeanManager();
        Set<Bean<?>> beans = (Set<Bean<?>>) bm.getBeans(classToFind);
        if (beans == null || beans.isEmpty()) {
            return null;
        }
        Bean<T> bean = (Bean<T>) bm.getBeans(classToFind).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        T dao = (T) bm.getReference(bean, classToFind, ctx); // this could be inlined, but intentionally left this way
        return dao;
    }

    public BeanManager getBeanManager() {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/env/BeanManager");
        } catch (NamingException e) {
            throw new RuntimeException("Não pôde encontrar BeanManager no JNDI.");

        }
    }
}
