/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import java.io.IOException;
import java.util.Iterator;
import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

/**
 *
 * @author john
 */
public class JSFExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler handler;

    public JSFExceptionHandler(ExceptionHandler handler) {
        this.handler = handler;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.handler;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        //equanto ouver envento
        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            //captura a origem da exceção contextualizado
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            //retorna a exceção;            
            Throwable exception = context.getException();
            boolean tratarException = false;
            try {
                // verifica se é tipo viewexpired para podemos redirecionar para pagina de login
                if (exception instanceof ViewExpiredException) {
                    tratarException = true;
                    redirect("/login.xhtml");
                }else{
                    
                }
            } finally {
                if (tratarException) {
                    events.remove();
                }

            }
        }
        getWrapped().handle();
    }

    @Override
    public Throwable getRootCause(Throwable throwable) {
        while ((ELException.class.isInstance(throwable) || FacesException.class.isInstance(throwable)) && throwable.getCause() != null) {
            throwable = throwable.getCause();
        }

        return throwable;
    }

    private void redirect(String page) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            //captura o caminho da aplicação
            String contextPath = externalContext.getRequestContextPath();
            externalContext.redirect(contextPath + page);
            facesContext.responseComplete();
        } catch (IOException ex) {
            throw new FacesException(ex);
        }
    }

}
