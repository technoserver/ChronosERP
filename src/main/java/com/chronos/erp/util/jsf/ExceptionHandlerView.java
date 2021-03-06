/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.erp.util.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.inject.Named;

/**
 * @author john
 */
@Named
@RequestScoped
public class ExceptionHandlerView {

    public void throwNullPointerException() {
        throw new NullPointerException();
    }

    public void throwWrappedIllegalStateException() {
        Throwable t = new IllegalStateException();
        throw new FacesException(t);
    }

    public void throwError() {
        throw new RuntimeException("throwing new error");
    }
}
