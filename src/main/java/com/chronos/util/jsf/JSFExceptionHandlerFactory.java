/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 *
 * @author john
 */
public class JSFExceptionHandlerFactory extends ExceptionHandlerFactory{

    private ExceptionHandlerFactory parent;
    
    public JSFExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
    
    
    
    @Override
    public ExceptionHandler getExceptionHandler() {
        return new JSFExceptionHandler(parent.getExceptionHandler());
    }
    
    
    
}
