/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.chronos.util.jsf;

import com.chronos.util.UtilErros;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author john
 */
public class Mensagem {

    public static void addErrorMessage(String message,Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message+"\n"+UtilErros.getMensagemErro(e), message));
    }

    public static void addInfoMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
    }
    
    

    public static void addWarnMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
    }

    public static void addFatalMessage(String message,Exception e) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, message+"\n"+UtilErros.getMensagemErro(e), message));
    }
}
