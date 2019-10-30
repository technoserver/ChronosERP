/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.util.jsf;

import com.chronos.erp.modelo.tenant.Tenant;
import com.chronos.erp.util.tenant.TenantInject;
import com.chronos.erp.util.tenant.TenantRegistry;
import com.chronos.erp.util.tenant.TenantRegistyInject;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * @author john
 */
public class FacesProducer {

    @Produces
    @RequestScoped
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Produces
    @RequestScoped
    public ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    @Produces
    @RequestScoped
    public HttpServletResponse getHttpServletResponse() {
        return ((HttpServletResponse) getExternalContext().getResponse());
    }


    @Produces
    @RequestScoped
    @TenantRegistyInject
    public TenantRegistry getTenantRegistry() {
        return new TenantRegistry();
    }

    @Produces
    @RequestScoped
    @TenantInject
    public Tenant create() {
        Tenant tenant = FacesUtil.getTenantId();
        return tenant;
    }


}
